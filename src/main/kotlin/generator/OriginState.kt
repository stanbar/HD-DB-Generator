package generator

import generator.data.Klass
import generator.data.Student
import generator.data.Teacher
import generator.data.relations.SubjectKlassRel
import generator.data.relations.SubjectTeacherRel
import java.util.concurrent.ThreadLocalRandom

class OriginState(year: Int, classSize: Int = defaultClassSize, maxTeachersPerSubject: Int = defaultMaxTeachers)
    : State(year, classSize, maxTeachersPerSubject) {

    fun generateStudentsForEachKlass() {
        for (klass in klasses) {
            repeat(classSize) {
                val student = Student.random(klass.key)
                students.put(student.pesel, student)
            }
        }
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

    /**
     * We assume that teachers will work in this school forever
     */
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


    /**
     * This is the first time
     */
    fun generateClassesWithSubjectRelations() {
        for (level in 1..4) {
            for (sign in CharRange('A', 'C')) {
                val tutor = generateTutorForKlass()
                val klass = Klass(sign, tutor.pesel, year - level + 1)
                klasses.put(klass.id, klass)
            }
        }
    }

}