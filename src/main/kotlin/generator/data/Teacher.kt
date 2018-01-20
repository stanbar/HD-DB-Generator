package generator.data

import generator.RandomDataGenerator
import generator.random
import java.util.*

data class Teacher(
        val pesel: String,
        val name: String,
        val surname: String,
        var title: String,
        val startWorkingDate: MyCalendar,
        var experienceYears: Int,
        val supervisorPesel: String,
        val subject: Subject
) : Insertable {
    override fun toInsert() = "$name;$pesel;$surname;$title;${startWorkingDate.id};$experienceYears;$supervisorPesel;${subject.id}"

    fun updateTitle(): Teacher {
        title = RandomDataGenerator.nextTitle(title)
        return this
    }

    fun updateExperience(year: Int): Teacher {
        experienceYears = currentExperience(year, startWorkingDate.calendar)
        return this
    }


    companion object : Schematable {
        override val tableName: String = "Teacher"
        override val schema: String = "CREATE TABLE $tableName\n" +
                "(\n" +
                "    Name nvarchar(40) NOT NULL,\n" +
                "    PESEL nvarchar(11) PRIMARY KEY,\n" +
                "    Surname nvarchar(40) NOT NULL,\n" +
                "    Title nvarchar(40) NOT NULL,\n" +
                "    StartWorkingDate_ID INTEGER NOT NULL FOREIGN KEY REFERENCES MyCalendar,\n" +
                "    ExperienceYears INTEGER NOT NULL,\n" +
                "    SupervisorPesel nvarchar(11) NOT NULL FOREIGN KEY REFERENCES Teacher,\n" +
                "    Subject_ID INTEGER NOT NULL FOREIGN KEY REFERENCES Subject,\n" +
                ")"


        fun random(year: Int, supervisorPesel: String?, subject: Subject): Teacher {
            val randomCalendar = RandomDataGenerator.randomCalendar()
            val myCalendar = MyCalendar(randomCalendar)
            val pesel = RandomDataGenerator.randomPesel()
            return Teacher(
                    pesel = pesel,
                    name = RandomDataGenerator.names.random(),
                    surname = RandomDataGenerator.surnames.random(),
                    title = RandomDataGenerator.titles.random(),
                    startWorkingDate = myCalendar,
                    experienceYears = currentExperience(year, randomCalendar),
                    supervisorPesel = supervisorPesel ?: pesel,
                    subject = subject)
        }

        fun currentExperience(now: Int, start: Calendar): Int {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, now)
            return calendar.get(Calendar.YEAR) - start.get(Calendar.YEAR)
        }
    }
}

