package main.java.utils.guiUtils;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.function.Predicate;

public class LogicLinker {
    public static void setUpFieldTextProperty(TextField textField, Label label, String newText, Predicate predicate){
        if(!predicate.test(newText)){
            if(newText.isEmpty()){
                label.setVisible(false);
                textField.getStyleClass().remove("error");
            }
            else if(!textField.getStyleClass().contains("error")){
                textField.getStyleClass().add("error");
                label.setVisible(true);

            }
            else label.setVisible(true);
        }
        else{
            label.setVisible(false);
            textField.getStyleClass().remove("error");
        }
    }
}
