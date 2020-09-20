package com.mankovskaya.githubtest.ui.common

import com.mankovskaya.githubtest.R
import com.mankovskaya.githubtest.data.network.error.ConnectionError

fun Throwable.getDefaultMessageId() =
    if (this is ConnectionError) R.string.error_connection else R.string.error_default
