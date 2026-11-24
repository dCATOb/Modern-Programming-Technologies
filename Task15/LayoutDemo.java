import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.*;

public class LayoutDemo extends JFrame {
    public LayoutDemo() {
        super("Менеджеры компоновки Swing"); setDefaultCloseOperation(EXIT_ON_CLOSE); setSize(500, 300); setLocationByPlatform(true);
        setLayout(new BorderLayout(8, 8));
        JLabel north = new JLabel("JFrame использует BorderLayout", SwingConstants.CENTER); north.setBorder(BorderFactory.createEmptyBorder(10, 4, 10, 4)); add(north, BorderLayout.NORTH);
        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 20)); center.setBorder(BorderFactory.createTitledBorder("JPanel использует FlowLayout"));
        center.add(new JButton("Один")); center.add(new JButton("Два")); center.add(new JButton("Три")); add(center, BorderLayout.CENTER);
        add(new JLabel("Измените размер окна, чтобы увидеть адаптацию компоновки", SwingConstants.CENTER), BorderLayout.SOUTH);
    }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new LayoutDemo().setVisible(true)); }
}
