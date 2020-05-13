package com.app.credassignmenttask.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
/* Entity class, define schema of database and table name of the data base - data_table  */
@Entity(tableName = "data_table")
data class UserData(@PrimaryKey(autoGenerate = true)
                    @ColumnInfo(name = "id")
                    var id:Long? = 0,
                    @ColumnInfo(name = "fname")
                    val fname: String? = "",
                    @ColumnInfo(name = "lname")
                    val lname: String? = "",
                    @ColumnInfo(name = "email")
                    val email: String? = "",
                    @ColumnInfo(name = "phone")
                    val phone: String? = "",
                    @ColumnInfo(name = "description")
                    val description: String? = ""):Parcelable{

}