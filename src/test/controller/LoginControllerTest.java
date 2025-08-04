package test.controller;

import controllers.UserControllers.LoginController;
import models.UserModel;
import models.Sesion;
import models.Usuario;
import views.Usuario.LoginView;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import static org.junit.Assert.*;

public class LoginControllerTest {
    private LoginController controller;
    private UserModelStub userModelStub;
    private LoginViewStub viewStub;

    
    private static class UserModelStub extends UserModel {
        public boolean cedulaValida = true;
        public boolean usuarioRegistrado = true;
        public boolean autenticacionExitosa = true;
        public Usuario usuarioARetornar;

        @Override
        public boolean esCedulaValida(String cedula) {
            return cedulaValida;
        }

        @Override
        public boolean usuarioYaRegistrado(String cedula) {
            return usuarioRegistrado;
        }

        @Override
        public boolean autenticarUsuario(String cedula, String contrasena) {
            return autenticacionExitosa;
        }

        @Override
        public Usuario obtenerUsuario(String cedula) {
            return usuarioARetornar;
        }
    }

    
    private static class LoginViewStub extends LoginView {
        public String errorMessage;
        public String successMessage;
        public boolean disposed = false;

        @Override
        public void mostrarError(String mensaje) {
            this.errorMessage = mensaje;
        }

        @Override
        public void mostrarMensaje(String mensaje) {
            this.successMessage = mensaje;
        }

        @Override
        public void dispose() {
            this.disposed = true;
        }

        @Override
        public String getCedula() {
            return "623456";
        }

        @Override
        public String getContrasena() {
            return "password123";
        }
    }

    @Before
    public void setUp() {
        userModelStub = new UserModelStub();
        viewStub = new LoginViewStub();
        
        
        userModelStub.usuarioARetornar = new Usuario(
            "623456", 
            100.0, 
            "Nombre Apellido", 
            "estudiante", 
            "foto.jpg", 
            false, 
            false
        );
        
        controller = new LoginController(userModelStub, viewStub);
        Sesion.cerrarSesion();
    }

    @Test
    public void testCedulaInvalida() {
        userModelStub.cedulaValida = false;
        controller.intentarLogin();
        
        assertEquals("La cédula no es válida.", viewStub.errorMessage);
        assertNull(viewStub.successMessage);
        assertFalse(viewStub.disposed);
        assertNull(Sesion.getUsuarioActual());
    }

    @Test
    public void testUsuarioNoRegistrado() {
        userModelStub.usuarioRegistrado = false;
        controller.intentarLogin();
        
        assertEquals("El usuario no está registrado.", viewStub.errorMessage);
        assertNull(viewStub.successMessage);
        assertFalse(viewStub.disposed);
        assertNull(Sesion.getUsuarioActual());
    }

    @Test
    public void testContrasenaIncorrecta() {
        userModelStub.autenticacionExitosa = false;
        controller.intentarLogin();
        
        assertEquals("Contraseña incorrecta.", viewStub.errorMessage);
        assertNull(viewStub.successMessage);
        assertFalse(viewStub.disposed);
        assertNull(Sesion.getUsuarioActual());
    }

    @Test
    public void testVolverAlMenuPrincipal() {
        controller.volverAlMenuPrincipal();
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