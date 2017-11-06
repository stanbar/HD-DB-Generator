package generator.data

import java.util.concurrent.atomic.AtomicInteger

data class Klass(val sign: Char,
        val tutor_pesel: String,
        val year: Int) {

    val id: Int = lastId.getAndIncrement()

    companion object {
        private val lastId = AtomicInteger(0)
    }
}

