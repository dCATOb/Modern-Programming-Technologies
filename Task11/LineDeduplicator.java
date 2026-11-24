import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Формат: первая строка — #removed=<число удалённых строк>; далее идут <число повторений>\t<исходная строка>.
 * Содержимое строк, включая пустые строки, сохраняется; табуляция-разделитель обрабатывается корректно. */
public class LineDeduplicator {
    public static int compress(Path input, Path output) throws IOException {
        List<String> lines = Files.readAllLines(input, StandardCharsets.UTF_8);
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (String line : lines) counts.put(line, counts.getOrDefault(line, 0) + 1);
        int removed = lines.size() - counts.size();
        List<String> encoded = new ArrayList<>(); encoded.add("#removed=" + removed);
        for (Map.Entry<String, Integer> entry : counts.entrySet()) encoded.add(entry.getValue() + "\t" + entry.getKey());
        Files.write(output, encoded, StandardCharsets.UTF_8);
        return removed;
    }
    public static void restore(Path compressed, Path output) throws IOException {
        List<String> encoded = Files.readAllLines(compressed, StandardCharsets.UTF_8);
        if (encoded.isEmpty() || !encoded.get(0).matches("#removed=\\d+")) throw new IOException("Недопустимый заголовок сжатого файла.");
        List<String> restored = new ArrayList<>();
        for (int i = 1; i < encoded.size(); i++) {
            int separator = encoded.get(i).indexOf('\t');
            if (separator < 1) throw new IOException("Недопустимая запись в сжатом файле, строка " + (i + 1));
            int count;
            try { count = Integer.parseInt(encoded.get(i).substring(0, separator)); }
            catch (NumberFormatException ex) { throw new IOException("Недопустимое число повторений в строке " + (i + 1), ex); }
            if (count < 1) throw new IOException("Число повторений должно быть положительным (строка " + (i + 1) + ").");
            String line = encoded.get(i).substring(separator + 1);
            for (int n = 0; n < count; n++) restored.add(line);
        }
        Files.write(output, restored, StandardCharsets.UTF_8);
    }
    public static void main(String[] args) throws IOException {
        if (args.length == 0 || args.length > 3) { System.out.println("Использование: java LineDeduplicator <исходный-файл> [сжатый-файл] [восстановленный-файл]"); return; }
        Path input = Paths.get(args[0]), compressed = Paths.get(args.length > 1 ? args[1] : "compressed.txt"), restored = Paths.get(args.length > 2 ? args[2] : "restored.txt");
        System.out.println("Удалено повторяющихся строк: " + compress(input, compressed));
        restore(compressed, restored);
        System.out.println("Созданы сжатый файл " + compressed + " и восстановленный файл " + restored);
    }
}
