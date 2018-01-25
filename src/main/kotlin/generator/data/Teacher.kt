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
        var supervisor: Teacher? = null,
        val subject: Subject
) : Insertable {
    override fun toInsert() = "$pesel;$name;$surname;$title;${startWorkingDate.year};$experienceYears;${supervisor?.pesel};${subject.id}"

    fun updateTitle(): Teacher {
        title = RandomDataGenerator.nextTitle(title)
        return this
    }

    fun updateExperience(year: Int): Teacher {
        experienceYears = currentExperience(year, startWorkingDate.calendar)
        return this
    }


    companion object : Schematable {
        override val primaryKey = "PESEL"
        override val tableName: String = "Teacher"
        override val schema: String = "CREATE TABLE $tableName\n" +
                "(\n" +
                "    PESEL varchar(11) NOT NULL PRIMARY KEY,\n" +
                "    Name varchar(100) NOT NULL,\n" +
                "    Surname varchar(100) NOT NULL,\n" +
                "    Title varchar(100) NOT NULL,\n" +
                "    StartWorkingDate_Year INTEGER NOT NULL,\n" +
                "    ExperienceYears INTEGER NOT NULL,\n" +
                "    Supervisor_PESEL varchar(11) NOT NULL FOREIGN KEY REFERENCES Teacher,\n" +
                "    Subject_ID INTEGER NOT NULL FOREIGN KEY REFERENCES Subject,\n" +
                ")"


        fun random(year: Int, supervisor: Teacher?, subject: Subject, myCalendar: MyCalendar): Teacher {
            val teacher = Teacher(
                    pesel = RandomDataGenerator.randomPesel(),
                    name = RandomDataGenerator.names.random(),
                    surname = RandomDataGenerator.surnames.random(),
                    title = RandomDataGenerator.titles.random(),
                    startWorkingDate = myCalendar,
                    experienceYears = currentExperience(year, myCalendar.calendar),
                    subject = subject)

            teacher.supervisor = supervisor?: teacher
            return teacher
        }

        fun currentExperience(now: Int, start: Calendar): Int {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, now)
            return calendar.get(Calendar.YEAR) - start.get(Calendar.YEAR)
        }
    }
}

