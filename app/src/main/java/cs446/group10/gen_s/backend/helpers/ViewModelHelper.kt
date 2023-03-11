object ViewModelHelper {
    /***
     * ViewModelHelper is also a Singleton that provides helpers to the ViewModel
     ***/

    val globalIDTable: MutableMap<String, Any> = mutableMapOf()

    fun sanitizePlanName(name: String): String {
        // TODO: Sanitize name before returning string
        return name
    }


}