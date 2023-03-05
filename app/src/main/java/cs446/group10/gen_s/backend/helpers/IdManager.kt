object IdManager {
    /***
     * IdManager is a Singleton class that manages and generates all IDs for the app
     ***/

    val globalIDTable: MutableMap<String, Any> = mutableMapOf()
    var mockIdGenerate: Int = 0

    // TODO: Create a robust system for generating IDs
    fun generateId(): String {
        return (mockIdGenerate++).toString()
    }


}