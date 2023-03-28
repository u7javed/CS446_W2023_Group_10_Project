object ViewModelHelper {
    /***
     * ViewModelHelper is also a Singleton that provides helpers to the cs446.group10.gen_s.backend.view_model.ViewModel
     ***/

    val globalIDTable: MutableMap<String, Any> = mutableMapOf()

    fun sanitizePlanName(name: String): String {
        // TODO: Sanitize name before returning string
        return name
    }

    fun validHexColor(input: String): Boolean {
        if (input[0] != '#')
            return false
        if (input.length != 7 && input.length != 9)
            return false

        input.substring(1).forEach { c ->
            if ((c !in '0'..'9') && (c !in 'a'..'f') && (c !in 'A'..'F'))
                return false
        }
        return true
    }


}