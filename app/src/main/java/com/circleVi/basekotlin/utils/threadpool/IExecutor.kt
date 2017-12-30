package com.circleVi.basekotlin.utils.threadpool

import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor

interface IExecutor {
    fun threadPoolExecutor(): ThreadPoolExecutor

    fun newSingleThreadExecutor(): Executor

    fun mainTaskExecutor(): Executor
}
