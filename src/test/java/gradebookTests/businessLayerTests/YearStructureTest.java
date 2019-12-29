package test.java.gradebookTests.businessLayerTests;

import main.java.businessLayer.domain.YearStructure;
import main.java.businessLayer.domain.SemesterStructure;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

class YearStructureTest {
    private YearStructure yearStructure;
    @BeforeEach
    void setUp() {
        var semester1 = new SemesterStructure(1, LocalDateTime.of(2019, Month.SEPTEMBER, 30, 10,0,0), LocalDateTime.of(2020, Month.JANUARY, 31, 10,0,0), LocalDateTime.of(2019, Month.DECEMBER, 23, 10,0,0), LocalDateTime.of(2020, Month.JANUARY, 6, 10,0,0));
        var semester2 = new SemesterStructure(2, LocalDateTime.of(2020, Month.FEBRUARY, 10, 10,0,0), LocalDateTime.of(2020, Month.JUNE, 12, 10,0,0), LocalDateTime.of(2019, Month.APRIL, 20, 10,0,0), LocalDateTime.of(2020, Month.APRIL, 27, 10,0,0));
        yearStructure = new YearStructure(2, "2019-2020", semester1, semester2);
    }

    @Test
    void getAnulUniversitar() {
        Assert.assertEquals("2019-2020", yearStructure.getAnulUniversitar());
    }

    @Test
    void setAnulUniversitar() {
        yearStructure.setAnulUniversitar("2020-2021");
        Assert.assertEquals("2020-2021", yearStructure.getAnulUniversitar());
    }

    @Test
    void getSem1() {
        Assert.assertEquals(1, (int) yearStructure.getSemester1().getId());
    }

    @Test
    void setSem1() {
        yearStructure.setSemester1(new SemesterStructure(3, LocalDateTime.of(2020, Month.SEPTEMBER, 30, 10,0,0), LocalDateTime.of(2021, Month.JANUARY, 31, 10,0,0), LocalDateTime.of(2019, Month.DECEMBER, 23, 10,0,0), LocalDateTime.of(2020, Month.JANUARY, 6, 10,0,0)));
        Assert.assertEquals(3, (int) yearStructure.getSemester1().getId());
    }

    @Test
    void getSem2() {
        Assert.assertEquals(2, (int) yearStructure.getSemester2().getId());
    }

    @Test
    void setSem2() {
        yearStructure.setSemester2(new SemesterStructure(3, LocalDateTime.of(2020, Month.SEPTEMBER, 30, 10,0,0), LocalDateTime.of(2021, Month.JANUARY, 31, 10,0,0), LocalDateTime.of(2019, Month.APRIL, 20, 10,0,0), LocalDateTime.of(2020, Month.APRIL, 27, 10,0,0)));
        Assert.assertEquals(3, (int) yearStructure.getSemester2().getId());
    }

    @Test
    void getSemestru() {
        Assert.assertEquals(1, (int) yearStructure.getSemestru(LocalDateTime.of(2019, Month.SEPTEMBER, 30, 10,0,0)).getId());
    }
}