package com.iptv.master.domain.usecase

import com.iptv.master.domain.model.Channel
import com.iptv.master.util.ContentTypeDetector
import java.util.UUID
import javax.inject.Inject

class ImportM3UUseCase @Inject constructor() {

    operator fun invoke(content: String): List<Channel> {
        val channels = mutableListOf<Channel>()
        val lines = content.lines().filter { it.isNotBlank() }
        var index = 0
        while (index < lines.size) {
            val line = lines[index].trim()
            if (line.startsWith("#EXTINF:")) {
                val params = parseExtInf(line)
                val name = params["name"] ?: "Unknown"
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
                        id = params["tvg-id"] ?: UUID.randomUUID().toString(),
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
                index += 2
            } else {
                index++
            }
        }
        return channels
    }

    private fun parseExtInf(line: String): Map<String, String> {
        val params = mutableMapOf<String, String>()
        val afterColon = line.substringAfter("#EXTINF:").substringAfter(",")
        val namePart = afterColon.substringBefore(",")
        val name = if (afterColon.contains(",")) afterColon.substringAfter(",") else namePart
        params["name"] = name.trim()

        val attrPattern = Regex("""(\w+)=["']([^"']*)["']""")
        val matches = attrPattern.findAll(line)
        for (match in matches) {
            params[match.groupValues[1]] = match.groupValues[2]
        }
        return params
    }
}
