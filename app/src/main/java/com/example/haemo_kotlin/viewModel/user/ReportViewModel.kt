package com.example.haemo_kotlin.viewModel.user

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class ReportState {
    NONE, SUCCESS, FAIL
}

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val repository: UserRepository,
    private val context: Context
) : ViewModel() {

    var reportReason = MutableStateFlow("")
    var content = MutableStateFlow("")
    var reportState = MutableStateFlow(ReportState.NONE)

    var isValid: StateFlow<Boolean> =
        combine(
            content,
            reportReason
        ) { content, reportReason ->
            content.isNotBlank() && reportReason.isNotBlank() && content.isNotBlank()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun sendReport(nickname: String) {
        reportState.value = ReportState.NONE
        viewModelScope.launch {
            try {
                val message = "유저: ${nickname}\n사유: ${reportReason.value}\n내용: ${content.value}"
                val response = repository.sendReport(message)
                if (response.isSuccessful && response.body() != null) {
                    val isSuccess = response.body()
                    if (isSuccess != null) {
                        if (isSuccess)
                            reportState.value = ReportState.SUCCESS
                        else reportState.value = ReportState.FAIL
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