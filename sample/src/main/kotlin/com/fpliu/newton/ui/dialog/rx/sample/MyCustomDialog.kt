package com.fpliu.newton.ui.dialog.rx.sample

import android.app.Activity
import android.os.Bundle
import com.fpliu.newton.log.Logger
import com.fpliu.newton.ui.dialog.CustomDialog
import com.fpliu.newton.ui.dialog.rx.scope
import com.uber.autodispose.autoDisposable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class MyCustomDialog(activity: Activity) : CustomDialog(activity) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Observable
            .interval(1, TimeUnit.SECONDS)
            .doOnDispose {
                Logger.i("XX", "doOnDispose()")
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .autoDisposable(scope())
            .subscribe {
                Logger.i("XX", "onNext()")
            }
    }

    override fun onDismissed() {
        super.onDismissed()
        Logger.i("XX", "onDismissed()")
    }
}