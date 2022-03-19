package com.iamcheng5.firestorerecyclerviewdemo_mvvm

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class PostViewModel() : ViewModel() {
    private val coroutineContext: CoroutineContext =
        viewModelScope.coroutineContext + Dispatchers.IO
    private val repository: PostRepository = PostRepository()
    //get data once
    /*
    fun getPosts(): LiveData<Result<List<Post>>> = liveData(coroutineContext) {
        emit(Result.Loading)
        val result = repository.getPosts()
        if (result is Result.Success && result.data.isEmpty())
            emit(Result.Error(Exception("Data is empty.")))
        else
            emit(result)
    }*/

    fun addPost(post:Post): LiveData<Result<Boolean>> = liveData(coroutineContext) {
        emit(Result.Loading)
        emit(repository.addPost(post))
    }

    fun removePost(post:Post): LiveData<Result<Boolean>> = liveData(coroutineContext) {
        emit(Result.Loading)
        emit(repository.removePost(post))
    }
    //listen for realtime updates
    fun getPosts(): LiveData<List<Post>> =repository.getPosts()
}
