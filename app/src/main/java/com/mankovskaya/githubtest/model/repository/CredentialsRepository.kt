package com.mankovskaya.githubtest.model.repository

import android.content.Context

class CredentialsRepository(context: Context) {

    private val preferences = context.getSharedPreferences("GitHubPreferences", Context.MODE_PRIVATE)

    var email: String?
        get() = preferences.getString(USER_EMAIL, null)
        set(value) {
            preferences.edit().putString(USER_EMAIL, value).apply()
        }

    var password: String?
        get() = preferences.getString(USER_PASSWORD, null)
        set(value) {
            preferences.edit().putString(USER_PASSWORD, value).apply()
        }

    companion object {
        private const val USER_EMAIL = "USER_EMAIL"
        private const val USER_PASSWORD = "USER_PASSWORD"
    }
}
