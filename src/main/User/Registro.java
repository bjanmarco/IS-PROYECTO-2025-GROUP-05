package User;
import javax.swing.*;

import User.Components.RoundedBorder;
import User.Components.RoundedPanel;

import java.awt.*;
import java.awt.event.*;

public class Registro extends JFrame {

    private JTextField credencialField;
    private JPasswordField passwordField;
    private JPasswordField confirmarPasswordField;
    private JButton registerButton;
    private JButton volverLoginButton;
    private JLabel titleLabel, credencialLabel, passwordLabel, confirmarLabel, volverLoginLabel;
    private RoundedPanel formPanel;

    public Registro() {
        setTitle("Registro de cuenta");
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

        titleLabel = new JLabel("Registro");
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

        registerButton = new JButton("Registrarse");
        registerButton.setBorder(new RoundedBorder(25));
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setBackground(new Color(0, 153, 255));
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setContentAreaFilled(true);
        registerButton.setOpaque(true);
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

        titleLabel.setBounds((formWidth - 100) / 2, startY, 100, 30);
        int y = startY + 30 + spacing;

        credencialLabel.setBounds(centerXLabel, y, labelWidth, fieldHeight);
        credencialField.setBounds(centerXField, y, fieldWidth, fieldHeight);
        y += fieldHeight + spacing;

        passwordLabel.setBounds(centerXLabel, y, labelWidth, fieldHeight);
        passwordField.setBounds(centerXField, y, fieldWidth, fieldHeight);
        y += fieldHeight + spacing;

        confirmarLabel.setBounds(centerXLabel-70, y, labelWidth + 100, fieldHeight);
        confirmarPasswordField.setBounds(centerXField, y, fieldWidth, fieldHeight);
        y += fieldHeight + spacing;

        registerButton.setBounds(centerXField, y, fieldWidth, 35);
        y += 35 + spacing;

        volverLoginLabel.setBounds(centerXLabel + 50, y, 140, 25); // o ajusta el ancho según convenga
        volverLoginButton.setBounds(centerXField + 30, y, 160, 25);
    }

    private void onRegister(ActionEvent evt) {
        String credencial = credencialField.getText();
        String pass = new String(passwordField.getPassword());
        String confirm = new String(confirmarPasswordField.getPassword());

        if (!pass.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Las contraseñas no coinciden", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Registro exitoso\nCredencial: " + credencial);
        // Aquí podrías redirigir al login real si lo integras
    }

    private void onVolverLogin(ActionEvent evt) {
        new Login();  // Abrimos la ventana de login
        dispose();    // Cerramos la actual
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Registro::new);
    }
}
