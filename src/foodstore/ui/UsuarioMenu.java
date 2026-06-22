package foodstore.ui;

import foodstore.entities.Usuario;
import foodstore.enums.Rol;
import foodstore.exception.EntidadNoEncontradaException;
import foodstore.service.UsuarioService;
import java.util.List;

public final class UsuarioMenu {

    private final InputHelper input;
    private final UsuarioService service;

    public UsuarioMenu(InputHelper input, UsuarioService service) {
        this.input = input;
        this.service = service;
    }

    public void iniciar() {
        int opcion;
        do {
            System.out.println("\nUSUARIOS");
            System.out.println("1. Listar usuarios");
            System.out.println("2. Crear usuario");
            System.out.println("3. Editar usuario");
            System.out.println("4. Eliminar usuario");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opcion: ");
            opcion = input.leerOpcion(0, 4);
            switch (opcion) {
                case 1 -> mostrar(service.listar());
                case 2 -> crear();
                case 3 -> editar();
                case 4 -> eliminar();
                default -> { }
            }
        } while (opcion != 0);
    }

    private void crear() {
        try {
            String nombre = input.leerTextoObligatorio("Nombre: ");
            String apellido = input.leerTextoObligatorio("Apellido: ");
            String mail = leerMailDisponible(null);
            String celular = input.leerTextoOpcional("Celular: ");
            String contrasena = input.leerTextoObligatorio("Contrasena: ");
            Rol rol = leerRol();
            Usuario usuario = new Usuario(
                    nombre, apellido, mail, celular, contrasena, rol);
            service.guardar(usuario);
            System.out.println("El usuario se creo con el id " + usuario.getId() + ".");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void editar() {
        try {
            List<Usuario> usuarios = service.listar();
            if (usuarios.isEmpty()) {
                System.out.println("No hay usuarios cargados.");
                return;
            }
            mostrar(usuarios);
            Long id = input.leerIdPositivo("Ingrese el id del usuario: ");
            Usuario usuario = service.buscarPorId(id);
            System.out.println("1. Cambiar nombre");
            System.out.println("2. Cambiar apellido");
            System.out.println("3. Cambiar mail");
            System.out.println("4. Cambiar celular");
            System.out.println("5. Cambiar contrasena");
            System.out.println("6. Cambiar rol");
            System.out.println("0. Cancelar");
            System.out.print("Seleccione una opcion: ");
            int opcion = input.leerOpcion(0, 6);
            if (opcion == 0) {
                System.out.println("No se realizaron cambios.");
                return;
            }
            if (opcion == 5) {
                service.actualizarContrasena(id,
                        input.leerTextoObligatorio("Nueva contrasena: "));
            } else {
                String nombre = usuario.getNombre();
                String apellido = usuario.getApellido();
                String mail = usuario.getMail();
                String celular = usuario.getCelular();
                Rol rol = usuario.getRol();
                switch (opcion) {
                    case 1 -> nombre = input.leerTextoObligatorio("Nuevo nombre: ");
                    case 2 -> apellido = input.leerTextoObligatorio("Nuevo apellido: ");
                    case 3 -> mail = leerMailDisponible(usuario.getMail());
                    case 4 -> celular = input.leerTextoOpcional("Nuevo celular: ");
                    case 6 -> rol = leerRol();
                    default -> { }
                }
                service.actualizar(id, nombre, apellido, mail, celular, rol);
            }
            System.out.println("El usuario se actualizo correctamente.");
        } catch (EntidadNoEncontradaException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void eliminar() {
        try {
            List<Usuario> usuarios = service.listar();
            if (usuarios.isEmpty()) {
                System.out.println("No hay usuarios cargados.");
                return;
            }
            mostrar(usuarios);
            Long id = input.leerIdPositivo("Ingrese el id del usuario: ");
            Usuario usuario = service.buscarPorId(id);
            if (!input.leerSiNo("Desea eliminar al usuario " + usuario.getNombre()
                    + " " + usuario.getApellido() + "? (S/N): ")) {
                System.out.println("No se elimino el usuario.");
                return;
            }
            service.eliminar(id);
            System.out.println("El usuario se elimino correctamente.");
        } catch (EntidadNoEncontradaException | IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private String leerMailDisponible(String mailActual) {
        while (true) {
            String mail = input.leerTextoObligatorio(
                    mailActual == null ? "Mail: " : "Nuevo mail: ");
            try {
                if (mailActual != null && mail.equalsIgnoreCase(mailActual)) {
                    return mail;
                }
                if (!service.existeMail(mail)) {
                    return mail;
                }
                System.out.println("Ya existe un usuario con ese mail.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private Rol leerRol() {
        System.out.println("Rol:");
        System.out.println("1. ADMIN");
        System.out.println("2. USUARIO");
        System.out.print("Seleccione una opcion: ");
        return input.leerOpcion(1, 2) == 1 ? Rol.ADMIN : Rol.USUARIO;
    }

    static void mostrar(List<Usuario> usuarios) {
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios cargados.");
            return;
        }
        System.out.println("\nUsuarios disponibles:");
        for (Usuario usuario : usuarios) {
            System.out.println("Usuario " + usuario.getId() + " - "
                    + usuario.getNombre() + " " + usuario.getApellido() + " - "
                    + usuario.getMail() + " - rol: " + usuario.getRol());
        }
    }
}
