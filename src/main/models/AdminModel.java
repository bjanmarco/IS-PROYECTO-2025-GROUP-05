package models;

import java.io.*;

public class AdminModel extends UserModel {

    public static final String ADMIN_PASSWORD = "admin123";
    public final File adminFile;
    public final File costosFile;

    public AdminModel() {
        adminFile = new File("src/main/data/admins.txt");
        costosFile = new File("src/main/data/costos.txt");

        ensureFileExists(adminFile);
        ensureFileExists(costosFile);
    }

    public boolean validateAdminPassword(String inputPassword) {
        return ADMIN_PASSWORD.equals(inputPassword);
    }

    public boolean verificarCredenciales(String credencial, String password) {
        return super.autenticar(credencial, password, adminFile);
    }

    public boolean existeCredencial(String credencial) {
        return super.credencialExiste(credencial, adminFile);
    }

    public boolean guardarAdmin(String credencial, String password) {
        return super.guardarCredencial(credencial, password, adminFile);
    }

    public boolean guardarCostos(String periodo, double ccb) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(costosFile,true))) {
            writer.write(periodo + "," + ccb);
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Error guardando costos: " + e.getMessage());
            return false;
        }
    }

    public double calcularCCB(double cf, double cv, int nb, double merma) {
        if(cv < 0 || cf < 0) {
            System.err.println("Error guardando costos: costos negativos no válidos");
            return 0;
        }
        if (nb == 0) return 0;
        return ((cf + cv) / nb) * (1 +(merma/100.0));
    }
}