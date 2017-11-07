package generator.data

import generator.RandomDataGenerator
import generator.random

data class Teacher(
        val pesel: String,
        val name: String,
        val surname: String,
        val title: String) : Insertable {
    override fun toInsert() = "$pesel;$name;$surname;$title"

    fun updateTitle()
            = Teacher(pesel = pesel, name = name, surname = surname, title = RandomDataGenerator.nextTitle(title))

    fun updateSurname()
            = Teacher(pesel = pesel, name = name, surname = RandomDataGenerator.surnames.random(), title = title)

    companion object : Schematable {
        override val schema: String = "CREATE TABLE Teacher\n" +
                "(\n" +
                "    PESEL nchar(11) PRIMARY KEY,\n" +
                "    Name nvarchar(40) NOT NULL,\n" +
                "    Surname nvarchar(40) NOT NULL,\n" +
                "    Title nvarchar(40) NOT NULL,\n" +
                ")"


        fun random(): Teacher {
            return Teacher(
                    pesel = RandomDataGenerator.randomPesel(),
                    name = RandomDataGenerator.names.random(),
                    surname = RandomDataGenerator.surnames.random(),
                    title = RandomDataGenerator.titles.random())
        }

    }
}

