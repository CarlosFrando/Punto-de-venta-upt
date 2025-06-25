package com.example.puntodeventaupt;

public class ItemVenta {
    private String folio;
    private String nombre;
    private int cantidad;
    private double precioUnitario;

    public ItemVenta(String folio, String nombre, int cantidad, double precioUnitario) {
        this.folio = folio;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public int getCantidad() { return cantidad; }
    public double getPrecioTotal() { return cantidad * precioUnitario; }
}

