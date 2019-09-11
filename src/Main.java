public class Main {

    public static void main(String[] args) {
        String infix = "(2*2)(3+2)";
        double output = EquationEvaluator.evaluateInfixExpression(infix);

        System.out.println("Equation: " + infix +
                "\nOutput: " + output);
    }
}
