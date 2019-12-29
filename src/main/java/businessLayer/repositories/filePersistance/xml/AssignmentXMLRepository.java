package main.java.businessLayer.repositories.filePersistance.xml;

import main.java.businessLayer.domain.Assignment;
import main.java.businessLayer.domain.YearStructure;
import main.java.businessLayer.repositories.CrudRepository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AssignmentXMLRepository extends XMLRepository<Integer, Assignment> {
    public AssignmentXMLRepository(CrudRepository<Integer, Assignment> crudRepository, String filepath) {
        super(crudRepository, filepath);
    }

    @Override
    protected Element createElementfromEntity(Document document, Assignment assignment) {
        Element element = document.createElement("student");
        element.setAttribute("id", assignment.getId().toString());
        element.setAttribute("descriere", assignment.getDescriere());
        element.setAttribute("startWeek", String.valueOf(assignment.getStartWeek()));
        element.setAttribute("deadlineWeek", String.valueOf(assignment.getDeadlineWeek()));
        element.setAttribute("startDate", assignment.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        return element;
    }

    @Override
    protected Assignment createEntityFromElement(Element temaElement) {
        String id = temaElement.getAttribute("id");
        String descriere = temaElement.getAttribute("descriere");
        String deadlineWeek = temaElement.getAttribute("deadlineWeek");
        String startDate = temaElement.getAttribute("startDate");
        Assignment s= new Assignment(Integer.parseInt(id), descriere, Integer.parseInt(deadlineWeek), LocalDateTime.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), YearStructure.now());
        return s;
    }
}
