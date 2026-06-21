package foodstore;

import foodstore.entities.Categoria;
import foodstore.entities.Pedido;
import foodstore.entities.Producto;
import foodstore.entities.Usuario;
import foodstore.enums.Estado;
import foodstore.enums.FormaPago;
import foodstore.enums.Rol;
import foodstore.exception.EntidadNoEncontradaException;
import foodstore.exception.StockInvalidoException;
import foodstore.service.CategoriaService;
import foodstore.service.PedidoService;
import foodstore.service.ProductoService;
import foodstore.service.UsuarioService;
import java.time.LocalDate;

public final class DatosIniciales {

    private DatosIniciales() {
    }

    public static boolean cargar(CategoriaService categoriaService,
            ProductoService productoService, UsuarioService usuarioService,
        PedidoService pedidoService) {
        try {
            Categoria comidas = new Categoria("Comidas", "Platos preparados");
            Categoria bebidas = new Categoria("Bebidas", "Bebidas frias y calientes");
            Categoria postres = new Categoria("Postres", "Opciones dulces");
            Categoria categoriaEliminada = new Categoria(
                    "Categoria eliminada", "Registro para probar la baja logica");

            categoriaService.guardar(comidas);
            categoriaService.guardar(bebidas);
            categoriaService.guardar(postres);
            categoriaService.guardar(categoriaEliminada);
            categoriaService.eliminar(categoriaEliminada.getId());

            Producto hamburguesa = new Producto(
                    "Hamburguesa", 8500.0, "Hamburguesa completa",
                    20, "hamburguesa.jpg", true, comidas);
            Producto papas = new Producto(
                    "Papas fritas", 3500.0, "Porcion de papas fritas",
                    15, "papas.jpg", true, comidas);
            Producto pizza = new Producto(
                    "Pizza muzzarella", 12000.0, "Pizza grande de muzzarella",
                    10, "pizza.jpg", true, comidas);
            Producto agua = new Producto(
                    "Agua mineral", 2000.0, "Botella de agua de 500 ml",
                    30, "agua.jpg", true, bebidas);
            Producto jugo = new Producto(
                    "Jugo de naranja", 3200.0, "Vaso de jugo exprimido",
                    0, "jugo.jpg", false, bebidas);
            Producto flan = new Producto(
                    "Flan casero", 4000.0, "Flan con dulce de leche",
                    8, "flan.jpg", true, postres);

            productoService.guardar(hamburguesa);
            productoService.guardar(papas);
            productoService.guardar(pizza);
            productoService.guardar(agua);
            productoService.guardar(jugo);
            productoService.guardar(flan);

            Usuario ana = new Usuario(
                    "Dario", "Frison", "frison@gmail.com", "2664000001",
                    "clave123", Rol.ADMIN);
            Usuario sofia = new Usuario(
                    "Maxi", "Aguero", "maxi@gmail.com", "2664000322",
                    "clave456", Rol.USUARIO);
            Usuario usuarioEliminado = new Usuario(
                    "Usuario", "Eliminado", "eliminado@mail.com", "2664000003",
                    "clave789", Rol.USUARIO);

            usuarioService.guardar(ana);
            usuarioService.guardar(sofia);
            usuarioService.guardar(usuarioEliminado);
            usuarioService.eliminar(usuarioEliminado.getId());

            Pedido primerPedido = new Pedido(
                    LocalDate.now(), Estado.PENDIENTE, FormaPago.TARJETA, ana);
            primerPedido.addDetallePedido(2, hamburguesa.getPrecio(), hamburguesa);
            primerPedido.addDetallePedido(1, papas.getPrecio(), papas);
            pedidoService.guardar(primerPedido);
            pedidoService.actualizar(
                    primerPedido.getId(), Estado.CONFIRMADO,
                    primerPedido.getFormaPago());

            Pedido segundoPedido = new Pedido(
                    LocalDate.now(), Estado.PENDIENTE, FormaPago.EFECTIVO, sofia);
            segundoPedido.addDetallePedido(1, pizza.getPrecio(), pizza);
            segundoPedido.addDetallePedido(2, agua.getPrecio(), agua);
            pedidoService.guardar(segundoPedido);
            return true;
        } catch (EntidadNoEncontradaException e) {
            System.out.println("No se pudieron cargar los datos iniciales.");
            System.out.println(e.getMessage());
            return false;
        } catch (StockInvalidoException e) {
            System.out.println("No se pudieron cargar los datos iniciales.");
            System.out.println(e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("No se pudieron cargar los datos iniciales.");
            System.out.println(e.getMessage());
            return false;
        }
    }
}
