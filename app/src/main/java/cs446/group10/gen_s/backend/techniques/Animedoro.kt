package cs446.group10.gen_s.backend.techniques

object Animedoro: StudyTechnique {
    override fun repetitions(): Int {
        return 4
    }

    override fun studyDuration(): Long {
        return 3000L
    }

    override fun breakDuration(): Long {
        return 1200L
    }

    override fun technique(): String {
        return "Animedoro"
    }

}