package test.controller;

import controllers.UserControllers.VerificacionIdentidadController;
import views.Usuario.VerificacionIdentidadView;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

import java.io.File;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class VerificacionIdentidadControllerTest {

    
    private static class MockView extends VerificacionIdentidadView {
        private String mensajeError = null;
        private String mensajeInfo = null;
        private boolean visible = false;

        public MockView() {
            super(new JFrame());
        }

        @Override
        public void mostrarError(String msg) {
            mensajeError = msg;
        }

        @Override
        public void mostrarMensaje(String msg) {
            mensajeInfo = msg;
        }

        @Override
        public void setVisible(boolean b) {
            visible = b;
        }
    }

    private VerificacionIdentidadController controller;
    private MockView mockView;

    @Before
    public void setUp() throws Exception {
        controller = new VerificacionIdentidadController(new JFrame());

        
        mockView = new MockView();
        java.lang.reflect.Field viewField = VerificacionIdentidadController.class.getDeclaredField("view");
        viewField.setAccessible(true);
        viewField.set(controller, mockView);
    }

    
    private void llamarVerificar() throws Exception {
        Method verificar = VerificacionIdentidadController.class.getDeclaredMethod("verificar");
        verificar.setAccessible(true);
        verificar.invoke(controller);
    }

    @Test
    public void testErrorSinArchivoSeleccionado() throws Exception {
        
        llamarVerificar();
        assertEquals("Debe seleccionar un archivo.", mockView.mensajeError);
    }

    @Test
    public void testUsuarioNoEncontrado() throws Exception {
        
        java.lang.reflect.Field archivoField = VerificacionIdentidadController.class.getDeclaredField("archivoSeleccionado");
        archivoField.setAccessible(true);
        archivoField.set(controller, new File("archivo_que_no_existe.jpg"));

        llamarVerificar();
        assertEquals("Usuario no encontrado en la base de datos.", mockView.mensajeError);
    }

}