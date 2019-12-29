package test.java.gradebookTests.businessLayerTests;

import main.java.businessLayer.domain.Assignment;
import main.java.businessLayer.domain.YearStructure;
import main.java.businessLayer.domain.SemesterStructure;
import main.java.businessLayer.exceptions.ValidationException;
import org.junit.jupiter.api.Test;

import main.java.serviceLayer.validators.AssignmentValidator;

import java.time.LocalDateTime;
import java.time.Month;

class AssignmentValidatorTest {

    @Test
    void validate() {
        AssignmentValidator validator = new AssignmentValidator();
        var semester1 = new SemesterStructure(1, LocalDateTime.of(2019, Month.SEPTEMBER, 30, 11,0,0), LocalDateTime.of(2020, Month.JANUARY, 31, 10,0,0), LocalDateTime.of(2019, Month.DECEMBER, 23, 10,0,0), LocalDateTime.of(2020, Month.JANUARY, 6, 10,0,0));
        var semester2 = new SemesterStructure(2, LocalDateTime.of(2020, Month.FEBRUARY, 10, 12,0,0), LocalDateTime.of(2020, Month.JUNE, 12, 10,0,0), LocalDateTime.of(2019, Month.APRIL, 20, 10,0,0), LocalDateTime.of(2020, Month.APRIL, 27, 10,0,0));
        var structuraAnUniversitar = new YearStructure(2, "2019-2020", semester1, semester2);
        Assignment assignment = new Assignment(1, "A fost greu", 12, LocalDateTime.of(2019, Month.NOVEMBER, 10, 10, 0, 0), structuraAnUniversitar);
        try{
            validator.validate(assignment);
            assert true;
        } catch (ValidationException e) {
            assert false;
        }

        Assignment assignment2 = new Assignment(1, "A fost greu", 15, LocalDateTime.of(2019, Month.NOVEMBER, 10, 10, 0, 0), structuraAnUniversitar);
        try{
            validator.validate(assignment2);
            assert false;
        } catch (ValidationException e) {
            assert true;
        }

        Assignment assignment3 = new Assignment(1, "A fost greu", 4, LocalDateTime.of(2019, Month.NOVEMBER, 10, 10, 0, 0), structuraAnUniversitar);
        try{
            validator.validate(assignment3);
            assert false;
        } catch (ValidationException e) {
            assert true;
        }
    }
}