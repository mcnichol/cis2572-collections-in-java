package org.mcnichol;

public enum STATISTICS {
    MAX(0), MIN(1), MEAN(2), SUM(3);

    public final int index;

    STATISTICS(int index){
        this.index = index;
    }
}