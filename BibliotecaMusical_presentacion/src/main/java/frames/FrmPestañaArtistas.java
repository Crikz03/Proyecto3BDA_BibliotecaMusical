/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package frames;

import dto.ArtistaDTO;
import dto.UsuarioDTO;
import excepciones.NegocioException;
import interfaces.IArtistaBO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import negocio.ArtistaBO;
import recursos.Forms;
import recursos.GestorImagenesMongo;

/**
 *
 * @author Chris
 */
public class FrmPestañaArtistas extends javax.swing.JFrame {

    private IArtistaBO abo;
    private UsuarioDTO usuarioLoggeado;
    private List<ArtistaDTO> artistasBuscados;
    private boolean isUpdatingComboBox = false;

    /**
     * Creates new form FrmPestañaArtistas
     *
     * @param usuarioLoggeado
     */
    public FrmPestañaArtistas(UsuarioDTO usuarioLoggeado) {
        initComponents();
        this.abo = new ArtistaBO();
        this.usuarioLoggeado = usuarioLoggeado;

        this.configuraFrame();
    }

    public FrmPestañaArtistas(UsuarioDTO usuarioLoggeado, List<ArtistaDTO> artistasBuscados) {
        initComponents();
        this.abo = new ArtistaBO();
        this.usuarioLoggeado = usuarioLoggeado;
        this.artistasBuscados = artistasBuscados;
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
        this.obtieneArtistas();
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
        comboResultados4.setEditable(false); // No editable
        comboResultados4.setVisible(false); // Oculto al inicio

        txtBuscar4.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtBuscar4.getText().equals("Buscar por...")) {
                    txtBuscar4.setText(""); // Limpiar el texto
                    txtBuscar4.setForeground(Color.WHITE); // Cambiar el color a texto normal
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtBuscar4.getText().trim().isEmpty()) {
                    txtBuscar4.setText("Buscar por..."); // Restaurar el placeholder
                    txtBuscar4.setForeground(Color.WHITE); // Cambiar el color a gris
                }
            }
        });

        // Eventos
        // Eventos para buscar dinámicamente
        txtBuscar4.getDocument().addDocumentListener(new DocumentListener() {
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
        comboResultados4.addActionListener(e -> {
            if (!isUpdatingComboBox) { // Solo permitir la selección si no está actualizando
                String seleccionado = (String) comboResultados4.getSelectedItem();
                if (seleccionado != null && !seleccionado.isEmpty()) {
                    llenarArtistaPanel(this.artistasBuscados);
                }
            }
        });

    }

    private void actualizarComboResultados() {
        String termino = txtBuscar4.getText().trim().toLowerCase();

        if (termino.isEmpty()) {
            comboResultados4.setVisible(false); // Ocultar si no hay término
            comboResultados4.setPopupVisible(false); // Ocultar el desplegable
            return;
        }

        isUpdatingComboBox = true; // Desactivar temporalmente el evento de selección

        List<String> coincidencias = new ArrayList<>();
        try {
            List<String> generosNoDeseados = usuarioLoggeado.getGenerosNoDeseados();

            // Buscar en Artistas
            if (checkNombre.isSelected() || (!checkGenero.isSelected())) {
                List<ArtistaDTO> artistas = abo.buscarArtistasPorNombre(termino);
                artistas.stream()
                        .filter(artista -> !generosNoDeseados.contains(artista.getGenero()))
                        .forEach(artista -> coincidencias.add("Artista: " + artista.getNombre()));
                this.artistasBuscados = artistas;
            }

            // Actualizar el comboResultados
            comboResultados4.removeAllItems();
            if (!coincidencias.isEmpty()) {
                coincidencias.forEach(comboResultados4::addItem);
                comboResultados4.setVisible(true); // Mostrar si hay resultados
                comboResultados4.setPopupVisible(true); // Desplegar automáticamente
            } else {
                comboResultados4.setVisible(false); // Ocultar si no hay resultados
                comboResultados4.setPopupVisible(false); // Ocultar el desplegable
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
        panelArtistas = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        lblNameUser = new javax.swing.JLabel();
        lblFoto = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        btnBuscar4 = new javax.swing.JButton();
        checkGenero = new javax.swing.JCheckBox();
        checkNombre = new javax.swing.JCheckBox();
        comboResultados4 = new javax.swing.JComboBox<>();
        txtBuscar4 = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

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
        bCerrarSesion.setForeground(new java.awt.Color(255, 0, 153));
        bCerrarSesion.setText("Cerrar Sesión");
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
                        .addGap(10, 10, 10)
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
                .addContainerGap(340, Short.MAX_VALUE))
        );

        panelArtistas.setBorder(new javax.swing.border.MatteBorder(null));

        javax.swing.GroupLayout panelArtistasLayout = new javax.swing.GroupLayout(panelArtistas);
        panelArtistas.setLayout(panelArtistasLayout);
        panelArtistasLayout.setHorizontalGroup(
            panelArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        panelArtistasLayout.setVerticalGroup(
            panelArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 523, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(0, 0, 0));
        jPanel3.setBorder(new javax.swing.border.MatteBorder(null));

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

        jLabel6.setBackground(new java.awt.Color(255, 0, 153));
        jLabel6.setForeground(new java.awt.Color(255, 0, 153));
        jLabel6.setText("jLabel6");
        jLabel6.setAlignmentX(0.5F);
        jLabel6.setOpaque(true);

        jPanel8.setBackground(new java.awt.Color(0, 0, 0));

        btnBuscar4.setBackground(new java.awt.Color(255, 0, 153));
        btnBuscar4.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        btnBuscar4.setForeground(new java.awt.Color(255, 255, 255));
        btnBuscar4.setText("Buscar");
        btnBuscar4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscar4ActionPerformed(evt);
            }
        });

        checkGenero.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        checkGenero.setForeground(new java.awt.Color(255, 255, 255));
        checkGenero.setText("Genero");

        checkNombre.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        checkNombre.setForeground(new java.awt.Color(255, 255, 255));
        checkNombre.setText("Nombre");

        comboResultados4.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtBuscar4.setBackground(new java.awt.Color(51, 51, 51));
        txtBuscar4.setForeground(new java.awt.Color(255, 255, 255));
        txtBuscar4.setText("Buscar por canciones, álbumes, artistas...");

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Buscar");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtBuscar4, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(comboResultados4, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                        .addComponent(checkNombre)
                        .addGap(18, 18, 18)
                        .addComponent(checkGenero)
                        .addGap(113, 113, 113)
                        .addComponent(btnBuscar4, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(39, 39, 39))))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jLabel10)
                .addGap(12, 12, 12)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnBuscar4, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(checkGenero)
                        .addComponent(checkNombre))
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(comboResultados4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtBuscar4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 0, 153));
        jLabel2.setText("Artistas");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 733, Short.MAX_VALUE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(panelArtistas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(24, 24, 24)))
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panelArtistas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        // TODO add your handling code here:
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

    private void btnBuscar4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscar4ActionPerformed

        String termino = txtBuscar4.getText().trim();

        if (termino.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa un término de búsqueda.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            List<String> generosNoDeseados = usuarioLoggeado.getGenerosNoDeseados();

            // Buscar en Artistas
            if (checkNombre.isSelected() || (!checkGenero.isSelected())) {
                List<ArtistaDTO> artistas = abo.buscarArtistasPorNombre(termino);
                List<ArtistaDTO> artistasFiltrados = artistas.stream()
                        .filter(artista -> !generosNoDeseados.contains(artista.getGenero()))
                        .collect(Collectors.toList());
                this.artistasBuscados = artistasFiltrados;
                llenarArtistaPanel(this.artistasBuscados);
            }

        } catch (NegocioException e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_btnBuscar4ActionPerformed

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
            // Obtener la lista de géneros no deseados del usuario
            List<String> generosNoDeseados = usuarioLoggeado.getGenerosNoDeseados();

            // Obtener la lista de artistas
            List<ArtistaDTO> artistas;
            if (this.artistasBuscados != null) {
                if (this.artistasBuscados.size() > 0) {
                    artistas = this.artistasBuscados;
                } else {
                    artistas = this.abo.obtenerArtistas();
                }
            } else {
                artistas = this.abo.obtenerArtistas();
            }

            // Filtrar artistas cuyo género no esté en la lista de géneros no deseados
            List<ArtistaDTO> artistasFiltrados = artistas.stream()
                    .filter(artista -> !generosNoDeseados.contains(artista.getGenero()))
                    .collect(Collectors.toList());

            // Mezclar y limitar a 28 artistas (7 columnas x 4 filas)
            Collections.shuffle(artistasFiltrados);
            int maxArtistas = 28;
            List<ArtistaDTO> artistasLimitados = artistasFiltrados.size() > maxArtistas
                    ? artistasFiltrados.subList(0, maxArtistas)
                    : artistasFiltrados;
            llenarArtistaPanel(artistasLimitados);

        } catch (NegocioException e) {
            e.printStackTrace();
        }
    }

    private void llenarArtistaPanel(List<ArtistaDTO> artistasLimitados) {
        panelArtistas.removeAll();
        // Configurar el layout con 4 filas y 7 columnas
        panelArtistas.setLayout(new GridLayout(4, 7, 20, 20));
        panelArtistas.setBackground(new Color(18, 18, 18));

        // Crear los paneles redondos para los artistas
        for (ArtistaDTO artista : artistasLimitados) {
            JPanel panelArtista = creaPanelRedondo(artista);
            panelArtistas.add(panelArtista);
        }

        panelArtistas.revalidate();
        panelArtistas.repaint();
    }

    private JPanel creaPanelRedondo(ArtistaDTO artista) {
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
        if (artista.getImagen() != null) {
            ImageIcon icon = GestorImagenesMongo.getImageIcon(artista.getImagen(), GestorImagenesMongo.SizeImage.MEDIUM);
            if (icon != null) {
                btnFoto.setIcon(new ImageIcon(makeRoundedImage(icon.getImage(), 130)));
            } else {
                btnFoto.setBackground(Color.RED);
            }
        } else {
            btnFoto.setBackground(Color.BLUE);
        }

        btnFoto.addActionListener(e -> {
            Forms.cargarForm(new FrmDetallesArtistas(usuarioLoggeado, artista), this);
        });

        JLabel lblNombre = new JLabel(artista.getNombre());
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAlbumes;
    private javax.swing.JButton bAlbumes1;
    private javax.swing.JButton bAlbumes2;
    private javax.swing.JButton bAlbumes3;
    private javax.swing.JButton bAlbumes4;
    private javax.swing.JButton bCerrarSesion;
    private javax.swing.JButton bHome;
    private javax.swing.JButton btnBuscar4;
    private javax.swing.JCheckBox checkGenero;
    private javax.swing.JCheckBox checkNombre;
    private javax.swing.JComboBox<String> comboResultados4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel lblFoto;
    private javax.swing.JLabel lblNameUser;
    private javax.swing.JPanel panelArtistas;
    private javax.swing.JTextField txtBuscar4;
    // End of variables declaration//GEN-END:variables
}
