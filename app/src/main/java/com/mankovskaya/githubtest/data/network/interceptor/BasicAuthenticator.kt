package com.mankovskaya.githubtest.data.network.interceptor

import com.mankovskaya.githubtest.data.repository.CredentialsHolder
import okhttp3.Credentials
import okhttp3.Interceptor

class BasicAuthenticator(private val credentialsRepository: CredentialsHolder) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        if (credentialsRepository.email == null || credentialsRepository.password == null) return chain.proceed(
            chain.request()
        )
        val credentials: String = Credentials.basic(
            credentialsRepository.email.orEmpty(),
            credentialsRepository.password.orEmpty()
        )
        request = request.newBuilder().header("Authorization", credentials).build()
        return chain.proceed(request)
    }
}