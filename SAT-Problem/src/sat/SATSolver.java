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
                    return solve(subClausesList, e);
                }
            }
        }

        // make sure always pick one of the smallest clause
        for (Clause thisClause : clauses){
            if (thisClause.size() == smallestClauseSize){
                chosenClause = thisClause;
                // break after found one of the shortest clauses
                break;
            }
        }

        // pick the first literal
        Literal chosenLiteral = chosenClause.chooseLiteral();
        if (chosenLiteral instanceof NegLiteral) chosenLiteral = chosenLiteral.getNegation();
        // try put positive literal to True
        Environment e = solve(
            substitute(clauses, chosenLiteral), 
            env.putTrue(chosenLiteral.getVariable()));

        if (e == null){
            // try put negative literal to False
            return solve(
                substitute(clauses, chosenLiteral.getNegation()), 
                env.putFalse(chosenLiteral.getVariable()));
        } else return e;
    }

    /**
     * given a clause list and literal, produce a new list resulting from
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