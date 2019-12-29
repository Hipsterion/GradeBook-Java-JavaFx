package main.java.businessLayer.repositories.filePersistance.xml;

import main.java.businessLayer.domain.Entity;
import main.java.businessLayer.repositories.CrudRepository;
import main.java.businessLayer.repositories.filePersistance.AbstractFileRepository;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class XMLRepository<ID, E extends Entity<ID>> extends AbstractFileRepository<ID,E> {
    public XMLRepository(CrudRepository<ID, E> crudRepository, String filepath) {
        super(crudRepository, filepath);
    }

    @Override
    protected void load() {
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(this.filepath);

            Element root = document.getDocumentElement();
            NodeList children = root.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node entityElement = children.item(i);
                if (entityElement instanceof Element) {
                    E entity = createEntityFromElement((Element) entityElement);
                    super.save(entity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void saveChanges() {
        try {
            Document document = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .newDocument();
            Pattern pattern = Pattern.compile(".*[/]([a-zA-Z0-9]+)[.][a-zA-Z0-9]+$");
            Matcher matcher = pattern.matcher(filepath);
            matcher.matches();
            Element root = document.createElement(matcher.group(1));
            document.appendChild(root);
            super.findAll().forEach(s -> {
                Element e = createElementfromEntity(document, s);
                root.appendChild(e);
            });

            //write Document to file
            Transformer transformer = TransformerFactory.
                    newInstance().newTransformer();

            Source source=new DOMSource(document);

            transformer.transform(source,
                    new StreamResult(filepath));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract Element createElementfromEntity(Document document, E entity);


    protected abstract E createEntityFromElement(Element entityElement);
}
