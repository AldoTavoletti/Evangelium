package it.unicam.cs.mpgc.rpg129852.model;

public enum ProblemType {
    ANGER ("Rabbia"),
    SADNESS ("Tristezza");

        private final String problem;

        ProblemType(String problem) {
            this.problem = problem;
        }

        public String getProblem() {
            return problem;
        }
}
