
import com.mag.parsing.CalculationParser;
import com.mag.parsing.Expression;
import com.mag.parsing.ParseException;
import com.mag.parsing.UnresolvedNameException;
import java.util.Scanner;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author michael
 */
public class ParserTester {
    public static void main(String[] args) throws ParseException, UnresolvedNameException {
        System.out.print("Give me an expression -> ");
        String input = new Scanner(System.in).nextLine();
        Expression e = CalculationParser.interpretExpressionFromString(input);
        System.out.println("Successfully parsed expression: " + e);
        System.out.println("Evaluated as: " + e.solve(null));
    }
}
