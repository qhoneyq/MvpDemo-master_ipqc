package youdian.apk.ipqc.bean

data class FirstCheckList(
        var id: Int, var list_name: String, var list_code: String, var se: String, var se_name: String, var se_category: String,
        var se_category_name: String, var first_checklist_processes: List<FirstCheckProcess>
) : BaseBean()