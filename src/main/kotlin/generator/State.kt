package generator

import generator.data.*
import generator.data.relations.Examination
import generator.data.relations.SubjectKlassRel
import generator.data.relations.SubjectTeacherRel
import java.io.File
import java.io.FileWriter
import java.util.*
import java.util.concurrent.ThreadLocalRandom
import kotlin.collections.ArrayList

fun Collection<Insertable>.dump(fileName: String) {
    FileWriter(fileName).use { writer ->
        forEach { writer.appendln(it.toInsert()) }
    }
}

open class State(var year: Int, protected val classSize: Int = defaultClassSize, protected val maxTeachersPerSubject: Int = defaultMaxTeachers) {

    protected val subjects = arrayListOf(Subject("J. Polski"), Subject("Matematyka"), Subject("Biologia")
            , Subject("Chemia"), Subject("Informatyka"), Subject("J. Angielski"), Subject("Fizyka"))


    protected val subjectTeachers = HashMap<Subject, LinkedList<Teacher>>()
    protected val subjectTeacherRel = LinkedList<SubjectTeacherRel>()

    protected val klasses = HashMap<Int, Klass>()
    protected val klassSubjects = LinkedList<SubjectKlassRel>()

    protected val grades = HashMap<Int, Grade>()
    protected val students = HashMap<String, Student>()

    protected val exams = HashMap<Int, Exam>()
    protected val examinations = LinkedList<Examination>()

    init {
        subjects.forEach { subjectTeachers.put(it, LinkedList()) }
    }


    fun dumpBulks() {
        File("bulks").mkdirs()
        subjects.dump("bulks/Subject.bulk")
        subjectTeachers.forEach { t, u -> u.dump("bulks/Teacher.bulk") }
        klasses.values.dump("bulks/Class.bulk")
        grades.values.dump("bulks/Grade.bulk")
        students.values.dump("bulks/Student.bulk")
        exams.values.dump("bulks/Exam.bulk")
        subjectTeacherRel.dump("bulks/SubjectTeacherRel.bulk")
        klassSubjects.dump("bulks/SubjectKlassRel.bulk")
        examinations.dump("bulks/Examination.bulk")
    }


    protected fun generateTutorForKlass(): Teacher {
        val randSubject = subjects[ThreadLocalRandom.current().nextInt(subjects.size)]
        val teachers = subjectTeachers[randSubject]!!
        return teachers[ThreadLocalRandom.current().nextInt(teachers.size)]
    }


    fun generateMarksForeachStudent(): List<Grade> {
        val updateList = ArrayList<Grade>()
        students.forEach { pesel, student ->
            if (klasses[student.class_id]!!.currentLevel(year) > 4)
                return@forEach
            subjects.forEach { subject ->
                val teacherPesel = getTeacherFor(subject, student.class_id)
                val grade = Grade.random(year, subject.id, teacherPesel, student.pesel)
                grades.put(grade.id, grade)
                updateList.add(grade)
            }
        }
        return updateList
    }


    protected fun getTeacherFor(subject: Subject, klass_id: Int): String {
        klassSubjects.filter { it.subject_id == subject.id && klass_id == it.klass_id }
                .forEach { return it.teacher_pesel }
        throw IllegalStateException("Could not find $subject and $klass_id relation")
    }

    fun generateExams(): List<Exam> {
        val updates = ArrayList<Exam>()
        subjects.forEach {
            val exam = Exam(it.name, year)
            exams.put(exam.id, exam)
            updates += exam
        }
        return updates
    }

    fun generateExamsWithStudentRelations(): List<Examination> {
        val updates = ArrayList<Examination>()
        exams.forEach { id, exam ->
            students.forEach { pesel, student ->
                if (klasses[student.class_id]!!.currentLevel(year) != 4) // Only 4 level classes take exams
                    return@forEach

                subjects.forEach { subject ->
                    val examRelation = Examination.random(student.pesel, exam.id, year)
                    examinations += examRelation
                    updates += examRelation
                }
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
                SubjectKlassRel(subject.id, klass.id, teacher.pesel)
            }.forEach {
                updateList.add(it)
                klassSubjects += it
            }
        }
        return updateList
    }

    fun generateStudentsForEachFirstKlass(): List<Student> {
        val updatedStudents = ArrayList<Student>()
        for (klass in klasses) {
            if (klass.value.currentLevel(year) > 1)
                continue

            repeat(classSize) {
                val student = Student.random(klass.key)
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

