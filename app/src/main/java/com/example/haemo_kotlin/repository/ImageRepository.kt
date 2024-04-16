package com.example.haemo_kotlin.repository

import android.content.Context
import android.net.Uri
import com.example.haemo_kotlin.network.RetrofitClient
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import javax.inject.Inject

class ImageRepository @Inject constructor(
    private val retrofitClient: RetrofitClient,
    private val context: Context
) {

    suspend fun uploadImage(imageUri: Uri): String {
        try {
            val file = getFileFromUri(imageUri, context)
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

            val response = RetrofitClient.service.uploadImage(body)
            if (response.isSuccessful) {
                val imageUrl = response.body()!!
                return imageUrl
            } else {
                throw Exception("Image upload failed: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            throw Exception("Image upload failed: ${e.message}")
        }
    }

    private fun getFileFromUri(uri: Uri, context: Context): File {
        val inputStream =
            context.contentResolver.openInputStream(uri) ?: throw FileNotFoundException()
        val file = File(context.cacheDir, "upload_image_${System.currentTimeMillis()}")
        FileOutputStream(file).use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        return file
    }
}