package main.java.serviceLayer.services;

import main.java.businessLayer.domain.Student;
import main.java.businessLayer.exceptions.ValidationException;
import main.java.businessLayer.repositories.CrudRepository;
import main.java.utils.events.ChangeEventType;
import main.java.utils.events.StudentChangeEvent;
import main.java.utils.observer.Observable;
import main.java.utils.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class StudentService implements Observable<StudentChangeEvent> {
    private CrudRepository<Integer, Student> repository;
    private List<Observer<StudentChangeEvent>> observers = new ArrayList<>();

    public StudentService(CrudRepository<Integer, Student> repository) {
        this.repository = repository;
    }

    public Student findOne(Integer id){
        return repository.findOne(id);
    }

    public Iterable<Student> findAll(){
        return repository.findAll();
    }

    public Student save(Integer id, String nume, String prenume, String grupa, String email, String cadru) throws ValidationException{
        var student = new Student(id, nume, prenume, grupa, email, cadru);
        Student oldStudent = repository.save(student);
        notifyObservers(new StudentChangeEvent(ChangeEventType.ADD, student));
        return oldStudent;
    }

    public Student delete(Integer id){
        Student oldStudent =  repository.delete(id);
        notifyObservers(new StudentChangeEvent(ChangeEventType.DELETE, oldStudent));
        return oldStudent;
    }

    public Student update(Integer id, String nume, String prenume, String grupa, String email, String cadru){
        var student = new Student(id, nume, prenume, grupa, email, cadru);
        Student oldStudent = repository.update(student);
        notifyObservers(new StudentChangeEvent(ChangeEventType.UPDATE, oldStudent));
        return oldStudent;
    }

    @Override
    public void addObserver(Observer<StudentChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<StudentChangeEvent> e) { }

    @Override
    public void notifyObservers(StudentChangeEvent t) {
        observers.forEach(x ->x.update(t));
    }
}

