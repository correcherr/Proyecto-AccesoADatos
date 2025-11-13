
package com.mongo.view;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Scanner;

public class ConsolaView {
    private final Scanner sc = new Scanner(System.in);

    public int menu() {
        System.out.println("""
                1. Alta professor
                2. Alta grup
                3. Baixa grup
                4. Alta excursió
                5. Baixa excursió
                6. Modificació excursió
                7. Llistat total d’excursions amb els noms i cognoms dels professors responsables de cadascuna.
                8. Llistat d’excursions finalitzades que s’han realitzat entre dues dates donades.
                9. Fer ressenya
                10. Llistat de ressenyes donat rang de puntuació.
                11. Eixir
                                """.trim());
        return leerEntero();

    }

    public String pedir(String etiqueta) {
        System.out.print(etiqueta + ": ");
        return sc.nextLine().trim();
    }

    /**
     * Pide un número entero al usuario, VALIDANDO que lo introduzca correctamente.
     */
    public int pedirEntero(String etiqueta) {
        System.out.print(etiqueta + ": ");
        return leerEntero();
    }

    public LocalDate pedirFecha(String etiqueta) {
        System.out.print(etiqueta + ": ");
        return leerFecha();
    }

    public double pedirDouble(String etiqueta) {
        System.out.print(etiqueta + ": ");
        return leerDouble();
    }

    public void info(String msg) {
        System.out.println(msg);
    }

    /**
     * Este otro metodo es para mostrar los mensajes de errorr.
     */
    public void error(String msg) {
        System.err.println("ERROR: " + msg);
    }

    /**
     * Lee un número entero desde teclado y repite mientras no sea válido.
     */
    private int leerEntero() {
        while (true) {
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.print("Introduce un número entero válido: ");
            }
        }
    }

    private LocalDate leerFecha() {
        while (true) {
            String s = sc.nextLine().trim();
            try {
                LocalDate parsedDate = LocalDate.parse(s);

                return parsedDate;
            } catch (DateTimeException e) {
                System.out.print("Introduce una fecha válida: ");
            }
        }
    }

    private double leerDouble() {
        while (true) {
            String s = sc.nextLine().trim();
            try {
                return Double.parseDouble(s);
            } catch (NumberFormatException e) {
                System.out.print("Introduce un número decimal válido: ");
            }
        }
    }
}
