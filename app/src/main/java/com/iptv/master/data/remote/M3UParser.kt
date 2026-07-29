package com.iptv.master.data.remote

import com.iptv.master.domain.model.Channel
import com.iptv.master.util.ContentTypeDetector

object M3UParser {

    fun parse(content: String, playlistId: String): List<Channel> {
        val channels = mutableListOf<Channel>()
        val lines = content.lines().filter { it.isNotBlank() }
        var index = 0
        var channelIndex = 0

        while (index < lines.size) {
            val line = lines[index].trim()
            if (line.startsWith("#EXTINF:")) {
                val params = parseExtInfAttributes(line)
                val displayName = line.substringAfterLast(",").trim()
                val name = params["tvg-name"] ?: displayName
                val groupTitle = params["group-title"] ?: "General"

                val streamUrl = if (index + 1 < lines.size && !lines[index + 1].trim().startsWith("#")) {
                    lines[index + 1].trim()
                } else {
                    index++
                    continue
                }

                val contentType = ContentTypeDetector.detect(groupTitle, streamUrl, name)

                channels.add(
                    Channel(
                        id = "${playlistId}_${channelIndex}",
                        name = name,
                        logoUrl = params["tvg-logo"],
                        streamUrl = streamUrl,
                        category = when (contentType) {
                            com.iptv.master.domain.model.ContentType.LIVE -> "Live TV"
                            com.iptv.master.domain.model.ContentType.MOVIE -> "Movies"
                            com.iptv.master.domain.model.ContentType.SERIES -> "Series"
                            else -> groupTitle.ifEmpty { "General" }
                        },
                        groupTitle = groupTitle,
                        tvgId = params["tvg-id"],
                        tvgName = params["tvg-name"],
                        tvgLogo = params["tvg-logo"],
                        contentType = contentType
                    )
                )
                channelIndex++
                index += 2
            } else if (line.startsWith("#EXTM3U")) {
                index++
            } else {
                index++
            }
        }
        return channels
    }

    private fun parseExtInfAttributes(line: String): Map<String, String> {
        val params = mutableMapOf<String, String>()
        val attrPattern = Regex("""(\w+)=["']([^"']*)["']""")
        val matches = attrPattern.findAll(line)
        for (match in matches) {
            params[match.groupValues[1]] = match.groupValues[2]
        }
        return params
    }
}
