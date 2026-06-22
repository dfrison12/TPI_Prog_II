package foodstore.ui;

import foodstore.entities.DetallePedido;
import foodstore.entities.Pedido;
import foodstore.entities.Producto;
import foodstore.entities.Usuario;
import foodstore.enums.Estado;
import foodstore.enums.FormaPago;
import foodstore.exception.EntidadNoEncontradaException;
import foodstore.exception.StockInvalidoException;
import foodstore.service.PedidoService;
import foodstore.service.ProductoService;
import foodstore.service.UsuarioService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class PedidoMenu {

    private final InputHelper input;
    private final PedidoService service;
    private final UsuarioService usuarioService;
    private final ProductoService productoService;

    public PedidoMenu(InputHelper input, PedidoService service,
            UsuarioService usuarioService, ProductoService productoService) {
        this.input = input;
        this.service = service;
        this.usuarioService = usuarioService;
        this.productoService = productoService;
    }

    public void iniciar() {
        int opcion;
        do {
            System.out.println("\nPEDIDOS");
            System.out.println("1. Listar todos los pedidos");
            System.out.println("2. Listar pedidos por usuario");
            System.out.println("3. Consultar pedido por id");
            System.out.println("4. Crear pedido");
            System.out.println("5. Actualizar pedido");
            System.out.println("6. Eliminar pedido");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opcion: ");
            opcion = input.leerOpcion(0, 6);
            switch (opcion) {
                case 1 -> mostrar(service.listar(), "No hay pedidos cargados.");
                case 2 -> listarPorUsuario();
                case 3 -> consultar();
                case 4 -> crear();
                case 5 -> actualizar();
                case 6 -> eliminar();
                default -> { }
            }
        } while (opcion != 0);
    }

    private void listarPorUsuario() {
        try {
            Long id = input.leerIdPositivo("Ingrese el id del usuario: ");
            mostrar(service.listarPorUsuario(id),
                    "No hay pedidos cargados para ese usuario.");
        } catch (EntidadNoEncontradaException e) {
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void consultar() {
        try {
            Pedido pedido = service.buscarPorId(
                    input.leerIdPositivo("Ingrese el id del pedido: "));
            mostrarPedido(pedido);
            System.out.println("Detalles:");
            for (DetallePedido detalle : pedido.getDetalles()) {
                if (!detalle.isEliminado()) {
                    double precioUnitario = detalle.getSubtotal() / detalle.getCantidad();
                    System.out.println("Detalle " + detalle.getId() + " - producto "
                            + detalle.getProducto().getId() + ": "
                            + detalle.getProducto().getNombre() + " - cantidad: "
                            + detalle.getCantidad() + " - precio unitario: $"
                            + dinero(precioUnitario) + " - subtotal: $"
                            + dinero(detalle.getSubtotal()));
                }
            }
        } catch (EntidadNoEncontradaException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void crear() {
        try {
            List<Usuario> usuarios = usuarioService.listar();
            if (usuarios.isEmpty()) {
                System.out.println("No hay usuarios disponibles para crear el pedido.");
                return;
            }
            UsuarioMenu.mostrar(usuarios);
            Usuario usuario = usuarioService.buscarPorId(
                    input.leerIdPositivo("Ingrese el id del usuario: "));
            Pedido pedido = new Pedido(LocalDate.now(), Estado.PENDIENTE,
                    leerFormaPago(), usuario);
            cargarDetalles(pedido);
            mostrarResumen(pedido);
            if (!input.leerSiNo("Desea guardar el pedido? (S/N): ")) {
                System.out.println("No se guardo el pedido.");
                return;
            }
            service.guardar(pedido);
            System.out.println("El pedido se creo con el id " + pedido.getId()
                    + " y un total de $" + dinero(pedido.getTotal()) + ".");
        } catch (EntidadNoEncontradaException | StockInvalidoException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
            System.out.println("No se guardo el pedido.");
        }
    }

    private void cargarDetalles(Pedido pedido) {
        List<Producto> disponibles = obtenerProductosDisponibles();
        if (disponibles.isEmpty()) {
            throw new IllegalArgumentException(
                    "No hay productos disponibles para crear el pedido.");
        }
        boolean agregarOtro = true;
        do {
            ProductoMenu.mostrar(disponibles, "No hay productos disponibles.");
            Producto producto;
            try {
                producto = productoService.buscarPorId(
                        input.leerIdPositivo("Ingrese el id del producto: "));
            } catch (EntidadNoEncontradaException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
                continue;
            }
            if (producto.isEliminado() || !producto.isDisponible()
                    || producto.getStock() <= 0) {
                System.out.println("El producto seleccionado no esta disponible.");
                agregarOtro = true;
                continue;
            }
            int cantidad = input.leerEnteroPositivo("Cantidad: ");
            DetallePedido existente = pedido.findDetallePedidoByProducto(producto);
            int acumulada = cantidad + (existente == null ? 0 : existente.getCantidad());
            if (acumulada > producto.getStock()) {
                System.out.println("La cantidad supera el stock disponible de "
                        + producto.getStock() + ".");
                agregarOtro = true;
                continue;
            }
            try {
                pedido.addDetallePedido(cantidad, producto.getPrecio(), producto);
            } catch (StockInvalidoException | IllegalArgumentException e) {
                System.out.println(e.getMessage());
                continue;
            }
            System.out.println("El producto se agrego al pedido.");
            agregarOtro = input.leerSiNo("Desea agregar otro producto? (S/N): ");
        } while (agregarOtro);
    }

    private List<Producto> obtenerProductosDisponibles() {
        List<Producto> disponibles = new ArrayList<>();
        for (Producto producto : productoService.listar()) {
            if (producto.isDisponible() && producto.getStock() > 0) {
                disponibles.add(producto);
            }
        }
        return disponibles;
    }

    private void actualizar() {
        try {
            List<Pedido> pedidos = service.listar();
            if (pedidos.isEmpty()) {
                System.out.println("No hay pedidos cargados.");
                return;
            }
            mostrar(pedidos, "No hay pedidos cargados.");
            Long id = input.leerIdPositivo("Ingrese el id del pedido: ");
            Pedido pedido = service.buscarPorId(id);
            System.out.println("1. Cambiar estado");
            System.out.println("2. Cambiar forma de pago");
            System.out.println("0. Cancelar");
            System.out.print("Seleccione una opcion: ");
            int opcion = input.leerOpcion(0, 2);
            if (opcion == 0) {
                System.out.println("No se realizaron cambios.");
                return;
            }
            Estado estado = pedido.getEstado();
            FormaPago formaPago = pedido.getFormaPago();
            if (opcion == 1) {
                estado = leerEstado();
            } else {
                formaPago = leerFormaPago();
            }
            service.actualizar(id, estado, formaPago);
            System.out.println("El pedido se actualizo correctamente.");
        } catch (EntidadNoEncontradaException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void eliminar() {
        try {
            List<Pedido> pedidos = service.listar();
            if (pedidos.isEmpty()) {
                System.out.println("No hay pedidos cargados.");
                return;
            }
            mostrar(pedidos, "No hay pedidos cargados.");
            Long id = input.leerIdPositivo("Ingrese el id del pedido: ");
            Pedido pedido = service.buscarPorId(id);
            if (!input.leerSiNo("Desea eliminar el pedido "
                    + pedido.getId() + "? (S/N): ")) {
                System.out.println("No se elimino el pedido.");
                return;
            }
            service.eliminar(id);
            System.out.println("El pedido se elimino correctamente.");
        } catch (EntidadNoEncontradaException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private FormaPago leerFormaPago() {
        System.out.println("Forma de pago:");
        System.out.println("1. TARJETA");
        System.out.println("2. TRANSFERENCIA");
        System.out.println("3. EFECTIVO");
        System.out.print("Seleccione una opcion: ");
        return switch (input.leerOpcion(1, 3)) {
            case 1 -> FormaPago.TARJETA;
            case 2 -> FormaPago.TRANSFERENCIA;
            default -> FormaPago.EFECTIVO;
        };
    }

    private Estado leerEstado() {
        System.out.println("Estado:");
        System.out.println("1. PENDIENTE");
        System.out.println("2. CONFIRMADO");
        System.out.println("3. TERMINADO");
        System.out.println("4. CANCELADO");
        System.out.print("Seleccione una opcion: ");
        return switch (input.leerOpcion(1, 4)) {
            case 1 -> Estado.PENDIENTE;
            case 2 -> Estado.CONFIRMADO;
            case 3 -> Estado.TERMINADO;
            default -> Estado.CANCELADO;
        };
    }

    private void mostrarResumen(Pedido pedido) {
        System.out.println("\nResumen del pedido:");
        for (DetallePedido detalle : pedido.getDetalles()) {
            System.out.println(detalle);
        }
        System.out.println("Total: $" + dinero(pedido.getTotal()));
    }

    private static void mostrar(List<Pedido> pedidos, String mensajeVacio) {
        if (pedidos.isEmpty()) {
            System.out.println(mensajeVacio);
            return;
        }
        System.out.println("\nPedidos disponibles:");
        for (Pedido pedido : pedidos) {
            mostrarPedido(pedido);
        }
    }

    private static void mostrarPedido(Pedido pedido) {
        System.out.println("Pedido " + pedido.getId() + " - fecha: "
                + pedido.getFecha() + " - usuario " + pedido.getUsuario().getId()
                + ": " + pedido.getUsuario().getNombre() + " "
                + pedido.getUsuario().getApellido() + " - estado: "
                + pedido.getEstado() + " - forma de pago: "
                + pedido.getFormaPago() + " - total: $" + dinero(pedido.getTotal()));
    }

    private static String dinero(double valor) {
        return String.format(Locale.US, "%.2f", valor);
    }
}
