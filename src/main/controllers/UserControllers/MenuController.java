package controllers.UserControllers;

import models.Sesion;
import models.Usuario;
import views.Usuario.MenuView;


public class MenuController {
    public MenuView view;
    private Usuario usuario;

    public MenuController() {
        this.usuario = Sesion.getUsuarioActual();
        this.view = new MenuView();
        this.view.setSaldo(usuario.getSaldo());

        initController();
        view.showMealMenu("Desayuno");
    }

    public MenuController(MenuView view) {
        this.usuario = Sesion.getUsuarioActual();
        this.view = view;
        this.view.setSaldo(usuario.getSaldo());
        initController();
        view.showMealMenu("Desayuno");
    }
    public void initController() {
        view.setDesayunoListener(_ -> view.showMealMenu("Desayuno"));
        view.setAlmuerzoListener(_ -> view.showMealMenu("Almuerzo"));

        view.setVolverListener(_ -> {
            new DashboardUserController(); 
            view.dispose();
        });
    }
}
