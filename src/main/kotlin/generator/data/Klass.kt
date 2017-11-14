package generator.data

import java.util.concurrent.atomic.AtomicInteger

data class Klass(
                 private val sign: Char,
                 private val tutor_pesel: String,
                 private val originYear: Int) : Insertable {

    val id: Int = lastId.getAndIncrement()

    fun currentLevel(currentYear: Int) = currentYear - originYear + 1

    override fun toInsert() = "$id;$sign;$tutor_pesel;$originYear"

    companion object : Schematable {
        private val lastId = AtomicInteger(0)

        override val schema = "CREATE TABLE Class\n" +
                "(\n" +
                "    ID INTEGER IDENTITY(1, 1) PRIMARY KEY,\n" +
                "    Sign nvarchar(1) NOT NULL,\n" +
                "    Tutor_PESEL nchar(11) NOT NULL FOREIGN KEY REFERENCES Teacher,\n" +
                "    OriginYear INTEGER NOT NULL,\n" +
                ")"
    }


}

