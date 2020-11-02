package youdian.apk.ipqc.bean

data class InsCheckListItem(
        var id: Int,
        var item: String,
        var item_sn: String,
        var method: String,
        var reference_value: String,
        var control: String,
        var frequency: String,
        var control_code: String) : BaseBean()
