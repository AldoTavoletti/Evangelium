package it.unicam.cs.mpgc.rpg129852.model.level;

public enum ProblemType {
    ANGER ("Rabbia"),
    SADNESS ("Tristezza"),
    ANXIETY ("Ansia"),
    LOST ("Smarrimento"),
    ENVY ("Invidia"),
    DESPAIR ("Disperazione"),
    RANCOR ("Rancore");

        private final String problem;

        ProblemType(String problem) {
            this.problem = problem;
        }

        public String getDisplayValue() {
            return problem;
        }
}
