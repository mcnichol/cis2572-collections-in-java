package org.mcnichol;

import java.util.ArrayList;
import java.util.Comparator;

public class MyGenericStack<E extends Comparable<E>> extends ArrayList<E> implements MinMax<E> {

    private E max;
    private E min;

    public E peek() {
        if (isEmpty()) return null;

        return get(size() - 1);
    }

    public E pop() {
        if (isEmpty()) return null;

        E e = this.get(size() - 1);
        remove(size() - 1);
        computeMinMax();

        return e;
    }

    public void push(E value) {
        add(value);

        computeMinMax();
    }

    private void computeMinMax() {
        if (isEmpty()) {
            resetMinMax();
        } else {
            this.stream().max(Comparator.naturalOrder()).ifPresent(this::setMax);
            this.stream().min(Comparator.naturalOrder()).ifPresent(this::setMin);
        }
    }

    private void resetMinMax() {
        max = null;
        min = null;
    }

    private void setMin(E newMin) {
        this.min = newMin;
    }

    @Override
    public E min() {
        return min;
    }

    private void setMax(E newMax) {
        this.max = newMax;
    }

    @Override
    public E max() {
        return max;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = size() - 1; i >= 0; i--) {
            sb.append(this.get(i)).append("\n");
        }

        return sb.toString();
    }

}
