package generator

import generator.data.*
import generator.data.relations.Examination
import generator.data.relations.SubjectKlassRel
import generator.data.relations.SubjectTeacherRel
import java.io.FileWriter
import java.util.concurrent.ThreadLocalRandom

val defaultClassSize = 30
val defaultMaxTeachers = 5


fun main(args: Array<String>) {
    val originState = buildOriginState(2014)
    buildUpdate(originState) // 2015
    buildUpdate(originState) // 2016
    buildUpdate(originState) // 2017
}


fun buildOriginState(originYear: Int): OriginState {
    val state = OriginState(originYear)
    with(state) {
        generateTeachersWithSubjectRelations()
        generateClassesWithSubjectRelations()
        generateKlassToSubjectRelations()
        generateStudentsForEachKlass()
        generateMarksForeachStudent()
        generateExams()
        generateExamsWithStudentRelations()
        dumpBulks()
    }
    generateSchema()
    generateInserts()

    return state
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
        it.appendln(Teacher.schema)
        it.appendln(Klass.schema)
        it.appendln(Student.schema)
        it.appendln(Grade.schema)
        it.appendln(Exam.schema)
        it.appendln(Examination.schema)
        it.appendln(SubjectTeacherRel.schema)
        it.appendln(SubjectKlassRel.schema)
    }
}

val bulksPath = "/Users/admin1/GoogleDrive/ProjectsJava/db-generator/bulks/"

fun generateInserts() {
    val entities = arrayOf("Subject", "Class", "Teacher", "Grade", "Student", "Exam", "Examination", "SubjectTeacherRel", "SubjectKlassRel")

    FileWriter("inserts.sql").use { writer ->
        writer.appendln("USE school")
        entities.forEach { writer.appendln("DELETE FROM $it") }
        entities.forEach { writer.appendln("BULK INSERT dbo.$it FROM '$bulksPath$it.bulk' WITH (FIELDTERMINATOR=';')") }
    }
}


fun buildUpdate(state: State) {
    state.year++
    val entities = arrayOf("Class", "Grade", "Student", "Exam", "Examination", "SubjectKlassRel")

    with(state) {
        generateFirstClasses().dump("bulks/Class${state.year}.bulk")
        generateKlassToSubjectRelationsForFirstClasses().dump("bulks/SubjectKlassRel${state.year}.bulk")
        generateStudentsForEachFirstKlass().dump("bulks/Student${state.year}.bulk")
        generateMarksForeachStudent().dump("bulks/Grade${state.year}.bulk")
        generateExams().dump("bulks/Exam${state.year}.bulk")
        generateExamsWithStudentRelations().dump("bulks/Examination${state.year}.bulk")
    }
    FileWriter("inserts${state.year}.sql").use { writer ->
        entities.forEach { writer.appendln("BULK INSERT dbo.$it FROM '$bulksPath$it${state.year}.bulk' WITH (FIELDTERMINATOR=';')") }
    }

    updateSlowyChangedVariable(state)
}

fun updateSlowyChangedVariable(state: State, updateCount: Int = ThreadLocalRandom.current().nextInt(1, 30)) {
    val updatedTeachers = state.updateRandomTeachers(updateCount)
    val updatedStudents = state.updateRandomStudents(updateCount)

    FileWriter("teacherUpdates${state.year}.sql").use { writer ->
        updatedTeachers.forEach { teacher -> writer.appendln("UPDATE dbo.Teacher SET Title = '${teacher.title}' WHERE PESEL = '${teacher.pesel}'") }
    }

    FileWriter("studentsUpdates${state.year}.sql").use { writer ->
        updatedStudents.forEach { student -> writer.appendln("UPDATE dbo.Student SET Surname = '${student.surname}' WHERE PESEL = '${student.pesel}'") }
    }

}


