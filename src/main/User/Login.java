package User;
import javax.swing.*;

import User.Components.RoundedBorder;
import User.Components.RoundedPanel;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class Login extends JFrame {

    private JTextField credencialField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel titleLabel, credencialLabel, passwordLabel, noCuentaLabel;
    private RoundedPanel formPanel;

    public Login() {
        setTitle("Inicio de sesión");
        setSize(850, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Fondo azul
        getContentPane().setBackground(new Color(36, 136, 242));
        getContentPane().setLayout(null);

        // Panel blanco con esquinas redondeadas
        formPanel = new RoundedPanel(20);
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(null);
        getContentPane().add(formPanel);

        // Componentes (declarados como atributos para reposicionar luego)
        titleLabel = new JLabel("Login");
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

        loginButton = new JButton("Iniciar sesión");
        loginButton.setBorder(new RoundedBorder(25));
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(new Color(0, 153, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setContentAreaFilled(true);
        loginButton.setOpaque(true); 
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

        // Layout dinámico
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                updateLayout();
            }
        });

        setVisible(true); // Muy importante para que getWidth() y getHeight() funcionen
    }

    private void updateLayout() {
        // Márgenes exteriores
        int topMargin = 40;
        int bottomMargin = 60;
        int sideMargin = 40;

        // Redimensionar el panel blanco
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

        // Altura total del bloque de elementos
        int totalFormHeight = 30 + spacing + fieldHeight + spacing + fieldHeight + spacing + 35 + spacing + 25;

        // Punto de inicio vertical para centrar
        int startY = (formHeight - totalFormHeight) / 2;

        int centerXLabel = (formWidth / 2) - fieldWidth / 2 - labelWidth;
        int centerXField = (formWidth / 2) - fieldWidth / 2;

        titleLabel.setBounds((formWidth - 100) / 2, startY, 100, 30);
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
    }

    private void onLogin(ActionEvent evt) {
        String cedula = credencialField.getText().trim();
        String contrasena = new String(passwordField.getPassword()).trim();

        if (cedula.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (validarCredenciales(cedula, contrasena)) {
            Usuario usuario = new Usuario(cedula);
            Sesion.iniciarSesion(usuario);

            JOptionPane.showMessageDialog(this, "Inicio de sesión exitoso\nBienvenido: " + cedula);
            
            // Aquí podrías abrir otra ventana (como dashboard o menú)
            // new VentanaPrincipal().setVisible(true);
            // dispose();

        } else {
            JOptionPane.showMessageDialog(this, "Credencial o contraseña incorrecta", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCredenciales(String cedula, String contrasena) {
        File archivo = new File("User/Model/usuarios.txt");

        if (!archivo.exists()) {
            JOptionPane.showMessageDialog(this, "Archivo de usuarios no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
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
            JOptionPane.showMessageDialog(this, "Error leyendo archivo de usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }

    private void onRegister(ActionEvent evt) {
        new Registro();  // Abrimos la ventana de registro
        dispose();       // Cerramos la actual    
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }
}