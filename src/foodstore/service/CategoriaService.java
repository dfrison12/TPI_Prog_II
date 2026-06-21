package foodstore.service;

import foodstore.entities.Categoria;
import foodstore.entities.Producto;
import foodstore.exception.EntidadNoEncontradaException;
import foodstore.util.Buscador;
import java.util.ArrayList;
import java.util.List;

public class CategoriaService {

    private final ArrayList<Categoria> categorias;
    private final Buscador<Categoria, Long> buscador;

    public CategoriaService() {
        categorias = new ArrayList<>();
        buscador = new Buscador<>();
    }

    public void guardar(Categoria categoria) {
        validarCategoria(categoria);
        if (buscarPorNombre(categoria.getNombre()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe una categoria con el nombre "
                    + categoria.getNombre().trim() + ".");
        }
        categoria.setNombre(categoria.getNombre().trim());
        categoria.setDescripcion(categoria.getDescripcion().trim());
        categorias.add(categoria);
    }

    public Categoria buscarPorId(Long id) throws EntidadNoEncontradaException {
        validarId(id);
        Categoria categoria = buscador.buscar(categorias, id);
        if (categoria == null || categoria.isEliminado()) {
            throw new EntidadNoEncontradaException(
                    "No se encontro una categoria activa con el id " + id + ".");
        }
        return categoria;
    }

    public List<Categoria> listar() {
        ArrayList<Categoria> categoriasActivas = new ArrayList<>();
        for (Categoria categoria : categorias) {
            if (!categoria.isEliminado()) {
                categoriasActivas.add(categoria);
            }
        }
        return categoriasActivas;
    }

    public boolean existeNombre(String nombre, Long idActual) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre de la categoria no puede estar vacio.");
        }
        Categoria categoria = buscarPorNombre(nombre);
        return categoria != null
                && (idActual == null || !categoria.sameId(idActual));
    }

    public void actualizar(Long id, String nombre, String descripcion)
            throws EntidadNoEncontradaException {
        validarId(id);
        Categoria categoriaGuardada = buscarPorId(id);
        validarDatos(nombre, descripcion);
        Categoria categoriaConMismoNombre = buscarPorNombre(nombre);
        if (categoriaConMismoNombre != null
                && !categoriaConMismoNombre.sameId(id)) {
            throw new IllegalArgumentException(
                    "Ya existe una categoria con el nombre " + nombre.trim() + ".");
        }
        categoriaGuardada.setNombre(nombre.trim());
        categoriaGuardada.setDescripcion(descripcion.trim());
    }

    public void eliminar(Long id) throws EntidadNoEncontradaException {
        Categoria categoria = buscarPorId(id);
        for (Producto producto : categoria.getProductos()) {
            if (!producto.isEliminado()) {
                throw new IllegalArgumentException(
                        "La categoria no se puede eliminar porque tiene productos activos.");
            }
        }
        categoria.setEliminado(true);
    }

    private void validarCategoria(Categoria categoria) {
        if (categoria == null) {
            throw new IllegalArgumentException("La categoria no puede estar vacia.");
        }
        categoria.validar();
    }

    private void validarDatos(String nombre, String descripcion) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre de la categoria no puede estar vacio.");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La descripcion de la categoria no puede estar vacia.");
        }
    }

    private Categoria buscarPorNombre(String nombre) {
        int i = 0;
        while (i < categorias.size()
                && !categorias.get(i).getNombre().equalsIgnoreCase(nombre.trim())) {
            i++;
        }
        if (i < categorias.size()) {
            return categorias.get(i);
        }
        return null;
    }

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "El id de la categoria tiene que ser mayor a 0.");
        }
    }
}
