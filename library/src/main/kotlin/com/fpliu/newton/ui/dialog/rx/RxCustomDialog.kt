package com.fpliu.newton.ui.dialog.rx

import android.app.Activity
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.text.TextUtils
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import com.fpliu.newton.ui.dialog.CustomDialog
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxCompoundButton
import com.jakewharton.rxbinding2.widget.RxTextView
import com.trello.rxlifecycle2.RxLifecycle
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.BehaviorSubject

open class RxCustomDialog(activity: Activity, theme: Int = android.R.style.Theme_Dialog) : CustomDialog(activity, theme) {

    private enum class DialogEvent {
        CREATE,
        START,
        SHOWING,
        DISMISS
    }

    private val lifecycleSubject = BehaviorSubject.create<DialogEvent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleSubject.onNext(DialogEvent.CREATE)
    }

    override fun onDismissed() {
        lifecycleSubject.onNext(DialogEvent.DISMISS)
        super.onDismissed()
    }

    fun <T> bindUntilDismiss() = RxLifecycle.bindUntilEvent<T, DialogEvent>(lifecycleSubject, DialogEvent.DISMISS)

    protected fun text(@IdRes textViewId: Int, text: CharSequence) {
        (findViewById(textViewId) as? TextView)?.text = text
    }

    protected fun text(textView: TextView, text: CharSequence) {
        textView.text = text
    }

    protected fun text(@IdRes textViewId: Int, @StringRes stringId: Int) {
        (findViewById(textViewId) as? TextView)?.text = context.getString(stringId)
    }

    protected fun text(textView: TextView, @StringRes stringId: Int) {
        textView.text = context.getString(stringId)
    }

    protected fun click(@IdRes viewId: Int): Observable<out Any> {
        return RxView.clicks(findViewById(viewId)).compose(bindUntilDismiss())
    }

    protected fun click(view: View): Observable<out Any> {
        return RxView.clicks(view).compose(bindUntilDismiss())
    }

    protected fun checkedThenEnabled(compoundButton: CompoundButton, view: View) {
        RxCompoundButton
            .checkedChanges(compoundButton)
            .compose(bindUntilDismiss())
            .subscribe(RxView.enabled(view))
    }

    protected fun checkedThenEnabled(@IdRes compoundButtonId: Int, @IdRes viewId: Int) {
        val view1 = findViewById(compoundButtonId) ?: return
        val view2 = findViewById(viewId) ?: return
        if (view1 is CompoundButton) {
            RxCompoundButton
                .checkedChanges(view1)
                .compose(bindUntilDismiss())
                .subscribe(RxView.enabled(view2))
        }
    }

    protected fun checkedThenEnabled(compoundButton: CompoundButton, @IdRes viewId: Int) {
        val view = findViewById(viewId) ?: return
        RxCompoundButton.checkedChanges(compoundButton)
            .compose(bindUntilDismiss())
            .subscribe(RxView.enabled(view))
    }

    protected fun checkedThenEnabled(@IdRes compoundButtonId: Int, view: View) {
        val view1 = findViewById(compoundButtonId) ?: return
        if (view1 is CompoundButton) {
            RxCompoundButton.checkedChanges(view1)
                .compose(bindUntilDismiss())
                .subscribe(RxView.enabled(view))
        }
    }

    protected fun notEmptyThenEnabled(textView: TextView, view: View) {
        afterTextChange(textView)
            .map { !TextUtils.isEmpty(it) }
            .subscribe(RxView.enabled(view))
    }

    protected fun notEmptyThenEnabled(@IdRes textViewId: Int, @IdRes viewId: Int) {
        afterTextChange(findViewById<TextView>(textViewId))
            .map { !TextUtils.isEmpty(it) }
            .subscribe(RxView.enabled(findViewById(viewId)))
    }

    protected fun notEmptyThenEnabled(textView: TextView, @IdRes viewId: Int) {
        afterTextChange(textView)
            .map { !TextUtils.isEmpty(it) }
            .subscribe(RxView.enabled(findViewById(viewId)))
    }

    protected fun notEmptyThenEnabled(@IdRes textViewId: Int, view: View) {
        afterTextChange(findViewById<TextView>(textViewId))
            .map { !TextUtils.isEmpty(it) }
            .subscribe(RxView.enabled(view))
    }

    protected fun afterTextChange(textView: TextView): Observable<String> {
        return RxTextView.afterTextChangeEvents(textView).compose(bindUntilDismiss()).map {
            it.editable()?.toString() ?: ""
        }
    }

    protected fun afterTextChange(@IdRes textViewId: Int): Observable<String> {
        return afterTextChange(findViewById<TextView>(textViewId))
    }

    protected fun editorActions(textView: TextView): Observable<Int> {
        return RxTextView.editorActions(textView).compose(bindUntilDismiss())
    }

    protected fun editorActions(@IdRes textViewId: Int): Observable<Int> {
        return RxTextView.editorActions(findViewById(textViewId)).compose(bindUntilDismiss())
    }

    protected fun checkedChange(compoundButton: CompoundButton): Observable<Boolean> {
        return RxCompoundButton.checkedChanges(compoundButton).compose(bindUntilDismiss())
    }

    protected fun checkedChange(@IdRes textViewId: Int): Observable<Boolean> {
        return RxCompoundButton.checkedChanges(findViewById(textViewId)).compose(bindUntilDismiss())
    }

    protected fun checked(compoundButton: CompoundButton): Consumer<in Boolean> {
        return RxCompoundButton.checked(compoundButton)
    }

    protected fun checked(@IdRes compoundButtonId: Int): Consumer<in Boolean> {
        return RxCompoundButton.checked(findViewById(compoundButtonId))
    }

    protected fun hint(textView: TextView): Consumer<in CharSequence> {
        return RxTextView.hint(textView)
    }

    protected fun hint(@IdRes textViewId: Int): Consumer<in CharSequence> {
        return RxTextView.hint(findViewById(textViewId))
    }

    protected fun enabled(view: View): Consumer<in Boolean> {
        return RxView.enabled(view)
    }

    protected fun enabled(@IdRes viewId: Int): Consumer<in Boolean> {
        return RxView.enabled(findViewById(viewId))
    }
}