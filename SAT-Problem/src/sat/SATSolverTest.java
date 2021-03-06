package sat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import sat.env.*;
import sat.formula.*;


public class SATSolverTest {
    Literal a = PosLiteral.make("a");
    Literal b = PosLiteral.make("b");
    Literal c = PosLiteral.make("c");
    Literal na = a.getNegation();
    Literal nb = b.getNegation();
    Literal nc = c.getNegation();

    public static void main(String[] args) throws Exception {
        String cnfFilePath;
        if (args.length != 0) {
            cnfFilePath = args[0];
        } else {
            cnfFilePath = "/home/mark/CODE/fuckSATSolver/2-SAT-Problem/src/sample.cnf";
        }
        BufferedReader buffer;
        Formula ClauseFormula = new Formula();
        Clause IndividualClause = new Clause();
        
        try {
            FileReader file = new FileReader(cnfFilePath);
            buffer = new BufferedReader(file);
            String line;

            while (true) {
                line = buffer.readLine();
                if (line == null) break;
                if (line.length() == 0) continue;

                String[] tokens = line.split(" ");
                if (tokens[0].equals("c")) continue;
                if (tokens[0].equals("p")){
                    continue;
                }

                for (String token: tokens){
                    if (token.length() == 0) continue;
                    if (token.equals("0")){
                        // add clause into formula 
                        ClauseFormula = ClauseFormula.addClause(IndividualClause);
                        // start a new clause 
                        IndividualClause = new Clause();
                    }else{
                        if (token.charAt(0) == '-'){
                            IndividualClause = IndividualClause.add(NegLiteral.make(token.substring(1)));
                        }else{
                            IndividualClause = IndividualClause.add(PosLiteral.make(token));
                        }
                    }
                }
            }
            buffer.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        
        // timer 
        System.out.println("SAT solver starts!!!");
        long started = System.nanoTime();
        // solve
        Environment env = SATSolver.solve(ClauseFormula);
        // timer
        long timeTaken = System.nanoTime() - started;
        System.out.println("Time taken: " + timeTaken/1000000.0 + "ms");

        if (env == null){
            System.out.println("Not satisfiable");
        } 
        else {
            System.out.println("Satisfiable");
            try {
                System.out.println(env);
                FileWriter myWriter = new FileWriter("BoolAssignment.txt");
                myWriter.write((env.toString()));
                myWriter.close();
                System.out.println("Successfully exported solution: BoolAssignment.txt");
              } catch (IOException e) {
                System.out.println("An error occurred while writing solution to file.");
                System.out.println(env);
              }
        }
    }
    
	
    public void testSATSolver1(){
    	// (a v b)
    	Environment e = SATSolver.solve(makeFm(makeCl(a,b))	);
/*
    	assertTrue( "one of the literals should be set to true",
    			Bool.TRUE == e.get(a.getVariable())  
    			|| Bool.TRUE == e.get(b.getVariable())	);
    	
*/    	
    }
    
    
    public void testSATSolver2(){
    	// (~a)
    	Environment e = SATSolver.solve(makeFm(makeCl(na)));
/*
    	assertEquals( Bool.FALSE, e.get(na.getVariable()));
*/    	
    }
    
    private static Formula makeFm(Clause... e) {
        Formula f = new Formula();
        for (Clause c : e) {
            f = f.addClause(c);
        }
        return f;
    }
    
    private static Clause makeCl(Literal... e) {
        Clause c = new Clause();
        for (Literal l : e) {
            c = c.add(l);
        }
        return c;
    }
    
    
    
}