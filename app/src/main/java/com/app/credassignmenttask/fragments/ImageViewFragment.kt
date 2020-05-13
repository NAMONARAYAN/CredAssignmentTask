package com.app.credassignmenttask.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.credassignmenttask.R
import com.app.credassignmenttask.utils.isConnected
import com.app.credassignmenttask.viewmodels.HomeScreenViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_image.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ImageViewFragment : MasterFragment(), View.OnClickListener {
    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)
    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var mScaleFactor = 1.0f

    companion object {
        fun getNewInstance(): ImageViewFragment {
            val fragment = ImageViewFragment()
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
        return inflater.inflate(R.layout.fragment_image, container, false)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        /* Intialize View model */
        activity?.let {
            val savedViewModel = ViewModelProviders.of(it).get(HomeScreenViewModel::class.java)
            observeInput(savedViewModel)
        }
        item_imgvw.isEnabled= true
        iv_back.setOnClickListener(this)
    }


    private fun observeInput(mViewModel: HomeScreenViewModel) {
        /*Observe set data from Main Screen fragment  and set values to the views*/
        mViewModel.selectdataPhoto?.observe(this, Observer {
            it?.let {
                if(it!=null){
                    if(isConnected(context)){
                        showLoader()
                        coroutineScope.launch(Dispatchers.Main) {
                            if(context!=null){
                                item_imgvw.setEnabled(true)
                                item_imgvw.setVisibility(View.VISIBLE)
                                hideLoader()
                                Glide.with(context!!).load(it.url).into(item_imgvw)
                            }
                        }
                    }
                }
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

    override fun onDestroy() {
        super.onDestroy()
        parentJob.cancel()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                popBackStack(activity!!.supportFragmentManager)
            }
        }
    }


}