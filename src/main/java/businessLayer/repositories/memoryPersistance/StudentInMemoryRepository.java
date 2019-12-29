package main.java.businessLayer.repositories.memoryPersistance;

import main.java.businessLayer.domain.Student;
import main.java.serviceLayer.validators.StudentValidator;

public class StudentInMemoryRepository extends InMemoryRepository<Integer, Student>{

    public StudentInMemoryRepository() {
        super(new StudentValidator());
    }
}


