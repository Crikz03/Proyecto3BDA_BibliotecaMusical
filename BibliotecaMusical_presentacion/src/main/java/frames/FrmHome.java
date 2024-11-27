/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package frames;

import datos.ArtistaDAO;
import excepciones.PersistenciaException;
import interfaces.IArtistaDAO;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import objetos.Artistas;
import recursos.GestorImagenesMongo;
import recursos.Imagen;

/**
 *
 * @author Chris
 */
public class FrmHome extends javax.swing.JFrame {

    private IArtistaDAO adao;

    /**
     * Creates new form FrmHome
     */
    public FrmHome() {
        initComponents();
        this.adao = new ArtistaDAO();

        this.obtieneArtistas();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelArtistas = new javax.swing.JPanel();
        lblFotoArtis = new javax.swing.JPanel();
        lblNombreArtistaj = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblFotoArtis.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        javax.swing.GroupLayout lblFotoArtisLayout = new javax.swing.GroupLayout(lblFotoArtis);
        lblFotoArtis.setLayout(lblFotoArtisLayout);
        lblFotoArtisLayout.setHorizontalGroup(
            lblFotoArtisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        lblFotoArtisLayout.setVerticalGroup(
            lblFotoArtisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        lblNombreArtistaj.setText("aaa");

        javax.swing.GroupLayout panelArtistasLayout = new javax.swing.GroupLayout(panelArtistas);
        panelArtistas.setLayout(panelArtistasLayout);
        panelArtistasLayout.setHorizontalGroup(
            panelArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelArtistasLayout.createSequentialGroup()
                .addGroup(panelArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelArtistasLayout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(lblFotoArtis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelArtistasLayout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(lblNombreArtistaj)))
                .addContainerGap(1006, Short.MAX_VALUE))
        );
        panelArtistasLayout.setVerticalGroup(
            panelArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelArtistasLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(lblFotoArtis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNombreArtistaj)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(272, 272, 272)
                .addComponent(panelArtistas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(157, 157, 157)
                .addComponent(panelArtistas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(635, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmHome().setVisible(true);
            }
        });
    }

    private void obtieneArtistas() {
        try {

            List<Artistas> artistas = this.adao.obtenerSeisArtistas();
            System.out.println("Número de artistas: " + artistas.size());
            for (Artistas artista : artistas) {
                System.out.println("Nombre del artista: " + artista.getNombre());
            }

            JPanel panelArtistas1 = new JPanel();
            panelArtistas1.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
            panelArtistas.setBackground(new Color(18, 18, 18));

            for (Artistas artista : artistas) {

                this.creaPanel(panelArtistas, artista.getNombre(), artista.getImagen());

            }

        } catch (PersistenciaException e) {
            e.printStackTrace();
        }
    }

    private JPanel creaPanel(JPanel panel, String artistName, Imagen imagen) {
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(18, 18, 18));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblFotoArti = new JLabel();
        lblFotoArti.setPreferredSize(new Dimension(80, 80));
        lblFotoArti.setOpaque(true);
        lblFotoArti.setBackground(Color.DARK_GRAY); // Fondo por defecto
        lblFotoArti.setBorder(new LineBorder(new Color(18, 18, 18), 5, true));
        lblFotoArti.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Usa GestorImagenesMongo para convertir la imagen
        if (imagen != null) {
            ImageIcon icon = GestorImagenesMongo.getImageIcon(imagen, GestorImagenesMongo.SizeImage.SMALL);
            if (icon != null) {
                lblFotoArti.setIcon(icon);
            } else {
                lblFotoArti.setBackground(Color.RED); // Indicador de error en la imagen
            }
        } else {
            lblFotoArti.setBackground(Color.BLUE);  // Fondo si no hay imagen
        }

        JLabel lblNombreArtista = new JLabel(artistName);
        lblNombreArtista.setFont(new Font("Arial", Font.PLAIN, 14));
        lblNombreArtista.setForeground(Color.WHITE);
        lblNombreArtista.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblFotoArti);
        panel.add(Box.createVerticalStrut(10)); // Espaciado
        panel.add(lblNombreArtista);

        return panel;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel lblFotoArtis;
    private javax.swing.JLabel lblNombreArtistaj;
    private javax.swing.JPanel panelArtistas;
    // End of variables declaration//GEN-END:variables
}
