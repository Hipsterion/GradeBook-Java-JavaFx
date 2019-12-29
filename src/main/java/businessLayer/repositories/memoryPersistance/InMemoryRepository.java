package main.java.businessLayer.repositories.memoryPersistance;

import main.java.businessLayer.domain.Entity;
import main.java.businessLayer.exceptions.RepositoryException;
import main.java.businessLayer.exceptions.ValidationException;
import main.java.businessLayer.repositories.CrudRepository;
import main.java.serviceLayer.validators.Validator;

import java.util.HashMap;





public abstract class InMemoryRepository<ID, E extends Entity<ID>> implements CrudRepository<ID, E> {
    protected HashMap<ID, E> entities;
    protected Validator<E> validator;

    public InMemoryRepository(Validator<E> validator) {
        this.entities = new HashMap<>();
        this.validator = validator;
//        this.validator = ValidatorFactory.getInstance().<E>getValidator();

    }

    public int size(){
        return entities.size();
    }

    @Override
    public E findOne(ID id){
        if(id == null) throw new IllegalArgumentException("id cannot be null");
        var entity = entities.get(id);
        if(entity == null) throw new RepositoryException("Id not found");
        return entity;

    }

    @Override
    public Iterable<E> findAll() {
        return entities.values();
    }

    @Override
    public E save(E entity) throws ValidationException {
        if(entity == null) throw new IllegalArgumentException("entity cannot  be null");
        validator.validate(entity);
        if(entities.get(entity.getId())!=null)
            throw new RepositoryException("Id is already found");
        return entities.put(entity.getId(), entity);
    }

    @Override
    public E delete(ID id){
        if(id == null) throw new IllegalArgumentException("id cannot be null");
        return entities.remove(id);
    }

    @Override
    public E update(E entity){
        if(entity == null) throw new IllegalArgumentException("entity cannot be null");
        validator.validate(entity);
        E e = findOne(entity.getId());
        if(e==null)
            throw new RepositoryException("Id is already found");
        e.update(entity);
        return entities.replace(entity.getId(), e);
    }

}
