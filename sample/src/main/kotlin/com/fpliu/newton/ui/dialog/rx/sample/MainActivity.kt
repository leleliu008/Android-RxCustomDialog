package com.fpliu.newton.ui.dialog.rx.sample

import android.os.Bundle
import android.view.Gravity
import androidx.lifecycle.Lifecycle
import com.fpliu.newton.ui.base.BaseActivity
import com.jakewharton.rxbinding3.view.clicks
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 自定义弹出框使用示例
 * @author 792793182@qq.com 2018-03-28.
 */
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "自定义弹出框使用示例"
        setContentView(R.layout.activity_main)

        xxBtn
            .clicks()
            .autoDisposable(AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY))
            .subscribe {
                MyCustomDialog(this).show(Gravity.CENTER, 0, 0)
            }
    }
}