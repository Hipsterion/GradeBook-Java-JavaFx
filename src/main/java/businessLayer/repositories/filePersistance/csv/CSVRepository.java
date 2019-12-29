package main.java.businessLayer.repositories.filePersistance.csv;

import main.java.businessLayer.domain.Entity;
import main.java.businessLayer.repositories.CrudRepository;
import main.java.businessLayer.repositories.filePersistance.AbstractFileRepository;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public abstract class CSVRepository<ID,E extends Entity<ID>> extends AbstractFileRepository<ID,E> {
    public CSVRepository(CrudRepository crudRepository, String filepath) {
        super(crudRepository, filepath);
    }

    public abstract E parseEntity(String line);

    @Override
    public void load() {
        File file = new File(filepath);
        if (file.exists() && !file.isDirectory()) {
            try {
                List<String> lines = Files.readAllLines(Paths.get(filepath));
                for (int i = 0; i < lines.size(); i++) {
                    String line = lines.get(i);
                    var entity = parseEntity(line);
                    crudRepository.save(entity);
                }
            } catch (IOException ex) {
            }
        }
    }

    @Override
    public void saveChanges() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filepath));
            for (var entity : crudRepository.findAll())
                writer.write(entity.toString());
            writer.close();
        } catch (IOException e) {
        }
    }
}
