import java.time.LocalDate

class ViewModel {

    init {

    }

    // TODO: Update events and preferences to their corresponding list types
    fun createPlan(name: String, startDate: LocalDate, endDate: LocalDate, events: List<Any>, preferences: List<Any>) {
        val sanitizedName: String = ViewModelHelper.sanitizePlanName(name)

    }

    private fun isValidDate(startDate: LocalDate, endDate: LocalDate): Boolean {
        // TODO: Validate a given start and end date for an event
        return true
    }

}