package main.java.presentationLayer.GUI.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.businessLayer.domain.Student;
import main.java.businessLayer.repositories.databasePersistance.GradeDBRepository;
import main.java.businessLayer.repositories.databasePersistance.StudentDBRepository;
import main.java.businessLayer.repositories.databasePersistance.AssignmentDBRepository;
import main.java.serviceLayer.services.GradeBookService;
import main.java.utils.guiUtils.LogicLinker;
import main.java.utils.guiUtils.TextFieldGroupInfoProvider;
import main.java.utils.stringExtensions.StringExtensions;
import main.java.serviceLayer.services.StudentService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import main.java.utils.events.StudentChangeEvent;
import main.java.utils.observer.Observer;


public class StudentController implements Observer<StudentChangeEvent> {
     private StudentService service;
     private Stage primaryStage;
     private ObservableList<Student> model = FXCollections.observableArrayList();

    @FXML
    TableView<Student> studentsTableView;
    @FXML
    TableColumn<Student, Integer> idTableColumn;
    @FXML
    TableColumn<Student, String> NumeTableColumn;
    @FXML
    TableColumn<Student, String> prenumeTableColumn;
    @FXML
    TableColumn<Student, String> grupaTableColumn;
    @FXML
    TableColumn<Student, String> emailTableColumn;
    @FXML
    TableColumn<Student, String> ProfessorTableColumn;

    @FXML
    TextField textFieldStudentId;
    @FXML
    TextField textFieldStudentNume;
    @FXML
    TextField textFieldStudentPrenume;
    @FXML
    TextField textFieldStudentGrupa;
    @FXML
    TextField textFieldStudentEmail;
    @FXML
    TextField textFieldStudentProfesor;
    @FXML
    TextField textFieldSearch;

    @FXML
    Label labelStudentIdWarning;
    @FXML
    Label labelStudentNumeWarning;
    @FXML
    Label labelStudentPrenumeWarning;
    @FXML
    Label labelStudentGrupaWarning;
    @FXML
    Label labelStudentEmailWarning;
    @FXML
    Label labelStudentProfesorWarning;


    public void setService(StudentService service){
        this.service = service;
        this.service.addObserver(this);
        initializeModel();
    }

    public void setPrimaryStage(Stage stage){
        this.primaryStage = stage;
    }
    private PseudoClass errorClass = PseudoClass.getPseudoClass("error");

    @FXML
    public void initialize() {
        setUpTable();
        setUpFieldsTextProperties();
    }

    private void setUpFieldsTextProperties() {
        textFieldSearch.textProperty().addListener((obs, oldText, newText) -> updateModelAfterSearch(newText));
        textFieldStudentId.textProperty().addListener((obs, oldText, newText) -> LogicLinker.setUpFieldTextProperty(textFieldStudentId, labelStudentIdWarning, newText, new Predicate() {
            @Override
            public boolean test(Object o) {
                String string = (String) o;
                return StringExtensions.isInteger(string);
            }
        }));
        textFieldStudentNume.textProperty().addListener((obs, oldText, newText) -> LogicLinker.setUpFieldTextProperty(textFieldStudentNume, labelStudentNumeWarning, newText, new Predicate() {
            @Override
            public boolean test(Object o) {
                String string = (String) o;
                return StringExtensions.matchesNameFormat(string);
            }
        }));
        textFieldStudentPrenume.textProperty().addListener((obs, oldText, newText) -> LogicLinker.setUpFieldTextProperty(textFieldStudentPrenume, labelStudentPrenumeWarning, newText, new Predicate() {
            @Override
            public boolean test(Object o) {
                String string = (String) o;
                return StringExtensions.matchesNameFormat(string);
            }
        }));
        textFieldStudentEmail.textProperty().addListener((obs, oldText, newText) -> LogicLinker.setUpFieldTextProperty(textFieldStudentEmail, labelStudentEmailWarning, newText, new Predicate() {
            @Override
            public boolean test(Object o) {
                String string = (String) o;
                return StringExtensions.matchesEmailFormat(string);
            }
        }));
        textFieldStudentProfesor.textProperty().addListener((obs, oldText, newText) -> LogicLinker.setUpFieldTextProperty(textFieldStudentProfesor, labelStudentProfesorWarning, newText, new Predicate() {
            @Override
            public boolean test(Object o) {
                String string = (String) o;
                return StringExtensions.matchesFullNameFormat(string);
            }
        }));
    }



