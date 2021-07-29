package m06.uf1.p1.grup6.model;

import java.io.File;
import java.io.FileReader;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

@XmlRootElement
public class Playlist {

    private String nombre;
    private String ruta;

    public Playlist() {
    }

    @XmlElement(name = "Nom")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @XmlElement(name = "RutaPlaylist")
    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public JSONObject getReferences() {
        JSONObject referencias = null;
        JSONParser parser = new JSONParser();
        try {
            File fichero = new File(ruta);

            if (fichero.exists()) {
                referencias = (JSONObject) parser.parse(new FileReader(ruta));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return referencias;
    }
}
