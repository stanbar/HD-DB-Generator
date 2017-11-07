package generator.data

import java.util.concurrent.atomic.AtomicInteger

data class Klass(val sign: Char,
                 val tutor_pesel: String,
                 val year: Int) : Insertable {

    val id: Int = lastId.getAndIncrement()

    override fun toInsert() = "$id;$sign;$tutor_pesel;$year"

    companion object : Schematable {
        private val lastId = AtomicInteger(0)

        override val schema = "CREATE TABLE Class\n" +
                "(\n" +
                "    Sign nvarchar(1) NOT NULL,\n" +
                "    Year INTEGER NOT NULL,\n" +
                "    Tutor_PESEL nchar(11) NOT NULL FOREIGN KEY REFERENCES Teacher,\n" +
                ")"
    }


}

