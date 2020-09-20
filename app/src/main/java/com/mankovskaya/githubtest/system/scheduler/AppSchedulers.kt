package com.mankovskaya.githubtest.system.scheduler

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Manages thread work. Contains function, that return thread, suitable for operation
 */
class AppSchedulers() : SchedulersProvider {

    override fun ui(): Scheduler = AndroidSchedulers.mainThread()

    override fun computation(): Scheduler = Schedulers.computation()

    override fun trampoline(): Scheduler = Schedulers.trampoline()

    override fun newThread(): Scheduler = Schedulers.newThread()

    override fun io(): Scheduler = Schedulers.io()

}
