# Kotilin Coroutines
## Concept
컴퓨터 공학에서 복수의 동시 과정을 관리하기 위한 두 종류의 멀티태스킹 방법이 있다.
하나의 유형으로 운영체제는 프로세스들 사이의 스위치를 제어한다.
또 다른 유형으로 프로세스가 스스로 자신의 행동을 제어하는 협동 멀티태스킹(cooperative multitasking)이라고 불린다.
코루틴은 협동 멀티태스킹을 위한 하위 루틴(sub routines)을 생성하는 소프트웨어 구성요소이다. 
비동기식으로 수행된 모든 작업(such as executors, HandlerThreads and IntentServices)들은 Coroutine으로 비교적 쉽고 효울적으로 수행할 수 있다. 
**Coroutine과 Thread는 동일하지 않다. 스레드는 동시에 실행가능한 많은 Coroutine을 가질 수 있으며, Coroutine는 Thread에서 실행되는 별도의 프로세서와 같다.** 

#
## Why we need asynchronous programming?
**대부분의 스마트폰은 최소 60Hz의 refresh 주파수를 가지고 있다.** 1초 동안 또는 1000밀리초 동안 앱이 60회 새로 고쳐진다.
1000밀리초를 60으로 나누자.16.666… 반복한다(*새로고침 1회당 16.6ms). 그래서 만약 우리 앱이 이 폰에서 실행된다면 대략 16초마다 메인 스레드를 사용하여 화면을 그려야 한다.
하지만 우리는 이미 시장에서 90Hz , 120hz와 같은 더 높은 재생 속도를 가진 더 나은 스마트폰을 가지고 있다. 
```
1. 90Hz: 1초당 90회 새로고침 (1회당 11ms)
2. 120Hz: 1초당 120회 새로고침 (1회당 8ms)
```
기본적으로 안드로이드 메인 스레드는 일련의 규칙적인 책임을 가지고 있다.항상 xml을 구문 분석하고 뷰 구성요소를 부풀려 매번 새로 고칠 때마다 반복해서 그려야 한다.
메인 스레드는 클릭 이벤트와 같은 사용자 상호 작용도 다루어야 한다.따라서 메인 스레드에 작업을 더 추가하면 실행 시간이 초소수 시간 차이를 초과하면 앱에 성능 오류가 표시된다. 
화면이 정지될 수 있다. 사용자에게 예측할 수 없는 동작신 보기 구성 요소가 나타난다. 그것은 심지어 응용 프로그램이 응답하지 않는 오류를 일으킬 것이다.
기술 진보의 결과로, 이러한 새로 고침 비율은 매년 점점 더 높아지고 있다.그러므로 안드로이드 개발자로서 우리는 항상 별도의 스레드에서 비동기적으로 장시간 실행되는 작업을 실행하도록 노력해야 한다. 
그것을 달성하기 위해, 오늘날 우리가 가지고 있는 가장 최신, 가장 효율적이고 효과적인 기술은 Kotilin Coroutine이다.

#
## How it is used?
Kotilin Coroutine에서는 범위 내의 모든 코루틴을 시작해야 한다. 스코프에 속하는 속성을 사용하면 Coroutine을 쉽게 추적하고 Coroutine을 취소하며 Coroutine이 던지는 오류나 예외를 처리할 수 있다.
이 CoroutineScope는 Coroutine에 대한 범위를 제공하기 위해 사용했던 인터페이스이다.Kotlin Koroutines에는 GlobalScope라는 또 다른 스코프 인터페이스가 있다. 글로벌 스코프는 전체 애플리케이션 수명 동안 작동하는 최상위 Coroutine을 출시하는 데 사용된다. 
안드로이드 개발에서 우리는 GlobalScope 를 거의 사용하지 않는다.

이 두 범위 모두 코루틴 context를 참조하는 역할도 한다. 이것이 코루틴스코프의 context이다. 이런 맥락에서 우리는 오직 dispatcher를 context으로 사용했을 뿐이다. 명시적 작업 인스턴스를 사용하려면 Job 인스턴스 이름과 Dispatcher를 범위의 컨텍스트에 포함할 수 있다.
더하기 연산자는 여러 코루틴 컨텍스트를 병합하는 데 사용할 수 있으며, **dispatcher는 코루틴이 실행되어야 하는 스레드의 종류를 설명한다. Kotlin Android 구조화된 동시성에서는 항상 메인 스레드를 사용하여 코루틴을 시작한 다음 백그라운드 스레드로 전환하는 것이 권장된다. 
메인 스레드에서 코루틴을 실행하기 위해서는 Dispatcher.Main을 사용한다. 우리는 또한 Dispatcers.IO, Dispatchers.Default, Dispatchers.Unconfined 을 갖고 있기도 하다.**

