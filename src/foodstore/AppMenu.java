package foodstore;

import foodstore.service.CategoriaService;
import foodstore.service.PedidoService;
import foodstore.service.ProductoService;
import foodstore.service.UsuarioService;
import java.util.Scanner;

public final class AppMenu {

    private final Scanner scanner;

    public AppMenu(Scanner scanner, CategoriaService categoriaService,
            ProductoService productoService, UsuarioService usuarioService,
            PedidoService pedidoService) {
        this.scanner = scanner;
    }

    public void iniciar() {
        int opcion = -1;
        do {
            System.out.println("\nFOOD STORE");
            System.out.println("1. Categorias");
            System.out.println("2. Productos");
            System.out.println("3. Usuarios");
            System.out.println("4. Pedidos");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());
                switch (opcion) {
                    case 1 -> System.out.println("Modulo categorias en desarrollo.");
                    case 2 -> System.out.println("Modulo productos en desarrollo.");
                    case 3 -> System.out.println("Modulo usuarios en desarrollo.");
                    case 4 -> System.out.println("Modulo pedidos en desarrollo.");
                    case 0 -> System.out.println("Hasta luego.");
                    default -> System.out.println("Opcion invalida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Debe ingresar un numero.");
            }
        } while (opcion != 0);
    }
}