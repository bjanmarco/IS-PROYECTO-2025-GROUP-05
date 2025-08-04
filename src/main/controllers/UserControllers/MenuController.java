package controllers.UserControllers;

import models.Sesion;
import models.Usuario;
import views.Usuario.MenuView;
import models.MenuSemanalModel;

import java.util.Map;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

import javax.swing.JOptionPane;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

public class MenuController {
    public MenuView view;
    private Usuario usuario;

    public MenuController() {
        this.usuario = Sesion.getUsuarioActual();
        this.view = new MenuView();
        this.view.setController(this);
        this.view.setSaldo(usuario.getSaldo());

        resetearReservasSiEsNecesario(); 
        cargarPrecioPorRol();
        initController();
        mostrarMenus("Desayuno");
        this.view.setVisible(true);
    }

    public MenuController(MenuView view) {
        this.usuario = Sesion.getUsuarioActual();
        this.view = view;
        this.view.setController(this);
        this.view.setSaldo(usuario.getSaldo());

        resetearReservasSiEsNecesario(); 
        cargarPrecioPorRol();
        initController();
        mostrarMenus("Desayuno");
    }

    private void initController() {
        view.setDesayunoListener(_ -> mostrarMenus("Desayuno"));
        view.setAlmuerzoListener(_ -> mostrarMenus("Almuerzo"));
        view.setVolverListener(_ -> {
            new DashboardUserController();
            view.dispose();
        });
    }

    private void cargarPrecioPorRol() {
        String cedula = usuario.getCedula();
        String rol = obtenerRolDesdeArchivo(cedula);
        double cbb = obtenerCBBDesdeArchivo();
        double precio = calcularPrecioPorRol(rol, cbb);
        view.setPrecioBandeja(precio);
    }

public void mostrarMenus(String tipoComida) {
    LocalDate hoy = LocalDate.now();
    LocalTime ahora = LocalTime.now();

    
    if (hoy.getDayOfWeek() == DayOfWeek.SATURDAY || hoy.getDayOfWeek() == DayOfWeek.SUNDAY) {
        view.showMealMenu(tipoComida, new HashMap<>());
        return;
    }

    
    String diaMostrado = hoy.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
    diaMostrado = diaMostrado.substring(0, 1).toUpperCase() + diaMostrado.substring(1).toLowerCase();

    MenuSemanalModel model = new MenuSemanalModel();
    Map<String, Map<String, String>> menusPorDia = new HashMap<>();

    Map<String, String> menu = model.obtenerMenu(diaMostrado, tipoComida);
    if (menu != null) {
        boolean habilitado = true;

        if (tipoComida.equalsIgnoreCase("Desayuno") && ahora.isAfter(LocalTime.of(10, 0))) {
            habilitado = false;
        } else if (tipoComida.equalsIgnoreCase("Almuerzo") && ahora.isAfter(LocalTime.of(15, 0))) {
            habilitado = false;
        }

        menu.put("habilitado", String.valueOf(habilitado));
        menusPorDia.put(diaMostrado.toUpperCase(), menu);
    }

    view.showMealMenu(tipoComida, menusPorDia);
}

    private String obtenerRolDesdeArchivo(String cedula) {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/data/usuarios.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 5 && partes[0].trim().equals(cedula.trim())) {
                    return partes[4].trim().toLowerCase();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "trabajador";
    }

    private double obtenerCBBDesdeArchivo() {
        String periodoActual = java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("MM-yyyy"));
        double ccb = 0.0;

        try (BufferedReader br = new BufferedReader(new FileReader("src/main/data/costos.txt"))) {
            String linea;
            String ultimaCoincidente = null;

            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");

                
                if (partes.length >= 2 && partes[0].trim().equals(periodoActual)) {
                    ultimaCoincidente = linea;
                }
            }

            if (ultimaCoincidente != null) {
                String[] partes = ultimaCoincidente.split(",");
                return Double.parseDouble(partes[1].trim());
            }

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return ccb; 
    }

    private double calcularPrecioPorRol(String rol, double cbb) {
        return switch (rol) {
            case "estudiante" -> cbb * 0.25;
            case "profesor" -> cbb * 0.80;
            case "trabajador" -> cbb * 1.00;
            default -> cbb;
        };
    }

    public void refrescarSaldo() {
        this.usuario = Sesion.getUsuarioActual();
        view.setSaldo(usuario.getSaldo());
    }

    public void reservarComida(String tipoComida) {
        try {
            String rutaArchivo = "src/main/data/usuarios.txt";
            List<String> lineas = Files.readAllLines(Paths.get(rutaArchivo));
            String cedulaUsuario = usuario.getCedula();

            List<String> lineasActualizadas = lineas.stream().map(linea -> {
                String[] partes = linea.split(",");
                if (partes.length >= 8 && partes[0].trim().equals(cedulaUsuario)) {
                    if ("Desayuno".equalsIgnoreCase(tipoComida)) {
                        partes[6] = "true";
                        usuario.setDesayuno(true);
                    } else if ("Almuerzo".equalsIgnoreCase(tipoComida)) {
                        partes[7] = "true";
                        usuario.setAlmuerzo(true);
                    }
                    return String.join(",", partes);
                }
                return linea;
            }).collect(Collectors.toList());

            Files.write(Paths.get(rutaArchivo), lineasActualizadas, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);

            JOptionPane.showMessageDialog(view, "Reserva exitosa", "Confirmación", JOptionPane.INFORMATION_MESSAGE);

            refrescarSaldo();
            mostrarMenus(tipoComida);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "Error al reservar. Intente nuevamente.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

private void resetearReservasSiEsNecesario() {
    LocalTime ahora = LocalTime.now();
    if (ahora.isBefore(LocalTime.of(18, 0))) return;

    try {
        String ruta = "src/main/data/usuarios.txt";
        List<String> lineas = Files.readAllLines(Paths.get(ruta));
        java.util.concurrent.atomic.AtomicBoolean huboCambios = new java.util.concurrent.atomic.AtomicBoolean(false);

        List<String> actualizadas = lineas.stream().map(linea -> {
            String[] partes = linea.split(",");
            if (partes.length >= 8) {
                if (partes[6].equalsIgnoreCase("true") || partes[7].equalsIgnoreCase("true")) {
                    partes[6] = "false";
                    partes[7] = "false";
                    huboCambios.set(true);
                    if (usuario.getCedula().equals(partes[0])) {
                        usuario.setDesayuno(false);
                        usuario.setAlmuerzo(false);
                    }
                }
                return String.join(",", partes);
            }
            return linea;
        }).collect(Collectors.toList());

        if (huboCambios.get()) {
            Files.write(Paths.get(ruta), actualizadas, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
}

}