스레드를 변경하기 위한 코드를 작성하지 않은 경우. 안드로이드 개발자로서 우리는 대부분 Dispatcers.IO,Dispatcers.Main 2가지를 사용한다.

### Dispatcher.Main
우리는 Dispatcher.Main을 비교적 작고 가벼운 작업(call to a ui function, call to a suspending function or to get updates from the livedata)에만 사용한다.
구조화된 동시성(In structured concurrency)에서는 항상 메인스레드에서 코루틴을 시작하고 나중에 코루틴을 background thread로 전환하는 것이 권장된다.
### Dispatcher.IO
백그라운드 스레드(from a shared pool of on-demand created threads)에서 실행되며, 우리는 로컬 데이터베이스와 통신하고 파일을 처리할 수도 있다.
### Dispatcher.Default
10000개의 목록 항목으로 데이터 목록을 정렬하거나 영화 10만개의 세부 정보가 포함된 거대한 JSON 파일을 구문 분석하는 것과 같은 CPU 집약적인 작업에 사용된다.
### Dispatcher.Uncomfined
GlobalScope와 함께 사용되는 dispatcher이다. Unconfined Coroutine은 현재 스레드에서 실행되지만,만약 코루틴이 일시 중단되었다 재개되면,일시 중단 기능이 실행 중인 스레드에서 실행된다. 
안드로이드 개발에는 이 디스패쳐를 사용하지 않는 것이 좋다.

#
## Suspend & Delay Function
```
btnDownloadUserData.setOnClickListener {
    CoroutineScope(Dispatchers.IO).launch {
        downloadUserData()
    }
}

private suspend fun downloadUserData() {
    for (i in 1..200000) {
        withContext(Dispatchers.Main){
            tvUserMessage.text = "Downloading user $i in ${Thread.currentThread().name}"
        }
        delay(3000)
    }
// MainThread에서 하나의 코루틴을 시작하고, 이것을 다른 SubThread1로 보내고, 또 SubThread2로 보냈다가 다시 MainThread 에서 작업을 하는게 가능하다. 
// 이렇게 컨텍스트 스위칭을 해주는 것이 withContext의 역할이다.
}
```
백그라운드 스레드에서 UI thread의 데이터 바인딩에 직접적으로 접근할 수 없기때문에 withContext를 사용하여 접근한다.
이는 runOnUiThread()와 유사하고, 반드시 suspend 키워드를 붙여야 하며, 메소드는 코루틴안에서 실행되어야 한다. 

Suspend 함수는 그 함수가 비동기 환경(Asynchronous)에서 사용될 수 있다는 의미를 내포한다. 비동기 함수인 suspend 함수는 다른 suspend 함수, 혹은 코루틴 내에서만 호출할 수 있다.
작업을 완료한 후 기능이 재개되면 스택 프레임이 저장된 위치에서 다시 복사되어 다시 실행되기 시작한다.
많은 suspend function들이 있는데, 코루틴 라이브러리 뿐만 아니라, room과 retrofit과 같은 라이브러리에서도 코루틴과 상호작용할 수 있도록 많은 suspend function들을 제공한다.
그리고 또한 우리가 만든 suspend function을 사용하려면 , suspend modifier를 사용해야하며, 코루틴에 대해서만 기능 사용을 사실상 제한하고 있다.
suspend function은 코루틴 블록이나 suspend function에서만 호출 할 수 있다. 
suspend function은 thread를 블럭하는 것이 아니라, 코루틴 자체를 suspend하는 것이다. 코루틴이 wait하는 동안 스레드는 thread pool로 돌아오고, 코루틴의 wait이 끝나면,
코루틴은 pool의 free thread에서 다시 재개한다.

