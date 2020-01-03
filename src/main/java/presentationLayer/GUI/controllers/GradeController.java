package main.java.presentationLayer.GUI.controllers;

import com.jfoenix.controls.*;
import com.jfoenix.validation.base.ValidatorBase;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import main.java.businessLayer.domain.Assignment;
import main.java.businessLayer.domain.Grade;
import main.java.businessLayer.domain.GradeDTO;
import main.java.businessLayer.domain.YearStructure;
import main.java.businessLayer.repositories.databasePersistance.StudentDBRepository;
import main.java.serviceLayer.services.GradeBookService;
import main.java.serviceLayer.services.StudentService;
import main.java.utils.events.GradeChangeEvent;
import main.java.utils.stringExtensions.StringExtensions;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import main.java.utils.observer.Observer;
import org.controlsfx.control.textfield.TextFields;

public class GradeController implements Observer<GradeChangeEvent> {
    private GradeBookService service;
    private ObservableList<GradeDTO> model = FXCollections.observableArrayList();
    private Stage primaryStage;

    @FXML
    TableView<GradeDTO> gradesTableView;
    @FXML
    TableColumn<GradeDTO, String> tableColumnNumeStudent;
    @FXML
    TableColumn<GradeDTO, Integer> tableColumnNrTema;
    @FXML
    TableColumn<GradeDTO, LocalDateTime> tableColumnDataPredare;
    @FXML
    TableColumn<GradeDTO, String> TableColumnProfesor;
    @FXML
    TableColumn<GradeDTO, Integer> tableColumnValue;
    @FXML
    TableColumn<GradeDTO, String> tableColumnFeedback;
    @FXML
    JFXComboBox<String> studentComboBox;
    @FXML
    JFXTextField textFieldProfesor;
    @FXML
    JFXTextField textFieldValue;
    @FXML
    TextField textFieldSearch;
    @FXML
    JFXTextArea textArea;
    @FXML
    JFXComboBox<String> temaComboBox;
    @FXML
    JFXDatePicker datePicker;
    @FXML
    JFXCheckBox sapt1;
    @FXML
    JFXCheckBox sapt2;
    @FXML
    ComboBox<String> reportsComboBox;
    @FXML
    ImageView statisticsIcon;


    public void setService(GradeBookService service) {
        this.service = service;
        this.service.addObserver(this);
        setUpStudentComboBox();
        setUpTemaComboBox();
        setDefaultAssignmentText();
        setUpDatePicker();
        setUpCheckBox();
        textFieldValue.textProperty().addListener((obs, oldT, newT) -> {
            if (StringExtensions.isInteger(textFieldValue.getText()))
                setPreprocessedValues();
        });
        initializeModel();
        setUpReportsComboBox();
        setUpStatisticsIcon();
    }

