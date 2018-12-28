package com.okatanaa.timemanager.viewmvp

import android.os.Bundle
import android.view.View

interface ViewMvp {
    fun getRootView(): View
    fun getViewState(): Bundle
    fun showToast(msg: String, length: Int)
}