package cs446.group10.gen_s.backend.dataClasses

/**
 * Space object for managing open spaces in the cs446.group10.gen_s.backend.dataClasses.Calendar
 */
data class Space (
    val start: Long,
    val end: Long,
    val duration: Long = end - start
)
