import java.util.LinkedList;
import java.util.Queue;

public class ExpressionParser {

    // The queue of terms, including operators, braces, and doubles, in the order of the original expression
    Queue<String> queue;

    // The current double, not yet ready to be added to the queue
    StringBuilder currentDouble;

    /**
     * Converts a standard infix equation into a queue of terms and operations. Can dynamically differentiate
     * between plus and minus operators, and positive or negative terms. This allows the algorithm to parse expressions
     * such as 1++2 into 1+2, or 2--1 into 2 - -1.
     *
     * @param infix
     * @return A queue in the order of the infix equation (1+2 -> [1, +, 2])
     */
    public Queue<String> infixStringToQueue(String infix) {
        queue = new LinkedList<>();

        currentDouble = new StringBuilder();

        // True if the previous term was a digit of decimal
        boolean previousWasDigit = false;

        // Loop through each character in the expression
        for (int i = 0; i < infix.length(); i++) {
            char currentChar = infix.charAt(i);

            // If the character is a digit or a decimal place add the digit/decimal place to the current double
            if (Character.isDigit(currentChar) ||
                    currentChar == '.') {
                currentDouble.append(currentChar);
                // Fix the boolean
                previousWasDigit = true;
            } else {
                // If the operation is a minus or plus
                if (currentChar == '-' ||
                currentChar == '+') {
                    // If the previous was a digit
                    if (previousWasDigit) {
                        // Add the current double
                        addCurrentDouble(queue, currentDouble);
                        // Add the plus or minus
                        queue.add(Character.toString(currentChar));
                    }
                    // If the previous wasn't a digit
                    else {
                        // Add a plus or minus to the current double
                        currentDouble.append(currentChar);
                    }
                }
                // If the character is any operator except -, add the current double and the operator
                else {
                    addCurrentDouble(queue, currentDouble);
                    queue.add(Character.toString(currentChar));
                }


                // Fix the boolean
                previousWasDigit = false;
            }
        }


        // Add the current double to the queue
        addCurrentDouble(queue, currentDouble);

        // Return the queue
        return queue;
    }

    private static void addCurrentDouble(Queue<String> queue, StringBuilder currentDouble) {
        if (currentDouble.length() != 0) {
            // If the current double only contains a minus
            if (currentDouble.toString().equals("-")) {
                // Add a 1, making it -1
                currentDouble.append("1");
            }
            // Add the current double to the queue
            Double doubleToAdd = Double.parseDouble(currentDouble.toString());
            queue.add(doubleToAdd.toString());
            // Clear the current double
            currentDouble.setLength(0);
        }
    }
}
