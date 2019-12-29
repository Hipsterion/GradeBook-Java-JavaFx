package test.java.gradebookTests.businessLayerTests;

import main.java.businessLayer.domain.Student;
import org.junit.Assert;

class StudentTest {
    private Student student;
    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        student = new Student(11,"Grigorescu","Vasile","212","vasile30@gmail.com","Pop Alex");
    }

    @org.junit.jupiter.api.Test
    void getNume() {
        Assert.assertEquals(student.getNume(), "Grigorescu");
    }

    @org.junit.jupiter.api.Test
    void setNume() {
        student.setNume("Ion");
        Assert.assertEquals(student.getNume(), "Ion");
    }

    @org.junit.jupiter.api.Test
    void getPrenume() {
        Assert.assertEquals(student.getPrenume(), "Vasile");
    }

    @org.junit.jupiter.api.Test
    void setPrenume() {
        student.setPrenume("Ion");
        Assert.assertEquals(student.getPrenume(), "Ion");
    }

    @org.junit.jupiter.api.Test
    void getEmail() {
        Assert.assertEquals(student.getEmail(), "vasile30@gmail.com");
    }

    @org.junit.jupiter.api.Test
    void setEmail() {
        student.setEmail("cocolino@yahoo.com");
        Assert.assertEquals(student.getEmail(), "cocolino@yahoo.com");
    }

    @org.junit.jupiter.api.Test
    void getGrupa() {
        Assert.assertEquals(student.getGrupa(), "212");
    }

    @org.junit.jupiter.api.Test
    void setGrupa() {
        student.setGrupa("213");
        Assert.assertEquals(student.getGrupa(), "213");
    }

    @org.junit.jupiter.api.Test
    void getCadruDidacticIndrumatorLab() {
        Assert.assertEquals(student.getProfesor(), "Pop Alex");
    }

    @org.junit.jupiter.api.Test
    void setCadriDidacticIndrumatorLab() {
        student.setProfesor("Constantinescu Elis");
        Assert.assertEquals(student.getProfesor(), "Constantinescu Elis");
    }

    @org.junit.jupiter.api.Test
    void update(){
        Student st = new Student(11,"Grigorescu","Vasile","234","vasile30@gmail.com","Pop Alex");
        student.update(st);
        Assert.assertEquals("234", st.getGrupa());
    }

}