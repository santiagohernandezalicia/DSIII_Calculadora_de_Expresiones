import java.util.*;

public class Converter {

    public static void main(String[] args) {
        String inputString;

        Scanner keyb = new Scanner(System.in);

        while (true) {
            System.out.println("Introduce una expresion en notacion infija");
            System.out.print("> ");
            inputString = keyb.nextLine();
            if (inputString.equalsIgnoreCase("quit")) {
                break;
            }

            List<String> tokens = getTokens(inputString);
            System.out.println("Tokens en notación infija: " + Converter.toString(tokens));

            List<String> postfix = Converter.toPostfix(tokens);
            System.out.println("Expresión en notación postfija: " + Converter.toString(postfix));

            double result = evaluatePostfix(postfix);
            System.out.println("Resultado: " + result);
        }
    }

    /* Regresa true si el token es un operador valido */
    public static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") ||
                token.equals("*") || token.equals("/") || token.equals("^");
    }

    /* Convertir una lista de tokens a su representación como cadena de caracteres */
    public static String toString(List<String> list) {
        StringBuilder output = new StringBuilder();
        for (String token : list) {
            output.append(token);
            output.append(" ");
        }
        return output.toString();
    }

    /* Convierte los tokens de una expresión infija a una lista de tokens de una expresión postfija */
    public static ArrayList<String> toPostfix(List<String> input) {
        Stack<String> stack = new Stack<>();
        ArrayList<String> output = new ArrayList<>();
        String t;

        for (String token : input) {
            if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!(t = stack.pop()).equals("(")) {
                    output.add(t);
                }
            } else if (isOperand(token)) {
                output.add(token);
            } else if (isOperator(token)) {
                while (!stack.isEmpty() && getPrec(stack.peek()) >= getPrec(token)) {
                    output.add(stack.pop());
                }
                stack.push(token);
            }
        }
        while (!stack.isEmpty()) {
            output.add(stack.pop());
        }
        return output;
    }

    /* Verifica si el token es un operando (número) */
    public static boolean isOperand(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /* Obtener la lista de tokens a partir de una cadena que contiene una expresión en notación infija */
    public static List<String> getTokens(String input) {
        StringTokenizer st = new StringTokenizer(input, " ()+-*/^", true);
        ArrayList<String> tokenList = new ArrayList<>();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            if (token.trim().length() == 0) {
                continue;
            }
            tokenList.add(token);
        }
        return tokenList;
    }

    /* Obtener la prioridad de cada operador */
    public static int getPrec(String token) {
        switch (token) {
            case "^":
                return 3;
            case "*":
            case "/":
                return 2;
            case "+":
            case "-":
                return 1;
            default:
                return 0;
        }
    }

    /* Evaluar la expresión en notación postfija */
    public static double evaluatePostfix(List<String> postfix) {
        Stack<Double> stack = new Stack<>();

        for (String token : postfix) {
            if (isOperand(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token)) {
                double b = stack.pop();
                double a = stack.pop();
                switch (token) {
                    case "+":
                        stack.push(a + b);
                        break;
                    case "-":
                        stack.push(a - b);
                        break;
                    case "*":
                        stack.push(a * b);
                        break;
                    case "/":
                        stack.push(a / b);
                        break;
                    case "^":
                        stack.push(Math.pow(a, b));
                        break;
                }
            }
        }
        return stack.pop();
    }
}
