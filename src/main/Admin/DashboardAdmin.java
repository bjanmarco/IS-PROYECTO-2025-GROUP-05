package Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashboardAdmin extends JFrame {
    private JPanel formPanel;
    private JLabel dateLabel;
    private JLabel profileIcon;
    private JLabel cedulaLabel;
    private JLabel rolLabel;
    private JButton consultarInsumosBtn;
    private JButton gestionarMenuBtn;
    private JButton generarReporteBtn;
    private JButton cargaCostosFijosBtn;
    private JButton cerrarSesionBtn;

    public DashboardAdmin() {
        setTitle("Panel de Administración");
        setSize(850, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Fondo azul degradado
        getContentPane().setBackground(new Color(36, 136, 242));
        getContentPane().setLayout(null);

        // Panel blanco con esquinas redondeadas
        formPanel = new RoundedPanel(20);
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(null);
        getContentPane().add(formPanel);

        // Fecha actual (esquina superior izquierda)
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        dateLabel = new JLabel(now.format(formatter));
        dateLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dateLabel.setForeground(Color.BLACK);
        formPanel.add(dateLabel);

        // Icono de perfil (círculo gris)
        profileIcon = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Círculo de fondo
                g2.setColor(new Color(169, 169, 169));
                g2.fillOval(0, 0, 100, 100);
                
                // Cabeza del icono
                g2.setColor(Color.WHITE);
                g2.fillOval(30, 20, 40, 40);
                
                // Cuerpo del icono
                g2.fillOval(15, 60, 70, 50);
                
                g2.dispose();
            }
        };
        formPanel.add(profileIcon);

        // Información del administrador
        cedulaLabel = new JLabel("V-12345678");
        cedulaLabel.setFont(new Font("Arial", Font.BOLD, 18));
        cedulaLabel.setForeground(Color.BLACK);
        cedulaLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(cedulaLabel);

        rolLabel = new JLabel("Administrador");
        rolLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        rolLabel.setForeground(new Color(102, 102, 102));
        rolLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(rolLabel);

        // Botones principales
        consultarInsumosBtn = createStyledButton("Consultar Insumos");
        gestionarMenuBtn = createStyledButton("Gestionar Menú");
        generarReporteBtn = createStyledButton("Generar Reporte");
        cargaCostosFijosBtn = createStyledButton("Carga de Costos Fijos");

        formPanel.add(consultarInsumosBtn);
        formPanel.add(gestionarMenuBtn);
        formPanel.add(generarReporteBtn);
        formPanel.add(cargaCostosFijosBtn);

        // Botón cerrar sesión
        cerrarSesionBtn = new JButton("Cerrar Sesión");
        cerrarSesionBtn.setFont(new Font("Arial", Font.BOLD, 14));
        cerrarSesionBtn.setBackground(new Color(0, 153, 255));
        cerrarSesionBtn.setForeground(Color.WHITE);
        cerrarSesionBtn.setBorder(BorderFactory.createEmptyBorder());
        cerrarSesionBtn.setFocusPainted(false);
        cerrarSesionBtn.setContentAreaFilled(true);
        cerrarSesionBtn.setOpaque(true);
        // Agregar esquinas redondeadas
        cerrarSesionBtn.setBorder(new RoundedBorder(25));
        formPanel.add(cerrarSesionBtn);

        // Layout dinámico
        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                updateLayout();
            }
        });

        // Llamar updateLayout inicial
        SwingUtilities.invokeLater(this::updateLayout);

        setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(0, 153, 255));
        button.setForeground(Color.WHITE);
        button.setBorder(new RoundedBorder(25));
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        
        // Efecto hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(0, 128, 230));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(0, 153, 255));
            }
        });
        
        return button;
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

        // Fecha en esquina superior izquierda
        dateLabel.setBounds(20, 20, 150, 20);

        // Perfil centrado en la mitad izquierda
        int profileX = (formWidth / 2 - 100) / 2;
        int profileY = (formHeight - 100) / 2 - 30;
        profileIcon.setBounds(profileX, profileY, 100, 100);

        // Información del usuario debajo del perfil
        int infoY = profileY + 110;
        cedulaLabel.setBounds(profileX - 25, infoY, 150, 25);
        rolLabel.setBounds(profileX - 25, infoY + 25, 150, 20);

        // Botones en la mitad derecha
        int buttonWidth = 200;
        int buttonHeight = 40;
        int buttonSpacing = 20;
        int buttonsStartX = formWidth / 2 + 50;
        int buttonsStartY = profileY - 20;

        consultarInsumosBtn.setBounds(buttonsStartX, buttonsStartY, buttonWidth, buttonHeight);
        gestionarMenuBtn.setBounds(buttonsStartX, buttonsStartY + buttonHeight + buttonSpacing, buttonWidth, buttonHeight);
        generarReporteBtn.setBounds(buttonsStartX, buttonsStartY + 2 * (buttonHeight + buttonSpacing), buttonWidth, buttonHeight);
        cargaCostosFijosBtn.setBounds(buttonsStartX, buttonsStartY + 3 * (buttonHeight + buttonSpacing), buttonWidth, buttonHeight);

        // Botón cerrar sesión en esquina inferior izquierda
        cerrarSesionBtn.setBounds(20, formHeight - 50, 150, 35);
    }

    // Clase interna para panel con esquinas redondeadas
    private class RoundedPanel extends JPanel {
        private final int radius;

        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
        }
    }

    // Clase interna para bordes redondeados
    private class RoundedBorder implements javax.swing.border.Border {
        private int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(5, 10, 5, 10);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 153, 255));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DashboardAdmin::new);
    }
}