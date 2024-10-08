import java.util.*;

public class Converter {

    public static void main(String[] args) {
        String inputString;
        Scanner keyb = new Scanner(System.in);

        while (true) {
            System.out.println("Introduce una expresion en notacion infija (o escribe 'cerrar' para salir)");
            System.out.print("> ");
            inputString = keyb.nextLine();

            if (inputString.equalsIgnoreCase("cerrar")) {
                break;
            }

            if (!validarEntrada(inputString)) {
                System.out.println("Error en la expresión (caracteres repetidos o paréntesis incompletos)");
                continue;
            }

            List<String> tokens = getTokens(inputString);
            System.out.println("Expresión inicial (infija): " + Converter.toString(tokens));

            List<String> postfix = Converter.toPostfix(tokens);
            System.out.println("Expresión en notación postfija: " + Converter.toString(postfix));

            double resultado = evaluatePostfix(postfix);
            System.out.println("Resultado: " + resultado);
        }
    }
    // METODO PARA VALIDAR LA EXPRESION
    public static boolean validarEntrada(String input) {
        // Verificar que solo tenga números, operadores y paréntesis
        if (!input.matches("[0-9+\\-*/^() ]+")) {
            return false;
        }

        // Verificar operadores repetidos
        if (input.matches(".*[+\\-*/^]{2,}.*")) {
            return false;
        }

        // Verificar que los paréntesis esten cerrados
        int balance = 0;
        for (char c : input.toCharArray()) {
            if (c == '(') {
                balance++;
            } else if (c == ')') {
                balance--;
            }
            if (balance < 0) {
                return false;
            }
        }
        return balance == 0;
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

    // Evaluar la expresión en notación postfija
    public static double evaluatePostfix(List<String> postfix) {
        Stack<Double> stack = new Stack<>();

        for (String token : postfix) {
            if (isOperand(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token)) {
                double n1 = stack.pop();
                double n2 = stack.pop();
                switch (token) {
                    case "+": stack.push(n2 + n1);
                        break;
                    case "-": stack.push(n2 - n1);
                        break;
                    case "*": stack.push(n2 * n1);
                        break;
                    case "/": stack.push(n2 / n1);
                        break;
                    case "^": stack.push(Math.pow(n2, n1));
                        break;
                }
            }
        }
        return stack.pop();
    }
}
