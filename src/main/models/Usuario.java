package models;

public class Usuario {
    private String credencial;
    private double saldo;

    public Usuario(String credencial) {
        this.credencial = credencial;
        this.saldo = 0.0;
    }

    public Usuario(String credencial, double saldo) {
        this.credencial = credencial;
        this.saldo = saldo;
    }

    public String getCredencial() {
        return credencial;
    }

    public void setCredencial(String credencial) {
        this.credencial = credencial;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

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