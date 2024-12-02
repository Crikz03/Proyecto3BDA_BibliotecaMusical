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
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import recursos.Forms;
import recursos.GestorImagenesMongo;
import recursos.Imagen;

/**
 *
 * @author Chris
 */
public class FrmPerfil extends javax.swing.JFrame {

    private IArtistaDAO adao;
    private IAlbumDAO albumdao;
    private IDetallesCancionDAO candao;
    private UsuarioDTO usuarioLoggeado;
    

    /**
     * Creates new form FrmHome
     *
     * @param usuarioLoggeado
     */
    public FrmPerfil(UsuarioDTO usuarioLoggeado) {
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
        this.cargarDatosUsuario();
        this.inicializarListaGenerosBaneados();
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
        jLabel2 = new javax.swing.JLabel();
        lblNameUser = new javax.swing.JLabel();
        btnEditarGenerosBaneados = new javax.swing.JButton();
        lblFoto = new javax.swing.JLabel();
        btnEditarDatosPersonales1 = new javax.swing.JButton();
        lblCorreo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listaGenerosBaneados = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));
        jPanel1.setLayout(null);

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 153)));

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
        bCerrarSesion.setForeground(new java.awt.Color(255, 51, 204));
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
                        .addGap(9, 9, 9)
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.add(jPanel2);
        jPanel2.setBounds(0, 0, 222, 889);

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Generos baneados");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(228, 342, 332, 36);

        lblNameUser.setBackground(new java.awt.Color(0, 0, 0));
        lblNameUser.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblNameUser.setForeground(new java.awt.Color(255, 255, 255));
        lblNameUser.setText("name");
        jPanel1.add(lblNameUser);
        lblNameUser.setBounds(460, 110, 224, 32);

        btnEditarGenerosBaneados.setBackground(new java.awt.Color(255, 51, 204));
        btnEditarGenerosBaneados.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnEditarGenerosBaneados.setForeground(new java.awt.Color(255, 255, 255));
        btnEditarGenerosBaneados.setText("Editar generos baneados");
        btnEditarGenerosBaneados.setActionCommand("");
        btnEditarGenerosBaneados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarGenerosBaneadosActionPerformed(evt);
            }
        });
        jPanel1.add(btnEditarGenerosBaneados);
        btnEditarGenerosBaneados.setBounds(234, 640, 211, 30);

        lblFoto.setText("jLabel5");
        jPanel1.add(lblFoto);
        lblFoto.setBounds(302, 75, 146, 135);

        btnEditarDatosPersonales1.setBackground(new java.awt.Color(255, 51, 204));
        btnEditarDatosPersonales1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnEditarDatosPersonales1.setForeground(new java.awt.Color(255, 255, 255));
        btnEditarDatosPersonales1.setText("Editar datos personales");
        btnEditarDatosPersonales1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarDatosPersonales1ActionPerformed(evt);
            }
        });
        jPanel1.add(btnEditarDatosPersonales1);
        btnEditarDatosPersonales1.setBounds(312, 259, 201, 30);

        lblCorreo.setBackground(new java.awt.Color(0, 0, 0));
        lblCorreo.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblCorreo.setForeground(new java.awt.Color(255, 255, 255));
        lblCorreo.setText("correo");
        jPanel1.add(lblCorreo);
        lblCorreo.setBounds(460, 160, 224, 19);

        listaGenerosBaneados.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(listaGenerosBaneados);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(250, 390, 130, 150);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1240, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 803, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void bCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCerrarSesionActionPerformed
        Forms.cargarForm(new FrmInicioSesion(), this);
    }//GEN-LAST:event_bCerrarSesionActionPerformed

    private void bAlbumes4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAlbumes4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bAlbumes4ActionPerformed

    private void bAlbumes3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAlbumes3ActionPerformed
        Forms.cargarForm(new FrmPestañaCanciones(usuarioLoggeado), this);
    }//GEN-LAST:event_bAlbumes3ActionPerformed

    private void bAlbumes2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAlbumes2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bAlbumes2ActionPerformed

    private void bAlbumes1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAlbumes1ActionPerformed
        Forms.cargarForm(new FrmPestañaArtistas(usuarioLoggeado), this);
    }//GEN-LAST:event_bAlbumes1ActionPerformed

    private void bAlbumesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAlbumesActionPerformed
        Forms.cargarForm(new FrmPestañaAlbumes(usuarioLoggeado), this);
    }//GEN-LAST:event_bAlbumesActionPerformed

    private void bHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bHomeActionPerformed
        Forms.cargarForm(new FrmInicio(usuarioLoggeado), this);
    }//GEN-LAST:event_bHomeActionPerformed

    private void btnEditarGenerosBaneadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarGenerosBaneadosActionPerformed
       try {
        // Verificar si los géneros no deseados están inicializados
        if (usuarioLoggeado.getGenerosNoDeseados() == null) {
            usuarioLoggeado.setGenerosNoDeseados(new ArrayList<>());
        }

        // Abrir el formulario para editar géneros baneados
        Forms.cargarForm(new FrmEditarGenerosBaneados(usuarioLoggeado), this);
    } catch (PersistenciaException ex) {
        Logger.getLogger(FrmPerfil.class.getName()).log(Level.SEVERE, "Error al cargar FrmEditarGenerosBaneados", ex);
        JOptionPane.showMessageDialog(this, "Ocurrió un error al cargar la edición de géneros baneados.",
                "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_btnEditarGenerosBaneadosActionPerformed

    private void btnEditarDatosPersonales1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarDatosPersonales1ActionPerformed
        Forms.cargarForm(new FrmEditarPerfil(usuarioLoggeado), this);
    }//GEN-LAST:event_btnEditarDatosPersonales1ActionPerformed

    private void SetImageLabel(JLabel labelname, String root) {
        ImageIcon image = new ImageIcon(root);
        Icon icon = new ImageIcon(image.getImage().getScaledInstance(labelname.getWidth(), labelname.getHeight(), Image.SCALE_DEFAULT));
        labelname.setIcon(icon);
        this.repaint();
    }

    private void cargarDatosUsuario() {
        if (usuarioLoggeado != null) {
            lblNameUser.setText(usuarioLoggeado.getNombreUsuario());
            lblCorreo.setText(usuarioLoggeado.getCorreo());
            ImageIcon imageIcon
                    = GestorImagenesMongo
                            .getImageIcon(
                                    usuarioLoggeado.getFotoPerfil(),
                                    GestorImagenesMongo.SizeImage.SMALL
                            );
            lblFoto.setIcon(imageIcon);
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
    
        private void inicializarListaGenerosBaneados() {
        if (usuarioLoggeado != null && usuarioLoggeado.getGenerosNoDeseados() != null) {
            DefaultListModel<String> modeloLista = new DefaultListModel<>();
            for (String genero : usuarioLoggeado.getGenerosNoDeseados()) {
                modeloLista.addElement(genero);
            }

            if (listaGenerosBaneados == null) {
                listaGenerosBaneados = new JList<>(modeloLista);
                JScrollPane scrollPane = new JScrollPane(listaGenerosBaneados);
                scrollPane.setPreferredSize(new Dimension(300, 150));
                scrollPane.setBorder(BorderFactory.createTitledBorder("Géneros Baneados"));
                add(scrollPane, BorderLayout.EAST);
            } else {
                listaGenerosBaneados.setModel(modeloLista);
            }
        }
        revalidate();
        repaint();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAlbumes;
    private javax.swing.JButton bAlbumes1;
    private javax.swing.JButton bAlbumes2;
    private javax.swing.JButton bAlbumes3;
    private javax.swing.JButton bAlbumes4;
    private javax.swing.JButton bCerrarSesion;
    private javax.swing.JButton bHome;
    private javax.swing.JButton btnEditarDatosPersonales1;
    private javax.swing.JButton btnEditarGenerosBaneados;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblCorreo;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblNameUser;
    private javax.swing.JList<String> listaGenerosBaneados;
    // End of variables declaration//GEN-END:variables
}
