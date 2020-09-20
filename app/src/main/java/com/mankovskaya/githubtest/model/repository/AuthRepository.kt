package com.mankovskaya.githubtest.model.repository

import com.mankovskaya.githubtest.model.network.AuthApi
import com.mankovskaya.githubtest.model.network.UnauthorizedError
import com.mankovskaya.githubtest.system.scheduler.SchedulersProvider
import io.reactivex.Completable
import io.reactivex.Single

class AuthRepository(
    private val authApi: AuthApi,
    private val credentialsRepository: CredentialsRepository,
    schedulers: SchedulersProvider
) : BaseRepository(schedulers) {

    fun login(email: String, password: String) =
        Completable.fromAction {
            credentialsRepository.email = email
            credentialsRepository.password = password
        }
            .andThen(authApi.getUser())
            .toSingleDefault(true)
            .onErrorResumeNext {
                if (it is UnauthorizedError) Single.just(false)
                else Single.error(it)
            }
            .schedule()

}