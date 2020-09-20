package com.mankovskaya.githubtest.data.network.interceptor

import android.content.Context
import android.net.ConnectivityManager
import com.mankovskaya.githubtest.data.network.error.ConnectionError
import com.mankovskaya.githubtest.data.network.error.UnauthorizedError
import okhttp3.Interceptor

class ErrorInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        if (!isConnected()) {
            throw ConnectionError()
        }

        var request = chain.request()
        val response = chain.proceed(request)
        if (response.code == 401 || response.code == 403) throw UnauthorizedError()
        return response
    }

    private fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetworkInfo
        return network?.isConnected ?: false
    }
}