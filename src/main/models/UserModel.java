package models;

import java.io.*;

public class UserModel extends BaseUserModel {

    private final File usuariosFile;
    private final File credencialesFile;


    public UserModel() {
        usuariosFile = new File("src/main/data/usuarios.txt");
        credencialesFile = new File("src/main/data/credenciales.txt");

        ensureFileExists(usuariosFile);
        ensureFileExists(credencialesFile);
    }

    public boolean esCredencialValida(String credencialStr) {
        try {
            int credencial = Integer.parseInt(credencialStr);
            return credencial >= 500_000 && credencial <= 32_000_000;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean credencialExiste(String credencial) {
        return super.credencialExiste(credencial, credencialesFile);
    }

    public boolean usuarioYaRegistrado(String credencial) {
        if (!usuariosFile.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(usuariosFile))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 1 && partes[0].trim().equals(credencial)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean guardarUsuario(String credencial, String contrasena, double saldoInicial) {
        ensureFileExists(usuariosFile);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(usuariosFile, true))) {
            writer.write(credencial + "," + contrasena + "," + saldoInicial);
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean autenticarUsuario(String credencial, String contrasena) {
        return super.autenticar(credencial, contrasena, usuariosFile);
    }

    public double obtenerSaldo(String credencial) {
        if (!usuariosFile.exists()) return 0.0;

        try (BufferedReader reader = new BufferedReader(new FileReader(usuariosFile))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 3 && partes[0].trim().equals(credencial)) {
                    return Double.parseDouble(partes[2].trim());
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return 0.0;
    }
}