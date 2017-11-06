package generator.data

import java.util.concurrent.atomic.AtomicInteger

data class Subject(val name: String) {

    val id: Int = lastId.getAndIncrement()

    companion object {
        private val lastId = AtomicInteger(0)
    }
}


