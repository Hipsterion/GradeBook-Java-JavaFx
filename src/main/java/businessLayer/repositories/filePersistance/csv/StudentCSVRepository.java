package main.java.businessLayer.repositories.filePersistance.csv;

import main.java.businessLayer.domain.Student;
import main.java.businessLayer.repositories.CrudRepository;

public class StudentCSVRepository extends CSVRepository<Integer, Student> {
    public StudentCSVRepository(CrudRepository<Integer, Student> crudRepository, String filepath) {
        super(crudRepository, filepath);
    }

    @Override
    public Student parseEntity(String line) {
        String[] fields = line.split(",");
        return new Student(Integer.parseInt(fields[0]), fields[1], fields[2], fields[3], fields[4], fields[5]);
    }
}
