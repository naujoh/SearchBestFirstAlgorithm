package ia.juho;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BestFirstMethod {

    public void BestFirstMethodA(char origin, char destiny) throws IOException {
        LinkedHashMap<Character, Data> open = new LinkedHashMap<>();
        LinkedHashMap<Character, Data> close = new LinkedHashMap<>();
        List<Data> nodeChildrens;
        Map.Entry<Character, Data> ea;
        int f;
        int oldF;

        open.put(origin, new Data(origin, 0)); //1.  ABIERTOS←(n0); CERRADOS←( ) 

        while (!open.isEmpty()) {                 //2. si ABIERTOSes la lista vacía, fin con fallo.
            //3. EA←primer elemento de ABIERTOS. Eliminar EAde ABIERTOSy llevarlo a CERRADOS.       
            ea = moveFirstTo(open, close);

            //4. Expandir nodo EA, generando todos sus sucesores como hijos de EA.
            nodeChildrens = expandNode(ea.getKey());
            // 5. Si alguno de los sucesores de EA es una meta, fin con éxito. 
            if (contains(nodeChildrens, destiny)) {
                // Devolver el camino hasta la meta.
                int aux_weight = 0;
                for(int i = 0; i < nodeChildrens.size(); i++){
                    if(nodeChildrens.get(i).p == destiny)
                        aux_weight = nodeChildrens.get(i).f;
                }
                getFinalPath(origin, destiny, aux_weight, ea, close);
                return;
            } else {
                for (Data currentNode : nodeChildrens) {                   //6. Para cada sucesor de EA                    
                    f = calculateF(currentNode, ea, open, close);      // a) Calcular f(q)
//                    System.out.println(currentNode.p);
//                    System.out.println(f);
                    if (open.containsKey(currentNode.p) || close.containsKey(currentNode.p)) {
                        //c) Si q estaba en ABIERTOSo en CERRADOS,
                        //comparar el nuevo valor f(q) con el anterior.
                        oldF = getInitToN(currentNode, open, close);
                        if (oldF < f) {
                            //Si el nuevo es menor, colocar EA como nuevo padre 
                            //y asignar el nuevo valor f(q).                            
                            open.replace(currentNode.p, new Data(ea.getKey(), f));
                            if (close.containsKey(currentNode.p)) { //Si estaba en CERRADOS
                                open.put(currentNode.p, close.get(currentNode.p)); //Llevarle a ABIERTOS.
                                close.remove(currentNode.p); // Eiminarle de CERRADOS
                            }

                        }   //Si el anterior es menor o igual, descartar el nodo recién generado.

                    } else { //b) Si q no estaba en ABIERTOS ni en CERRADOS,
                        //colocarlo en ABIERTOS, asignando el valor f(q)
                        open.put(currentNode.p, new Data(ea.getKey(), f));

                    }
                }

            }
            // 7. Reordenar ABIERTOSsegún valores crecientes de f.
            open = sortHashAscendingly(open);

        }
        System.out.println("No se encontro el nodo buscado");
    }
    // Obtener la ruta del nodo final al nodo inicial
    private void getFinalPath(Character initial, Character destiny, int destiny_cost,
                             Map.Entry<Character, Data> parent, LinkedHashMap<Character, Data> close){
        Character current_parent = parent.getKey();
        int weight = destiny_cost;
        System.out.printf(destiny + " < ");
        if(close.containsKey(current_parent))
            weight += close.get(current_parent).f;
        while(current_parent!=initial) {
            System.out.printf(current_parent.toString() + " < ");
            if (close.containsKey(current_parent)) {
                current_parent = close.get(current_parent).p;
            }
        }
        System.out.println(current_parent.toString() + " <<padre | costo: " + weight);
    }

    // Calcula el valor de f siguiendo con > costo(inicial, n) + costo(n,nodo_actual)
    private int calculateF(Data currentNode, Map.Entry<Character, Data> ea,
            LinkedHashMap<Character, Data> open,
            LinkedHashMap<Character, Data> close) {

        if (open.containsKey(ea.getKey())) {
            return currentNode.f + open.get(ea.getKey()).f;
        } else if (close.containsKey(ea.getKey())) {
            return currentNode.f + close.get(ea.getKey()).f;
        } else {
            return 0;
        }

    }

    //Obtener el costo del nodo inicial hasta el nodo n
    private int getInitToN(Data currentNode,
            LinkedHashMap<Character, Data> open,
            LinkedHashMap<Character, Data> close
    ) {
        if (open.containsKey(currentNode.p)) {
            return currentNode.f + open.get(currentNode.p).f;
        } else if (close.containsKey(currentNode.p)) {
            return currentNode.f + close.get(currentNode.p).f;
        } else {
            return 0;
        }

    }

    //Determinar si un nodo se encuentra en la lista de nodos expandidos
    private boolean contains(List<Data> list, char node) {
        return list.stream().anyMatch((data) -> (data.p == node));

    }

    //Obtener una lista de nodos expandidos a partir de un padre
    private List<Data> expandNode(char node) throws IOException {
        FilesManager data = new FilesManager();
        List<Data> expandedNode = new ArrayList<>();

        Register register = data.readRandom(node);
        for (int i = 0; i < register.CONNECTIONS_NUMBER; i++) {
            if (register.childs_keys[i] != '-') {
                expandedNode.add(new Data(register.childs_keys[i], register.childs_weight[i]));
            }
        }
        return expandedNode;
    }

    //Sacar un valor de un nodo seleccionado de una lista de nodos
    private Map.Entry pop(HashMap<Character, Data> map) {
        Map.Entry<Character, Data> entry = map.entrySet().iterator().next();
        if (map.containsKey(entry.getKey())) {
            map.remove(entry.getKey());
            return entry;
        } else {
            return null;
        }

    }

    //Sacaer un valor de una lista y moverlo a otra (Usado para trabajar con ABIERTA y CERRADA)
    private Map.Entry moveFirstTo(HashMap<Character, Data> from, HashMap<Character, Data> to) {
        Map.Entry<Character, Data> entry = pop(from);
        if (entry != null) {
            to.put(entry.getKey(), entry.getValue());
            return entry;
        } else {
            return null;
        }

    }
    //Metodo utilizado para ordenar ABIERTA
    private LinkedHashMap sortHashAscendingly(LinkedHashMap<Character, Data> map) {
        // Ordenar el HaspMap por valor
        Comparator<Map.Entry<Character, Data>> valueComparator = (Map.Entry<Character, Data> e1, Map.Entry<Character, Data> e2) -> {
            Integer v1 = e1.getValue().f;
            Integer v2 = e2.getValue().f;
            return v1.compareTo(v2);
        };

        List<Map.Entry<Character, Data>> listOfEntries = new ArrayList<>(map.entrySet());

        Collections.sort(listOfEntries, valueComparator);

        LinkedHashMap<Character, Data> sortedByValue = new LinkedHashMap<>(listOfEntries.size());

        listOfEntries.forEach((entry) -> {
            sortedByValue.put(entry.getKey(), entry.getValue());
        });

        return sortedByValue;
    }
}
