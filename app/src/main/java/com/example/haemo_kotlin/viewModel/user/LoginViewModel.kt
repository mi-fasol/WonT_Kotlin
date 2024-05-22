package com.example.haemo_kotlin.viewModel.user

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.model.retrofit.user.LoginModel
import com.example.haemo_kotlin.model.retrofit.user.UserResponseModel
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
    private val repository: UserRepository,
    private val context: Context
) : ViewModel() {

    enum class LoginUserState { SUCCESS, LOGIN, NONE }

    private val _loginState = MutableStateFlow<Resource<LoginModel>>(Resource.loading(null))
    val loginState: StateFlow<Resource<LoginModel>> = _loginState.asStateFlow()

    private val _userState = MutableStateFlow(LoginUserState.NONE)
    val loginUser: StateFlow<LoginUserState> = _userState.asStateFlow()

    private val _isLoginSuccess = MutableStateFlow(false)
    val isLoginSuccess: StateFlow<Boolean> = _isLoginSuccess.asStateFlow()

    val id = MutableStateFlow("")
    val pwd = MutableStateFlow("")

    var isValid: StateFlow<Boolean> = combine(id, pwd) { id, pwd ->
        id.isNotBlank() && pwd.isNotBlank()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    suspend fun checkUserExists() {
        _userState.value = LoginUserState.NONE
        val studentId = SharedPreferenceUtil(context).getString("studentId", "").toString()
        if (studentId.isNotBlank()) {
            viewModelScope.launch {
                try {
                    Log.d("미란 ", "실행은 됨")
                    Log.d("미란 이전 state", _userState.value.toString())
                    val response = repository.checkHaveAccount(studentId.toInt())
                    if (response.isSuccessful && response.body() != null) {
                        val responseUId = response.body()
                        if (responseUId == 0) {
                            _userState.value = LoginUserState.LOGIN
                            Log.d("미란", "회원가입 필요")
                        } else {
                            _userState.value = LoginUserState.SUCCESS
                            SharedPreferenceUtil(context).setInt("uId", responseUId!!)
                            Log.d("미란 유저", responseUId.toString())
                        }
                        Log.d("미란 state", _userState.value.toString())
                    } else{
                        Log.e("API Exception", "널로 받아짐")
                    }
                } catch (e: Exception) {
                    Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
                }
            }
        }
    }

    fun login(id: String, pwd: String) {
        _isLoginSuccess.value = false
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
                    if (isSuccess!!) {
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

    fun signOut(mainColor: Int) {
        SharedPreferenceUtil(context).removeAll()
        SharedPreferenceUtil(context).setInt("mainColor", mainColor)
    }
}