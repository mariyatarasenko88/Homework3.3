select * from student;
select * from student where age between 10 and 20;
select student.name from student;
select * from student where name like '%о%';
select * from student where age < id;
select  * from student order by age;

SELECT count(*) FROM student;

SELECT AVG(age) FROM student;

SELECT * FROM student ORDER BY id DESC LIMIT 5;

SELECT * FROM avatar ORDER BY id LIMIT 2 OFFSET 4;
