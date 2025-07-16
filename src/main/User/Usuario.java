package User;

public class Usuario {
    private String cedula;
    private double saldo; // nuevo campo

    // Constructor con saldo inicial 0
    public Usuario(String cedula) {
        this.cedula = cedula;
        this.saldo = 0.0;
    }

    // Constructor con saldo específico
    public Usuario(String cedula, double saldo) {
        this.cedula = cedula;
        this.saldo = saldo;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    // Puedes agregar otros métodos si lo necesitas, por ejemplo:
    public void recargarSaldo(double monto) {
        this.saldo += monto;
    }

    public boolean descontarSaldo(double monto) {
        if (this.saldo >= monto) {
            this.saldo -= monto;
            return true;
        }
        return false;
    }
}