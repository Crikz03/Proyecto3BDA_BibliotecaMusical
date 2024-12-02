/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package frames;

import dto.DetallesCancionDTO;
import dto.UsuarioDTO;
import excepciones.NegocioException;
import interfaces.IAlbumBO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import negocio.AlbumBO;
import recursos.Forms;
import recursos.GestorImagenesMongo;

/**
 *
 * @author Chris
 */
public class FrmPestañaCanciones extends javax.swing.JFrame {

    private IAlbumBO albumbo;
    private UsuarioDTO usuarioLoggeado;

    /**
     * Creates new form FrmPestañaCanciones
     */
    public FrmPestañaCanciones(UsuarioDTO usuarioLoggeado) {
        initComponents();
        this.albumbo = new AlbumBO();
        this.usuarioLoggeado = usuarioLoggeado;

        this.configuraFrame();
    }

    private void configuraFrame() {
        setSize(1830, 1000);
        this.SetImageLabel(jLabel1, "images/logo.png");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.obtieneCanciones();
        this.cargarDatosUsuario();
    }

    private void SetImageLabel(JLabel labelname, String root) {
        ImageIcon image = new ImageIcon(root);
        Icon icon = new ImageIcon(image.getImage().getScaledInstance(labelname.getWidth(), labelname.getHeight(), Image.SCALE_DEFAULT));
        labelname.setIcon(icon);
        this.repaint();
    }

    private void cargarDatosUsuario() {
        if (usuarioLoggeado != null) {
            lblNameUser.setText(usuarioLoggeado.getNombreUsuario());
            ImageIcon imageIcon
                    = GestorImagenesMongo
                            .getImageIcon(
                                    usuarioLoggeado.getFotoPerfil(),
                                    GestorImagenesMongo.SizeImage.SMALL
                            );
            lblFoto.setIcon(imageIcon);
        }
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
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        bHome = new javax.swing.JButton();
        bAlbumes = new javax.swing.JButton();
        bAlbumes1 = new javax.swing.JButton();
        bAlbumes2 = new javax.swing.JButton();
        bAlbumes3 = new javax.swing.JButton();
        bAlbumes4 = new javax.swing.JButton();
        bCerrarSesion = new javax.swing.JButton();
        panelCanciones = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        lblNameUser = new javax.swing.JLabel();
        lblFoto = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 153)));
        jPanel2.setForeground(new java.awt.Color(0, 0, 0));

        jLabel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 153)));

        bHome.setBackground(new java.awt.Color(0, 0, 0));
        bHome.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        bHome.setForeground(new java.awt.Color(255, 255, 255));
        bHome.setText("Home");
        bHome.setContentAreaFilled(false);
        bHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bHomeActionPerformed(evt);
            }
        });

        bAlbumes.setBackground(new java.awt.Color(0, 0, 0));
        bAlbumes.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        bAlbumes.setForeground(new java.awt.Color(255, 255, 255));
        bAlbumes.setText("Albumes");
        bAlbumes.setContentAreaFilled(false);
        bAlbumes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAlbumesActionPerformed(evt);
            }
        });

        bAlbumes1.setBackground(new java.awt.Color(0, 0, 0));
        bAlbumes1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        bAlbumes1.setForeground(new java.awt.Color(255, 255, 255));
        bAlbumes1.setText("Artistas");
        bAlbumes1.setContentAreaFilled(false);
        bAlbumes1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAlbumes1ActionPerformed(evt);
            }
        });

        bAlbumes2.setBackground(new java.awt.Color(0, 0, 0));
        bAlbumes2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        bAlbumes2.setForeground(new java.awt.Color(255, 255, 255));
        bAlbumes2.setText("Favoritos");
        bAlbumes2.setContentAreaFilled(false);
        bAlbumes2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAlbumes2ActionPerformed(evt);
            }
        });

        bAlbumes3.setBackground(new java.awt.Color(0, 0, 0));
        bAlbumes3.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        bAlbumes3.setForeground(new java.awt.Color(255, 255, 255));
        bAlbumes3.setText("Canciones");
        bAlbumes3.setContentAreaFilled(false);
        bAlbumes3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAlbumes3ActionPerformed(evt);
            }
        });

        bAlbumes4.setBackground(new java.awt.Color(0, 0, 0));
        bAlbumes4.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        bAlbumes4.setForeground(new java.awt.Color(255, 255, 255));
        bAlbumes4.setText("Perfil de usuario");
        bAlbumes4.setContentAreaFilled(false);
        bAlbumes4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAlbumes4ActionPerformed(evt);
            }
        });

        bCerrarSesion.setBackground(new java.awt.Color(0, 0, 0));
        bCerrarSesion.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        bCerrarSesion.setForeground(new java.awt.Color(204, 0, 204));
        bCerrarSesion.setText("Cerrar Sesion");
        bCerrarSesion.setContentAreaFilled(false);
        bCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCerrarSesionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(bAlbumes3, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bAlbumes2, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bAlbumes1, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bAlbumes, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bHome, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(bCerrarSesion)))
                .addContainerGap(21, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bAlbumes4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(69, 69, 69)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(bHome)
                .addGap(18, 18, 18)
                .addComponent(bAlbumes)
                .addGap(18, 18, 18)
                .addComponent(bAlbumes1)
                .addGap(18, 18, 18)
                .addComponent(bAlbumes2)
                .addGap(18, 18, 18)
                .addComponent(bAlbumes3)
                .addGap(18, 18, 18)
                .addComponent(bAlbumes4)
                .addGap(18, 18, 18)
                .addComponent(bCerrarSesion)
                .addContainerGap(338, Short.MAX_VALUE))
        );

        panelCanciones.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 153)));

        javax.swing.GroupLayout panelCancionesLayout = new javax.swing.GroupLayout(panelCanciones);
        panelCanciones.setLayout(panelCancionesLayout);
        panelCancionesLayout.setHorizontalGroup(
            panelCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelCancionesLayout.setVerticalGroup(
            panelCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 523, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 153)));

        lblNameUser.setBackground(new java.awt.Color(0, 0, 0));
        lblNameUser.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        lblNameUser.setForeground(new java.awt.Color(255, 255, 255));
        lblNameUser.setText("name");

        lblFoto.setText("jLabel5");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblFoto, javax.swing.GroupLayout.DEFAULT_SIZE, 116, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNameUser, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNameUser))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 733, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(panelCanciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelCanciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
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

    private void bHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bHomeActionPerformed
       Forms.cargarForm(new FrmInicio(usuarioLoggeado), this);
    }//GEN-LAST:event_bHomeActionPerformed

    private void bAlbumesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAlbumesActionPerformed
       Forms.cargarForm(new FrmPestañaAlbumes(usuarioLoggeado), this);
    }//GEN-LAST:event_bAlbumesActionPerformed

    private void bAlbumes1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAlbumes1ActionPerformed
        Forms.cargarForm(new FrmPestañaArtistas(usuarioLoggeado), this);
    }//GEN-LAST:event_bAlbumes1ActionPerformed

    private void bAlbumes2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAlbumes2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bAlbumes2ActionPerformed

    private void bAlbumes3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAlbumes3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bAlbumes3ActionPerformed

    private void bAlbumes4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAlbumes4ActionPerformed
        Forms.cargarForm(new FrmPerfil(usuarioLoggeado), this);
    }//GEN-LAST:event_bAlbumes4ActionPerformed

    private void bCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCerrarSesionActionPerformed
        Forms.cargarForm(new FrmInicioSesion(), this);
    }//GEN-LAST:event_bCerrarSesionActionPerformed

    private void obtieneCanciones() {
        try {
            List<DetallesCancionDTO> canciones = this.albumbo.obtenerCancionesDeAlbumes();

            Set<DetallesCancionDTO> cancionesUnicas = new HashSet<>(canciones);
            List<DetallesCancionDTO> cancionesSinDuplicados = new ArrayList<>(cancionesUnicas);
            Collections.shuffle(cancionesSinDuplicados);

            List<DetallesCancionDTO> cancionesAMostrar = cancionesSinDuplicados.subList(0, Math.min(9, cancionesSinDuplicados.size()));

            panelCanciones.setLayout(new GridLayout(0, 1, 0, 10));
            panelCanciones.setBackground(new Color(18, 18, 18));

            for (DetallesCancionDTO cancion : cancionesAMostrar) {
                JPanel panelCancion = creaPanelCancion(cancion);
                panelCanciones.add(panelCancion);
            }

            panelCanciones.revalidate();
            panelCanciones.repaint();
        } catch (NegocioException e) {
            e.printStackTrace();
        }
    }

    private JPanel creaPanelCancion(DetallesCancionDTO cancion) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(24, 24, 24));
        panel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY)); // Línea divisoria

        // Crear imagen del álbum
        JLabel lblImagen = new JLabel();
        lblImagen.setPreferredSize(new Dimension(65, 65));
        lblImagen.setHorizontalAlignment(SwingConstants.CENTER);
        if (cancion.getFotoAlbum() != null) {
            ImageIcon icon = GestorImagenesMongo.getImageIcon(cancion.getFotoAlbum(), GestorImagenesMongo.SizeImage.SMALL);
            lblImagen.setIcon(icon);
        } else {
            lblImagen.setOpaque(true);
            lblImagen.setBackground(Color.BLUE); // Fondo azul si no hay imagen
        }

        // Información principal de la canción
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(1, 3));
        infoPanel.setBackground(new Color(24, 24, 24));

        JLabel lblTitulo = new JLabel(cancion.getTitulo());
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 14));

