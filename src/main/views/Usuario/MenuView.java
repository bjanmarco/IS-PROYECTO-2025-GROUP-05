package views.Usuario;

import views.Components.RoundedPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import views.Components.BotonAzul;
import models.Usuario;
import models.Sesion;

public class MenuView extends JFrame {

    private JLabel dateLabel, titleLabel, credencialInfoLabel, saldoLabel;
    private BotonAzul breakfastButton, lunchButton, volverButton;
    private RoundedPanel daysPanel, formPanel, buttonPanel;
    private String currentMealType = "Desayuno";
    private final String[] days = {"LUNES", "MARTES", "MIÉRCOLES", "JUEVES", "VIERNES"};

    public MenuView() {
        initComponents();
        setupWindow();
    }

    private void initComponents() {
        Usuario usuario = Sesion.getUsuarioActual();
        setTitle("Sistema de Menú Semanal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(new Color(36, 136, 242));
        getContentPane().setLayout(null);

        formPanel = new RoundedPanel(20);
        formPanel.setBackground(Color.WHITE);
        formPanel.setLayout(null);
        getContentPane().add(formPanel);

        dateLabel = new JLabel(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        formPanel.add(dateLabel);

        credencialInfoLabel = new JLabel("Credencial: " + usuario.getCredencial());
        credencialInfoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        credencialInfoLabel.setForeground(Color.BLACK);
        formPanel.add(credencialInfoLabel);

        saldoLabel = new JLabel();
        saldoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        saldoLabel.setForeground(new Color(0, 153, 255));
        formPanel.add(saldoLabel);

        titleLabel = new JLabel("Menú de la Semana");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(titleLabel);

        buttonPanel = new RoundedPanel(15);
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
        formPanel.add(buttonPanel);

        breakfastButton = new BotonAzul("Desayuno", new Dimension(140, 35));
        lunchButton = new BotonAzul("Almuerzo", new Dimension(140, 35));
        buttonPanel.add(breakfastButton);
        buttonPanel.add(lunchButton);

        daysPanel = new RoundedPanel(15);
        daysPanel.setLayout(new GridLayout(1, 5, 15, 0));
        daysPanel.setOpaque(false);
        formPanel.add(daysPanel);

        volverButton = new BotonAzul("Volver al Dashboard", new Dimension(180, 35));
        formPanel.add(volverButton);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                updateLayout();
            }
        });
    }

    public void setSaldo(double saldo) {
        saldoLabel.setText(String.format("Saldo: %.2f$", saldo));
    }

    public void showMealMenu(String mealType) {
        this.currentMealType = mealType;
        daysPanel.removeAll();

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

        JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
        dayLabel.setFont(new Font("Arial", Font.BOLD, 14));
        dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel mealLabel = createCenteredLabel(currentMealType + ":<br>Plato Principal");
        JLabel side1Label = createCenteredLabel("• Contorno #1");
        JLabel side2Label = createCenteredLabel("• Contorno #2");
        JLabel drinkLabel = createCenteredLabel("• Bebida");
        JLabel dessertLabel = createCenteredLabel("• Postre");
        JLabel statusLabel = createCenteredLabel("Estado: Disponible");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));

        BotonAzul reserveButton = new BotonAzul("Reservar", new Dimension(120, 30));
        reserveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        reserveButton.setActionCommand(day);  // Usado por el controlador
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

    public void setDesayunoListener(ActionListener listener) {
        breakfastButton.addActionListener(listener);
    }

    public void setAlmuerzoListener(ActionListener listener) {
        lunchButton.addActionListener(listener);
    }

    public void setVolverListener(ActionListener listener) {
        volverButton.addActionListener(listener);
    }

    private JLabel createCenteredLabel(String text) {
        JLabel label = new JLabel("<html><div style='text-align:center;'>" + text + "</div></html>");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        return label;
    }

    private void updateLayout() {
        int width = getWidth() - 100;
        int height = getHeight() - 100;

        formPanel.setBounds(50, 50, width, height);
        credencialInfoLabel.setBounds(width - 220, 20, 200, 20);
        saldoLabel.setBounds(width - 220, 45, 200, 20);
        titleLabel.setBounds(0, 20, width, 30);
        buttonPanel.setBounds((width - 350) / 2, 80, 350, 50);
        daysPanel.setBounds((width - 800) / 2, 150, 800, 220);
        volverButton.setBounds(width - 200, height - 50, 180, 35);
    }

    private void setupWindow() {
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}