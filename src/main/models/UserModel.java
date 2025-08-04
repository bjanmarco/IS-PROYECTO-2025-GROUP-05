package models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserModel extends BaseUserModel {

    private final File usuariosFile;
    private final File cedulaesFile;


    public UserModel() {
        usuariosFile = new File("src/main/data/usuarios.txt");
        cedulaesFile = new File("src/main/data/credenciales.txt");

        ensureFileExists(usuariosFile);
        ensureFileExists(cedulaesFile);
    }

    public boolean esCedulaValida(String cedulaStr) {
        try {
            int cedula = Integer.parseInt(cedulaStr);
            return cedula >= 500_000 && cedula <= 32_000_000;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean cedulaExiste(String cedula) {
        return super.cedulaExiste(cedula, cedulaesFile);
    }

    public boolean usuarioYaRegistrado(String cedula) {
        if (!usuariosFile.exists()) return false;

        try (BufferedReader reader = new BufferedReader(new FileReader(usuariosFile))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 1 && partes[0].trim().equals(cedula)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean guardarUsuario(String cedula, String contrasena, double saldoInicial) {
        String[] datos = obtenerDatosCedula(cedula);
        if (datos == null) return false;

        String nombreApellido = datos[0];
        String rol = datos[1];
        String foto = datos[2];

        
        boolean desayuno = false;
        boolean almuerzo = false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(usuariosFile, true))) {
            writer.write(String.join(",", cedula, contrasena, String.valueOf(saldoInicial), nombreApellido, rol, foto,
                    String.valueOf(desayuno), String.valueOf(almuerzo)));
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarSaldo(String cedula, double nuevoSaldo) {
        List<String> lineasActualizadas = new ArrayList<>();
        boolean actualizado = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(usuariosFile))) {
            String linea;

            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",", 6); 

                if (partes.length < 6) {
                    lineasActualizadas.add(linea); 
                    continue;
                }

            if (partes[0].trim().equals(cedula.trim()) && partes.length >= 8) {
                partes[2] = String.format(Locale.US, "%.2f", nuevoSaldo);
                actualizado = true;
                lineasActualizadas.add(String.join(",", partes));
            } else {
                    lineasActualizadas.add(linea);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(usuariosFile, false))) {
            for (String lineaActualizada : lineasActualizadas) {
                writer.write(lineaActualizada);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return actualizado;
    }

    public String[] obtenerDatosCedula(String cedula) {
            try (BufferedReader reader = new BufferedReader(new FileReader(cedulaesFile))) {
                String linea;
                while ((linea = reader.readLine()) != null) {
                    String[] partes = linea.split(",");
                    if (partes.length >= 4 && partes[0].trim().equals(cedula)) {
                        return new String[]{partes[1].trim(), partes[2].trim(), partes[3].trim()}; 
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
    }

    public boolean autenticarUsuario(String cedula, String contrasena) {
            return super.autenticar(cedula, contrasena, usuariosFile);
    }

    public double obtenerSaldo(String cedula) {
        try (BufferedReader reader = new BufferedReader(new FileReader(usuariosFile))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 3 && partes[0].trim().equals(cedula)) {
                    return Double.parseDouble(partes[2].trim());
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public Usuario obtenerUsuario(String cedula) {
        try (BufferedReader reader = new BufferedReader(new FileReader(usuariosFile))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length >= 8 && partes[0].trim().equals(cedula)) {
                    return new Usuario(
                        partes[0].trim(),
                        Double.parseDouble(partes[2].trim()),
                        partes[3].trim(),
                        partes[4].trim(),
                        partes[5].trim(),
                        Boolean.parseBoolean(partes[6].trim()),
                        Boolean.parseBoolean(partes[7].trim())
                    );
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }
}