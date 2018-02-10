package com.circleVi.basekotlin.common.extensions

import com.circleVi.basekotlin.common.model.IntervalGenerator
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun Disposable.addToCompositeDisposable(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun <T> Observable<T>.applyScheduler(): Observable<T> {
    return this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.applyScheduler(): Single<T> {
    return this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Completable.applyScheduler(): Completable {
    return this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Maybe<T>.applyScheduler(): Maybe<T> {
    return this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> Flowable<T>.applyScheduler(): Flowable<T> {
    return this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> ObservableEmitter<T>.checkDisposed(): ObservableEmitter<T>? {
    return if (this.isDisposed) {
        null
    } else {
        this
    }
}

fun <T> SingleEmitter<T>.checkDisposed(): SingleEmitter<T>? {
    return if (this.isDisposed) {
        null
    } else {
        this
    }
}

fun <T> CompletableEmitter.checkDisposed(): CompletableEmitter? {
    return if (this.isDisposed) {
        null
    } else {
        this
    }
}

fun <T> MaybeEmitter<T>.checkDisposed(): MaybeEmitter<T>? {
    return if (this.isDisposed) {
        null
    } else {
        this
    }
}

fun <T> Observable<T>.retryInterval(maxRetry: Int): Observable<T> {
    return this.retryWhen(RetryWithDelay(maxRetry))
}

fun <T> Observable<T>.retryInterval(maxRetry: Int, timeInterval: Long): Observable<T> {
    return this.retryWhen(RetryWithDelay(maxRetry, timeInterval))
}

private class RetryWithDelay : Function<Observable<Throwable>, Observable<*>> {

    private val interval: IntervalGenerator
    private val maxRetries: Int
    private var retryCount: Int

    constructor(maxRetries: Int) {
        this.maxRetries = maxRetries
        this.retryCount = 0
        interval = IntervalGenerator()
    }

    constructor(maxRetries: Int, timeInterval: Long) {
        this.maxRetries = maxRetries
        this.retryCount = 0
        this.interval = IntervalGenerator(timeInterval)
    }

    override fun apply(attempts: Observable<Throwable>): Observable<*> {
        return attempts.flatMap(Function<Throwable, Observable<*>> { throwable ->
            if (++retryCount < maxRetries) {
                return@Function Observable.timer(interval.next(), TimeUnit.MILLISECONDS)
            }
            Observable.error<Throwable>(throwable)
        })
    }
}