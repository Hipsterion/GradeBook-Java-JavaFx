package main.java.businessLayer.repositories.databasePersistance;
import main.java.businessLayer.domain.Grade;
import main.java.serviceLayer.validators.GradeValidator;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GradeDBRepository extends AbstractDBRepository<String, Grade> {

    public GradeDBRepository(String tableName) throws SQLException {
        super(tableName, new GradeValidator());
    }

    @Override
    public Grade queryRowToEntity(ResultSet resultSet) {
        try {
            return new Grade(resultSet.getInt(2), resultSet.getInt(3),
                    LocalDateTime.parse(resultSet.getString(4), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), resultSet.getString(5), resultSet.getInt(6), resultSet.getString(7));
        } catch (SQLException e) {
            return null;
        }

    }
}
