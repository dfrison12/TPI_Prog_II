package foodstore.entities;

import java.util.Locale;

public class Producto extends Base {

    private static long ultimoId = 0;

    private String nombre;
    private Double precio;
    private String descripcion;
    private int stock;
    private String imagen;
    private boolean disponible;
    private Categoria categoria;

    public Producto(String nombre, Double precio, String descripcion, int stock,
            String imagen, boolean disponible, Categoria categoria) {
        super(++ultimoId);
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.stock = stock;
        this.imagen = imagen;
        this.disponible = disponible;
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public final void setCategoria(Categoria categoria) {
        if (this.categoria == categoria) {
            return;
        }
        Categoria categoriaAnterior = this.categoria;
        this.categoria = categoria;
        if (categoriaAnterior != null) {
            categoriaAnterior.getProductos().remove(this);
        }
        if (categoria != null && !categoria.getProductos().contains(this)) {
            categoria.agregarProducto(this);
        }
    }

    public void validar() {
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
        if (categoria == null || categoria.getId() == null
                || categoria.getId() <= 0) {
            throw new IllegalArgumentException(
                    "El producto tiene que tener una categoria valida.");
        }
    }

    @Override
    public String toString() {
        String nombreCategoria = categoria == null ? "Sin categoria" : categoria.getNombre();
        return "Producto " + getId()
                + " - " + nombre
                + " - $" + String.format(Locale.US, "%.2f", precio)
                + " - stock: " + stock
                + " - categoria: " + nombreCategoria;
    }
}
