package foodstore.service;

import foodstore.entities.DetallePedido;
import foodstore.entities.Pedido;
import foodstore.entities.Usuario;
import foodstore.enums.Estado;
import foodstore.enums.FormaPago;
import foodstore.exception.EntidadNoEncontradaException;
import foodstore.exception.StockInvalidoException;
import foodstore.util.Buscador;
import foodstore.util.ComparadorPedidoPorFecha;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PedidoService {

    private final ArrayList<Pedido> pedidos;
    private final Buscador<Pedido, Long> buscador;
    private final ComparadorPedidoPorFecha comparadorPorFecha;
    private final UsuarioService usuarioService;

    public PedidoService(UsuarioService usuarioService) {
        pedidos = new ArrayList<>();
        buscador = new Buscador<>();
        comparadorPorFecha = new ComparadorPedidoPorFecha();
        this.usuarioService = usuarioService;
    }

    public void guardar(Pedido pedido) throws StockInvalidoException {
        validarPedido(pedido);
        Usuario usuarioActivo = pedido.getUsuario();
        pedido.setFecha(LocalDate.now());
        pedido.setEstado(Estado.PENDIENTE);
        pedido.calcularTotal();
        descontarStock(pedido);
        pedido.setUsuario(null);
        pedido.setUsuario(usuarioActivo);
        pedidos.add(pedido);
    }

    public Pedido buscarPorId(Long id) throws EntidadNoEncontradaException {
        validarId(id);
        Pedido pedido = buscador.buscar(pedidos, id);
        if (pedido == null || pedido.isEliminado()) {
            throw new EntidadNoEncontradaException(
                    "No se encontro un pedido activo con el id " + id + ".");
        }
        return pedido;
    }

    public List<Pedido> listar() {
        ArrayList<Pedido> pedidosActivos = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            if (!pedido.isEliminado()) {
                pedidosActivos.add(pedido);
            }
        }
        pedidosActivos.sort(comparadorPorFecha);
        return pedidosActivos;
    }

    public List<Pedido> listarPorUsuario(Long usuarioId)
            throws EntidadNoEncontradaException {
        Usuario usuario = usuarioService.buscarPorId(usuarioId);
        ArrayList<Pedido> pedidosFiltrados = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            if (!pedido.isEliminado() && pedido.getUsuario().equals(usuario)) {
                pedidosFiltrados.add(pedido);
            }
        }
        pedidosFiltrados.sort(comparadorPorFecha);
        return pedidosFiltrados;
    }

    public void actualizar(Long id, Estado estado, FormaPago formaPago)
            throws EntidadNoEncontradaException {
        validarPedidoActualizable(id, estado, formaPago);
        Pedido pedidoGuardado = buscarPorId(id);
        pedidoGuardado.setEstado(estado);
        pedidoGuardado.setFormaPago(formaPago);
    }

    public void eliminar(Long id) throws EntidadNoEncontradaException {
        Pedido pedido = buscarPorId(id);
        pedido.setEliminado(true);
        for (DetallePedido detalle : pedido.getDetalles()) {
            detalle.setEliminado(true);
        }
    }

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El id tiene que ser mayor a 0.");
        }
    }

    private void validarPedido(Pedido pedido) throws StockInvalidoException {
        if (pedido == null) {
            throw new IllegalArgumentException("El pedido no puede estar vacio.");
        }
        if (pedido.getUsuario() == null || pedido.getUsuario().getId() == null
                || pedido.getUsuario().isEliminado()) {
            throw new IllegalArgumentException(
                    "El pedido tiene que tener un usuario activo.");
        }
        if (pedido.getFormaPago() == null) {
            throw new IllegalArgumentException(
                    "El pedido tiene que tener una forma de pago.");
        }
        int cantidadDetallesActivos = 0;
        for (DetallePedido detalle : pedido.getDetalles()) {
            if (detalle != null && !detalle.isEliminado()) {
                validarDetalle(detalle);
                cantidadDetallesActivos++;
            }
        }
        if (cantidadDetallesActivos == 0) {
            throw new IllegalArgumentException(
                    "El pedido tiene que tener al menos un detalle.");
        }
    }

    private void validarDetalle(DetallePedido detalle)
            throws StockInvalidoException {
        if (detalle == null || detalle.getProducto() == null
                || detalle.getProducto().getId() == null) {
            throw new IllegalArgumentException(
                    "El detalle tiene que tener un producto.");
        }
        if (detalle.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad tiene que ser mayor a 0.");
        }
        if (detalle.getProducto().isEliminado()
                || !detalle.getProducto().isDisponible()) {
            throw new IllegalArgumentException(
                    "El producto " + detalle.getProducto().getNombre()
                    + " no esta disponible.");
        }
        if (detalle.getCantidad() > detalle.getProducto().getStock()) {
            throw new StockInvalidoException(
                    "La cantidad de " + detalle.getProducto().getNombre()
                    + " supera el stock disponible.");
        }
    }

    private void descontarStock(Pedido pedido) {
        for (DetallePedido detalle : pedido.getDetalles()) {
            if (!detalle.isEliminado()) {
                int stockActualizado = detalle.getProducto().getStock()
                        - detalle.getCantidad();
                detalle.getProducto().setStock(stockActualizado);
            }
        }
    }

    private void validarPedidoActualizable(Long id, Estado estado,
            FormaPago formaPago) {
        validarId(id);
        if (estado == null) {
            throw new IllegalArgumentException("El pedido tiene que tener un estado.");
        }
        if (formaPago == null) {
            throw new IllegalArgumentException(
                    "El pedido tiene que tener una forma de pago.");
        }
    }
}
