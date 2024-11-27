/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package frames;

import datos.AlbumDAO;
import datos.ArtistaDAO;
import datos.DetallesCancionDAO;
import excepciones.PersistenciaException;
import interfaces.IAlbumDAO;
import interfaces.IArtistaDAO;
import interfaces.IDetallesCancionDAO;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import objetos.Albumes;
import objetos.Artistas;
import objetos.DetallesCancion;
import recursos.GestorImagenesMongo;
import recursos.Imagen;

/**
 *
 * @author Chris
 */
public class FrmInicio extends javax.swing.JFrame {

    private IArtistaDAO adao;
    private IAlbumDAO albumdao;
    private IDetallesCancionDAO candao;

    /**
     * Creates new form FrmHome
     */
    public FrmInicio() {
        initComponents();
        this.SetImageLabel(jLabel1, "C:/Users/Chris/Documents/GitHub/Proyecto3BDA_BibliotecaMusical/BibliotecaMusical_presentacion/images/logo.png");
        this.adao = new ArtistaDAO();
        this.albumdao = new AlbumDAO();
        this.candao = new DetallesCancionDAO();

        this.obtieneArtistas();
        this.obtieneAlbumes();
        this.obtieneCanciones();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        panelCanciones = new javax.swing.JPanel();
        panelAlbumes = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        panelArtistas = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout panelCancionesLayout = new javax.swing.GroupLayout(panelCanciones);
        panelCanciones.setLayout(panelCancionesLayout);
        panelCancionesLayout.setHorizontalGroup(
            panelCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelCancionesLayout.setVerticalGroup(
            panelCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 160, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelAlbumesLayout = new javax.swing.GroupLayout(panelAlbumes);
        panelAlbumes.setLayout(panelAlbumesLayout);
        panelAlbumesLayout.setHorizontalGroup(
            panelAlbumesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelAlbumesLayout.setVerticalGroup(
            panelAlbumesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 160, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setForeground(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(102, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(67, 67, 67)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelArtistasLayout = new javax.swing.GroupLayout(panelArtistas);
        panelArtistas.setLayout(panelArtistasLayout);
        panelArtistasLayout.setHorizontalGroup(
            panelArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 316, Short.MAX_VALUE)
        );
        panelArtistasLayout.setVerticalGroup(
            panelArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 160, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(89, 89, 89)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelArtistas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelAlbumes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelCanciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(595, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(132, 132, 132)
                .addComponent(panelArtistas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(panelAlbumes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(panelCanciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(101, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SetImageLabel(JLabel labelname, String root) {
        ImageIcon image = new ImageIcon(root);
        Icon icon = new ImageIcon(image.getImage().getScaledInstance(labelname.getWidth(), labelname.getHeight(), Image.SCALE_DEFAULT));
        labelname.setIcon(icon);
        this.repaint();
    }

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
            java.util.logging.Logger.getLogger(FrmInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmInicio.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmInicio().setVisible(true);
            }
        });
    }

    private void obtieneArtistas() {
        try {
            List<Artistas> artistas = this.adao.obtenerSeisArtistas();

            panelArtistas.setLayout(new GridLayout(1, artistas.size(), 20, 0));
            panelArtistas.setBackground(new Color(18, 18, 18));

            for (Artistas artista : artistas) {
                JPanel panelArtista = creaPanel(artista.getNombre(), artista.getImagen());
                panelArtistas.add(panelArtista);
            }
            panelArtistas.revalidate();
            panelArtistas.repaint();
        } catch (PersistenciaException e) {
            e.printStackTrace();
        }
    }

    private void obtieneAlbumes() {
        try {
            List<Albumes> albumes = this.albumdao.obtenerSeisAlbumes();

            panelAlbumes.setLayout(new GridLayout(1, albumes.size(), 20, 0));
            panelAlbumes.setBackground(new Color(18, 18, 18));

            for (Albumes album : albumes) {
                JPanel panelAlbum = creaPanel(album.getNombre(), album.getImagenPortada());
                panelAlbumes.add(panelAlbum);
            }
            panelAlbumes.revalidate();
            panelAlbumes.repaint();
        } catch (PersistenciaException e) {
            e.printStackTrace();
        }
    }

    private void obtieneCanciones() {
        try {
            List<DetallesCancion> canciones = this.albumdao.obtenerCancionesDeAlbumes();

            panelCanciones.setLayout(new GridLayout(1, canciones.size(), 20, 0));
            panelCanciones.setBackground(new Color(18, 18, 18));

            for (DetallesCancion cancion : canciones) {
                JPanel panelArtista = creaPanel(cancion.getTitulo(), cancion.getFotoAlbum());
                panelCanciones.add(panelArtista);
            }
            panelCanciones.revalidate();
            panelCanciones.repaint();
        } catch (PersistenciaException e) {
            e.printStackTrace();
        }
    }

    private JPanel creaPanel(String nombre, Imagen imagen) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(18, 18, 18));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnFoto = new JButton(); // Cambiado a JButton
        btnFoto.setPreferredSize(new Dimension(130, 130));
        btnFoto.setOpaque(true);
        btnFoto.setContentAreaFilled(false); // Elimina el fondo por defecto
        btnFoto.setBorder(new LineBorder(new Color(18, 18, 18), 5, true));
        btnFoto.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Usa GestorImagenesMongo para convertir la imagen
        if (imagen != null) {
            ImageIcon icon = GestorImagenesMongo.getImageIcon(imagen, GestorImagenesMongo.SizeImage.MEDIUM);
            if (icon != null) {
                btnFoto.setIcon(icon); // Asigna la imagen como icono
            } else {
                btnFoto.setBackground(Color.RED); // Indicador de error en la imagen
            }
        } else {
            btnFoto.setBackground(Color.BLUE); // Fondo si no hay imagen
        }

        // Añadir un ActionListener al botón para manejar clics
        btnFoto.addActionListener(e -> {
        });

        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 14));
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(btnFoto);
        panel.add(Box.createVerticalStrut(10)); // Espaciado
        panel.add(lblNombre);

        return panel;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel panelAlbumes;
    private javax.swing.JPanel panelArtistas;
    private javax.swing.JPanel panelCanciones;
    // End of variables declaration//GEN-END:variables
}
