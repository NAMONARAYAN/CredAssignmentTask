package com.app.credassignmenttask.repository.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.app.credassignmenttask.model.UserData

/* Dao class interface, database curd operation insert, get and delete */

@Dao
interface DataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDatas(datas: UserData?)

    @Query("SELECT * from data_table ORDER BY id ASC")
    fun getData(): LiveData<List<UserData>>

    @Query("DELETE FROM data_table")
    fun deleteAllData()

}