//        JLabel lblFecha = new JLabel(cancion.);
//        lblFecha.setForeground(Color.LIGHT_GRAY);
//        lblFecha.setFont(new Font("Arial", Font.PLAIN, 12));
//        JLabel lblAlbum = new JLabel(cancion.get);
//        lblAlbum.setForeground(Color.LIGHT_GRAY);
//        lblAlbum.setFont(new Font("Arial", Font.PLAIN, 12));
        // Duración
        JLabel lblDuracion = new JLabel(cancion.getDuracionFormateada());
        lblDuracion.setForeground(Color.PINK);
        lblDuracion.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDuracion.setHorizontalAlignment(SwingConstants.RIGHT);

        // Agregar elementos al panel principal
        infoPanel.add(lblTitulo);
//        infoPanel.add(lblFecha);
//        infoPanel.add(lblAlbum);

        panel.add(lblImagen, BorderLayout.WEST);
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(lblDuracion, BorderLayout.EAST);

        return panel;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAlbumes;
    private javax.swing.JButton bAlbumes1;
    private javax.swing.JButton bAlbumes2;
    private javax.swing.JButton bAlbumes3;
    private javax.swing.JButton bAlbumes4;
    private javax.swing.JButton bCerrarSesion;
    private javax.swing.JButton bHome;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblNameUser;
    private javax.swing.JPanel panelCanciones;
    // End of variables declaration//GEN-END:variables
}
