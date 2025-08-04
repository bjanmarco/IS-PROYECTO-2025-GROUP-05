package models;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AdminModel extends UserModel {

    public static final String ADMIN_PASSWORD = "admin123";
    public final File adminFile;
    public final File costosFile;
    public final File cedulaesFile;

    public AdminModel() {
        adminFile = new File("src/main/data/admins.txt");
        costosFile = new File("src/main/data/costos.txt");
        cedulaesFile = new File("src/main/data/credenciales.txt");

        ensureFileExists(adminFile);
        ensureFileExists(costosFile);
        ensureFileExists(cedulaesFile);
    }

    public boolean validateAdminPassword(String inputPassword) {
        return ADMIN_PASSWORD.equals(inputPassword);
    }

    public boolean verificarCedulaes(String cedula, String password) {
        return super.autenticar(cedula, password, adminFile);
    }

    public boolean cedulaExiste(String cedula) {
        return super.cedulaExiste(cedula, adminFile);
    }

    public boolean guardarAdmin(String cedula, String password) {
        boolean guardadoEnAdmin = super.guardarCedula(cedula, password, adminFile);

        
        UserModel userModel = new UserModel();
        boolean guardadoEnUsuario = userModel.guardarUsuario(cedula, password, 0.0);

        return guardadoEnAdmin && guardadoEnUsuario;
    }

    public boolean cedulaTrabajador(String cedula) {
        try (BufferedReader reader = new BufferedReader(new FileReader(cedulaesFile))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 3 && partes[0].trim().equals(cedula)) {
                    String rol = partes[2].trim().toLowerCase();
                    return rol.equals("trabajador");
                }
            }
        } catch (IOException e) {
            System.err.println("Error al verificar autorización de cedula: " + e.getMessage());
        }
        return false;
    }

    public boolean esPeriodoValido(String periodo) {
        try {
            String fechaCompleta = "01-" + periodo; 
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate fechaPeriodo = LocalDate.parse(fechaCompleta, formatter);
            LocalDate fechaActual = LocalDate.now().withDayOfMonth(1);

            return !fechaPeriodo.isBefore(fechaActual);
        } catch (Exception e) {
            System.err.println("Error validando período: " + e.getMessage());
            return false;
        }
    }

    public boolean existeCostoPeriodo(String periodo) {
        if (!costosFile.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(costosFile))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 1 && partes[0].trim().equals(periodo)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error verificando período existente: " + e.getMessage());
        }
        return false;
    }

    /**
     * Guarda los costos con todos los parámetros de cálculo
     * Formato: periodo,ccb,cf,cv,nb,merma
     */
    public boolean guardarCostos(String periodo, double ccb, double cf, double cv, int nb, double merma) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(costosFile, true))) {
            writer.write(String.format("%s,%.5f,%.2f,%.2f,%d,%.2f", 
                        periodo, ccb, cf, cv, nb, merma));
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error guardando costos: " + e.getMessage());
            return false;
        }
    }

    /**
     * Versión antigua del método (deprecated)
     */
    public boolean guardarCostos(String periodo, double ccb) {
        System.err.println("Advertencia: Usando método guardarCostos deprecated. Usa la versión completa.");
        return false;
    }

    public double calcularCCB(double cf, double cv, int nb, double merma) {
        if (cf < 0 || cv < 0) {
            System.err.println("Error calculando CCB: costos negativos no válidos");
            return 0;
        }
        if (nb == 0) {
            System.err.println("Error calculando CCB: número de bandejas no puede ser cero");
            return 0;
        }
        return ((cf + cv) / nb) * (1 + (merma / 100.0));
    }
}
