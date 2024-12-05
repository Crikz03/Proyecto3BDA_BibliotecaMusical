/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package frames;

import dto.AlbumDTO;
import dto.ArtistaDTO;
import dto.UsuarioDTO;
import excepciones.NegocioException;
import interfaces.IAlbumBO;
import interfaces.IArtistaBO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import negocio.AlbumBO;
import negocio.ArtistaBO;
import recursos.Forms;
import recursos.GestorImagenesMongo;

/**
 *
 * @author Chris
 */
public class FrmPestañaAlbumes extends javax.swing.JFrame {

    private IAlbumBO albumbo;
    private UsuarioDTO usuarioLoggeado;
    List<AlbumDTO> albumesGlobal;
    private IArtistaBO abo;
    private boolean isUpdatingComboBox = false;

    /**
     * Creates new form FrmPestañaAlbumes
     */
    public FrmPestañaAlbumes(UsuarioDTO usuarioLoggeado) {
        initComponents();
        this.albumbo = new AlbumBO();
        this.usuarioLoggeado = usuarioLoggeado;
        this.abo = new ArtistaBO();
        this.configuraFrame();
    }

    /**
     * Constructor con lista de álbumes global.
     */
    public FrmPestañaAlbumes(UsuarioDTO usuarioLoggeado, List<AlbumDTO> albumesGlobal) {
        initComponents();
        this.albumbo = new AlbumBO();
        this.usuarioLoggeado = usuarioLoggeado;
        this.albumesGlobal = albumesGlobal;
        this.abo = new ArtistaBO();
        this.configuraFrame();
    }


        private void configuraFrame() {
        // Maximiza el frame al tamaño de la pantalla
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Crea un JScrollPane alrededor de jPanel1
        JScrollPane scrollPane = new JScrollPane(jPanel1);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Configura el layout del contenido principal
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Configuraciones adicionales del frame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        this.SetImageLabel(jLabel1, "images/logo.png");

        // Lógica personalizada
        this.obtieneAlbumes();
        this.cargarDatosUsuario();


        // Revalida y repinta
        jPanel1.revalidate();
        jPanel1.repaint();
    }

    private void SetImageLabel(JLabel labelname, String root) {
        ImageIcon image = new ImageIcon(root);
        Icon icon = new ImageIcon(image.getImage().getScaledInstance(labelname.getWidth(), labelname.getHeight(), Image.SCALE_DEFAULT));
        labelname.setIcon(icon);
        this.repaint();
    }

