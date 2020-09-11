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
    private lateinit var job1:Job
    lateinit var deferred1:Deferred<Int>
    var returnedValue :Int = 0
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
    /*        CoroutineScope(IO).launch {
                Log.i("MyTag", "Calculation started..")
                val stock1 = async(start = CoroutineStart.LAZY) { getStock1() }
                val stock2 = async(start = CoroutineStart.LAZY) { getStock2() }
                delay(4000)
                Log.i("MyTag", "*** Just Before await ***")
                val total = stock1.await()+ stock2.await()
                Log.i("MyTag", "Total is $total")
            }*/

/*            CoroutineScope(Main).launch {
                withTimeout(3000){
                    withContext(IO){
                        for(i in 1..10000){
                            delay(100)
                            Log.i("MyTag", "is $i")
                        }
                    }
                }
            }*/

/*            Log.i("MyTag", "Start")
            runBlocking {
                for(i in 1..10){
                    delay(100)
                    Log.i("MyTag", "$i")
                }
            }
            Log.i("MyTag", "End")*/

/*            job1 = CoroutineScope(Main).launch {
                tvUserMessage.text = UserDataManager().getTotalUserCount().toString()
            }*/

        }

/*        btn_cancel.setOnClickListener {
            job1.cancel()
        }

        btn_status.setOnClickListener {
            if(deferred1.isActive){
                Toast.makeText(applicationContext,"Active",Toast.LENGTH_SHORT).show()
            }else if(deferred1.isCancelled){
                Toast.makeText(applicationContext,"Canceled",Toast.LENGTH_SHORT).show()
            }else if(deferred1.isCompleted){
                Toast.makeText(applicationContext,"Completed",Toast.LENGTH_SHORT).show()
            }
        }*/
    }

    private suspend fun downloadData(){
        withContext(IO){
            repeat(30){
                delay(100)
                Log.i("MyTag","repeating $it")
            }
        }
    }

    private suspend fun downloadData2(): Int {
        var i = 0
        withContext(IO){
            repeat(30){
                delay(1000)
                Log.i("MyTag","repeating $it")
                i++
            }
        }
        return i
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
        Log.i("MyTag", "Counting stock 1 started")
        delay(2000)
        Log.i("MyTag", "stock 1 returned ")
        return 55000
    }

    private suspend fun getStock2(): Int {
        Log.i("MyTag", "Counting stock 2 started")
        delay(1000)
        Log.i("MyTag", "stock 2 returned ")
        return 35000
    }

}