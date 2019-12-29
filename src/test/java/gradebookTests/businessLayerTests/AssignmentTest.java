package test.java.gradebookTests.businessLayerTests;

import main.java.businessLayer.domain.YearStructure;
import main.java.businessLayer.domain.SemesterStructure;
import main.java.businessLayer.domain.Assignment;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;

class AssignmentTest {
    private Assignment assignment;
    @BeforeEach
    void setUp() {
        var semester1 = new SemesterStructure(1, LocalDateTime.of(2019, Month.SEPTEMBER, 30, 10,0,0), LocalDateTime.of(2020, Month.JANUARY, 31, 10,0,0), LocalDateTime.of(2019, Month.DECEMBER, 23, 10,0,0), LocalDateTime.of(2020, Month.JANUARY, 6, 10,0,0));
        var semester2 = new SemesterStructure(2, LocalDateTime.of(2020, Month.FEBRUARY, 10, 10,0,0), LocalDateTime.of(2020, Month.JUNE, 12, 10,0,0), LocalDateTime.of(2019, Month.APRIL, 20, 10,0,0), LocalDateTime.of(2020, Month.APRIL, 27, 10,0,0));
        var structuraAnUniversitar = new YearStructure(2, "2019-2020", semester1, semester2);
        assignment = new Assignment(1, "A fost greu", 12, LocalDateTime.of(2019, Month.NOVEMBER, 10, 10,0,0), structuraAnUniversitar);
    }

    @Test
    void getDescriere() {
        Assert.assertEquals("A fost greu", assignment.getDescriere());
    }

    @Test
    void setDescriere() {
        assignment.setDescriere("Greut");
        Assert.assertEquals("Greut", assignment.getDescriere());
    }

    @Test
    void getStartWeek() {
        Assert.assertEquals(6, assignment.getStartWeek());
    }

    @Test
    void getDeadlineWeek() {
        Assert.assertEquals(12, assignment.getDeadlineWeek());
    }

    @Test
    void setDeadlineWeek() {
        assignment.setDeadlineWeek(10);
        Assert.assertEquals(10, assignment.getDeadlineWeek());
    }

    @Test
    void update(){
        var semester1 = new SemesterStructure(1, LocalDateTime.of(2019, Month.SEPTEMBER, 30, 10,0,0), LocalDateTime.of(2020, Month.JANUARY, 31, 10,0,0), LocalDateTime.of(2019, Month.DECEMBER, 23, 10,0,0), LocalDateTime.of(2020, Month.JANUARY, 6, 10,0,0));
        var semester2 = new SemesterStructure(2, LocalDateTime.of(2020, Month.FEBRUARY, 10, 10,0,0), LocalDateTime.of(2020, Month.JUNE, 12, 10,0,0), LocalDateTime.of(2019, Month.APRIL, 20, 10,0,0), LocalDateTime.of(2020, Month.APRIL, 27, 10,0,0));
        var structuraAnUniversitar = new YearStructure(2, "2019-2020", semester1, semester2);
        Assignment ntema = new Assignment(2, "E grea", 6, LocalDateTime.of(2019, Month.NOVEMBER, 10, 10,0,0), structuraAnUniversitar);
        assignment.update(ntema);
        Assert.assertEquals(6, assignment.getDeadlineWeek());
        Assignment ntema2 = new Assignment(2, "E grea", 4, LocalDateTime.of(2019, Month.NOVEMBER, 10, 10,0,0), structuraAnUniversitar);
        try{
            assignment.update(ntema2);
            assert false;
        }
        catch(IllegalArgumentException e){
            assert true;
        }

    }
}