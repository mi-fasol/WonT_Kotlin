package com.example.haemo_kotlin.viewModel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.MainActivity
import com.example.haemo_kotlin.model.LoginModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.repository.UserRepository
import com.example.haemo_kotlin.util.SharedPreferenceUtil
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
class LoginViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    enum class LoginUserState{SUCCESS, LOGIN, NONE}

    private val _loginState = MutableStateFlow<Resource<LoginModel>>(Resource.loading(null))
    val loginState: StateFlow<Resource<LoginModel>> = _loginState.asStateFlow()

    private val _loginId = MutableStateFlow<String?>("")
    val loginId: StateFlow<String?> = _loginId.asStateFlow()

    private val _user = MutableStateFlow(LoginUserState.NONE)
    val loginUser: StateFlow<LoginUserState> = _user.asStateFlow()

    private val _isLoginSuccess = MutableStateFlow<Boolean>(false)
    val isLoginSuccess: StateFlow<Boolean> = _isLoginSuccess.asStateFlow()

    val id = MutableStateFlow("")
    val pwd = MutableStateFlow("")

    var isValid: StateFlow<Boolean> = combine(id, pwd) { id, pwd ->
        id.isNotBlank() && pwd.isNotBlank()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun checkUserExists(context: Context) {
        val studentId = SharedPreferenceUtil(context).getString("studentId", "").toString()
        val uId = SharedPreferenceUtil(context).getInt("uId", 0)

        if(studentId.isNotBlank()){
            if(uId == 0){
                _user.value = LoginUserState.LOGIN
            } else{
                _user.value = LoginUserState.SUCCESS
            }
        }
    }

    fun login(id: String, pwd: String, context: Context) {
        viewModelScope.launch {
            _loginState.value = Resource.loading(null)
            try {
                val loginModel = LoginModel(id, pwd)
                val response = repository.tryLogin(loginModel)
                if (response.isSuccessful && response.body() != null) {
                    val isSuccess = response.body()
                    if (isSuccess != null) {
                        _isLoginSuccess.value = isSuccess
                    }
                    if(isSuccess!!){
                        SharedPreferenceUtil(context).setString("studentId", id)
                    }
                    Log.d("로그인 결과", _isLoginSuccess.value.toString())
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "로그인 에러 응답: $errorBody")
                    _loginState.value = Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "로그인 요청 중 예외 발생: ${e.message}")
                _loginState.value = Resource.error(e.message ?: "An error occurred", null)
            }
        }
    }
}