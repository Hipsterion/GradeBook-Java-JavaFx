package main.java.businessLayer.repositories.filePersistance.xml;

import main.java.businessLayer.domain.Student;
import main.java.businessLayer.repositories.CrudRepository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class StudentXMLRepository extends XMLRepository<Integer, Student> {
    public StudentXMLRepository(CrudRepository<Integer, Student> crudRepository, String filepath) {
        super(crudRepository, filepath);
    }

    @Override
    protected Element createElementfromEntity(Document document, Student student) {
        Element element = document.createElement("student");
        element.setAttribute("id", student.getId().toString());
        element.setAttribute("nume", student.getNume());
        element.setAttribute("prenume", student.getPrenume());
        element.setAttribute("grupa", student.getGrupa());
        element.setAttribute("email", student.getEmail());
        element.setAttribute("profesor", student.getProfesor());
        return element;
    }

    @Override
    protected Student createEntityFromElement(Element studentElement) {
        String id = studentElement.getAttribute("id");
        String nume = studentElement.getAttribute("nume");
        String prenume = studentElement.getAttribute("prenume");
        String email = studentElement.getAttribute("email");
        String profesor = studentElement.getAttribute("profesor");
        String grupa = studentElement.getAttribute("grupa");
        Student s= new Student(Integer.parseInt(id), nume, prenume, grupa, email, profesor);
        return s;
    }
}
