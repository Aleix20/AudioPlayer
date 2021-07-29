/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m06.uf1.grup06.utils;

/**
 *
 * @author Aleix
 */
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

public class ResultSetModel extends AbstractTableModel {

    private String tipoTabla;
    private final ArrayList<String> nomsColumnes;
    private final ArrayList<ArrayList> dadesFiles;

    public ResultSetModel(ArrayList<String> datos, String playListNombre) {
        super();
        nomsColumnes = new ArrayList<>();
        dadesFiles = new ArrayList<>();
        

        
            nomsColumnes.add(playListNombre);
        

        for (int i = 0; i < datos.size(); i++) {
            
            ArrayList<String> datosFila = new ArrayList<>();
            datosFila.add(datos.get(i));
            dadesFiles.add(datosFila);
        }
            

            

            
        
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        
            return false;
        
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        ArrayList aux = dadesFiles.get(row);
        aux.set(col,(String)value);
        fireTableCellUpdated(row, col);
    }

    @Override
    public String getColumnName(int i) {
        return formatTitle(nomsColumnes.get(i));
    }

    @Override
    public int getRowCount() {
        return dadesFiles.size();
    }

    @Override
    public int getColumnCount() {
        return nomsColumnes.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return dadesFiles.get(rowIndex).get(columnIndex);
    }
    
    private String formatTitle(String title) {
        String formatedTitle = String.valueOf(title.charAt(0)).toUpperCase();
        
        for(int i = 1; i < title.length(); i++) {
            if (Character.isUpperCase(title.charAt(i))) {
                formatedTitle += " " + title.charAt(i);
            } else {
                formatedTitle += title.charAt(i);
            }
        }
        return formatedTitle;
    }   
}
