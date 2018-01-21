package generator.data

import generator.RandomDataGenerator
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

data class Grade(
        val id: Int,
        private val date: MyCalendar,
        private val grade: Double,
        private val subject: Subject,
        private val teacher: Teacher,
        private val student: Student) : Insertable{
    override fun toInsert(): String = "$id;${date.id};$grade;${subject.id};${teacher.id};${student.id}"


    companion object : Schematable {

        private val lastId = AtomicInteger(0)
        fun random(year: Int, subject: Subject, teacher: Teacher, student: Student): Grade {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            return Grade(lastId.getAndIncrement(), MyCalendar(calendar), RandomDataGenerator.randomGrade(), subject, teacher, student)
        }
        override val tableName: String = "Grade"
        override val schema: String = "CREATE TABLE $tableName\n" +
                "(\n" +
                "    ID INTEGER IDENTITY(1, 1) PRIMARY KEY,\n" +
                "    Year INTEGER NOT NULL,\n" +
                "    Grade FLOAT NOT NULL,\n" +
                "    Subject_ID INTEGER NOT NULL FOREIGN KEY REFERENCES Subject,\n" +
                "    Teacher_ID INTEGER NOT NULL FOREIGN KEY REFERENCES Teacher,\n" +
                "    Student_ID INTEGER NOT NULL FOREIGN KEY REFERENCES Student,\n" +
                ")"


    }
}


