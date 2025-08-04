package models;

public class Usuario {
    private String cedula;
    private double saldo;
    private String nombreApellido;
    private String rol;
    private String foto;
    private boolean desayuno;
    private boolean almuerzo;   

    public Usuario(String cedula) {
        this.cedula = cedula;
        this.saldo = 0.0;
    }


    public Usuario(String cedula, double saldo, String nombreApellido, String rol, String foto, boolean desayuno, boolean almuerzo) {
        this.cedula = cedula;
        this.saldo = saldo;
        this.nombreApellido = nombreApellido;
        this.rol = rol;
        this.foto = foto;
        this.desayuno = desayuno;
        this.almuerzo = almuerzo;
    }

    public String getNombreApellido() {
            return nombreApellido;
    }
    public void setNombreApellido(String nombreApellido) {
        this.nombreApellido = nombreApellido;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getFoto() {
        return foto;
    }
    
    public void setFoto(String foto) {
        this.foto = foto;
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

    public boolean tieneDesayuno() {
        return desayuno;
    }

    public void setDesayuno(boolean desayuno) {
        this.desayuno = desayuno;
    }

    public boolean tieneAlmuerzo() {
        return almuerzo;
    }

    public void setAlmuerzo(boolean almuerzo) {
        this.almuerzo = almuerzo;
    }
}