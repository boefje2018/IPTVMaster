package com.iptv.master.tv

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.leanback.app.DetailsFragment
import androidx.leanback.widget.Action
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.ClassPresenterSelector
import androidx.leanback.widget.DetailsOverviewRow
import androidx.leanback.widget.FullWidthDetailsOverviewRowPresenter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.SparseArrayObjectAdapter
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.iptv.master.domain.model.Channel
import com.iptv.master.player.PlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TVDetailsFragment : DetailsFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: PlayerViewModel
    private lateinit var channel: Channel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[PlayerViewModel::class.java]

        channel = arguments?.getParcelable("channel") ?: return

        viewModel.loadChannel(channel.id)

        val presenterSelector = ClassPresenterSelector()
        val detailsPresenter = FullWidthDetailsOverviewRowPresenter(DetailsDescriptionPresenter())
        presenterSelector.addClassPresenter(DetailsOverviewRow::class.java, detailsPresenter)
        presenterSelector.addClassPresenter(ListRow::class.java, ListRowPresenter())

        val adapter = ArrayObjectAdapter(presenterSelector)
        val detailsRow = DetailsOverviewRow(channel)

        val cardTarget = object : CustomTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                detailsRow.imageDrawable = resource
                adapter.notifyArrayItemRangeChanged(0, 1)
            }
            override fun onLoadCleared(placeholder: Drawable?) {}
        }

        if (!channel.logoUrl.isNullOrBlank()) {
            Glide.with(this)
                .load(channel.logoUrl)
                .into(cardTarget)
        }

        val actionsAdapter = SparseArrayObjectAdapter()
        actionsAdapter.set(0, Action(0, "Watch Now", "Start playing this channel"))
        actionsAdapter.set(1, Action(1, "Add to Favorites", ""))
        detailsRow.actionsAdapter = actionsAdapter

        adapter.add(detailsRow)

        val epgHeader = HeaderItem(1, "Program Schedule")
        val epgAdapter = ArrayObjectAdapter(EPGCardPresenter())
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            epgAdapter.clear()
            state.currentProgram?.let { epgAdapter.add(it) }
            state.nextProgram?.let { epgAdapter.add(it) }
        }
        adapter.add(ListRow(epgHeader, epgAdapter))

        this.adapter = adapter

        onItemViewClickedListener = OnItemViewClickedListener { _, item, action, _ ->
            when (action?.id) {
                0L -> {
                    val intent = Intent(requireContext(), TVPlayerActivity::class.java).apply {
                        putExtra("channelId", channel.id)
                    }
                    startActivity(intent)
                }
                1L -> {
                    viewModel.loadChannel(channel.id)
                }
            }
        }
    }
}
