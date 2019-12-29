package main.java.businessLayer.domain;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Assignment extends Entity<Integer> {
    private String descriere;
    private int startWeek;
    private int deadlineWeek;
    private LocalDateTime startDate;


    public Assignment(int id, String descriere, int deadlineWeek, LocalDateTime startDate, YearStructure yearStructure) {
        super(id);
        this.descriere = descriere;
        this.deadlineWeek = deadlineWeek;
        this.startDate = startDate;
        this.startWeek = yearStructure.getSemestru(startDate).getWeek(startDate);
    }
    public String getDescriere() {
        return descriere;
    }

    public void setDescriere(String descriere) {
        this.descriere = descriere;
    }
    public int getStartWeek() {
        return startWeek;
    }
    public int getDeadlineWeek() {
        return deadlineWeek;
    }

    public void setDeadlineWeek(int deadlineWeek) throws IllegalArgumentException{
        if(startWeek > deadlineWeek)
            throw new IllegalArgumentException("Nr saptamanii curente trebuei sa fie mai mic sau egal decat nr saptamanii cu termenul de predare");
        this.deadlineWeek = deadlineWeek;
    }
    public LocalDateTime getStartDate(){
        return startDate;
    }

    public void setStartWeek(int startWeek) {
        //this.startWeek = startWeek;
    }
    public void setStartDate(LocalDateTime startDate){}

    @Override
    public void update(Entity entity){
        var tema = (Assignment) entity;
        this.setDeadlineWeek(tema.getDeadlineWeek());
    }

    @Override
    public String toString() {
        return getId() + "," + getDescriere() + "," + getStartWeek() + "," + getDeadlineWeek() + "," + getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")) + "\n";
    }
}

