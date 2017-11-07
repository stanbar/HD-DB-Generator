package generator

import generator.data.*
import generator.data.relations.Examination
import generator.data.relations.SubjectKlassRel
import generator.data.relations.SubjectTeacherRel
import java.io.File
import java.io.FileWriter
import java.io.Writer
import java.util.*
import java.util.concurrent.ThreadLocalRandom

class State(val year: Int, val classSize: Int = 30, val maxTeachersPersubject: Int = 5) {


    val subjects = arrayListOf(Subject("J. Polski"), Subject("Matematyka"), Subject("Biologia")
            , Subject("Chemia"), Subject("Informatyka"), Subject("J. Angielski"), Subject("Fizyka"))


    val subjectTeachers = HashMap<Subject, LinkedList<Teacher>>()
    val subjectTeacherRel = LinkedList<SubjectTeacherRel>()

    val klasses = HashMap<Int, Klass>()
    val klassSubjects = LinkedList<SubjectKlassRel>()

    val grades = HashMap<Int, Grade>()
    val students = HashMap<String, Student>()

    val exams = HashMap<Int, Exam>()
    val studentExams = LinkedList<Examination>()

    init {
        subjects.forEach { subjectTeachers.put(it, LinkedList()) }
    }

    fun Collection<Insertable>.dump(writer: Writer) {
        forEach { writer.appendln(it.toInsert()) }; writer.close()
    }

    fun dumpBulks() {
        File("bulks").mkdirs()
        subjects.dump(FileWriter("bulks/subjects.bulk"))
        subjectTeachers.forEach { t, u -> u.dump(FileWriter("bulks/teachers.bulk")) }
        klasses.values.dump(FileWriter("bulks/classes.bulk"))
        grades.values.dump(FileWriter("bulks/grades.bulk"))
        students.values.dump(FileWriter("bulks/students.bulk"))
        exams.values.dump(FileWriter("bulks/exams.bulk"))
        subjectTeacherRel.dump(FileWriter("bulks/subjectTeacherRel.bulk"))
        klassSubjects.dump(FileWriter("bulks/klassSubjects.bulk"))
        studentExams.dump(FileWriter("bulks/studentExams.bulk"))
    }


    fun generateTeachersWithSubjectRelations() {
        for (subject in subjects) {
            repeat(ThreadLocalRandom.current().nextInt(1, maxTeachersPersubject)) {
                val teacher = Teacher.random()

                subjectTeachers[subject]?.add(teacher)
                val relation = SubjectTeacherRel(teacher.pesel, subject.id)
                subjectTeacherRel += relation
            }
        }
    }

    fun generateClassesWithSubjectRelations() {
        for (sign in CharRange('A', 'C')) {
            val tutor = generateTutorForKlasse()
            val klass = Klass(sign, tutor.pesel, year)
            klasses.put(klass.id, klass)
        }

    }

    private fun generateTutorForKlasse(): Teacher {
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
        for (rel in klassSubjects)
            if (rel.subject_id == subject.id && klass_id == rel.klass_id) {
                return rel.teacher_pesel
            }
        throw IllegalStateException("Could not find $subject and $klass_id relation")
    }

    fun generateExamsWithStudentRelations() {
        students.forEach { pesel, student ->
            subjects.forEach { subject ->
                val exam = Exam(subject.name, year)
                val examRelation = Examination.random(student.pesel, exam.id, year)
                exams.put(exam.id, exam)
                studentExams += examRelation

            }
        }
    }

}