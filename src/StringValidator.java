public class StringValidator {

    /**
     * Returns true if the character is one of 6 basic operators: ^, *, /, %, +, or -.
     *
     * @param op
     * @return
     */
    public static boolean isOperator(String op) {
        return (op.equals("^") ||
                op.equals("*") ||
                op.equals("/") ||
                op.equals("%") ||
                op.equals("+") ||
                op.equals("-"));
    }


}
