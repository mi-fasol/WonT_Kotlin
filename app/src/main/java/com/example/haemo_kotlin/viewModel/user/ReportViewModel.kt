package com.example.haemo_kotlin.viewModel.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.model.retrofit.user.MailModel
import com.example.haemo_kotlin.model.retrofit.user.MailState
import com.example.haemo_kotlin.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    var reportReason = MutableStateFlow("")
    var content = MutableStateFlow("")
    var reportState = MutableStateFlow(MailState.NONE)

    var isValid: StateFlow<Boolean> =
        combine(
            content,
            reportReason
        ) { content, reportReason ->
            content.isNotBlank() && reportReason.isNotBlank()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun sendReport(nickname: String) {
        reportState.value = MailState.NONE
        viewModelScope.launch {
            try {
                val message = "유저: ${nickname}\n사유: ${reportReason.value}\n내용: ${content.value}"
                val mail = MailModel(
                    title = "WonT 신고 접수",
                    content = message
                )
                val response = repository.sendMail(mail)
                if (response.isSuccessful && response.body() != null) {
                    val isSuccess = response.body()
                    if (isSuccess != null) {
                        if (isSuccess)
                            reportState.value = MailState.SUCCESS
                        else reportState.value = MailState.FAIL
                        Log.d(
                            "미란 메일 전송", reportState.value.toString()
                        )
                    }
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "메일 에러 응답: $errorBody")
                }
            } catch (e: Exception) {
                Log.e("API Exception", "메일 요청 중 예외 발생: ${e.message}")
            }
        }
    }
}