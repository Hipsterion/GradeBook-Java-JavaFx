package main.java.businessLayer.repositories.memoryPersistance;

import main.java.businessLayer.domain.Assignment;
import main.java.serviceLayer.validators.AssignmentValidator;

public class TemaInMemoryRepository extends InMemoryRepository<Integer, Assignment> {
    public TemaInMemoryRepository() {
        super(new AssignmentValidator());
    }
}
