package main.java.utils.guiUtils;

import javafx.scene.control.TextField;

import java.util.List;

public class TextFieldGroupInfoProvider {
    public static boolean listContainErrorField(List<TextField> fieldList){
        for(var field : fieldList) {
            if (field.getStyleClass().contains("error"))
                return true;
        }
        return false;
    }

    public static String getEmptyFieldsAsString(List<TextField> fields){
        String message = "";
        for(var field : fields) {
            if (field.getText().isEmpty())
                message += field.getPromptText() + " field cannot be empty\n";
        }
        return message;
    }

    public static boolean listContainsEmptyField(List<TextField> fieldList){
        return !getEmptyFieldsAsString(fieldList).isEmpty();
    }
}
