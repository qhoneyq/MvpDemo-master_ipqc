package com.foxconn.youdian.apk.ipqc.bean

import youdian.apk.ipqc.bean.BaseBean

data class ListResponseData<T> (var count:Int,var next:String,var previous:String,
                             var results:List<T>): BaseBean()


