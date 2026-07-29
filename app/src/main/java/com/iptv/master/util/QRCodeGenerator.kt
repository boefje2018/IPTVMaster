package com.iptv.master.util

import android.graphics.Bitmap
import android.graphics.Color

object QRCodeGenerator {

    fun generate(
        text: String,
        size: Int = 512,
        foregroundColor: Int = Color.BLACK,
        backgroundColor: Int = Color.WHITE
    ): Bitmap? {
        if (text.isBlank()) return null
        return try {
            val qrBits = generateQRCode(text)
            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
            val pixels = IntArray(size * size)
            val moduleCount = qrBits.size
            val moduleSize = size.toFloat() / moduleCount.toFloat()

            for (y in 0 until size) {
                for (x in 0 until size) {
                    val moduleX = (x.toFloat() / moduleSize).toInt().coerceIn(0, moduleCount - 1)
                    val moduleY = (y.toFloat() / moduleSize).toInt().coerceIn(0, moduleCount - 1)
                    pixels[y * size + x] = if (qrBits[moduleY][moduleX]) {
                        foregroundColor
                    } else {
                        backgroundColor
                    }
                }
            }

            bitmap.setPixels(pixels, 0, size, 0, 0, size, size)
            bitmap
        } catch (_: Exception) {
            null
        }
    }

    private fun generateQRCode(text: String): Array<BooleanArray> {
        val byteData = text.toByteArray(Charsets.UTF_8)
        val dataBits = byteData.flatMap { byte ->
            (7 downTo 0).map { bit -> ((byte.toInt() shr bit) and 1) == 1 }
        }.toMutableList()

        val terminatorBits = minOf(4, (8 - (dataBits.size % 8)) % 8)
        repeat(terminatorBits) { dataBits.add(false) }
        while (dataBits.size % 8 != 0) { dataBits.add(false) }

        val dataCodewords = dataBits.chunked(8).map { chunk ->
            chunk.fold(0) { acc, b -> (acc shl 1) or (if (b) 1 else 0) }
        }

        val totalCodewords = 26
        while (dataCodewords.size < totalCodewords) {
            dataCodewords.add(0xEC.toByte().toInt() and 0xFF)
            if (dataCodewords.size < totalCodewords) {
                dataCodewords.add(0x11.toByte().toInt() and 0xFF)
            }
        }

        val version = 1
        val moduleCount = 17 + 4 * version
        val matrix = Array(moduleCount) { BooleanArray(moduleCount) }

        drawFinderPatterns(matrix, moduleCount)
        drawTimingPatterns(matrix, moduleCount)
        drawData(matrix, moduleCount, dataCodewords)
        applyMask(matrix, moduleCount)

        return matrix
    }

    private fun drawFinderPatterns(matrix: Array<BooleanArray>, size: Int) {
        val positions = listOf(0 to 0, 0 to size - 7, size - 7 to 0)
        for ((row, col) in positions) {
            for (r in 0 until 7) {
                for (c in 0 until 7) {
                    val isOuter = r == 0 || r == 6 || c == 0 || c == 6
                    val isInner = r in 2..4 && c in 2..4
                    matrix[row + r][col + c] = isOuter || isInner
                }
            }
        }
    }

    private fun drawTimingPatterns(matrix: Array<BooleanArray>, size: Int) {
        for (i in 6 until size - 6) {
            matrix[6][i] = i % 2 == 0
            matrix[i][6] = i % 2 == 0
        }
    }

    private fun drawData(matrix: Array<BooleanArray>, size: Int, data: List<Int>) {
        var col = size - 1
        var row = size - 1
        var bitIndex = 0
        var dataIndex = 0
        var directionUp = true

        while (col > 0 && dataIndex < data.size) {
            if (col == 6) col--
            for (i in 0 until size) {
                val r = if (directionUp) size - 1 - i else i
                for (c in col downTo col - 1) {
                    if (r < 0 || r >= size || c < 0 || c >= size) continue
                    if (isReservedModule(r, c, size)) continue
                    if (bitIndex >= 8) {
                        bitIndex = 0
                        dataIndex++
                    }
                    if (dataIndex < data.size) {
                        matrix[r][c] = ((data[dataIndex] shr (7 - bitIndex)) and 1) == 1
                        bitIndex++
                    }
                }
            }
            directionUp = !directionUp
            col -= 2
        }
    }

    private fun applyMask(matrix: Array<BooleanArray>, size: Int) {
        for (r in 0 until size) {
            for (c in 0 until size) {
                if (isReservedModule(r, c, size)) continue
                val maskCondition = (r + c) % 3 == 1
                if (maskCondition) {
                    matrix[r][c] = !matrix[r][c]
                }
            }
        }
    }

    private fun isReservedModule(row: Int, col: Int, size: Int): Boolean {
        // Finder patterns
        if (row < 7 && col < 7) return true
        if (row < 7 && col >= size - 7) return true
        if (row >= size - 7 && col < 7) return true
        // Timing patterns
        if (row == 6 || col == 6) return true
        // Format info area
        if (row < 8 && col == size - 8) return true
        if (row == size - 8 && col < 8) return true
        if (row < 8 && col == 8) return true
        if (row == 8 && col < 8) return true
        return false
    }
}
