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
public class FrmEditarPerfil extends javax.swing.JFrame {

    private Imagen imagenPerfil;
    private UsuarioDTO usuario;
    private IUsuarioBO usuariobo;

    /**
     * Creates new form FrmRegistrarse
     */
    public FrmEditarPerfil(UsuarioDTO usuario1) {
        initComponents();
        this.usuariobo = new UsuarioBO();
        this.usuario = usuario1;
        setLocationRelativeTo(null);
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
        panelArtistas = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        txtCorreo = new javax.swing.JTextField();
        bGuardarCambios = new javax.swing.JButton();
        txtConfirmarPassword = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        lblImage = new javax.swing.JLabel();
        btnCambiarFoto = new javax.swing.JButton();
        lblEditarPerfil = new javax.swing.JLabel();
        bCancelar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        panelArtistas.setBackground(new java.awt.Color(102, 0, 102));
        panelArtistas.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(204, 0, 153)));

        jLabel4.setBackground(new java.awt.Color(0, 0, 0));
        jLabel4.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Correo electrónico:");

        jLabel5.setBackground(new java.awt.Color(0, 0, 0));
        jLabel5.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Contraseña:");

        txtPassword.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
        txtPassword.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)));

        txtCorreo.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(221, 221, 221)));

        bGuardarCambios.setBackground(new java.awt.Color(255, 0, 153));
        bGuardarCambios.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        bGuardarCambios.setForeground(new java.awt.Color(255, 255, 255));
        bGuardarCambios.setText("Guardar Cambios");
        bGuardarCambios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bGuardarCambiosActionPerformed(evt);
            }
        });

        txtConfirmarPassword.setFont(new java.awt.Font("Georgia", 3, 18)); // NOI18N
        txtConfirmarPassword.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)));

        jLabel6.setBackground(new java.awt.Color(0, 0, 0));
        jLabel6.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Confirmar contraseña:");

        txtUsuario.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(221, 221, 221)));

        jLabel7.setBackground(new java.awt.Color(0, 0, 0));
        jLabel7.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Nombre de usuario:");

        lblImage.setBackground(new java.awt.Color(0, 0, 0));
        lblImage.setForeground(new java.awt.Color(255, 255, 255));
        lblImage.setBorder(new javax.swing.border.MatteBorder(null));

        btnCambiarFoto.setBackground(new java.awt.Color(255, 0, 153));
        btnCambiarFoto.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        btnCambiarFoto.setForeground(new java.awt.Color(255, 255, 255));
        btnCambiarFoto.setText("Cambiar foto perfil");
        btnCambiarFoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambiarFotoActionPerformed(evt);
            }
        });

        lblEditarPerfil.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblEditarPerfil.setForeground(new java.awt.Color(255, 255, 255));
        lblEditarPerfil.setText("Editar Perfil");

        bCancelar.setBackground(new java.awt.Color(255, 0, 153));
        bCancelar.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        bCancelar.setForeground(new java.awt.Color(255, 255, 255));
        bCancelar.setText("Cancelar");
        bCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bCancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelArtistasLayout = new javax.swing.GroupLayout(panelArtistas);
        panelArtistas.setLayout(panelArtistasLayout);
        panelArtistasLayout.setHorizontalGroup(
            panelArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 157, Short.MAX_VALUE)
                        .addGroup(panelArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelArtistasLayout.createSequentialGroup()
                                .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(144, 144, 144))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelArtistasLayout.createSequentialGroup()
                                .addComponent(btnCambiarFoto)
                                .addGap(128, 128, 128))))
                    .addGroup(panelArtistasLayout.createSequentialGroup()
                        .addGroup(panelArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblEditarPerfil)
                            .addGroup(panelArtistasLayout.createSequentialGroup()
                                .addComponent(bGuardarCambios, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(bCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(191, 191, 191))))
        );
        panelArtistasLayout.setVerticalGroup(
            panelArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelArtistasLayout.createSequentialGroup()
                .addGroup(panelArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelArtistasLayout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(lblEditarPerfil)
                        .addGap(48, 48, 48)
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
                        .addGap(198, 198, 198)
                        .addComponent(lblImage, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCambiarFoto)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtConfirmarPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(panelArtistasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bGuardarCambios)
                    .addComponent(bCancelar))
                .addContainerGap(33, Short.MAX_VALUE))
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
                .addContainerGap(60, Short.MAX_VALUE)
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

    private void btnCambiarFotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambiarFotoActionPerformed
        try {
            this.elegirFotoPerfil();
        } catch (IOException ex) {
            Logger.getLogger(FrmEditarPerfil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnCambiarFotoActionPerformed

    private void bGuardarCambiosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bGuardarCambiosActionPerformed
        if (!this.validarTodosLosCampos()) {
            return;
        }

        // Actualizar los datos del usuario con la información ingresada
        usuario.setNombreUsuario(txtUsuario.getText());
        usuario.setCorreo(txtCorreo.getText());

        // Actualizar la contraseña solo si el campo no está vacío
        String nuevaContrasena = new String(txtPassword.getPassword()).trim();
        if (!nuevaContrasena.isEmpty()) {
            usuario.setContrasena(nuevaContrasena);
        }

        usuario.setFotoPerfil(imagenPerfil);

        try {
            this.usuariobo.actualizarUsuario(usuario); // Método para actualizar usuario
            JOptionPane.showMessageDialog(this, "Se han guardado los cambios!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            Forms.cargarForm(new FrmPerfil(usuario), this); // Regresar a FrmPerfil
        } catch (NegocioException ex) {
            JOptionPane.showMessageDialog(this, "Ocurrió un error al guardar los cambios!", "Error", JOptionPane.ERROR_MESSAGE);
        }


    }//GEN-LAST:event_bGuardarCambiosActionPerformed

    private void bCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bCancelarActionPerformed
        Forms.cargarForm(new FrmPerfil(usuario), this);
    }//GEN-LAST:event_bCancelarActionPerformed

    private void elegirFotoPerfil() throws IOException {
        String path = this.obtenerPath();

        if (path != null) { // Si el usuario seleccionó una imagen válida
            File file = new File(path);
            this.imagenPerfil = GestorImagenesMongo.crearImagen("perfil", file); // Guardar la imagen en la variable
            Icon imageIcon = GestorImagenesMongo.getImageIcon(
                    this.imagenPerfil,
                    GestorImagenesMongo.SizeImage.MEDIUM
            );
            lblImage.setIcon(imageIcon); // Mostrar la nueva imagen en el JLabel
        }
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
        return true;
    }

    private boolean validarCorreo() {
        String correo = txtCorreo.getText().trim();
        if (!ValidadorFrames.isValidEmail(correo)) {
            JOptionPane.showMessageDialog(this, "Correo inválido.", "Advertencia", JOptionPane.ERROR_MESSAGE);
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

    // Método para cargar los datos del usuario en los campos del formulario
    private void cargarDatosUsuario() {
        txtUsuario.setText(usuario.getNombreUsuario());
        txtCorreo.setText(usuario.getCorreo());
        txtPassword.setText("");
        txtConfirmarPassword.setText("");

        // Cargar la foto de perfil si existe
        if (usuario.getFotoPerfil() != null) {
            imagenPerfil = usuario.getFotoPerfil(); // Asignar la imagen a la variable
            Icon imageIcon = GestorImagenesMongo.getImageIcon(
                    imagenPerfil,
                    GestorImagenesMongo.SizeImage.MEDIUM
            );
            lblImage.setIcon(imageIcon); // Mostrar la imagen en el JLabel
        }

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bCancelar;
    private javax.swing.JButton bGuardarCambios;
    private javax.swing.JButton btnCambiarFoto;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblEditarPerfil;
    private javax.swing.JLabel lblImage;
    private javax.swing.JPanel panelArtistas;
    private javax.swing.JPasswordField txtConfirmarPassword;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables
}
