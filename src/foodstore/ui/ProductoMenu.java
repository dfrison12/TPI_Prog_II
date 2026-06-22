package foodstore.ui;

import foodstore.entities.Categoria;
import foodstore.entities.Producto;
import foodstore.exception.EntidadNoEncontradaException;
import foodstore.service.CategoriaService;
import foodstore.service.ProductoService;
import java.util.List;

public final class ProductoMenu {

    private final InputHelper input;
    private final ProductoService service;
    private final CategoriaService categoriaService;

    public ProductoMenu(InputHelper input, ProductoService service,
            CategoriaService categoriaService) {
        this.input = input;
        this.service = service;
        this.categoriaService = categoriaService;
    }

    public void iniciar() {
        int opcion;
        do {
            System.out.println("\nPRODUCTOS");
            System.out.println("1. Listar todos los productos");
            System.out.println("2. Listar productos por categoria");
            System.out.println("3. Crear producto");
            System.out.println("4. Editar producto");
            System.out.println("5. Eliminar producto");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opcion: ");
            opcion = input.leerOpcion(0, 5);
            switch (opcion) {
                case 1 -> mostrar(service.listar(), "No hay productos cargados.");
                case 2 -> listarPorCategoria();
                case 3 -> crear();
                case 4 -> editar();
                case 5 -> eliminar();
                default -> { }
            }
        } while (opcion != 0);
    }

    private void listarPorCategoria() {
        try {
            List<Categoria> categorias = categoriaService.listar();
            CategoriaMenu.mostrar(categorias);
            if (categorias.isEmpty()) {
                return;
            }
            Long id = input.leerIdPositivo("Ingrese el id de la categoria: ");
            mostrar(service.listarPorCategoria(id),
                    "No hay productos cargados para esa categoria.");
        } catch (EntidadNoEncontradaException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void crear() {
        try {
            List<Categoria> categorias = categoriaService.listar();
            CategoriaMenu.mostrar(categorias);
            if (categorias.isEmpty()) {
                return;
            }
            Long categoriaId = input.leerIdPositivo("Ingrese el id de la categoria: ");
            Categoria categoria = categoriaService.buscarPorId(categoriaId);
            String nombre = input.leerTextoObligatorio("Nombre: ");
            String descripcion = input.leerTextoOpcional("Descripcion: ");
            double precio = input.leerDoubleNoNegativo("Precio: ");
            int stock = input.leerEnteroNoNegativo("Stock: ");
            String imagen = input.leerTextoOpcional("Imagen: ");
            boolean disponible = input.leerSiNo(
                    "El producto esta disponible? (S/N): ");
            Producto producto = new Producto(nombre, precio, descripcion, stock,
                    imagen, disponible, categoria);
            service.guardar(producto);
            System.out.println("El producto se creo con el id " + producto.getId() + ".");
        } catch (EntidadNoEncontradaException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void editar() {
        try {
            List<Producto> productos = service.listar();
            if (productos.isEmpty()) {
                System.out.println("No hay productos cargados.");
                return;
            }
            mostrar(productos, "No hay productos cargados.");
            Long id = input.leerIdPositivo("Ingrese el id del producto: ");
            Producto producto = service.buscarPorId(id);
            System.out.println("1. Cambiar nombre");
            System.out.println("2. Cambiar descripcion");
            System.out.println("3. Cambiar precio");
            System.out.println("4. Cambiar stock");
            System.out.println("5. Cambiar imagen");
            System.out.println("6. Cambiar disponibilidad");
            System.out.println("7. Cambiar categoria");
            System.out.println("0. Cancelar");
            System.out.print("Seleccione una opcion: ");
            int opcion = input.leerOpcion(0, 7);
            if (opcion == 0) {
                System.out.println("No se realizaron cambios.");
                return;
            }

            String nombre = producto.getNombre();
            String descripcion = producto.getDescripcion();
            Double precio = producto.getPrecio();
            int stock = producto.getStock();
            String imagen = producto.getImagen();
            boolean disponible = producto.isDisponible();
            Long categoriaId = producto.getCategoria().getId();

            switch (opcion) {
                case 1 -> nombre = input.leerTextoObligatorio("Nuevo nombre: ");
                case 2 -> descripcion = input.leerTextoOpcional("Nueva descripcion: ");
                case 3 -> precio = input.leerDoubleNoNegativo("Nuevo precio: ");
                case 4 -> stock = input.leerEnteroNoNegativo("Nuevo stock: ");
                case 5 -> imagen = input.leerTextoOpcional("Nueva imagen: ");
                case 6 -> disponible = input.leerSiNo(
                        "El producto esta disponible? (S/N): ");
                case 7 -> categoriaId = leerCategoria();
                default -> { }
            }
            service.actualizar(id, nombre, precio, descripcion, stock, imagen,
                    disponible, categoriaId);
            System.out.println("El producto se actualizo correctamente.");
        } catch (EntidadNoEncontradaException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private Long leerCategoria() throws EntidadNoEncontradaException {
        List<Categoria> categorias = categoriaService.listar();
        CategoriaMenu.mostrar(categorias);
        if (categorias.isEmpty()) {
            throw new IllegalArgumentException("No hay categorias disponibles.");
        }
        Long id = input.leerIdPositivo("Ingrese el id de la nueva categoria: ");
        return categoriaService.buscarPorId(id).getId();
    }

    private void eliminar() {
        try {
            List<Producto> productos = service.listar();
            if (productos.isEmpty()) {
                System.out.println("No hay productos cargados.");
                return;
            }
            mostrar(productos, "No hay productos cargados.");
            Long id = input.leerIdPositivo("Ingrese el id del producto: ");
            Producto producto = service.buscarPorId(id);
            if (!input.leerSiNo("Desea eliminar el producto "
                    + producto.getNombre() + "? (S/N): ")) {
                System.out.println("No se elimino el producto.");
                return;
            }
            service.eliminar(id);
            System.out.println("El producto se elimino correctamente.");
        } catch (EntidadNoEncontradaException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    static void mostrar(List<Producto> productos, String mensajeVacio) {
        if (productos.isEmpty()) {
            System.out.println(mensajeVacio);
            return;
        }
        System.out.println("\nProductos disponibles:");
        for (Producto producto : productos) {
            System.out.println(producto);
        }
    }
}
