package youdian.apk.ipqc.bean

/**
 * 时间　: 2019/12/23
 * 描述　: 账户信息
 */

data class UserData(var is_superuser: Boolean = false,
                    var menus: List<String> = listOf<String>(),
                    var groups: List<KVEntity> = listOf<KVEntity>(),
                    var role_code: String="",
                    var feecode: String="",
                    var pnum: String="",
                    var token: String="",
                    var department_name: String="",
                    var department_code: String="",
                    var bu_name: String="",
                    var bu_code: String="",
                    var username: String="") : BaseBean()
