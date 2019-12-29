package test.java.gradebookTests.businessLayerTests;

import main.java.businessLayer.domain.Grade;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class NotaTest {
    private Grade grade;
    @BeforeEach
    void setUp() {
        grade = new Grade(1, 2, LocalDateTime.parse("2019-11-21 11:23", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), "Andronescu Vasile", 6, "A fost okay");
    }

    @Test
    void update() {
        grade.update(new Grade(1, 2, LocalDateTime.parse("2019-11-21 11:23", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), "Andronescu Vasile", 7, "A fost okay"));
        Assert.assertEquals(7, grade.getValue());
    }
}