package com.circleVi.basekotlin.utils.threadpool

import java.util.concurrent.*

class DefaultExecutor private constructor() : IExecutor {
    companion object {
        private val MAX_POOL_SIZE = 2 * Runtime.getRuntime().availableProcessors() + 1

        private val CORE_POOL_SIZE = 0

        private val TIME_A_LIVE = 5

        private var sInstance: DefaultExecutor? = null

        @Synchronized
        fun getInstance(): IExecutor {
            if (sInstance == null) {
                sInstance = DefaultExecutor()
            }
            return sInstance!!
        }
    }

    private val mMainExecutor: Executor by lazy { MainThreadExecutor() }

    private val mThreadPoolExecutor: ThreadPoolExecutor by lazy {
        ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, TIME_A_LIVE.toLong(),
                TimeUnit.SECONDS, LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory())
    }

    override fun threadPoolExecutor(): ThreadPoolExecutor = mThreadPoolExecutor

    override fun newSingleThreadExecutor(): Executor = Executors.newSingleThreadExecutor()

    override fun mainTaskExecutor(): Executor = mMainExecutor
}
