package main.java.serviceLayer.validators;

import main.java.businessLayer.domain.Grade;
import main.java.businessLayer.exceptions.ValidationException;

public class GradeValidator implements Validator<Grade> {
    @Override
    public void validate(Grade entity) throws ValidationException {
        validateValue(entity);
    }

    private void validateValue(Grade entity) {
        if(entity.getValue() < 1 || entity.getValue() > 10) throw new ValidationException("Valoarea notei trebuie sa fie intre 1 si 10");
    }

    private void valsdidate(Grade entity) {
        if(entity.getValue() < 1 || entity.getValue() > 10) throw new ValidationException("Valoarea notei trebuie sa fie intre 1 si 10");
    }
}
