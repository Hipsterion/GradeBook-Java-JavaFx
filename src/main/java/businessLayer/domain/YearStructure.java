package main.java.businessLayer.domain;

import java.time.LocalDateTime;
import java.time.Month;

public class YearStructure extends Entity<Integer>{
    private String anulUniversitar;
    private SemesterStructure semester1;
    private SemesterStructure semester2;

    public YearStructure(int id, String anulUniversitar, SemesterStructure semester1, SemesterStructure semester2) {
        super(id);
        this.anulUniversitar = anulUniversitar;
        this.semester1 = semester1;
        this.semester2 = semester2;
    }

    public String getAnulUniversitar() {
        return anulUniversitar;
    }

    public void setAnulUniversitar(String anulUniversitar) {
        this.anulUniversitar = anulUniversitar;
    }

    public SemesterStructure getSemester1() {
        return semester1;
    }

    public void setSemester1(SemesterStructure semester1) {
        this.semester1 = semester1;
    }

    public SemesterStructure getSemester2() {
        return semester2;
    }

    public void setSemester2(SemesterStructure semester2) {
        this.semester2 = semester2;
    }

    public SemesterStructure getSemestru(LocalDateTime currentTime){
        if(currentTime.isBefore(semester1.getEndDate()))
            return semester1;
        return semester2;
    }

    public static YearStructure now(){
        return new YearStructure(1, "2019-2020",
                new SemesterStructure(1, LocalDateTime.of(2019, Month.SEPTEMBER, 30, 10,0,0), LocalDateTime.of(2020, Month.JANUARY, 31, 10,0,0), LocalDateTime.of(2019, Month.DECEMBER, 23, 10,0,0), LocalDateTime.of(2020, Month.JANUARY, 6, 10,0,0)),
                new SemesterStructure(2, LocalDateTime.of(2020, Month.FEBRUARY, 10, 10,0,0), LocalDateTime.of(2020, Month.JUNE, 12, 10,0,0), LocalDateTime.of(2020, Month.APRIL, 20, 10,0,0), LocalDateTime.of(2020, Month.APRIL, 27, 10,0,0)));
    }
}
