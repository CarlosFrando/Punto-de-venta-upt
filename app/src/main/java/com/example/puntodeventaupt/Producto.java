package com.example.puntodeventaupt;

public class Producto {
    private String folio;
    private String nombre;
    private int cantidad;
    private double precioCompra;
    private double precioVenta;

    public Producto(String folio, String nombre, int cantidad, double precioCompra, double precioVenta) {
        this.folio = folio;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precioCompra = precioCompra;
        this.precioVenta = precioVenta;
    }

    public String getFolio() {
        return folio;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioCompra() {
        return precioCompra;
    }

    public double getPrecioVenta() {
        return precioVenta;
    }
}


