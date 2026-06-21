package foodstore.entities;

import foodstore.enums.Rol;
import java.util.ArrayList;
import java.util.List;

public class Usuario extends Base {

    private static long ultimoId = 0;

    private String nombre;
    private String apellido;
    private String mail;
    private String celular;
    private String contrasena;
    private Rol rol;
    private List<Pedido> pedidos;

    public Usuario(String nombre, String apellido, String mail, String celular,
            String contrasena, Rol rol) {
        super(++ultimoId);
        this.pedidos = new ArrayList<>();
        this.nombre = nombre;
        this.apellido = apellido;
        this.mail = mail;
        this.celular = celular;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(List<Pedido> pedidos) {
        this.pedidos = new ArrayList<>();
        if (pedidos != null) {
            for (Pedido pedido : pedidos) {
                agregarPedido(pedido);
            }
        }
    }

    public void agregarPedido(Pedido pedido) {
        if (pedido == null) {
            return;
        }
        if (!pedidos.contains(pedido)) {
            pedidos.add(pedido);
        }
        if (pedido.getUsuario() != this) {
            pedido.setUsuario(this);
        }
    }

    public void validar() {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del usuario no puede estar vacio.");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El apellido del usuario no puede estar vacio.");
        }
        if (mail == null || mail.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El mail del usuario no puede estar vacio.");
        }
        if (contrasena == null || contrasena.isBlank()) {
            throw new IllegalArgumentException(
                    "La contrasena del usuario no puede estar vacia.");
        }
        if (rol == null) {
            throw new IllegalArgumentException("El usuario tiene que tener un rol.");
        }
    }

    @Override
    public String toString() {
        return "Usuario " + getId()
                + " - " + nombre + " " + apellido
                + " - " + mail
                + " - rol: " + rol
                + " - pedidos: " + pedidos.size();
    }
}
