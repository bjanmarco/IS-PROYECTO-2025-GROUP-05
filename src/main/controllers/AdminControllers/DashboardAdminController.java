package controllers.AdminControllers;

import models.Usuario;
import models.Sesion;
import views.Admin.DashboardAdminView;

public class DashboardAdminController {
    private DashboardAdminView view;


    public DashboardAdminController(DashboardAdminView view) {
        this.view = view;
        inicializarVista();
        agregarListeners();
    }

    public DashboardAdminController() {
        this.view = new DashboardAdminView();

        inicializarVista();
        agregarListeners();
    }

    private void inicializarVista() {
        Usuario admin = Sesion.getUsuarioActual();

        if (admin != null) {
            view.setCredencial(admin.getCredencial());
            view.updateDate(java.time.LocalDateTime.now()); 
        } else {
            view.mostrarError("No hay una sesión activa.");
            view.dispose();
        }
    }

    private void agregarListeners() {
        view.getConsultarInsumosBtn().addActionListener(_ -> consultarInsumos());
        view.getGestionarMenuBtn().addActionListener(_ -> gestionarMenu());
        view.getGenerarReporteBtn().addActionListener(_ -> generarReporte());
        view.getCargaCostosFijosBtn().addActionListener(_ -> cargaCostosFijos());
        view.getCerrarSesionBtn().addActionListener(_ -> cerrarSesion());
    }

    private void consultarInsumos() {
    }

    private void gestionarMenu() {
    }

    private void generarReporte() {
    }

    public void cargaCostosFijos() {
        new CargaCostosController(view);
    }

    public void cerrarSesion() {
        Sesion.cerrarSesion();
        view.dispose();
        new LoginAdminController(); 
    }
}