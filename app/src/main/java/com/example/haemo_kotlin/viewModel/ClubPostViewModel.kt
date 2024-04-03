package com.example.haemo_kotlin.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.haemo_kotlin.model.HangulUtils
import com.example.haemo_kotlin.model.post.ClubPostModel
import com.example.haemo_kotlin.model.post.containsHangulSearch
import com.example.haemo_kotlin.network.Resource
import com.example.haemo_kotlin.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlin.collections.joinToString
import javax.inject.Inject


@HiltViewModel
class ClubPostViewModel @Inject constructor(
    private val repository: PostRepository
) : ViewModel() {

    private val _clubPostList = MutableStateFlow<List<ClubPostModel>>(emptyList())
    val clubPostList: StateFlow<List<ClubPostModel>> = _clubPostList.asStateFlow()

    private val _clubPost = MutableStateFlow<ClubPostModel?>(null)
    val postModel: StateFlow<ClubPostModel?> = _clubPost.asStateFlow()

    private val _clubPostState = MutableStateFlow<Resource<ClubPostModel>>(Resource.loading(null))
    val clubPostState: StateFlow<Resource<ClubPostModel>> = _clubPostState.asStateFlow()

    private val _clubPostListState =
        MutableStateFlow<Resource<List<ClubPostModel>>>(Resource.loading(null))
    val clubPostListState: StateFlow<Resource<List<ClubPostModel>>> =
        _clubPostListState.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    init {
        viewModelScope.launch {
            getClubPost()
        }
    }

    fun isHangul(c: Char): Boolean {
        return c.toInt() in 0xAC00..0xD7A3 || c in 'ㄱ'..'ㅣ'
    }

    fun getHangulInitialSound(c: Char): Char {
        val init = ((c.toInt() - 0xAC00) / 28 / 21).toChar()
        return if (init in 'ㄱ'..'ㅎ') init + '가'.toInt() - 'ㄱ'.toInt() else init
    }

    fun filterPostsByTitle(searchText: String): List<ClubPostModel> {
        return _clubPostList.value.filter { post ->
            post.containsHangulSearch(searchText)
        }
    }

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

    suspend fun getClubPost() {
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

    suspend fun getOnePost(idx: Int) {
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
}