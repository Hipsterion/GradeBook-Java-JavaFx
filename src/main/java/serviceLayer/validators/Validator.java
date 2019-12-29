package main.java.serviceLayer.validators;

import main.java.businessLayer.exceptions.ValidationException;

public interface Validator<E> {
    void validate(E entity) throws ValidationException;
}

