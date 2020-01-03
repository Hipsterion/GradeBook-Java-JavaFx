package main.java.serviceLayer.services;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import main.java.businessLayer.domain.Grade;
import main.java.businessLayer.domain.YearStructure;
import main.java.businessLayer.domain.Student;
import main.java.businessLayer.domain.Assignment;
import main.java.businessLayer.exceptions.ValidationException;
import main.java.businessLayer.repositories.CrudRepository;
import main.java.config.ApplicationContext;
import main.java.utils.events.ChangeEventType;
import main.java.utils.events.GradeChangeEvent;
import main.java.utils.observer.Observable;
import main.java.utils.observer.Observer;

import java.io.*;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class GradeBookService implements Observable<GradeChangeEvent> {
    private CrudRepository<String, Grade> gradeRepository;
    private CrudRepository<Integer, Student> studentRepository;
    private CrudRepository<Integer, Assignment> assignmentRepository;

    public GradeBookService(CrudRepository<String, Grade> gradeRepository, CrudRepository<Integer, Student> studentRepository, CrudRepository<Integer, Assignment> assignmentRepository) {
        this.gradeRepository = gradeRepository;
        this.studentRepository = studentRepository;
        this.assignmentRepository = assignmentRepository;
        saveReportsInPdf();
    }

    public Grade findOne(String id) {
        return gradeRepository.findOne(id);
    }

    public Iterable<Grade> findAll() {
        return gradeRepository.findAll();
    }

    public Grade save(Integer idSt, Integer idTema, LocalDateTime data, String profesor, int value, String feedback) throws ValidationException {
        int newValue = getPunctePenalizare(data, idTema);
        if (newValue < -2)
            newValue = 1;
        else
            newValue += value;
        var nota = new Grade(idSt, idTema, data, profesor, newValue, feedback);
        try {
            export(nota);
        } catch (IOException e) {
        }
        var notaOld = gradeRepository.save(nota);
        notifyObservers(new GradeChangeEvent(ChangeEventType.ADD, notaOld));
        return notaOld;
    }

    public void sendEmailToStudentWithThread(String studentMail){
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                sendEmailToStudent(studentMail);
            }
        });
    }
    public void sendEmailToStudent(String studentEmail) {
        final String username = "mgradebook@gmail.com";  // like yourname@outlook.com
        final String password = "cocolino123";   // password here

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        session.setDebug(true);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(studentEmail));   // like inzi769@gmail.com
            message.setSubject("You've just got a new GRADE");
            message.setText("Your teacher just gave you a grade. Check it out in the GradeBook app.");

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public Grade delete(String id) {
        return gradeRepository.delete(id);
    }

    public Grade update(Integer idSt, Integer idTema, LocalDateTime data, String profesor, int value, String feedback) {
        var nota = new Grade(idSt, idTema, data, profesor, value, feedback);
        return gradeRepository.update(nota);
    }

    private void export(Grade grade) throws IOException {
        BufferedWriter writer = new BufferedWriter(
                new FileWriter(ApplicationContext.getPROPERTIES().getProperty("serializedFiles.path") + studentRepository.findOne(grade.getIdStudent()).getNume() + studentRepository.findOne(grade.getIdStudent()).getPrenume() + ".txt", true)  //Set true for append mode
        );
        writer.write("Tema: " + grade.getIdTema() + " ; " + "Nota: " + grade.getValue() + " ; " + "Predata in saptamana: " + grade.getData().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")) + " ; " + "Deadline: " + assignmentRepository.findOne(grade.getIdTema()).getDeadlineWeek() + " ; " + "Feedback: " + grade.getFeedback() + "\n");
        writer.close();

    }

    public List<Student> filterStudentsByGrupa(String grupa) {
        return StreamSupport.stream(studentRepository.findAll().spliterator(), false).filter(x -> grupa.equals(x.getGrupa())).collect(Collectors.toList());
    }

    public List<Student> filterStudentsByTema(Integer idT) {
        return StreamSupport.stream(gradeRepository.findAll().spliterator(), false).filter(x -> idT == x.getIdTema()).map(x -> studentRepository.findOne(x.getIdStudent())).collect(Collectors.toList());
    }

    public List<Student> filterStudentsByTemaAndProfesor(Integer idT, String profesor) {
        return StreamSupport.stream(gradeRepository.findAll().spliterator(), false).filter(x -> idT == x.getIdTema()).filter(x -> profesor.equals(x.getProfesor())).map(x -> studentRepository.findOne(x.getIdStudent())).collect(Collectors.toList());
    }

    public List<Grade> filterNoteByTemaAndWeek(Integer idT, int week) {
        return StreamSupport.stream(gradeRepository.findAll().spliterator(), false).filter(x -> idT == x.getIdTema()).filter(x -> week == YearStructure.now().getSemestru(x.getData()).getWeek(x.getData())).collect(Collectors.toList());
    }

    private List<Observer<GradeChangeEvent>> observers = new ArrayList<>();

    @Override
    public void addObserver(Observer<GradeChangeEvent> e) {
        observers.add(e);
    }

    @Override
    public void removeObserver(Observer<GradeChangeEvent> e) {

    }

    @Override
    public void notifyObservers(GradeChangeEvent t) {
        observers.forEach(x -> x.update(t));
    }

    public Student findStudent(Integer id) {
        return studentRepository.findOne(id);
    }

    public Iterable<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    public Iterable<Assignment> findAllTeme() {
        return assignmentRepository.findAll();
    }

    public Assignment findTema(Integer id) {
        return assignmentRepository.findOne(id);
    }

    public Assignment getMatchingAssignmentForGivenDate(LocalDateTime date) {
        return StreamSupport.stream(findAllTeme().spliterator(), false).filter(x -> x.getDeadlineWeek() < YearStructure.now().getSemestru(date).getWeek(date)).collect(Collectors.toList()).get(0);
    }

    public int getPunctePenalizare(LocalDateTime dataPredare, Integer idTema) {
        int saptamanaPredare = YearStructure.now().getSemestru(dataPredare).getWeek(dataPredare);
        int deadline = assignmentRepository.findOne(idTema).getDeadlineWeek();
        int diff = deadline - saptamanaPredare;
        if (diff < 0)
            return diff;
        return 0;
    }

    public Student findStudentByNume(String nume) {
        return StreamSupport.stream(studentRepository.findAll().spliterator(), false).filter(x -> (x.getNume() + " " + x.getPrenume()).equals(nume)).collect(Collectors.toList()).get(0);
    }

    public float getStudentFinalGradeValue(Student student) {
        float up = 0;
        float down = 0;
        for (var grade : gradeRepository.findAll()) {
            if (student.getId() == grade.getIdStudent()) {
                Assignment assignment = assignmentRepository.findOne(grade.getIdTema());
                int pondere = assignment.getDeadlineWeek() - assignment.getStartWeek();
                up += grade.getValue() * pondere;
                down += pondere;
            }
        }
        if (up == 0) return 1;
        return up / down;
    }

    public float getAssignmentAverageResults(Assignment assignment) {
        float up = 0;
        int nr = 0;
        for (var grade : gradeRepository.findAll()) {
            if (grade.getIdTema() == assignment.getId()) {
                nr++;
                up += grade.getValue();
            }
        }
        return up / nr;
    }

    public Iterable<Student> getStudentsWithoutDelayedAssignments(){
        ArrayList<Student> list = new ArrayList<>();
        for(var student : studentRepository.findAll()){
            boolean ok = true;
            for(var grade : gradeRepository.findAll())
                if(grade.getIdStudent() == student.getId() && (YearStructure.now().getSemestru(grade.getData()).getWeek(grade.getData()) > assignmentRepository.findOne(grade.getIdTema()).getDeadlineWeek()))
                {
                    ok = false;
                    break;
                }
            if(ok)
                list.add(student);
        }
        return list;
    }

    public void saveReportsInPdf() {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("src/main/resources/pdfs/StatisticsReports.pdf"));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        String pdfcontext = "Note finale studenti:\n\n";
        try {
            document.add(new Paragraph(pdfcontext));
            for(var student : findAllStudents()) {
                document.add(new Paragraph(student.getNume() + " " + student.getPrenume() + " Nota:  " + String.valueOf(getStudentFinalGradeValue(student)) + "\n"));
            }
            document.add(new Paragraph("\n\n"));
            Assignment assignment = getAssignmentWithLowestGrades();
            document.add(new Paragraph("Cea mai grea tema: "));
            document.add(new Paragraph("Laborator-ul din saptamana " + String.valueOf(assignment.getStartWeek()) +"-"+String.valueOf(assignment.getDeadlineWeek()) + " media: " + String.valueOf(getAssignmentAverageResults(assignment)) + "\n\n\n"));
            document.add(new Paragraph("Studentii ce pot intra in examen:\n\n"));
            for(var student : findAllStudents())
                if(getStudentFinalGradeValue(student) > 4.0)
                    document.add(new Paragraph(student.getNume() + " " + student.getPrenume() + " Nota:  " + String.valueOf(getStudentFinalGradeValue(student)) + "\n"));
            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph("Stundentii fara intarzieri:\n\n"));
            for(var student : getStudentsWithoutDelayedAssignments())
                document.add(new Paragraph(student.getNume() + " " + student.getPrenume() + "\n"));
        }
        catch (DocumentException e) {
            e.printStackTrace();
        }
        document.close();
    }

    public Assignment getAssignmentWithLowestGrades() {
        Assignment assignment = null;
        float minVal = 11;
        for(var ass : findAllTeme())
            if(getAssignmentAverageResults(ass) < minVal)
            {
                assignment = ass;
                minVal = getAssignmentAverageResults(ass);
            }
        return assignment;
    }
}
