package com.app.credassignmenttask.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.app.credassignmenttask.R
import com.app.credassignmenttask.model.UriDataModel
import com.app.credassignmenttask.utils.dpToPx
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.viewholder.view.*

class RecyclerAdapter (val contex: Context?) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    var dataList: ArrayList<UriDataModel>? = null
    open var onClicked: ((view: View) -> Any)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder = ViewHolder(LayoutInflater.from(contex).inflate(R.layout.viewholder,parent,false))
        holder.onClicked = onClicked
        return holder
    }

    override fun getItemCount(): Int {
        return if(dataList?.size?:0 >0) dataList?.size?:0 else 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mData = dataList?.get(position)
        if(mData!=null){
            holder.bindData(mData,position)
        }
    }

    fun setData(dataList: ArrayList<UriDataModel>?) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder (view: View): RecyclerView.ViewHolder(view){


        open var onClicked: ((view: View) -> Any)? = null


        fun bindData(model: UriDataModel,position: Int) {

            if(position ==0){
                mainContainer.setPadding(0, 0, dpToPx(8), dpToPx(20))
                imageView.getLayoutParams().height = dpToPx(265)
                imageView.requestLayout()
            }else{
                imageView.getLayoutParams().height = dpToPx(240)
                imageView.requestLayout()
                mainContainer.setPadding(0, 0, dpToPx(8), dpToPx(20))
            }
            showLoader(progressBar)
            Glide.with(itemView.context).load(model.url).listener(object :
                RequestListener<Drawable?> { override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable?>?, isFirstResource: Boolean): Boolean {
                hideLoader(progressBar)
                return false
            }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable?>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    hideLoader(progressBar)
                    return false
                }

            }).into(imageView)
            itemView?.tag = model
            itemView.setOnClickListener {
                onClicked?.invoke(it)
            }
        }
        val imageView = view.item_imgvw
        val mainContainer = view.mainContainer
        val progressBar = view.progressBar

        protected fun showLoader(progressBar: ProgressBar) {
            if (progressBar != null) {
                progressBar.visibility = View.VISIBLE
            }

        }
        protected fun hideLoader(progressBar: ProgressBar) {
            if (progressBar != null) {
                progressBar.visibility = View.INVISIBLE
            }
        }

    }
}