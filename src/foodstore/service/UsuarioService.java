package foodstore.service;

import foodstore.entities.Usuario;
import foodstore.enums.Rol;
import foodstore.exception.EntidadNoEncontradaException;
import foodstore.util.Buscador;
import java.util.ArrayList;
import java.util.List;

public class UsuarioService {

    private final ArrayList<Usuario> usuarios;
    private final Buscador<Usuario, Long> buscador;

    public UsuarioService() {
        usuarios = new ArrayList<>();
        buscador = new Buscador<>();
    }

    public void guardar(Usuario usuario) {
        validarUsuario(usuario);
        normalizarUsuario(usuario);
        if (buscarPorMail(usuario.getMail()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un usuario con el mail " + usuario.getMail() + ".");
        }
        usuarios.add(usuario);
    }

    public Usuario buscarPorId(Long id) throws EntidadNoEncontradaException {
        validarId(id);
        Usuario usuario = buscador.buscar(usuarios, id);
        if (usuario == null || usuario.isEliminado()) {
            throw new EntidadNoEncontradaException(
                    "No se encontro un usuario activo con el id " + id + ".");
        }
        return usuario;
    }

    public List<Usuario> listar() {
        ArrayList<Usuario> usuariosActivos = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (!usuario.isEliminado()) {
                usuariosActivos.add(usuario);
            }
        }
        return usuariosActivos;
    }

    public boolean existeMail(String mail) {
        validarMail(mail);
        String mailLimpio = mail.trim().toLowerCase();
        return buscarPorMail(mailLimpio) != null;
    }

    public void actualizar(Long id, String nombre, String apellido, String mail,
            String celular, Rol rol) throws EntidadNoEncontradaException {
        Usuario usuarioGuardado = buscarPorId(id);
        validarDatos(nombre, apellido, mail, rol);
        String mailNormalizado = mail.trim().toLowerCase();
        Usuario usuarioConMismoMail = buscarPorMail(mailNormalizado);
        if (usuarioConMismoMail != null
                && !usuarioConMismoMail.sameId(usuarioGuardado.getId())) {
            throw new IllegalArgumentException(
                    "Ya existe un usuario con el mail " + mailNormalizado + ".");
        }
        usuarioGuardado.setNombre(nombre.trim());
        usuarioGuardado.setApellido(apellido.trim());
        usuarioGuardado.setMail(mailNormalizado);
        usuarioGuardado.setCelular(celular == null ? "" : celular.trim());
        usuarioGuardado.setRol(rol);
    }

    public void actualizarContrasena(Long id, String contrasena)
            throws EntidadNoEncontradaException {
        Usuario usuarioGuardado = buscarPorId(id);
        if (contrasena == null || contrasena.isBlank()) {
            throw new IllegalArgumentException(
                    "La contrasena del usuario no puede estar vacia.");
        }
        usuarioGuardado.setContrasena(contrasena);
    }

    public void eliminar(Long id) throws EntidadNoEncontradaException {
        Usuario usuario = buscarPorId(id);
        usuario.setEliminado(true);
    }

    private void validarUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede estar vacio.");
        }
        usuario.validar();
        validarMail(usuario.getMail());
    }

    private void validarDatos(String nombre, String apellido, String mail, Rol rol) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre del usuario no puede estar vacio.");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El apellido del usuario no puede estar vacio.");
        }
        validarMail(mail);
        if (rol == null) {
            throw new IllegalArgumentException("El usuario tiene que tener un rol.");
        }
    }

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException(
                    "El id del usuario tiene que ser mayor a 0.");
        }
    }

    private void validarMail(String mail) {
        if (mail == null || mail.trim().isEmpty()) {
            throw new IllegalArgumentException("El mail del usuario no puede estar vacio.");
        }

        String mailLimpio = mail.trim();
        int posicionArroba = mailLimpio.indexOf('@');
        int posicionPunto = mailLimpio.lastIndexOf('.');
        boolean formatoValido = posicionArroba > 0
                && posicionPunto > posicionArroba + 1
                && posicionPunto < mailLimpio.length() - 1
                && !mailLimpio.contains(" ");
        if (!formatoValido) {
            throw new IllegalArgumentException("El formato del mail no es valido.");
        }
    }

    private Usuario buscarPorMail(String mail) {
        int i = 0;
        while (i < usuarios.size()
                && !usuarios.get(i).getMail().equalsIgnoreCase(mail)) {
            i++;
        }
        if (i < usuarios.size()) {
            return usuarios.get(i);
        }
        return null;
    }

    private void normalizarUsuario(Usuario usuario) {
        usuario.setNombre(usuario.getNombre().trim());
        usuario.setApellido(usuario.getApellido().trim());
        usuario.setMail(usuario.getMail().trim().toLowerCase());
        usuario.setCelular(
                usuario.getCelular() == null ? "" : usuario.getCelular().trim());
    }

}
