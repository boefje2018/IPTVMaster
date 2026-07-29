package com.iptv.master.tv

import android.graphics.Color
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.leanback.widget.ImageCardView
import androidx.leanback.widget.Presenter
import com.bumptech.glide.Glide
import com.iptv.master.R
import com.iptv.master.domain.model.Channel

class CardPresenter : Presenter() {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        val cardView = ImageCardView(parent.context).apply {
            isFocusable = true
            isFocusableInTouchMode = true
            setMainImageDimensions(200, 120)
            setBackgroundColor(ContextCompat.getColor(parent.context, R.color.cardview_dark_background))
            cardType = ImageCardView.CARD_TYPE_INFO_UNDER
            infoVisibility = ImageCardView.CARD_INFO_VISIBLE_WHEN_ACTIVE
        }
        return ViewHolder(cardView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, item: Any) {
        if (item !is Channel) return
        val cardView = viewHolder.view as ImageCardView
        cardView.titleText = item.name
        cardView.contentText = item.category

        if (!item.logoUrl.isNullOrBlank()) {
            Glide.with(cardView.context)
                .load(item.logoUrl)
                .placeholder(R.drawable.ic_tv)
                .error(R.drawable.ic_tv)
                .into(cardView.mainImageView)
        } else {
            cardView.setMainImage(ContextCompat.getDrawable(cardView.context, R.drawable.ic_tv))
        }
    }

    override fun onUnbindViewHolder(viewHolder: ViewHolder) {
        val cardView = viewHolder.view as ImageCardView
        cardView.mainImageView?.let {
            Glide.with(cardView.context).clear(it)
        }
    }
}
