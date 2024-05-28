package com.example.haemo_kotlin.viewModel.user

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.model.retrofit.user.MailModel
import com.example.haemo_kotlin.model.retrofit.user.MailState
import com.example.haemo_kotlin.repository.UserRepository
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InquiryViewModel @Inject constructor(
    private val repository: UserRepository,
    context: Context
) : ViewModel() {

    var inquiryType = MutableStateFlow("카테고리 선택")
    var email = MutableStateFlow("")
    var content = MutableStateFlow("")
    var inquiryState = MutableStateFlow(MailState.NONE)
    private val nickname = SharedPreferenceUtil(context).getString("nickname", "")

    var isValid: StateFlow<Boolean> =
        combine(
            content,
            inquiryType,
            email
        ) { content, inquiryType, email ->
            content.isNotBlank() && inquiryType != "카테고리 선택" && email.isNotBlank()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun sendInquiry() {
        inquiryState.value = MailState.NONE
        viewModelScope.launch {
            try {
                if (nickname!!.isNotBlank()) {
                    val message =
                        "유저: ${nickname}\n답변 이메일: ${email.value}\n유형: ${inquiryType.value}\n내용: ${content.value}"
                    val mail = MailModel(
                        title = "WonT 문의 사항",
                        content = message
                    )
                    val response = repository.sendMail(mail)
                    if (response.isSuccessful && response.body() != null) {
                        val isSuccess = response.body()
                        if (isSuccess != null) {
                            if (isSuccess)
                                inquiryState.value = MailState.SUCCESS
                            else inquiryState.value = MailState.FAIL
                            Log.d(
                                "미란 메일 전송", inquiryState.value.toString()
                            )
                        }
                    } else {
                        val errorBody = response.errorBody()?.string() ?: "Unknown error"
                        Log.e("API Error", "메일 에러 응답: $errorBody")
                    }
                }
            } catch (e: Exception) {
                Log.e("API Exception", "메일 요청 중 예외 발생: ${e.message}")
            }
        }
    }
}