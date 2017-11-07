package generator.data.relations

import generator.data.Insertable
import generator.data.Schematable
import java.util.concurrent.atomic.AtomicInteger

data class SubjectKlassRel(val subject_id: Int,
                           val klass_id: Int,
                           val teacher_pesel: String) : Insertable {
    override fun toInsert() = "$id;$subject_id;$klass_id;$teacher_pesel"

    private val id: Int = lastId.getAndIncrement()

    companion object : Schematable {
        override val schema = "CREATE TABLE SubjectKlassRel\n" +
                "(\n" +
                "    ID INTEGER IDENTITY(1, 1) PRIMARY KEY,\n" +
                "    Subject_ID INTEGER NOT NULL FOREIGN KEY REFERENCES Subject,\n" +
                "    Class_ID INTEGER NOT NULL FOREIGN KEY REFERENCES Class,\n" +
                "    Teacher_PESEL nchar(11) NOT NULL FOREIGN KEY REFERENCES Teacher,\n" +
                ")"
        private val lastId = AtomicInteger(0)
    }
}