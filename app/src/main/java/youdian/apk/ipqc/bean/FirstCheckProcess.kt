package youdian.apk.ipqc.bean

data class FirstCheckProcess(
        var id: Int, var process_name: String, var process_code: String, var process_sn: String, var note: String,
        var first_checklist_items: List<FirstCheckListItem>) : BaseBean()