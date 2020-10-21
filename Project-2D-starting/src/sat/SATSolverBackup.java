package sat;

import immutable.*;
import sat.env.Environment;
import sat.env.Bool;
import sat.env.Variable;
import sat.formula.Clause;
import sat.formula.Formula;
import sat.formula.*;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolverBackup {

    private static Variable x;
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
        Environment env = new Environment();
        return solve(formula.getClauses(), env);
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
        // if (clauses.size() == 0) return env;
        if (clauses.size() == 1 && clauses.first() == null) {
            return env;
        } else {

            // to find the smallest clause
            Clause chosenClause = clauses.first();

            for(Clause thisClause: clauses) {


                if (thisClause.isEmpty()) {
                    // no literal in this clause 
                    // System.out.println(">>>>>>>>>>>>>>> 1");
                    return null;

                } else if (thisClause.isUnit()){
                    // only one literal in this clause 
                    // System.out.println(">>>>>>>>>>>>>>> 2");
                    if (thisClause.chooseLiteral() instanceof PosLiteral) {
                        env = env.putTrue(thisClause.chooseLiteral().getVariable());
                    } else {
                        env = env.putFalse(thisClause.chooseLiteral().getVariable());
                    }

                    clauses = substitute(clauses, thisClause.chooseLiteral());

                    try {
                        System.out.println(clauses.size());
                        clauses = clauses.remove(thisClause);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        System.out.println("hi");
                    }


                } else {
                    // more than 1 literals in this clause 
                    // System.out.println(">>>>>>>>>>>>>>> 3");
                    // to find the smallest clause
                    if (thisClause.size() < chosenClause.size()){
                        chosenClause = thisClause;
                    }
                }
            }


            //the idea is to choose the first literal from the smallest clause and evaluate to true
            x = chosenClause.chooseLiteral().getVariable();

            if (chosenClause.chooseLiteral() instanceof PosLiteral) {
                env = env.putTrue(chosenClause.chooseLiteral().getVariable());
            } else {
                env = env.putFalse(chosenClause.chooseLiteral().getVariable());
            }

            clauses = substitute(clauses,chosenClause.chooseLiteral());
            System.out.println(clauses);

            Environment temp = solve(clauses, env);

            if (temp == null) {
                if (env.get(x) == Bool.FALSE) {
                    env.putTrue(x);
                } else {
                    env.putFalse(x);
                }

                return solve(clauses,env);

            } else {
                return temp;
            }
        }
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
        Clause c = clauses.first().reduce(l);
        ImList<Clause> clausesNew = null;
        if (c != null) {
            clausesNew = new NonEmptyImList<Clause>(c);
        }

        for (Clause thisClause: clauses) {
            if (! (thisClause.equals(clauses.first()))) {

                c = thisClause.reduce(l);

                if (c != null && clausesNew==null) {
                    clausesNew = new NonEmptyImList<Clause>(c);
                }

                if (c != null) {
                    clausesNew = clausesNew.add(c);

                }
            }
        }
        return clausesNew;
    }

}
