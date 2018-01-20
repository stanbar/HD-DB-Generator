USE schoolGenerated
/*
DELETE FROM Subject
DELETE FROM Class
DELETE FROM Teacher
DELETE FROM Grade
DELETE FROM Student
DELETE FROM Exam
DELETE FROM Examination
DELETE FROM SubjectTeacherRel
DELETE FROM SubjectKlassRel
*/
BULK INSERT dbo.Subject FROM '/Users/admin1/GoogleDrive/JavaProjects/db-generator/bulks/Subject.bulk' WITH (FIELDTERMINATOR=';', ROWTERMINATOR='|')
BULK INSERT dbo.Class FROM '/Users/admin1/GoogleDrive/JavaProjects/db-generator/bulks/Class.bulk' WITH (FIELDTERMINATOR=';', ROWTERMINATOR='|')
BULK INSERT dbo.Teacher FROM '/Users/admin1/GoogleDrive/JavaProjects/db-generator/bulks/Teacher.bulk' WITH (FIELDTERMINATOR=';', ROWTERMINATOR='|')
BULK INSERT dbo.Grade FROM '/Users/admin1/GoogleDrive/JavaProjects/db-generator/bulks/Grade.bulk' WITH (FIELDTERMINATOR=';', ROWTERMINATOR='|')
BULK INSERT dbo.Student FROM '/Users/admin1/GoogleDrive/JavaProjects/db-generator/bulks/Student.bulk' WITH (FIELDTERMINATOR=';', ROWTERMINATOR='|')
BULK INSERT dbo.Exam FROM '/Users/admin1/GoogleDrive/JavaProjects/db-generator/bulks/Exam.bulk' WITH (FIELDTERMINATOR=';', ROWTERMINATOR='|')
BULK INSERT dbo.Examination FROM '/Users/admin1/GoogleDrive/JavaProjects/db-generator/bulks/Examination.bulk' WITH (FIELDTERMINATOR=';', ROWTERMINATOR='|')
BULK INSERT dbo.SubjectTeacherRel FROM '/Users/admin1/GoogleDrive/JavaProjects/db-generator/bulks/SubjectTeacherRel.bulk' WITH (FIELDTERMINATOR=';', ROWTERMINATOR='|')
BULK INSERT dbo.SubjectKlassRel FROM '/Users/admin1/GoogleDrive/JavaProjects/db-generator/bulks/SubjectKlassRel.bulk' WITH (FIELDTERMINATOR=';', ROWTERMINATOR='|')
