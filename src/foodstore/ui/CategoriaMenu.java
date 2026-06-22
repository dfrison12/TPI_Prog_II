package foodstore.ui;

import foodstore.entities.Categoria;
import foodstore.exception.EntidadNoEncontradaException;
import foodstore.service.CategoriaService;
import java.util.List;

public final class CategoriaMenu {

    private final InputHelper input;
    private final CategoriaService service;

    public CategoriaMenu(InputHelper input, CategoriaService service) {
        this.input = input;
        this.service = service;
    }

    public void iniciar() {
        int opcion;
        do {
            System.out.println("\nCATEGORIAS");
            System.out.println("1. Listar categorias");
            System.out.println("2. Crear categoria");
            System.out.println("3. Editar categoria");
            System.out.println("4. Eliminar categoria");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opcion: ");
            opcion = input.leerOpcion(0, 4);
            switch (opcion) {
                case 1 -> listar();
                case 2 -> crear();
                case 3 -> editar();
                case 4 -> eliminar();
                default -> { }
            }
        } while (opcion != 0);
    }

    private void listar() {
        mostrar(service.listar());
    }

    private void crear() {
        try {
            String nombre = leerNombreDisponible(null);
            String descripcion = input.leerTextoObligatorio("Descripcion: ");
            Categoria categoria = new Categoria(nombre, descripcion);
            service.guardar(categoria);
            System.out.println("La categoria se creo con el id " + categoria.getId() + ".");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void editar() {
        try {
            if (!mostrarSiHayRegistros()) {
                return;
            }
            Long id = input.leerIdPositivo("Ingrese el id de la categoria: ");
            Categoria categoria = service.buscarPorId(id);
            System.out.println("1. Cambiar nombre");
            System.out.println("2. Cambiar descripcion");
            System.out.println("0. Cancelar");
            System.out.print("Seleccione una opcion: ");
            int opcion = input.leerOpcion(0, 2);
            if (opcion == 0) {
                System.out.println("No se realizaron cambios.");
                return;
            }
            String nombre = categoria.getNombre();
            String descripcion = categoria.getDescripcion();
            if (opcion == 1) {
                nombre = leerNombreDisponible(id);
            } else {
                descripcion = input.leerTextoObligatorio("Nueva descripcion: ");
            }
            service.actualizar(id, nombre, descripcion);
            System.out.println("La categoria se actualizo correctamente.");
        } catch (EntidadNoEncontradaException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void eliminar() {
        try {
            if (!mostrarSiHayRegistros()) {
                return;
            }
            Long id = input.leerIdPositivo("Ingrese el id de la categoria: ");
            Categoria categoria = service.buscarPorId(id);
            if (!input.leerSiNo("Desea eliminar la categoria "
                    + categoria.getNombre() + "? (S/N): ")) {
                System.out.println("No se elimino la categoria.");
                return;
            }
            service.eliminar(id);
            System.out.println("La categoria se elimino correctamente.");
        } catch (EntidadNoEncontradaException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private String leerNombreDisponible(Long idActual) {
        while (true) {
            String nombre = input.leerTextoObligatorio(
                    idActual == null ? "Nombre: " : "Nuevo nombre: ");
            try {
                if (!service.existeNombre(nombre, idActual)) {
                    return nombre;
                }
                System.out.println("Ya existe una categoria con ese nombre.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private boolean mostrarSiHayRegistros() {
        List<Categoria> categorias = service.listar();
        if (categorias.isEmpty()) {
            System.out.println("No hay categorias cargadas.");
            return false;
        }
        mostrar(categorias);
        return true;
    }

    static void mostrar(List<Categoria> categorias) {
        if (categorias.isEmpty()) {
            System.out.println("No hay categorias cargadas.");
            return;
        }
        System.out.println("\nCategorias disponibles:");
        for (Categoria categoria : categorias) {
            System.out.println("Categoria " + categoria.getId() + " - "
                    + categoria.getNombre() + " - " + categoria.getDescripcion());
        }
    }
}
