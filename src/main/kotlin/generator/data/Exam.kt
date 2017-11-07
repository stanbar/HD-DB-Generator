package generator.data

import java.util.concurrent.atomic.AtomicInteger

data class Exam(private val name: String, private val year: Int) : Insertable {

    override fun toInsert(): String = "$id;$name;$year"

    val id: Int = lastId.getAndIncrement()

    companion object : Schematable {
        private val lastId = AtomicInteger(0)
        override val schema: String = "CREATE TABLE Exam\n" +
                "(\n" +
                "    ID INTEGER IDENTITY(1, 1) PRIMARY KEY,\n" +
                "    Name nvarchar(40) NOT NULL,\n" +
                "    Year INTEGER NOT NULL,\n" +
                ")"
    }
}
