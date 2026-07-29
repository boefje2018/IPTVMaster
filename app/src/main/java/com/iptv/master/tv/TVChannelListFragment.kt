package com.iptv.master.tv

import android.content.Intent
import android.os.Bundle
import androidx.leanback.app.VerticalGridFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.leanback.widget.VerticalGridPresenter
import androidx.lifecycle.ViewModelProvider
import com.iptv.master.domain.model.Channel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TVChannelListFragment : VerticalGridFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: TVBrowseViewModel
    private val adapter = ArrayObjectAdapter(CardPresenter())

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[TVBrowseViewModel::class.java]

        setupGrid()
        observeData()
    }

    private fun setupGrid() {
        val gridPresenter = VerticalGridPresenter().apply {
            numberOfColumns = 5
            isFocusOutEnabled = true
        }
        setGridPresenter(gridPresenter)
        adapter = this.adapter

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
        viewModel.allChannels.observe(viewLifecycleOwner) { channels ->
            adapter.clear()
            adapter.addAll(0, channels)
        }
    }
}
