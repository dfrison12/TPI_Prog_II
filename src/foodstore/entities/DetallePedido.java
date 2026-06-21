package foodstore.entities;

import java.util.Locale;

public class DetallePedido extends Base {

    private static long ultimoId = 0;

    private int cantidad;
    private Double subtotal;
    private Producto producto;

    public DetallePedido(int cantidad, Double precioUnitario, Producto producto) {
        super(++ultimoId);
        this.cantidad = cantidad;
        this.producto = producto;
        this.subtotal = cantidad * precioUnitario;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        Double precioUnitario = this.cantidad > 0 ? subtotal / this.cantidad : 0.0;
        this.cantidad = cantidad;
        this.subtotal = cantidad * precioUnitario;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public void agregarCantidad(int cantidad, Double precioUnitario) {
        this.cantidad += cantidad;
        this.subtotal += cantidad * precioUnitario;
    }

    public Double calcularSubtotal() {
        return subtotal;
    }

    @Override
    public String toString() {
        String nombreProducto = producto == null ? "Sin producto" : producto.getNombre();
        return "Detalle " + getId()
                + " - " + nombreProducto
                + " - cantidad: " + cantidad
                + " - subtotal: $" + String.format(Locale.US, "%.2f", subtotal);
    }
}
