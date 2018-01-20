package generator.data

import java.util.concurrent.atomic.AtomicInteger


data class Subject(val name: String) : Insertable {
    override fun toInsert() = "$id;$name"

    val id: Int = lastId.getAndIncrement()

    companion object : Schematable {

        private val lastId = AtomicInteger(0)
        override val tableName: String = "Subject"
        override val schema: String = "CREATE TABLE $tableName\n" +
                "(\n" +
                "    ID INTEGER IDENTITY(1, 1) PRIMARY KEY,\n" +
                "    Name nvarchar(40) NOT NULL,\n" +
                ")"
    }


}


