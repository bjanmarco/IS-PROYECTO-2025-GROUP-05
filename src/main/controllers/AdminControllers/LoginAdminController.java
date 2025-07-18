package controllers.AdminControllers;

import controllers.AppController;
import models.AdminModel;
import models.Sesion;
import models.Usuario;
import views.Admin.LoginAdminView;

public class LoginAdminController {
    private final LoginAdminView view;
    private final AdminModel model;

    public LoginAdminController() {
        this.view = new LoginAdminView();
        this.model = new AdminModel();
        setupListeners();
    }

    public void setupListeners() {
        view.setLoginListener(_ -> intentarLogin());
        view.setVolverMainListener(_ -> volverAlMenu());
        view.setVolverRegistroListener(_ -> irARegistro());
    }

   public void intentarLogin() {
        String credencial = view.getCredencial();
        String contrasena = view.getPassword();

        if (credencial.isEmpty() || contrasena.isEmpty()) {
            view.mostrarError("Todos los campos son obligatorios");
            return;
        }

        if (!model.usuarioYaRegistrado(credencial)) {
            view.mostrarError("El usuario no está registrado.");
            return;
        }
        
        if (!model.esClaveValida(contrasena)) {
            view.mostrarError("Contraseña debe ser de al menos 6 caracteres.");
            return;
        }

        if (!model.verificarCredenciales(credencial, contrasena)) {
            view.mostrarError("Contraseña incorrecta.");
            return;
        }

        double saldo = model.obtenerSaldo(credencial); 
        Sesion.iniciarSesion(new Usuario(credencial, saldo));

        view.mostrarMensaje("Inicio de sesión exitoso.");
        view.dispose();
        new DashboardAdminController(); 
    }

    public void volverAlMenu() {
        view.dispose();
        new AppController(); 
    }

    public void irARegistro() {
        view.dispose();
        new RegistroAdminController();
    }
}