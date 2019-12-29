package main.java.businessLayer.repositories.memoryPersistance;

import main.java.businessLayer.domain.Grade;
import main.java.serviceLayer.validators.GradeValidator;

public class NotaInMemoryRepository extends InMemoryRepository<String, Grade>{

    public NotaInMemoryRepository() {
        super(new GradeValidator());
    }
}

