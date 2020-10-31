package youdian.apk.ipqc.bean

data class FirstCheckListItem(
        var id: Int,
        var item: String,
        var item_sn: String,
        var method: String,
        var reference_value: String,
        var control: String,
        var control_code: String) : BaseBean()
