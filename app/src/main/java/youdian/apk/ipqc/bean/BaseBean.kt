package youdian.apk.ipqc.bean

import com.google.gson.Gson
import java.io.Serializable

/**
 * 作者 create by H7111906 on 2020/4/22
 */
open class BaseBean : Serializable {
    fun toJson() = Gson().toJson(this)

//    fun toFormatJson() = JsonFormatUtils.format(toJson())

}