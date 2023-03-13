package cs446.group10.gen_s.backend.dataClasses

data class Preference(
    val name: String,
    val startRange: Long,
    val endRange: Long,
    val duration: Long,
    var notification: Long? = null,
)
