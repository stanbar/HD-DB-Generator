package generator.data

import generator.RandomDataGenerator
import generator.random
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

data class Teacher(
        val id : Int = lastId.getAndIncrement(),
        val pesel: String,
        val name: String,
        val surname: String,
        var title: String,
        val startWorkingDate: MyCalendar,
        var experienceYears: Int,
        val supervisorId: Int,
        val subject: Subject
) : Insertable {
    override fun toInsert() = "$id;$name;$pesel;$surname;$title;${startWorkingDate.id};$experienceYears;$supervisorId;${subject.id}"

    fun updateTitle(): Teacher {
        title = RandomDataGenerator.nextTitle(title)
        return this
    }

    fun updateExperience(year: Int): Teacher {
        experienceYears = currentExperience(year, startWorkingDate.calendar)
        return this
    }


    companion object : Schematable {
        private val lastId = AtomicInteger(0)

        override val tableName: String = "Teacher"
        override val schema: String = "CREATE TABLE $tableName\n" +
                "(\n" +
                "    ID INTEGER IDENTITY(1,1) NOT NULL PRIMARY KEY, \n" +
                "    Name nvarchar(40) NOT NULL,\n" +
                "    PESEL nvarchar(11),\n" +
                "    Surname nvarchar(40) NOT NULL,\n" +
                "    Title nvarchar(40) NOT NULL,\n" +
                "    StartWorkingDate_ID INTEGER NOT NULL FOREIGN KEY REFERENCES MyCalendar,\n" +
                "    ExperienceYears INTEGER NOT NULL,\n" +
                "    Supervisor_ID INTEGER NOT NULL FOREIGN KEY REFERENCES Teacher,\n" +
                "    Subject_ID INTEGER NOT NULL FOREIGN KEY REFERENCES Subject,\n" +
                ")"


        fun random(year: Int, supervisorId: Int?, subject: Subject): Teacher {
            val randomCalendar = RandomDataGenerator.randomCalendar()
            val myCalendar = MyCalendar(randomCalendar)
            val id = lastId.getAndIncrement()
            return Teacher(
                    id = id,
                    pesel = RandomDataGenerator.randomPesel(),
                    name = RandomDataGenerator.names.random(),
                    surname = RandomDataGenerator.surnames.random(),
                    title = RandomDataGenerator.titles.random(),
                    startWorkingDate = myCalendar,
                    experienceYears = currentExperience(year, randomCalendar),
                    supervisorId = supervisorId ?: id,
                    subject = subject)
        }

        fun currentExperience(now: Int, start: Calendar): Int {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, now)
            return calendar.get(Calendar.YEAR) - start.get(Calendar.YEAR)
        }
    }
}

