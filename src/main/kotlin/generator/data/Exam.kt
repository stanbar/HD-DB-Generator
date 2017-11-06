package generator.data

import java.util.concurrent.atomic.AtomicInteger

data class Exam(val name: String,
                val year: Int){

    val id: Int = lastId.getAndIncrement()

    companion object {
        private val lastId = AtomicInteger(0)
    }
}
