package main.java.presentationLayer.consoleUI;

import main.java.businessLayer.domain.Assignment;
import main.java.businessLayer.domain.Grade;
import main.java.businessLayer.domain.YearStructure;
import main.java.businessLayer.domain.Student;
import main.java.businessLayer.repositories.databasePersistance.GradeDBRepository;
import main.java.businessLayer.repositories.databasePersistance.StudentDBRepository;
import main.java.businessLayer.repositories.databasePersistance.AssignmentDBRepository;
import main.java.serviceLayer.services.GradeBookService;
import main.java.serviceLayer.services.StudentService;
import main.java.serviceLayer.services.AssignmentService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    public static void invoke() throws IOException {
        YearStructure yearStructure = YearStructure.now();

        //StudentInMemoryRepository memoryStudentRepo = new StudentInMemoryRepository();
        //TemaInMemoryRepository memoryTemaRepo = new TemaInMemoryRepository();
        //NotaInMemoryRepository memoryNota = new NotaInMemoryRepository();

        //StudentCSVRepository studRepo = new StudentCSVRepository(memoryStudentRepo, ApplicationContext.getPROPERTIES().getProperty("data.gradebook.studentiCSV"));
        StudentDBRepository studRepo = null;
        AssignmentDBRepository temaRepo = null;
        GradeDBRepository notaRepo = null;
        try {
            studRepo = new StudentDBRepository("student");
            temaRepo = new AssignmentDBRepository("tema");
            notaRepo = new GradeDBRepository("nota");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //StudentXMLRepository studRepo = new StudentXMLRepository(memoryStudentRepo, ApplicationContext.getPROPERTIES().getProperty("data.gradebook.studentiXML"));
        //TemaCSVRepository temaRepo = new TemaCSVRepository(memoryTemaRepo, ApplicationContext.getPROPERTIES().getProperty("data.gradebook.temeCSV"));
        //TemaXMLRepository temaRepo = new TemaXMLRepository(memoryTemaRepo, ApplicationContext.getPROPERTIES().getProperty("data.gradebook.temeXML"));
        //NotaCSVRepository notaRepo = new NotaCSVRepository(memoryNota, ApplicationContext.getPROPERTIES().getProperty("data.gradebook.noteCSV"));
        //NotaXMLRepository notaRepo = new NotaXMLRepository(memoryNota, ApplicationContext.getPROPERTIES().getProperty("data.gradebook.noteXML"));
        StudentService studentService = new StudentService(studRepo);
        AssignmentService assignmentService = new AssignmentService(temaRepo);
        GradeBookService gradeBookService = new GradeBookService(notaRepo, studRepo, temaRepo);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Scanner input = new Scanner(System.in);
        while (true) {
            showMenu();
            String cmd = reader.readLine();
            if (cmd.equals("Exit"))
                return;
            else if (cmd.equals("Add student")) {
                ConsoleInputValidator consoleInputValidator = new ConsoleInputValidator();
                System.out.println("ID=");
                String idS = reader.readLine();
                consoleInputValidator.validateIntegerId(idS);
                System.out.println("Nume=");
                String nume = reader.readLine();
                System.out.println("Prenume=");
                String prenume = reader.readLine();
                System.out.println("Email=");
                String email = reader.readLine();
                consoleInputValidator.validateEmail(email);
                System.out.println("Cadru=");
                String cadru = reader.readLine();
                System.out.println("Grupa=");
                String grupa = reader.readLine();
                if (consoleInputValidator.getErrorMessage().equals("")) {
                    try {
                        studentService.save(Integer.parseInt(idS), nume, prenume, grupa, email, cadru);
                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                else System.out.println(consoleInputValidator.getErrorMessage());
            }
             else if (cmd.equals("Delete student")) {
                ConsoleInputValidator consoleInputValidator = new ConsoleInputValidator();
                System.out.println("ID=");
                String idS = reader.readLine();
                consoleInputValidator.validateIntegerId(idS);
                if (consoleInputValidator.getErrorMessage().equals("")) {
                    try {
                        studentService.delete(Integer.parseInt(idS));
                    } catch(RuntimeException e){
                        System.out.println(e.getMessage());
                    }
                }
                else System.out.println(consoleInputValidator.getErrorMessage());
            }
             else if (cmd.equals("Update student")) {
                ConsoleInputValidator consoleInputValidator = new ConsoleInputValidator();
                System.out.println("ID=");
                String idS = reader.readLine();
                consoleInputValidator.validateIntegerId(idS);
                System.out.println("Nume=");
                String nume = reader.readLine();
                System.out.println("Prenume=");
                String prenume = reader.readLine();
                System.out.println("Email=");
                String email = reader.readLine();
                consoleInputValidator.validateEmail(email);
                System.out.println("Cadru=");
                String cadru = reader.readLine();
                System.out.println("Grupa=");
                String grupa = reader.readLine();
                if (consoleInputValidator.getErrorMessage().equals("")) {
                    try {
                        studentService.update(Integer.parseInt(idS), nume, prenume, grupa, email, cadru);
                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                    }
                }
                else System.out.println(consoleInputValidator.getErrorMessage());
            }
             else if (cmd.equals("Find student")) {
                ConsoleInputValidator consoleInputValidator = new ConsoleInputValidator();
                System.out.println("ID=");
                String idS = reader.readLine();
                consoleInputValidator.validateIntegerId(idS);
                if (consoleInputValidator.getErrorMessage().equals("")) {
                    try {
                        System.out.println(studentService.findOne(Integer.parseInt(idS)));
                    } catch(RuntimeException e){
                        System.out.println(e.getMessage());
                    }
                }
                else System.out.println(consoleInputValidator.getErrorMessage());
            } else if (cmd.equals("Find all students")) {
                Iterable<Student> st = studentService.findAll();
                for (Student s : st) {
                    System.out.println(s);
                }
            }


             else if (cmd.equals("Add t")) {
                 try{
                    System.out.println("ID tema=");
                    int idt = Integer.parseInt(reader.readLine());
                    System.out.println("Descriere=");
                    String desc = reader.readLine();
                    System.out.println("Deadline=");
                    String deadlin = reader.readLine();
                    int deadline = Integer.parseInt(deadlin);
                    System.out.println("Data crearii temei =");
                    String date = reader.readLine();
                    LocalDateTime startDate = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    assignmentService.save(idt, desc, deadline, startDate, yearStructure);
                } catch(NumberFormatException e){
                     System.out.println("Id-ul trebuie sa fie intreg.");
                 }
                 catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }
            } else if (cmd.equals("Delete t")) {
                 try{
                    System.out.println("ID=");
                    int id = Integer.parseInt(reader.readLine());
                    assignmentService.delete(id);
                }catch(RuntimeException e){
                    System.out.println(e.getMessage());
                }

            } else if (cmd.equals("Update t")) {
                try{
                    System.out.println("ID tema=");
                    int idt = Integer.parseInt(reader.readLine());
                    System.out.println("Descriere=");
                    String desc = reader.readLine();
                    System.out.println("Deadline=");
                    int deadl = Integer.parseInt(reader.readLine());
                    System.out.println("Start date= ");
                    String date = reader.readLine();
                    LocalDateTime startDate = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    assignmentService.update(idt, desc, deadl, startDate, yearStructure);
                } catch (RuntimeException e) {
                    System.out.println(e.getMessage());
                }

            } else if (cmd.equals("Find t")) {
                System.out.println("ID=");
                try{
                    int id = Integer.parseInt(reader.readLine());
                    System.out.println(assignmentService.findOne(id));
                } catch(RuntimeException e){
                    System.out.println(e.getMessage());
                }
            } else if (cmd.equals("Find all t")) {
                Iterable<Assignment> tt = assignmentService.findAll();
                for (Assignment t : tt) {
                    System.out.println(t);
                }
            }
             else if (cmd.equals("Add n")) {
                ConsoleInputValidator consoleInputValidator = new ConsoleInputValidator();
                System.out.println("ID student= ");
                String idSt = reader.readLine();
                System.out.println("ID tema= ");
                String idTema = reader.readLine();
                System.out.println("Adaugata in data de= ");
                String data = reader.readLine();
                System.out.println("Profesor=");
                String profesor = reader.readLine();
                System.out.println("Value=");
                String value = reader.readLine();
                System.out.println("Feedback=");
                String feedback = reader.readLine();

                consoleInputValidator.validateIntegerId(idSt);
                consoleInputValidator.validateIntegerId(idTema);
                consoleInputValidator.parsableToInteger(value);
                consoleInputValidator.validateLocalDateTime(data);
                if(!consoleInputValidator.errorsAreFound())
                    try {
                        gradeBookService.save(Integer.parseInt(idSt), Integer.parseInt(idTema), LocalDateTime.parse(data, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), profesor, Integer.parseInt(value), feedback);
                    } catch (RuntimeException e) {
                        System.out.println(e.getMessage());
                    }
                else System.out.println(consoleInputValidator.getErrorMessage());
            }
                 else if (cmd.equals("Find n")) {
                System.out.println("ID=");
                String id = reader.readLine();
                try {
                    System.out.println(gradeBookService.findOne(id));
                }catch(RuntimeException e){
                    System.out.println(e.getMessage());
                }
            } else if (cmd.equals("Find all n")) {
                Iterable<Grade> tt = gradeBookService.findAll();
                for (var t : tt) {
                    System.out.println(t);
                }
            } else if (cmd.equals("Delete n")) {
                System.out.println("ID=");
                String id = reader.readLine();
                try {
                    System.out.println(gradeBookService.delete(id));
                }
                catch(RuntimeException e){
                    System.out.println(e.getMessage());
                }
            }
                 else if (cmd.equals("Update n")) {
                 try{
                    System.out.println("ID student= ");
                    int idSt = Integer.parseInt(reader.readLine());
                    System.out.println("ID tema= ");
                    int idTema = Integer.parseInt(reader.readLine());
                    System.out.println("Adaugata in data de= ");
                    String data = reader.readLine();
                    System.out.println("Profesor=");
                    String profesor = reader.readLine();
                    System.out.println("Value=");
                    int value = Integer.parseInt(reader.readLine());
                    System.out.println("Feedback=");
                    String feedback = reader.readLine();
                    gradeBookService.update(idSt, idTema, LocalDateTime.parse(data, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), profesor, value, feedback);
                } catch(RuntimeException e){
                    System.out.println(e.getMessage());
                }
            }
                 else if(cmd.equals("Filter students by grupa")){
                System.out.println("Grupa = ");
                     String grupa = reader.readLine();
                     List<Student> results = gradeBookService.filterStudentsByGrupa(grupa);
                     if(results.size() == 0) System.out.println("No results");
                     else results.forEach(System.out::println);
            }
                 else if(cmd.equals("Filter students by given tema")){
                     ConsoleInputValidator consoleInputValidator = new ConsoleInputValidator();
                System.out.println("Id tema = ");
                String idTema = reader.readLine();
                consoleInputValidator.validateIntegerId(idTema);
                if(consoleInputValidator.errorsAreFound()) System.out.println(consoleInputValidator.getErrorMessage());
                else {
                    List<Student> results = gradeBookService.filterStudentsByTema(Integer.parseInt(idTema));
                    if(results.size() == 0) System.out.println("No results");
                    else results.forEach(System.out::println);
                }
            }
            else if(cmd.equals("Filter students by given tema and professor")){
                ConsoleInputValidator consoleInputValidator = new ConsoleInputValidator();
                System.out.println("Id tema = ");
                String idTema = reader.readLine();
                consoleInputValidator.validateIntegerId(idTema);
                System.out.println("Profesor = ");
                String profesor = reader.readLine();
                if(consoleInputValidator.errorsAreFound()) System.out.println(consoleInputValidator.getErrorMessage());
                else {
                    List<Student> results = gradeBookService.filterStudentsByTemaAndProfesor(Integer.parseInt(idTema), profesor);
                    if(results.size() == 0) System.out.println("No results");
                    else results.forEach(System.out::println);
                }
            }
            else if(cmd.equals("Filter note by tema and data")){
                ConsoleInputValidator consoleInputValidator = new ConsoleInputValidator();
                System.out.println("Id tema = ");
                String idTema = reader.readLine();
                consoleInputValidator.validateIntegerId(idTema);
                System.out.println("Week = ");
                String week = reader.readLine();
                consoleInputValidator.validateIntegerWeek(week);
                if(consoleInputValidator.errorsAreFound()) System.out.println(consoleInputValidator.getErrorMessage());
                else {
                    List<Grade> results = gradeBookService.filterNoteByTemaAndWeek(Integer.parseInt(idTema), Integer.parseInt(week));
                    if(results.size() == 0) System.out.println("No results");
                    else results.forEach(System.out::println);
                }
            }
        }
    }

    private static void showMenu() {
        System.out.println("Meniu:");
        System.out.println("1.Add student/t/n");
        System.out.println("2.Find one student/t/n");
        System.out.println("3.Find all students/t/n");
        System.out.println("4.Delete student/t/n");
        System.out.println("5.Update student/t/n");
        System.out.println("6.Filter students by grupa");
        System.out.println("7.Filter students by given tema");
        System.out.println("8.Filter students by given tema and professor");
        System.out.println("9.Filter note by tema and data");
        System.out.println("10.Exit");
    }
}


