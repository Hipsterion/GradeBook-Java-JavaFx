package main.java.businessLayer.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class SemesterStructure extends Entity<Integer> {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime startHolidayDate;
    private LocalDateTime endHolidayDate;


    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public SemesterStructure(int id, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime startHolidayDate, LocalDateTime endHolidayDate) {
        super(id);
        this.startDate = startDate;
        this.endDate = endDate;
        this.startHolidayDate = startHolidayDate;
        this.endHolidayDate = endHolidayDate;
    }

    public int getWeek(LocalDateTime currentDate){
        int weekNumber = (int)(ChronoUnit.WEEKS.between(startDate, currentDate)) - (int)(ChronoUnit.WEEKS.between(startHolidayDate, endHolidayDate)) + 1;
        if(currentDate.isBefore(startHolidayDate))
            weekNumber = (int)(ChronoUnit.WEEKS.between(startDate, currentDate) + 1);
        else if(currentDate.isBefore(endHolidayDate))
            weekNumber = (int)(ChronoUnit.WEEKS.between(startDate, currentDate) + 1) - (int)(ChronoUnit.WEEKS.between(startHolidayDate, currentDate) + 1);
        return weekNumber;
    }

    public LocalDate getMondayDateOfGivenWeek(int week){
        return startDate.toLocalDate().plusWeeks(week);
    }
}
