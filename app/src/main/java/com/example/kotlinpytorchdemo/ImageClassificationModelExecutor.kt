package com.example.kotlinpytorchdemo

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.pytorch.IValue

import org.pytorch.Module
import org.pytorch.torchvision.TensorImageUtils

class ImageClassificationModelExecutor(val context: Context) {

    private var moduleAssetName: String = "model.pt"
    private lateinit var _model : Module
    val model : Module
        get() {
            if(!::_model.isInitialized || _model==null){
                _model = Module.load(Utils.assetFilePath(context,moduleAssetName))
            }
            return _model
        }

    companion object{
        private val TAG = "ImageClassMExec"
        const val TOP_K = 3

    }


    fun execute(
        bitmapImage: Bitmap
    ): List<ClassificationResult>?{
        try{
            val results = mutableListOf<ClassificationResult>()
            val bitmap = Utils.getResizedBitmap(bitmapImage, 220, 220)
            val inputTensor = TensorImageUtils.bitmapToFloat32Tensor(bitmap,
                TensorImageUtils.TORCHVISION_NORM_MEAN_RGB, TensorImageUtils.TORCHVISION_NORM_STD_RGB)
            val outputTensor = model.forward(IValue.from(inputTensor)).toTensor()
            val scores = outputTensor.dataAsFloatArray

            scores?.let{
                val ixs = Utils.topK(scores, TOP_K)
                val topKClassNames = Array(TOP_K) { i -> (i * i).toString() }
                val topKScores = FloatArray(TOP_K)

                for (i in 0 until TOP_K) {
                    val ix = ixs[i]
                    if (ix <= ImageNetClasses.IMAGENET_CLASSES.size) {
                        topKClassNames[i] = ImageNetClasses.IMAGENET_CLASSES[ix]
                    }else{
                        topKClassNames[i] = "Not Defined"
                    }
                    topKScores[i] = scores[ix]
                    results.add(ClassificationResult(topKClassNames[i], topKScores[i]))
                }

                return results
            }
            return null

        } catch (e: Exception){
            val exceptionLog = "something went wrong: ${e.message}"
            Log.wtf(TAG, exceptionLog)
            val results = mutableListOf<ClassificationResult>()
            val message: String =  "Error during image analysis " + e.message
            val score = 0F
            results.add(ClassificationResult(message, score))
            return results

        }


    }



}