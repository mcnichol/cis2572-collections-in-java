package org.mcnichol.tableview;

import org.mcnichol.algorithm.PrimeComputer;

import java.util.Objects;

/**
 * Model Entity for displaying Table Rows in a {@link javafx.scene.control.TableView}
 */
public class PrimeComputerTableRow {
    private String implementation;
    private String primeComputerColumnIndex0;
    private String primeComputerColumnIndex1;
    private String primeComputerColumnIndex2;
    private String primeComputerColumnIndex3;
    private String primeComputerColumnIndex4;
    private String primeComputerColumnIndex5;
    private String runCount;

    public PrimeComputerTableRow(PrimeComputer thisComputer, Long[] executionTimeOfPrimeComputations, int runCount) {
        this.implementation = thisComputer.getClass().getSimpleName();
        this.primeComputerColumnIndex0 = convertFromNanoToSecond(executionTimeOfPrimeComputations[0]);
        this.primeComputerColumnIndex1 = convertFromNanoToSecond(executionTimeOfPrimeComputations[1]);
        this.primeComputerColumnIndex2 = convertFromNanoToSecond(executionTimeOfPrimeComputations[2]);
        this.primeComputerColumnIndex3 = convertFromNanoToSecond(executionTimeOfPrimeComputations[3]);
        this.primeComputerColumnIndex4 = convertFromNanoToSecond(executionTimeOfPrimeComputations[4]);
        this.primeComputerColumnIndex5 = convertFromNanoToSecond(executionTimeOfPrimeComputations[5]);
        this.runCount = String.valueOf(runCount);
    }

    private String convertFromNanoToSecond(Long executionTimeOfPrimeComputations) {
        return String.format("%.4f", executionTimeOfPrimeComputations / 1_000_000_000D);
    }

    public String getImplementation() {
        return implementation;
    }

    public void setImplementation(String implementation) {
        this.implementation = implementation;
    }

    public String getPrimeComputerColumnIndex0() {
        return primeComputerColumnIndex0;
    }

    public void setPrimeComputerColumnIndex0(String primeComputerColumnIndex0) {
        this.primeComputerColumnIndex0 = primeComputerColumnIndex0;
    }

    public String getPrimeComputerColumnIndex1() {
        return primeComputerColumnIndex1;
    }

    public void setPrimeComputerColumnIndex1(String primeComputerColumnIndex1) {
        this.primeComputerColumnIndex1 = primeComputerColumnIndex1;
    }

    public String getPrimeComputerColumnIndex2() {
        return primeComputerColumnIndex2;
    }

    public void setPrimeComputerColumnIndex2(String primeComputerColumnIndex2) {
        this.primeComputerColumnIndex2 = primeComputerColumnIndex2;
    }

    public String getPrimeComputerColumnIndex3() {
        return primeComputerColumnIndex3;
    }

    public void setPrimeComputerColumnIndex3(String primeComputerColumnIndex3) {
        this.primeComputerColumnIndex3 = primeComputerColumnIndex3;
    }

    public String getPrimeComputerColumnIndex4() {
        return primeComputerColumnIndex4;
    }

    public void setPrimeComputerColumnIndex4(String primeComputerColumnIndex4) {
        this.primeComputerColumnIndex4 = primeComputerColumnIndex4;
    }

    public String getPrimeComputerColumnIndex5() {
        return primeComputerColumnIndex5;
    }

    public void setPrimeComputerColumnIndex5(String primeComputerColumnIndex5) {
        this.primeComputerColumnIndex5 = primeComputerColumnIndex5;
    }

    @Override
    public String toString() {
        return "PrimeComputerTableRow{" +
                "implementation='" + implementation + '\'' +
                ", c1=" + primeComputerColumnIndex0 +
                ", c2=" + primeComputerColumnIndex1 +
                ", c3=" + primeComputerColumnIndex2 +
                ", c4=" + primeComputerColumnIndex3 +
                ", c5=" + primeComputerColumnIndex4 +
                ", c6=" + primeComputerColumnIndex5 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PrimeComputerTableRow that = (PrimeComputerTableRow) o;
        return Objects.equals(implementation, that.implementation) && Objects.equals(primeComputerColumnIndex0, that.primeComputerColumnIndex0) && Objects.equals(primeComputerColumnIndex1, that.primeComputerColumnIndex1) && Objects.equals(primeComputerColumnIndex2, that.primeComputerColumnIndex2) && Objects.equals(primeComputerColumnIndex3, that.primeComputerColumnIndex3) && Objects.equals(primeComputerColumnIndex4, that.primeComputerColumnIndex4) && Objects.equals(primeComputerColumnIndex5, that.primeComputerColumnIndex5);
    }

    @Override
    public int hashCode() {
        return Objects.hash(implementation, primeComputerColumnIndex0, primeComputerColumnIndex1, primeComputerColumnIndex2, primeComputerColumnIndex3, primeComputerColumnIndex4, primeComputerColumnIndex5);
    }

    public String getRunCount() {
        return runCount;
    }

    public void setRunCount(String runCount) {
        this.runCount = runCount;
    }
}
