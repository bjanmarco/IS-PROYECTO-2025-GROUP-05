package test.model;  

import models.Sesion;
import models.Usuario;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SesionTest {
    
    private Usuario usuarioPrueba;
    private Usuario otroUsuario;

    @Before
    public void setUp() {
        
        usuarioPrueba = new Usuario(
            "admin123", 
            0.0, 
            "Administrador Principal", 
            "admin", 
            "admin.jpg", 
            false, 
            false
        );
        
        otroUsuario = new Usuario(
            "567890", 
            100.0, 
            "Usuario Normal", 
            "user", 
            "user.jpg", 
            true, 
            false
        );
    }

    @After
    public void limpiarSesion() {
        Sesion.cerrarSesion();
    }

    @Test
    public void testIniciarYCerrarSesion() {
        Sesion.iniciarSesion(usuarioPrueba);
        assertNotNull(Sesion.getUsuarioActual());
        assertEquals("admin123", Sesion.getUsuarioActual().getCedula());
        assertEquals("Administrador Principal", Sesion.getUsuarioActual().getNombreApellido());

        Sesion.cerrarSesion();
        assertNull(Sesion.getUsuarioActual());
    }

    @Test
    public void testSesionSinIniciar() {
        assertNull(Sesion.getUsuarioActual());
    }

    @Test
    public void testReemplazarUsuarioEnSesion() {
        Sesion.iniciarSesion(usuarioPrueba);
        Sesion.iniciarSesion(otroUsuario);
        
        assertEquals("567890", Sesion.getUsuarioActual().getCedula());
        assertEquals(100.0, Sesion.getUsuarioActual().getSaldo(), 0.001);
        assertTrue(Sesion.getUsuarioActual().tieneDesayuno());
    }

    @Test
    public void testSetUsuarioActual() {
        Sesion.setUsuarioActual(usuarioPrueba);
        assertNotNull(Sesion.getUsuarioActual());
        assertEquals("admin123", Sesion.getUsuarioActual().getCedula());
        
        Sesion.setUsuarioActual(otroUsuario);
        assertEquals("567890", Sesion.getUsuarioActual().getCedula());
    }
}