package generator.data

import generator.RandomDataGenerator
import generator.random

data class Student(val class_id: Int, val pesel: String, val name: String, var surname: String) : Insertable {

    override fun toInsert() = "$pesel;$name;$surname;$class_id"

    companion object : Schematable {

        fun random(class_id: Int) = Student(class_id = class_id, pesel = RandomDataGenerator.randomPesel(),
                name = RandomDataGenerator.names.random(), surname = RandomDataGenerator.surnames.random())

        override val schema: String = "CREATE TABLE Student\n" +
                "(\n" +
                "    PESEL nchar(11) PRIMARY KEY,\n" +
                "    Name nvarchar(40) NOT NULL,\n" +
                "    Surname nvarchar(40) NOT NULL,\n" +
                "    Class_ID INTEGER NOT NULL FOREIGN KEY REFERENCES Class\n" +
                ")"

    }

    fun updateSurname(): Student {
        surname = RandomDataGenerator.surnames.random()
        return this
    }

}
