package models;

public class Producto {
    public int id;
    public String nombre;
    public double precio;
    public String descuento;

    public Producto(int id, String nombre, double precio, String descuento) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descuento = descuento;
    }
}
