package main.java.businessLayer.domain;

import java.time.LocalDate;

public class GradeDTO {
    private String numeStudent;
    private Integer nrTema;
    private LocalDate dataPredare;
    private String profesor;
    private int value;
    private String feedback;

    public GradeDTO(String numeStudent, Integer nrTema, LocalDate dataPredare, String profesor, int value, String feedback) {
        this.numeStudent = numeStudent;
        this.nrTema = nrTema;
        this.dataPredare = dataPredare;
        this.profesor = profesor;
        this.value = value;
        this.feedback = feedback;
    }

    public String getNumeStudent() {
        return numeStudent;
    }

    public void setNumeStudent(String numeStudent) {
        this.numeStudent = numeStudent;
    }

    public Integer getNrTema() {
        return nrTema;
    }

    public void setNrTema(Integer nrTema) {
        this.nrTema = nrTema;
    }

    public LocalDate getDataPredare() {
        return dataPredare;
    }

    public void setDataPredare(LocalDate dataPredare) {
        this.dataPredare = dataPredare;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
