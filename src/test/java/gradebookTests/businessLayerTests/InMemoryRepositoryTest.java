package test.java.gradebookTests.businessLayerTests;

import main.java.businessLayer.domain.Student;
import main.java.businessLayer.exceptions.ValidationException;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import main.java.businessLayer.repositories.memoryPersistance.InMemoryRepository;
import main.java.businessLayer.repositories.memoryPersistance.StudentInMemoryRepository;

class InMemoryRepositoryTest {
    InMemoryRepository<Integer,Student> studentInMemoryRepository;
    @BeforeEach
    void setUp() {
        studentInMemoryRepository = new StudentInMemoryRepository();
        var st1 = new Student(1,"Vasile", "Ion", "212", "cocolino@gsd.com", "A lu Hugea");
        var st2 = new Student(2,"Vasiasdasdle", "Iofgdfgn", "213", "cocoasdasdlino@gsd.com", "A lu asdasdHugea");
        var st3 = new Student(3,"Vasilgdfgdfge", "Iodfvbdfvdfn", "214", "cocoligergerg@gsxasx.com", "A lxasxasu Hugea");
        try {
            studentInMemoryRepository.save(st1);
            studentInMemoryRepository.save(st2);
            studentInMemoryRepository.save(st3);
        }
        catch(Exception e){ }
    }
    @Test
    void update(){
        Assert.assertEquals(3, studentInMemoryRepository.size());
    }
    @Test
    void findOne() {
        try{
            studentInMemoryRepository.findOne(null);
            assert false;
        }
        catch(IllegalArgumentException e){
            assert true;
        }
        Assert.assertEquals("213", studentInMemoryRepository.findOne(2).getGrupa());
    }

    @Test
    void findAll() {
        var entities = studentInMemoryRepository.findAll();
        var iterator = studentInMemoryRepository.findAll().iterator();
        Student student = iterator.next();
        Assert.assertEquals(1, (int)student.getId());
    }

    @Test
    void save() {
        Student student = new Student(4, "Ionucu", "Petrica", "215", "ionucutau@yahoo.com", "Corcodus Eminovici");
        try {
            studentInMemoryRepository.save(student);
            assert true;
        }
        catch(ValidationException e){ assert false;}

        try{
            studentInMemoryRepository.save(null);
            assert false;
        }
        catch(IllegalArgumentException e){
            assert true;
        }
        catch(ValidationException v){
            assert false;
        }

        Assert.assertEquals(4, studentInMemoryRepository.size());
    }

    @Test
    void delete() {
        try{
            studentInMemoryRepository.delete(null);
            assert false;
        }
        catch(IllegalArgumentException e){
            assert true;
        }

        studentInMemoryRepository.delete(1);
        studentInMemoryRepository.delete(3);
        Assert.assertEquals(1, studentInMemoryRepository.size());
        Assert.assertEquals("213", studentInMemoryRepository.findOne(2).getGrupa());
    }
}