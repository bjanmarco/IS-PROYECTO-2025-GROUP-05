package test.controller;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import controllers.AdminControllers.DashboardAdminController;
import models.Sesion;
import models.Usuario;
import views.Admin.DashboardAdminView;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import java.time.LocalDateTime;

public class DashboardAdminControllerTest {
    
    private static class TestDashboardAdminView extends DashboardAdminView {
        public String cedulaSet;
        public LocalDateTime fechaSet;
        public boolean vistaCerrada;
        public String mensajeError;
        public ActionListener gestionarMenuListener;
        public ActionListener verificarDesayunoListener;
        public ActionListener cargaCostosListener;
        public ActionListener cerrarSesionListener;
        
        @Override
        public void setCedula(String cedula) {
            this.cedulaSet = cedula;
        }
        
        @Override
        public void updateDate(LocalDateTime fecha) {
            this.fechaSet = fecha;
        }
        
        @Override
        public void mostrarError(String mensaje) {
            this.mensajeError = mensaje;
        }
        
        @Override
        public void dispose() {
            this.vistaCerrada = true;
        }
        
        @Override
        public JButton getGestionarMenuBtn() {
            return new JButton() {
                @Override
                public void addActionListener(ActionListener l) {
                    gestionarMenuListener = l;
                }
            };
        }
        
        @Override
        public JButton getVerificarDesayunoBtn() {
            return new JButton() {
                @Override
                public void addActionListener(ActionListener l) {
                    verificarDesayunoListener = l;
                }
            };
        }
        
        @Override
        public JButton getCargaCostosFijosBtn() {
            return new JButton() {
                @Override
                public void addActionListener(ActionListener l) {
                    cargaCostosListener = l;
                }
            };
        }
        
        @Override
        public JButton getCerrarSesionBtn() {
            return new JButton() {
                @Override
                public void addActionListener(ActionListener l) {
                    cerrarSesionListener = l;
                }
            };
        }
    }
    
    @Before
    public void setUp() {
        Sesion.setUsuarioActual(new Usuario(
            "admin123", 
            0.0, 
            "Admin", 
            "admin", 
            "admin.jpg", 
            false, 
            false
        ));
    }
    
    @After
    public void tearDown() {
        Sesion.cerrarSesion();
    }
    
    @Test
    public void testConstructorConVistaValida() {
        TestDashboardAdminView view = new TestDashboardAdminView();
        new DashboardAdminController(view);
        
        assertEquals("admin123", view.cedulaSet);
        assertNotNull(view.fechaSet);
        assertNull(view.mensajeError);
        assertFalse(view.vistaCerrada);
        
        
        assertNotNull(view.gestionarMenuListener);
        assertNotNull(view.verificarDesayunoListener);
        assertNotNull(view.cargaCostosListener);
        assertNotNull(view.cerrarSesionListener);
    }
    
    @Test
    public void testConstructorConVistaNull() {
        try {
            new DashboardAdminController(null);
            fail("Debería haber lanzado NullPointerException");
        } catch (NullPointerException e) {
            assertTrue(e.getMessage().contains("view"));
        }
    }
    
    @Test
    public void testConstructorSinSesion() {
        Sesion.cerrarSesion();
        TestDashboardAdminView view = new TestDashboardAdminView();
        new DashboardAdminController(view);
        
        assertNull(view.cedulaSet);
        assertNotNull(view.mensajeError);
        assertEquals("No hay una sesión activa.", view.mensajeError);
        assertTrue(view.vistaCerrada);
    }

    
    @Test
    public void testCerrarSesion() {
        TestDashboardAdminView view = new TestDashboardAdminView();
        DashboardAdminController controller = new DashboardAdminController(view);
        
        view.cerrarSesionListener.actionPerformed(null);
        
        assertNull(Sesion.getUsuarioActual());
        assertTrue(view.vistaCerrada);
    }
    
    @Test
    public void testConstructorDefault() {
        DashboardAdminController controller = new DashboardAdminController();
        
        assertTrue(true);
    }
}