import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] values = { 5, 1, 4, 2, 8 };
        System.out.println("До сортировки: " + Arrays.toString(values));
        BubbleSort.sort(values);
        System.out.println("После сортировки: " + Arrays.toString(values));
    }
}

class BubbleSort {
    static void sort(int[] values) {
        for (int end = values.length - 1; end > 0; end--) {
            boolean swapped = false;
            for (int i = 0; i < end; i++) {
                if (values[i] > values[i + 1]) {
                    int temporary = values[i];
                    values[i] = values[i + 1];
                    values[i + 1] = temporary;
                    swapped = true;
                }
            }
            if (!swapped) return;
        }
    }
}
