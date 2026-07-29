package com.iptv.master.tv

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.iptv.master.R
import com.iptv.master.domain.model.EPGProgram
import com.iptv.master.util.DateUtils

class EPGCardPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val cardView = ImageCardView(parent.context).apply {
            isFocusable = true
            isFocusableInTouchMode = true
            setMainImageDimensions(300, 80)
            cardType = ImageCardView.CARD_TYPE_INFO_UNDER
            infoVisibility = ImageCardView.CARD_INFO_VISIBLE_WHEN_ACTIVE
        }
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        if (item !is EPGProgram) return
        val cardView = viewHolder.view as ImageCardView
        cardView.titleText = item.title
        cardView.contentText = "${DateUtils.formatTime(item.startTime)} - ${DateUtils.formatTime(item.endTime)}"
        cardView.setMainImage(ContextCompat.getDrawable(cardView.context, R.drawable.ic_epg))
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {}
}
