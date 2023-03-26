package cs446.group10.gen_s.backend.techniques

object FiftyTwoSeventeen : StudyTechnique {
    override fun repetitions(): Int {
        return 4
    }

    override fun studyDuration(): Long {
        return 3120L
    }

    override fun breakDuration(): Long {
        return 1020L
    }

    override fun technique(): String {
        return "52/17"
    }

}