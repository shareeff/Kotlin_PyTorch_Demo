package com.example.kotlinpytorchdemo

import android.app.Activity.RESULT_CANCELED
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.kotlinpytorchdemo.databinding.HomeFragmentBinding
import java.io.IOException


const val GALLERY = 1
const val CAMERA = 2

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: HomeFragmentBinding
    private lateinit var imageClassificationModelExecutor: ImageClassificationModelExecutor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        //return inflater.inflate(R.layout.home_fragment, container, false)

        binding.selectImageButton.setOnClickListener{
            choosePhotoFromGallery()
        }
        binding.captureImageButton.setOnClickListener {
            onCamera()
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        // Set the viewmodel for databinding - this allows the bound layout access to all of the
        // data in the VieWModel
        binding.homeViewModel = viewModel
        // Specify the current activity as the lifecycle owner of the binding. This is used so that
        // the binding can observe LiveData updates
        binding.lifecycleOwner = this
        binding.resultRecyclerView.adapter = DisplayResultAdapter()
        imageClassificationModelExecutor = ImageClassificationModelExecutor(requireContext())
        viewModel.rawImage.observe(viewLifecycleOwner, Observer {
                viewImage ->
            //Log.wtf("INFO", "@@@@@@@@@@@@@@@@@@@ImageView Changed@@@@@@@@@@@@@@@@@@@@")
            setImageView(binding.displayImage, viewImage)
            viewModel.onExecuteModel(imageClassificationModelExecutor)


        }
        )




    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Log.wtf("OnActivityResult", "&&&&&&&&&&&&&Calling from on Activity Result &&&&&&&&&&&&&")
        if (resultCode == RESULT_CANCELED) {
            return
        }
        when(requestCode){
            GALLERY -> {
                if (data != null) {
                    val contentURI = data.data
                    try {
                        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                            val image =
                                MediaStore.Images.Media.getBitmap(activity?.contentResolver, contentURI)
                            viewModel.setRawImage(image)
                            Toast.makeText(activity, "Image Saved!", Toast.LENGTH_SHORT).show()
                        } else {
                            contentURI?.let {
                                val source = ImageDecoder.createSource(activity!!.contentResolver, it)
                                val image = ImageDecoder.decodeBitmap(source)
                                viewModel.setRawImage(image)
                                Toast.makeText(activity, "Image Saved!", Toast.LENGTH_SHORT)
                                    .show()

                            }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(activity, "Failed!", Toast.LENGTH_SHORT).show()
                    }}


            }
            CAMERA -> {
                val image =  data?.extras?.get("data") as Bitmap
                viewModel.setRawImage(image)


            }
        }


    }

    private fun onCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)

    }
    private fun choosePhotoFromGallery() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        //activity?.startActivityForResult(galleryIntent, GALLERY)
        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun setImageView(imageView: ImageView, image: Bitmap) {
        Glide.with(requireContext())
            .load(image)
            .override(1024, 1024)
            .fitCenter()
            .into(imageView)
    }

}
