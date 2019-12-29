package main.java.businessLayer.repositories;

import main.java.businessLayer.domain.Entity;
import main.java.businessLayer.exceptions.ValidationException;

public interface CrudRepository<ID, E extends Entity<ID>> {
    E findOne(ID id);
    Iterable<E> findAll();
    E save(E entity) throws ValidationException;
    E delete(ID id);
    E update(E entity);
}
