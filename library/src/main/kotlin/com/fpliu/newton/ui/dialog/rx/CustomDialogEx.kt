package com.fpliu.newton.ui.dialog.rx

import com.fpliu.newton.ui.dialog.CustomDialog

fun CustomDialog.scope() = DialogScopeProvider.from(this)
