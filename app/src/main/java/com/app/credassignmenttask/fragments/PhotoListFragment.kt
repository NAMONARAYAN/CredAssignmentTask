package com.app.credassignmenttask.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.app.credassignmenttask.R
import com.app.credassignmenttask.adapter.RecyclerAdapter
import com.app.credassignmenttask.model.UriDataModel
import com.app.credassignmenttask.utils.isConnected
import com.app.credassignmenttask.viewmodels.HomeScreenViewModel
import kotlinx.android.synthetic.main.frag_photo_screen.*


class PhotoListFragment : MasterFragment(),View.OnClickListener  {

    var mMainActivityViewModel: HomeScreenViewModel? = null
    var mRecyclerAdapter : RecyclerAdapter?= null
    var datalist : ArrayList<UriDataModel>? = ArrayList()

    companion object {
        fun getNewInstance(): PhotoListFragment {
            val fragment = PhotoListFragment()
            fragment.arguments = Bundle()
            return fragment
        }
    }

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
        return inflater.inflate(R.layout.frag_photo_screen, container, false)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun getData(){
        showLoader()
        mMainActivityViewModel?.getdata()?.observe( this,  Observer {
            it.let {
                if(it!=null)
                {
                    hideLoader()
                    updateUI(it)
                }
            }
        })
        mMainActivityViewModel?.getHomeProgressStatus()?.observe( this, Observer {
            it.let {
                if(it == true){
                    showLoader()
                }else{
                    hideLoader()
                }
            }
        })

    }

    private fun updateUI(mlist : ArrayList<UriDataModel>?){
        if (mlist != null) {
            datalist = ArrayList()
            datalist?.addAll(mlist)
        }
        mRecyclerAdapter?.setData(datalist)
    }

    /* initialize recycler view */
    private fun initRecyclerView() {
        val layoutManager= GridLayoutManager(activity, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (position) {
                    0 -> 2
                    else -> 1
                }
            }
        }
        recycler_view.layoutManager = layoutManager
        mRecyclerAdapter = RecyclerAdapter(context)
        mRecyclerAdapter?.onClicked = this::onClick
        recycler_view?.adapter = mRecyclerAdapter

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /* Intialize View model */
        activity?.let {
            mMainActivityViewModel = ViewModelProviders.of(it).get(HomeScreenViewModel::class.java)
        }
        initRecyclerView()
        if(isConnected(context))
            getData()
        iv_uopload.setOnClickListener(this)
        iv_back.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.mainContainer -> {
                val dataItem = v?.tag as UriDataModel
                mMainActivityViewModel?.selectdataPhoto?.postValue(dataItem)
                val fragment: MasterFragment = ImageViewFragment()
                addFragment(fragmentManager, fragment)
            }
            R.id.iv_uopload ->{
                val fragment: MasterFragment = UplaodImageFragment()
                addToBackStack(fragmentManager, fragment)
            } R.id.iv_back ->{
            popBackStack(activity!!.supportFragmentManager)
            }
        }
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

}