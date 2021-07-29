package m06.uf1.p1.grup6.model;

import java.util.ArrayList;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BibliotecaMusical {
    private ArrayList<Cancion> canciones;
    private ArrayList<Playlist> playlists;
    
    public BibliotecaMusical() {
        this.canciones = new ArrayList<>();
        this.playlists = new ArrayList<>();
    }
    
    public void addCancion(Cancion cancion) {
        this.canciones.add(cancion);
    }
    
    public void addPlaylist(Playlist playlist) {
        this.playlists.add(playlist);
    }
    
    public Playlist getPlayList(String name) {
        for (Playlist pl : playlists) {
            if(pl.getNombre().equals(name)) {
                return pl;
            }
        }
        return null;
    } 

    public Cancion getSongSource(String songName) {
        for (Cancion cancion : canciones) {
            if(cancion.getNombre().equals(songName)) {
                return cancion;
            }
        }
        return null;
    }

    @XmlElementWrapper(name="Canciones")
    @XmlElement(name = "Cancion")
    public ArrayList<Cancion> getCanciones() {
        return canciones;
    }
    
    @XmlElementWrapper(name="Playlists")
    @XmlElement(name = "Playlist")
    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }
}
