package cs446.group10.gen_s.backend.techniques

object FlowtimeB : StudyTechnique {
    override fun repetitions(): Int {
        return 4
    }

    override fun studyDuration(): Long {
        return 3600L
    }

    override fun breakDuration(): Long {
        return 600L
    }

    override fun technique(): String {
        return "Flowtime Type B"
    }

}