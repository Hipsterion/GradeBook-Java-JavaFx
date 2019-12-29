package main.java.businessLayer.domain;

public class AssignmentDTO {
    private Integer nrTema;
    private int deadlineWeek;

    public Integer getNrTema() {
        return nrTema;
    }

    public void setNrTema(Integer nrTema) {
        this.nrTema = nrTema;
    }

    public int getDeadlineWeek() {
        return deadlineWeek;
    }

    public void setDeadlineWeek(int deadlineWeek) {
        this.deadlineWeek = deadlineWeek;
    }

    public AssignmentDTO(Integer id, int deadlineWeek) {
        this.nrTema = id;
        this.deadlineWeek = deadlineWeek;
    }
}
