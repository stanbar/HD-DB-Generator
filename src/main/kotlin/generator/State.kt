package generator

import generator.data.*
import generator.data.relations.SubjectKlassRel
import generator.data.relations.SubjectTeacherRel
import java.io.File
import java.io.FileWriter
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.collections.ArrayList

fun Collection<Insertable>.dump(fileName: String) {
    FileWriter(fileName).use { writer ->
        forEachIndexed { index, it ->
            writer.append(it.toInsert() + "|")
            if (shouldPrintNewLine(index, size)) writer.appendln()
        }
    }
}


open class State
(var year: Int, protected val classSize: Int = defaultClassSize, protected val maxTeachersPerSubject: Int = defaultMaxTeachers, protected val minTeachersPerSubject: Int = defaultMinTeachers) {

    protected val subjects = arrayListOf(Subject("J. Polski"), Subject("Matematyka"), Subject("Biologia")
            , Subject("Chemia"), Subject("Informatyka"), Subject("J. Angielski"), Subject("Fizyka"))


    protected val subjectTeachers = HashMap<Subject, LinkedList<Teacher>>()
    protected val subjectTeacherRel = LinkedList<SubjectTeacherRel>()

    protected val klasses = HashMap<Int, Klass>()
    protected val klassSubjects = LinkedList<SubjectKlassRel>()
    protected val teachers: HashMap<String, Teacher>
        get() {
            val teachers = HashMap<String, Teacher>()
            subjectTeachers.forEach { subject, teachersList -> teachersList.forEach { teachers[it.pesel] = it } }
            return teachers
        }

    protected val grades = HashMap<Int, Grade>()
    protected val students = HashMap<String, Student>()

    protected val exams = HashMap<Int, Exam>()

    init {
        subjects.forEach { subjectTeachers.put(it, LinkedList()) }
    }


    fun dumpBulks() {
        File("bulks").mkdirs()
        subjects.dump("bulks/${Subject.tableName}.bulk")
        teachers.values.dump("bulks/${Teacher.tableName}.bulk")
        klasses.values.dump("bulks/${Klass.tableName}.bulk")
        grades.values.dump("bulks/${Grade.tableName}.bulk")
        students.values.dump("bulks/${Student.tableName}.bulk")
        exams.values.dump("bulks/${Exam.tableName}.bulk")
        subjectTeacherRel.dump("bulks/${SubjectTeacherRel.tableName}.bulk")
        klassSubjects.dump("bulks/${SubjectKlassRel.tableName}.bulk")
    }


    protected fun generateTutorForKlass(): Teacher {
        val randSubject = subjects[ThreadLocalRandom.current().nextInt(subjects.size)]
        val teachers = subjectTeachers[randSubject]!!
        return teachers[ThreadLocalRandom.current().nextInt(teachers.size)]
    }

    protected fun getTeacherFor(subject: Subject, klass: Klass): Teacher {
        klassSubjects.filter { it.subject.id == subject.id && klass.id == it.klass.id }
                .forEach { return it.teacher }
        throw IllegalStateException("Could not find $subject and $klass.id relation")
    }


    fun generateMarksForeachStudent(): List<Grade> {
        val updateList = ArrayList<Grade>()
        students.forEach { pesel, student ->
            if (klasses[student.klass.id]!!.currentLevel(year) > 4)
                return@forEach
            subjects.forEach { subject ->
                val teacherPesel = getTeacherFor(subject, student.klass)

                val grade = Grade.random(year, subject, teacherPesel, student)
                grades.put(grade.id, grade)
                updateList.add(grade)
            }
        }
        return updateList
    }


    fun generateExamsWithStudentRelations(): List<Exam> {
        val updates = ArrayList<Exam>()
        subjects.forEach { subject ->
            students.forEach { pesel, student ->
                if (klasses[student.klass.id]!!.currentLevel(year) != 4) // Only 4 level classes take exams
                    return@forEach
                val teacher = getTeacherFor(subject, student.klass)
                val exam = Exam.random(student,
                        subject,
                        teacher,
                        year)
                exams[exam.id] = exam
                updates += exam
            }
        }
        return updates
    }


    fun generateFirstClasses(): List<Klass> {
        val updateList = ArrayList<Klass>()
        for (sign in CharRange('A', 'C')) {
            val tutor = generateTutorForKlass()
            val klass = Klass(sign, tutor.pesel, year)
            klasses.put(klass.id, klass)
            updateList.add(klass)
        }
        return updateList
    }

    fun generateKlassToSubjectRelationsForFirstClasses(): List<SubjectKlassRel> {
        val updateList = ArrayList<SubjectKlassRel>()
        klasses.forEach { id, klass ->
            if (klass.currentLevel(year) > 1)
                return@forEach

            subjects.map { subject ->
                val teachers = subjectTeachers[subject]!!
                val teacher = teachers.get(ThreadLocalRandom.current().nextInt(teachers.size))
                SubjectKlassRel(subject, klass, teacher)
            }.forEach {
                        updateList.add(it)
                        klassSubjects += it
                    }
        }
        return updateList
    }

    fun generateTeacherUpdateExpirience(): List<Teacher> {
        val updateList = ArrayList<Teacher>()
        teachers.forEach { t, u -> u.updateExperience(year); updateList.add(u) }
        return updateList
    }

    fun generateStudentsForEachFirstKlass(): List<Student> {
        val updatedStudents = ArrayList<Student>()
        for (klass in klasses) {
            if (klass.value.currentLevel(year) > 1)
                continue
            var superVisorPesel: String? = null
            repeat(classSize) {
                val student = Student.random(klass.value, superVisorPesel)
                if (superVisorPesel == null)
                    superVisorPesel = student.pesel

                //TODO supervisor end null or himself
                students.put(student.pesel, student)
                updatedStudents.add(student)
            }
        }
        return updatedStudents
    }


    fun updateRandomTeachers(updateCount: Int): List<Teacher> {
        val updates = ArrayList<Teacher>()
        repeat(updateCount) {
            val randSubject = subjects[ThreadLocalRandom.current().nextInt(subjects.size)]
            val teachers = subjectTeachers[randSubject]!!
            val randomTeacher = teachers[ThreadLocalRandom.current().nextInt(teachers.size)]
            updates.add(randomTeacher.updateTitle())
        }
        return updates
    }

    fun updateRandomStudents(updateCount: Int): List<Student> {
        val updates = ArrayList<Student>()

        repeat(updateCount) {
            val studentsList = ArrayList(students.values)
            val randomStudent = studentsList[ThreadLocalRandom.current().nextInt(studentsList.size)]
            updates.add(randomStudent.updateSurname())
        }
        return updates

    }

}

