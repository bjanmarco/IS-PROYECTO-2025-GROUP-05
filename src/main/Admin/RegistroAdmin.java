package Admin;
import javax.swing.*;

import Components.RoundedBorder;
import Components.RoundedPanel;
import Sistema.Sistema;
import Components.BotonAzul;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class RegistroAdmin extends JFrame {

    private JTextField credencialField;
    private JPasswordField passwordField;
    private JPasswordField confirmarPasswordField;
    private JButton registerButton;
    private JButton volverLoginButton;
    private JLabel titleLabel, credencialLabel, passwordLabel, confirmarLabel, volverLoginLabel;
    private RoundedPanel formPanel;
    private BotonAzul volverMainButton;

    public RegistroAdmin() {
        setTitle("Registro de cuenta - Admin");
        setSize(850, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        getContentPane().setBackground(new Color(36, 136, 242));
        getContentPane().setLayout(null);

        formPanel = new RoundedPanel(20);
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(null);
        getContentPane().add(formPanel);

        titleLabel = new JLabel("Registro Admin");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(titleLabel);

        credencialLabel = new JLabel("Credencial:");
        credencialLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(credencialLabel);

        credencialField = new JTextField();
        credencialField.setBorder(new RoundedBorder(15));
        credencialField.setForeground(Color.BLACK);
        credencialField.setBackground(Color.WHITE);
        credencialField.setOpaque(true);
        credencialField.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(credencialField);

        passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBorder(new RoundedBorder(15));
        passwordField.setForeground(Color.BLACK);
        passwordField.setBackground(Color.WHITE);
        passwordField.setOpaque(true);
        passwordField.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(passwordField);

        confirmarLabel = new JLabel("Confirmar contraseña:");
        confirmarLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(confirmarLabel);

        confirmarPasswordField = new JPasswordField();
        confirmarPasswordField.setBorder(new RoundedBorder(15));
        confirmarPasswordField.setForeground(Color.BLACK);
        confirmarPasswordField.setBackground(Color.WHITE);
        confirmarPasswordField.setOpaque(true);
        confirmarPasswordField.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(confirmarPasswordField);

        registerButton = new BotonAzul("Registrarse", new Dimension(180, 35));
        registerButton.addActionListener(this::onRegister);
        formPanel.add(registerButton);

        volverLoginLabel = new JLabel("¿Ya tienes cuenta? ");
        volverLoginLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(volverLoginLabel);

        volverLoginButton = new JButton("Inicia sesión");
        volverLoginButton.setFont(new Font("Arial", Font.BOLD, 12));
        volverLoginButton.setForeground(new Color(51, 204, 255));
        volverLoginButton.setBorderPainted(false);
        volverLoginButton.setFocusPainted(false);
        volverLoginButton.setContentAreaFilled(false);
        volverLoginButton.addActionListener(this::onVolverLogin);
        formPanel.add(volverLoginButton);

        volverMainButton = new BotonAzul("Volver al Menú Principal", new Dimension(200, 35));
        volverMainButton.addActionListener(e -> volverAlMain());
        formPanel.add(volverMainButton);


        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                updateLayout();
            }
        });

        setVisible(true);
    }

    private void updateLayout() {
        int topMargin = 40;
        int bottomMargin = 60;
        int sideMargin = 40;

        formPanel.setBounds(
            sideMargin,
            topMargin,
            getWidth() - 2 * sideMargin,
            getHeight() - topMargin - bottomMargin
        );

        int formWidth = formPanel.getWidth();
        int formHeight = formPanel.getHeight();

        int labelWidth = 100;
        int fieldWidth = 180;
        int fieldHeight = 30;
        int spacing = 20;

        int totalFormHeight = 30 + spacing + 3 * (fieldHeight + spacing) + 35 + spacing + 25;
        int startY = (formHeight - totalFormHeight) / 2;

        int centerXLabel = (formWidth / 2) - fieldWidth / 2 - labelWidth;
        int centerXField = (formWidth / 2) - fieldWidth / 2;

        titleLabel.setBounds((formWidth - 200) / 2, startY, 200, 30);
        int y = startY + 30 + spacing;

        credencialLabel.setBounds(centerXLabel, y, labelWidth, fieldHeight);
        credencialField.setBounds(centerXField, y, fieldWidth, fieldHeight);
        y += fieldHeight + spacing;

        passwordLabel.setBounds(centerXLabel, y, labelWidth, fieldHeight);
        passwordField.setBounds(centerXField, y, fieldWidth, fieldHeight);
        y += fieldHeight + spacing;

        confirmarLabel.setBounds(centerXLabel - 70, y, labelWidth + 100, fieldHeight);
        confirmarPasswordField.setBounds(centerXField, y, fieldWidth, fieldHeight);
        y += fieldHeight + spacing;

        registerButton.setBounds(centerXField, y, fieldWidth, 35);
        y += 35 + spacing;

        volverLoginLabel.setBounds(centerXLabel + 50, y, 140, 25);
        volverLoginButton.setBounds(centerXField + 30, y, 160, 25);
    
        // Botón "Volver al Menú Principal" abajo a la izquierda
        int bottomY = formHeight - 50; // 50 píxeles desde el borde inferior
        int leftX = 20; // 20 píxeles desde el borde izquierdo
        
        volverMainButton.setBounds(leftX, bottomY, 200, 35);
    }

    private void onRegister(ActionEvent evt) {
        String cedula = credencialField.getText().trim();
        String pass = new String(passwordField.getPassword()).trim();
        String confirm = new String(confirmarPasswordField.getPassword()).trim();

        if (cedula.isEmpty() || pass.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Registro exitoso: guardar en sesión
        Administrador nuevoAdmin = new Administrador(cedula);
        SesionAdmin.iniciarSesion(nuevoAdmin);

        JOptionPane.showMessageDialog(this, "¡Registro exitoso!\nBienvenido Admin: " + cedula);

        guardarAdmin(cedula, pass);
    }

    private void guardarAdmin(String cedula, String contrasena) {
        File archivo = new File("admin/Model/admins.txt");

        try (FileWriter writer = new FileWriter(archivo, true);
             BufferedWriter bw = new BufferedWriter(writer)) {

            bw.write(cedula + "," + contrasena);
            bw.newLine();

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar el admin: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onVolverLogin(ActionEvent evt) {
        new LoginAdmin();
        dispose();
    }

    private void volverAlMain() {
        new Sistema();
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegistroAdmin::new);
    }
}
