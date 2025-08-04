package test.model;

import models.MenuSemanalModel;
import org.junit.*;

import java.io.*;
import java.nio.file.*;
import java.util.Map;

import static org.junit.Assert.*;

public class MenuSemanalModelTest {

    private MenuSemanalModel model;
    private final String TEST_FILE = "src/main/data/MenuSemanal.txt";
    private String originalContent;

    @Before
    public void setUp() throws Exception {
        model = new MenuSemanalModel();

        
        originalContent = readFileIfExists(TEST_FILE);

        
        Files.createDirectories(Paths.get("src/main/data"));

        
        writeFile(TEST_FILE,
                "Lunes,Desayuno,Arepa,Huevo,Queso,Jugo,Fruta\n" +
                "Lunes,Almuerzo,Arroz,Carne,Ensalada,Jugo,Flan\n");
    }

    private String readFileIfExists(String path) throws IOException {
        if (Files.exists(Paths.get(path))) {
            return new String(Files.readAllBytes(Paths.get(path)));
        }
        return null;
    }

    private void writeFile(String path, String content) throws IOException {
        Files.write(Paths.get(path),
                content.getBytes(),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE);
    }

    @Test
    public void testExisteMenuParaDiaYTipo() {
        assertTrue(model.existeMenuParaDiaYTipo("Lunes", "Desayuno"));
        assertTrue(model.existeMenuParaDiaYTipo("Lunes", "Almuerzo"));
        assertFalse(model.existeMenuParaDiaYTipo("Martes", "Desayuno"));
    }

    @Test
    public void testObtenerMenu() {
        Map<String, String> menu = model.obtenerMenu("Lunes", "Almuerzo");
        assertNotNull(menu);
        assertEquals("Arroz", menu.get("Plato principal"));
        assertEquals("Carne", menu.get("Contorno 1"));
        assertEquals("Ensalada", menu.get("Contorno 2"));
        assertEquals("Jugo", menu.get("Bebida"));
        assertEquals("Flan", menu.get("Postre"));
    }

    @Test
    public void testObtenerMenuInexistente() {
        Map<String, String> menu = model.obtenerMenu("Miércoles", "Desayuno");
        assertNull(menu);
    }

    @Test
    public void testGuardarNuevoMenu() {
        boolean resultado = model.guardarOModificarMenu("Martes", "Desayuno", "Pan", "Jamon", "Queso", "Café", "Galleta", false);
        assertTrue(resultado);
        assertTrue(model.existeMenuParaDiaYTipo("Martes", "Desayuno"));
    }

    @Test
    public void testModificarMenuExistente() {
        boolean resultado = model.guardarOModificarMenu("Lunes", "Desayuno", "Cachapa", "Queso", "Jamón", "Jugo", "Torta", true);
        assertTrue(resultado);
        Map<String, String> menu = model.obtenerMenu("Lunes", "Desayuno");
        assertEquals("Cachapa", menu.get("Plato principal"));
        assertEquals("Queso", menu.get("Contorno 1"));
        assertEquals("Jamón", menu.get("Contorno 2"));
        assertEquals("Jugo", menu.get("Bebida"));
        assertEquals("Torta", menu.get("Postre"));
    }

    @Test
    public void testNoModificarMenuExistenteCuandoSobrescribirFalse() {
        boolean resultado = model.guardarOModificarMenu("Lunes", "Desayuno", "OtraCosa", "Nada", "Nada", "Agua", "Nada", false);
        assertTrue(resultado);
        Map<String, String> menu = model.obtenerMenu("Lunes", "Desayuno");
        assertEquals("Arepa", menu.get("Plato principal")); 
    }

    @After
    public void tearDown() throws Exception {
        restoreFile(TEST_FILE, originalContent);
    }

    private void restoreFile(String path, String content) throws IOException {
        if (content != null) {
            writeFile(path, content);
        } else {
            Files.deleteIfExists(Paths.get(path));
        }
    }
}