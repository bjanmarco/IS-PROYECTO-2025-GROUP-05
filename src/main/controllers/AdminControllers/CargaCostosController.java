package controllers.AdminControllers;

import models.AdminModel;
import views.Admin.CargaCostosView;
import javax.swing.*;

public class CargaCostosController {
    private final CargaCostosView view;
    private final AdminModel model;

    public CargaCostosController(JFrame parent) {
        this.model = new AdminModel();
        this.view = new CargaCostosView(parent);

        this.view.addGuardarListener(_ -> guardarCostos());

        this.view.setVisible(true);
    }

    private void guardarCostos() {
        String periodo = view.getPeriodo();
        String cfStr = view.getCf();
        String cvStr = view.getCv();
        String nbStr = view.getNb();
        String mermaStr = view.getMerma();

        if (periodo.isEmpty() || cfStr.isEmpty() || cvStr.isEmpty() || nbStr.isEmpty() || mermaStr.isEmpty()) {
            view.mostrarError("Todos los campos deben estar llenos.");
            return;
        }

        if (!periodo.matches("^(0[1-9]|1[0-2])-\\d{4}$")) {
            view.mostrarError("Formato de período inválido. Use MM-YYYY.");
            return;
        }

        double cf, cv, merma;
        int nb;

        try {
            cf = Double.parseDouble(cfStr);
            cv = Double.parseDouble(cvStr);
            merma = Double.parseDouble(mermaStr);
            nb = Integer.parseInt(nbStr);

            if (cf < 0 || cv < 0 || nb <= 0 || merma < 0 || merma > 100) {
                throw new IllegalArgumentException();
            }

        } catch (NumberFormatException ex) {
            view.mostrarError("Ingrese solo números válidos en los campos de costos, merma y bandejas.");
            return;
        } catch (IllegalArgumentException ex) {
            view.mostrarError("Verifique que:\n- Los costos no sean negativos\n- N° de bandejas sea mayor que cero\n- Merma esté entre 0% y 100%");
            return;
        }

        if (model.guardarCostos(periodo, model.calcularCCB(cf, cv, nb, merma))) {
            view.mostrarMensaje("Costos guardados exitosamente.");
            view.dispose(); 
        } else {
            view.mostrarError("Error al guardar los costos.");
        }
    }
}