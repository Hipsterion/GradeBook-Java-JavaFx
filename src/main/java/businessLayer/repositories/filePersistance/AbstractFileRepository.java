package main.java.businessLayer.repositories.filePersistance;

import main.java.businessLayer.domain.Entity;
import main.java.businessLayer.exceptions.ValidationException;
import main.java.businessLayer.repositories.CrudRepository;

public abstract class AbstractFileRepository<ID, E extends Entity<ID>> implements CrudRepository<ID, E> {
    protected CrudRepository<ID, E> crudRepository;
    protected String filepath;

    public AbstractFileRepository(CrudRepository<ID, E> crudRepository, String filepath) {
        this.crudRepository = crudRepository;
        this.filepath = filepath;
        load();

    }

    //public abstract E parseEntity(String line);

    protected abstract void load();

    protected abstract void saveChanges();


    @Override
    public E findOne(ID id) {
        return crudRepository.findOne(id);
    }

    @Override
    public Iterable<E> findAll() {
        return crudRepository.findAll();
    }

    @Override
    public E save(E entity) throws ValidationException {
        E old = crudRepository.save(entity);
        saveChanges();
        return old;
    }
    @Override
    public E delete(ID id) {
        E old = crudRepository.delete(id);
        saveChanges();
        return old;
    }
    @Override
    public E update(E entity) {
        E old = crudRepository.update(entity);
        saveChanges();
        return old;
    }
}


