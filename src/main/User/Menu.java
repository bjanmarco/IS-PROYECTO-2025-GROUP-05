package User;

import Components.RoundedPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import Components.BotonAzul;

public class Menu extends JFrame {
    
    private JLabel dateLabel;
    private JLabel titleLabel;
    private JButton breakfastButton;
    private JButton lunchButton;
    private JPanel daysPanel;
    private RoundedPanel formPanel;
    private JPanel buttonPanel; // Ahora es un atributo de clase
    private String currentMealType = "Desayuno";

    public Menu() {
        initComponents();
        setupWindow();
        showMealMenu(currentMealType);
    }

    private void initComponents() {
        setTitle("Sistema de Menú Semanal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Fondo azul
        getContentPane().setBackground(new Color(36, 136, 242));
        getContentPane().setLayout(null);

        // Panel blanco con esquinas redondeadas
        formPanel = new RoundedPanel(20);
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(null);
        formPanel.setBounds(50, 50, 900, 450);
        getContentPane().add(formPanel);

        // Fecha
        dateLabel = new JLabel(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dateLabel.setBounds(30, 20, 150, 20);
        formPanel.add(dateLabel);

        // Título
        titleLabel = new JLabel("Menú de la Semana");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(0, 20, 900, 30);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(titleLabel);

        // Panel de botones (ahora es un atributo de clase)
        buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setBounds(0, 80, 900, 60);

        breakfastButton = new BotonAzul("Desayuno");
        breakfastButton.addActionListener(e -> showMealMenu("Desayuno"));

        lunchButton = new BotonAzul("Almuerzo");
        lunchButton.addActionListener(e -> showMealMenu("Almuerzo"));

        buttonPanel.add(breakfastButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(30, 0)));
        buttonPanel.add(lunchButton);
        formPanel.add(buttonPanel);

        // Panel para los días
        daysPanel = new JPanel(new GridLayout(1, 5, 15, 0));
        daysPanel.setOpaque(false);
        daysPanel.setBounds(50, 160, 800, 250);
        formPanel.add(daysPanel);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                updateLayout();
            }
        });
    }

    private void updateLayout() {
        int margin = 50;
        int width = getWidth() - 2 * margin;
        int height = getHeight() - margin - 100;
        
        // Actualizar tamaño del panel principal
        formPanel.setBounds(margin, margin, width, height);
        
        // Centrar título
        titleLabel.setBounds(0, 20, width, 30);
        
        // Centrar panel de botones
        int buttonPanelWidth = 350; // Ancho total de los botones + espacio
        buttonPanel.setBounds((width - buttonPanelWidth) / 2, 80, buttonPanelWidth, 60);
        
        // Centrar panel de días
        int daysPanelWidth = Math.min(800, width - 100);
        daysPanel.setBounds((width - daysPanelWidth) / 2, 160, daysPanelWidth, 250);
        
        // Ajustar tamaño de las columnas de días si es necesario
        if (daysPanelWidth < 800) {
            daysPanel.setLayout(new GridLayout(1, 5, 10, 0)); // Reducir espacio entre columnas
        }
    }

    private void showMealMenu(String mealType) {
        currentMealType = mealType;
        daysPanel.removeAll();
        
        String[] days = {"LUNES", "MARTES", "MIÉRCOLES", "JUEVES", "VIERNES"};
        
        for (String day : days) {
            daysPanel.add(createDayColumn(day));
        }
        
        daysPanel.revalidate();
        daysPanel.repaint();
    }

    private JPanel createDayColumn(String day) {
        RoundedPanel column = new RoundedPanel(15);
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setBackground(new Color(240, 240, 240));
        column.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Título del día
        JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
        dayLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Items del menú
        JLabel mealLabel = createCenteredLabel(currentMealType + ":<br>Plato Principal");
        JLabel side1Label = createCenteredLabel("• Contorno #1");
        JLabel side2Label = createCenteredLabel("• Contorno #2");
        JLabel drinkLabel = createCenteredLabel("• Bebida");
        JLabel dessertLabel = createCenteredLabel("• Postre");
        
        // Estado
        JLabel statusLabel = createCenteredLabel("Estado: Disponible");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Botón de reserva
        BotonAzul reserveButton = new BotonAzul("Reservar", new Dimension(120, 30));
        reserveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        reserveButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Reserva para " + day.toLowerCase() + " confirmada",
                "Reserva Exitosa",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Añadir componentes
        column.add(dayLabel);
        column.add(Box.createRigidArea(new Dimension(0, 10)));
        column.add(mealLabel);
        column.add(side1Label);
        column.add(side2Label);
        column.add(drinkLabel);
        column.add(dessertLabel);
        column.add(Box.createRigidArea(new Dimension(0, 10)));
        column.add(statusLabel);
        column.add(Box.createRigidArea(new Dimension(0, 15)));
        column.add(reserveButton);
        
        return column;
    }

    private JLabel createCenteredLabel(String text) {
        JLabel label = new JLabel("<html><div style='text-align:center;'>" + text + "</div></html>", SwingConstants.CENTER);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        return label;
    }

    private void setupWindow() {
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 500));
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new Menu();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}