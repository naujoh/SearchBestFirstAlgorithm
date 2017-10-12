package ia.juho;

import java.io.*;
import java.util.StringTokenizer;

public class FilesManager {

    private RandomAccessFile master_file;
    private RandomAccessFile index_file;
    private long register_size;
    public Register o_register;

    //Se definen constructores
    public FilesManager() {
        this.o_register = new Register();
    }

    public FilesManager(Register register) {
        this.o_register = register;
    }

    //Escribe en el archivo de acceso aleatrio un registro que se obtiene a partir de un objeto tipo Register
    public void writeInFiles() throws IOException {
        int logic_address;
        this.master_file = new RandomAccessFile("master_file", "rw");
        this.index_file = new RandomAccessFile("index_file", "rw");

        if (this.master_file.length() != 0) {
            getRegisterSize(this.master_file);
            this.master_file.seek(this.master_file.length());
            this.index_file.seek(this.index_file.length());
            logic_address = ((int) (this.master_file.length() / this.register_size)) + 1;
        } else {
            logic_address = 1;
        }

        this.index_file.writeChar(this.o_register.value);  //WRITE VALUE TO INDEX_FILE
        this.index_file.writeInt(logic_address);                 //LOGIC_ADDR TO INDEX_FILE
        this.master_file.writeChar(this.o_register.value); //WRITE VALUE TO MASTER_FILE

        for (int i = 0; i < o_register.CONNECTIONS_NUMBER; i++) {
            this.master_file.writeChar(this.o_register.childs_keys[i]); //WRITE VALUE CONNECTED TO NODE
            this.master_file.writeInt(this.o_register.childs_weight[i]); //WRITE WEIGHT OF VALUE CONNECTED TO NODE
        }

        master_file.close();
        index_file.close();
    }

    //Leer el archivo de acceso aleatorio de manera secuencial
    public void readSequential() throws IOException {
        int i;
        this.master_file = new RandomAccessFile("master_file", "r");
        while (master_file.getFilePointer() != master_file.length()) {
            //Read Data
            Register reg = new Register();
            reg.value = this.master_file.readChar();
            for (int j = 0; j < reg.CONNECTIONS_NUMBER; j++) {
                reg.childs_keys[j] = this.master_file.readChar();
                reg.childs_weight[j] = this.master_file.readInt();
            }
            //Print info
            System.out.println("----------------------------------------------------------");
            System.out.println("NODO | " + reg.value);
            for (i = 0; i < reg.CONNECTIONS_NUMBER; i++) {
                if (reg.childs_keys[i] != '-') {
                    System.out.println("CONECTADO | " + reg.childs_keys[i]);
                    System.out.println("PESO | " + reg.childs_weight[i]);
                }
            }
            System.out.println("----------------------------------------------------------");
        }
    }
    //Obtener un valor objeto Regigster (Representa un registro) a partir de una llave buscada
    public Register readRandom(char node_key) throws IOException {
        char key = ' ';
        long overflow = 0;
        int logic_address = 0;
        Register reg = null;
        this.master_file = new RandomAccessFile("master_file", "r");
        this.index_file = new RandomAccessFile("index_file", "r");
        while (index_file.getFilePointer() != index_file.length()) {
            key = this.index_file.readChar();
            if (key == node_key) {
                logic_address = index_file.readInt();
                getRegisterSize(this.master_file);
                overflow = (logic_address - 1) * this.register_size;
                this.master_file.seek(overflow);
                reg = new Register();
                reg.value = master_file.readChar();
                for (int k = 0; k < reg.CONNECTIONS_NUMBER; k++) {
                    reg.childs_keys[k] = master_file.readChar();
                    reg.childs_weight[k] = master_file.readInt();
                }
                break;
            }
            index_file.readInt();
        }
        return reg;
    }

    //Obtener el tamano del registro
    private void getRegisterSize(RandomAccessFile file) throws IOException {
        file.readChar();
        for (int j = 0; j < this.o_register.CONNECTIONS_NUMBER; j++) {
            file.readChar(); //VALUE CHILD CONNECTED
            file.readInt();  //WEIGHT OF CHILD CONNECTED
        }
        this.register_size = file.getFilePointer();
    }

    //Metodo para leer los registros del grafo de un archivo de texto y pasarlos a un archivo de acceso aleatorio
    public void readArchiveText() throws FileNotFoundException, IOException {
        String read;
        FileReader fr = new FileReader("nodes.txt");
        BufferedReader bf = new BufferedReader(fr);
        this.o_register = new Register();

        while ((read = bf.readLine()) != null) {
            StringTokenizer tokens = new StringTokenizer(read, ",");
            while (tokens.hasMoreTokens()) {
                this.o_register.value = tokens.nextToken().charAt(0);
                for (int i = 0; i < this.o_register.CONNECTIONS_NUMBER; i++) {
                    if (tokens.hasMoreTokens()) {
                        this.o_register.childs_keys[i] = tokens.nextToken().charAt(0);
                        this.o_register.childs_weight[i] = Integer.parseInt(tokens.nextToken());
                    } else {
                        this.o_register.childs_keys[i] = '-';
                        this.o_register.childs_weight[i] = -1;
                    }
                }
                writeInFiles();
            }
        }
    }
}
