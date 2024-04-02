package com.example.haemo_kotlin.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.model.PostModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {
    val num = MutableStateFlow(0)

    private val _postModelList = MutableStateFlow<List<PostModel>>(emptyList())
    val postModelList: StateFlow<List<PostModel>> = _postModelList

    private val _postModel = MutableStateFlow<PostModel?>(null)
    val postModel: StateFlow<PostModel?> = _postModel

//    private val _formattedDate = MutableStateFlow<String?>(null)
//    val formattedDate: StateFlow<String?> = _formattedDate


    private val _postModelState = MutableStateFlow<Resource<PostModel>>(Resource.loading(null))
    val postModelState: StateFlow<Resource<PostModel>> = _postModelState.asStateFlow()


    private val _postModelListState = MutableStateFlow<Resource<List<PostModel>>>(Resource.loading(null))
    val postModelListState: StateFlow<Resource<List<PostModel>>> = _postModelListState.asStateFlow()


    fun add() {
        num.value = num.value + 1
    }

    fun setZero() {
        num.value = 0
    }

    fun getPost() {
        viewModelScope.launch {
            _postModelState.value = Resource.loading(null)
            try {
                val response = repository.getPost()
                if (response.isSuccessful && response.body() != null) {
                    val postList = response.body()
                    _postModelList.value = postList!!
                    _postModelListState.value = Resource.success(postList)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 에러 응답: $errorBody")
                    _postModelListState.value = Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
                _postModelListState.value = Resource.error(e.message ?: "An error occurred", null)
            }
        }
    }

    suspend fun getOnePost(idx: Int) {
        viewModelScope.launch {
            _postModelState.value = Resource.loading(null)
            try {
                val response = repository.getOnePost(idx)
                if (response.isSuccessful && response.body() != null) {
                    val post = response.body()
                    _postModel.value = post!!
                    _postModelState.value = Resource.success(post)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 하나 에러 응답: $errorBody")
                    _postModelState.value = Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
                _postModelState.value = Resource.error(e.message ?: "An error occurred", null)
            }
        }
    }

    fun convertDate(input: String) : String{
        val parsedDate = parseDateString(input, "yyyy년 MM월 dd일 hh시")
        val formattedDate = formatDate(parsedDate, "yyyy.MM.dd hh시")

        return formattedDate
    }

    fun parseDateString(dateString: String, format: String): Date {
        val parser = SimpleDateFormat(format, Locale.KOREA)
        return parser.parse(dateString) ?: Date()
    }

    fun formatDate(date: Date, format: String): String {
        val formatter = SimpleDateFormat(format, Locale.KOREA)
        return formatter.format(date)
    }

}