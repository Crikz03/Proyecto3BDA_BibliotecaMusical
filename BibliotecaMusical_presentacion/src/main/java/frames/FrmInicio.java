/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package frames;

import datos.AlbumDAO;
import datos.ArtistaDAO;
import datos.DetallesCancionDAO;
import dto.UsuarioDTO;
import excepciones.PersistenciaException;
import interfaces.IAlbumDAO;
import interfaces.IArtistaDAO;
import interfaces.IDetallesCancionDAO;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import objetos.Albumes;
import objetos.Artistas;
import objetos.DetallesCancion;
import recursos.Forms;
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
    private UsuarioDTO usuarioLoggeado;

    /**
     * Creates new form FrmHome
     * @param usuarioLoggeado
     */
    public FrmInicio(UsuarioDTO usuarioLoggeado) {
        initComponents();
        this.SetImageLabel(jLabel1, "images/logo.png");
        this.adao = new ArtistaDAO();
        this.albumdao = new AlbumDAO();
        this.candao = new DetallesCancionDAO();
        this.usuarioLoggeado = usuarioLoggeado;

        this.configuraFrame();
    }

    private void configuraFrame() {
        setSize(1830, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.obtieneArtistas();
        this.obtieneAlbumes();
        this.obtieneCanciones();
        this.cargarDatosUsuario();
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
        bHome = new javax.swing.JButton();
        bAlbumes = new javax.swing.JButton();
        bAlbumes1 = new javax.swing.JButton();
        bAlbumes2 = new javax.swing.JButton();
        bAlbumes3 = new javax.swing.JButton();
        bAlbumes4 = new javax.swing.JButton();
        bCerrarSesion = new javax.swing.JButton();
        panelArtistas = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        lblNameUser = new javax.swing.JLabel();
        lblFoto = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        panelCanciones.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 153)));

        javax.swing.GroupLayout panelCancionesLayout = new javax.swing.GroupLayout(panelCanciones);
        panelCanciones.setLayout(panelCancionesLayout);
        panelCancionesLayout.setHorizontalGroup(
            panelCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelCancionesLayout.setVerticalGroup(
            panelCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 178, Short.MAX_VALUE)
        );

        panelAlbumes.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 153)));

        javax.swing.GroupLayout panelAlbumesLayout = new javax.swing.GroupLayout(panelAlbumes);
        panelAlbumes.setLayout(panelAlbumesLayout);
        panelAlbumesLayout.setHorizontalGroup(
            panelAlbumesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelAlbumesLayout.setVerticalGroup(
            panelAlbumesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 178, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 153)));
        jPanel2.setForeground(new java.awt.Color(0, 0, 0));

        jLabel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 153)));

        bHome.setBackground(new java.awt.Color(0, 0, 0));
        bHome.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
        bHome.setForeground(new java.awt.Color(255, 255, 255));
        bHome.setText("Home");
        bHome.setContentAreaFilled(false);
        bHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bHomeActionPerformed(evt);
            }
        });

        bAlbumes.setBackground(new java.awt.Color(0, 0, 0));
        bAlbumes.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
        bAlbumes.setForeground(new java.awt.Color(255, 255, 255));
        bAlbumes.setText("Albumes");
        bAlbumes.setContentAreaFilled(false);
        bAlbumes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAlbumesActionPerformed(evt);
            }
        });

        bAlbumes1.setBackground(new java.awt.Color(0, 0, 0));
        bAlbumes1.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
        bAlbumes1.setForeground(new java.awt.Color(255, 255, 255));
        bAlbumes1.setText("Artistas");
        bAlbumes1.setContentAreaFilled(false);
        bAlbumes1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAlbumes1ActionPerformed(evt);
            }
        });

        bAlbumes2.setBackground(new java.awt.Color(0, 0, 0));
        bAlbumes2.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
        bAlbumes2.setForeground(new java.awt.Color(255, 255, 255));
        bAlbumes2.setText("Favoritos");
        bAlbumes2.setContentAreaFilled(false);
        bAlbumes2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAlbumes2ActionPerformed(evt);
            }
        });

        bAlbumes3.setBackground(new java.awt.Color(0, 0, 0));
        bAlbumes3.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
        bAlbumes3.setForeground(new java.awt.Color(255, 255, 255));
        bAlbumes3.setText("Canciones");
        bAlbumes3.setContentAreaFilled(false);
        bAlbumes3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAlbumes3ActionPerformed(evt);
            }
        });

        bAlbumes4.setBackground(new java.awt.Color(0, 0, 0));
        bAlbumes4.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
        bAlbumes4.setForeground(new java.awt.Color(255, 255, 255));
        bAlbumes4.setText("Perfil de usuario");
        bAlbumes4.setContentAreaFilled(false);
        bAlbumes4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAlbumes4ActionPerformed(evt);
            }
        });

        bCerrarSesion.setBackground(new java.awt.Color(0, 0, 0));
        bCerrarSesion.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
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
                    .addComponent(bCerrarSesion))
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelArtistas.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 153)));

        javax.swing.GroupLayout panelArtistasLayout = new javax.swing.GroupLayout(panelArtistas);
        panelArtistas.setLayout(panelArtistasLayout);
        panelArtistasLayout.setHorizontalGroup(
            panelArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 578, Short.MAX_VALUE)
        );
        panelArtistasLayout.setVerticalGroup(
            panelArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 178, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 153)));

        lblNameUser.setBackground(new java.awt.Color(0, 0, 0));
        lblNameUser.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
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

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Canciones");

        jLabel3.setBackground(new java.awt.Color(0, 0, 0));
        jLabel3.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Albumes");

        jLabel4.setBackground(new java.awt.Color(0, 0, 0));
        jLabel4.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Artistas");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(panelArtistas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelAlbumes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(panelCanciones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(486, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 61, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelArtistas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelAlbumes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelCanciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
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
        // TODO add your handling code here:
    }//GEN-LAST:event_bHomeActionPerformed

    private void bAlbumesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAlbumesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bAlbumesActionPerformed

    private void bAlbumes1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAlbumes1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bAlbumes1ActionPerformed

    private void bAlbumes2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAlbumes2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bAlbumes2ActionPerformed

    private void bAlbumes3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAlbumes3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bAlbumes3ActionPerformed

    private void bAlbumes4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAlbumes4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bAlbumes4ActionPerformed

    private void bCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCerrarSesionActionPerformed
        Forms.cargarForm(new FrmInicioSesion(), this);
    }//GEN-LAST:event_bCerrarSesionActionPerformed

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

    private void obtieneArtistas() {
        try {
            // Obtener la lista de artistas
            List<Artistas> artistas = this.adao.obtenerCincoArtistas();
            Collections.shuffle(artistas);

            // Configurar el layout con espacio adicional para el botón
            panelArtistas.setLayout(new GridLayout(1, artistas.size(), 1, 0));
            panelArtistas.setBackground(new Color(18, 18, 18));

            // Crear los paneles redondos para los artistas
            for (Artistas artista : artistas) {
                JPanel panelArtista = creaPanelRedondo(artista.getNombre(), artista.getImagen());
                panelArtistas.add(panelArtista);
            }

            // Crear y agregar el botón "Ver Todos" al final
            JButton btnVerTodos = crearBotonVerTodos("Ver todos los artistas", e -> {
                System.out.println("Botón 'Ver Todos' presionado.");
                // Acción al presionar "Ver Todos" (como abrir otra ventana)
            });
            JPanel panelBoton = new JPanel();
            panelBoton.setBackground(new Color(18, 18, 18));
            panelBoton.setLayout(new GridBagLayout());
            panelBoton.add(btnVerTodos);

            panelArtistas.add(panelBoton);

            panelArtistas.revalidate();
            panelArtistas.repaint();
        } catch (PersistenciaException e) {
            e.printStackTrace();
        }
    }

    private void obtieneAlbumes() {
        try {
            List<Albumes> albumes = this.albumdao.obtenerCincoAlbumes();
            Collections.shuffle(albumes);

            panelAlbumes.setLayout(new GridLayout(1, albumes.size(), 1, 0));
            panelAlbumes.setBackground(new Color(18, 18, 18));

            for (Albumes album : albumes) {
                JPanel panelAlbum = creaPanel(album.getNombre(), album.getImagenPortada());
                panelAlbumes.add(panelAlbum);
            }

            // Crear y agregar el botón "Ver Todos" al final
            JButton btnVerTodos = crearBotonVerTodos("Ver todos los artistas", e -> {
                System.out.println("Botón 'Ver Todos' presionado.");
                // Acción al presionar "Ver Todos" (como abrir otra ventana)
            });
            JPanel panelBoton = new JPanel();
            panelBoton.setBackground(new Color(18, 18, 18));
            panelBoton.setLayout(new GridBagLayout());
            panelBoton.add(btnVerTodos);

            panelAlbumes.add(panelBoton);

            panelAlbumes.revalidate();
            panelAlbumes.repaint();
        } catch (PersistenciaException e) {
            e.printStackTrace();
        }
    }

    private void obtieneCanciones() {
        try {
            List<DetallesCancion> canciones = this.albumdao.obtenerCancionesDeAlbumes();

            Set<DetallesCancion> cancionesUnicas = new HashSet<>(canciones);
            List<DetallesCancion> cancionesSinDuplicados = new ArrayList<>(cancionesUnicas);

            Collections.shuffle(cancionesSinDuplicados);

            List<DetallesCancion> cancionesAMostrar = cancionesSinDuplicados.subList(0, Math.min(5, cancionesSinDuplicados.size()));

            panelCanciones.setLayout(new GridLayout(1, cancionesAMostrar.size(), 1, 0));
            panelCanciones.setBackground(new Color(18, 18, 18));

            for (DetallesCancion cancion : cancionesAMostrar) {
                JPanel panelArtista = creaPanel(cancion.getTitulo(), cancion.getFotoAlbum());
                panelCanciones.add(panelArtista);
            }

            // Crear y agregar el botón "Ver Todos" al final
            JButton btnVerTodos = crearBotonVerTodos("Ver todos los artistas", e -> {
                System.out.println("Botón 'Ver Todos' presionado.");
                // Acción al presionar "Ver Todos" (como abrir otra ventana)
            });
            JPanel panelBoton = new JPanel();
            panelBoton.setBackground(new Color(18, 18, 18));
            panelBoton.setLayout(new GridBagLayout());
            panelBoton.add(btnVerTodos);

            panelCanciones.add(panelBoton);

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
        btnFoto.setPreferredSize(new Dimension(80, 140));
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
            System.out.println("Nocausa");
        });

        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 14));
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(btnFoto);
        panel.add(Box.createVerticalStrut(5)); // Espaciado
        panel.add(lblNombre);

        return panel;
    }

    private JPanel creaPanelRedondo(String nombre, Imagen imagen) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(18, 18, 18));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnFoto = new JButton();
        btnFoto.setPreferredSize(new Dimension(80, 140));
        btnFoto.setOpaque(false);
        btnFoto.setContentAreaFilled(false);
        btnFoto.setBorder(null);
        btnFoto.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Crear imagen redonda
        if (imagen != null) {
            ImageIcon icon = GestorImagenesMongo.getImageIcon(imagen, GestorImagenesMongo.SizeImage.MEDIUM);
            if (icon != null) {
                btnFoto.setIcon(new ImageIcon(makeRoundedImage(icon.getImage(), 130)));
            } else {
                btnFoto.setBackground(Color.RED);
            }
        } else {
            btnFoto.setBackground(Color.BLUE);
        }

        btnFoto.addActionListener(e -> {
            System.out.println("Nocausa");
        });

        JLabel lblNombre = new JLabel(nombre);
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 14));
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(btnFoto);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblNombre);

        return panel;
    }

    /**
     * Método para redondear una imagen.
     */
    private Image makeRoundedImage(Image img, int diameter) {
        BufferedImage roundedImage = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = roundedImage.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setClip(new Ellipse2D.Float(0, 0, diameter, diameter));
        g2.drawImage(img, 0, 0, diameter, diameter, null);
        g2.dispose();

        return roundedImage;
    }

    private JButton crearBotonVerTodos(String tooltip, ActionListener actionListener) {
        JButton btnVerTodos = new JButton("+") {
            @Override
            protected void paintComponent(Graphics g) {
                // Hacer que el botón sea redondo al dibujar un óvalo relleno
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillOval(0, 0, getWidth(), getHeight());
                super.paintComponent(g2);
            }

            @Override
            protected void paintBorder(Graphics g) {
                // Dibujar el borde redondeado
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getForeground());
                g2.drawOval(0, 0, getWidth() - 1, getHeight() - 1);
            }

            @Override
            public Dimension getPreferredSize() {
                // Asegurar que el botón tenga un tamaño igual en ancho y alto
                return new Dimension(60, 60);
            }
        };

        btnVerTodos.setFont(new Font("Arial", Font.BOLD, 24));
        btnVerTodos.setForeground(Color.WHITE); // Texto blanco
        btnVerTodos.setBackground(new Color(36, 36, 36)); // Fondo oscuro
        btnVerTodos.setFocusPainted(false); // Sin borde al hacer clic
        btnVerTodos.setContentAreaFilled(false); // Evitar relleno estándar
        btnVerTodos.setToolTipText(tooltip);

        // Añadir acción personalizada
        btnVerTodos.addActionListener(actionListener);

        return btnVerTodos;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblNameUser;
    private javax.swing.JPanel panelAlbumes;
    private javax.swing.JPanel panelArtistas;
    private javax.swing.JPanel panelCanciones;
    // End of variables declaration//GEN-END:variables
}
