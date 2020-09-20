package com.mankovskaya.githubtest.model.repository

import android.content.Context

//for now save only in runtime
class CredentialsHolder(context: Context) {

    var email: String? = null
    var password: String? = null

    fun saveCredentials(email: String, password: String) {
        this.email = email
        this.password = password
    }

    fun clearCredentials() {
        email = null
        password = null
    }

    fun credentialsSaved() = email != null && password != null
}
