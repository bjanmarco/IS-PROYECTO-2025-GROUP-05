package test.model; 

import models.AdminModel;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import java.io.*;
import java.lang.reflect.Field;


public class AdminModelTest {
    private AdminModel model;
    private final String TEST_ADMINS_FILE = "test_admins.txt";
    private final String TEST_COSTOS_FILE = "test_costos.txt";
    private final String TEST_PERIODO = "2023-10";
    
    @Before
    public void setUp() throws Exception {
        
        createTestFile(TEST_ADMINS_FILE, "admin123,securePass456\n");
        createTestFile(TEST_COSTOS_FILE, "");
        
        model = new AdminModel();
        
        setPrivateField(model, "adminFile", new File(TEST_ADMINS_FILE));
        setPrivateField(model, "costosFile", new File(TEST_COSTOS_FILE));
    }

    private void setPrivateField(Object target, String fieldName, Object value) 
            throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private void createTestFile(String filename, String content) 
            throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(content);
        }
    }

    @Test
    public void testValidarPasswordAdmin() {
        assertTrue(model.validateAdminPassword("admin123"));
        assertFalse(model.validateAdminPassword("wrongpass"));
    }

    @Test
    public void testVerificarCedulaes() {
        assertTrue(model.verificarCedulaes("admin123", "securePass456"));
        assertFalse(model.verificarCedulaes("admin123", "wrongpass"));
        assertFalse(model.verificarCedulaes("unknown", "securePass456"));
    }

    @Test
    public void testCalcularCCB_NormalCase() {
        double cf = 5000.0;  
        double cv = 3000.0;  
        int nb = 1000;       
        double merma = 5.0;  
        
        double expected = ((5000 + 3000) / 1000) * (1 + (5.0/100));
        assertEquals(expected, model.calcularCCB(cf, cv, nb, merma), 0.001);
    }

    @Test
    public void testCalcularCCB_ZeroTrays() {
        
        assertEquals(0.0, model.calcularCCB(5000, 3000, 0, 5), 0.001);
        
        
        assertEquals(0.0, model.calcularCCB(0, 0, 0, 0), 0.001);
    }

    @Test
    public void testCalcularCCB_NoWaste() {
        assertEquals(8.0, model.calcularCCB(5000, 3000, 1000, 0), 0.001);
    }

    @Test
    public void testCalcularCCB_HighWaste() {
        double expected = ((5000 + 3000) / 1000) * (1 + (20.0/100));
        assertEquals(expected, model.calcularCCB(5000, 3000, 1000, 20), 0.001);
    }

    @Test
    public void testCalcularCCB_EdgeCases() {
        
        assertEquals(0.0, model.calcularCCB(-1000, -500, 100, 5), 0.001);
        
        
        double result = model.calcularCCB(1000, 500, 100, -5);
        assertTrue("deberia saber que hacer con saldo negativo", result >= 0);
        
        assertEquals(8400, model.calcularCCB(5000, 3000, 1, 5), 0.001);
    }

    @Test
    public void testCalcularCCB_DecimalPrecision() {
        double cf = 1234.56;
        double cv = 789.01;
        int nb = 50;
        double merma = 7.5;
        
        double expected = ((1234.56 + 789.01) / 50) * 1.075;
        assertEquals(expected, model.calcularCCB(cf, cv, nb, merma), 0.0001);
    }

    @After
    public void tearDown() {
        
        new File(TEST_ADMINS_FILE).delete();
        new File(TEST_COSTOS_FILE).delete();
    }
}