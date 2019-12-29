package main.java.businessLayer.repositories.databasePersistance;

import main.java.businessLayer.domain.Student;
import main.java.serviceLayer.validators.StudentValidator;

import java.sql.*;

public class StudentDBRepository extends AbstractDBRepository<Integer, Student> {

    public StudentDBRepository(String tableName) throws SQLException {
        super(tableName, new StudentValidator());
    }

    @Override
    public Student queryRowToEntity(ResultSet resultSet) {
        try {
            return new Student(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                    resultSet.getString(4), resultSet.getString(5), resultSet.getString(6));
        } catch (SQLException e) {
            return null;
        }

    }
}
