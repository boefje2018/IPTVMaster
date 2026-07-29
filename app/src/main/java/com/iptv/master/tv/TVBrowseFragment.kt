package com.iptv.master.tv

import android.content.Intent
import android.os.Bundle
import androidx.leanback.app.BrowseFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.Presenter
import androidx.leanback.widget.Row
import androidx.leanback.widget.RowPresenter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import com.iptv.master.domain.model.Channel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TVBrowseFragment : BrowseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: TVBrowseViewModel
    private val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[TVBrowseViewModel::class.java]

        setupUI()
        observeData()
    }

    private fun setupUI() {
        adapter = rowsAdapter
        title = "IPTV Master"
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        brandColor = resources.getColor(android.R.color.holo_blue_dark, null)
        searchAffordanceColor = resources.getColor(android.R.color.white, null)

        onItemViewClickedListener = OnItemViewClickedListener { _, item, _, _ ->
            if (item is Channel) {
                val intent = Intent(requireContext(), TVPlayerActivity::class.java).apply {
                    putExtra("channelId", item.id)
                }
                startActivity(intent)
            }
        }
    }

    private fun observeData() {
        viewModel.channelsByCategory.observe(viewLifecycleOwner) { categoryMap ->
            rowsAdapter.clear()
            var index = 0
            categoryMap.forEach { (category, channels) ->
                val header = HeaderItem(index.toLong(), category)
                val cardPresenter = CardPresenter()
                val rowAdapter = ArrayObjectAdapter(cardPresenter)
                rowAdapter.addAll(0, channels)
                rowsAdapter.add(ListRow(header, rowAdapter))
                index++
            }
        }
    }
}
