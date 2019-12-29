package main.java.businessLayer.repositories.filePersistance.csv;

import main.java.businessLayer.domain.Assignment;
import main.java.businessLayer.domain.YearStructure;
import main.java.businessLayer.repositories.CrudRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AssignmentCSVRepository extends CSVRepository<Integer, Assignment> {
    public AssignmentCSVRepository(CrudRepository<Integer, Assignment> crudRepository, String filepath) {
        super(crudRepository, filepath);
    }

    @Override
    public Assignment parseEntity(String line) {
        String[] fields = line.split(",");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return new Assignment(Integer.parseInt(fields[0]), fields[1], Integer.parseInt(fields[3]), LocalDateTime.parse(fields[4], formatter), YearStructure.now());
    }

}
