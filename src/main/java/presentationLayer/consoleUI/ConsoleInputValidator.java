package main.java.presentationLayer.consoleUI;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ConsoleInputValidator {
    private String message;

    public ConsoleInputValidator() {
        this.message = "";
    }

    public boolean errorsAreFound(){
        return !message.equals("");
    }

    public boolean parsableToInteger(String input){
        try{
            Integer.parseInt(input);
            return true;
        }catch(NumberFormatException e){
            return false;
        }
    }

    public void validateIntegerId(String input){
        if(!parsableToInteger(input))
            message+="Id must pe integer\n";
    }

    public void validateEmail(String input){
        if(!input.matches(".+@.+[.].+$"))
            message+="Email must match email format\n";
    }

    public void validateIntegerValue(String input){
        if(!parsableToInteger(input))
            message+="Value must pe integer\n";
    }

    public void validateIntegerWeek(String input){
        if(!parsableToInteger(input))
            message+="Week must pe integer\n";
    }

    public void validateLocalDateTime(String input){
        try{
            LocalDateTime.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }catch(DateTimeParseException e){
            message+="DateTime must match format yyyy-MM-dd HH:mm\n";
        }
    }

    public String getErrorMessage(){
        return message;
    }



}
