package com.app.credassignmenttask.repository

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.credassignmenttask.model.AppDatabase
import com.app.credassignmenttask.model.UriDataModel
import com.app.credassignmenttask.model.UserData
import com.app.credassignmenttask.repository.db.DataDao
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class HomeScreenRepository(application: Application) : CoroutineScope {

    private var isProgress: Boolean = false
    private var allsavedData : LiveData<List<UserData>>?
    var progressstatusAddUser = MutableLiveData<Boolean>()
    var progressstatus = MutableLiveData<Boolean>()
    var progressData = MutableLiveData<Int>()
    var progressDataAddUser = MutableLiveData<String>()
    var homeProgressstatus = MutableLiveData<Boolean>()



    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
    private var dataDao: DataDao?
    init {
        /*Intializing data base and dao class get all saved data in this method*/
        val db = AppDatabase.getDatabase(application.applicationContext)
        dataDao = db?.dataDao()
        allsavedData = dataDao?.getData()
        progressDataAddUser.postValue("")
    }
    fun getProgressStatus() : MutableLiveData<Boolean> {
        val progressStatus : MutableLiveData<Boolean> = MutableLiveData()
        progressStatus.value = isProgress
        return progressStatus
    }

    fun getData() : LiveData<ArrayList<UserData?>> {
        isProgress = true
        val userDataList : MutableLiveData<ArrayList<UserData?>> = MutableLiveData()
       var mdatalist :  ArrayList<UserData?> = ArrayList()
        val path : String = "credassignmenttask"
        val ref = FirebaseDatabase.getInstance().getReference(path)
        var phone = ""
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("@@TAG", "loadPost:onCancelled", databaseError.toException())
                isProgress = false
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    Log.d("@@Data", " "+ postSnapshot)
                    val item : UserData? =  postSnapshot.getValue(UserData::class.java) as UserData
                    mdatalist.add(item)
                }
                isProgress = false
                userDataList.postValue(mdatalist)
            }

        })
          return userDataList
    }

    /*Save data into local room data base*/
    fun saveData(mlist: ArrayList<UserData?>?) {
        launch  { insertDataintoDb(mlist) }
    }

    private suspend fun insertDataintoDb(mlist: ArrayList<UserData?>?){
        withContext(Dispatchers.IO){
            val size  : Int = mlist?.size ?:0
            if(size >0)
                if(mlist!=null){
                    for(list in mlist){
                        dataDao?.insertDatas(list)
                        Log.d("@@@@@DB",""+list?.fname)
                        Log.d("@@@@@DB",""+list?.id)
                    }

                }
        }
    }

    fun deleteDbDate(){
        launch {
            withContext(Dispatchers.IO){
                dataDao?.deleteAllData()
            }
        }
    }

    /*get saved data from local room data base*/
    fun getSavedData() : LiveData<List<UserData>>? {
        return allsavedData
    }



    fun saveCurrentUser(userData : UserData){
        progressstatusAddUser.postValue(true)
        val path : String = "credassignmenttask" + "/" + userData?.id
        val ref = FirebaseDatabase.getInstance().getReference(path)
        if (userData != null) {
            ref.setValue(userData)
        }

        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("TAG", "loadPost:onCancelled", databaseError.toException())
                progressstatusAddUser.postValue(false)
                progressDataAddUser.postValue("Error while Adding User")

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    progressstatusAddUser.postValue(false)
                    progressDataAddUser.postValue("User Added Sucessfully")
                }
            }

        })

    }



    fun upLoadFile(file: Uri) {
        progressstatus.postValue(true)
        val tsLong = System.currentTimeMillis() / 1000
        val ts = tsLong.toString()
        val riversRef: StorageReference? =
            FirebaseStorage.getInstance().getReference()?.child("image/uid/$ts")
        riversRef?.putFile(file)?.addOnSuccessListener { taskSnapshot ->
            //                progressDialog.dismiss();
            progressstatus.postValue(false)
        }?.addOnFailureListener {
            progressstatus.postValue(false)

        }?.addOnProgressListener { taskSnapshot ->
            val progress: Double = 100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount()
            progressData.postValue(progress.toInt())
        }
    }


    fun getfirebasedata(): LiveData<ArrayList<UriDataModel>> {
        homeProgressstatus.postValue(true)
        val mimagelist: MutableLiveData<ArrayList<UriDataModel>> = MutableLiveData()
        val uriDataModelItems: ArrayList<UriDataModel> = ArrayList()
        var listRef: StorageReference =
            FirebaseStorage.getInstance().getReference().child("image/uid")
        val listAllTask: Task<ListResult> = listRef.listAll()
        listAllTask.addOnCompleteListener { result ->
            var id: Int = 0;
            for (item in result.result?.items!!) {
                item.downloadUrl?.addOnCompleteListener {
                    it.let {
                        uriDataModelItems.add(UriDataModel(id, it.result))
                        id++
                        return@let mimagelist.postValue(uriDataModelItems)
                        homeProgressstatus.postValue(false)
                    }
                }
            }
        }
        return mimagelist

    }


}