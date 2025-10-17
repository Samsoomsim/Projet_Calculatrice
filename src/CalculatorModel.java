import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/** Événements envoyés à la vue quand l'état du modèle change. */
interface CalculatorListener {
    void onChange(double accumulator, Deque<Double> stackSnapshot);
}

/**
 * Modèle d'une calculatrice RPN (accumulateur + pile).
 * Invariants :
 *  - 'stack' contient le bas...haut (haut = top) via Deque#push/pop
 *  - 'accumulator' est la valeur en cours d'édition/affichage
 */
public class CalculatorModel {

    private double accumulator = 0.0;
    private final Deque<Double> stack = new ArrayDeque<>();
    private final List<CalculatorListener> listeners = new ArrayList<>();

    /* --------- Observateurs --------- */
    public void addListener(CalculatorListener l) { if (l != null) listeners.add(l); }
    private void notifyChange() {
        // on envoie une copie défensive de la pile pour éviter les modifications externes
        Deque<Double> snapshot = new ArrayDeque<>(stack);
        for (CalculatorListener l : listeners) l.onChange(accumulator, snapshot);
    }

    /* --------- Accumulateur --------- */
    public double getAccumulator() { return accumulator; }

    /** Fixe l'accumulateur à une valeur (ex: saisie utilisateur). */
    public void setAccumulator(double value) {
        this.accumulator = value;
        notifyChange();
    }

    /** Remet l'accumulateur à zéro. */
    public void clearAccumulator() {
        this.accumulator = 0.0;
        notifyChange();
    }

    /* --------- Pile --------- */
    /** Empile l'accumulateur (top = dernier poussé). */
    public void push() {
        stack.push(accumulator);
        notifyChange();
    }

    /** Dépile le sommet dans l'accumulateur (si pile vide: ne fait rien). */
    public void pop() {
        if (!stack.isEmpty()) {
            accumulator = stack.pop();
            notifyChange();
        }
    }

    /** Supprime le sommet de pile sans toucher à l'accumulateur. */
    public void drop() {
        if (!stack.isEmpty()) {
            stack.pop();
            notifyChange();
        }
    }

    /** Vide entièrement la pile. */
    public void dropAll() {
        if (!stack.isEmpty()) {
            stack.clear();
            notifyChange();
        }
    }

    /** Échange accumulateur <-> sommet de pile (si pile vide: ne fait rien). */
    public void swap() {
        if (!stack.isEmpty()) {
            double a = accumulator;
            double b = stack.pop();
            accumulator = b;
            stack.push(a);
            notifyChange();
        }
    }

    /* --------- Opérations binaires RPN --------- */
    /** Récupère l'opérande de gauche depuis la pile, sinon jette une exception. */
    private double requireLeftOperand() {
        if (!stack.isEmpty()) return stack.pop();
        throw new IllegalStateException("Pile insuffisante: fais 'push' pour empiler le premier opérande.");
    }

    /** Addition: (left from stack) + accumulator -> accumulator */
    public void add() {
        double left = requireLeftOperand();
        accumulator = left + accumulator;
        notifyChange();
    }

    /** Soustraction: (left from stack) - accumulator -> accumulator */
    public void sub() {
        double left = requireLeftOperand();
        accumulator = left - accumulator;
        notifyChange();
    }

    /** Multiplication: (left from stack) * accumulator -> accumulator */
    public void mul() {
        double left = requireLeftOperand();
        accumulator = left * accumulator;
        notifyChange();
    }

    /** Division: (left from stack) / accumulator -> accumulator */
    public void div() {
        double left = requireLeftOperand();
        if (accumulator == 0.0) throw new ArithmeticException("Division par zéro");
        accumulator = left / accumulator;
        notifyChange();
    }

    /* --------- Utilitaires (facultatifs) --------- */
    /** Copie de la pile (haut en premier) pour affichage/tests. */
    public Deque<Double> getStackSnapshot() { return new ArrayDeque<>(stack); }

    /** Nombre d'éléments dans la pile (pour tests). */
    public int stackSize() { return stack.size(); }
}
