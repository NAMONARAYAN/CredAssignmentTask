package com.app.credassignmenttask.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
class UriDataModel (
    var id:Int?,
    var url: Uri?): Parcelable {

}

class UriConverters {
    fun fromString(value: String?): Uri? {
        return if (value == null) null else Uri.parse(value)
    }
    fun toString(uri: Uri?): String? {
        return uri.toString()
    }
}