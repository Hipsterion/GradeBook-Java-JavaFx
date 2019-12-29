package main.java.businessLayer.repositories.filePersistance.csv;

import main.java.businessLayer.domain.Grade;
import main.java.businessLayer.repositories.CrudRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GradeCSVRepository extends CSVRepository<String, Grade> {
    public GradeCSVRepository(CrudRepository<String, Grade> crudRepository, String filepath) {
        super(crudRepository, filepath);
    }

    @Override
    public Grade parseEntity(String line) {
        String[] fields = line.split(",");
        return new Grade(Integer.parseInt(fields[1]), Integer.parseInt(fields[2]), LocalDateTime.parse(fields[3], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), fields[4], Integer.parseInt(fields[5]), fields[6]);
    }

}

