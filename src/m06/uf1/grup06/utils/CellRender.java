/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m06.uf1.grup06.utils;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class CellRender extends DefaultTableCellRenderer {

    private final String tipo;
    private final Font normal = new Font("Arial", Font.PLAIN, 14);
    private final Font bold = new Font("Arial", Font.BOLD, 14);

    public CellRender() {
        this.tipo = "default";
    }

    public CellRender(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focused, int row, int column) {

        this.setText((String) value);
        this.setForeground(new Color(0, 0, 0));
        this.setHorizontalAlignment(JLabel.CENTER);
        this.setFont(normal);

        if (selected) {
            this.setBackground(new Color(244, 244, 209));
            this.setFont(bold);
        } else {
            if (row % 2 == 0) {
                this.setBackground(Color.WHITE);

            } else {
                this.setBackground(new Color(204, 255, 255));
            }
        }

        if (tipo.equals("Header")) {
            this.setText(((String) value).toUpperCase());
            this.setFont(bold);
            this.setBackground(new Color(0, 128, 128));
            this.setForeground(Color.WHITE);
            return this;
        }

        return this;
    }
}