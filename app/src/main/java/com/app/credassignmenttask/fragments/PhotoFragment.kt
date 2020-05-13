package com.app.credassignmenttask.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.credassignmenttask.R
import com.app.credassignmenttask.viewmodels.HomeScreenViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_preview.*
import kotlinx.android.synthetic.main.fragment_preview.progressBar

class PhotoFragment internal constructor() : MasterFragment(), View.OnClickListener {

    var mMainActivityViewModel: HomeScreenViewModel? = null

    companion object {
        fun getNewInstance(): PhotoFragment {
            val fragment = PhotoFragment()
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
        return inflater.inflate(R.layout.fragment_preview, container, false)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            mMainActivityViewModel = ViewModelProviders.of(it).get(HomeScreenViewModel::class.java)
        }
        mMainActivityViewModel?.capturedImage?.observe(this, Observer {
            it.let {
                Glide.with(view).load(it).placeholder(R.drawable.ic_photo).into(item_imgvw)
            }
        })
        iv_back.setOnClickListener(this)
        upload_button.setOnClickListener(this)
        mMainActivityViewModel?.getprogressStatus()?.observe(this, Observer {
            it.let {
                if(it==true){
                    showLoader()
                }else{
                    hideLoader()

                }
            }
        })

        mMainActivityViewModel?.getprogressData?.observe(this, Observer {
            it.let {
                if(it ==100){
                    Toast.makeText(activity,"Photo Uploaded Successfully!!!",Toast.LENGTH_LONG).show()
                    Handler().postDelayed({
                        popBackStack(activity!!.supportFragmentManager)
                    },1000)
                    mMainActivityViewModel?.getprogressData?.postValue(0)
                }
            }
        })
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                popBackStack(activity!!.supportFragmentManager)
            }R.id.upload_button -> {
            if(mMainActivityViewModel?.capturedImage?.value!=null)
            mMainActivityViewModel?.uploadImage(mMainActivityViewModel?.capturedImage?.value!!)
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