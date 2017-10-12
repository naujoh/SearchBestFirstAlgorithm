package ia.juho;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        FilesManager o_fm;
        Register o_register;
        String key, city;
        int menu_option, another_reg = 0;
        do {
            System.out.println("==========================================================");
            System.out.println("\nIngresa:\n1)Escribir de archivo txt"
                    + "\n2)Leer secuencial \n3)Busqueda primero el mejor \n4)Salir");
            System.out.println("==========================================================");
            Scanner input = new Scanner(System.in);
            menu_option = input.nextInt();
            switch (menu_option) {
                case 1:
                    try {
                        o_fm = new FilesManager(new Register());
                        o_fm.readArchiveText();
                    } catch (Exception e) {
                        System.out.println("ERROR AL ESCRIBIR EN ARCHIVO");
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    o_fm = new FilesManager();
                    try {
                        o_fm.readSequential();
                    } catch (Exception e) {
                        System.out.println("OCURRIO UN ERROR AL LEER EL ARCHIVO DE MANERA SCUENCIAL");
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    System.out.println("METODO DE BUSQUEDA PRIMERO EL MEJOR");
                    Character origin, destiny;
                    System.out.println("Ingresa el nombre del nodo origen:");
                    origin = input.next().charAt(0);
                    System.out.println("Ingresa el nombre del nodo destino:");
                    destiny = input.next().charAt(0);
                    BestFirstMethod firstMethod = new BestFirstMethod();
                     {
                        try {
                            firstMethod.BestFirstMethodA(origin, destiny);
                        } catch (IOException ex) {
                            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    break;
                default:
                    break;
            }
        } while (menu_option != 4);
    }

}
