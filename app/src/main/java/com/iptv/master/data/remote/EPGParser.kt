package com.iptv.master.data.remote

import com.iptv.master.domain.model.EPGProgram
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import java.util.UUID
import javax.xml.parsers.DocumentBuilderFactory

object EPGParser {

    fun parse(xmlContent: String): Map<String, List<EPGProgram>> {
        val programs = mutableMapOf<String, MutableList<EPGProgram>>()
        try {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val xml = xmlContent.trimStart()
            val document = builder.parse(xml.byteInputStream())
            val programmeNodes = document.getElementsByTagName("programme")
            val dateFormats = listOf(
                SimpleDateFormat("yyyyMMddHHmmss Z", Locale.US),
                SimpleDateFormat("yyyyMMddHHmmss", Locale.US)
            )
            dateFormats.forEach { it.timeZone = TimeZone.getTimeZone("UTC") }

            for (i in 0 until programmeNodes.length) {
                val node = programmeNodes.item(i)
                val element = node as? org.w3c.dom.Element ?: continue
                val channelId = element.getAttribute("channel")
                val startAttr = element.getAttribute("start")
                val stopAttr = element.getAttribute("stop")
                val title = getTextContent(element, "title") ?: continue
                val description = getTextContent(element, "desc")
                val category = getTextContent(element, "category")
                val icon = getIconUrl(element)
                val rating = getRating(element)

                val startTime = parseDate(startAttr, dateFormats)
                val endTime = parseDate(stopAttr, dateFormats)
                if (startTime == null || endTime == null) continue

                val program = EPGProgram(
                    id = UUID.randomUUID().toString(),
                    channelId = channelId,
                    title = title,
                    description = description,
                    startTime = startTime,
                    endTime = endTime,
                    category = category,
                    icon = icon,
                    rating = rating,
                    isLive = false
                )
                programs.getOrPut(channelId) { mutableListOf() }.add(program)
            }
        } catch (e: Exception) {
            return emptyMap()
        }
        return programs
    }

    private fun getTextContent(element: org.w3c.dom.Element, tagName: String): String? {
        val list = element.getElementsByTagName(tagName)
        return if (list.length > 0) list.item(0).textContent else null
    }

    private fun getIconUrl(element: org.w3c.dom.Element): String? {
        val list = element.getElementsByTagName("icon")
        return if (list.length > 0) {
            (list.item(0) as? org.w3c.dom.Element)?.getAttribute("src")
        } else null
    }

    private fun getRating(element: org.w3c.dom.Element): String? {
        val list = element.getElementsByTagName("rating")
        if (list.length > 0) {
            val ratingElement = list.item(0) as? org.w3c.dom.Element ?: return null
            return getTextContent(ratingElement, "value")
        }
        return null
    }

    private fun parseDate(dateStr: String, formats: List<SimpleDateFormat>): Long? {
        for (format in formats) {
            try {
                return format.parse(dateStr.trim())?.time
            } catch (_: Exception) {}
        }
        return null
    }
}
