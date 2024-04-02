package com.example.haemo_kotlin.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.model.LoginModel
import com.example.haemo_kotlin.model.UserModel
import com.example.haemo_kotlin.model.UserResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.repository.UserRepository
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.example.haemo_kotlin.util.userProfileList
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
class UserRegisterViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    enum class LoginUserState { SUCCESS, LOGIN, NONE }

    private val _registerState =
        MutableStateFlow<Resource<UserResponseModel>>(Resource.loading(null))
    val registerState: StateFlow<Resource<UserResponseModel>> = _registerState.asStateFlow()

    private val _isRegisterSuccess = MutableStateFlow<Boolean>(false)
    val isRegisterSuccess: StateFlow<Boolean> = _isRegisterSuccess.asStateFlow()

    val image = MutableStateFlow(0)
    val nickname = MutableStateFlow("")
    val gender = MutableStateFlow("")
    val major = MutableStateFlow("")

    var isValid: StateFlow<Boolean> = combine(nickname, gender, major) { nickname, gender, major ->
        nickname.isNotBlank() && gender.isNotBlank() && major.isNotBlank()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun registerUser(nickname: String, major: String, gender: String, context: Context) {
        val user = UserModel(nickname, major, gender, image.value)

        viewModelScope.launch {
            _registerState.value = Resource.loading(null)
            try {
                val response = repository.registerUser(user)
                if (response.isSuccessful && response.body() != null) {
                    val responseUser = response.body()
                    _registerState.value = Resource.success(response.body())
                    _isRegisterSuccess.value = true
                    SharedPreferenceUtil(context).setInt("uId", responseUser!!.uId)
                    SharedPreferenceUtil(context).setInt("image", responseUser.userImage)
                    Log.d("유저", responseUser.toString())
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "에러 응답: $errorBody")
                    _registerState.value = Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
                _registerState.value = Resource.error(e.message ?: "An error occurred", null)
            }
        }
    }
}