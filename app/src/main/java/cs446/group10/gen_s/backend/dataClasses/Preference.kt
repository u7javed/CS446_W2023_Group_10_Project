data class Preference(
    val preferenceId: String,
    val name: String,
    val startRange: Long,
    val endRange: Long,
    val duration: Long,
    var notification: Long? = null,
    var priority: Int? = null
)
