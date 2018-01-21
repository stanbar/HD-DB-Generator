package generator.data

import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicInteger

data class Exam(
        private val result: Double,
        private val date: MyCalendar,
        private val student: Student,
        private val subject: Subject,
        private val teacher: Teacher) : Insertable {

    override fun toInsert(): String = "$id;$result;${date.id};${student.id};${subject.id};${teacher.id}"

    val id: Int = lastId.getAndIncrement()

    companion object : Schematable {
        override val tableName: String = "Exam"
        private val lastId = AtomicInteger(1)

        fun random(student: Student, subject: Subject, teacher: Teacher,  calendar : MyCalendar): Exam {


            return Exam(
                    result = ThreadLocalRandom.current().nextDouble(0.0, 100.0),
                    date = calendar,
                    student = student,
                    subject = subject,
                    teacher = teacher)
        }

        override val schema: String = "CREATE TABLE $tableName\n" +
                "(\n" +
                "    ID INTEGER IDENTITY(1, 1) PRIMARY KEY,\n" +
                "    Result FLOAT NOT NULL,\n" +
                "    Date_ID INTEGER NOT NULL FOREIGN KEY REFERENCES MyCalendar,\n" +
                "    Student_ID INTEGER NOT NULL FOREIGN KEY REFERENCES Student,\n" +
                "    Subject_ID INTEGER NOT NULL FOREIGN KEY REFERENCES Subject,\n" +
                "    Teacher_ID INTEGER NOT NULL FOREIGN KEY REFERENCES Teacher,\n" +
                ")"
    }
}
