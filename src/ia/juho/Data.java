package ia.juho;
/*
 * Clase usada para almacenar los valores de peso y el padre en las tablas ABIERTA y CERRADA
 */

public class Data {

    public int f; //Inidica el valor de la funcion f
    public char p; //Indica el padre

    public Data(char p,int f) {
        this.f = f;
        this.p = p;
    }

}
