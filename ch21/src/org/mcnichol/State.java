package org.mcnichol;

import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * <h1>State</h1>
 * <p>
 * State Object is used for population of ComboBoxes and driving Lexicon Controller display behavior.  Extends StringConverter<State>
 * in order to cleanly display and populate dropdown boxes.
 * <p>
 * Leverages Builder pattern for FluentAPI dev based on Fowlers / Gang Of Four stylings.
 */
public class State extends StringConverter<State> {
    public State(){};

    private State(Builder builder) {
        this.abbreviation = builder.abbreviation;
        this.area = builder.area;
        this.capital = builder.capital;
        this.motto = builder.motto;
        this.name = builder.name;
        this.population = builder.population;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }

    private String abbreviation;
    private BigDecimal area;
    private String capital;
    private String motto;
    private String name;
    private Long population;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return Objects.equals(abbreviation, state.abbreviation) && Objects.equals(area, state.area) && Objects.equals(capital, state.capital) && Objects.equals(motto, state.motto) && Objects.equals(name, state.name) && Objects.equals(population, state.population);
    }

    @Override
    public int hashCode() {
        return Objects.hash(abbreviation, area, capital, motto, name, population);
    }

    @Override
    public String toString() {
        return "State Information: \n" +
                "\tAbbrev:\t\t" + abbreviation + "\n" +
                "\tArea:\t\t" + area + "\n" +
                "\tCapital:\t\t" + capital + "\n" +
                "\tMotto:\t\t" + motto + "\n" +
                "\tName:\t\t" + name + "\n" +
                "\tPop.:\t\t" + population;
    }

    @Override
    public String toString(State object) {
        return object.name;
    }

    @Override
    public State fromString(String string) {
        Builder builder = new Builder();
        String[] split = string.split("\t");

        builder
                .name(split[0])
                .area(BigDecimal.valueOf(Long.parseLong(split[3])))
                .population(Long.parseLong(split[4]))
                .abbreviation(split[1])
                .capital(split[2])
                .motto(split[5]);
        return builder.build();
    }

    public static class Builder {
        private String abbreviation;
        private BigDecimal area;
        private String capital;
        private String motto;
        private String name;
        private Long population;

        public Builder abbreviation(String abbreviation) {
            this.abbreviation = abbreviation;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder area(BigDecimal area) {
            this.area = area;
            return this;
        }

        public Builder capital(String capital) {
            this.capital = capital;
            return this;
        }

        public Builder motto(String motto) {
            this.motto = motto;
            return this;
        }

        public Builder population(Long population) {
            this.population = population;
            return this;
        }

        public State build() {
            return new State(this);
        }
    }
}
