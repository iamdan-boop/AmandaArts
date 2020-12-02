package com.arts.amanda.ui.tabs.feed

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.PagedList
import com.arts.amanda.R
import com.arts.amanda.data.Arts
import com.arts.amanda.databinding.ActivityFeedHolderBinding
import com.arts.amanda.databinding.FragmentFeedBinding
import com.arts.amanda.service.FirebaseService
import com.arts.amanda.utils.snack
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class FeedFragment : Fragment(R.layout.fragment_feed) {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FirestorePagingAdapter<Arts, FeedHolder>
    private val feedViewModel: FeedViewModel by viewModels()

    @Inject
    lateinit var serviceData: FirebaseService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFeedBinding.bind(view)
        setAdapter()

        binding.swipeRefresh.setOnRefreshListener {
            adapter.refresh()
        }
    }

    private fun setAdapter() {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(20)
            .setPageSize(10)
            .build()

        val options = FirestorePagingOptions.Builder<Arts>()
            .setLifecycleOwner(viewLifecycleOwner)
            .setQuery(serviceData.getQueries(), config, Arts::class.java)
            .build()


        adapter = object: FirestorePagingAdapter<Arts, FeedHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder {
                val view = ActivityFeedHolderBinding.inflate(layoutInflater, parent, false)
                return FeedHolder(view)
            }

            override fun onBindViewHolder(holder: FeedHolder, position: Int, model: Arts) {
                holder.bind(model)
                holder.itemView.setOnLongClickListener {
                    feedViewModel.deleteArt(model.title!!)
                    adapter.refresh()
                    snack("Data Has Been Deleted")
                    return@setOnLongClickListener true
                }
            }

            override fun onError(e: Exception) {
                super.onError(e)
                println("Adapter Error: $e")
                binding.swipeRefresh.isRefreshing = false
            }

            override fun onLoadingStateChanged(state: LoadingState) {
                when (state) {
                    LoadingState.ERROR -> binding.swipeRefresh.isRefreshing = false
                    LoadingState.LOADING_INITIAL -> binding.swipeRefresh.isRefreshing = true
                    LoadingState.LOADED -> binding.swipeRefresh.isRefreshing = false
                    LoadingState.FINISHED -> binding.swipeRefresh.isRefreshing = false
                    LoadingState.LOADING_MORE -> binding.swipeRefresh.isRefreshing = true
                }
            }
        }

        binding.listArts.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter.stopListening()
        _binding = null
    }

    override fun onPause() {
        super.onPause()
        adapter.stopListening()
    }

    override fun onResume() {
        super.onResume()
        adapter.startListening()
    }
}