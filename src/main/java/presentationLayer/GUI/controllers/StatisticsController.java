package main.java.presentationLayer.GUI.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.effect.Reflection;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.java.serviceLayer.services.GradeBookService;
import main.java.utils.events.GradeChangeEvent;
import main.java.utils.observer.Observer;

import java.util.ArrayList;
import java.util.Set;
import java.util.stream.StreamSupport;

public class StatisticsController implements Observer<GradeChangeEvent> {
    private GradeBookService service;

    public void setService(GradeBookService service){
        this.service = service;
        this.service.addObserver(this);
        setUpReport1PieChart();
        setUpReport4PieChart();
        setUpReport3PieChart();
    }



    @FXML
    PieChart report4PieChart;
    @FXML
    PieChart report3PieChart;
    @FXML
    PieChart report1PieChart;
    @FXML
    Label caption;


    @FXML
    private void initialize() {

    }

    private void setUpReport1PieChart() {
        var list = new ArrayList<Float>();
        for(var student : service.findAllStudents())
            list.add(service.getStudentFinalGradeValue(student));
        var numberOfLowStudents = StreamSupport.stream(list.spliterator(), false).filter(n -> n < 5).count();
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Studenti cu nota finala intre 0-5", numberOfLowStudents),
                        new PieChart.Data("Studenti cu nota finala intre 5-10", StreamSupport.stream(service.findAllStudents().spliterator(), false).count() - numberOfLowStudents)
                );
        report1PieChart.setData(pieChartData);
        report1PieChart.setTitle("Statistica note finale studenti");
        pieChartData.get(0).getNode().setStyle("-fx-pie-color: " + "#d1e6ff;");
        pieChartData.get(1).getNode().setStyle("-fx-pie-color: " + "#9fdeff;");
        Set<Node> items = report1PieChart.lookupAll("Label.chart-legend-item");
        int i = 0;
        Color[] colors = { Color.web("#d1e6ff"), Color.web("#9fdeff"), Color.web("#22bad9"), Color.web("#0181e2"), Color.web("#2f357f") };
        for (Node item : items) {
            Label label = (Label) item;
            final Rectangle rectangle = new Rectangle(10, 10, colors[i]);
            final Glow niceEffect = new Glow();
            niceEffect.setInput(new Reflection());
            rectangle.setEffect(niceEffect);
            label.setGraphic(rectangle);
            i++;
        }
        for (final PieChart.Data data : report1PieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                        @Override public void handle(MouseEvent e) {
                            caption.setTranslateX(e.getSceneX());
                            caption.setTranslateY(e.getSceneY());
                            caption.setText(String.valueOf(Math.round(data.getPieValue())));
                            caption.setVisible(true);
                        }
                    });
        }
    }

    private void setUpReport3PieChart() {
        var numberOfAlrightStudents = StreamSupport.stream(service.findAllStudents().spliterator(), false).filter(s -> service.getStudentFinalGradeValue(s) > 4).count();
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Studenti ce pot intra in examen", numberOfAlrightStudents),
                        new PieChart.Data("Studenti cu nu pot intra in examen", StreamSupport.stream(service.findAllStudents().spliterator(), false).count() - numberOfAlrightStudents)
                );
        report3PieChart.setData(pieChartData);
        report3PieChart.setTitle("Statistica studenti acceptati in sesiune");
        pieChartData.get(0).getNode().setStyle("-fx-pie-color: " + "yellow;");
        pieChartData.get(1).getNode().setStyle("-fx-pie-color: " + "#74ff44;");
        Set<Node> items = report3PieChart.lookupAll("Label.chart-legend-item");
        int i = 0;
        Color[] colors = { Color.web("yellow"), Color.web("#74ff44"), Color.web("#22bad9"), Color.web("#0181e2"), Color.web("#2f357f") };
        for (Node item : items) {
            Label label = (Label) item;
            final Rectangle rectangle = new Rectangle(10, 10, colors[i]);
            final Glow niceEffect = new Glow();
            niceEffect.setInput(new Reflection());
            rectangle.setEffect(niceEffect);
            label.setGraphic(rectangle);
            i++;
        }
        for (final PieChart.Data data : report3PieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                        @Override public void handle(MouseEvent e) {
                            caption.setTranslateX(e.getSceneX());
                            caption.setTranslateY(e.getSceneY());
                            caption.setText(String.valueOf(Math.round(data.getPieValue())));
                            caption.setVisible(true);
                        }
                    });
        }
    }

    private void setUpReport4PieChart() {
        var numberOfStudentsWithoutDelay = StreamSupport.stream(service.getStudentsWithoutDelayedAssignments().spliterator(), false).count();
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("Studenti fara intarzieri de predare", numberOfStudentsWithoutDelay),
                        new PieChart.Data("Studenti cu intarzieri de predare", StreamSupport.stream(service.findAllStudents().spliterator(), false).count() - numberOfStudentsWithoutDelay)
                        );
        report4PieChart.setData(pieChartData);
        report4PieChart.setTitle("Statistica intarzieri la predare");
        for (final PieChart.Data data : report4PieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                    new EventHandler<MouseEvent>() {
                        @Override public void handle(MouseEvent e) {
                            caption.setTranslateX(e.getSceneX());
                            caption.setTranslateY(e.getSceneY());
                            caption.setText(String.valueOf(Math.round(data.getPieValue())));
                            caption.setVisible(true);
                        }
                    });
        }
    }

    @Override
    public void update(GradeChangeEvent gradeChangeEvent) {
        setUpReport1PieChart();
        setUpReport3PieChart();
        setUpReport4PieChart();
    }
}