    private void inicializarBuscador() {

        // ComboBox para mostrar los resultados
        comboResultados.setEditable(false); // No editable
        comboResultados.setVisible(false); // Oculto al inicio

        txtBuscar.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtBuscar.getText().equals("Buscar por...")) {
                    txtBuscar.setText(""); // Limpiar el texto
                    txtBuscar.setForeground(Color.WHITE); // Cambiar el color a texto normal
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtBuscar.getText().trim().isEmpty()) {
                    txtBuscar.setText("Buscar por..."); // Restaurar el placeholder
                    txtBuscar.setForeground(Color.WHITE); // Cambiar el color a gris
                }
            }
        });

        // Eventos
        // Eventos para buscar dinámicamente
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                actualizarComboResultados();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                actualizarComboResultados();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                actualizarComboResultados();
            }
        });

        // Evento de selección del JComboBox
        comboResultados.addActionListener(e -> {
            if (!isUpdatingComboBox) { // Solo permitir la selección si no está actualizando
                String seleccionado = (String) comboResultados.getSelectedItem();
                if (seleccionado != null && !seleccionado.isEmpty()) {
                    llenarAlbum(this.albumesGlobal);
                }
            }
        });

    }

    private void actualizarComboResultados() {
        String termino = txtBuscar.getText().trim().toLowerCase();

        if (termino.isEmpty()) {
            comboResultados.setVisible(false); // Ocultar si no hay término
            comboResultados.setPopupVisible(false); // Ocultar el desplegable
            return;
        }

        isUpdatingComboBox = true; // Desactivar temporalmente el evento de selección

        List<String> coincidencias = new ArrayList<>();
        try {
            List<String> generosNoDeseados = usuarioLoggeado.getGenerosNoDeseados();

            // Buscar en Álbumes
            if (checkAlbumes.isSelected() || (!checkFechaLanzamiento.isSelected() && !checkGenero.isSelected())) {
                List<AlbumDTO> albumes = albumbo.buscarPorNombre(termino);
                albumes.stream()
                        .filter(album -> {
                            try {
                                ArtistaDTO artista = abo.obtenerArtistaPorId(album.getArtistaId());
                                return !generosNoDeseados.contains(artista.getGenero());
                            } catch (NegocioException e) {
                                e.printStackTrace();
                                return false;
                            }
                        })
                        .forEach(album -> coincidencias.add("Álbum: " + album.getNombre()));
                this.albumesGlobal = albumes;
            }

            // Actualizar el comboResultados
            comboResultados.removeAllItems();
            if (!coincidencias.isEmpty()) {
                coincidencias.forEach(comboResultados::addItem);
                comboResultados.setVisible(true); // Mostrar si hay resultados
                comboResultados.setPopupVisible(true); // Desplegar automáticamente
            } else {
                comboResultados.setVisible(false); // Ocultar si no hay resultados
                comboResultados.setPopupVisible(false); // Ocultar el desplegable
            }

        } catch (NegocioException e) {
            e.printStackTrace();
        }

        isUpdatingComboBox = false; // Reactivar el evento de selección
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
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btnBuscar = new javax.swing.JButton();
        checkGenero = new javax.swing.JCheckBox();
        checkAlbumes = new javax.swing.JCheckBox();
        checkFechaLanzamiento = new javax.swing.JCheckBox();
        comboResultados = new javax.swing.JComboBox<>();
        txtBuscar = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jPanel2.setBackground(new java.awt.Color(0, 0, 0));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 3, new java.awt.Color(255, 0, 153)));

        jLabel1.setBorder(new javax.swing.border.MatteBorder(null));

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

        panelCanciones.setBorder(new javax.swing.border.MatteBorder(null));

        javax.swing.GroupLayout panelCancionesLayout = new javax.swing.GroupLayout(panelCanciones);
        panelCanciones.setLayout(panelCancionesLayout);
        panelCancionesLayout.setHorizontalGroup(
            panelCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1000, Short.MAX_VALUE)
        );
        panelCancionesLayout.setVerticalGroup(
            panelCancionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 453, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setBorder(new javax.swing.border.MatteBorder(null));

        lblNameUser.setBackground(new java.awt.Color(0, 0, 0));
        lblNameUser.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        lblNameUser.setForeground(new java.awt.Color(255, 255, 255));
        lblNameUser.setText("name");

        lblFoto.setText("jLabel5");

        jLabel6.setBackground(new java.awt.Color(255, 0, 153));
        jLabel6.setForeground(new java.awt.Color(255, 0, 153));
        jLabel6.setText("jLabel6");
        jLabel6.setOpaque(true);

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
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFoto, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblNameUser))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(0, 0, 0));

        btnBuscar.setBackground(new java.awt.Color(255, 0, 153));
        btnBuscar.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        btnBuscar.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscar.setText("Buscar");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });

        checkGenero.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        checkGenero.setForeground(new java.awt.Color(255, 255, 255));
        checkGenero.setText("Género");
        checkGenero.setToolTipText("");

        checkAlbumes.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        checkAlbumes.setForeground(new java.awt.Color(255, 255, 255));
        checkAlbumes.setText("Álbum");

        checkFechaLanzamiento.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        checkFechaLanzamiento.setForeground(new java.awt.Color(255, 255, 255));
        checkFechaLanzamiento.setText("Fecha de Lanzamiento");

        comboResultados.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtBuscar.setBackground(new java.awt.Color(51, 51, 51));
        txtBuscar.setForeground(new java.awt.Color(255, 255, 255));
        txtBuscar.setText("Buscar por...");
        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Buscar");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboResultados, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(checkFechaLanzamiento)
                        .addGap(18, 18, 18)
                        .addComponent(checkAlbumes)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(checkGenero)
                        .addGap(18, 18, 18)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addGap(12, 12, 12)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(checkGenero)
                        .addComponent(checkAlbumes)
                        .addComponent(checkFechaLanzamiento))
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(comboResultados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(panelCanciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        // TODO add your handling code here:
    }//GEN-LAST:event_bAlbumesActionPerformed

    private void bAlbumes1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAlbumes1ActionPerformed
        Forms.cargarForm(new FrmPestañaArtistas(usuarioLoggeado), this);
    }//GEN-LAST:event_bAlbumes1ActionPerformed

    private void bAlbumes2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAlbumes2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bAlbumes2ActionPerformed

    private void bAlbumes3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAlbumes3ActionPerformed
        Forms.cargarForm(new FrmPestañaCanciones(usuarioLoggeado), this);
    }//GEN-LAST:event_bAlbumes3ActionPerformed

    private void bAlbumes4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bAlbumes4ActionPerformed
        Forms.cargarForm(new FrmPerfil(usuarioLoggeado), this);
    }//GEN-LAST:event_bAlbumes4ActionPerformed

    private void bCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCerrarSesionActionPerformed
        Forms.cargarForm(new FrmInicioSesion(), this);
    }//GEN-LAST:event_bCerrarSesionActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed

        String termino = txtBuscar.getText().trim();

        if (termino.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa un término de búsqueda.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            List<String> generosNoDeseados = usuarioLoggeado.getGenerosNoDeseados();

            // Buscar en Álbumes
            if (checkAlbumes.isSelected() || (!checkFechaLanzamiento.isSelected() && !checkGenero.isSelected())) {
                List<AlbumDTO> albumes = albumbo.buscarPorNombre(termino);
                 List<AlbumDTO> albumesFiltrados = albumes.stream()
                    .filter(album -> {
                        try {
                            return album.getGenero().stream().noneMatch(generosNoDeseados::contains);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    })
                    .collect(Collectors.toList());
                this.albumesGlobal = albumesFiltrados;
                llenarAlbum(this.albumesGlobal);
            }
        } catch (NegocioException e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_btnBuscarActionPerformed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarActionPerformed

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

    /*private void obtieneAlbumes() {
        try {
            // Obtiene la lista de géneros no deseados del usuario
            List<String> generosNoDeseados = usuarioLoggeado.getGenerosNoDeseados();

            // Obtiene todos los álbumes
            List<AlbumDTO> albumes = this.albumbo.obtenerAlbumes();

            // Filtra los álbumes basados en el género del artista
            List<AlbumDTO> albumesFiltrados = albumes.stream()
                    .filter(album -> {
                        try {
                            // Buscar los álbumes del artista
                            List<AlbumDTO> albumesArtista = albumbo.buscarPorArtista(album.getArtistaId());
                            if (albumesArtista.isEmpty()) {
                                return false; // No hay información sobre este artista
                            }

                            // Verificar si algún género del artista está en géneros no deseados
                            List<String> generoArtista = albumesArtista.get(0).getGenero();
                            return generoArtista.stream().noneMatch(generosNoDeseados::contains);
                        } catch (NegocioException e) {
                            e.printStackTrace();
                            return false; // Si ocurre un error, excluye este álbum
                        }
                    })
                    .collect(Collectors.toList());

            // Imprimir los álbumes que se van a mostrar
            System.out.println("Álbumes que se mostrarán:");
            albumesFiltrados.forEach(album -> System.out.println(
                    "Nombre: " + album.getNombre() + ", Géneros del artista: " + album.getGenero()
            ));

            // Mezclar y limitar los álbumes para mostrar
            Collections.shuffle(albumesFiltrados);
            int maxArtistas = 28;
            List<AlbumDTO> albumesLimitados = albumesFiltrados.size() > maxArtistas
                    ? albumesFiltrados.subList(0, maxArtistas)
                    : albumesFiltrados;

            // Configurar el layout con 4 filas y 7 columnas
            panelCanciones.setLayout(new GridLayout(4, 7, 15, 15));
            panelCanciones.setBackground(new Color(18, 18, 18));

            // Crear paneles para los álbumes filtrados
            for (AlbumDTO album : albumesLimitados) {
                JPanel panelAlbum = creaPanel(album, album.getImagenPortada());
                panelCanciones.add(panelAlbum);
            }

            panelCanciones.revalidate();
            panelCanciones.repaint();
        } catch (NegocioException e) {
            e.printStackTrace();
        }
    }*/
    private void obtieneAlbumes() {
        try {
            // Obtener la lista de géneros no deseados del usuario
            List<String> generosNoDeseados = usuarioLoggeado.getGenerosNoDeseados();

            // Obtener la lista de álbumes a usar (global o todos los álbumes)
            List<AlbumDTO> albumes;
            if (albumesGlobal != null && !albumesGlobal.isEmpty()) {
                albumes = albumesGlobal;
            } else {
                albumes = this.albumbo.obtenerAlbumes();
            }

            // Filtrar los álbumes basados en el género del artista
            List<AlbumDTO> albumesFiltrados = albumes.stream()
                    .filter(album -> {
                        try {
                            return album.getGenero().stream().noneMatch(generosNoDeseados::contains);
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    })
                    .collect(Collectors.toList());

            // Mezclar y limitar los álbumes a 28
            Collections.shuffle(albumesFiltrados);
            int maxAlbumes = 28;
            List<AlbumDTO> albumesAMostrar = albumesFiltrados.subList(0, Math.min(maxAlbumes, albumesFiltrados.size()));
            llenarAlbum(albumesAMostrar);

        } catch (NegocioException e) {
            e.printStackTrace();
        }
    }

    private void llenarAlbum(List<AlbumDTO> albumesAMostrar) {
        panelCanciones.removeAll();
        // Configurar el layout con 4 filas y 7 columnas
        panelCanciones.setLayout(new GridLayout(4, 7, 15, 15));
        panelCanciones.setBackground(new Color(18, 18, 18));

        // Crear paneles para los álbumes filtrados
        for (AlbumDTO album : albumesAMostrar) {
            JPanel panelAlbum = creaPanel(album);
            panelCanciones.add(panelAlbum);
        }

        panelCanciones.revalidate();
        panelCanciones.repaint();
    }

    /*private JPanel creaPanel(AlbumDTO album, Imagen imagen) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(18, 18, 18));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnFoto = new JButton(); // Cambiado a JButton
        btnFoto.setPreferredSize(new Dimension(150, 150));
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
            Forms.cargarForm(new FrmDetallesAlbum(usuarioLoggeado, album), this);
        });

        JLabel lblNombre = new JLabel(album.getNombre());
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 14));
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(btnFoto);
        panel.add(Box.createVerticalStrut(5)); // Espaciado
        panel.add(lblNombre);

        return panel;
    }*/
    private JPanel creaPanel(AlbumDTO album) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(18, 18, 18));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnFoto = new JButton(); // Cambiado a JButton
        btnFoto.setPreferredSize(new Dimension(150, 150));
        btnFoto.setOpaque(true);
        btnFoto.setContentAreaFilled(false); // Elimina el fondo por defecto
        btnFoto.setBorder(new LineBorder(new Color(18, 18, 18), 5, true));
        btnFoto.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Usa GestorImagenesMongo para convertir la imagen
        if (album.getImagenPortada() != null) {
            ImageIcon icon = GestorImagenesMongo.getImageIcon(album.getImagenPortada(), GestorImagenesMongo.SizeImage.MEDIUM);
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
            System.out.println("Álbum seleccionado: " + album.getNombre());
            Forms.cargarForm(new FrmDetallesAlbum(usuarioLoggeado, album), this);
        });

        JLabel lblNombre = new JLabel(album.getNombre());
        lblNombre.setFont(new Font("Arial", Font.PLAIN, 14));
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(btnFoto);
        panel.add(Box.createVerticalStrut(5)); // Espaciado
        panel.add(lblNombre);

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
    private javax.swing.JButton btnBuscar;
    private javax.swing.JCheckBox checkAlbumes;
    private javax.swing.JCheckBox checkFechaLanzamiento;
    private javax.swing.JCheckBox checkGenero;
    private javax.swing.JComboBox<String> comboResultados;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblNameUser;
    private javax.swing.JPanel panelCanciones;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
