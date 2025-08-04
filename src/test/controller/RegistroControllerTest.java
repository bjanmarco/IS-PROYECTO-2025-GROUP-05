package test.controller;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import controllers.UserControllers.RegistroController;
import models.Sesion;
import models.UserModel;
import models.Usuario;
import views.Usuario.RegistroView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

public class RegistroControllerTest {
    private RegistroViewStub viewStub;
    private UserModelStub modelStub;
    RegistroController controller;

    
    private static class RegistroViewStub extends RegistroView {
        public String cedula;
        public String contrasena;
        public String confirmacion;
        public String mensajeError;
        public String mensajeExito;
        public boolean disposed;
        public ActionListener registroListener;
        public ActionListener volverLoginListener;
        public ActionListener volverMainListener;

        public RegistroViewStub() {
            super();
        }

        @Override
        public String getCedula() {
            return cedula;
        }

        @Override
        public String getContrasena() {
            return contrasena;
        }

        @Override
        public String getConfirmacion() {
            return confirmacion;
        }

        @Override
        public void mostrarError(String mensaje) {
            this.mensajeError = mensaje;
        }

        @Override
        public void mostrarMensaje(String mensaje) {
            this.mensajeExito = mensaje;
        }

        @Override
        public void dispose() {
            this.disposed = true;
        }

        @Override
        public void setRegistroListener(ActionListener listener) {
            this.registroListener = listener;
        }

        @Override
        public void setVolverLoginListener(ActionListener listener) {
            this.volverLoginListener = listener;
        }

        @Override
        public void setVolverMainListener(ActionListener listener) {
            this.volverMainListener = listener;
        }
    }

    
    private static class UserModelStub extends UserModel {
        public boolean cedulaValida = true;
        public boolean cedulaExiste = true;
        public boolean usuarioYaRegistrado = false;
        public boolean claveValida = true;
        public boolean guardarExitoso = true;
        public boolean obtenerUsuarioExitoso = true;

        @Override
        public boolean esCedulaValida(String cedula) {
            return cedulaValida;
        }

        @Override
        public boolean cedulaExiste(String cedula) {
            return cedulaExiste;
        }

        @Override
        public boolean usuarioYaRegistrado(String cedula) {
            return usuarioYaRegistrado;
        }

        @Override
        public boolean esClaveValida(String clave) {
            return claveValida;
        }

        @Override
        public boolean guardarUsuario(String cedula, String clave, double saldo) {
            return guardarExitoso;
        }

        @Override
        public Usuario obtenerUsuario(String cedula) {
            if (obtenerUsuarioExitoso) {
                return new Usuario(
                    cedula, 
                    0.0, 
                    "Nombre Apellido", 
                    "user", 
                    "foto.jpg", 
                    false, 
                    false
                );
            }
            return null;
        }
    }

    @Before
    public void setUp() {
        viewStub = new RegistroViewStub();
        modelStub = new UserModelStub();
        controller = new RegistroController(viewStub, modelStub);
        Sesion.cerrarSesion();
    }

    @After
    public void tearDown() {
        Sesion.cerrarSesion();
    }

    @Test
    public void testRegistroExitoso() {
        
        viewStub.cedula = "12345678";
        viewStub.contrasena = "password123";
        viewStub.confirmacion = "password123";
        modelStub.guardarExitoso = true;
        modelStub.obtenerUsuarioExitoso = true;
        
        
        viewStub.registroListener.actionPerformed(new ActionEvent(new JButton(), 0, ""));
        
        
        assertNotNull("Debería haber iniciado sesión", Sesion.getUsuarioActual());
        assertEquals("12345678", Sesion.getUsuarioActual().getCedula());
        assertEquals("¡Registro exitoso! Bienvenido: 12345678", viewStub.mensajeExito);
        assertTrue("Debería cerrar la vista", viewStub.disposed);
    }

    @Test
    public void testCamposVacios() {
        viewStub.cedula = "";
        viewStub.contrasena = "";
        viewStub.confirmacion = "";
        
        viewStub.registroListener.actionPerformed(new ActionEvent(new JButton(), 0, ""));
        
        assertEquals("Todos los campos son obligatorios", viewStub.mensajeError);
        assertNull("No debería haber sesión iniciada", Sesion.getUsuarioActual());
    }

    @Test
    public void testCedulaInvalida() {
        viewStub.cedula = "123";
        viewStub.contrasena = "password123";
        viewStub.confirmacion = "password123";
        modelStub.cedulaValida = false;
        
        viewStub.registroListener.actionPerformed(new ActionEvent(new JButton(), 0, ""));
        
        assertEquals("Cédula inválida.", viewStub.mensajeError);
    }

    @Test
    public void testCedulaNoAutorizada() {
        viewStub.cedula = "87654321";
        viewStub.contrasena = "password123";
        viewStub.confirmacion = "password123";
        modelStub.cedulaExiste = false;
        
        viewStub.registroListener.actionPerformed(new ActionEvent(new JButton(), 0, ""));
        
        assertEquals("La cédula no está autorizada.", viewStub.mensajeError);
    }

    @Test
    public void testUsuarioYaRegistrado() {
        viewStub.cedula = "11223344";
        viewStub.contrasena = "password123";
        viewStub.confirmacion = "password123";
        modelStub.usuarioYaRegistrado = true;
        
        viewStub.registroListener.actionPerformed(new ActionEvent(new JButton(), 0, ""));
        
        assertEquals("La cédula ya está registrada.", viewStub.mensajeError);
    }

    @Test
    public void testClaveInvalida() {
        viewStub.cedula = "12345678";
        viewStub.contrasena = "short";
        viewStub.confirmacion = "short";
        modelStub.claveValida = false;
        
        viewStub.registroListener.actionPerformed(new ActionEvent(new JButton(), 0, ""));
        
        assertEquals("Contraseña debe ser de al menos 6 caracteres.", viewStub.mensajeError);
    }

    @Test
    public void testContrasenasNoCoinciden() {
        viewStub.cedula = "12345678";
        viewStub.contrasena = "password123";
        viewStub.confirmacion = "different123";
        
        viewStub.registroListener.actionPerformed(new ActionEvent(new JButton(), 0, ""));
        
        assertEquals("Las contraseñas no coinciden.", viewStub.mensajeError);
    }

    @Test
    public void testVolverLogin() {
        viewStub.volverLoginListener.actionPerformed(new ActionEvent(new JButton(), 0, ""));
        assertTrue("Debería cerrar la vista", viewStub.disposed);
    }

    @Test
    public void testVolverMain() {
        viewStub.volverMainListener.actionPerformed(new ActionEvent(new JButton(), 0, ""));
        assertTrue("Debería cerrar la vista", viewStub.disposed);
    }
}