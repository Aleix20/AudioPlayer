package m06.uf1.p1.grup6.controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javazoom.jlgui.basicplayer.BasicController;
import javazoom.jlgui.basicplayer.BasicPlayerEvent;
import javazoom.jlgui.basicplayer.BasicPlayerException;
import javazoom.jlgui.basicplayer.BasicPlayerListener;
import m06.uf1.grup06.utils.CellRender;
import m06.uf1.grup06.utils.ResultSetModel;
import m06.uf1.p1.grup6.model.Audio;
import m06.uf1.p1.grup6.model.BibliotecaMusical;
import m06.uf1.p1.grup6.model.Cancion;
import m06.uf1.p1.grup6.model.Playlist;
import m06.uf1.p1.grup6.vista.Vista;

public class Controlador implements ActionListener {

    private Vista vista;
    private BibliotecaMusical bibliotecaMusical;
    private Audio currentAudio;
    private ArrayList<String> nombreCanciones;
    private Boolean isPlaying = false;

    public Controlador() {
        vista = new Vista();
        nombreCanciones = new ArrayList<>();
        bibliotecaMusical = BibliotecaMusicalReader.getDefault();
        onClickTable();
        afegirListenerBotons();
        actualizarComboBox();
        onClickComboBox();
        actualitzaTaula(vista.getTblList(), new DefaultTableModel());
        vista.setVisible(true);
        getTodasLasCanciones();
    }

    public void afegirListenerBotons() {
        vista.getBtnPlay().addActionListener(this);
        vista.getBtnStop().addActionListener(this);
        vista.getBtnAnterior().addActionListener(this);
        vista.getBtnSiguiente().addActionListener(this);
    }

    public void actionPerformed(ActionEvent esdeveniment) {
        Object gestorEsdeveniments = esdeveniment.getSource();
        try {
            if (gestorEsdeveniments.equals(vista.getBtnPlay())) {
                if (isPlaying) {
                    currentAudio.getPlayer().pause();
                    vista.getBtnPlay().setText("Play");
                    vista.getBtnPlay().setIcon(new ImageIcon("AlbumArt/PlayIcon.png"));
                    isPlaying = false;
                } else {
                    if (currentAudio == null) {
                        if (vista.getTblList().getSelectionModel().isSelectionEmpty()) {
                            vista.getTblList().setRowSelectionInterval(0, 0);
                        }
                        playSelected();
                    } else {
                        currentAudio.getPlayer().resume();
                    }

                    vista.getBtnPlay().setText("Pause");
                    vista.getBtnPlay().setIcon(new ImageIcon("AlbumArt/PauseIcon.png"));
                    isPlaying = true;
                }

            } else if (gestorEsdeveniments.equals(vista.getBtnStop())) {
                if (currentAudio != null) {
                    currentAudio.getPlayer().stop();
                    isPlaying = false;
                    currentAudio = null;
                    vista.getBtnPlay().setText("Play");
                    vista.getBtnPlay().setIcon(new ImageIcon("AlbumArt/PlayIcon.png"));
                }

            } else if (gestorEsdeveniments.equals(vista.getBtnSiguiente())) {
                nextSong();
            } else if (gestorEsdeveniments.equals(vista.getBtnAnterior())) {
                backwardSong();
            }

        } catch (BasicPlayerException e) {
            e.printStackTrace();
        }
    }

