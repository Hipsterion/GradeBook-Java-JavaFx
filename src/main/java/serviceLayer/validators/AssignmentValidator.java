package main.java.serviceLayer.validators;

import main.java.businessLayer.domain.Assignment;
import main.java.businessLayer.exceptions.ValidationException;

public class AssignmentValidator implements Validator<Assignment> {
    @Override
    public void validate(Assignment entity) throws ValidationException {
        validStartWeek(entity);
        validEndWeek(entity);
        validWeeks(entity);
    }

    private void validStartWeek(Assignment entity) throws ValidationException{
        if(entity.getStartWeek() > 14 || entity.getStartWeek() < 1) throw new ValidationException("startWeek trebuie sa fie intre 1 si 14");
    }

    private void validEndWeek(Assignment entity) throws ValidationException{
        if(entity.getDeadlineWeek() > 14 || entity.getDeadlineWeek() < 1) throw new ValidationException("deadlinewWeek trebuie sa fie intre 1 si 14");
    }

    private void validWeeks(Assignment entity) throws ValidationException{
        if(entity.getStartWeek() > entity.getDeadlineWeek()) throw new ValidationException("startWeek trebuie sa fie mai mic decat deadlineWeek");
    }


}
