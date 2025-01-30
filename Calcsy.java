import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Calcsy extends JFrame implements ActionListener {
    private final JTextField display;
    private double firstNumber = 0;
    private String operator = "";
    private boolean newInput = true;

    public Calcsy() {
        // Frame setup
        setTitle("Calcsy");
        setSize(350, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main container with dark theme
        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.setBackground(new Color(45, 45, 45));

        // Display setup
        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 36));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(new Color(30, 30, 30));
        display.setForeground(Color.WHITE);
        display.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        display.setPreferredSize(new Dimension(350, 100));
        container.add(display, BorderLayout.NORTH);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 5, 5));
        buttonPanel.setBackground(new Color(45, 45, 45));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Button labels
        String[] buttons = {
            "C", "±", "%", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            ".", "0", "⌫", "="
        };

        // Create and style buttons
        for (String text : buttons) {
            JButton button = createStyledButton(text);
            button.addActionListener(this);
            buttonPanel.add(button);
        }

        container.add(buttonPanel, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 24));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setBackground(new Color(60, 60, 60));
        button.setForeground(Color.WHITE);
        
        // Operator buttons color
        if (text.matches("[÷×\\-+%=]")) {
            button.setBackground(new Color(255, 149, 0));
        }
        // Special buttons color
        if (text.matches("[C±%⌫]")) {
            button.setBackground(new Color(80, 80, 80));
        }

        // Hover effects
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                button.setBackground(button.getBackground().brighter());
            }
            public void mouseExited(MouseEvent evt) {
                button.setBackground(button.getBackground().darker());
            }
        });

        return button;
    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        if (command.matches("[0-9]")) {
            handleNumberInput(command);
        } else if (command.equals(".")) {
            handleDecimalPoint();
        } else if (command.matches("[÷×\\-+]")) {
            handleOperator(command);
        } else if (command.equals("=")) {
            calculateResult();
        } else if (command.equals("C")) {
            clearCalculator();
        } else if (command.equals("±")) {
            toggleSign();
        } else if (command.equals("⌫")) {
            handleBackspace();
        } else if (command.equals("%")) {
            handlePercentage();
        }
    }

    private void handleNumberInput(String number) {
        if (newInput) {
            display.setText("");
            newInput = false;
        }
        display.setText(display.getText() + number);
    }

    private void handleDecimalPoint() {
        if (newInput) {
            display.setText("0.");
            newInput = false;
        } else if (!display.getText().contains(".")) {
            display.setText(display.getText() + ".");
        }
    }

    private void handleOperator(String newOperator) {
        if (!operator.isEmpty() && !newInput) {
            calculateResult();
        }
        firstNumber = Double.parseDouble(display.getText());
        operator = newOperator;
        newInput = true;
    }

    private void calculateResult() {
        if (operator.isEmpty()) return;
        
        double secondNumber = Double.parseDouble(display.getText());
        double result = 0;

        switch (operator) {
            case "+" -> result = firstNumber + secondNumber;
            case "-" -> result = firstNumber - secondNumber;
            case "×" -> result = firstNumber * secondNumber;
            case "÷" -> {
                if (secondNumber == 0) {
                    display.setText("Error");
                    clearCalculator();
                    return;
                }
                result = firstNumber / secondNumber;
            }
        }

        display.setText(formatResult(result));
        operator = "";
        newInput = true;
    }

    private String formatResult(double result) {
        if (result == (int) result) {
            return String.valueOf((int) result);
        }
        return String.valueOf(result);
    }

    private void clearCalculator() {
        display.setText("0");
        firstNumber = 0;
        operator = "";
        newInput = true;
    }

    private void toggleSign() {
        double value = Double.parseDouble(display.getText());
        display.setText(formatResult(-value));
    }

    private void handleBackspace() {
        String currentText = display.getText();
        if (!currentText.isEmpty() && !currentText.equals("0")) {
            String newText = currentText.substring(0, currentText.length() - 1);
            display.setText(newText.isEmpty() ? "0" : newText);
            newInput = false;
        }
    }

    private void handlePercentage() {
        double value = Double.parseDouble(display.getText());
        display.setText(formatResult(value / 100));
        newInput = true;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}
        
        SwingUtilities.invokeLater(() -> new Calcsy().setVisible(true));
    }
}