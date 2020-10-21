package sat;

import immutable.*;
import sat.env.*;
import sat.formula.*;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     * 
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    public static Environment solve(Formula formula) {
        return solve(formula.getClauses(), new Environment());
    }

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     * 
     * @param clauses
     *            formula in conjunctive normal form
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
        // System.out.println(clauses);
        // System.out.println(env);
        // System.out.println("-----------------------------------");
        Clause chosenClause = new Clause();
        int smallestClauseSize = Integer.MAX_VALUE;
        // no clauses, backtrack
        if (clauses.isEmpty()) return env;

        for (Clause thisClause : clauses){
            // no literal in this clause
            if (thisClause.isEmpty()) return null;

            if (thisClause.size() < smallestClauseSize){
                // keep track of smallest size 
                smallestClauseSize = thisClause.size();

                if (thisClause.isUnit()){
                    // only one literal in this clause 
                    chosenClause = thisClause;
                    // System.out.println("chosen clause" + chosenClause);
                    Environment e;
                    ImList<Clause> subClausesList;
                    // get the literal
                    Literal chosenLiteral = chosenClause.chooseLiteral();
                    
                    if (chosenLiteral instanceof PosLiteral){
                        e = env.putTrue(chosenLiteral.getVariable());
                        subClausesList = substitute(clauses, chosenLiteral);
                    }else{
                        e = env.putFalse(chosenLiteral.getVariable());
                        subClausesList = substitute(clauses, chosenLiteral);
                    }
                    // System.out.println(">>> 1");
                    return solve(subClausesList, e);
                }
            }
        }

        for (Clause thisClause : clauses){
            if (thisClause.size() == smallestClauseSize){
                chosenClause = thisClause;
                // System.out.println("chosen clause2" + chosenClause);
            }
        }

        // pick the first literal
        Literal chosenLiteral = chosenClause.chooseLiteral();
        if (chosenLiteral instanceof NegLiteral) chosenLiteral = chosenLiteral.getNegation();

        // System.out.println(">>> 2");
        Environment e = solve(
            substitute(clauses, chosenLiteral), 
            env.put(chosenLiteral.getVariable(), Bool.TRUE));

        if (e == null){
            // System.out.println(">>> 3");
            return solve(
                substitute(clauses, chosenLiteral.getNegation()), 
                env.put(chosenLiteral.getVariable(), Bool.FALSE));
        } else return e;
    }

    /**
     * given a thisClause list and literal, produce a new list resulting from
     * setting that literal to true
     *
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses, Literal l) {
        Clause tmp;
        for (Clause thisClause : clauses) {
            if (thisClause.contains(l) || thisClause.contains(l.getNegation())) {
                tmp = thisClause.reduce(l);
                clauses = clauses.remove(thisClause);
                if (tmp != null) clauses = clauses.add(tmp);
            }
        }
        return clauses;
    }
}