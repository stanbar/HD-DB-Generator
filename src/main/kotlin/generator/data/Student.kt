package generator.data

import generator.RandomDataGenerator
import generator.random
import java.util.concurrent.ThreadLocalRandom

data class Student(
                   val klass: Klass,
                   val pesel: String,
                   val name: String,
                   var surname: String,
                   var supervisor: Student? = null,
                   val gimScore : Int) : Insertable {

    override fun toInsert() = "$pesel;$name;$surname;${klass.id};${supervisor?.pesel};$gimScore"

    companion object : Schematable {

        fun random(klass: Klass, supervisor: Student?): Student {
            val student = Student(
                    klass = klass,
                    pesel = RandomDataGenerator.randomPesel(),
                    name = RandomDataGenerator.names.random(),
                    surname = RandomDataGenerator.surnames.random(),
                    gimScore = ThreadLocalRandom.current().nextInt(20,100))
            student.supervisor = supervisor?:student

            return student
        }

        override val primaryKey: String = "PESEL"

        override val tableName: String = "Student"
        override val schema: String = "CREATE TABLE $tableName\n" +
                "(\n" +
                "    PESEL varchar(100) NOT NULL PRIMARY KEY,\n" +
                "    Name varchar(100) NOT NULL,\n" +
                "    Surname varchar(100) NOT NULL,\n" +
                "    Class_ID INTEGER NOT NULL FOREIGN KEY REFERENCES Class,\n" +
                "    Supervisor_PESEL varchar(100) NOT NULL FOREIGN KEY REFERENCES Student,\n" +
                "    GimScore INTEGER NOT NULL\n" +
                ")"

    }

    fun updateSurname(): Student {
        surname = RandomDataGenerator.surnames.random()
        return this
    }

}
