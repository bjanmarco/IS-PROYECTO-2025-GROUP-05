package Admin;

public class SesionAdmin {
    private static Administrador adminActual;

    public static void iniciarSesion(Administrador admin) {
        adminActual = admin;
    }

    public static Administrador getAdminActual() {
        return adminActual;
    }

    public static void cerrarSesion() {
        adminActual = null;
    }
}