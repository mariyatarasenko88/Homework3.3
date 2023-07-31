package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;
    private final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private Integer currentStudent = 0;


    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }
    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }
    public Student findStudent(long id) {
        logger.info("Was invoked method for find student by id");
        return studentRepository.findById(id).orElse(null);
    }
    public Student editStudent(Student student) {
        logger.info("Was invoked method for edit student");
        if (findStudent(student.getId()) != null) {
            return studentRepository.save(student);
        }
        return null;
    }
    public void deleteStudent(long id) {
        logger.info("Was invoked method for delete student by id");
        studentRepository.deleteById(id);
    }
    public Collection<Student> getAllStudents() {
        logger.info("Was invoked method for find all students");
        return studentRepository.findAll();
    }
    public Collection<Student> getStudentsByAge(Integer age) {
        logger.info("Was invoked method for find students by age");
        return studentRepository.findStudentByAge(age);
    }
    public Collection<Student> getStudentByAgeBetween(Integer min, Integer max) {
        logger.info("Was invoked method for find students by age between two digits");
        return studentRepository.findStudentByAgeBetween(min, max);
    }
    public Faculty getFacultyByStudent(Long studentId) {
        logger.info("Was invoked method for find faculty by student id");
        Student student = studentRepository.findStudentById(studentId);
        return facultyRepository.findFacultyByStudents(student);
    }
    public int getCountAllStudents() {
        logger.info("Was invoked method for get count students");
        return studentRepository.getCountAllStudents();
    }

    public double getAverageAgeAllStudents() {
        logger.info("Was invoked method for get average age from all students");
        return studentRepository.getAverageAgeAllStudents();
    }

    public Collection<Student> getFiveLastStudents() {
        logger.info("Was invoked method for get five last students");
        return studentRepository.getFiveLastStudents();
    }
    public Collection<String> getAllStudentsStartingLetterA() {
        Collection<Student> students = this.getAllStudents();
        return students.stream()
                .map(e -> e.getName().toUpperCase(Locale.ROOT))
                .filter(e -> e.startsWith("A"))
                .sorted().toList();
    }

    public Double getAverageAge() {
        Collection<Student> students = this.getAllStudents();
        return students.stream()
                .mapToInt(e -> e.getAge())
                .average()
                .orElse(0);
    }
    public void printStudentsThreads() {
        List<Student> students = new ArrayList<>(this.getAllStudents());
        new Thread(() -> {
            printStudents(students, "Поток 1", 2);
            printStudents(students, "Поток 1", 3);
        }).start();
        new Thread(() -> {
            printStudents(students, "Поток 2", 4);
            printStudents(students, "Поток 2", 5);
        }).start();

        printStudents(students, "Поток 0", 0);
        printStudents(students, "Поток 0", 1);
    }
    private void printStudents(List<Student> students, String message, int number) {
        System.out.println(message + ": №" + number + ": " + students.get(number).getName());
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
            System.out.println("Thread has been interrupted");
        }
    }
    public void printStudentsThreadsSync() {
        List<Student> students = new ArrayList<>(this.getAllStudents());

        Thread thread1 = new Thread(() -> {
            printStudentsSync(students, "Поток 1");
            printStudentsSync(students, "Поток 1");
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            printStudentsSync(students, "Поток 2");
            printStudentsSync(students, "Поток 2");
        });
        thread2.start();

        printStudentsSync(students, "Поток 0");
        printStudentsSync(students, "Поток 0");
    }
    private synchronized void printStudentsSync(List<Student> students, String message) {
        System.out.println(message + ": №" + currentStudent + ": " + students.get(currentStudent).getName());
        currentStudent++;
    }
}
