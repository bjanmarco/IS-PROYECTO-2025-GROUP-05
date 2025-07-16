import Admin.LoginAdmin;
import User.Login;
import Components.RoundedPanel;
import Components.RoundedBorder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JFrame {
    private RoundedPanel formPanel;
    private JLabel titleLabel;
    private JButton btnAdmin, btnUser;

    public Main() {
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

        titleLabel = new JLabel("Seleccione el tipo de acceso", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        formPanel.add(titleLabel);

        btnAdmin = new JButton("Administrador");
        btnAdmin.setFont(new Font("Arial", Font.BOLD, 16));
        btnAdmin.setBackground(new Color(0, 153, 255));
        btnAdmin.setForeground(Color.WHITE);
        btnAdmin.setBorder(new RoundedBorder(25));
        btnAdmin.setFocusPainted(false);
        btnAdmin.setContentAreaFilled(true);
        btnAdmin.setOpaque(true);
        btnAdmin.addActionListener(e -> abrirLoginAdmin());
        formPanel.add(btnAdmin);

        btnUser = new JButton("Usuario");
        btnUser.setFont(new Font("Arial", Font.BOLD, 16));
        btnUser.setBackground(new Color(0, 153, 255));
        btnUser.setForeground(Color.WHITE);
        btnUser.setBorder(new RoundedBorder(25));
        btnUser.setFocusPainted(false);
        btnUser.setContentAreaFilled(true);
        btnUser.setOpaque(true);
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

        int totalHeight = 30 + spacing + buttonHeight + spacing + buttonHeight;
        int startY = (formHeight - totalHeight) / 2;

        titleLabel.setBounds((formWidth - 400) / 2, startY, 400, 30);
        int y = startY + 30 + spacing;

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
        SwingUtilities.invokeLater(Main::new);
    }
}