package main.java.businessLayer.domain;

public class Professor extends Entity<Integer> {
    private String nume;
    private String prenume;
    private String email;

    public Professor(int id, String nume, String prenume, String email) {
        super(id);
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
