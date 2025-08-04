package test.controller;

import controllers.UserControllers.MenuController;
import models.Sesion;
import models.Usuario;
import views.Usuario.MenuView;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.util.Map;

public class MenuControllerTest {
    MenuController controller;
    private MenuViewStub viewStub;
    private Usuario usuarioTest;

    
    private static class MenuViewStub extends MenuView {
        public String menuMostrado;
        public String tipoComidaMostrada;
        public Map<String, Map<String, String>> menusMostrados;
        public boolean disposed = false;
        public double saldoMostrado;
        public double precioMostrado;
        public ActionListener desayunoListener;
        public ActionListener almuerzoListener;
        public ActionListener volverListener;

        @Override
        public void showMealMenu(String mealType, Map<String, Map<String, String>> menus) {
            this.tipoComidaMostrada = mealType;
            this.menusMostrados = menus;
        }

        @Override
        public void setSaldo(double saldo) {
            this.saldoMostrado = saldo;
        }

        @Override
        public void setPrecioBandeja(double precio) {
            this.precioMostrado = precio;
        }

        @Override
        public void dispose() {
            this.disposed = true;
        }

        @Override
        public void setDesayunoListener(ActionListener listener) {
            this.desayunoListener = listener;
        }

        @Override
        public void setAlmuerzoListener(ActionListener listener) {
            this.almuerzoListener = listener;
        }

        @Override
        public void setVolverListener(ActionListener listener) {
            this.volverListener = listener;
        }
    }

    @Before
    public void setUp() {
        usuarioTest = new Usuario(
            "623456", 
            150.0, 
            "Nombre Apellido", 
            "estudiante", 
            "foto.jpg", 
            false, 
            false
        );
        Sesion.iniciarSesion(usuarioTest);
        viewStub = new MenuViewStub();
        controller = new MenuController(viewStub);
    }

    @Test
    public void testConstructor_InicializacionCorrecta() {
        assertEquals(150.0, viewStub.saldoMostrado, 0.001);
        assertTrue(viewStub.precioMostrado >= 0);
        assertNotNull(viewStub.desayunoListener);
        assertNotNull(viewStub.almuerzoListener);
        assertNotNull(viewStub.volverListener);
        assertEquals("Desayuno", viewStub.tipoComidaMostrada);
        assertNotNull(viewStub.menusMostrados);
    }

    @Test
    public void testDesayunoListener() {
        viewStub.desayunoListener.actionPerformed(new ActionEvent(new JButton(), 0, ""));
        assertEquals("Desayuno", viewStub.tipoComidaMostrada);
        assertNotNull(viewStub.menusMostrados);
    }

    @Test
    public void testAlmuerzoListener() {
        viewStub.almuerzoListener.actionPerformed(new ActionEvent(new JButton(), 0, ""));
        assertEquals("Almuerzo", viewStub.tipoComidaMostrada);
        assertNotNull(viewStub.menusMostrados);
    }

    @Test
    public void testVolverListener() {
        viewStub.volverListener.actionPerformed(new ActionEvent(new JButton(), 0, ""));
        assertTrue(viewStub.disposed);
    }

    @Test
    public void testRefrescarSaldo() {
        usuarioTest.recargarSaldo(50.0);
        controller.refrescarSaldo();
        assertEquals(200.0, viewStub.saldoMostrado, 0.001);
    }

    @After
    public void tearDown() {
        Sesion.cerrarSesion();
    }
}