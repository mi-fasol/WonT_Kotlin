package com.example.haemo_kotlin.viewModel.board

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.model.retrofit.post.NoticeModel
import com.example.haemo_kotlin.model.retrofit.post.NoticeResponseModel
import com.example.haemo_kotlin.model.retrofit.post.PostResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.repository.PostRepository
import com.example.haemo_kotlin.util.getCurrentDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class NoticeViewModel @Inject constructor(
    private val repository: PostRepository,
    private val context: Context
) : ViewModel() {

    // Get 변수
    private val _noticeList = MutableStateFlow<List<NoticeResponseModel>>(emptyList())
    val noticeList: StateFlow<List<NoticeResponseModel>> = _noticeList

    private val _noticeModel = MutableStateFlow<NoticeResponseModel?>(null)
    val noticeModel: StateFlow<NoticeResponseModel?> = _noticeModel

    private val _visibility = MutableStateFlow<Boolean?>(null)
    val visibility: StateFlow<Boolean?> = _visibility

    private val _noticeModelState =
        MutableStateFlow<Resource<NoticeResponseModel>>(Resource.loading(null))
    val noticeModelState: StateFlow<Resource<NoticeResponseModel>> = _noticeModelState.asStateFlow()

    // post 변수
    val title = MutableStateFlow("")
    val category = MutableStateFlow("")
    val content = MutableStateFlow("")

    private val _noticeListState =
        MutableStateFlow<Resource<List<NoticeResponseModel>>>(Resource.loading(null))
    val noticeListState: StateFlow<Resource<List<NoticeResponseModel>>> =
        _noticeListState.asStateFlow()

    // 필드 유효성 검사
    var isValid: StateFlow<Boolean> =
        combine(
            title,
            category,
            content
        ) { title, category, content ->
            title.isNotBlank() && category.isNotBlank() && content.isNotBlank()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _noticeRegisterState =
        MutableStateFlow<Resource<NoticeResponseModel>>(Resource.loading(null))
    val noticeRegisterState: StateFlow<Resource<NoticeResponseModel>> =
        _noticeRegisterState.asStateFlow()

    suspend fun getAllNotice() {
        viewModelScope.launch {
            _noticeModelState.value = Resource.loading(null)
            try {
                val response = repository.getNotice()
                if (response.isSuccessful && response.body() != null) {
                    val postList = response.body()
                    _noticeList.value = postList!!
                    _noticeListState.value = Resource.success(postList)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "공지사항 에러 응답: $errorBody")
                    _noticeListState.value =
                        Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
                _noticeListState.value = Resource.error(e.message ?: "An error occurred", null)
            }
        }
    }

    suspend fun getNoticeById(id: Int) {
        viewModelScope.launch {
            _noticeModelState.value = Resource.loading(null)
            try {
                val response = repository.getNoticeById(id)
                if (response.isSuccessful && response.body() != null) {
                    val post = response.body()
                    _noticeModel.value = post!!
                    _noticeModelState.value = Resource.success(post)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 하나 에러 응답: $errorBody")
                    _noticeModelState.value = Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
                _noticeModelState.value = Resource.error(e.message ?: "An error occurred", null)
            }
        }
    }

    suspend fun changeVisibility(id: Int) {
        viewModelScope.launch {
            _noticeModelState.value = Resource.loading(null)
            try {
                val response = repository.changeNoticeVisibility(id)
                if (response.isSuccessful && response.body() != null) {
                    val post = response.body()
                    _visibility.value = post!!
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 하나 에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
            }
        }
    }

    fun registerNotice() {
        val today = getCurrentDateTime()

        val notice = NoticeModel(
            title = title.value,
            content = content.value,
            category = category.value,
            visibility = true,
            date = today
        )

        viewModelScope.launch {
            _noticeRegisterState.value = Resource.loading(null)
            try {
                val response = repository.registerNotice(notice)
                if (response.isSuccessful && response.body() != null) {
                    _noticeRegisterState.value = Resource.success(response.body())
                    Log.d("공지사항 전송", response.body().toString())
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
            }
        }
    }
}