import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.List;

public class ShuttleScheduler<T extends Comparable<? super T>> {
    
    private List<Node<T>> trees = new LinkedList<>();
    
    private static class Node<T> {
        T data;
        Node<T> child;
        Node<T> sibling;
        
        Node(T data) {
            this.data = data;
        }
    }

    static class Hall implements Comparable<Hall> {
        String name;
        int load;  // Number of passengers waiting

        Hall(String name, int load) {
            this.name = name;
            this.load = load;
        }

        @Override
        public int compareTo(Hall other) {
            return other.load - this.load;
        }

        @Override
        public String toString() {
            return name + " (" + load + " passengers)";
        }
    }

    public void insert(T data) {
        ShuttleScheduler<T> oneItem = new ShuttleScheduler<>();
        Node<T> node = new Node<>(data);
        oneItem.trees.add(node);
        merge(oneItem);
    }

    public T deleteMax() {
        if (trees.isEmpty()) {
            return null;
        }
        
        int maxIndex = findMaxIndex();
        T maxVal = trees.get(maxIndex).data;
        Node<T> oldRoot = trees.remove(maxIndex);
        
        // Reverse the children of the removed tree and create a new queue
        ShuttleScheduler<T> deletedTree = new ShuttleScheduler<>();
        Node<T> child = oldRoot.child;
        while (child != null) {
            Node<T> next = child.sibling;
            child.sibling = null;
            deletedTree.trees.add(0, child);
            child = next;
        }
        merge(deletedTree);
        
        return maxVal;
    }

    private int findMaxIndex() {
    int maxIndex = 0;
    for (int i = 1; i < trees.size(); i++) {
        Node<T> currentNode = trees.get(i);
        Node<T> maxNode = trees.get(maxIndex);
        if (currentNode != null && (maxNode == null || currentNode.data.compareTo(maxNode.data) > 0)) {
            maxIndex = i;
        }
    }
    return maxIndex;
}
    public boolean isEmpty() {
        return trees.isEmpty();
    }

    public void merge(ShuttleScheduler<T> other) {
    int i = 0, j = 0;
    Node<T> carry = null;

    while (i < trees.size() || j < other.trees.size() || carry != null) {
        Node<T> t1 = (i < trees.size()) ? trees.get(i) : null;
        Node<T> t2 = (j < other.trees.size()) ? other.trees.get(j) : null;

        int whichCase = (t1 != null ? 1 : 0) + (t2 != null ? 2 : 0) + (carry != null ? 4 : 0);

        switch (whichCase) {
            case 0: // No trees
            case 1: // Only t1
                i++;
                break;
            case 2: // Only t2
                if (i < trees.size()) {
                    trees.set(i, t2);
                } else {
                    trees.add(t2);
                }
                j++;
                break;
            case 3: // t1 and t2
                carry = combineTrees(t1, t2);
                trees.set(i, null);
                j++;
                break;
            case 4: // Only carry
                if (i < trees.size()) {
                    trees.set(i, carry);
                } else {
                    trees.add(carry);
                }
                carry = null;
                break;
            case 5: // t1 and carry
                carry = combineTrees(t1, carry);
                trees.set(i, null);
                break;
            case 6: // t2 and carry
                carry = combineTrees(t2, carry);
                if (i < trees.size()) {
                    trees.set(i, null);
                }
                j++;
                break;
            case 7: // t1, t2, and carry
                if (i < trees.size()) {
                    trees.set(i, carry);
                } else {
                    trees.add(carry);
                }
                carry = combineTrees(t1, t2);
                j++;
                break;
        }
        i++;
    }
    other.trees.clear();
}

    

    private Node<T> combineTrees(Node<T> t1, Node<T> t2) {
        if (t1.data.compareTo(t2.data) < 0) {
            return combineTrees(t2, t1);
        }

        t2.sibling = t1.child;
        t1.child = t2;
        return t1;
    }

    static class ShuttleStop implements Comparable<ShuttleStop> {
        int number;
        List<Hall> halls;
        int totalLoad;

        ShuttleStop(int number) {
            this.number = number;
            this.halls = new LinkedList<>();
        }

        void addHall(Hall hall) {
            this.halls.add(hall);
            this.totalLoad += hall.load;
        }

        @Override
        public int compareTo(ShuttleStop other) {
            return other.totalLoad - this.totalLoad;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Shuttle Stop ").append(number).append(" (Total ").append(totalLoad).append(" passengers) serving halls: ");
            for (Hall hall : halls) {
                sb.append(hall.name).append(", ");
            }
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        ShuttleScheduler<ShuttleStop> scheduler = new ShuttleScheduler<>();

        // Define some halls with varying passenger loads
        Hall jacarandaHall = new Hall("Jacaranda Hall", 100);
        Hall cypressHall = new Hall("Cypress Hall", 50);
        Hall liveOakHall = new Hall("Live Oak Hall", 75);
        // ... [Define other halls as needed]

        // Define shuttle stops based on the halls they serve
        ShuttleStop stop1 = new ShuttleStop(1);
        stop1.addHall(jacarandaHall);
        stop1.addHall(cypressHall);

        ShuttleStop stop2 = new ShuttleStop(2);
        stop2.addHall(liveOakHall);
        // ... [Define other shuttle stops and their halls as needed]

        // Insert shuttle stops into the scheduler
        scheduler.insert(stop1);
        scheduler.insert(stop2);
        // ... [Insert other shuttle stops as needed]

        // Send shuttles to the stops with the most passengers first
        System.out.println("Sending shuttle to: " + scheduler.deleteMax());
        System.out.println("Sending shuttle to: " + scheduler.deleteMax());
        System.out.println("Sending shuttle to: " + scheduler.deleteMax());
    }
}
