package youdian.apk.ipqc.bean

data class InsCheckProcess(
        var id: Int, var process_name: String, var process_code: String, var process_sn: String, var note: String,
        var ins_checklist_items: List<InsCheckListItem>) : BaseBean()