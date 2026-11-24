import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.*;

public class SwingCalculator extends JFrame {
    private final JTextField display = new JTextField("0");
    private double stored;
    private String operation;
    private boolean startNew = true;
    public SwingCalculator() {
        super("Калькулятор"); setDefaultCloseOperation(EXIT_ON_CLOSE); setSize(300, 360); setLocationByPlatform(true);
        display.setHorizontalAlignment(JTextField.RIGHT); display.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 26)); display.setEditable(false); add(display, BorderLayout.NORTH);
        JPanel keys = new JPanel(new GridLayout(4, 4, 4, 4));
        String[] labels = {"7","8","9","÷","4","5","6","×","1","2","3","−","C","0","=","+"};
        for (String label : labels) { JButton button = new JButton(label); button.addActionListener(e -> press(label)); keys.add(button); }
        add(keys, BorderLayout.CENTER);
    }
    private void press(String key) {
        if (key.matches("[0-9]")) { display.setText(startNew || display.getText().equals("Ошибка") ? key : display.getText() + key); startNew = false; return; }
        if (key.equals("C")) { display.setText("0"); stored = 0; operation = null; startNew = true; return; }
        if (key.equals("=")) { calculate(); operation = null; startNew = true; return; }
        if (operation != null && !startNew) calculate();
        try { stored = Double.parseDouble(display.getText()); operation = key; startNew = true; }
        catch (NumberFormatException ex) { display.setText("Ошибка"); }
    }
    private void calculate() {
        try {
            double right = Double.parseDouble(display.getText()), result;
            switch (operation) {
                case "+": result = stored + right; break; case "−": result = stored - right; break;
                case "×": result = stored * right; break; case "÷": if (right == 0) throw new ArithmeticException(); result = stored / right; break;
                default: return;
            }
            display.setText(Double.isFinite(result) ? Double.toString(result) : "Ошибка"); stored = result;
        } catch (ArithmeticException | NumberFormatException ex) { display.setText("Ошибка"); operation = null; }
    }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new SwingCalculator().setVisible(true)); }
}
