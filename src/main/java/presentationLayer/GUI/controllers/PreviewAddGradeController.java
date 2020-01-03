package main.java.presentationLayer.GUI.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import main.java.businessLayer.domain.GradeDTO;
import main.java.serviceLayer.services.GradeBookService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PreviewAddGradeController {
    @FXML
    private Label labelStudent;
    @FXML
    private Label labelTema;
    @FXML
    private Label labelDate;
    @FXML
    private Label labelProfesor;
    @FXML
    private Label labelValue;
    @FXML
    private TextArea labelFeedback;



    private GradeBookService service;
    private Stage dialogStage;
    private GradeDTO gradeDTO;

    @FXML
    private void initialize() {
    }

    public void setService(GradeBookService service,  Stage stage, GradeDTO gradeDTO) {
        this.service = service;
        this.dialogStage = stage;
        this.gradeDTO = gradeDTO;
        if (null != gradeDTO) {
            setFields();
        }
    }


    private void setFields()
    {
        labelStudent.setText(gradeDTO.getNumeStudent());
        labelTema.setText(gradeDTO.getNrTema().toString());
        labelDate.setText(gradeDTO.getDataPredare().toString());
        labelValue.setText(String.valueOf(gradeDTO.getValue()));
        labelProfesor.setText(gradeDTO.getProfesor());
        labelFeedback.setText(gradeDTO.getFeedback());
    }

    @FXML
    public void handleCancel(){
        dialogStage.close();
    }

    @FXML
    public void handleSave(){
        try {
            var notaOld = service.save(service.findStudentByNume(labelStudent.getText()).getId(), Integer.parseInt(labelTema.getText()),
                    LocalDate.parse(labelDate.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay(),
                    labelProfesor.getText(), Integer.parseInt(labelValue.getText()), labelFeedback.getText());
            if(notaOld == null) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Save", "Nota a fost adaugata cu succes");
                service.sendEmailToStudentWithThread(service.findStudentByNume(labelStudent.getText()).getEmail());
            }
            else MessageAlert.showErrorMessage(null, "Studentul a predat deja această temă");
        }catch (RuntimeException e){
            MessageAlert.showErrorMessage(null, "bla" + e.getMessage());
        }
        dialogStage.close();
    }
}
