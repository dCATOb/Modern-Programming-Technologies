import java.awt.BorderLayout;
import java.io.File;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

public class XmlTreeViewer extends JFrame {
    private final JTree tree = new JTree(new DefaultMutableTreeNode("Выберите XML-файл"));
    public XmlTreeViewer() { super("Дерево XML-тегов"); setDefaultCloseOperation(EXIT_ON_CLOSE); setSize(600, 500); setLocationByPlatform(true); add(new JScrollPane(tree), BorderLayout.CENTER);
        JButton open = new JButton("Открыть XML..."); open.addActionListener(e -> chooseFile()); add(open, BorderLayout.NORTH); }
    private void chooseFile() { JFileChooser chooser = new JFileChooser(); if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return; try { load(chooser.getSelectedFile()); }
        catch (Exception ex) { JOptionPane.showMessageDialog(this, ex.getMessage(), "Ошибка XML", JOptionPane.ERROR_MESSAGE); } }
    private void load(File file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false); factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        Document doc = factory.newDocumentBuilder().parse(file); doc.getDocumentElement().normalize();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(file.getName()); appendElement(doc.getDocumentElement(), root);
        tree.setModel(new DefaultTreeModel(root)); for (int i = 0; i < tree.getRowCount(); i++) tree.expandRow(i);
    }
    private void appendElement(Element element, DefaultMutableTreeNode parent) {
        // Добавляются только элементы без атрибутов, как требуется; обход охватывает всех потомков.
        if (element.getAttributes().getLength() == 0) parent = addNode(parent, element.getTagName());
        NodeList children = element.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) if (children.item(i) instanceof Element) appendElement((Element)children.item(i), parent);
    }
    private DefaultMutableTreeNode addNode(DefaultMutableTreeNode parent, String name) { DefaultMutableTreeNode node = new DefaultMutableTreeNode(name); parent.add(node); return node; }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new XmlTreeViewer().setVisible(true)); }
}
