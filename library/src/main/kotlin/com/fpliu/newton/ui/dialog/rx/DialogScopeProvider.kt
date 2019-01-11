package com.fpliu.newton.ui.dialog.rx

import android.content.DialogInterface
import com.fpliu.newton.ui.dialog.CustomDialog
import com.uber.autodispose.ScopeProvider
import io.reactivex.CompletableObserver
import io.reactivex.CompletableSource
import io.reactivex.android.MainThreadDisposable

/**
 * A [ScopeProvider] that can provide scoping for Android [CustomDialog] classes.
 * <pre>`
 * AutoDispose.autoDisposable(DialogScopeProvider.from(dialog));
`</pre> *
 */
class DialogScopeProvider private constructor(private val dialog: CustomDialog) : ScopeProvider {

    companion object {
        fun from(dialog: CustomDialog) = DialogScopeProvider(dialog)
    }

    override fun requestScope(): CompletableSource = DetachEventCompletable(dialog)

    internal class DetachEventCompletable(
        private val customDialog: CustomDialog
    ) : CompletableSource {

        override fun subscribe(co: CompletableObserver) {
            Listener(customDialog, co).let {
                co.onSubscribe(it)
                customDialog.setOnDismissListener(it)
            }
        }

        internal class Listener(
            private val customDialog: CustomDialog,
            private val co: CompletableObserver
        ) : MainThreadDisposable(), DialogInterface.OnDismissListener {

            override fun onDismiss(dialog: DialogInterface) {
                if (!isDisposed) {
                    co.onComplete()
                }
            }

            override fun onDispose() {
                customDialog.removeOnDismissListener(this)
            }
        }
    }
}