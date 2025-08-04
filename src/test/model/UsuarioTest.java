package test.model;  

import models.Usuario;  
import org.junit.Test;
import static org.junit.Assert.*;

public class UsuarioTest {
    
    @Test
    public void testCrearUsuarioConSaldoInicial() {
        Usuario usuario = new Usuario("123456", 100.0, "Nombre Apellido", "rol", "foto.jpg", false, false);
        assertEquals("123456", usuario.getCedula());
        assertEquals(100.0, usuario.getSaldo(), 0.001);
    }

    @Test
    public void testRecargarSaldo() {
        Usuario usuario = new Usuario("123456", 50.0, "Nombre Apellido", "rol", "foto.jpg", false, false);
        usuario.recargarSaldo(30.5);
        assertEquals(80.5, usuario.getSaldo(), 0.001);
    }

    @Test
    public void testDescontarSaldoExitoso() {
        Usuario usuario = new Usuario("123456", 100.0, "Nombre Apellido", "rol", "foto.jpg", false, false);
        assertTrue(usuario.descontarSaldo(40.0));
        assertEquals(60.0, usuario.getSaldo(), 0.001);
    }

    @Test
    public void testDescontarSaldoFallido() {
        Usuario usuario = new Usuario("123456", 30.0, "Nombre Apellido", "rol", "foto.jpg", false, false);
        assertFalse(usuario.descontarSaldo(50.0));
        assertEquals(30.0, usuario.getSaldo(), 0.001);
    }

    @Test
    public void testConstructorSimple() {
        Usuario usuario = new Usuario("654321");
        assertEquals("654321", usuario.getCedula());
        assertEquals(0.0, usuario.getSaldo(), 0.001);
        assertNull(usuario.getNombreApellido());
        assertNull(usuario.getRol());
        assertNull(usuario.getFoto());
        assertFalse(usuario.tieneDesayuno());
        assertFalse(usuario.tieneAlmuerzo());
    }

    @Test
    public void testSettersYGetters() {
        Usuario usuario = new Usuario("123");
        
        usuario.setNombreApellido("Juan Pérez");
        usuario.setRol("Estudiante");
        usuario.setFoto("juan.jpg");
        usuario.setDesayuno(true);
        usuario.setAlmuerzo(false);
        usuario.setSaldo(50.0);
        
        assertEquals("Juan Pérez", usuario.getNombreApellido());
        assertEquals("Estudiante", usuario.getRol());
        assertEquals("juan.jpg", usuario.getFoto());
        assertTrue(usuario.tieneDesayuno());
        assertFalse(usuario.tieneAlmuerzo());
        assertEquals(50.0, usuario.getSaldo(), 0.001);
    }
}