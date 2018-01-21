package generator

import generator.data.Klass
import generator.data.Student
import generator.data.Teacher
import generator.data.relations.SubjectKlassRel
import generator.data.relations.SubjectTeacherRel
import java.util.concurrent.ThreadLocalRandom

class OriginState(year: Int, classSize: Int = defaultClassSize)
    : State(year, classSize) {

    fun generateStudentsForEachKlass() {
        var supervisorId: Int? = null
        for (klass in klasses) {
            repeat(classSize) {
                val student = Student.random(klass.value, supervisorId = supervisorId)
                if (supervisorId == null)
                    supervisorId = student.id
                students.put(student.id, student)
            }
        }
    }

    fun generateKlassToSubjectRelations() {
        klasses.forEach { id, klass ->
            subjects.map { subject ->
                val teachers = subjectTeachers[subject]!!
                val teacher = teachers.get(ThreadLocalRandom.current().nextInt(teachers.size))
                SubjectKlassRel(subject, klass, teacher)
            }.forEach { klassSubjects += it }
        }
    }

    /**
     * We assume that teachers will work in this school forever
     */
    fun generateTeachersWithSubjectRelations() {
        for (subject in subjects) {
            var teacherSupervisor: Int? = null
            repeat(ThreadLocalRandom.current().nextInt(minTeachersPerSubject, maxTeachersPerSubject)) {
                val teacher = Teacher.random(year, teacherSupervisor, subject) // BUG ??
                if (teacherSupervisor == null)
                    teacherSupervisor = teacher.id

                subjectTeachers[subject]?.add(teacher)
                val relation = SubjectTeacherRel(teacher, subject)
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
                val klass = Klass(sign, tutor, year - level + 1)
                klasses.put(klass.id, klass)
            }
        }
    }

}