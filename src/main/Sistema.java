import Admin.LoginAdmin;
import User.Login;
import Components.RoundedPanel;
import Components.BotonAzul;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Sistema extends JFrame {
    private RoundedPanel formPanel;
    private JLabel titleLabel;
    private BotonAzul btnAdmin, btnUser;

    public Sistema() {
        setTitle("Sistema de Acceso");
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

        // Configuración mejorada del título
        titleLabel = new JLabel("<html><div style='text-align: center;'>Sistema de Gestión del<br>Comedor Universitario SGCU</div></html>", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22)); // Reducimos un poco el tamaño de fuente
        formPanel.add(titleLabel);

        btnAdmin = new BotonAzul("Administrador", new Dimension(180, 40));
        btnAdmin.addActionListener(e -> abrirLoginAdmin());
        formPanel.add(btnAdmin);

        btnUser = new BotonAzul("Usuario", new Dimension(180, 40));
        btnUser.addActionListener(e -> abrirLoginUsuario());
        formPanel.add(btnUser);

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

        int spacing = 30;
        int buttonWidth = 180;
        int buttonHeight = 40;

        // Ajustamos el cálculo para el título de dos líneas
        int titleHeight = 60; // Más espacio para dos líneas
        int totalHeight = titleHeight + spacing + buttonHeight + spacing + buttonHeight;
        int startY = (formHeight - totalHeight) / 2;

        // Aumentamos el ancho del título y permitimos más altura
        titleLabel.setBounds((formWidth - 600) / 2, startY, 600, titleHeight);
        int y = startY + titleHeight + spacing;

        btnAdmin.setBounds((formWidth - buttonWidth) / 2, y, buttonWidth, buttonHeight);
        y += buttonHeight + spacing;

        btnUser.setBounds((formWidth - buttonWidth) / 2, y, buttonWidth, buttonHeight);
    }

    private void abrirLoginAdmin() {
        new LoginAdmin();
        dispose();
    }

    private void abrirLoginUsuario() {
        new Login();
        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Sistema::new);
    }
}