import java.time.LocalDateTime

data class Preference(val preferenceId: String) {
    var name: String? = null
    var startDate: LocalDateTime? = null
    var endDate: LocalDateTime? = null
    var priority: Int? = null
}
