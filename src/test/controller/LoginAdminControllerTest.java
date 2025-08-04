package test.controller;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import controllers.AdminControllers.LoginAdminController;
import models.AdminModel;
import models.Sesion;
import models.Usuario;
import views.Admin.LoginAdminView;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;



public class LoginAdminControllerTest {

    private static class TestLoginAdminView extends LoginAdminView {
        public String errorMessage;
        public String successMessage;
        public boolean disposed = false;
        public String cedula = "admin";
        public String password = "password123";
        
        @Override public String getCedula() { return cedula; }
        @Override public String getPassword() { return password; }
        @Override public void mostrarError(String mensaje) { this.errorMessage = mensaje; }
        @Override public void mostrarMensaje(String mensaje) { this.successMessage = mensaje; }
        @Override public void dispose() { this.disposed = true; }
    }

    private static class TestAdminModel extends AdminModel {
        public boolean cedulaExisteResult = true;
        public boolean claveValidaResult = true;
        public boolean verificarCedulaesResult = true;
        
        @Override
        public boolean cedulaExiste(String cedula) {
            return cedulaExisteResult;
        }
        
        @Override
        public boolean esClaveValida(String contrasena) {
            return claveValidaResult;
        }
        
        @Override
        public boolean verificarCedulaes(String cedula, String contrasena) {
            return verificarCedulaesResult;
        }
    }

    private LoginAdminController controller;
    private TestLoginAdminView viewStub;
    private TestAdminModel modelStub;

    @Before
    public void setUp() {
        viewStub = new TestLoginAdminView();
        modelStub = new TestAdminModel();
        
        
        try {
            controller = new LoginAdminController();
            
            
            Field viewField = LoginAdminController.class.getDeclaredField("view");
            viewField.setAccessible(true);
            viewField.set(controller, viewStub);
            
            
            Field modelField = LoginAdminController.class.getDeclaredField("adminModel");
            modelField.setAccessible(true);
            modelField.set(controller, modelStub);
            
        } catch (Exception e) {
            fail("Error configurando la prueba: " + e.getMessage());
        }
        
        Sesion.cerrarSesion();
    }

    @Test
    public void testCamposVacios() {
        viewStub.cedula = "";
        viewStub.password = "";
        
        controller.intentarLogin();
        
        assertEquals("Todos los campos son obligatorios", viewStub.errorMessage);
        assertNull(Sesion.getUsuarioActual());
    }

    @Test
    public void testUsuarioNoRegistrado() {
        modelStub.cedulaExisteResult = false;
        
        controller.intentarLogin();
        
        assertEquals("El usuario no está registrado.", viewStub.errorMessage);
        assertNull(Sesion.getUsuarioActual());
    }

    @Test
    public void testClaveInvalida() {
        modelStub.claveValidaResult = false;
        
        controller.intentarLogin();
        
        assertEquals("Contraseña debe ser de al menos 6 caracteres.", viewStub.errorMessage);
        assertNull(Sesion.getUsuarioActual());
    }

    @Test
    public void testCedulaesInvalidas() {
        modelStub.verificarCedulaesResult = false;
        
        controller.intentarLogin();
        
        assertEquals("Contraseña incorrecta.", viewStub.errorMessage);
        assertNull(Sesion.getUsuarioActual());
    }

    @Test
    public void testLoginExitoso() {
        controller.intentarLogin();
        
        assertEquals("Inicio de sesión exitoso.", viewStub.successMessage);
        assertTrue(viewStub.disposed);
        
        Usuario usuario = Sesion.getUsuarioActual();
        assertNotNull(usuario);
        assertEquals("admin", usuario.getCedula());
    }

    @Test
    public void testVolverAlMenu() {
        controller.volverAlMenu();
        assertTrue(viewStub.disposed);
    }

    @Test
    public void testIrARegistro() {
        controller.irARegistro();
        assertTrue(viewStub.disposed);
    }

    @After
    public void tearDown() {
        Sesion.cerrarSesion();
    }
}