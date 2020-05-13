package com.app.credassignmenttask.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.credassignmenttask.R
import com.app.credassignmenttask.model.UserData
import kotlinx.android.synthetic.main.view_holder_layout.view.*

class HomePageRecyclerAdapter  (val contex: Context?) : RecyclerView.Adapter<HomePageRecyclerAdapter.ViewHolder>(){

    var dataList: ArrayList<UserData?>? = null
    open var onClicked: ((view: View) -> Any)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomePageRecyclerAdapter.ViewHolder {
        val holder = ViewHolder(LayoutInflater.from(contex).inflate(R.layout.view_holder_layout,parent,false))
        holder.onClicked = onClicked
        return holder
    }

    override fun getItemCount(): Int {
        return if(dataList?.size?:0 >0) dataList?.size?:0 else 0
    }

    override fun onBindViewHolder(holder: HomePageRecyclerAdapter.ViewHolder, position: Int) {
        val mData = dataList?.get(position)
        if(mData!=null){
            holder.bindData(mData,position)
        }
    }

    fun setData(dataList: ArrayList<UserData?>?) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    class ViewHolder (view: View): RecyclerView.ViewHolder(view){

        open var onClicked: ((view: View) -> Any)? = null

        fun bindData(model: UserData,position: Int) {
            name.text =  model?.fname + " " + model?.lname
            tv_description.text = model?.description
            itemView?.tag = model
            itemView.setOnClickListener {
                onClicked?.invoke(it)
            }

        }

        val name = view.tv_name
        val tv_description = view.tv_description

        }
}