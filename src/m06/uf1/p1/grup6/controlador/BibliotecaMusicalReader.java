package m06.uf1.p1.grup6.controlador;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import m06.uf1.p1.grup6.model.BibliotecaMusical;

public class BibliotecaMusicalReader {

    private static final String FILE_PATH = "BibliotecaMusical.xml";

    public static BibliotecaMusical getDefault() {
        try {
            File fitxer = new File(FILE_PATH);
            JAXBContext context = JAXBContext.newInstance(BibliotecaMusical.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            
            BibliotecaMusical biblioteca = (BibliotecaMusical) unmarshaller.unmarshal(fitxer);

            return biblioteca;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }
}
