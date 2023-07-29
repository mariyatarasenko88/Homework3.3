package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.stream.Stream;

@Service
public class FacultyService {
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;
    private final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository, StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
    }
    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        return facultyRepository.save(faculty);
    }
    public Faculty findFaculty(long id) {
        logger.info("Was invoked method for find faculty");
        logger.debug("Requesting find faculty by id {}", id);
        Faculty result = facultyRepository.findById(id).orElse(null);
        logger.debug("{} faculty found by id {}", result, id);
        return result;
    }
    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked method for edit faculty");
        if (findFaculty(faculty.getId()) != null) {
            return facultyRepository.save(faculty);
        }
        return null;
    }
    public void deleteFaculty(long id) {
        logger.info("Was invoked method for delete faculty");
        facultyRepository.deleteById(id);
    }
    public Collection<Faculty> getAllFaculties() {
        logger.info("Was invoked method for find all faculties");
        return facultyRepository.findAll();
    }
    public Collection<Faculty> getFacultiesByColor(String color) {
        logger.info("Was invoked method for find faculties by color");
        return facultyRepository.findByColor(color);
    }
    public Collection<Faculty> getFacultyByNameOrColor(String nameOrColor) {
        logger.info("Was invoked method for find faculties by name or color");
        return facultyRepository.findFacultiesByNameIgnoreCaseOrColorIgnoreCase(nameOrColor, nameOrColor);
    }
    public Collection<Student> getStudentsByFaculty(long id) {
        logger.info("Was invoked method for find students by faculty id");
        return studentRepository.findStudentByFacultyId(id);
    }
    public String getFacultyNameWithLongestName() {
        return getAllFaculties().stream()
                .map(e -> e.getName())
                .reduce("", (a, b) -> a.length() > b.length() ? a : b);
    }

    public ResponseEntity<String> calculateFormula() {
        long start = System.nanoTime();
        Integer result = Stream.iterate(1, a -> a + 1)
                .limit(100_000)
                .parallel()
                .reduce(0, (a, b) -> a + b);
        long finish = System.nanoTime();
        long elapsed = finish - start;

        return ResponseEntity.ok("время " + elapsed + " результат " + result);
    }
    public ResponseEntity<String> calculateFormulaTwo() {
        long start = System.nanoTime();

        int sum = 0;
        int i;
        for(i = 0; i < 100_000; i++) {
            sum += i;
        }
        long finish = System.nanoTime();
        long elapsed = finish - start;

        return ResponseEntity.ok("время " + elapsed + " результат " + sum);
    }
    public ResponseEntity<String> calculateFormulaThree() {
        long start = System.nanoTime();
        Integer result = Stream.iterate(1, a -> a + 1)
                .limit(100_000)
                .reduce(0, (a, b) -> a + b);
        long finish = System.nanoTime();
        long elapsed = finish - start;

        return ResponseEntity.ok("время " + elapsed + " результат " + result);
    }
}