    public void onClickTable() {
        vista.getTblList().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                try {
                    int row = vista.getTblList().rowAtPoint(evt.getPoint());
                    int col = 0;

                    String cancion = (String) vista.getTblList().getValueAt(row, col);//Nombre Cancion
                    for (int i = 0; i < bibliotecaMusical.getCanciones().size(); i++) {
                        if (bibliotecaMusical.getCanciones().get(i).getNombre().equals(cancion)) {
                            Cancion c = bibliotecaMusical.getSongSource(cancion);
                            playCancion(c);
                        }
                    }
                } catch (BasicPlayerException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void playCancion(Cancion cancion) throws BasicPlayerException {
        if (currentAudio != null) {
            currentAudio.getPlayer().stop();
        }
        currentAudio = cancion.getAudio();
        vista.getLblCancion().setText(cancion.getNombre());
        vista.getLblAutor().setText(cancion.getAutor());
        vista.getLblFinal().setText(String.format("00:%02d", cancion.getDuracion()));
        vista.getLblInicio().setText("00:00");
        vista.getPbTiempo().setMaximum(cancion.getDuracion());
        vista.getPbTiempo().setValue(0);
        setPlayerLisener(currentAudio);
        currentAudio.getPlayer().play();
        vista.getBtnPlay().setText("Pause");
        vista.getBtnPlay().setIcon(new ImageIcon("AlbumArt/PauseIcon.png"));
        isPlaying = true;
    }

    private void actualitzaTaula(JTable taula, TableModel model) {
        taula.getTableHeader().setDefaultRenderer(new CellRender("Header"));
        taula.setModel(model);
        CellRender renderizador = new CellRender();
        for (int i = 0; i < taula.getColumnCount(); i++) {
            taula.getColumnModel().getColumn(i).setCellRenderer(renderizador);
        }
    }

    private void actualizarComboBox() {
        vista.getCbPlaylists().removeAllItems();
        vista.getCbPlaylists().addItem("Todas las canciones");
        vista.getLblImage().setIcon(new ImageIcon("AlbumArt/AppIcon.png"));
        for (int i = 0; i < bibliotecaMusical.getPlaylists().size(); i++) {
            vista.getCbPlaylists().addItem(bibliotecaMusical.getPlaylists().get(i).getNombre());
        }
    }

    private void onClickComboBox() {
        vista.getCbPlaylists().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String index = (String) vista.getCbPlaylists().getSelectedItem();

                if (currentAudio != null) {
                    try {
                        currentAudio.getPlayer().stop();
                        currentAudio = null;
                        vista.getBtnPlay().setText("Play");
                        vista.getBtnPlay().setIcon(new ImageIcon("AlbumArt/PlayIcon.png"));
                    } catch (BasicPlayerException ex) {
                        ex.printStackTrace();
                    }
                }
                isPlaying = false;
                if (index.equals("Todas las canciones")) {
                    getTodasLasCanciones();
                } else {
                    getCancionesPlayList(index);
                }
            }
        });
    }

    private void getTodasLasCanciones() {
        nombreCanciones.clear();
        for (int i = 0; i < bibliotecaMusical.getCanciones().size(); i++) {
            String nombre = bibliotecaMusical.getCanciones().get(i).getNombre();
            nombreCanciones.add(nombre);
        }
        actualitzaTaula(vista.getTblList(), new ResultSetModel(nombreCanciones, "Todas las canciones"));
        vista.getLblImage().setIcon(new ImageIcon("AlbumArt/AppIcon.png"));
    }

    private void getCancionesPlayList(String nombrePlayList) {
        Playlist playlist = bibliotecaMusical.getPlayList(nombrePlayList);

        if (playlist != null) {
            ArrayList<String> cancionesPlayList = (ArrayList<String>) playlist.getReferences().get("Canciones");
            actualitzaTaula(vista.getTblList(), new ResultSetModel(cancionesPlayList, nombrePlayList));
            String imgRef = (String) playlist.getReferences().get("Imagen");
            vista.getLblImage().setIcon(new ImageIcon(imgRef));
        }
    }

    public void setPlayerLisener(Audio audio) {
        audio.getPlayer().addBasicPlayerListener(new BasicPlayerListener() {
            @Override
            public void opened(Object o, Map map) {
            }

            @Override
            public void progress(int i, long l, byte[] bytes, Map map) {
                String microSeg = map.get("mp3.position.microseconds").toString();
                int seg = Integer.parseInt(microSeg) / 500000;
                vista.getLblInicio().setText(String.format("00:%02d", seg));
                vista.getPbTiempo().setValue(seg);
            }

            @Override
            public void stateUpdated(BasicPlayerEvent bpe) {
                switch (bpe.getCode()) {
                    case 8: //FIN CANCION
                        nextSong();
                        break;
                    default:
                }
            }

            @Override
            public void setController(BasicController bc) {
            }
        });
    }

    private void nextSong() {
        if (!vista.getTblList().getSelectionModel().isSelectionEmpty()) {
            int selected = vista.getTblList().getSelectedRow();

            if ((selected + 1) >= vista.getTblList().getRowCount()) {
                vista.getTblList().setRowSelectionInterval(0, 0);
            } else {
                vista.getTblList().setRowSelectionInterval(selected + 1, selected + 1);
            }
            playSelected();
        }
    }

    private void backwardSong() {
        if (!vista.getTblList().getSelectionModel().isSelectionEmpty()) {
            int selected = vista.getTblList().getSelectedRow();
            System.out.println(selected);

            if ((selected - 1) < 0) {
                vista.getTblList().setRowSelectionInterval(vista.getTblList().getRowCount() - 1, vista.getTblList().getRowCount() - 1);
            } else {
                vista.getTblList().setRowSelectionInterval(selected - 1, selected - 1);
            }
            playSelected();
        }
    }

    private void playSelected() {
        if (!vista.getTblList().getSelectionModel().isSelectionEmpty()) {
            try {
                String songText = (String) vista.getTblList().getValueAt(vista.getTblList().getSelectedRow(), 0);
                Cancion cancion = bibliotecaMusical.getSongSource(songText);
                playCancion(cancion);
            } catch (BasicPlayerException ex) {
                ex.printStackTrace();
            }
        }
    }
}
