USE school

DELETE FROM Subject
DELETE FROM Class
DELETE FROM Teacher
DELETE FROM Grade
DELETE FROM Student
DELETE FROM Exam
DELETE FROM Examination
DELETE FROM SubjectTeacherRel
DELETE FROM SubjectKlassRel
BULK INSERT dbo.Subject FROM '/Users/admin1/GoogleDrive/ProjectsJava/db-generator/bulks/Subject.bulk' WITH (FIELDTERMINATOR=';')
BULK INSERT dbo.Class FROM '/usr/src/bulks/Class.bulk' WITH (FIELDTERMINATOR=';')
BULK INSERT dbo.Teacher FROM '/Users/admin1/GoogleDrive/ProjectsJava/db-generator/bulks/Teacher.bulk' WITH (FIELDTERMINATOR=';')
BULK INSERT dbo.Grade FROM '/Users/admin1/GoogleDrive/ProjectsJava/db-generator/bulks/Grade.bulk' WITH (FIELDTERMINATOR=';')
BULK INSERT dbo.Student FROM '/Users/admin1/GoogleDrive/ProjectsJava/db-generator/bulks/Student.bulk' WITH (FIELDTERMINATOR=';')
BULK INSERT dbo.Exam FROM '/Users/admin1/GoogleDrive/ProjectsJava/db-generator/bulks/Exam.bulk' WITH (FIELDTERMINATOR=';')
BULK INSERT dbo.Examination FROM '/Users/admin1/GoogleDrive/ProjectsJava/db-generator/bulks/Examination.bulk' WITH (FIELDTERMINATOR=';')
BULK INSERT dbo.SubjectTeacherRel FROM '/Users/admin1/GoogleDrive/ProjectsJava/db-generator/bulks/SubjectTeacherRel.bulk' WITH (FIELDTERMINATOR=';')
BULK INSERT dbo.SubjectKlassRel FROM '/Users/admin1/GoogleDrive/ProjectsJava/db-generator/bulks/SubjectKlassRel.bulk' WITH (FIELDTERMINATOR=';')
