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

class OriginState(var year: Int, private val classSize: Int = defaultClassSize, private val maxTeachersPerSubject: Int = defaultMaxTeachers) {

    private val subjects = arrayListOf(Subject("J. Polski"), Subject("Matematyka"), Subject("Biologia")
            , Subject("Chemia"), Subject("Informatyka"), Subject("J. Angielski"), Subject("Fizyka"))


    private val subjectTeachers = HashMap<Subject, LinkedList<Teacher>>()
    private val subjectTeacherRel = LinkedList<SubjectTeacherRel>()

    private val klasses = HashMap<Int, Klass>()
    private val klassSubjects = LinkedList<SubjectKlassRel>()

    private val grades = HashMap<Int, Grade>()
    private val students = HashMap<String, Student>()

    private val exams = HashMap<Int, Exam>()
    private val studentExams = LinkedList<Examination>()

    init {
        subjects.forEach { subjectTeachers.put(it, LinkedList()) }
    }

    private fun Collection<Insertable>.dump(fileName: String) {
        FileWriter(fileName).use { writer ->
            forEach { writer.appendln(it.toInsert()) }
        }
    }

    fun dumpBulks() {
        File("bulks").mkdirs()
        subjects.dump("bulks/subjects.bulk")
        subjectTeachers.forEach { t, u -> u.dump("bulks/teachers.bulk") }
        klasses.values.dump("bulks/classes.bulk")
        grades.values.dump("bulks/grades.bulk")
        students.values.dump("bulks/students.bulk")
        exams.values.dump("bulks/exams.bulk")
        subjectTeacherRel.dump("bulks/subjectTeacherRel.bulk")
        klassSubjects.dump("bulks/klassSubjects.bulk")
        studentExams.dump("bulks/studentExams.bulk")
    }

    //
    fun generateTeachersWithSubjectRelations() {
        for (subject in subjects) {
            repeat(ThreadLocalRandom.current().nextInt(1, maxTeachersPerSubject)) {
                val teacher = Teacher.random()

                subjectTeachers[subject]?.add(teacher)
                val relation = SubjectTeacherRel(teacher.pesel, subject.id)
                subjectTeacherRel += relation
            }
        }
    }

    fun generateClassesWithSubjectRelations() {
        for (level in 1..4) {
            for (sign in CharRange('A', 'C')) {
                val tutor = generateTutorForKlass()
                val klass = Klass(level, sign, tutor.pesel, year)
                klasses.put(klass.id, klass)
            }
        }
    }


    private fun generateTutorForKlass(): Teacher {
        val randSubject = subjects[ThreadLocalRandom.current().nextInt(subjects.size)]
        val teachers = subjectTeachers[randSubject]!!
        return teachers[ThreadLocalRandom.current().nextInt(teachers.size)]
    }

    fun generateKlassToSubjectRelations() {
        klasses.forEach { id, klass ->
            subjects.map { subject ->
                val teachers = subjectTeachers[subject]!!
                val teacher = teachers.get(ThreadLocalRandom.current().nextInt(teachers.size))
                SubjectKlassRel(subject.id, klass.id, teacher.pesel)
            }.forEach { klassSubjects += it }
        }
    }

    fun generateStudentsForEachKlass() {
        for (klass in klasses) {
            repeat(classSize) {
                val student = Student.random(klass.key)
                students.put(student.pesel, student)
            }
        }
    }

    fun generateMarksForeachStudent() {
        students.forEach { pesel, student ->
            subjects.forEach { subject ->
                val teacherPesel = getTeacherFor(subject, student.class_id)
                val grade = Grade.random(year, subject.id, teacherPesel, student.pesel)
                grades.put(grade.id, grade)
            }


        }
    }


    private fun getTeacherFor(subject: Subject, klass_id: Int): String {
        klassSubjects.filter { it.subject_id == subject.id && klass_id == it.klass_id }
                .forEach { return it.teacher_pesel }
        throw IllegalStateException("Could not find $subject and $klass_id relation")
    }

    fun generateExamsWithStudentRelations() {
        students.forEach { pesel, student ->
            if (klasses[student.class_id]!!.level != 4) // Only 4 level classes take exams
                return@forEach

            subjects.forEach { subject ->
                val exam = Exam(subject.name, year)
                val examRelation = Examination.random(student.pesel, exam.id, year)
                exams.put(exam.id, exam)
                studentExams += examRelation

            }
        }
    }

    fun nextYear() {
        generateFirstClasses()
        updateRandomTeachers(10)
        updateRandomTeachers(10)

    }

    private fun generateFirstClasses() {

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