    private void setUpStatisticsIcon() {
        var toolTip = new Tooltip("Preview statistics graphically");
        toolTip.setShowDelay(Duration.seconds(0));
        toolTip.setHideDelay(Duration.seconds(0));
        Tooltip.install(statisticsIcon, toolTip);
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    private void setDefaultNota() {
        if (temaComboBox.getItems().contains(temaComboBox.getValue())) {
            YearStructure anUniversitar = YearStructure.now();
            int diff = getDeadLineWeek() - anUniversitar.getSemestru(datePicker.getValue().atStartOfDay()).getWeek(datePicker.getValue().atStartOfDay());
            textFieldValue.resetValidation();
            if (diff >= 0) {
                sapt1.setSelected(false);
                sapt2.setSelected(false);
                sapt1.setVisible(false);
                sapt2.setVisible(false);
                textFieldValue.setText("10");
                if (textArea.getText().toUpperCase().contains("AI INTARZIAT O SAPTAMANA") || textArea.getText().toUpperCase().contains("AI INTARZIAT O SAPTAMANA"))
                    textArea.setText("");
            } else if (diff >= -1) {
                sapt1.setSelected(false);
                sapt1.setVisible(true);
                textFieldValue.setText("9");
                if (!textArea.getText().toUpperCase().contains("AI INTARZIAT O SAPTAMANA"))
                    textArea.setText("Ai intarziat o saptamana.");
            } else if (diff >= -2) {
                sapt1.setSelected(false);
                sapt1.setVisible(true);
                sapt2.setSelected(false);
                sapt2.setVisible(true);
                textFieldValue.setText("8");
                if (!textArea.getText().toUpperCase().contains("AI INTARZIAT 2 SAPTAMANI"))
                    textArea.setText("Ai intarziat 2 saptamani.");
            } else {
                sapt1.setSelected(false);
                sapt2.setSelected(false);
                sapt1.setVisible(false);
                sapt2.setVisible(false);
                textFieldValue.setText("1");
                textArea.setText("Nu se mai poate preda tema. Se va pune 1");
            }
        }
    }

    private void setUpCheckBox() {
        sapt1.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    if (StringExtensions.isInteger(textFieldValue.getText()))
                        textFieldValue.setText(String.valueOf(Integer.parseInt(textFieldValue.getText()) + 1));
                } else {
                    if (StringExtensions.isInteger(textFieldValue.getText()))
                        textFieldValue.setText(String.valueOf(Integer.parseInt(textFieldValue.getText()) - 1));
                    setPreprocessedValues();
                }
            }
        });
        sapt2.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    if (StringExtensions.isInteger(textFieldValue.getText()))
                        textFieldValue.setText(String.valueOf(Integer.parseInt(textFieldValue.getText()) + 1));
                } else {
                    if (StringExtensions.isInteger(textFieldValue.getText()))
                        textFieldValue.setText(String.valueOf(Integer.parseInt(textFieldValue.getText()) - 1));
                    setPreprocessedValues();
                }
            }
        });
    }

    private int getDeadLineWeek() {
        String text = temaComboBox.getEditor().getText();
        String result = text.split(";")[1].strip().replaceAll("[^0-9]", "");
        return Integer.parseInt(result);
    }

    private void setPreprocessedValues() {
        int diff = service.getPunctePenalizare(datePicker.getValue().atStartOfDay(), getTemaId());
        int maxN = 10 + diff;
        if (sapt1.isSelected()) maxN++;
        if (sapt2.isSelected()) maxN++;
        if (diff <= -3) maxN = 1;
        if (StringExtensions.isInteger(textFieldValue.getText())) {
            if (Integer.parseInt(textFieldValue.getText()) > maxN) textFieldValue.setText(String.valueOf(maxN));
            if (Integer.parseInt(textFieldValue.getText()) < 1) textFieldValue.setText(String.valueOf(1));
        }
    }

    private int getTemaId() {
        String text = temaComboBox.getEditor().getText();
        String result = text.split(";")[0].strip().replaceAll("[^0-9]", "");
        return Integer.parseInt(result);
    }

    private void setUpDatePickerFormatter() {
        datePicker.setConverter(new StringConverter<LocalDate>() {
            private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            @Override
            public String toString(LocalDate localDate) {
                if (localDate == null)
                    return "";
                return dateTimeFormatter.format(localDate);
            }

            @Override
            public LocalDate fromString(String dateString) {
                if (dateString == null || dateString.trim().isEmpty()) {
                    return null;
                }
                return LocalDate.parse(dateString, dateTimeFormatter);
            }
        });
    }

    private void setDefaultDate() {

        if (temaComboBox.getItems().contains(temaComboBox.getEditor().getText()))
            datePicker.setValue(getDate());
    }

    private LocalDate getDate() {
        return YearStructure.now().getSemestru(LocalDateTime.now()).getMondayDateOfGivenWeek(getDeadLineWeek());
    }

    private void setUpTemaComboBox() {
        var teme = StreamSupport.stream(service.findAllTeme().spliterator(), false).map(x -> "Tema: " + x.getId().toString() + ";Deadline saptamana: " + String.valueOf(x.getDeadlineWeek())).collect(Collectors.toList());
        TextFields.bindAutoCompletion(temaComboBox.getEditor(), teme);
        temaComboBox.getItems().addAll(teme);
        temaComboBox.getEditor().textProperty().addListener((obs, newT, oldT) -> {
            if (temaComboBox.getItems().contains(temaComboBox.getEditor().getText()))
                setDefaultDate();
        });
    }

    private void setUpStudentComboBox() {
        var students = StreamSupport.stream(service.findAllStudents().spliterator(), false).map(x -> x.getNume() + " " + x.getPrenume()).collect(Collectors.toList());
        TextFields.bindAutoCompletion(studentComboBox.getEditor(), students);
        studentComboBox.getItems().addAll(students);
    }

    private void setDefaultAssignmentText() {
        Assignment t = service.getMatchingAssignmentForGivenDate(LocalDateTime.now());
        temaComboBox.setValue("Tema: " + t.getId().toString() + ";Deadline saptamana: " + t.getDeadlineWeek());
    }

    private void setUpDatePicker() {
        datePicker.valueProperty().addListener((obs, oldV, newV) -> {
            setDefaultNota();
        });
        datePicker.getEditor().textProperty().addListener((obs, oldV, newV) -> {
            if(!datePicker.getEditor().getText().isEmpty())
                setDefaultNota();
        });
    }

    @FXML
    public void initialize() {
        setUpTable();
        setUpComboBoxValidation(studentComboBox, new Predicate() {
            @Override
            public boolean test(Object o) {
                return StringExtensions.matchesFullNameFormat((String) o) && StreamSupport.stream(service.findAllStudents().spliterator(), false).map(x -> x.getNume() + " " + x.getPrenume()).collect(Collectors.toList()).contains((String) o);
            }
        }, "Select an existing student");
        setUpComboBoxValidation(temaComboBox, new Predicate() {
            @Override
            public boolean test(Object o) {
                return temaComboBox.getItems().contains(((String) o).strip());
            }
        }, "Select an existing assignment");
        setUpDatePickerValidation(datePicker, new Predicate() {
            @Override
            public boolean test(Object o) {
                return true;
            }
        }, "");
        setUpTextFieldValidation(textFieldProfesor, new Predicate() {
            @Override
            public boolean test(Object o) {
                return StringExtensions.matchesFullNameFormat((String) o);
            }
        }, "This must be a combination of 2 names");
        setUpTextFieldValidation(textFieldValue, new Predicate() {
            @Override
            public boolean test(Object o) {
                return StringExtensions.isInteger((String) o);
            }
        }, "This must be an integer number");
        setUpDatePickerFormatter();
    }


    private void setUpDatePickerValidation(JFXDatePicker datePicker, Predicate predicate, String errorMessage) {
        var emptyValidator = new RequiredFieldValidator();
        emptyValidator.setMessage("Please select a date");
        var regexValidator = new ValidatorBase() {
            @Override
            protected void eval() {
                if (predicate.test(datePicker.getEditor().getText())) {
                    this.hasErrors.set(false);
                } else {
                    this.hasErrors.set(true);
                }
            }
        };
        regexValidator.setMessage(errorMessage);
        datePicker.getValidators().add(emptyValidator);
        datePicker.getValidators().add(regexValidator);
        datePicker.focusedProperty().addListener((obs, old, newT) -> {
            if (!newT) datePicker.validate();
        });
    }

    private void setUpComboBoxValidation(JFXComboBox<?> comboBox, Predicate predicate, String errorMessage) {
        var emptyValidator = new RequiredFieldValidator();
        emptyValidator.setMessage("This field cannot be empty");
        var regexValidator = new ValidatorBase() {
            @Override
            protected void eval() {
                if (predicate.test(comboBox.getEditor().getText())) {
                    this.hasErrors.set(false);
                } else {
                    this.hasErrors.set(true);
                }
            }
        };
        regexValidator.setMessage(errorMessage);
        comboBox.getValidators().add(emptyValidator);
        comboBox.getValidators().add(regexValidator);
        comboBox.focusedProperty().addListener((obs, old, newT) -> {
            if (!newT) comboBox.validate();
        });
    }

    private void setUpTextFieldValidation(JFXTextField textField, Predicate predicate, String errorMessage) {
        var emptyValidator = new RequiredFieldValidator();
        emptyValidator.setMessage("This field cannot be empty");
        var regexValidator = new ValidatorBase() {
            @Override
            protected void eval() {
                TextInputControl textField = (TextInputControl) this.srcControl.get();
                if (predicate.test(textField.getText())) {
                    this.hasErrors.set(false);
                } else {
                    this.hasErrors.set(true);
                }
            }
        };
        regexValidator.setMessage(errorMessage);
        textField.getValidators().add(emptyValidator);
        textField.getValidators().add(regexValidator);
        textField.focusedProperty().addListener((obs, oldT, newT) -> {
            if (!newT) textField.validate();
        });
    }

    private void setUpTable() {
        tableColumnNumeStudent.setCellValueFactory(new PropertyValueFactory<GradeDTO, String>("numeStudent"));
        tableColumnNrTema.setCellValueFactory(new PropertyValueFactory<GradeDTO, Integer>("nrTema"));
        tableColumnDataPredare.setCellValueFactory(new PropertyValueFactory<GradeDTO, LocalDateTime>("dataPredare"));
        TableColumnProfesor.setCellValueFactory(new PropertyValueFactory<GradeDTO, String>("profesor"));
        tableColumnValue.setCellValueFactory(new PropertyValueFactory<GradeDTO, Integer>("value"));
        tableColumnFeedback.setCellValueFactory(new PropertyValueFactory<GradeDTO, String>("feedback"));
        gradesTableView.setItems(model);
    }

    private void initializeModel() {
        Iterable<Grade> students = service.findAll();
        List<GradeDTO> studentList = StreamSupport.stream(students.spliterator(), false).map(x -> new GradeDTO(service.findStudent(x.getIdStudent()).getNume() + " " + service.findStudent(x.getIdStudent()).getPrenume(), x.getIdTema(), Date.from(x.getData().atZone(ZoneId.systemDefault()).toInstant()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), x.getProfesor(), x.getValue(), x.getFeedback()))
                .collect(Collectors.toList());
        model.setAll(studentList);
    }

    @Override
    public void update(GradeChangeEvent studentChangeEvent) {
        initializeModel();
    }

    public void handleSave(ActionEvent actionEvent) {
        studentComboBox.validate();
        temaComboBox.validate();
        datePicker.validate();
        textFieldProfesor.validate();
        textFieldValue.validate();
        textArea.validate();
        if (studentComboBox.getActiveValidator() != null || temaComboBox.getActiveValidator() != null || datePicker.getActiveValidator() != null || textFieldProfesor.getActiveValidator() != null || textFieldValue.getActiveValidator() != null || textArea.getActiveValidator() != null)
            MessageAlert.showErrorMessage(null, "One or more fields contain invalid data. Go back");
        else {
            try {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/previewAddGradeView.fxml"));
                AnchorPane root = (AnchorPane) loader.load();
                Stage dialogStage = new Stage();
                dialogStage.setTitle("Edit Student");
                dialogStage.initModality(Modality.APPLICATION_MODAL);
                Scene scene = new Scene(root);
                dialogStage.setScene(scene);
                PreviewAddGradeController editStudentViewController = loader.getController();
                editStudentViewController.setService(service, dialogStage, new GradeDTO(studentComboBox.getValue(), getTemaId(), datePicker.getValue(), textFieldProfesor.getText(), Integer.parseInt(textFieldValue.getText()), textArea.getText()));
                dialogStage.showAndWait();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleReset(ActionEvent actionEvent) {
        studentComboBox.getEditor().clear();
        studentComboBox.resetValidation();
        temaComboBox.getEditor().clear();
        temaComboBox.resetValidation();
        datePicker.getEditor().clear();
        datePicker.resetValidation();
        textFieldProfesor.clear();
        textFieldProfesor.resetValidation();
        textFieldValue.clear();
        textFieldValue.resetValidation();
        textArea.clear();
        textArea.resetValidation();
    }

    public void handleUpdateStudent(ActionEvent actionEvent) {
    }

    public void handleDelete(ActionEvent actionEvent) {
    }

    public void handleStudentsButton(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/studentView.fxml"));
            AnchorPane root = (AnchorPane) loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            StudentController studentController = loader.getController();
            studentController.setService(new StudentService(new StudentDBRepository("student")));
            studentController.setPrimaryStage(primaryStage);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void setUpReportsComboBox() {
        reportsComboBox.getItems().add("Note finale studenti");
        reportsComboBox.getItems().add("Cea mai grea tema");
        reportsComboBox.getItems().add("Studentii care pot intra in examen");
        reportsComboBox.getItems().add("Studentii fara intarzieri");
        reportsComboBox.valueProperty().addListener((obs, oldv, newv) -> {
                    if(newv.contains("finale")) {
                        String reportMessage = "";
                        for(var student : service.findAllStudents())
                            reportMessage += student.getNume() + " " + student.getPrenume() + " Nota:  " + String.valueOf(service.getStudentFinalGradeValue(student)) + "\n";
                        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Studentii si mediile lor finale", reportMessage);
                    }
                    else if(newv.contains("grea")) {
                        String reportMessage = "";
                        Assignment assignment = service.getAssignmentWithLowestGrades();
                        reportMessage += "Laborator-ul din saptamana " + String.valueOf(assignment.getStartWeek()) +"-"+String.valueOf(assignment.getDeadlineWeek()) + " media: " + String.valueOf(service.getAssignmentAverageResults(assignment)) + "\n";
                        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Tema cu cea mai mica medie a notelor primite", reportMessage);
                    }
                    else if(newv.contains("examen")) {
                        String reportMessage = "";
                        for(var student : service.findAllStudents())
                            if(service.getStudentFinalGradeValue(student) > 4.0)
                                reportMessage += student.getNume() + " " + student.getPrenume() + " Nota:  " + String.valueOf(service.getStudentFinalGradeValue(student)) + "\n";
                        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Studentii ce pot intra in examen", reportMessage);
                    }
                    else {
                        String reportMessage = "";
                        for(var student : service.getStudentsWithoutDelayedAssignments())
                            reportMessage += student.getNume() + " " + student.getPrenume() + "\n";
                        MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Studentii fara intarzieri", reportMessage);
                    }
                }
        );
    }

    public void handleStatisticsPreview(MouseEvent mouseEvent) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/statisticsView.fxml"));
        AnchorPane root = null;
        try {
            root = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Preview Statistics");
            //dialogStage.initModality(Modality.APPLICATION_MODAL);
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);
            StatisticsController statisticsController = loader.getController();
            statisticsController.setService(service);
            dialogStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleGeneratePdf(ActionEvent actionEvent) {
        service.saveReportsInPdf();
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File("E:\\GradeBook-Java-JavaFx-master\\src\\main\\resources\\pdfs\\StatisticsReports.pdf");
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                // no application registered for PDFs
            }
        }
    }
}