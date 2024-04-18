package com.example.haemo_kotlin.viewModel.board

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.model.post.ClubPostModel
import com.example.haemo_kotlin.model.post.ClubPostResponseModel
import com.example.haemo_kotlin.model.user.UserResponseModel
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.repository.ImageRepository
import com.example.haemo_kotlin.repository.PostRepository
import com.example.haemo_kotlin.util.SharedPreferenceUtil
import com.example.haemo_kotlin.util.getCurrentDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject


@HiltViewModel
class ClubPostViewModel @Inject constructor(
    private val repository: PostRepository,
    private val imageRepository: ImageRepository
) : ViewModel() {

    private val _clubPostList = MutableStateFlow<List<ClubPostResponseModel>>(emptyList())
    val clubPostList: StateFlow<List<ClubPostResponseModel>> = _clubPostList.asStateFlow()

    private val _clubPost = MutableStateFlow<ClubPostResponseModel?>(null)
    val clubPost: StateFlow<ClubPostResponseModel?> = _clubPost.asStateFlow()

    private val _user = MutableStateFlow<UserResponseModel?>(null)
    val user: StateFlow<UserResponseModel?> = _user.asStateFlow()

    private val _clubPostState =
        MutableStateFlow<Resource<ClubPostResponseModel>>(Resource.loading(null))
    val clubPostState: StateFlow<Resource<ClubPostResponseModel>> = _clubPostState.asStateFlow()

    private val _clubPostListState =
        MutableStateFlow<Resource<List<ClubPostResponseModel>>>(Resource.loading(null))
    val clubPostListState: StateFlow<Resource<List<ClubPostResponseModel>>> =
        _clubPostListState.asStateFlow()

    private val _clubPostRegisterState =
        MutableStateFlow<Resource<ClubPostResponseModel>>(Resource.loading(null))
    val clubPostRegisterState: StateFlow<Resource<ClubPostResponseModel>> =
        _clubPostRegisterState.asStateFlow()

    // 유효성 검사

    val title = MutableStateFlow("")
    val description = MutableStateFlow("")
    val image = MutableStateFlow("")
    val person = MutableStateFlow(0)
    val content = MutableStateFlow("")

    var isValid: StateFlow<Boolean> =
        combine(
            title,
            person,
            description,
            image,
            content
        ) { title, person, description, image, content ->
            title.isNotBlank() && person != 0 && description.isNotBlank() && image.isNotBlank() && content.isNotBlank()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun uploadImage(imageUri: Uri) {
        viewModelScope.launch {
            try {
                val result = imageRepository.uploadImage(imageUri)
                image.value = result
                Log.d("사진", result)
            } catch (e: HttpException) {
                Log.e("ImageUploadViewModel", "API Error: HTTP ${e.code()} ${e.message}")
            } catch (e: Exception) {
                Log.e("ImageUploadViewModel", "Unknown Error: ${e.message}")
            }
        }
    }

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    init {
        viewModelScope.launch {
            getClubPostList()
        }
    }

//    fun isHangul(c: Char): Boolean {
//        return c.toInt() in 0xAC00..0xD7A3 || c in 'ㄱ'..'ㅣ'
//    }
//
//    fun getHangulInitialSound(c: Char): Char {
//        val init = ((c.toInt() - 0xAC00) / 28 / 21).toChar()
//        return if (init in 'ㄱ'..'ㅎ') init + '가'.toInt() - 'ㄱ'.toInt() else init
//    }
//
//    fun filterPostsByTitle(searchText: String): List<ClubPostResponseModel> {
//        return _clubPostList.value.filter { post ->
//            post.containsHangulSearch(searchText)
//        }
//    }

//    fun onSearchTextChange(text: String) {
//        _searchText.value = text
//    }
//
//    fun onToogleSearch() {
//        _isSearching.value = !_isSearching.value
//        if (!_isSearching.value) {
//            onSearchTextChange("")
//        }
//    }
//
//    private val _filteredClubPost = MutableStateFlow<List<ClubPostModel>>(emptyList())
//    val filteredPostList = searchText.combine(
//        _filteredClubPost
//    ) { text, posts ->
//        if (text.isBlank()) {
//            _clubPostList.value
//        } else {
//            val filteredText = text.trim()
//            val isInitialSearch = filteredText.all { it.isHangul() }
//            val initials = if (isInitialSearch) {
//                (HangulUtils.getHangulInitialSound(filteredText) as List<String>).joinToString("")
//            } else {
//                filteredText
//            }
//            posts.filter { post ->
//                val title = post.title.trim()
//                val titleInitials = (HangulUtils.getHangulInitialSound(filteredText) as List<String>).joinToString("")
//                val containsInitials = titleInitials.contains(initials, ignoreCase = true)
//                val containsText = title.contains(filteredText, ignoreCase = true)
//                val containsInitialLastChar =
//                    if (isInitialSearch && filteredText.isNotEmpty() && filteredText.last().isHangul()) {
//                        val lastCharInitial =
//                            HangulUtils.getHangulInitialSound(filteredText.last().toString()).first()
//                        titleInitials.contains(lastCharInitial, ignoreCase = true)
//                    } else {
//                        true
//                    }
//                containsInitials || (containsText && containsInitialLastChar)
//            }
//        }
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5000),
//        initialValue = emptyList()
//    )

//    private val _filteredClubPost = MutableStateFlow(_clubPostList)
//    val filteredPostList = searchText.combine(
//        _filteredClubPost
//    ) { text, posts ->
//        if (text.isBlank()) {
//            _clubPostList.value
//        } else {
//            val filteredText = text.trim()
//            val isInitialSearch = filteredText.all { it.isHangul() }
//            val initials = if (isInitialSearch) {
//                (HangulUtils.getHangulInitialSound(filteredText) as List<String>).joinToString("")
//            } else {
//                filteredText
//            }
//            posts.value.filter { post ->
//                val title = post.title.trim()
//                val titleInitials = (HangulUtils.getHangulInitialSound(filteredText) as List<String>).joinToString("")
//                val containsInitials = titleInitials.contains(initials, ignoreCase = true)
//                val containsText = title.contains(filteredText, ignoreCase = true)
//                val containsInitialLastChar =
//                    if (isInitialSearch && filteredText.isNotEmpty() && filteredText.last().isHangul()
//                    ) {
//                        val lastCharInitial =
//                            hangulUtils.getHangulInitialSound(filteredText.last().toString())
//                                .first()
//                        titleInitials.contains(lastCharInitial, ignoreCase = true)
//                    } else {
//                        true
//                    }
//                containsInitials || (containsText && containsInitialLastChar)
//            }
//        }
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5000),
//        initialValue = _clubPostList.value
//    )

    suspend fun getClubPostList() {
        viewModelScope.launch {
            _clubPostListState.value = Resource.loading(null)
            try {
                val response = repository.getClubPost()
                if (response.isSuccessful && response.body() != null) {
                    val postList = response.body()
                    _clubPostList.value = postList!!
                    _clubPostListState.value = Resource.success(postList)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 에러 응답: $errorBody")
                    _clubPostListState.value = Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
                _clubPostListState.value = Resource.error(e.message ?: "An error occurred", null)
            }
        }
    }

    suspend fun getOneClubPost(idx: Int) {
        viewModelScope.launch {
            _clubPostState.value = Resource.loading(null)
            try {
                val response = repository.getOneClubPost(idx)
                if (response.isSuccessful && response.body() != null) {
                    val post = response.body()
                    _clubPost.value = post!!
                    _clubPostState.value = Resource.success(post)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    Log.e("API Error", "포스트 하나 에러 응답: $errorBody")
                    _clubPostState.value = Resource.error(response.errorBody().toString(), null)
                }
            } catch (e: Exception) {
                Log.e("API Exception", "요청 중 예외 발생: ${e.message}")
                _clubPostState.value = Resource.error(e.message ?: "An error occurred", null)
            }
        }
    }

    suspend fun getClubPostingUser(pId: Int) {
        viewModelScope.launch {
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

    fun registerPost(context: Context) {
        val today = getCurrentDateTime()

//        public constructor ClubPostModel(
//            val title: String,
//        val content: String,
//        val nickname: String,
//        val person: Int,
//        val description: String,
//        val date: String,
//        val image: String?,
//        val wish: Int
//        )

        val _image = if(image.value == "") null else image.value

        val post = ClubPostModel(
            title.value,
            content.value,
            SharedPreferenceUtil(context).getString("nickname", "")!!.toString(),
            person.value,
            description.value,
            today,
            _image,
            0
        )

        viewModelScope.launch {
            _clubPostRegisterState.value = Resource.loading(null)
            try {
                val response = repository.registerClubPost(post)
                if (response.isSuccessful && response.body() != null) {
//                    _registerState.value = Resource.success(response.body())
                    _clubPostRegisterState.value = Resource.success(response.body())
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
}