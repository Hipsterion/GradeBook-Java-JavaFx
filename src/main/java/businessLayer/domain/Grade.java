package main.java.businessLayer.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Grade extends Entity<String> {
    private Integer idStudent;
    private Integer idTema;
    private LocalDateTime data;
    private String profesor;
    private int value;
    private String feedback;


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setIdStudent(Integer idStudent) {
        this.idStudent = idStudent;
    }

    public void setIdTema(Integer idTema) {
        this.idTema = idTema;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public Grade(Integer idSt, Integer idTema, LocalDateTime data, String profesor, int value, String feedback) {
        super(idSt.toString() + "-" + idTema.toString());
        this.idStudent = idSt;
        this.idTema = idTema;
        this.data = data;
        this.profesor = profesor;
        this.value = value;
        this.feedback = feedback;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public String getProfesor() {
        return profesor;
    }

    @Override
    public String toString() {
        return getId() + "," + getIdStudent() + "," + getIdTema() + "," + getData().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + "," + getProfesor() + "," + getValue() + "," + getFeedback() + "\n";
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    @Override
    public void update(Entity<String> entity) {
        Grade grade = (Grade) entity;
        this.setValue(grade.getValue());
    }

    public Integer getIdStudent() {
        return idStudent;
    }

    public Integer getIdTema() {
        return idTema;
    }
}
