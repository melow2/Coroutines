package com.khs.coroutines

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class MainVM :ViewModel(){
    private var userRepository = UserRepository()
    private var users:MutableLiveData<List<User>> = MutableLiveData()

    fun getUserData(){
        viewModelScope.launch {
            // Any coroutine launched in this scope will be automatically canceled if the ViewModel is cleared.
            var result:List<User>? = null
            withContext(Dispatchers.IO){
                result = userRepository.getUsers()
            }
            users.value = result
        }
    }
}