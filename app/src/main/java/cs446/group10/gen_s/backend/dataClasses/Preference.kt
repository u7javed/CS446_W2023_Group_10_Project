import java.util.*

data class Preference(val preferenceId: String) {
    var name: String? = null
    var startDate: Date? = null
    var endDate: Date? = null
    var priority: Int? = null
}
