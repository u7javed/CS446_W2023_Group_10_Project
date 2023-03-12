object ViewModelHelper {
    /***
     * ViewModelHelper is also a Singleton that provides helpers to the cs446.group10.gen_s.backend.view_model.ViewModel
     ***/

    val globalIDTable: MutableMap<String, Any> = mutableMapOf()

    fun sanitizePlanName(name: String): String {
        // TODO: Sanitize name before returning string
        return name
    }


}