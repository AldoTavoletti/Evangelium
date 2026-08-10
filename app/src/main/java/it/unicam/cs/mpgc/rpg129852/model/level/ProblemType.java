package it.unicam.cs.mpgc.rpg129852.model.level;

public enum ProblemType {
    DOUBT ("Dubbio"),
    FEAR ("Paura"),
    GUILT ("Senso di colpa"),
    PRIDE ("Orgoglio"),
    BITTERNESS ("Amarezza"),
    DESPAIR ("Disperazione");

        private final String problem;

        ProblemType(String problem) {
            this.problem = problem;
        }

        public String getDisplayValue() {
            return problem;
        }
}
