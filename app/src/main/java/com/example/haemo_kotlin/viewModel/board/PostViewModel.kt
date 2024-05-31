package com.example.haemo_kotlin.viewModel.board

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.model.retrofit.acceptation.AcceptationModel
import com.example.haemo_kotlin.model.retrofit.acceptation.AcceptationResponseModel
import com.example.haemo_kotlin.model.retrofit.comment.comment.CommentResponseModel
import com.example.haemo_kotlin.model.retrofit.post.PostModel
import com.example.haemo_kotlin.model.retrofit.post.PostResponseModel
import com.example.haemo_kotlin.model.retrofit.user.UserResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.repository.AcceptationRepository
import com.example.haemo_kotlin.repository.PostRepository
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.example.haemo_kotlin.util.getCurrentDateTime
import com.example.haemo_kotlin.util.getCurrentYear
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class AcceptState {
    NONE, REQUEST, JOIN
}

@HiltViewModel
class PostViewModel @Inject constructor(
    private val repository: PostRepository,
    private val acceptationRepository: AcceptationRepository,
    private val context: Context
) : ViewModel() {

    // Get 변수
    private val _postModelList = MutableStateFlow<List<PostResponseModel>>(emptyList())
    val postModelList: StateFlow<List<PostResponseModel>> = _postModelList

    private val _postModel = MutableStateFlow<PostResponseModel?>(null)
    val postModel: StateFlow<PostResponseModel?> = _postModel

    private val _user = MutableStateFlow<UserResponseModel?>(null)
    val user: StateFlow<UserResponseModel?> = _user

    private val _todayPostList = MutableStateFlow<List<PostResponseModel>>(emptyList())
    val todayPostList: StateFlow<List<PostResponseModel>> = _todayPostList

    private val _postModelState =
        MutableStateFlow<Resource<PostResponseModel>>(Resource.loading(null))
    val postModelState: StateFlow<Resource<PostResponseModel>> = _postModelState.asStateFlow()

    private val _acceptationList = MutableStateFlow<List<AcceptationResponseModel>>(emptyList())
    val acceptationList: StateFlow<List<AcceptationResponseModel>> = _acceptationList

    private val _myAcceptState = MutableStateFlow(AcceptState.NONE)
    val myAcceptState: StateFlow<AcceptState> = _myAcceptState

    private val _commentList = MutableStateFlow<List<CommentResponseModel>>(emptyList())
    val commentList: StateFlow<List<CommentResponseModel>> = _commentList

    // post 변수
    val title = MutableStateFlow("")
    val person = MutableStateFlow(0)
    val category = MutableStateFlow("")
    val deadlineYear = MutableStateFlow(getCurrentYear())
    val deadlineMonth = MutableStateFlow("01월")
    val deadlineDay = MutableStateFlow("01일")
    val deadlineTime = MutableStateFlow("01시")
    val content = MutableStateFlow("")

    private val deadline =
        MutableStateFlow("")

    // 상태 관리
    private val _todayPostModelState =
        MutableStateFlow<Resource<List<PostResponseModel>>>(Resource.loading(null))
    val todayPostModelState: StateFlow<Resource<List<PostResponseModel>>> =
        _todayPostModelState.asStateFlow()


    private val _postModelListState =
        MutableStateFlow<Resource<List<PostResponseModel>>>(Resource.loading(null))
    val postModelListState: StateFlow<Resource<List<PostResponseModel>>> =
        _postModelListState.asStateFlow()

    private val _postDeleteState =
        MutableStateFlow<Boolean?>(null)
    val postDeleteState: StateFlow<Boolean?> =
        _postDeleteState.asStateFlow()

    fun deletePost(pId: Int) {
        viewModelScope.launch {
            _postDeleteState.value = null
            try {
                val response = repository.deleteClubPost(pId)
                if (response.isSuccessful && response.body() != null) {
                    _postDeleteState.value = response.body()!!
                    Log.d("게시물 전송", response.body().toString())
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
            }
        }
    }

    // 필드 유효성 검사
    var isValid: StateFlow<Boolean> =
        combine(
            title,
            person,
            category,
            content
        ) { title, person, category, content ->
            title.isNotBlank() && person != 0 && category.isNotBlank() && content.isNotBlank()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _postRegisterState =
        MutableStateFlow<Resource<PostResponseModel>>(Resource.loading(null))
    val postRegisterState: StateFlow<Resource<PostResponseModel>> = _postRegisterState.asStateFlow()

    private val _acceptRegisterState =
        MutableStateFlow<Resource<AcceptationResponseModel>>(Resource.loading(null))
    val acceptRegisterState: StateFlow<Resource<AcceptationResponseModel>> = _acceptRegisterState.asStateFlow()

    suspend fun getPost() {
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
                    _postModelListState.value =
                        Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
                _postModelListState.value = Resource.error(e.message ?: "An error occurred", null)
            }
        }
    }

    suspend fun getTodayPost() {
        viewModelScope.launch {
            _postModelState.value = Resource.loading(null)
            try {
                val response = repository.getTodayPost()
                if (response.isSuccessful && response.body() != null) {
                    val postList = response.body()
                    _todayPostList.value = postList!!
                    _todayPostModelState.value = Resource.success(postList)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 에러 응답: $errorBody")
                    _todayPostModelState.value =
                        Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
                _todayPostModelState.value = Resource.error(e.message ?: "An error occurred", null)
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

    suspend fun getPostingUser(pId: Int) {
        viewModelScope.launch {
            _postModelState.value = Resource.loading(null)
            try {
                val response = repository.getPostingUser(pId)
                if (response.isSuccessful && response.body() != null) {
                    val user = response.body()
                    _user.value = user!!
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 하나 에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
            }
        }
    }

    suspend fun getAcceptationByPId(pId: Int) {
        _myAcceptState.value = AcceptState.NONE
        viewModelScope.launch {
            try {
                val response = acceptationRepository.getJoinUserByPId(pId)
                if (response.isSuccessful && response.body() != null) {
                    val acceptList = response.body()
                    _acceptationList.value = acceptList!!
                    _acceptationList.value.forEach {
                        if (
                            it.uId == SharedPreferenceUtil(context).getInt("uId", 0)) {
                            if (it.acceptation) {
                                _myAcceptState.value = AcceptState.JOIN
                            } else {
                                _myAcceptState.value = AcceptState.REQUEST
                            }
                        }
                    }
                    Log.d("미란 참여 테이블", acceptationList.value.toString())
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 하나 에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
            }
        }
    }

    suspend fun getCommentListByPId(pId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getCommentListByPId(pId)
                if (response.isSuccessful && response.body() != null) {
                    val commentList = response.body()
                    _commentList.value = commentList!!
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 하나 에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
            }
        }
    }

    fun registerPost() {
        val today = getCurrentDateTime()
        deadline.value =
            "${deadlineYear.value} ${deadlineMonth.value} ${deadlineDay.value} ${deadlineTime.value}"
        val post = PostModel(
            title.value,
            content.value,
            SharedPreferenceUtil(context).getString("nickname", "")!!.toString(),
            person.value,
            deadline.value,
            category.value,
            today,
            0
        )

        viewModelScope.launch {
            _postRegisterState.value = Resource.loading(null)
            try {
                val response = repository.registerPost(post)
                if (response.isSuccessful && response.body() != null) {
//                    _registerState.value = Resource.success(response.body())
                    _postRegisterState.value = Resource.success(response.body())
                    Log.d("게시물 전송", response.body().toString())
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
            }
        }
    }

    fun sendAcceptationRequest(pId: Int) {
        val accept = AcceptationModel(
            pId = pId,
            uId = SharedPreferenceUtil(context).getInt("uId", 0),
            acceptation = false
        )

        viewModelScope.launch {
            _postRegisterState.value = Resource.loading(null)
            try {
                val response = acceptationRepository.registerAcceptRequest(accept)
                if (response.isSuccessful && response.body() != null) {
                    _acceptRegisterState.value = Resource.success(response.body())
                    Log.d("참여 요청 완료", response.body().toString())
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