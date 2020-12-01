package com.arts.amanda.ui.tabs.upload

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.arts.amanda.R
import com.arts.amanda.databinding.FragmentUploadBinding
import com.arts.amanda.utils.snack
import com.arts.data.Arts
import com.arts.data.DataState
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UploadFragment : Fragment(R.layout.fragment_upload) {

    private var fragmentUploadBinding: FragmentUploadBinding? = null
    private val _fragmentUploadBinding get() = fragmentUploadBinding!!
    private val viewModel: UploadViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentUploadBinding = FragmentUploadBinding.bind(view)
        registerObserver()

        viewModel.setViewState(ArtsStateEvent.GetArtsEvent)

        _fragmentUploadBinding.uploadImage.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }
    }

    private fun registerObserver() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            when (dataState) {
                is DataState.Success<List<Arts>> -> {
                    println(dataState.data.toString())
                    println("DATA RECEIVE")
                }

                is DataState.Error -> {
                    println(dataState.error)
                }

                is DataState.Loading -> {
                    println("DATA IS LOADING")
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                val fileUri = data?.data
                _fragmentUploadBinding.uploadImage.setImageURI(fileUri)
                snack(getString(R.string.image_import))
            }
            ImagePicker.RESULT_ERROR -> {
                snack(ImagePicker.getError(data))
            }
            else -> {
                snack("Operation Cancelled")
            }
        }
    }
}