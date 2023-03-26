package cs446.group10.gen_s.backend.techniques

object Pomodoro : StudyTechnique {

    override fun repetitions(): Int {
        return 4
    }

    override fun studyDuration(): Long {
        return 1500L
    }

    override fun breakDuration(): Long {
        return 300L
    }

    override fun technique(): String {
        return "Pomodoro"
    }


}