#
## Async & Await
4개의 온라인 데이터 소스에서 결과를 가져와 모두 결합하여 사용자에게 최종 결과를 보여줘야 한다고 가정해 봅시다.
```
첫 번째 작업에는 10초가 소요됨
두 번째 작업은 15초가 소요됨
세 번째 작업은 12초가 걸린다.
그리고 네 번째 과제는 13초가 걸린다.

CoroutineScope(Dispatchers.IO).launch {
    Log.i("MyTag","Calculation started..")
    val stock1 = getStock1()
    val stock2 = getStock2()
    val total = stock1 + stock2
    Log.i("MyTag","Total is $total")
}

private suspend fun getStock1() :Int {
    delay(10000)
    Log.i("MyTag","stock 1 returned ")
    return 55000
}

private suspend fun getStock2() :Int {
    delay(8000)
    Log.i("MyTag","stock 2 returned ")
    return 35000
}

```
우리는 이 데이터 세트를 하나씩 다운로드 하기 위해 코드를 쉽게 쓸 수 있다.하지만 그렇게 한다면 사용자는 최종 결과를 보기 위해 최소 50초 이상 기다려야 한다.
이 모든 데이터를 병렬로 다운로드할 수 있다면 어떨까? 만약 우리가 그렇게 할 수 있다면 우리는 단 15초 만에 결과를 보여줄 수 있을 것이다.
이러한 데이터를 병렬로 다운로드하여 마지막에 결합하는 코드를 작성하는 것을  parallel decomposition 라고 한다. parallel decomposition는 그렇게 쉽지 않았다.
우리는 복잡하고, 길고, 읽기 어렵고, 코드를 유지하기가 힘들었다. 그러나 코틀린 코루틴을 사용하면 우리는 parallel decomposition는를 매우 쉽게 할 수 있다.
```
// 가장먼저 getStock2()가 반환. 
// 10초만에 결과 값을 받을 수 있음. 
CoroutineScope(Dispatchers.IO).launch {
    Log.i("MyTag", "Calculation started..")
    val stock1 = async { getStock1() }
    val stock2 = async { getStock2() }
    val total = stock1.await()+ stock2.await()
    Log.i("MyTag", "Total is $total")
}
   
// 메인 스레드에서 실행.
// async 부분만 백그라운드에서 실행.
CoroutineScope(Main).launch {
    Log.i("MyTag", "Calculation started..")
    val stock1 = async(IO) { getStock1() }
    val stock2 = async(IO) { getStock2() }
    val total = stock1.await()+ stock2.await()
    Log.i("MyTag", "Total is $total")
    Toast.makeText(applicationContext,"Total is $total",Toast.LENGTH_SHORT).show()
}

```
### Lazily Started Async
우리는 코루틴을 병렬로 실행하기 위해 비동기식 빌더를 사용한다. 하지만 때때로 우리는 그것을 즉시 시작하고 싶지 않다. 때로는 비동기 코루틴 실행을 시작하기 전에 몇 가지 단계가 완료될 때까지 기다려야 할 수도 있다.
```
// await 함수가 실행되면 자동으로 실행 된다. 
CoroutineScope(IO).launch {
    Log.i("MyTag", "Calculation started..")
    val stock1 = async(start = CoroutineStart.LAZY) { getStock1() }
    val stock2 = async(start = CoroutineStart.LAZY) { getStock2() }
    delay(4000)
    Log.i("MyTag", "*** Just Before await ***")
    val total = stock1.await()+ stock2.await()
    Log.i("MyTag", "Total is $total")
}
```

#
## Time Outs
```
// 3초 이후에 context switch.
CoroutineScope(Main).launch {
    withTimeout(3000){
        withContext(IO){
            for(i in 1..10000){
                delay(100)
                Log.i("MyTag", "is $i")
            }
        }
    }
}
```
withTimeOut()은 시간 초과 시 예외가 발생하므로 try,catch로 잡아줘야한다. 예외를 두지 않으려면 withTimeoutOrNull()을 사용하면 된다.
withTimeoutOrNull()은 시간 초과 시  null을 반환하고, withTimeoutOrNull()이든 withTimeOut()이던 모두 suspend function을 포함하고 있다. 


#
## Run Blocking Builder
```
Log.i("MyTag", "Start")
runBlocking {
    for(i in 1..10){
        delay(100)
        Log.i("MyTag", "$i")
    }
}
Log.i("MyTag", "End")
```
suspend가 아니라 현재 스레드에 대한 blocking이기 때문에 ANR위험이 있어 사용하지 않는 것이 좋다.


