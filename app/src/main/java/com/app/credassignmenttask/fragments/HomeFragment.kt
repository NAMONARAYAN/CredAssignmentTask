package com.app.credassignmenttask.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.app.credassignmenttask.R
import com.app.credassignmenttask.adapter.HomePageRecyclerAdapter
import com.app.credassignmenttask.model.UserData
import com.app.credassignmenttask.utils.isConnected
import com.app.credassignmenttask.viewmodels.HomeScreenViewModel
import kotlinx.android.synthetic.main.frag_main_screen.*
import kotlin.collections.ArrayList

class HomeFragment : MasterFragment(), View.OnClickListener {
    companion object {
        fun getNewInstance(): HomeFragment {
            val fragment = HomeFragment()
            fragment.arguments = Bundle()
            return fragment
        }
    }

    var mMainActivityViewModel: HomeScreenViewModel ? = null
    var mRecyclerAdapter : HomePageRecyclerAdapter?= null
    var datalist : ArrayList<UserData?>? = ArrayList()


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_main_screen, container, false)


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* Intialize View model */
        activity?.let {
            mMainActivityViewModel = ViewModelProviders.of(it).get(HomeScreenViewModel::class.java)
        }
        initRecyclerView()
//        mMainActivityViewModel?.deleteDBData()
        mMainActivityViewModel?.getSavedData?.observe(this, Observer {
            it?.let {
                if(it!=null && it.size>0){
                    if (it != null) {
                        datalist = ArrayList()
                        datalist?.addAll(it)
                        mRecyclerAdapter?.setData(datalist)
                    }

                }

            } })

        if(isConnected(activity)){
            getUsersData()
        }
        iv_add.setOnClickListener(this)
        iv_imglist.setOnClickListener(this)
    }

    private fun initRecyclerView() {

        val layoutManager= GridLayoutManager(activity, 1)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (position) {
                    0 -> 1
                    else -> 1
                }
            }
        }
        recycler_view.layoutManager = layoutManager
        mRecyclerAdapter = HomePageRecyclerAdapter(context)
        mRecyclerAdapter?.onClicked = this::onClick
        recycler_view?.adapter = mRecyclerAdapter

    }


    private fun getUsersData(){
        showLoader()
        mMainActivityViewModel?.getDataList?.observe(this, Observer{
            it.let {
                Log.d("@@@getUsersData",""+it)
                mMainActivityViewModel?.saveDbData(it)
                mRecyclerAdapter?.setData(it)
                hideLoader()
                Log.d("@@@@@DB",""+mMainActivityViewModel?.getSavedData)
            }

        })
    }


    protected fun showLoader() {
        if (progressBar != null) {
            progressBar.visibility = View.VISIBLE
        }

    }
    protected fun hideLoader() {
        if (progressBar != null) {
            progressBar.visibility = View.INVISIBLE
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_add ->{
                mMainActivityViewModel?.selectdata?.postValue(null)
                val addUserFragment: AddUserFragment =
                    AddUserFragment.getNewInstance()
                addToBackStack(fragmentManager, addUserFragment, false)
            }R.id.iv_imglist ->{
                val addUserFragment: PhotoListFragment =
                    PhotoListFragment.getNewInstance()
                addToBackStack(fragmentManager, addUserFragment, false)
            }
            R.id.mainContainer -> {
                val dataItem = v?.tag as UserData
                mMainActivityViewModel?.selectdata?.postValue(dataItem)
                val fragment: MasterFragment = AddUserFragment()
                addToBackStack(fragmentManager, fragment, false)
            }
        }
    }
}