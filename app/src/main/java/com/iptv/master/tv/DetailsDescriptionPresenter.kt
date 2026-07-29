package com.iptv.master.tv

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter
import com.iptv.master.domain.model.Channel

class DetailsDescriptionPresenter : AbstractDetailsDescriptionPresenter() {
    override fun onBindDescription(viewHolder: ViewHolder, item: Any) {
        if (item is Channel) {
            viewHolder.title.text = item.name
            viewHolder.subtitle.text = item.category
            viewHolder.body.text = "Country: ${item.country ?: "N/A"} | Language: ${item.language ?: "N/A"}"
        }
    }
}
