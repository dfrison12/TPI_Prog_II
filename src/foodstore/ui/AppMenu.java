package foodstore.ui;

import foodstore.service.CategoriaService;
import foodstore.service.PedidoService;
import foodstore.service.ProductoService;
import foodstore.service.UsuarioService;
import java.util.Scanner;

public final class AppMenu {

    private final InputHelper input;
    private final CategoriaMenu categoriaMenu;
    private final ProductoMenu productoMenu;
    private final UsuarioMenu usuarioMenu;
    private final PedidoMenu pedidoMenu;

    public AppMenu(Scanner scanner, CategoriaService categoriaService,
            ProductoService productoService, UsuarioService usuarioService,
            PedidoService pedidoService) {
        input = new InputHelper(scanner);
        categoriaMenu = new CategoriaMenu(input, categoriaService);
        productoMenu = new ProductoMenu(input, productoService, categoriaService);
        usuarioMenu = new UsuarioMenu(input, usuarioService);
        pedidoMenu = new PedidoMenu(
                input, pedidoService, usuarioService, productoService);
    }

    public void iniciar() {
        int opcion;
        do {
            System.out.println("\nFOOD STORE");
            System.out.println("1. Categorias");
            System.out.println("2. Productos");
            System.out.println("3. Usuarios");
            System.out.println("4. Pedidos");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");
            opcion = input.leerOpcion(0, 4);
            switch (opcion) {
                case 1 -> categoriaMenu.iniciar();
                case 2 -> productoMenu.iniciar();
                case 3 -> usuarioMenu.iniciar();
                case 4 -> pedidoMenu.iniciar();
                default -> { }
            }
        } while (opcion != 0);
        System.out.println("Hasta luego.");
    }
}
