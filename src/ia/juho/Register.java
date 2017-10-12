package ia.juho;

/*
 * Clase usada para almacenar los valores de los registros leidos del archivo secuencial indexado
 */

public class Register {
    public char value; //Valor del nodo (nombre)
    public char[] childs_keys; //Nodos conectados
    public int[] childs_weight; //Peso de los nodos conectados
    public final int CONNECTIONS_NUMBER = 5; //Numero de maximo conexiones por nodo

    public Register(){
        this.childs_keys = new char[CONNECTIONS_NUMBER];
        this.childs_weight = new int[CONNECTIONS_NUMBER];
    }
}
