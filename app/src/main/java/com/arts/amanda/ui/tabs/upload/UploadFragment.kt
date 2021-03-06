package com.arts.amanda.ui.tabs.upload

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.arts.amanda.R
import com.arts.amanda.data.Arts
import com.arts.amanda.databinding.FragmentUploadBinding
import com.arts.amanda.utils.snack
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.io.File
import java.util.*


@AndroidEntryPoint
class UploadFragment : Fragment(R.layout.fragment_upload) {

    private var fragmentUploadBinding: FragmentUploadBinding? = null
    private val _fragmentUploadBinding get() = fragmentUploadBinding!!
    private val uploadViewModel: UploadViewModel by viewModels()
    private var imageUrl: String? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentUploadBinding = FragmentUploadBinding.bind(view)
        _fragmentUploadBinding.date.inputType = InputType.TYPE_NULL
        registerObserver()
        loginObserver()


        _fragmentUploadBinding.uploadImage.setOnClickListener {
            ImagePicker.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        _fragmentUploadBinding.uploadArt.setOnClickListener {
            val arts = Arts(
                image = imageUrl,
                title = _fragmentUploadBinding.title.text.toString(),
                description = _fragmentUploadBinding.description.text.toString(),
                date = _fragmentUploadBinding.date.text.toString(),
            )
            uploadViewModel.uploadArts(arts)
        }

        _fragmentUploadBinding.date.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(this.requireContext(), { _, years, months, dayOfMonth ->
                val date = "$dayOfMonth/$months/$years"
                _fragmentUploadBinding.date.setText(date)
            }, year, month, day)
        datePickerDialog.show()

        datePickerDialog.setOnDismissListener {
            it.dismiss()
        }
    }

    private fun loginObserver() {
        lifecycleScope.launchWhenStarted {
            uploadViewModel.stateUploadState.collect {
                when (it) {

                    is UploadState.Success -> {
                        checkIfValid(it.isSuccess)
                    }

                    is UploadState.Error -> {
                        snack(it.error)
                    }

                    is UploadState.Initial -> {
                        // NOTHING
                    }
                }
            }
        }
    }

    private fun registerObserver() {
        uploadViewModel.imageUrl.observe(viewLifecycleOwner) {
            if (it == null) {
                return@observe
            }
            imageUrl = it
        }
    }

    private fun checkIfValid(isValid: Boolean) {
        if (isValid) {
            findNavController().navigate(R.id.action_uploadFragment_to_feedFragment)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                val filePath = ImagePicker.getFilePath(data)!!
                val finalFile = Uri.fromFile(File(filePath))
                _fragmentUploadBinding.uploadImage.setImageURI(finalFile)
                uploadViewModel.uploadImage(finalFile!!)
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this.context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this.context, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragmentUploadBinding = null
    }
}