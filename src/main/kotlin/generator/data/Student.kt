package generator.data

import generator.RandomDataGenerator
import generator.random

data class Student(val klass: Klass, val pesel: String, val name: String, var surname: String, val supervisorPesel : String) : Insertable {


    override fun toInsert() = "$name;$pesel;$surname;${klass.id};$supervisorPesel"

    companion object : Schematable {

        fun random(klass: Klass, supervisorPesel: String?) : Student{
            val pesel = RandomDataGenerator.randomPesel()
            return Student(klass = klass, pesel = pesel,
                    name = RandomDataGenerator.names.random(), surname = RandomDataGenerator.surnames.random(), supervisorPesel = supervisorPesel ?: pesel) //TODO boss to himself
        }
        override val tableName: String = "Student"
        override val schema: String = "CREATE TABLE $tableName\n" +
                "(\n" +
                "    Name nvarchar(40) NOT NULL,\n" +
                "    PESEL nvarchar(11) PRIMARY KEY,\n" +
                "    Surname nvarchar(40) NOT NULL,\n" +
                "    Class_ID INTEGER NOT NULL FOREIGN KEY REFERENCES Class\n" +
                "    supervisorPesel nvarchar(11) NOT NULL FOREIGN KEY REFERENCES Student\n" +
                ")"

    }

    fun updateSurname(): Student {
        surname = RandomDataGenerator.surnames.random()
        return this
    }

}
