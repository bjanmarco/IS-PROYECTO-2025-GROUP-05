package Admin;

import Components.RoundedBorder;
import Components.RoundedPanel;
import Sistema.Sistema;
import Components.BotonAzul;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LoginAdmin extends JFrame {

    private JTextField credencialField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel titleLabel, credencialLabel, passwordLabel, noCuentaLabel;
    private RoundedPanel formPanel;
    private BotonAzul volverMainButton;

    public LoginAdmin() {
        setTitle("Inicio de sesión - Admin");
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

        titleLabel = new JLabel("Login Admin");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
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

        loginButton = new BotonAzul("Iniciar sesión", new Dimension(180, 35));
        loginButton.addActionListener(this::onLogin);
        formPanel.add(loginButton);

        noCuentaLabel = new JLabel("¿No tienes una cuenta?");
        noCuentaLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(noCuentaLabel);

        registerButton = new JButton("Crea tu cuenta aquí");
        registerButton.setFont(new Font("Arial", Font.BOLD, 12));
        registerButton.setForeground(new Color(51, 204, 255));
        registerButton.setBorderPainted(false);
        registerButton.setFocusPainted(false);
        registerButton.setContentAreaFilled(false);
        registerButton.addActionListener(this::onRegister);
        formPanel.add(registerButton);

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

        int totalFormHeight = 30 + spacing + fieldHeight + spacing + fieldHeight + spacing + 35 + spacing + 25;
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

        loginButton.setBounds(centerXField, y, fieldWidth, 35);
        y += 35 + spacing;

        noCuentaLabel.setBounds(centerXLabel, y, 150, 25);
        registerButton.setBounds(centerXField + 30, y, 160, 25);
        
        int bottomY = formHeight - 50; // 50 píxeles desde el borde inferior
        int leftX = 20; // 20 píxeles desde el borde izquierdo
        
        volverMainButton.setBounds(leftX, bottomY, 200, 35);
    }

    private void onLogin(ActionEvent evt) {
        String cedula = credencialField.getText().trim();
        String contrasena = new String(passwordField.getPassword()).trim();

        if (cedula.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (validarCredenciales(cedula, contrasena)) {
            Administrador admin = new Administrador(cedula);
            SesionAdmin.iniciarSesion(admin);

            JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso\nBienvenido Admin: " + cedula);
        } else {
            JOptionPane.showMessageDialog(this, "Credencial o contraseña incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCredenciales(String cedula, String contrasena) {
        File archivo = new File("admin/Model/admins.txt");

        if (!archivo.exists()) {
            JOptionPane.showMessageDialog(this, "Archivo de admins no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 2) {
                    String cedulaArchivo = partes[0].trim();
                    String passArchivo = partes[1].trim();

                    if (cedula.equals(cedulaArchivo) && contrasena.equals(passArchivo)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error leyendo archivo de admins: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }

    private void onRegister(ActionEvent evt) {
        new RegistroAdmin();
        dispose();    
    }

    private void volverAlMain() {
        new Sistema();
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginAdmin::new);
    }
}
