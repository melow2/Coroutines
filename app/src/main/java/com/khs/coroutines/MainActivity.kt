package com.khs.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

class MainActivity : AppCompatActivity() {
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnCount.setOnClickListener {
            tvCount.text = count++.toString()

        }

        btnDownloadUserData.setOnClickListener {
/*            CoroutineScope(Dispatchers.IO).launch {
                downloadUserData()
            }*/
            CoroutineScope(Main).launch {
                Log.i("MyTag", "Calculation started..")
                val stock1 = async(IO) { getStock1() }
                val stock2 = async(IO) { getStock2() }
                val total = stock1.await()+ stock2.await()
                Log.i("MyTag", "Total is $total")
                Toast.makeText(applicationContext,"Total is $total",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private suspend fun downloadUserData() {
        for (i in 1..200000) {
            withContext(Dispatchers.Main) {
                tvUserMessage.text = "Downloading user $i in ${Thread.currentThread().name}"
            }
            delay(3000)
        }
    }

    private suspend fun getStock1(): Int {
        delay(10000)
        Log.i("MyTag", "stock 1 returned ")
        return 55000
    }

    private suspend fun getStock2(): Int {
        delay(8000)
        Log.i("MyTag", "stock 2 returned ")
        return 35000
    }

}