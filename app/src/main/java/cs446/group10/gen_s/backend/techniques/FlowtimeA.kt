package cs446.group10.gen_s.backend.techniques

object FlowtimeA : StudyTechnique {
    override fun repetitions(): Int {
        return 4
    }

    override fun studyDuration(): Long {
        return 2400L
    }

    override fun breakDuration(): Long {
        return 480L
    }

    override fun technique(): String {
        return "Flowtime Type A"
    }

}