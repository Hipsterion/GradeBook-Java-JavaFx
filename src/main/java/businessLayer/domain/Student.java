package main.java.businessLayer.domain;

import java.io.Serializable;

public class Student extends Entity<Integer> implements Serializable {
    private String nume;
    private String prenume;
    private String grupa;
    private String email;
    private String profesor;


    public Student(int id, String nume, String prenume, String grupa, String email, String cadruDidacticIndrumatorLab) {
        super(id);
        this.nume = nume;
        this.prenume = prenume;
        this.grupa = grupa;
        this.email = email;
        this.profesor = cadruDidacticIndrumatorLab;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getGrupa() {
        return grupa;
    }

    public void setGrupa(String grupa) {
        this.grupa = grupa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    @Override
    public void update(Entity entity){
        var student = (Student) entity;
        this.setGrupa(student.getGrupa());
        this.setNume(student.getNume());
        this.setPrenume(student.getPrenume());
        this.setEmail(student.getEmail());
        this.setProfesor(student.getProfesor());
    }

    @Override
    public String toString() {
        return getId() + "," + getNume() + "," + getPrenume() + "," + getGrupa() + "," + getEmail() + "," + getProfesor() + "\n";
    }
}
