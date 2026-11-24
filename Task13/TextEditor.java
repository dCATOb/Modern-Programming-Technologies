import java.awt.BorderLayout;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.*;

public class TextEditor extends JFrame {
    protected final JTextArea textArea = new JTextArea();
    protected Path currentFile;
    private boolean dirty;
    public TextEditor() {
        super("Текстовый редактор"); setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); setSize(700, 500); setLocationByPlatform(true);
        add(new JScrollPane(textArea), BorderLayout.CENTER); setJMenuBar(createMenuBar());
        textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { dirty = true; }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { dirty = true; }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { dirty = true; }
        });
        addWindowListener(new java.awt.event.WindowAdapter() { @Override public void windowClosing(java.awt.event.WindowEvent e) { if (confirmDiscard()) dispose(); } });
    }
    protected JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar(); JMenu file = new JMenu("Файл");
        JMenuItem open = new JMenuItem("Открыть..."); open.addActionListener(e -> openFile());
        JMenuItem save = new JMenuItem("Сохранить"); save.addActionListener(e -> saveFile());
        JMenuItem saveAs = new JMenuItem("Сохранить как..."); saveAs.addActionListener(e -> saveAs());
        JMenuItem exit = new JMenuItem("Выход"); exit.addActionListener(e -> { if (confirmDiscard()) dispose(); });
        file.add(open); file.add(save); file.add(saveAs); file.addSeparator(); file.add(exit); bar.add(file); return bar;
    }
    private void openFile() {
        if (!confirmDiscard()) return;
        JFileChooser chooser = new JFileChooser(); if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try { currentFile = chooser.getSelectedFile().toPath(); textArea.setText(new String(Files.readAllBytes(currentFile), StandardCharsets.UTF_8)); dirty = false; textArea.setCaretPosition(0); }
        catch (IOException ex) { showError(ex); }
    }
    private void saveAs() { JFileChooser chooser = new JFileChooser(); if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) { currentFile = chooser.getSelectedFile().toPath(); saveFile(); } }
    private void saveFile() { if (currentFile == null) { saveAs(); return; } try { Files.write(currentFile, textArea.getText().getBytes(StandardCharsets.UTF_8)); dirty = false; }
        catch (IOException ex) { showError(ex); } }
    private boolean confirmDiscard() { return !dirty || JOptionPane.showConfirmDialog(this, "Закрыть без сохранения?", "Несохранённый текст", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION; }
    private void showError(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка работы с файлом", JOptionPane.ERROR_MESSAGE); }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new TextEditor().setVisible(true)); }
}
