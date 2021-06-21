package com.wyl.testnewretrofitokhttp3.network

import android.net.Uri
import android.util.Log
import com.example.myapplication.network.util.Constant
import com.wyl.testnewretrofitokhttp3.network.converter.ResponseConverterFactory
import com.wyl.testnewretrofitokhttp3.network.dsl.RetrofitCoroutineDSL
import com.wyl.testnewretrofitokhttp3.network.request.MyRequest
import com.wyl.testnewretrofitokhttp3.network.util.GsonUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.IOException
import java.net.ConnectException
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class NetworkManager {
    private val TAG = "NetworkManager"
    private val retrofit: Retrofit
    private val request: MyRequest

    companion object {
        private var instance: NetworkManager? = null
        fun getInstance(): NetworkManager {
            if (instance == null) {
                synchronized(this) {
                    instance = NetworkManager()
                }
            }
            return instance!!
        }
    }

    init {
        // 初始化okhttp
        //初始化一个OKhttpClient
        val loggingInterceptor = HttpLoggingInterceptor { message: String? ->
            try {
                val text = Uri.decode(message)
                Log.d(TAG, text)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.d(TAG, message.toString())
            }
        }
        //设置日志Level
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val trustAllCerts = arrayOf<TrustManager>(
            object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
        )

        val sslContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        val sslSocketFactory = sslContext.socketFactory
        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .addNetworkInterceptor(loggingInterceptor) //（网络-okhttp core)
            .sslSocketFactory(sslSocketFactory, object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            })
            .hostnameVerifier { _, _ -> true }
            .build()

        // 初始化Retrofit
        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(ResponseConverterFactory.create(GsonUtil.gson))
            .build()
        request = retrofit.create(MyRequest::class.java)
    }

    fun getRequest(): MyRequest {
        return request
    }

}
fun <T> CoroutineScope.retrofit(dsl: RetrofitCoroutineDSL<T>.() -> Unit) {
    //在主线程中开启协程
    this.launch(Dispatchers.Main) {
        val coroutine = RetrofitCoroutineDSL<T>().apply(dsl)
        coroutine.api?.let { call ->
            //async 并发执行 在IO线程中
            val deferred = async(Dispatchers.IO) {
                try {
                    call.execute() //已经在io线程中了，所以调用Retrofit的同步方法
                } catch (e: ConnectException) {
                    coroutine.onFail?.invoke("网络连接出错", -1)
                    null
                } catch (e: IOException) {
                    coroutine.onFail?.invoke("未知网络错误", -1)
                    null
                }
            }
            //当协程取消的时候，取消网络请求
            deferred.invokeOnCompletion {
                if (deferred.isCancelled) {
                    call.cancel()
                    coroutine.clean()
                }
            }
            //await 等待异步执行的结果
//            val response = deferred.await()
//            if (response == null) {
//                coroutine.onFail?.invoke("返回为空", -1)
//            } else {
//                response.let {
//                    if (response.isSuccessful) {
//                        //访问接口成功
//                        if (response.body()?.status == 1) {
//                            //判断status 为1 表示获取数据成功
//                            coroutine.onSuccess?.invoke(response.body()!!.data)
//                        } else {
//                            coroutine.onFail?.invoke(response.body()?.msg ?: "返回数据为空", response.code())
//                        }
//                    } else {
//                        coroutine.onFail?.invoke(response.errorBody().toString(), response.code())
//                    }
//                }
//            }
//            coroutine.onComplete?.invoke()
        }
    }
}