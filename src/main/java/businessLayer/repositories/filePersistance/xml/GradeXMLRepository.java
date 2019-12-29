package main.java.businessLayer.repositories.filePersistance.xml;

import main.java.businessLayer.domain.Grade;
import main.java.businessLayer.repositories.CrudRepository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GradeXMLRepository extends XMLRepository<String, Grade> {
    public GradeXMLRepository(CrudRepository<String, Grade> crudRepository, String filepath) {
        super(crudRepository, filepath);
    }

    @Override
    protected Element createElementfromEntity(Document document, Grade grade) {
        Element element = document.createElement("nota");
        element.setAttribute("id", grade.getId());
        element.setAttribute("idSt", grade.getIdStudent().toString());
        element.setAttribute("idTema", grade.getIdTema().toString());
        element.setAttribute("data", grade.getData().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        element.setAttribute("value", String.valueOf(grade.getValue()));
        element.setAttribute("profesor", grade.getProfesor());
        element.setAttribute("feedback", grade.getFeedback());
        return element;
    }

    @Override
    protected Grade createEntityFromElement(Element notaElement) {
        String idSt = notaElement.getAttribute("idSt");
        String idTema = notaElement.getAttribute("idTema");
        String profesor = notaElement.getAttribute("profesor");
        String feedback = notaElement.getAttribute("feedback");
        String data = notaElement.getAttribute("data");
        String value = notaElement.getAttribute("value");
        Grade n = new Grade(Integer.parseInt(idSt), Integer.parseInt(idTema), LocalDateTime.parse(data, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")), profesor, Integer.parseInt(value), feedback);
        return n;
    }
}
