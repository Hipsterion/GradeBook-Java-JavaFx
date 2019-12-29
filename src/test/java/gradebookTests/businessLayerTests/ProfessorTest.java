package test.java.gradebookTests.businessLayerTests;

import main.java.businessLayer.domain.Professor;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProfessorTest {
    private Professor professor;
    @BeforeEach
    void setUp() {
        professor = new Professor(11,"Grigorescu","Vasile","vasile30@gmail.com");
    }

    @Test
    void getNume() {
        Assert.assertEquals(professor.getNume(), "Grigorescu");
    }

    @Test
    void setNume() {
        professor.setNume("Ion");
        Assert.assertEquals(professor.getNume(), "Ion");
    }

    @Test
    void getPrenume() {
        Assert.assertEquals(professor.getPrenume(), "Vasile");
    }

    @Test
    void setPrenume() {
        professor.setPrenume("Ion");
        Assert.assertEquals(professor.getPrenume(), "Ion");
    }

    @Test
    void getEmail() {
        Assert.assertEquals(professor.getEmail(), "vasile30@gmail.com");
    }

    @Test
    void setEmail() {
        professor.setEmail("cocolino@yahoo.com");
        Assert.assertEquals(professor.getEmail(), "cocolino@yahoo.com");
    }
}