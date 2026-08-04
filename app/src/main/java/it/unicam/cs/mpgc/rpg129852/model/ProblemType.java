package it.unicam.cs.mpgc.rpg129852.model;

public enum ProblemType {
    ANGER ("Rabbia"),
    SADNESS ("Tristezza"),
    ANXIETY ("Ansia"),
    LOST ("Smarrimento"),
    ENVY ("Invidia");

        private final String problem;

        ProblemType(String problem) {
            this.problem = problem;
        }

        public String getDisplayValue() {
            return problem;
        }
}
