import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class EquationEvaluator {

    /**
     * Evaluates a valid infix expression into a double.
     *
     * @param infixString
     * @return
     */
    public static double evaluateInfixExpression(String infixString) throws NumberFormatException {
        ExpressionParser expressionParser = new ExpressionParser();
        Queue infixQueue = expressionParser.infixStringToQueue(infixString);
        if (!verifyInfixQueue(infixQueue))
            throw new NumberFormatException("Invalid Expression");
        System.out.println("Infix: " + infixQueue.toString());
        Queue postfixQueue = infixToPostfix(infixQueue);
        return evaluatePostfixQueue(postfixQueue);
    }

    /**
     * Verifies that an infix queue has an equal number of braces, and only valid terms (braces, operators, and doubles)
     *
     * @param infixQueue
     * @return
     */
    private static boolean verifyInfixQueue(Queue<String> infixQueue) {
        int openingBraces = 0;
        int closingBraces = 0;

        for (String term : infixQueue) {
            if (term.equals("("))
                openingBraces++;
            else if (term.equals(")"))
                closingBraces++;
            else if (StringValidator.isOperator(term))
                continue;
            else {
                try {
                    Double.parseDouble(term);
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }

        return (openingBraces == closingBraces);
    }



    /**
     * Converts an infix expression into a postfix expression.
     *
     * @param infixQueue The infix expression
     * @return A queue representing the completed postfix expression
     */
    private static Queue infixToPostfix(Queue infixQueue) {
        Stack stack = new Stack();
        Queue postfixQueue = new LinkedList();

        int numOfTerms = infixQueue.size();
        // Loop through each character in the inset equation (String)
        for (int i = 0; i < numOfTerms; i++) {
            String currentTerm = infixQueue.remove().toString();
            // If the term is a number add it to the postfixQueue
            if (isDigit(currentTerm)) {
                postfixQueue.add(currentTerm);
            }
            // If the term is an operator
            else if (StringValidator.isOperator(currentTerm)) {
                // While there's an operator on the top of the stack with greater precedence
                while (!stack.isEmpty() &&
                        getOperatorPrecedence(currentTerm) <= getOperatorPrecedence((String) stack.peek())) {
                    // Pop and add it to the postfixQueue
                    postfixQueue.add(stack.pop());
                }
                // Then push the current operator onto the stack
                stack.push(currentTerm);
            }
            // If the term is an left parenthesis, push it onto the stack
            else if (currentTerm.equals("("))
                stack.push(currentTerm);
                // If the term is a right parenthesis
            else if (currentTerm.equals(")")) {
                // While there's not a left parenthesis at the top of the stack
                while (!stack.peek().equals("(")) {
                    // Add the top of the stack to the queue
                    postfixQueue.add(stack.pop());
                }
                // Remove the left parenthesis
                stack.pop();
            }
        }

        // While there are operators on the stack, add them to the queue
        while (!stack.isEmpty()) {
            postfixQueue.add(stack.pop());
        }

        // Return the completed postfix string.
        return postfixQueue;
    }

    private static boolean isDigit(String currentTerm) {
        return (!StringValidator.isOperator(currentTerm) &&
                !currentTerm.equals("(") &&
                !currentTerm.equals(")"));
    }


    /**
     * Returns the basic precedence specified by the order of operations, with the addition of modulo (%), which
     * has precedence right below division(/)
     *
     * @param op The operator that precedence is calculated from.
     * @return An integer representing the precedence of the operation character.
     */
    private static int getOperatorPrecedence(String op) {
        char[] operations = {'-', '+', '%', '/', '*', '^'};
        if (op.length() != 1)
            return -1;

        for (int i = 0; i < operations.length; i++) {
            if (operations[i] == op.charAt(0)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * Calculates the answer to any valid expression in postfix notation.
     *
     * @param postfixQueue The queue containing the characters of the postfix expression.
     * @return
     */
    private static double evaluatePostfixQueue(Queue postfixQueue) {
        Stack<String> stack = new Stack<>();

        int numOfTerms = postfixQueue.size();

        for (int i = 0; i < numOfTerms; i++) {
            String currentTerm = (String) postfixQueue.remove();

            // If the current term is not an operator
            if (!StringValidator.isOperator(currentTerm)) {
                // Then push the term onto the stack
                stack.push(currentTerm);
            }
            // If the current term is an operator
            else {
                // Then pop off two values from the stack
                String secondTerm = stack.pop();
                String firstTerm = stack.pop();
                // Perform the current operation on the values
                double answer = performOperation(currentTerm, firstTerm, secondTerm);
                // Push the answer onto the stack
                stack.push(Double.toString(answer));
            }
        }
        int stackSize = stack.size();
        double product = 1;
        for (int i = 0; i < stackSize; i++) {
            product *= Double.parseDouble(stack.pop());
        }
        return product;
    }

    /**
     * Performs a basic operation on two operands, based on the input operation. The first operation always goes
     * before the operation when written in infix notation.
     * <p>
     * Example: if the operation is +, the first operand is 1, and the second operand is 2, then the answer would
     * be calculated by the expression 1 + 2.
     *
     * @param operation    The operation to perform on the operands.
     * @param firstString  The operand that will go before the operation.
     * @param secondString The operand that will go after the operation.
     * @return
     */
    private static double performOperation(String operation, String firstString, String secondString) {
        Double firstOperand = Double.parseDouble(firstString);
        Double secondOperand = Double.parseDouble(secondString);
        switch (operation) {
            case "^":
                return Math.pow(firstOperand, secondOperand);
            case "*":
                return firstOperand * secondOperand;
            case "/":
                return firstOperand / secondOperand;
            case "%":
                return firstOperand % secondOperand;
            case "+":
                return firstOperand + secondOperand;
            case "-":
                return firstOperand - secondOperand;
            default:
                try {
                    throw new Exception("Invalid operation character used to perform operation.");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                    return -1;
                }
        }
    }


}
