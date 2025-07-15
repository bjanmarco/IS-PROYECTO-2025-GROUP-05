package Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class LoginAdmin extends JFrame {
    private JTextField credencialField;
    private JPasswordField passwordField;
    private JButton loginButton, volverButton;

    public LoginAdmin() {
        setTitle("Login Administrador");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(5, 1));

        credencialField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Iniciar sesión");
        volverButton = new JButton("Volver");

        add(new JLabel("Cédula"));
        add(credencialField);
        add(new JLabel("Contraseña"));
        add(passwordField);
        add(loginButton);
        add(volverButton);

        loginButton.addActionListener(this::onLogin);
        volverButton.addActionListener(e -> {
            dispose();
        });

        setVisible(true);
    }

    private void onLogin(ActionEvent evt) {
        String cedula = credencialField.getText().trim();
        String contrasena = new String(passwordField.getPassword()).trim();

        if (cedula.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Campos vacíos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (validarCredenciales(cedula, contrasena)) {
            Administrador admin = new Administrador(cedula);
            SesionAdmin.iniciarSesion(admin);
            JOptionPane.showMessageDialog(this, "Bienvenido administrador: " + cedula);
            // Aquí puedes abrir otra ventana admin
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validarCredenciales(String cedula, String contrasena) {
        File archivo = new File("admin/Model/admins.txt");
        if (!archivo.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 2 && partes[0].trim().equals(cedula) && partes[1].trim().equals(contrasena)) {
                    return true;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error leyendo admins.txt", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginAdmin::new);
    }
}