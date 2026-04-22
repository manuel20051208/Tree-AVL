import javax.swing.JOptionPane;
import java.util.Optional;
import java.util.function.IntPredicate;

public class Principal {

    private static ArbolBinario arbol;
    private static String treeName;

    public static void main(String[] args) {
        new Principal().menuOpciones();
    }

    private void probar() {
        arbol.insertar(10);
        arbol.insertar(4);
        arbol.insertar(7);
        arbol.insertar(8);
        arbol.insertar(16);
        arbol.insertar(19);
        arbol.insertar(11);
    }

    private void iniciar() {
        int op;
        do {
            op = pedirOpcion(treeName + """
                    Tree search AVL
                    ─────────────────────────
                    1. Input element
                    2. Recorrer tree
                    3. Search for an element
                    4. Delete an element
                    5. Operation
                    6. Leave
                    """);

            switch (op) {
                case 1 -> insertarElemento();
                case 2 -> menuRecorridos();
                case 3 -> buscarElemento();
                case 4 -> eliminarElemento();
                case 5 -> mostrarOperaciones();
                case 6 -> mostrar("Saliendo...");
                default -> mostrar("Opción inválida.");
            }
        } while (op != 6);
    }

    private void insertarElemento() {
        int num = pedirNumero("Ingresa un valor:");
        var mensaje = arbol.insertar(num)           // ← antes: ingresar_nodo
                ? "Dato agregado: " + num
                : "El dato " + num + " ya existe.";
        mostrar(mensaje);
    }

    private void menuRecorridos() {
        var tipos = new ArbolAVL.Recorrido[]{
                null,
                ArbolAVL.Recorrido.PREORDEN,
                ArbolAVL.Recorrido.INORDEN,
                ArbolAVL.Recorrido.POSORDEN
        };

        int op;
        do {
            op = pedirOpcion(treeName + """
                    Tipo de recorrido
                    ─────────────────────
                    1. Preorden
                    2. Inorden
                    3. Posorden
                    4. Volver
                    """);

            if (op >= 1 && op <= 3) arbol.recorrer(tipos[op]);
        } while (op != 4);
    }

    private void buscarElemento() {
        int num = pedirNumero("Ingresa un valor a buscar:");
        var mensaje = arbol.buscar(num)
                .map(n -> "El dato " + num + " SÍ existe.")
                .orElse("El dato " + num + " NO existe.");
        mostrar(mensaje);
    }

    private void eliminarElemento() {
        int num = pedirNumero("Ingresa un valor a eliminar:");
        var mensaje = arbol.eliminar(num)
                .map(n -> "El dato " + num + " Ha sido eliminado")
                .orElse("El dato " + num + " NO existe.");
        mostrar(mensaje);
    }

    private void mostrarOperaciones() {
        var positivos = contarPorCondicion(arbol.getRaiz(), n -> n > 0);
        var negativos = contarPorCondicion(arbol.getRaiz(), n -> n < 0);
        var pares = contarPorCondicion(arbol.getRaiz(), n -> n % 2 == 0);

        mostrar(treeName + """
                Operaciones sobre el árbol
                ──────────────────────────────
                Positivos : %d
                Negativos : %d
                Pares     : %d
                """.formatted(positivos, negativos, pares));
    }

    private int contarPorCondicion(Nodo nodo, IntPredicate condicion) {
        if (nodo == null) return 0;
        int contador = condicion.test(nodo.getValor()) ? 1 : 0;
        return contador
                + contarPorCondicion(nodo.izquierda, condicion)
                + contarPorCondicion(nodo.derecha, condicion);
    }

    private void menuOpciones() {
        int mensaje = 0;

        do {
            mensaje = pedirOpcion("""
                            Opciones sobre
                            1. Binary Tree
                            2. AVL tree
                            3. Leave
                    """);

            switch (mensaje) {
                case 1 -> {
                    arbol = new ArbolBinario();

                    String chosen = "Binary Tree\n";
                    treeName = chosen;

                    probar();
                    iniciar();
                }
                case 2 -> {
                    arbol = new ArbolAVL();

                    String chosen = "AVL Tree\n";
                    treeName = chosen;

                    probar();
                    iniciar();
                }
                case 3 -> {
                    String leave = "Ha cancelado hasta la proxima";
                    probar();
                    mostrar(leave);
                }
                default -> {
                    String option = "Opcion invalida, vuele a intentarlo";
                    mostrar(option);
                }
            }
        } while (mensaje != 3);
    }

    private int pedirOpcion(String mensaje) {
        return pedirNumero(mensaje);
    }

    private int pedirNumero(String mensaje) {
        return Integer.parseInt(JOptionPane.showInputDialog(null, mensaje));
    }

    private void mostrar(String mensaje) {
        JOptionPane.showMessageDialog(null, mensaje);
    }
}