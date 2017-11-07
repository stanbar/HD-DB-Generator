DELETE FROM Subject
DELETE FROM Class
DELETE FROM Teacher
DELETE FROM Grade
DELETE FROM Student
DELETE FROM Exam
DELETE FROM Examination
DELETE FROM SubjectTeacherRel
DELETE FROM SubjectKlassRel
BULK INSERT dbo.Subject FROM '/home/bulks/Subject.bulk' WITH (FIELDTERMINATOR=';')
BULK INSERT dbo.Class FROM '/home/bulks/Class.bulk' WITH (FIELDTERMINATOR=';')
BULK INSERT dbo.Teacher FROM '/home/bulks/Teacher.bulk' WITH (FIELDTERMINATOR=';')
BULK INSERT dbo.Grade FROM '/home/bulks/Grade.bulk' WITH (FIELDTERMINATOR=';')
BULK INSERT dbo.Student FROM '/home/bulks/Student.bulk' WITH (FIELDTERMINATOR=';')
BULK INSERT dbo.Exam FROM '/home/bulks/Exam.bulk' WITH (FIELDTERMINATOR=';')
BULK INSERT dbo.Examination FROM '/home/bulks/Examination.bulk' WITH (FIELDTERMINATOR=';')
BULK INSERT dbo.SubjectTeacherRel FROM '/home/bulks/SubjectTeacherRel.bulk' WITH (FIELDTERMINATOR=';')
BULK INSERT dbo.SubjectKlassRel FROM '/home/bulks/SubjectKlassRel.bulk' WITH (FIELDTERMINATOR=';')
