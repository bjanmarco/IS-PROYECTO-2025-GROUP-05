package test.model;

import models.RefBancariaModel;
import models.RefBancariaModel.RefBancaria;

import org.junit.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class RefBancariaModelTest {

    private RefBancariaModel model;
    private final String TEST_FILE = "src/main/data/referencias_bancarias.txt";
    private String originalContent;

    @Before
    public void setUp() throws Exception {
        model = new RefBancariaModel();

        
        originalContent = readFileIfExists(TEST_FILE);

        
        Files.createDirectories(Paths.get("src/main/data"));

        
        writeFile(TEST_FILE,
                "11112222,150.0,BancoA,2025-08-01 10:00:00\n" +
                "33334444,300.5,BancoB,2025-08-02 12:30:00\n");
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
    public void testBuscarReferenciaExistente() {
        RefBancaria ref = model.buscarReferencia("11112222");
        assertNotNull(ref);
        assertEquals("BancoA", ref.getBanco());
        assertEquals(150.0, ref.getMonto(), 0.001);
    }

    @Test
    public void testBuscarReferenciaInexistente() {
        assertNull(model.buscarReferencia("99999999"));
    }

    @Test
    public void testAgregarReferenciaValida() {
        boolean resultado = model.agregarReferencia("55556666", 250.0, "BancoNuevo");
        assertTrue(resultado);
        RefBancaria ref = model.buscarReferencia("55556666");
        assertNotNull(ref);
        assertEquals("BancoNuevo", ref.getBanco());
        assertEquals(250.0, ref.getMonto(), 0.001);
    }

    @Test
    public void testAgregarReferenciaDuplicada() {
        boolean resultado = model.agregarReferencia("33334444", 300.5, "BancoB");
        assertFalse(resultado);
    }

    @Test
    public void testAgregarMontoInvalido() {
        boolean resultado = model.agregarReferencia("77778888", 0.00, "BancoInvalido");
        assertFalse(resultado);
    }

    @Test
    public void testEliminarReferenciaExistente() {
        boolean eliminado = model.eliminarReferencia("33334444");
        assertTrue(eliminado);
        assertNull(model.buscarReferencia("33334444"));
    }

    @Test
    public void testEliminarReferenciaInexistente() {
        boolean eliminado = model.eliminarReferencia("00000000");
        assertFalse(eliminado);
    }

    @Test
    public void testEsReferenciaValida() {
        assertTrue(model.esReferenciaValida("12345678"));
        assertTrue(model.esReferenciaValida("987654321012"));
        assertFalse(model.esReferenciaValida("1234")); 
        assertFalse(model.esReferenciaValida("1234567890123")); 
        assertFalse(model.esReferenciaValida("abc12345")); 
    }

    @Test
    public void testToStringReferencia() {
        LocalDateTime ahora = LocalDateTime.now();
        RefBancaria ref = new RefBancaria("99887766", 123.45, "BancoX", ahora);
        String s = ref.toString();
        assertTrue(s.contains("Ref: 99887766"));
        assertTrue(s.contains("Banco: BancoX"));
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