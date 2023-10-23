import java.util.PriorityQueue;

public class BinomialQueue<T extends Comparable<T>> {
    private PriorityQueue<T> queue = new PriorityQueue<>();

    public void insert(T item) {
        queue.add(item);
    }

    public T deleteMax() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
