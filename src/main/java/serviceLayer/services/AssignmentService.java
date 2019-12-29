package main.java.serviceLayer.services;

import main.java.businessLayer.domain.Assignment;
import main.java.businessLayer.domain.YearStructure;
import main.java.businessLayer.exceptions.ValidationException;
import main.java.businessLayer.repositories.CrudRepository;
import main.java.utils.events.GradeChangeEvent;
import main.java.utils.observer.Observable;
import main.java.utils.observer.Observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class AssignmentService implements Observable<GradeChangeEvent> {
    private CrudRepository<Integer, Assignment> repository;
    private List<Observer<GradeChangeEvent>> observers = new ArrayList<>();
    public AssignmentService(CrudRepository<Integer, Assignment> repository) {
        this.repository = repository;
    }

    public Assignment findOne(Integer id){
        return repository.findOne(id);
    }
    public Iterable<Assignment> findAll(){
        return repository.findAll();
    }
    public Assignment save(Integer id, String desc, int deadline, LocalDateTime startDate, YearStructure yearStructure) throws ValidationException {
        var tema = new Assignment(id,desc,deadline,startDate, yearStructure);
        return repository.save(tema);
    }
    public Assignment delete(Integer id){
        return repository.delete(id);
    }
    public Assignment update(Integer id, String desc, int deadline, LocalDateTime startDate, YearStructure yearStructure){
        var student = new Assignment(id,desc,deadline,startDate, yearStructure);
        return repository.update(student);

    }

    @Override
    public void addObserver(Observer<GradeChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<GradeChangeEvent> e) {

    }

    @Override
    public void notifyObservers(GradeChangeEvent t) {
        observers.forEach(x ->x.update(t));
    }
}

