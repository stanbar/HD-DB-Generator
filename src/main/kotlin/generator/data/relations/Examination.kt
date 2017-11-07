package generator.data.relations

import generator.data.Insertable
import generator.data.Schematable
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.atomic.AtomicInteger

data class Examination(
        private val student_pesel: String,
        private val exam_id: Int,
        private val year: Int,
        private val result: Double) :Insertable {
    override fun toInsert() = "$id;$student_pesel;$exam_id;$year;$result"


    val id: Int = lastId.getAndIncrement()

    companion object : Schematable {

        private val lastId = AtomicInteger(0)
        fun random(student_pesel: String, exam_id: Int, year: Int)
                = Examination(student_pesel, exam_id, year
                , ThreadLocalRandom.current().nextDouble(0.0, 100.0))

        override val schema: String = "CREATE TABLE Examination\n" +
                "(\n" +
                "    ID INTEGER IDENTITY(1, 1) PRIMARY KEY,\n" +
                "    Year INTEGER NOT NULL,\n" +
                "    Result FLOAT NOT NULL,\n" +
                "    Student_PESEL nchar(11) NOT NULL FOREIGN KEY REFERENCES Student,\n" +
                "    Exam_ID INTEGER NOT NULL FOREIGN KEY REFERENCES Exam\n" +
                ")"
    }

}