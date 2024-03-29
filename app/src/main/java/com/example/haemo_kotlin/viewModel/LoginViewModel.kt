package com.example.haemo_kotlin.viewModel

//import dagger.hilt.android.lifecycle.HiltViewModel
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.model.LoginModel
import com.example.haemo_kotlin.model.PostModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.repository.UserRepository
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

//import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<Resource<LoginModel>>(Resource.loading(null))
    val loginState: StateFlow<Resource<LoginModel>> = _loginState.asStateFlow()

    private val _loginId = MutableStateFlow<String?>("")
    val loginId :StateFlow<String?> = _loginId.asStateFlow()

    val id = MutableStateFlow("")
    val pwd = MutableStateFlow("")

    fun checkIdExists(context: Context){
        _loginId.value = SharedPreferenceUtil(context).getString("studentId", "").toString()
    }

    fun login(id: String, pwd: String) {
        viewModelScope.launch {
            _loginState.value = Resource.loading(null)
            try {

                val loginModel = LoginModel(id, pwd)
                val response = repository.tryLogin(loginModel)
                if (response.isSuccessful && response.body() != null) {
                    val isSuccess = response.body()
                    Log.d("로그인 결과", isSuccess.toString())
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