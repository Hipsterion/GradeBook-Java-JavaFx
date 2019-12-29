package test.java.gradebookTests.businessLayerTests;

import main.java.businessLayer.domain.SemesterStructure;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

class SemesterStructureTest {
    private SemesterStructure semestru;
    @BeforeEach
    void setUp() {
        semestru = new SemesterStructure(1, LocalDateTime.of(2019, Month.SEPTEMBER, 30, 10,0,0), LocalDateTime.of(2020, Month.JANUARY, 31, 10,0,0), LocalDateTime.of(2019, Month.DECEMBER, 23, 10,0,0), LocalDateTime.of(2020, Month.JANUARY, 6, 10,0,0));
    }

    @Test
    void getStartDate() {
        Assert.assertEquals(semestru.getStartDate(), LocalDateTime.of(2019, Month.SEPTEMBER, 30, 10,0,0));
    }

    @Test
    void setStartDate() {
        semestru.setStartDate(LocalDateTime.of(2021, Month.SEPTEMBER, 30, 10,0,0));
        Assert.assertEquals(semestru.getStartDate(), LocalDateTime.of(2021, Month.SEPTEMBER, 30, 10,0,0));
    }

    @Test
    void getEndDate() {
        Assert.assertEquals(semestru.getEndDate(), LocalDateTime.of(2020, Month.JANUARY, 31, 10,0,0));
    }

    @Test
    void setEndDate() {
        semestru.setEndDate(LocalDateTime.of(2020, Month.FEBRUARY, 1, 10,0,0));
        Assert.assertEquals(semestru.getEndDate(), LocalDateTime.of(2020, Month.FEBRUARY, 1, 10,0,0));
    }

    @Test
    void getCurrentWeek() {
        Assert.assertEquals(3, semestru.getWeek(LocalDateTime.of(2019, Month.OCTOBER, 20, 10,0,0)));
        Assert.assertEquals(13, semestru.getWeek(LocalDateTime.of(2020, Month.JANUARY, 7, 10, 0, 0)));
        Assert.assertEquals(12, semestru.getWeek(LocalDateTime.of(2019, Month.DECEMBER, 22, 10, 0, 0)));
        Assert.assertEquals(12, semestru.getWeek(LocalDateTime.of(2019, Month.DECEMBER, 26, 10, 0, 0)));
    }
}