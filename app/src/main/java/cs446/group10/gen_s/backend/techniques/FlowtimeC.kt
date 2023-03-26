package cs446.group10.gen_s.backend.techniques

object FlowtimeC : StudyTechnique {

    override fun repetitions(): Int {
        return 4
    }

    override fun studyDuration(): Long {
        return 5400L
    }

    override fun breakDuration(): Long {
        return 900L
    }

    override fun technique(): String {
        return "Flowtime Type C"
    }

}