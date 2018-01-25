package generator.data

import java.util.concurrent.atomic.AtomicInteger

data class Klass(
                 private val sign: Char,
                 private val tutor: Teacher,
                 private val originYear: Int) : Insertable {

    val id: Int = lastId.getAndIncrement()

    fun currentLevel(currentYear: Int) = currentYear - originYear + 1

    override fun toInsert() = "$id;$sign;${tutor.pesel};$originYear"

    companion object : Schematable {
        override val primaryKey: String = "ID"
        private val lastId = AtomicInteger(1)
        override val tableName: String = "Class"
        override val schema = "CREATE TABLE $tableName\n" +
                "(\n" +
                "    ID INTEGER IDENTITY(1, 1) PRIMARY KEY,\n" +
                "    Sign varchar(1) NOT NULL,\n" +
                "    Tutor_PESEL varchar(100) NOT NULL FOREIGN KEY REFERENCES Teacher,\n" +
                "    OriginYear INTEGER NOT NULL,\n" +
                ")"
    }


}

