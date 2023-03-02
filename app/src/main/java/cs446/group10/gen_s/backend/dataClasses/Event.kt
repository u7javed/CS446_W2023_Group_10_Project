import java.time.LocalDateTime

data class Event (val eventId: String){
    var name: String? = null
    var startDate: LocalDateTime? = null
    var endDate: LocalDateTime? = null
    var notification: LocalDateTime? = null
}
