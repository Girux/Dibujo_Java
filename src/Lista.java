import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;

public class Lista {
    // Propiedad privada que almacena la lista enlazada
    private LinkedList<Figura> lista;

    // Constructor de la clase que inicializa la lista enlazada
    public Lista() {
        lista = new LinkedList<>();
    }

    // Método para obtener la lista completa
    public LinkedList<Figura> obtenerLista() {
        return lista;
    }

    // Método para agregar un elemento a la lista
    public void agregar(Figura elemento) {
        lista.add(elemento); // Agrega el elemento al final de la lista
    }

    // Método para eliminar un elemento de la lista
    public void eliminar(Figura elemento) {
        lista.remove(elemento); // Elimina la primera ocurrencia del elemento de la lista
    }

    public boolean guardar(String nombreArchivo) {
        String[] datos = new String[this.lista.size()];
        
        for(int i = 0; i < this.lista.size(); i++){
            Figura figura = this.lista.get(i);
            datos[i] = figura.tipo + ";" + figura.x1 + ";" + figura.y1 + ";" + figura.x2 + ";" + figura.y2;

        }

        return Archivo.guardarArchivo(nombreArchivo, datos);
    }

      public void desdeArchivo(String nombreArchivo) {
        BufferedReader br = Archivo.abrirArchivo(nombreArchivo);
        if (br != null) {
            try {
                String linea = br.readLine();
                while (linea != null) {
                    String[] datos = linea.split(";");
                    if (datos.length >= 5) {
                        Figura n = new Figura(datos[0], Integer.parseInt(datos[1]) , Integer.parseInt(datos[2]), Integer.parseInt(datos[3]), Integer.parseInt(datos[4]));
                        agregar(n);
                    }
                    linea = br.readLine();
                }

            } catch (IOException ex) {

            } catch (Exception ex) {

            }
        }
    }

    public void eliminarTrazos(int x, int y) {
        Figura figuraAEliminar = null;
        
        for (Figura figura : lista) {
            if (contienePunto(figura, x, y)) {
                figuraAEliminar = figura;
                break;
            }
        }

        if (figuraAEliminar != null) {
            lista.remove(figuraAEliminar);
        }
    }

    private boolean contienePunto(Figura figura, int x, int y) {
        switch (figura.tipo) {
            case "Linea":
                return (x >= Math.min(figura.x1, figura.x2) && x <= Math.max(figura.x1, figura.x2) &&
                        y >= Math.min(figura.y1, figura.y2) && y <= Math.max(figura.y1, figura.y2));
                
            case "Rectangulo":
                return (x >= figura.x1 && x <= figura.x1 + figura.x2 &&
                        y >= figura.y1 && y <= figura.y1 + figura.y2);
                
            case "Ovalo":
                int centroX = figura.x1 + figura.x2 / 2;
                int centroY = figura.y1 + figura.y2 / 2;
                int radioX = figura.x2 / 2;
                int radioY = figura.y2 / 2;
                return ((Math.pow((x - centroX) / (double) radioX, 2) +
                         Math.pow((y - centroY) / (double) radioY, 2)) <= 1);
        }
        return false;
    }

    public void eliminarUltimo() {
        if (!lista.isEmpty()) {
            lista.removeLast(); // Elimina la última figura dibujada
        }
    }
}
