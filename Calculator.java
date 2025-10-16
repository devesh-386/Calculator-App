import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JFrame {
    private final JTextField display;
    private double result = 0;
    private String currentOp = "";
    private boolean startNewNumber = true;

    public Calculator() {
        setTitle("Calculator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(320, 420);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(6, 6));

        display = new JTextField("0");
        display.setFont(new Font("SansSerif", Font.PLAIN, 26));
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setEditable(false);
        display.setBackground(Color.WHITE);
        display.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        add(display, BorderLayout.NORTH);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 4, 6, 6));

        String[] buttons = {
            "CE", "C", "←", "/",
            "7", "8", "9", "*",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "±", "0", ".", "="
        };

        for (String txt : buttons) {
            JButton btn = new JButton(txt);
            btn.setFont(new Font("SansSerif", Font.BOLD, 20));
            btn.addActionListener(this::onButtonClick);
            panel.add(btn);
        }

        add(panel, BorderLayout.CENTER);

        // Make UI a bit nicer
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void onButtonClick(ActionEvent e) {
        String cmd = ((JButton)e.getSource()).getText();

        if ("0123456789".contains(cmd)) {
            appendDigit(cmd);
        } else if (cmd.equals(".")) {
            appendDot();
        } else if (cmd.equals("C")) {
            clearAll();
        } else if (cmd.equals("CE")) {
            clearEntry();
        } else if (cmd.equals("←")) {
            backspace();
        } else if (cmd.equals("±")) {
            toggleSign();
        } else if (cmd.equals("=")) {
            calculate();
            currentOp = "";
        } else { // operator: + - * /
            setOperator(cmd);
        }
    }

    private void appendDigit(String d) {
        if (startNewNumber) {
            display.setText(d);
            startNewNumber = false;
        } else {
            if (display.getText().equals("0")) display.setText(d);
            else display.setText(display.getText() + d);
        }
    }

    private void appendDot() {
        if (startNewNumber) {
            display.setText("0.");
            startNewNumber = false;
        } else if (!display.getText().contains(".")) {
            display.setText(display.getText() + ".");
        }
    }

    private void clearAll() {
        display.setText("0");
        result = 0;
        currentOp = "";
        startNewNumber = true;
    }

    private void clearEntry() {
        display.setText("0");
        startNewNumber = true;
    }

    private void backspace() {
        if (startNewNumber) return;
        String s = display.getText();
        if (s.length() <= 1) {
            display.setText("0");
            startNewNumber = true;
        } else {
            display.setText(s.substring(0, s.length() - 1));
        }
    }

    private void toggleSign() {
        if (display.getText().equals("0")) return;
        if (display.getText().startsWith("-"))
            display.setText(display.getText().substring(1));
        else
            display.setText("-" + display.getText());
    }

    private void setOperator(String op) {
        try {
            double x = Double.parseDouble(display.getText());
            if (currentOp.isEmpty()) {
                result = x;
            } else {
                result = applyOp(result, x, currentOp);
                display.setText(trimDouble(result));
            }
            currentOp = op;
            startNewNumber = true;
        } catch (NumberFormatException ex) {
            display.setText("Error");
            startNewNumber = true;
        } catch (ArithmeticException ex) {
            display.setText("Error");
            startNewNumber = true;
        }
    }

    private void calculate() {
        if (currentOp.isEmpty()) return;
        try {
            double x = Double.parseDouble(display.getText());
            result = applyOp(result, x, currentOp);
            display.setText(trimDouble(result));
            startNewNumber = true;
        } catch (NumberFormatException | ArithmeticException ex) {
            display.setText("Error");
            startNewNumber = true;
        }
    }

    private double applyOp(double a, double b, String op) {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/":
                if (b == 0) throw new ArithmeticException("Division by zero");
                return a / b;
            default: return b;
        }
    }

    private String trimDouble(double val) {
        if (val == (long) val) return String.format("%d", (long) val);
        else return String.format("%s", val);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Calculator calc = new Calculator();
            calc.setVisible(true);
        });
    }
}
