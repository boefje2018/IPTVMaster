package com.iptv.master.util

import com.iptv.master.domain.model.Channel

object M3ULinkParser {

    private val extInfRegex = Regex("""#EXTINF:(-?\d+)(?:\s+(.*))?,(.*)""")
    private val attrRegex = Regex("""(\w+)=["']([^"']*)["']""")

    data class ExtInfData(
        val duration: String,
        val attributes: Map<String, String>,
        val name: String
    )

    fun parse(content: String, playlistId: String = "default"): List<Channel> {
        val channels = mutableListOf<Channel>()
        val lines = content.lines().filter { it.isNotBlank() }
        var i = 0

        while (i < lines.size) {
            val line = lines[i].trim()

            if (line.startsWith("#EXTINF:")) {
                val extInf = parseExtInfLine(line) ?: run { i++; continue }
                var streamUrl = ""

                if (i + 1 < lines.size) {
                    val nextLine = lines[i + 1].trim()
                    if (!nextLine.startsWith("#")) {
                        streamUrl = nextLine
                    } else if (i + 2 < lines.size) {
                        val nextNextLine = lines[i + 2].trim()
                        if (!nextNextLine.startsWith("#")) {
                            streamUrl = nextNextLine
                        }
                    }
                }

                if (streamUrl.isNotBlank() && (streamUrl.startsWith("http") || streamUrl.startsWith("rtmp") || streamUrl.startsWith("rtsp"))) {
                    val channelIndex = channels.size
                    val attrs = extInf.attributes
                    channels.add(
                        Channel(
                            id = "${playlistId}_$channelIndex",
                            name = extInf.name,
                            logoUrl = attrs["tvg-logo"] ?: attrs["logo"],
                            streamUrl = streamUrl,
                            category = attrs["group-title"] ?: "General",
                            groupTitle = attrs["group-title"] ?: "General",
                            tvgId = attrs["tvg-id"],
                            tvgName = attrs["tvg-name"],
                            tvgLogo = attrs["tvg-logo"]
                        )
                    )
                }
                i += 2
            } else if (line.startsWith("#EXTGRP:")) {
                i++
            } else if (line.startsWith("#")) {
                i++
            } else if (line.isNotBlank() && line.startsWith("http")) {
                // Handle loose URLs without EXTINF
                channels.add(
                    Channel(
                        id = "${playlistId}_${channels.size}",
                        name = "Channel ${channels.size + 1}",
                        streamUrl = line,
                        category = "General",
                        groupTitle = "General"
                    )
                )
                i++
            } else {
                i++
            }
        }

        return channels
    }

    fun parseExtInfLine(line: String): ExtInfData? {
        val match = extInfRegex.find(line) ?: return null
        val duration = match.groupValues[1]
        val rawAttrs = match.groupValues[2]
        val name = match.groupValues[3]

        val attributes = mutableMapOf<String, String>()
        if (rawAttrs.isNotBlank()) {
            val attrMatches = attrRegex.findAll(rawAttrs)
            for (attrMatch in attrMatches) {
                attributes[attrMatch.groupValues[1]] = attrMatch.groupValues[2]
            }
        }

        return ExtInfData(duration, attributes, name)
    }

    fun isValidM3U(content: String): Boolean {
        return content.trimStart().startsWith("#EXTM3U")
    }

    fun extractUrls(content: String): List<String> {
        val urls = mutableListOf<String>()
        val lines = content.lines()
        for (line in lines) {
            val trimmed = line.trim()
            if (trimmed.startsWith("http") || trimmed.startsWith("rtmp") || trimmed.startsWith("rtsp")) {
                urls.add(trimmed)
            }
        }
        return urls
    }
}
