package generator.data

import java.util.*
import java.util.concurrent.atomic.AtomicInteger

data class Grade(
        val date: Date,
        val grade: Float,
        val subject_id: Int,
        val teacher_pesel: Int,
        val student_pesel: Int) {

    val id: Int = lastId.getAndIncrement()

    companion object {
        private  val lastId = AtomicInteger(0)
    }
}


