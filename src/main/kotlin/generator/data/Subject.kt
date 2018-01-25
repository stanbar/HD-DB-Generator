package generator.data

import java.util.concurrent.atomic.AtomicInteger


data class Subject(val name: String) : Insertable {
    override fun toInsert() = "$id;$name"

    val id: Int = lastId.getAndIncrement()

    companion object : Schematable {
        override val primaryKey: String = "ID"
        private val lastId = AtomicInteger(1)
        override val tableName: String = "Subject"
        override val schema: String = "CREATE TABLE $tableName\n" +
                "(\n" +
                "    ID INTEGER IDENTITY(1, 1) PRIMARY KEY,\n" +
                "    Name varchar(100) NOT NULL,\n" +
                ")"
    }


}


