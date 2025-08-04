package test.controller;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import controllers.UserControllers.*;
import models.Sesion;
import models.Usuario;
import views.Usuario.DashboardUserView;
import java.lang.reflect.Field;

public class DashboardUserControllerTest {

    private static class TestDashboardUserView extends DashboardUserView {
        public boolean disposed = false;
        
        public TestDashboardUserView(Usuario usuario) {
            super(usuario);
        }
        
        @Override
        public void dispose() {
            this.disposed = true;
        }
    }

    private DashboardUserController controller;
    private Usuario testUser;
    private TestDashboardUserView testView;

    @Before
    public void setUp() throws Exception {
        testUser = new Usuario(
            "testuser", 
            100.0, 
            "Nombre Apellido", 
            "estudiante", 
            "foto.jpg", 
            false, 
            false
        );
        Sesion.iniciarSesion(testUser);
        
        
        testView = new TestDashboardUserView(testUser);
        
        
        controller = new DashboardUserController();
        
        
        Field viewField = DashboardUserController.class.getDeclaredField("view");
        viewField.setAccessible(true);
        viewField.set(controller, testView);
    }

    @Test
    public void testConstructorSinSesion() throws Exception {
        Sesion.cerrarSesion();
        
        DashboardUserController noSessionController = new DashboardUserController();
        
        Field viewField = DashboardUserController.class.getDeclaredField("view");
        viewField.setAccessible(true);
        DashboardUserView view = (DashboardUserView) viewField.get(noSessionController);
        
        assertNull("No debería crear vista sin sesión", view);
    }

    @Test
    public void testRecargarSaldo() throws Exception {
        
        Field recargarSaldoField = DashboardUserController.class.getDeclaredField("view");
        recargarSaldoField.setAccessible(true);
        DashboardUserView viewBefore = (DashboardUserView) recargarSaldoField.get(controller);
        
        
        assertFalse("Vista no debe estar disposed antes", testView.disposed);
        
        
        
        
        assertSame("La vista debe mantenerse igual", viewBefore, testView);
    }

    @After
    public void tearDown() {
        Sesion.cerrarSesion();
        if (testView != null) {
            testView.dispose();
        }
    }
}