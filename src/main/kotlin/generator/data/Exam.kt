package generator.data

import generator.RandomDataGenerator
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicInteger

data class Exam(
        private val result: Double,
        private val year: Int,
        private val month: Int,
        private val student: Student,
        private val subject: Subject,
        private val teacher: Teacher) : Insertable {

    override fun toInsert(): String = "$id;$result;$year;$month;${student.pesel};${subject.id};${teacher.pesel}"

    val id: Int = lastId.getAndIncrement()

    companion object : Schematable {
        override val primaryKey: String = "ID"
        override val tableName: String = "Exam"
        private val lastId = AtomicInteger(1)

        fun random(student: Student, subject: Subject, teacher: Teacher, year: Int): Exam {


            return Exam(
                    result = ThreadLocalRandom.current().nextDouble(0.0, 100.0),
                    month = RandomDataGenerator.randomMonth(),
                    year = year,
                    student = student,
                    subject = subject,
                    teacher = teacher)
        }

        override val schema: String = "CREATE TABLE $tableName\n" +
                "(\n" +
                "    ID INTEGER IDENTITY(1, 1) PRIMARY KEY,\n" +
                "    Result FLOAT NOT NULL,\n" +
                "    Year INTEGER NOT NULL,\n" +
                "    Month INTEGER NOT NULL,\n" +
                "    Student_PESEL varchar(100) NOT NULL FOREIGN KEY REFERENCES Student,\n" +
                "    Subject_ID INTEGER NOT NULL FOREIGN KEY REFERENCES Subject,\n" +
                "    Teacher_PESEL varchar(100) NOT NULL FOREIGN KEY REFERENCES Teacher,\n" +
                ")"
    }
}
