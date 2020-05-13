@file:JvmName("AppUtils")

package com.app.credassignmenttask.utils

import android.content.Context
import android.content.res.Resources
import android.net.ConnectivityManager




fun dpToPx(dp: Int): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}


fun isConnected(context: Context?): Boolean {
    val connectivityManager =
        context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkInfo = connectivityManager.activeNetworkInfo
    return networkInfo != null && networkInfo.isConnected
}

