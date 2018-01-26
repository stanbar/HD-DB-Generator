package generator.data.relations

import generator.data.Insertable
import generator.data.Schematable
import generator.data.Subject
import generator.data.Teacher
import java.util.concurrent.atomic.AtomicInteger

class SubjectTeacherRel(private val teacher: Teacher,
                        private val subject: Subject) : Insertable {
    override fun toInsert() = "$id;${subject.id};${teacher.pesel}"

    private val id: Int = lastId.getAndIncrement()

    companion object : Schematable {
        override val primaryKey: String = "ID"
        override val tableName: String = "SubjectTeacherRel"
        private val lastId = AtomicInteger(1)
        override val schema: String = "CREATE TABLE $tableName\n" +
                "(\n" +
                "    ID INTEGER IDENTITY(1, 1) PRIMARY KEY,\n" +
                "    Subject_ID INTEGER NOT NULL FOREIGN KEY REFERENCES Subject,\n" +
                "    Teacher_PESEL varchar(100) NOT NULL FOREIGN KEY REFERENCES Teacher,\n" +
                ")"
    }
}

