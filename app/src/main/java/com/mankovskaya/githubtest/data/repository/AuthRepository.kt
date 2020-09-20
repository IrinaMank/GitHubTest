package com.mankovskaya.githubtest.data.repository

import com.mankovskaya.githubtest.core.android.BaseRepository
import com.mankovskaya.githubtest.data.network.api.AuthApi
import com.mankovskaya.githubtest.data.network.error.UnauthorizedError
import com.mankovskaya.githubtest.system.scheduler.SchedulersProvider
import io.reactivex.Completable
import io.reactivex.Single

class AuthRepository(
    private val authApi: AuthApi,
    private val credentialsRepository: CredentialsHolder,
    schedulers: SchedulersProvider
) : BaseRepository(schedulers) {

    fun login(email: String, password: String) =
        Completable.fromAction {
            credentialsRepository.saveCredentials(email, password)
        }
            .andThen(authApi.getUser())
            .toSingleDefault(true)
            .onErrorResumeNext {
                if (it is UnauthorizedError) {
                    logout()
                    Single.just(false)
                } else Single.error(it)
            }
            .schedule()

    fun userLogged(): Boolean = credentialsRepository.credentialsSaved()

    fun logout() {
        credentialsRepository.clearCredentials()
    }

}