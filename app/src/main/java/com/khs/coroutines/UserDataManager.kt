package com.khs.coroutines

import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO

class UserDataManager {
    var count = 0
    lateinit var deferred:Deferred<Int>
    suspend fun getTotalUserCount():Int{
        coroutineScope {
            launch (IO){
                delay(1000)
                count = 50
            }
            val deferred  = async(IO) {
                delay(3000)
                return@async 70
            }
        }
        return count + deferred.await()
    }
}