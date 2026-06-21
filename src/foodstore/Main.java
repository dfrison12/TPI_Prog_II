package foodstore;

import foodstore.service.CategoriaService;
import foodstore.service.PedidoService;
import foodstore.service.ProductoService;
import foodstore.service.UsuarioService;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        CategoriaService categoriaService = new CategoriaService();
        ProductoService productoService = new ProductoService(categoriaService);
        UsuarioService usuarioService = new UsuarioService();
        PedidoService pedidoService = new PedidoService(usuarioService);

    
        try (Scanner scanner = new Scanner(System.in)) {
            AppMenu menu = new AppMenu(
                    scanner, categoriaService, productoService,
                    usuarioService, pedidoService);
            menu.iniciar();
        }
    }
}
