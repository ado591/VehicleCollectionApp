package commands.extra

interface Autogeneratable {

    /**
     * Checks if the given flag is "--auto".
     *
     * @param flag the flag to check
     * @return true if the flag is "--auto", false otherwise
     */
    fun checkFlag(flag: String): Boolean {
        return flag == "--auto"
    }
}