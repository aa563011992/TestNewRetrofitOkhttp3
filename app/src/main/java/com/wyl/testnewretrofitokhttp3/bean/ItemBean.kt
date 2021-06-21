package com.example.myapplication.bean

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class ItemBean(val title:String,@SerializedName("imagepath") val imagePath:String)
data class ItemList(val NAItem:ArrayList<ItemBean>){

    override fun toString(): String {
        return Gson().toJson(this)
    }
}
