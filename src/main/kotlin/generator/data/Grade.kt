package generator.data

import java.util.*

data class Grade(
        val id: Int,
        val data: Date,
        val ocena: Float,
        val przedmiot_id: Int,
        val nauczyciel_pesel: Int,
        val uczen_pesel: Int
)


