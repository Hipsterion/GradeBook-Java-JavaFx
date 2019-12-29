package main.java.presentationLayer.GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.java.businessLayer.domain.Student;
import main.java.businessLayer.repositories.CrudRepository;
import main.java.businessLayer.repositories.databasePersistance.StudentDBRepository;
import main.java.presentationLayer.GUI.controllers.StudentController;
import main.java.serviceLayer.services.StudentService;

import java.io.IOException;

public class MainApp extends Application {
     private CrudRepository<Integer, Student> studentRepository;
     private StudentService studentService;
     public static void main(String[] args) throws IOException { launch(args);}

    @Override
    public void start(Stage stage) throws Exception {
        studentRepository = new StudentDBRepository("student");
        studentService = new StudentService(studentRepository);
        initializeStage(stage);
        stage.show();
    }

    private void initializeStage(Stage stage) throws IOException{
        FXMLLoader studentLoader = new FXMLLoader();
        studentLoader.setLocation(getClass().getResource("/views/studentView.fxml"));
        AnchorPane studentLayout = studentLoader.load();
        Scene scene = new Scene(studentLayout);
        stage.setScene(scene);

        StudentController studentController = studentLoader.getController();
        studentController.setService(studentService);
        studentController.setPrimaryStage(stage);



    }
}
