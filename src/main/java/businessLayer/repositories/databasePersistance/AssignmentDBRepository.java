package main.java.businessLayer.repositories.databasePersistance;

import main.java.businessLayer.domain.Assignment;
import main.java.businessLayer.domain.YearStructure;
import main.java.serviceLayer.validators.AssignmentValidator;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AssignmentDBRepository extends AbstractDBRepository<Integer, Assignment> {

    public AssignmentDBRepository(String tableName) throws SQLException {
        super(tableName, new AssignmentValidator());
    }

    @Override
    public Assignment queryRowToEntity(ResultSet resultSet) {
        try {
            return new Assignment(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(4),
                    LocalDateTime.parse(resultSet.getString(5), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), YearStructure.now());
        } catch (SQLException e) {
            return null;
        }

    }
}
