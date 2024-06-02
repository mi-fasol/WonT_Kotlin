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

@HiltViewModel
class AcceptationViewModel @Inject constructor(
    private val acceptationRepository: AcceptationRepository,
    private val context: Context
) : ViewModel() {

    private val _acceptationList = MutableStateFlow<List<AcceptationResponseModel>>(emptyList())
    val acceptationList: StateFlow<List<AcceptationResponseModel>> = _acceptationList

    private val _myAcceptState = MutableStateFlow(AcceptState.NONE)
    val myAcceptState: StateFlow<AcceptState> = _myAcceptState

    private val _attendeeList =
        MutableStateFlow<MutableMap<Int, List<UserResponseModel>>>(HashMap())
    val attendeeList: StateFlow<MutableMap<Int, List<UserResponseModel>>> = _attendeeList

    private val _attendeeModelList =
        MutableStateFlow<MutableMap<Int, List<AcceptationResponseModel>>>(HashMap())
    val attendeeModelList: StateFlow<MutableMap<Int, List<AcceptationResponseModel>>> =
        _attendeeModelList

    private val _acceptRegisterState =
        MutableStateFlow<Resource<AcceptationResponseModel>>(Resource.loading(null))
    val acceptRegisterState: StateFlow<Resource<AcceptationResponseModel>> =
        _acceptRegisterState.asStateFlow()

    private val _acceptDeleteState =
        MutableStateFlow<Resource<Boolean?>>(Resource.loading(null))
    val acceptDeleteState: StateFlow<Resource<Boolean?>> = _acceptDeleteState.asStateFlow()

    private val _allowUserState =
        MutableStateFlow<Resource<Boolean?>>(Resource.loading(null))
    val allowUserState: StateFlow<Resource<Boolean?>> = _allowUserState.asStateFlow()

    suspend fun getAcceptationByPId(pId: Int) {
        _myAcceptState.value = AcceptState.NONE
        viewModelScope.launch {
            try {
                val response = acceptationRepository.getJoinUserByPId(pId)
                if (response.isSuccessful && response.body() != null) {
                    val acceptList = response.body()
                    _attendeeModelList.value[pId] = acceptList!!
                    _attendeeModelList.value[pId]!!.forEach {
                        if (
                            it.uId == SharedPreferenceUtil(context).getInt("uId", 0)) {
                            if (it.acceptation) {
                                _myAcceptState.value = AcceptState.JOIN
                            } else {
                                _myAcceptState.value = AcceptState.REQUEST
                            }
                        }
                    }
                    Log.d("미란 참여 테이블", acceptationList.value[pId].toString())
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 하나 에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
            }
        }
    }

    suspend fun getAttendeeByPId(pId: Int) {
        _myAcceptState.value = AcceptState.NONE
        viewModelScope.launch {
            try {
                val response = acceptationRepository.getAttendeeListByPId(pId)
                if (response.isSuccessful && response.body() != null) {
                    val acceptList = response.body()
                    _attendeeList.value[pId] = acceptList!!
                    Log.d("미란 참여 유저", _attendeeList.value[pId].toString())
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 하나 에러 응답: $errorBody")
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
            _acceptRegisterState.value = Resource.loading(null)
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

    fun deleteAcceptationRequest(pId: Int) {
        val uId = SharedPreferenceUtil(context).getInt("uId", 0)
        viewModelScope.launch {
            _acceptDeleteState.value = Resource.loading(null)
            try {
                val response = acceptationRepository.deleteAcceptRequest(uId, pId)
                if (response.isSuccessful && response.body() != null) {
                    _acceptDeleteState.value = Resource.success(response.body())
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

    fun allowUserToJoin(pId: Int, uId: Int) {
        viewModelScope.launch {
            _allowUserState.value = Resource.loading(null)
            try {
                val response = acceptationRepository.allowUserToJoin(uId, pId)
                if (response.isSuccessful && response.body() != null) {
                    _allowUserState.value = Resource.success(response.body())
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