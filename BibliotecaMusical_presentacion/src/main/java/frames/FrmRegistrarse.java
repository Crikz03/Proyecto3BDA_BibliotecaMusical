/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package frames;

import dto.UsuarioDTO;
import excepciones.NegocioException;
import excepciones.PersistenciaException;
import interfaces.IUsuarioBO;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import negocio.UsuarioBO;
import recursos.Forms;
import recursos.GestorImagenesMongo;
import recursos.Imagen;
import recursos.ValidadorFrames;

/**
 *
 * @author Chris
 */
public class FrmRegistrarse extends javax.swing.JFrame {

    private Imagen imagenPerfil;
    private UsuarioDTO usuario;
    private IUsuarioBO usuariobo;

    /**
     * Creates new form FrmRegistrarse
     */
    public FrmRegistrarse() {
        initComponents();
        this.usuariobo = new UsuarioBO();
        setLocationRelativeTo(null); 
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
        panelArtistas = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        txtCorreo = new javax.swing.JTextField();
        bRegistra = new javax.swing.JButton();
        bHome = new javax.swing.JButton();
        bAlbumes = new javax.swing.JButton();
        txtConfirmarPassword = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        lblImage = new javax.swing.JLabel();
        beligeFoto = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        panelArtistas.setBackground(new java.awt.Color(102, 0, 102));
        panelArtistas.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 153)));
        panelArtistas.setForeground(new java.awt.Color(0, 0, 0));

        jLabel4.setBackground(new java.awt.Color(0, 0, 0));
        jLabel4.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Correo electrónico:");

        jLabel5.setBackground(new java.awt.Color(0, 0, 0));
        jLabel5.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Contraseña:");

        txtPassword.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
        txtPassword.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)));

        txtCorreo.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(221, 221, 221)));

        bRegistra.setBackground(new java.awt.Color(204, 0, 204));
        bRegistra.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
        bRegistra.setForeground(new java.awt.Color(0, 0, 0));
        bRegistra.setText("Registrarse");
        bRegistra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bRegistraActionPerformed(evt);
            }
        });

        bHome.setBackground(new java.awt.Color(0, 0, 0));
        bHome.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
        bHome.setForeground(new java.awt.Color(255, 255, 255));
        bHome.setText("Registrarse");
        bHome.setContentAreaFilled(false);
        bHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bHomeActionPerformed(evt);
            }
        });

        bAlbumes.setBackground(new java.awt.Color(0, 0, 0));
        bAlbumes.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
        bAlbumes.setForeground(new java.awt.Color(255, 255, 255));
        bAlbumes.setText("Iniciar Sesion");
        bAlbumes.setContentAreaFilled(false);
        bAlbumes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bAlbumesActionPerformed(evt);
            }
        });

        txtConfirmarPassword.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
        txtConfirmarPassword.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)));

        jLabel6.setBackground(new java.awt.Color(0, 0, 0));
        jLabel6.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Confirmar contraseña:");

        txtUsuario.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(221, 221, 221)));

        jLabel7.setBackground(new java.awt.Color(0, 0, 0));
        jLabel7.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Nombre de usuario:");

        lblImage.setBackground(new java.awt.Color(0, 0, 0));
        lblImage.setForeground(new java.awt.Color(255, 255, 255));
        lblImage.setBorder(new javax.swing.border.MatteBorder(null));

        beligeFoto.setBackground(new java.awt.Color(204, 0, 204));
        beligeFoto.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
        beligeFoto.setForeground(new java.awt.Color(0, 0, 0));
        beligeFoto.setText("Elegir foto perfil");
        beligeFoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                beligeFotoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelArtistasLayout = new javax.swing.GroupLayout(panelArtistas);
        panelArtistas.setLayout(panelArtistasLayout);
        panelArtistasLayout.setHorizontalGroup(
            panelArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelArtistasLayout.createSequentialGroup()
                .addContainerGap(212, Short.MAX_VALUE)
                .addGroup(panelArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelArtistasLayout.createSequentialGroup()
                        .addComponent(bHome, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(65, 65, 65)
                        .addComponent(bAlbumes))
                    .addComponent(bRegistra, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(219, 219, 219))
            .addGroup(panelArtistasLayout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addGroup(panelArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelArtistasLayout.createSequentialGroup()
                        .addGroup(panelArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtConfirmarPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addContainerGap())
                    .addGroup(panelArtistasLayout.createSequentialGroup()
                        .addGroup(panelArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 192, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelArtistasLayout.createSequentialGroup()
                                .addComponent(beligeFoto)
                                .addGap(129, 129, 129))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelArtistasLayout.createSequentialGroup()
                                .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(144, 144, 144))))))
        );
        panelArtistasLayout.setVerticalGroup(
            panelArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelArtistasLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(panelArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bAlbumes)
                    .addComponent(bHome))
                .addGroup(panelArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelArtistasLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtCorreo, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelArtistasLayout.createSequentialGroup()
                        .addGap(134, 134, 134)
                        .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(beligeFoto)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtConfirmarPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addComponent(bRegistra)
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(130, Short.MAX_VALUE)
                .addComponent(panelArtistas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(118, 118, 118))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(79, Short.MAX_VALUE)
                .addComponent(panelArtistas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
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
        Forms.cargarForm(new FrmInicioSesion(), this);
    }//GEN-LAST:event_bAlbumesActionPerformed

    private void beligeFotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_beligeFotoActionPerformed
        try {
            this.elegirFotoPerfil();
        } catch (IOException ex) {
            Logger.getLogger(FrmRegistrarse.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_beligeFotoActionPerformed

    private void bRegistraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bRegistraActionPerformed
        if (!this.validarTodosLosCampos()) {
            return;
        }

        usuario = new UsuarioDTO();
        usuario.setNombreUsuario(txtUsuario.getText());
        usuario.setCorreo(txtCorreo.getText());
        usuario.setContrasena(txtPassword.getText());
        usuario.setFotoPerfil(imagenPerfil);

        try {
            this.usuariobo.registrarUsuario(usuario);
            JOptionPane.showMessageDialog(this, "Se ha registrado su usuario!.", "EXITO", JOptionPane.INFORMATION_MESSAGE);
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, "Ocurrio un error durante el proceso!.", "Advertencia", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_bRegistraActionPerformed

    private void elegirFotoPerfil() throws IOException {
        String path = this.obtenerPath();

        File file = new File(path);
        this.imagenPerfil = GestorImagenesMongo.crearImagen("perfil", file);
        Icon imageIcon = GestorImagenesMongo
                .getImageIcon(
                        this.imagenPerfil,
                        GestorImagenesMongo.SizeImage.MEDIUM
                );
        lblImage.setIcon(imageIcon);

    }

    private String obtenerPath() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar la imagen de perfil.");

        FileNameExtensionFilter imageFilter
                = new FileNameExtensionFilter(
                        "Image files",
                        "jpg", "jpeg", "png"
                );
        fileChooser.setFileFilter(imageFilter);

        int userSelection = fileChooser.showOpenDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {

            return fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return null;
        }
    }

    private boolean validarTodosLosCampos() {
        if (this.imagenPerfil == null) {
            JOptionPane.showMessageDialog(this, "Foto de perfil.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return validarCorreo() && validarUsername() && validarPassword();
    }

    private boolean validarUsername() {

        String username = txtUsuario.getText().trim();

        if (!ValidadorFrames.isValidText(username)) {
            JOptionPane.showMessageDialog(this, "Nombre de usuario es obligatorio.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return false;
        }

//        Usuario usuario = this.unitOfWork
//                .usuariosDAO()
//                .consultarPorUsername(username);
        if (usuario != null) {
            JOptionPane.showMessageDialog(this, "Nombre de usuario existente.", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }

    private boolean validarCorreo() {
        String correo = txtCorreo.getText().trim();

        if (!ValidadorFrames.isValidEmail(correo)) {
            JOptionPane.showMessageDialog(this, "Correo inválido.", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!ValidadorFrames.isValidText(correo)) {
            JOptionPane.showMessageDialog(this, "Correo es obligatorio.", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (usuario != null) {
            JOptionPane.showMessageDialog(this, "Correo existente.", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validarPassword() {
        String password = new String(txtPassword.getPassword());
        String confirPassword = new String(txtConfirmarPassword.getPassword());

        boolean isVacias = password.isBlank() || confirPassword.isBlank();
        boolean isDiferentes = !password.matches(confirPassword);

        if (isVacias || isDiferentes) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden.", "Advertencia", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        if (!ValidadorFrames.isValidPassword(password)) {
            JOptionPane.showMessageDialog(this, "La contraseña no es lo suficiente segura.\n"
                    + "Contraseña segura: Mayúscula, minúscula,\n "
                    + "número, carácter especial, 8+ caracteres.", "Advertencia", JOptionPane.ERROR_MESSAGE);

            return true;
        }
        return true;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bAlbumes;
    private javax.swing.JButton bHome;
    private javax.swing.JButton bRegistra;
    private javax.swing.JButton beligeFoto;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblImage;
    private javax.swing.JPanel panelArtistas;
    private javax.swing.JPasswordField txtConfirmarPassword;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
