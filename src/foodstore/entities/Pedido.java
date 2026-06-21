package foodstore.entities;

import foodstore.enums.Estado;
import foodstore.enums.FormaPago;
import foodstore.exception.StockInvalidoException;
import foodstore.interfaces.Calculable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class Pedido extends Base implements Calculable {

    private static long ultimoId = 0;

    private LocalDate fecha;
    private Estado estado;
    private Double total;
    private FormaPago formaPago;
    private final List<DetallePedido> detalles;
    private Usuario usuario;

    public Pedido(LocalDate fecha, Estado estado, FormaPago formaPago, Usuario usuario) {
        super(++ultimoId);
        this.total = 0.0;
        this.detalles = new ArrayList<>();
        this.fecha = fecha;
        this.estado = estado;
        this.formaPago = formaPago;
        this.usuario = usuario;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public FormaPago getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(FormaPago formaPago) {
        this.formaPago = formaPago;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public final void setUsuario(Usuario usuario) {
        if (this.usuario == usuario) {
            return;
        }
        Usuario usuarioAnterior = this.usuario;
        this.usuario = usuario;
        if (usuarioAnterior != null) {
            usuarioAnterior.getPedidos().remove(this);
        }
        if (usuario != null && !usuario.getPedidos().contains(this)) {
            usuario.agregarPedido(this);
        }
    }

    @Override
    public void calcularTotal() {
        double suma = 0;
        for (DetallePedido detalle : detalles) {
            if (!detalle.isEliminado()) {
                suma += detalle.calcularSubtotal();
            }
        }
        total = suma;
    }

    public void addDetallePedido(int cantidad, Double precioUnitario, Producto producto)
            throws StockInvalidoException {
        if (producto == null) {
            throw new IllegalArgumentException(
                    "No se puede agregar un detalle sin producto.");
        }
        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad tiene que ser mayor a 0.");
        }
        if (precioUnitario == null || precioUnitario < 0) {
            throw new IllegalArgumentException(
                    "El precio unitario tiene que ser 0 o mayor.");
        }
        if (producto.isEliminado() || !producto.isDisponible()) {
            throw new IllegalArgumentException(
                    "El producto " + producto.getNombre() + " no esta disponible.");
        }

        DetallePedido detalleExistente = findDetallePedidoByProducto(producto);
        int cantidadAcumulada = cantidad;
        if (detalleExistente != null) {
            cantidadAcumulada += detalleExistente.getCantidad();
        }
        if (cantidadAcumulada > producto.getStock()) {
            throw new StockInvalidoException(
                    "La cantidad de " + producto.getNombre()
                    + " supera el stock disponible.");
        }

        if (detalleExistente == null) {
            detalles.add(new DetallePedido(cantidad, precioUnitario, producto));
        } else {
            detalleExistente.agregarCantidad(cantidad, precioUnitario);
        }
        calcularTotal();
    }

    public DetallePedido findDetallePedidoByProducto(Producto producto) {
        if (producto == null) {
            return null;
        }
        for (DetallePedido detalle : detalles) {
            if (!detalle.isEliminado() && producto.equals(detalle.getProducto())) {
                return detalle;
            }
        }
        return null;
    }

    public void deleteDetallePedidoByProducto(Producto producto) {
        DetallePedido detalle = findDetallePedidoByProducto(producto);
        if (detalle != null) {
            detalle.setEliminado(true);
            calcularTotal();
        }
    }

    @Override
    public String toString() {
        return "Pedido " + getId()
                + " - fecha: " + fecha
                + " - estado: " + estado
                + " - forma de pago: " + formaPago
                + " - total: $" + String.format(Locale.US, "%.2f", total);
    }
}
