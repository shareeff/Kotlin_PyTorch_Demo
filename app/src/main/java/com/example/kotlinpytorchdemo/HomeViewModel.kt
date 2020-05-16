package com.example.kotlinpytorchdemo



import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class HomeViewModel :ViewModel()  {


    private val _rawImage = MutableLiveData<Bitmap>()

    private val _classificationResults = MutableLiveData<List<ClassificationResult>>()

    val classificationResults: LiveData<List<ClassificationResult>>
        get() = _classificationResults

    val rawImage: LiveData<Bitmap>
        get() = _rawImage

    fun setRawImage(image: Bitmap){
        _rawImage.value = image

    }

    fun onExecuteModel(
        imageClassificationModelExecutor: ImageClassificationModelExecutor
    ){
        if(_rawImage.value != null){
            _classificationResults.value = imageClassificationModelExecutor.execute(_rawImage.value!!)
        }


    }



}
