package generator.data

import java.util.concurrent.atomic.AtomicInteger

data class Grade(
        val id: Int = lastId.getAndIncrement(),
        private val year: Int,
        private val month: Int,
        private val grade: Double,
        private val subject: Subject,
        private val teacher: Teacher,
        private val student: Student) : Insertable {
    override fun toInsert(): String = "$id;$year;$month;$grade;${subject.id};${teacher.pesel};${student.pesel}"

    companion object : Schematable {
        private val lastId = AtomicInteger(1)
        override val primaryKey: String = "ID"
        override val tableName: String = "Grade"
        override val schema: String = "CREATE TABLE $tableName\n" +
                "(\n" +
                "    ID INTEGER IDENTITY(1, 1) PRIMARY KEY,\n" +
                "    Year INTEGER NOT NULL,\n" +
                "    Month INTEGER NOT NULL,\n" +
                "    Grade FLOAT NOT NULL,\n" +
                "    Subject_ID INTEGER NOT NULL FOREIGN KEY REFERENCES Subject,\n" +
                "    Teacher_PESEL varchar(100) NOT NULL FOREIGN KEY REFERENCES Teacher,\n" +
                "    Student_PESEL varchar(100) NOT NULL FOREIGN KEY REFERENCES Student,\n" +
                ")"


    }
}


