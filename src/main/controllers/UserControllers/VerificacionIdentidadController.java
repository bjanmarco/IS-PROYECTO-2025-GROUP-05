package controllers.UserControllers;

import models.Usuario;
import views.Usuario.VerificacionIdentidadView;


import javax.swing.*;

import controllers.AdminControllers.DashboardAdminController;

import java.io.*;
import java.util.Locale;

public class VerificacionIdentidadController {
    private final VerificacionIdentidadView view;
    private final JFrame parentView; 
    private File archivoSeleccionado;

    public VerificacionIdentidadController(JFrame parent) {
        this.parentView = parent;
        this.view = new VerificacionIdentidadView(parent);

        view.addSeleccionarArchivoListener(_ -> seleccionarArchivo());
        view.addVerificarListener(_ -> verificar());

        view.setVisible(true);
    }

private void seleccionarArchivo() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Selecciona una imagen");
    fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Imágenes", "jpg", "jpeg", "png"));

    int result = fileChooser.showOpenDialog(view);
    if (result == JFileChooser.APPROVE_OPTION) {
        File archivo = fileChooser.getSelectedFile();

        try {
            
            if (javax.imageio.ImageIO.read(archivo) == null) {
                view.mostrarError("El archivo seleccionado no es una imagen válida.");
                return;
            }
            archivoSeleccionado = archivo;
        } catch (IOException e) {
            view.mostrarError("Error al verificar la imagen.");
        }
    }
}

private void actualizarAccesoUsuario(String cedula, String tipo) {
    File archivo = new File("src/main/data/usuarios.txt");
    StringBuilder contenidoActualizado = new StringBuilder();

    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] partes = linea.split(",");
            if (partes[0].equals(cedula)) {
                if (tipo.equals("desayuno")) {
                    partes[6] = "false";
                } else if (tipo.equals("almuerzo")) {
                    partes[7] = "false";
                }
                linea = String.join(",", partes);
            }
            contenidoActualizado.append(linea).append(System.lineSeparator());
        }
    } catch (IOException e) {
        e.printStackTrace();
        return;
    }

    try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
        bw.write(contenidoActualizado.toString());
    } catch (IOException e) {
        e.printStackTrace();
    }
}


private void verificar() {
    if (archivoSeleccionado == null) {
        view.mostrarError("Debe seleccionar un archivo.");
        return;
    }

    String archivoSubido = archivoSeleccionado.getName();

    try (BufferedReader reader = new BufferedReader(new FileReader("src/main/data/usuarios.txt"))) {
        String linea;
        while ((linea = reader.readLine()) != null) {
            String[] partes = linea.split(",");
            if (partes.length >= 6) {
                String cedula = partes[0].trim();
                double saldo = Double.parseDouble(partes[2].trim());
                String nombreApellido = partes[3].trim();
                String rol = partes[4].trim().toLowerCase();
                String archivoGuardado = partes[5].trim();
                boolean desayuno = Boolean.parseBoolean(partes[6].trim());
                boolean almuerzo = Boolean.parseBoolean(partes[7].trim());

                if (archivoSubido.equals(archivoGuardado)) {
                    double cbb = obtenerCBBDesdeArchivo();
                    double costo = switch (rol) {
                        case "estudiante" -> cbb * 0.25;
                        case "profesor" -> cbb * 0.80;
                        default -> cbb;
                    };
                    
                    Usuario usuario = new Usuario(cedula, saldo, nombreApellido, rol, archivoGuardado, desayuno, almuerzo);

                    java.time.LocalTime ahora = java.time.LocalTime.now();
                    boolean dentroHorarioDesayuno = ahora.isAfter(java.time.LocalTime.of(5, 59)) && ahora.isBefore(java.time.LocalTime.of(10, 1));
                    boolean dentroHorarioAlmuerzo = ahora.isAfter(java.time.LocalTime.of(10, 59)) && ahora.isBefore(java.time.LocalTime.of(15, 1));

                    if (dentroHorarioDesayuno && desayuno) {
                        if (saldo >= costo) {
                        double nuevoSaldo = saldo - costo;
                        actualizarSaldoEnArchivo(cedula, nuevoSaldo);
                        view.mostrarMensaje("Acceso exitoso. Se descontaron " + String.format(Locale.US, "%.2f", costo) + "Bs.");
                        } else {
                            view.mostrarError("Saldo insuficiente para completar la operación.");
                            return;
                        }
                        actualizarAccesoUsuario(cedula, "desayuno");

                    } else if (dentroHorarioAlmuerzo && almuerzo) {
                        if (saldo >= costo) {
                        double nuevoSaldo = saldo - costo;
                        actualizarSaldoEnArchivo(cedula, nuevoSaldo);
                        view.mostrarMensaje("Acceso exitoso. Se descontaron " + String.format(Locale.US, "%.2f", costo) + "Bs.");
                        } else {
                            view.mostrarError("Saldo insuficiente para completar la operación.");
                            return;
                        }
                        actualizarAccesoUsuario(cedula, "almuerzo");

                    } else if (dentroHorarioAlmuerzo && !almuerzo) {
                        view.mostrarError("El usuario no ha reservado bandeja de almuerzo o ya fue escaneado.");
                        return;

                    } else if (dentroHorarioDesayuno && !desayuno) {
                        view.mostrarError("El usuario no ha reservado bandeja de desayuno o ya fue escaneado.");
                        return;

                    }else {
                        view.mostrarError("El escaneo no está habilitado, no hay turnos en este horario.");
                        return;
                    }

                    return;
                }
            }
        }
        view.mostrarError("Usuario no encontrado en la base de datos.");
    } catch (Exception e) {
        e.printStackTrace();
        view.mostrarError("Error al verificar identidad.");
    }
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

private void actualizarSaldoEnArchivo(String cedula, double nuevoSaldo) {
    File archivo = new File("src/main/data/usuarios.txt");
    StringBuilder contenidoActualizado = new StringBuilder();

    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] partes = linea.split(",");
            if (partes[0].equals(cedula)) {
                partes[2] = String.format(Locale.US, "%.2f", nuevoSaldo);
                linea = String.join(",", partes);
            }
            contenidoActualizado.append(linea).append(System.lineSeparator());
        }
    } catch (IOException e) {
        e.printStackTrace();
        return;
    }

    
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo))) {
        bw.write(contenidoActualizado.toString());
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}
