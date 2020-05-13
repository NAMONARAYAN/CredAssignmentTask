package com.app.credassignmenttask.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.credassignmenttask.model.UriDataModel
import com.app.credassignmenttask.model.UserData
import com.app.credassignmenttask.repository.HomeScreenRepository

/*View Model class to link between Model and View*/
class HomeScreenViewModel(application: Application) : AndroidViewModel(application) {
    var myrepository: HomeScreenRepository? = HomeScreenRepository(application)
    private val savedData : LiveData<Boolean>? = myrepository?.getProgressStatus()

    var mmutabledata : LiveData<ArrayList<UserData>>? = null
    val getDataList = myrepository?.getData()
    fun saveDbData(mlist: ArrayList<UserData?>?){
        myrepository?.saveData(mlist)
    }

    val getSavedData = myrepository?.getSavedData()

    fun uploadImage(uri: Uri) = myrepository?.upLoadFile(uri)
    fun uploadUserData (userData: UserData) =  myrepository?.saveCurrentUser(userData)

    fun  getAddUserProgressStatus() = myrepository?.progressstatusAddUser
    fun  getUploadProgressStatus() = myrepository?.progressstatus
    val capturedImage = MutableLiveData<Uri>()
    val selectdata = MutableLiveData<UserData>()
    val selectdataPhoto = MutableLiveData<UriDataModel>()
    fun deleteDBData() = myrepository?.deleteDbDate()



    val  getprogressData = myrepository?.progressData
    fun  getprogressStatus() = myrepository?.progressstatus

    /*Api Call to fetch data*/
    fun getdata() =  myrepository?.getfirebasedata()
    fun  getHomeProgressStatus() = myrepository?.progressstatus
    val userAddingStatus = myrepository?.progressDataAddUser

}