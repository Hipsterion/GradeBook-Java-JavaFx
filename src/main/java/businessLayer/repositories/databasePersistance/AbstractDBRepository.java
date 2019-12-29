package main.java.businessLayer.repositories.databasePersistance;

import main.java.businessLayer.domain.Entity;
import main.java.businessLayer.exceptions.RepositoryException;
import main.java.businessLayer.exceptions.ValidationException;
import main.java.businessLayer.repositories.CrudRepository;
import main.java.config.ApplicationContext;
import main.java.serviceLayer.validators.Validator;
import scala.actors.threadpool.Arrays;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractDBRepository<ID, E extends Entity<ID>> implements CrudRepository<ID, E> {
    private String tableName;
    private Statement statement;
    private Validator<E> validator;

    public AbstractDBRepository(String tableName, Validator<E> validator) throws SQLException {
        try {
            Class.forName(ApplicationContext.getPROPERTIES().getProperty("JDBC_DRIVER"));
            Connection connection = DriverManager.getConnection(ApplicationContext.getPROPERTIES().getProperty("JDBC_URL"),
                    ApplicationContext.getPROPERTIES().getProperty("JDBC_USER"),
                    ApplicationContext.getPROPERTIES().getProperty("JDBC_PASSWORD"));
            statement = connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.getMessage());
        }
        this.tableName = tableName;
        this.validator = validator;
    }

    @Override
    public E findOne(ID id) {
        String query = null;
        if(!id.getClass().equals(String.class))
            query = "select * from " + tableName + " where id = " + id.toString();
        else query = "select * from " + tableName + " where id = " + "'" + id.toString() + "'";
        try {
            ResultSet result = statement.executeQuery(query);
            result.next();
            E entity = queryRowToEntity(result);
            if(entity == null)
                throw new RepositoryException("Id not found");
            return entity;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract E queryRowToEntity(ResultSet executeQuery);

    @Override
    public Iterable<E> findAll() {
        var list = new ArrayList<E>();
        String query = "select * from " + tableName;
        try {
            ResultSet result = statement.executeQuery(query);
            while(result.next()) list.add(queryRowToEntity(result));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public E save(E entity) throws ValidationException {
        validator.validate(entity);
        try {
            if (findOne(entity.getId()) != null)
                return entity;
        }catch(RepositoryException e){}
        Class c = entity.getClass();
        Class superclass = c.getSuperclass();
        List<Field> fields = new ArrayList<>(Arrays.asList(superclass.getDeclaredFields()));
        fields.addAll(Arrays.asList(c.getDeclaredFields()));
        String query = "insert into " + tableName + " values(";
        try {
            for (var field : fields) {
                try {
                    Object obj = new PropertyDescriptor(field.getName(), entity.getClass()).getReadMethod().invoke(entity);
                    Class<?> cc = obj.getClass();
                    if (!cc.equals(String.class) && !cc.equals(LocalDateTime.class))
                        query += obj.toString() + ",";
                    else {
                        if(cc.equals(LocalDateTime.class))
                            query += "'" + LocalDateTime.parse(obj.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")) + "',";
                        else query += "'" + (String) obj + "',";}
                }catch(IntrospectionException e){}

            }
            query = query.substring(0, query.length()-1);
            query += ")";
            statement.executeUpdate(query);
            return null;
        }catch(IllegalAccessException e){
                e.printStackTrace();
            } catch(InvocationTargetException e){
                e.printStackTrace();
            } catch (SQLException e) {
            //e.printStackTrace();
        }
        return entity;
    }


    @Override
    public E delete(ID id) {
        E entity = findOne(id);
        if(entity == null) return null;
        String query=null;
        if(!id.getClass().equals(String.class))
            query = "delete from " + tableName + " where id = " + id.toString();
        else query = "delete from " + tableName + " where id = " + "'" + id.toString() + "'";

        try {
            statement.executeUpdate(query);
            return entity;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public E update(E entity) {
        validator.validate(entity);
        Entity en = null;
        try {
            en = findOne(entity.getId());
            if (en == null) return null;

        }catch(RepositoryException e){}
        Class c = entity.getClass();
        Class superclass = c.getSuperclass();
        List<Field> fields = new ArrayList<>(Arrays.asList(superclass.getDeclaredFields()));
        fields.addAll(Arrays.asList(c.getDeclaredFields()));
        en.update(entity);
        String query = "update " + tableName + " set ";
                try {
                    for (var field : fields) {
                        try {
                            Object obj = new PropertyDescriptor(field.getName(), en.getClass()).getReadMethod().invoke(en);
                            Class<?> cc = obj.getClass();
                    if (!cc.equals(String.class) && !cc.equals(LocalDateTime.class))
                        query += field.getName() + "=" + obj.toString() + ",";
                    else {
                        if(cc.equals(LocalDateTime.class))
                            query += field.getName() + "=" + "'" + LocalDateTime.parse(obj.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")) + "',";
                        else query += field.getName() + "=" + "'" + (String) obj + "',";}
                }catch(IntrospectionException e){}
            }
            query = query.substring(0, query.length()-1);
            if(!entity.getId().getClass().equals(String.class))
                query += " where id=" + en.getId().toString();
            else query += " where id=" + "'" + en.getId().toString() + "'";
            statement.executeUpdate(query);
            return null;
        }catch(IllegalAccessException e){
            e.printStackTrace();
        } catch(InvocationTargetException e){
            e.printStackTrace();
        } catch (SQLException e) {

        }
        return entity;
    }
}
