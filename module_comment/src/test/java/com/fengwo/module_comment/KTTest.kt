package com.fengwo.module_comment

import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class KTTest {

    @Test
    fun test1(){
        System.out.println(System.currentTimeMillis().formatTime())
    }

    fun Long.formatTime():String{
        val simpleDateFormat = SimpleDateFormat.getInstance() as SimpleDateFormat
        simpleDateFormat.applyPattern("aa HH:mm")
        return simpleDateFormat.format(Date(this))
    }
}