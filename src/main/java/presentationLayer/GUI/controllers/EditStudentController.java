package main.java.presentationLayer.GUI.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.businessLayer.domain.Student;
import main.java.serviceLayer.services.StudentService;
import main.java.utils.guiUtils.LogicLinker;
import main.java.utils.stringExtensions.StringExtensions;
import main.java.utils.guiUtils.TextFieldGroupInfoProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class EditStudentController {
    @FXML
    private TextField textFieldNume;
    @FXML
    private TextField textFieldPrenume;
    @FXML
    private TextField textFieldGrupa;
    @FXML
    private TextField textFieldEmail;
    @FXML
    private TextField textFieldProfesor;

    @FXML
    private Label labelNumeWarning;
    @FXML
    private Label labelPrenumeWarning;
    @FXML
    private Label labelGrupaWarning;
    @FXML
    private Label labelEmailWarning;
    @FXML
    private Label labelProfesorWarning;


    private StudentService service;
    private Stage dialogStage;
    private Student student;

    @FXML
    private void initialize() {
        setUpFieldsTextProperty();
    }

    private void setUpFieldsTextProperty() {
        textFieldNume.textProperty().addListener((obs, oldText, newText) -> LogicLinker.setUpFieldTextProperty(textFieldNume, labelNumeWarning, newText, new Predicate() {
            @Override
            public boolean test(Object o) {
                String string = (String) o;
                return StringExtensions.matchesNameFormat(string);
            }
        }));
        textFieldPrenume.textProperty().addListener((obs, oldText, newText) -> LogicLinker.setUpFieldTextProperty(textFieldPrenume, labelPrenumeWarning, newText, new Predicate() {
            @Override
            public boolean test(Object o) {
                String string = (String) o;
                return StringExtensions.matchesNameFormat(string);
            }
        }));
        textFieldEmail.textProperty().addListener((obs, oldText, newText) -> LogicLinker.setUpFieldTextProperty(textFieldEmail, labelEmailWarning, newText, new Predicate() {
            @Override
            public boolean test(Object o) {
                String string = (String) o;
                return StringExtensions.matchesEmailFormat(string);
            }
        }));
        textFieldProfesor.textProperty().addListener((obs, oldText, newText) -> LogicLinker.setUpFieldTextProperty(textFieldProfesor, labelProfesorWarning, newText, new Predicate() {
            @Override
            public boolean test(Object o) {
                String string = (String) o;
                return StringExtensions.matchesFullNameFormat(string);
            }
        }));
    }

    public void setService(StudentService service,  Stage stage, Student student) {
        this.service = service;
        this.dialogStage = stage;
        this.student = student;
        if (null != student) {
            setFields(student);
        }
    }

    @FXML
    public void handleUpdate(){
        String nume = textFieldNume.getText();
        String prenume = textFieldPrenume.getText();
        String grupa = textFieldGrupa.getText();
        String email = textFieldEmail.getText();
        String profesor = textFieldProfesor.getText();
        var fieldList = new ArrayList<TextField>(List.of(textFieldNume, textFieldPrenume, textFieldGrupa, textFieldEmail, textFieldProfesor));
        if(TextFieldGroupInfoProvider.listContainsEmptyField(fieldList)) MessageAlert.showErrorMessage(null, TextFieldGroupInfoProvider.getEmptyFieldsAsString(fieldList));
        else {
            if (TextFieldGroupInfoProvider.listContainErrorField(fieldList))
                MessageAlert.showErrorMessage(null, "One or more fields contain invalid data. Try again");
            else {
                try {
                    Student newStudent = service.update(student.getId(), nume, prenume, grupa, email, profesor);
                    MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Update", "Studentul a fost modificat cu succes");
                    dialogStage.close();
                } catch (RuntimeException e) {
                    MessageAlert.showErrorMessage(null, e.getMessage());
                }
            }
        }

    }

//    private void updateMessage(MessageTask m)
//    {
//        try {
//            MessageTask r= this.service.updateMessageTask(m);
//            if (r==null)
//                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Modificare mesaj","Mesajul a fost modificat");
//        } catch (ValidationException e) {
//            MessageAlert.showErrorMessage(null,e.getMessage());
//        }
//        dialogStage.close();
//    }
//
//
//    private void saveMessage(MessageTask m)
//    {
//        try {
//            MessageTask r= this.service.addMessageTaskTask(m);
//            if (r==null)
//                dialogStage.close();
//            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION,"Slavare mesaj","Mesajul a fost salvat");
//        } catch (ValidationException e) {
//            MessageAlert.showErrorMessage(null,e.getMessage());
//        }
//
//    }

    private void setFields(Student s)
    {
        textFieldNume.setText(s.getNume());
        textFieldPrenume.setText(s.getPrenume());
        textFieldGrupa.setText(s.getGrupa());
        textFieldEmail.setText(s.getEmail());
        textFieldProfesor.setText(s.getProfesor());
    }

    @FXML
    public void handleCancel(){
        dialogStage.close();
    }
}
