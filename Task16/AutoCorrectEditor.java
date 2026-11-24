import java.awt.BorderLayout;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/** Самостоятельный редактор с автоматической коррекцией в фоновом потоке. Изменение документа выполняется только в EDT. */
public class AutoCorrectEditor extends JFrame {
    private final JTextArea textArea = new JTextArea();
    private final Map<String, String> corrections = new ConcurrentHashMap<>();
    private final BlockingQueue<Boolean> requests = new LinkedBlockingQueue<>();
    private final Thread worker;
    private final Timer timer;
    private volatile boolean running = true;
    private Path currentFile;
    private boolean dirty;
    public AutoCorrectEditor() {
        super("Текстовый редактор с автокоррекцией"); setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); setSize(700, 500); setLocationByPlatform(true);
        add(new JScrollPane(textArea), BorderLayout.CENTER); setJMenuBar(createMenuBar());
        textArea.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { dirty = true; if (e.getLength() > 0) try { if (textArea.getText(e.getOffset(), e.getLength()).contains(" ")) requests.offer(true); } catch (BadLocationException ignored) { } }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { dirty = true; }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { dirty = true; }
        });
        corrections.put("превет", "привет"); corrections.put("спосибо", "спасибо"); corrections.put("севодня", "сегодня");
        worker = new Thread(this::processRequests, "поток-автокоррекции"); worker.setDaemon(true); worker.start();
        timer = new Timer(3000, e -> requests.offer(true)); timer.start();
        addWindowListener(new java.awt.event.WindowAdapter() { @Override public void windowClosing(java.awt.event.WindowEvent e) { if (confirmDiscard()) dispose(); }
            @Override public void windowClosed(java.awt.event.WindowEvent e) { shutdown(); } });
    }
    private JMenuBar createMenuBar() {
        JMenuBar bar = new JMenuBar(); JMenu file = new JMenu("Файл");
        JMenuItem open = new JMenuItem("Открыть..."); open.addActionListener(e -> openFile());
        JMenuItem save = new JMenuItem("Сохранить"); save.addActionListener(e -> saveFile());
        JMenuItem saveAs = new JMenuItem("Сохранить как..."); saveAs.addActionListener(e -> saveAs());
        JMenuItem exit = new JMenuItem("Выход"); exit.addActionListener(e -> { if (confirmDiscard()) dispose(); });
        file.add(open); file.add(save); file.add(saveAs); file.addSeparator(); file.add(exit); bar.add(file); return bar;
    }
    private void openFile() { if (!confirmDiscard()) return; JFileChooser chooser = new JFileChooser(); if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        try { currentFile = chooser.getSelectedFile().toPath(); textArea.setText(new String(Files.readAllBytes(currentFile), StandardCharsets.UTF_8)); dirty = false; textArea.setCaretPosition(0); }
        catch (IOException ex) { showError(ex); } }
    private void saveAs() { JFileChooser chooser = new JFileChooser(); if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) { currentFile = chooser.getSelectedFile().toPath(); saveFile(); } }
    private void saveFile() { if (currentFile == null) { saveAs(); return; } try { Files.write(currentFile, textArea.getText().getBytes(StandardCharsets.UTF_8)); dirty = false; } catch (IOException ex) { showError(ex); } }
    private boolean confirmDiscard() { return !dirty || JOptionPane.showConfirmDialog(this, "Закрыть без сохранения?", "Несохранённый текст", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION; }
    private void showError(Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка работы с файлом", JOptionPane.ERROR_MESSAGE); }
    private void processRequests() { while (running) try { requests.take(); SwingUtilities.invokeLater(this::correctLastWord); } catch (InterruptedException ex) { if (!running) return; } }
    private void correctLastWord() {
        if (!running) return;
        Document document = textArea.getDocument(); int caret = textArea.getCaretPosition();
        try {
            int end = caret; if (end > 0 && textArea.getText(end - 1, 1).equals(" ")) end--;
            int start = end; while (start > 0 && Character.isLetter(textArea.getText(start - 1, 1).charAt(0))) start--;
            if (start == end) return;
            String word = document.getText(start, end - start), replacement = corrections.get(word.toLowerCase(java.util.Locale.ROOT));
            if (replacement != null) { document.remove(start, end - start); document.insertString(start, replacement, null); textArea.setCaretPosition(caret); }
        } catch (BadLocationException ignored) { }
    }
    private void shutdown() { running = false; timer.stop(); worker.interrupt(); }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new AutoCorrectEditor().setVisible(true)); }
}
