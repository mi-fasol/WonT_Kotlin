package com.example.haemo_kotlin.viewModel.user

import android.app.VoiceInteractor
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.model.retrofit.user.LoginModel
import com.example.haemo_kotlin.model.retrofit.user.UserModel
import com.example.haemo_kotlin.model.retrofit.user.UserResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.repository.UserRepository
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.io.IOException
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
    private val _reportState = MutableStateFlow(ReportState.NONE)
    val reportState: StateFlow<ReportState> = _reportState.asStateFlow()

    var isValid: StateFlow<Boolean> =
        combine(
            content,
            reportReason
        ) { content, reportReason ->
            content.isNotBlank() && reportReason.isNotBlank() && content.isNotBlank()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun sendReport(nickname: String) {
        _reportState.value = ReportState.NONE
        viewModelScope.launch {
            try {
                val message = "유저: ${nickname}\n사유: ${reportReason.value}\n내용: ${content.value}"
                val response = repository.sendReport(message)
                if (response.isSuccessful && response.body() != null) {
                    val isSuccess = response.body()
                    if (isSuccess != null) {
                        if (isSuccess)
                            _reportState.value = ReportState.SUCCESS
                        else _reportState.value = ReportState.FAIL
                        Log.d(
                            "미란 메일 전송", _reportState.value.toString()
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