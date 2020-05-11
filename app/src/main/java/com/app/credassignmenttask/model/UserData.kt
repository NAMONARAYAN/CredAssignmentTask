package com.app.credassignmenttask.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserData(@PrimaryKey(autoGenerate = true)
                    @ColumnInfo(name = "id")
                    var id:Int?,
                    @ColumnInfo(name = "fname")
                    val fname: String?,
                    @ColumnInfo(name = "lname")
                    val lname: String?,
                    @ColumnInfo(name = "email")
                    val email: String?,
                    @ColumnInfo(name = "phone")
                    val phone: String?,
                    @ColumnInfo(name = "description")
                    val description: String?):Parcelable{

}