package generator.data.relations

import generator.data.Insertable
import generator.data.Schematable
import java.util.concurrent.atomic.AtomicInteger

class SubjectTeacherRel(private val teacher_pesel: String,
                        private val subject_id: Int) : Insertable {
    override fun toInsert() = "$id;$teacher_pesel;$subject_id"

    private val id: Int = lastId.getAndIncrement()

    companion object : Schematable {

        private val lastId = AtomicInteger(0)
        override val schema: String = "CREATE TABLE SubjectTeacherRel\n" +
                "(\n" +
                "    ID INTEGER IDENTITY(1, 1) PRIMARY KEY,\n" +
                "    Subject_ID INTEGER NOT NULL FOREIGN KEY REFERENCES Subject,\n" +
                "    Teacher_PESEL nchar(11) NOT NULL FOREIGN KEY REFERENCES Teacher,\n" +
                ")"
    }
}

