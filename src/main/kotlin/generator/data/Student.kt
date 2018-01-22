package generator.data

import generator.RandomDataGenerator
import generator.random
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicInteger

data class Student(val id: Int = lastId.getAndIncrement(),
                   val klass: Klass,
                   val pesel: String,
                   val name: String,
                   var surname: String,
                   val supervisorID: Int,
                   val gimScore : Int) : Insertable {

    override fun toInsert() = "$id;$name;$pesel;$surname;${klass.id};$supervisorID;$gimScore"

    companion object : Schematable {

        fun random(klass: Klass, supervisorId: Int?): Student {
            val id = lastId.getAndIncrement()
            return Student(id = id,
                    klass = klass,
                    pesel = RandomDataGenerator.randomPesel(),
                    name = RandomDataGenerator.names.random(),
                    surname = RandomDataGenerator.surnames.random(),
                    supervisorID = supervisorId ?: id, //TODO boss to himself
                    gimScore = ThreadLocalRandom.current().nextInt(20,100))
        }

        private val lastId = AtomicInteger(1)
        override val tableName: String = "Student"
        override val schema: String = "CREATE TABLE $tableName\n" +
                "(\n" +
                "    ID INTEGER IDENTITY(1,1) NOT NULL PRIMARY KEY, \n" +
                "    Name nvarchar(40) NOT NULL,\n" +
                "    PESEL nvarchar(11),\n" +
                "    Surname nvarchar(40) NOT NULL,\n" +
                "    Class_ID INTEGER NOT NULL FOREIGN KEY REFERENCES Class,\n" +
                "    supervisor_ID INTEGER NOT NULL FOREIGN KEY REFERENCES Student,\n" +
                "    gimScore INTEGER NOT NULL\n" +
                ")"

    }

    fun updateSurname(): Student {
        surname = RandomDataGenerator.surnames.random()
        return this
    }

}
