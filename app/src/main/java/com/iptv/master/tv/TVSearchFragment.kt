package com.iptv.master.tv

import android.content.Intent
import android.os.Bundle
import androidx.leanback.app.SearchFragment
import androidx.leanback.widget.ArrayObjectAdapter
import androidx.leanback.widget.HeaderItem
import androidx.leanback.widget.ListRow
import androidx.leanback.widget.ListRowPresenter
import androidx.leanback.widget.ObjectAdapter
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.lifecycle.ViewModelProvider
import com.iptv.master.domain.model.Channel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TVSearchFragment : SearchFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: TVSearchViewModel
    private val rowsAdapter = ArrayObjectAdapter(ListRowPresenter())

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[TVSearchViewModel::class.java]

        adapter = rowsAdapter

        setSearchResultProvider(object : SearchFragment.SearchResultProvider {
            override fun onSearchQueryChanged(query: String): ObjectAdapter {
                viewModel.search(query)
                return rowsAdapter
            }

            override fun onSearchQuerySubmitted(query: String): ObjectAdapter {
                viewModel.search(query)
                return rowsAdapter
            }
        })

        onItemViewClickedListener = OnItemViewClickedListener { _, item, _, _ ->
            if (item is Channel) {
                val intent = Intent(requireContext(), TVPlayerActivity::class.java).apply {
                    putExtra("channelId", item.id)
                }
                startActivity(intent)
            }
        }

        observeResults()
    }

    private fun observeResults() {
        viewModel.searchResults.observe(viewLifecycleOwner) { results ->
            rowsAdapter.clear()
            if (results.isNotEmpty()) {
                val header = HeaderItem(0, "Search Results")
                val cardPresenter = CardPresenter()
                val rowAdapter = ArrayObjectAdapter(cardPresenter)
                rowAdapter.addAll(0, results)
                rowsAdapter.add(ListRow(header, rowAdapter))
            }
        }
    }
}