    private void setUpTable() {
        idTableColumn.setCellValueFactory(new PropertyValueFactory<Student, Integer>("id"));
        NumeTableColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("nume"));
        prenumeTableColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("prenume"));
        grupaTableColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("grupa"));
        emailTableColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        ProfessorTableColumn.setCellValueFactory(new PropertyValueFactory<Student, String>("profesor"));
        studentsTableView.setItems(model);
    }

    private void initializeModel() {
        Iterable<Student> students = service.findAll();
        List<Student> studentList = StreamSupport.stream(students.spliterator(), false)
                .collect(Collectors.toList());
        model.setAll(studentList);
    }

    private void updateModelAfterSearch(String input){
        if(input.equals("")) initializeModel();
        else {
            try {
                Student student = service.findOne(Integer.parseInt(textFieldSearch.getText()));
                model.clear();
                model.setAll(student);
            }catch (RuntimeException e) {
                model.clear();
            }
        }
    }

    @Override
    public void update(StudentChangeEvent studentChangeEvent) {
        initializeModel();
    }

    public void handleSave(ActionEvent actionEvent) {
        String id = textFieldStudentId.getText();
        String nume = textFieldStudentNume.getText();
        String prenume = textFieldStudentPrenume.getText();
        String grupa = textFieldStudentGrupa.getText();
        String email = textFieldStudentEmail.getText();
        String profesor = textFieldStudentProfesor.getText();
        var fieldList = new ArrayList<TextField>(List.of(textFieldStudentId, textFieldStudentNume, textFieldStudentPrenume, textFieldStudentGrupa, textFieldStudentEmail, textFieldStudentProfesor));
        if(TextFieldGroupInfoProvider.listContainsEmptyField(fieldList))
            MessageAlert.showErrorMessage(null, TextFieldGroupInfoProvider.getEmptyFieldsAsString(fieldList));
        else{
            if(TextFieldGroupInfoProvider.listContainErrorField(fieldList))
                MessageAlert.showErrorMessage(null, "One or more fields contain invalid data. Try again");
            else {
                try {
                    Student student = service.save(Integer.parseInt(id.strip()), nume.strip(), prenume.strip(), grupa.strip(), email.strip(), profesor.strip());
                    if (student == null)
                        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Save", "Studentul a fost adaugat cu succes");
                    else MessageAlert.showErrorMessage(null, "Id already found");
                } catch (RuntimeException e) {
                    MessageAlert.showErrorMessage(null, e.getMessage());
                }
            }
        }
    }

    public void handleReset(ActionEvent actionEvent){
        textFieldStudentId.clear();
        textFieldStudentNume.clear();
        textFieldStudentPrenume.clear();
        textFieldStudentGrupa.clear();
        textFieldStudentEmail.clear();
        textFieldStudentProfesor.clear();
    }



    private void showStudentUpdateDialog(Student student) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/editStudentView.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Student");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            EditStudentController editStudentViewController = loader.getController();
            editStudentViewController.setService(service, dialogStage, student);

            dialogStage.showAndWait();
            //dialogStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleUpdateStudent(ActionEvent actionEvent){
        Student selected = studentsTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            showStudentUpdateDialog(selected);
        } else
            MessageAlert.showErrorMessage(null, "Nu ati selectat nici un student");
    }

    @FXML
    public void handleDelete(ActionEvent actionEvent){
        Student selected = studentsTableView.getSelectionModel().getSelectedItem();
        if(selected != null){
            service.delete(selected.getId());
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Delete", "Studentul a fost sters");
        }
        else MessageAlert.showErrorMessage(null, "Nu ati selectat nici un student");
    }

    public void handleNoteButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/gradeView.fxml"));

            AnchorPane root = (AnchorPane) loader.load();

           // dialogStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            GradeController gradeController = loader.getController();
            gradeController.setService(new GradeBookService(new GradeDBRepository("nota"), new StudentDBRepository("student"), new AssignmentDBRepository("tema")));
            gradeController.setPrimaryStage(primaryStage);
            //dialogStage.close();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void handleClickOutsideOfTable(MouseEvent mouseEvent) {
        studentsTableView.getSelectionModel().clearSelection();
    }
}
