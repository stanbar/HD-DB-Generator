package generator

import generator.data.*
import generator.data.relations.SubjectKlassRel
import generator.data.relations.SubjectTeacherRel
import java.io.File
import java.io.FileWriter
import java.util.concurrent.ThreadLocalRandom
import kotlin.reflect.full.companionObjectInstance

val defaultClassSize = 30
val defaultMaxTeachers = 5
val defaultMinTeachers = 3

var bulksDir = File(System.getProperty("user.dir"), "bulks")

val dbName = "schoolGenerated"
val FIELD_TERMINATOR = ";"
val ROW_TERMINATOR = "|"
val BULK_OPTIONS = "(FIELDTERMINATOR='$FIELD_TERMINATOR', ROWTERMINATOR='$ROW_TERMINATOR')"

fun main(args: Array<String>) {
    bulksDir.mkdirs()

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
        generateExamsWithStudentRelations()
        dumpBulks()
    }
    generateSchema()
    generateInserts()

    return state
}

fun generateSchema() {

    FileWriter("schema.sql").use {
        it.appendln("/*")
        it.appendln("DROP TABLE IF EXISTS ${SubjectKlassRel.tableName}")
        it.appendln("DROP TABLE IF EXISTS ${SubjectTeacherRel.tableName}")
        it.appendln("DROP TABLE IF EXISTS ${Subject.tableName}")
        it.appendln("DROP TABLE IF EXISTS ${Klass.tableName}")
        it.appendln("DROP TABLE IF EXISTS ${Teacher.tableName}")
        it.appendln("DROP TABLE IF EXISTS ${Grade.tableName}")
        it.appendln("DROP TABLE IF EXISTS ${Student.tableName}")
        it.appendln("DROP TABLE IF EXISTS ${Exam.tableName}")
        it.appendln("*/")
        it.appendln(Subject.schema)
        it.appendln(MyCalendar.schema)
        it.appendln(Teacher.schema)
        it.appendln(Klass.schema)
        it.appendln(Student.schema)
        it.appendln(Grade.schema)
        it.appendln(Exam.schema)

        it.appendln(SubjectTeacherRel.schema)
        it.appendln(SubjectKlassRel.schema)
    }
}


fun generateInserts() {
    val entities = arrayOf(Subject::class, Klass::class, Teacher::class, Grade::class, Student::class,
            Exam::class, SubjectTeacherRel::class, SubjectKlassRel::class).map { it.companionObjectInstance as Schematable }

    FileWriter("inserts.sql").use { writer ->
        writer.appendln("USE $dbName")
        writer.appendln("/*")
        entities.forEach { writer.appendln("DELETE FROM ${it.tableName}") }
        writer.appendln("*/")
        entities.forEach { writer.appendln("BULK INSERT dbo.${it.tableName} FROM '${File(bulksDir, "${it.tableName}.bulk").absolutePath}' WITH $BULK_OPTIONS") }
    }
}


fun buildUpdate(state: State) {
    state.year++
    val entities = arrayOf(Klass::class, Grade::class, Student::class, Exam::class, SubjectKlassRel::class, Teacher::class)
            .map { it.companionObjectInstance as Schematable }


    with(state) {
        generateFirstClasses().dump("bulks/${Klass.tableName}${state.year}.bulk")
        generateMarksForeachStudent().dump("bulks/${Grade.tableName}${state.year}.bulk")
        generateStudentsForEachFirstKlass().dump("bulks/${Student.tableName}${state.year}.bulk")
        generateExamsWithStudentRelations().dump("bulks/${Exam.tableName}${state.year}.bulk")
        generateKlassToSubjectRelationsForFirstClasses().dump("bulks/${SubjectKlassRel.tableName}${state.year}.bulk")
        generateTeacherUpdateExpirience().dump("bulks/${Teacher.tableName}${state.year}.bulk")
    }
    FileWriter("inserts${state.year}.sql").use { writer ->
        entities.forEachIndexed { index, it ->
            writer.append("BULK INSERT dbo.${it.tableName} FROM '${File(bulksDir, "${it.tableName}.${state.year}.bulk").absolutePath}' WITH $BULK_OPTIONS")
            if (shouldPrintNewLine(index, entities.size)) writer.append('\n')
        }
    }

    updateSlowyChangedVariable(state)
}

fun updateSlowyChangedVariable(state: State, updateCount: Int = ThreadLocalRandom.current().nextInt(1, 30)) {
    val updatedTeachers = state.updateRandomTeachers(updateCount)
    val updatedStudents = state.updateRandomStudents(updateCount)

    FileWriter("teacherUpdates${state.year}.sql").use { writer ->
        updatedTeachers.forEachIndexed { index, teacher ->
            writer.append("UPDATE dbo.Teacher SET Title = '${teacher.title}' WHERE PESEL = '${teacher.pesel}'")
            if (shouldPrintNewLine(index, updatedTeachers.size)) writer.append('\n')
        }
    }

    FileWriter("studentsUpdates${state.year}.sql").use { writer ->
        updatedStudents.forEachIndexed { index, student ->
            writer.append("UPDATE dbo.Student SET Surname = '${student.surname}' WHERE PESEL = '${student.pesel}'")
            if (shouldPrintNewLine(index, updatedStudents.size)) writer.append('\n')
        }
    }

}

fun shouldPrintNewLine(index: Int, size: Int) = index != size - 1

