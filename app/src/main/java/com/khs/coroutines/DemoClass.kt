package com.khs.coroutines

import android.util.Log
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DemoClass {
    suspend fun test1() {
        coroutineScope {
            Log.i("MyTag", "Top ${Thread.currentThread().name}")
            val job = launch(IO) {
                Log.i("MyTag", "Before Delay ${Thread.currentThread().name}")
                delay(1000)
                Log.i("MyTag", "After Delay ${Thread.currentThread().name}")
            }
            job.join()
            Log.i("MyTag", "Bottom ${Thread.currentThread().name}")
        }
    }
}