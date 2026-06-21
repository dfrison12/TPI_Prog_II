package foodstore.ui;

import java.util.Scanner;

public final class InputHelper {

    private final Scanner scanner;

    public InputHelper(Scanner scanner) {
        if (scanner == null) {
            throw new IllegalArgumentException("El scanner no puede ser nulo.");
        }
        this.scanner = scanner;
    }

    public int leerOpcion(int minimo, int maximo) {
        while (true) {
            String entrada = scanner.nextLine().trim();
            try {
                int opcion = Integer.parseInt(entrada);
                if (opcion >= minimo && opcion <= maximo) {
                    return opcion;
                }
            } catch (NumberFormatException e) {
                // El mensaje comun se muestra debajo.
            }
            System.out.print("Ingrese una opcion entre " + minimo + " y " + maximo + ": ");
        }
    }

    public Long leerIdPositivo(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                long valor = Long.parseLong(scanner.nextLine().trim());
                if (valor > 0) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // El mensaje comun se muestra debajo.
            }
            System.out.println("Ingrese un id numerico mayor a 0.");
        }
    }

    public double leerDoubleNoNegativo(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                double valor = Double.parseDouble(scanner.nextLine().trim());
                if (Double.isFinite(valor) && valor >= 0) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // El mensaje comun se muestra debajo.
            }
            System.out.println("Ingrese un numero mayor o igual a 0.");
        }
    }

    public int leerEnteroNoNegativo(String mensaje) {
        return leerEntero(mensaje, false);
    }

    public int leerEnteroPositivo(String mensaje) {
        return leerEntero(mensaje, true);
    }

    private int leerEntero(String mensaje, boolean positivo) {
        while (true) {
            System.out.print(mensaje);
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                if (positivo ? valor > 0 : valor >= 0) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // El mensaje comun se muestra debajo.
            }
            String condicion = positivo ? "mayor a 0" : "mayor o igual a 0";
            System.out.println("Ingrese un numero entero " + condicion + ".");
        }
    }

    public boolean leerSiNo(String mensaje) {
        System.out.print(mensaje);
        while (true) {
            String respuesta = scanner.nextLine().trim();
            if (respuesta.equalsIgnoreCase("S")) {
                return true;
            }
            if (respuesta.equalsIgnoreCase("N")) {
                return false;
            }
            System.out.print("Ingrese S o N: ");
        }
    }

    public String leerTextoObligatorio(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = scanner.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("El dato no puede estar vacio.");
        }
    }

    public String leerTextoOpcional(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }
}
