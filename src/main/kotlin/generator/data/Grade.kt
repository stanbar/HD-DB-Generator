package generator.data

import generator.RandomDataGenerator
import java.util.concurrent.atomic.AtomicInteger

data class Grade(
        val id: Int,
        private val year: Int,
        private val grade: Double,
        private val subject_id: Int,
        private val teacher_pesel: String,
        private val student_pesel: String) : Insertable{
    override fun toInsert(): String = "$id;$year;$grade;$subject_id;$teacher_pesel;$student_pesel"


    companion object : Schematable {

        private val lastId = AtomicInteger(0)
        fun random(year: Int, subject_id: Int, teacher_pesel: String, student_pesel: String): Grade {
            return Grade(lastId.getAndIncrement(), year, RandomDataGenerator.randomGrade(), subject_id, teacher_pesel, student_pesel)
        }

        override val schema: String = "CREATE TABLE Grade\n" +
                "(\n" +
                "    ID INTEGER IDENTITY(1, 1) PRIMARY KEY,\n" +
                "    Year INTEGER NOT NULL,\n" +
                "    Grade FLOAT NOT NULL,\n" +
                "    Subject_ID INTEGER NOT NULL FOREIGN KEY REFERENCES Subject,\n" +
                "    Teacher_PESEL nchar(11) NOT NULL FOREIGN KEY REFERENCES Teacher,\n" +
                "    Student_PESEL nchar(11) NOT NULL FOREIGN KEY REFERENCES Student,\n" +
                ")"


    }
}