#
## Job Of a Coroutine
간단한 예로 장난감 자동차를 생각한다면, 우리는 장난감 자동차의 현재 상태를 알 수 있는 컨트롤러를 가지고 있다.
우리는 필요할 때 차를 멈추기 위해 컨트롤러를 사용할 수도 있다. 그것처럼 모든 코루틴은 그것만의 Jop Instance를 가지고 있다.
우리는 그 Job Instance를 이용하여 코루틴에 대해 알고 필요할 때마다 코루틴을 취소할 수 있다. 
하나의 job은 6개의 status를 갖는다.
```
New, Active, Completing, Cancelling, Cancelled, and Completed.
```
우리는 그들을 이용해서 코루틴의 상태를 알 수 있다. 코루틴을 취소하려면, job의 cancel() 함수를 호출할 수 있다. 코루틴이 작동하는 동안 job은 활성 상태이다.

```
private lateinit var job1:Job
lateinit var deferred1:Deferred<Int>

job1 = CoroutineScope(Main).launch {
    deferred1 = async {  downloadData2() }
    returnedValue = deferred1.await()
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
```

### Unstructured Concurrency
```
// 결과값은 70
class UserDataManager {
    var count = 0
    lateinit var deferred:Deferred<Int>
    suspend fun getTotalUserCount():Int{
        CoroutineScope(IO).launch {
            delay(1000)
            count = 50
        }
        deferred = CoroutineScope(IO).async {
            delay(3000)
            return@async 70
        }
        return count + deferred.await()
    }
}
```

### Structured Concurrency
```
// 결과값은 120 
class UserDataManager {
    var count = 0
    lateinit var deferred:Deferred<Int>
    suspend fun getTotalUserCount():Int{
        coroutineScope {
            launch (IO){
                delay(1000)
                count = 50
            }
            deferred  = async(IO) {
                delay(3000)
                return@async 70
            }
        }
        return count + deferred.await()
    }
}
```
### Join Function
```
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
```
join function은 job이 끝날때까지 순서를 보장해준다.

#
## Coroutine in ViewModelScope
```
class UserRepository {
    suspend fun getUsers() : List<User>{
        delay(5000)
        val users:List<User> = listOf(
            User(1,"Sam1"),
            User(2,"Sam2"),
            User(3,"Sam3"),
            User(4,"Sam4")
        )
        return users
    }
}

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
```
#
## Coroutine in LifecycleScope
Activity나 Fragment와 같은 LifeCycle이 있는 object에서 Coroutin을 생성해야한다.
라이프사이클이 파괴되면 이 새로운 범위의 모든 코루틴이 취소된다.그래서 만약 그것이 Activity라면, 그 범위의 모든 코루틴은 그 것의 onDestroy 방식일 때 취소될 것이다.
Fragment일 경우 Fragment의 onDestroy 방식이 발동되면 Fragment 범위 내 모든 코루틴이 취소된다. 따라서 수동으로 Job를 만들고, Job을 스코프의 컨텍스트에 추가하며, 
코루틴을 지우기 위해 ondestroy 메소드를 재정의하고 코드를 작성할 필요가 없다. 그 모든 것은 자동으로 처리될 것이다.
### In Fragment
```
class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
        lifecycleScope.launch(Dispatchers.IO) {
            Log.i("MyTag", " thread is : ${Thread.currentThread().name}")
        }
    }
}
```
### In Activity
```
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }

        lifecycleScope.launch(Dispatchers.IO) {
              Log.i("MyTag", " thread is : ${Thread.currentThread().name}")
        }

        lifecycleScope.launchWhenCreated{

        }

        lifecycleScope.launchWhenStarted {

        }

        lifecycleScope.launchWhenResumed {


        }
    }
}
```
#
## Coroutine in LiveData
```
class MainVM :ViewModel(){
    private var userRepository = UserRepository()
    private var users = liveData(Dispatchers.IO){
        val result = userRepository.getUsers()
        emit(result)
    }
}
```


#
## Coroutine in RoomDB
```
@Dao
interface SubscriberDAO {

    @Insert
    suspend fun insert(subscriber: Subscriber)

    @Query("DELETE FROM subscriber_data_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM subscriber_data_table")
    fun getAllSubscribers() : LiveData<List<Subscriber>>
    // Room은 우리가 직접 테이블의 데이터를 목록으로 실시간 데이터로서 가져올 수 있게 해준다. 
    // 메인 스레드에서 라이브 데이터를 직접 관찰할 수 있기 때문에 이 기능을 suspend 함수로 쓸 필요가 없다
}
```
