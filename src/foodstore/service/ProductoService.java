package foodstore.service;

import foodstore.entities.Categoria;
import foodstore.entities.Producto;
import foodstore.exception.EntidadNoEncontradaException;
import foodstore.util.Buscador;
import java.util.ArrayList;
import java.util.List;

public class ProductoService {

    private final ArrayList<Producto> productos;
    private final Buscador<Producto, Long> buscador;
    private final CategoriaService categoriaService;

    public ProductoService(CategoriaService categoriaService) {
        productos = new ArrayList<>();
        buscador = new Buscador<>();
        this.categoriaService = categoriaService;
    }

    public void guardar(Producto producto) throws EntidadNoEncontradaException {
        validarProducto(producto);
        Categoria categoriaActiva = categoriaService.buscarPorId(
                producto.getCategoria().getId());
        normalizarProducto(producto);
        producto.setCategoria(null);
        producto.setCategoria(categoriaActiva);
        productos.add(producto);
    }

    public Producto buscarPorId(Long id) throws EntidadNoEncontradaException {
        validarIdProducto(id);
        Producto producto = buscador.buscar(productos, id);
        if (producto == null || producto.isEliminado()) {
            throw new EntidadNoEncontradaException(
                    "No se encontro un producto activo con el id " + id + ".");
        }
        return producto;
    }

    public List<Producto> listar() {
        ArrayList<Producto> productosActivos = new ArrayList<>();
        for (Producto producto : productos) {
            if (!producto.isEliminado()) {
                productosActivos.add(producto);
            }
        }
        return productosActivos;
    }

    public List<Producto> listarPorCategoria(Long categoriaId)
            throws EntidadNoEncontradaException {
        Categoria categoria = categoriaService.buscarPorId(categoriaId);
        ArrayList<Producto> productosFiltrados = new ArrayList<>();
        for (Producto producto : productos) {
            if (!producto.isEliminado() && producto.getCategoria().equals(categoria)) {
                productosFiltrados.add(producto);
            }
        }
        return productosFiltrados;
    }

    public void actualizar(Long id, String nombre, Double precio, String descripcion,
            int stock, String imagen, boolean disponible, Long categoriaId)
            throws EntidadNoEncontradaException {
        Producto productoGuardado = buscarPorId(id);
        Categoria categoriaActiva = categoriaService.buscarPorId(categoriaId);
        validarDatos(nombre, precio, stock);
        productoGuardado.setNombre(nombre.trim());
        productoGuardado.setDescripcion(limpiarTextoOpcional(descripcion));
        productoGuardado.setPrecio(precio);
        productoGuardado.setStock(stock);
        productoGuardado.setImagen(limpiarTextoOpcional(imagen));
        productoGuardado.setDisponible(disponible);
        productoGuardado.setCategoria(categoriaActiva);
    }

    public void eliminar(Long id) throws EntidadNoEncontradaException {
        Producto producto = buscarPorId(id);
        producto.setEliminado(true);
    }

    private void validarIdProducto(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "El id del producto tiene que ser mayor a 0.");
        }
    }

    private void validarProducto(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede estar vacio.");
        }
        producto.validar();
    }

    private void validarDatos(String nombre, Double precio, int stock) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del producto no puede estar vacio.");
        }
        if (precio == null || !Double.isFinite(precio) || precio < 0) {
            throw new IllegalArgumentException(
                    "El precio del producto no puede ser menor a 0.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException(
                    "El stock del producto no puede ser menor a 0.");
        }
    }

    private void normalizarProducto(Producto producto) {
        producto.setNombre(producto.getNombre().trim());
        producto.setDescripcion(limpiarTextoOpcional(producto.getDescripcion()));
        producto.setImagen(limpiarTextoOpcional(producto.getImagen()));
    }

    private String limpiarTextoOpcional(String texto) {
        return texto == null ? "" : texto.trim();
    }
}
