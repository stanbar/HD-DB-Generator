package generator

import generator.data.*
import generator.data.relations.Examination
import java.io.FileWriter

fun main(args: Array<String>) {
    buildOriginState()
    generateSchema()
    generateInserts()
}


fun buildOriginState() {
    val state = State(2015)
    with(state) {
        generateTeachersWithSubjectRelations()
        generateClassesWithSubjectRelations()
        generateKlassToSubjectRelations()
        generateStudentsForEachKlass()
        generateMarksForeachStudent()
        generateExamsWithStudentRelations()
        dumpBulks()
    }


}
fun generateInserts(){
    val entities = arrayOf("Subject","Class","Teacher","Grade","Student","Exam","Examination","SubjectTeacherRel","SubjectKlassRel")
    FileWriter("inserts.sql").use { writer ->
        entities.forEach { writer.appendln("DELETE FROM $it") }
        entities.forEach { writer.appendln("BULK INSERT dbo.$it FROM '/home/bulks/$it.bulk' WITH (FIELDTERMINATOR=';')") }
    }
}

fun generateSchema() {

    FileWriter("schema.sql").use {
        it.appendln("DROP TABLE IF EXISTS SubjectKlassRel")
        it.appendln("DROP TABLE IF EXISTS SubjectTeacherRel")
        it.appendln("DROP TABLE IF EXISTS Examination")
        it.appendln("DROP TABLE IF EXISTS Subject")
        it.appendln("DROP TABLE IF EXISTS Class")
        it.appendln("DROP TABLE IF EXISTS Teacher")
        it.appendln("DROP TABLE IF EXISTS Grade")
        it.appendln("DROP TABLE IF EXISTS Student")
        it.appendln("DROP TABLE IF EXISTS Exam")
        it.appendln(Subject.schema)
        it.appendln(Klass.schema)
        it.appendln(Teacher.schema)
        it.appendln(Student.schema)
        it.appendln(Grade.schema)
        it.appendln(Exam.schema)
        it.appendln(Examination.schema)
    }
}
