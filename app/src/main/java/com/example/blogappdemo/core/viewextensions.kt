package com.example.blogappdemo.core

import android.view.View
import android.view.View.GONE


fun View.hide(){
    this.visibility= GONE
}

fun View.show(){
    this.visibility= View.VISIBLE
}