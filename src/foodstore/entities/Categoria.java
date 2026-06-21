package foodstore.entities;

import java.util.ArrayList;
import java.util.List;

public class Categoria extends Base {

    private static long ultimoId = 0;

    private String nombre;
    private String descripcion;
    private List<Producto> productos;

    public Categoria(String nombre, String descripcion) {
        super(++ultimoId);
        this.productos = new ArrayList<>();
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = new ArrayList<>();
        if (productos != null) {
            for (Producto producto : productos) {
                agregarProducto(producto);
            }
        }
    }

    public void agregarProducto(Producto producto) {
        if (producto == null) {
            return;
        }
        if (!productos.contains(producto)) {
            productos.add(producto);
        }
        if (producto.getCategoria() != this) {
            producto.setCategoria(this);
        }
    }

    public void validar() {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre de la categoria no puede estar vacio.");
        }
        if (descripcion == null || descripcion.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "La descripcion de la categoria no puede estar vacia.");
        }
    }

    @Override
    public String toString() {
        return "Categoria " + getId()
                + " - " + nombre
                + " - " + descripcion
                + " - productos: " + productos.size();
    }
}
