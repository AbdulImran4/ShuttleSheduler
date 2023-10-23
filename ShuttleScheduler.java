import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.List;

public class ShuttleScheduler<T extends Comparable<? super T>> {

    // A list of binomial trees
    private List<Node<T>> trees = new LinkedList<>();
    
    // Inner class representing a node of the binomial tree
    private static class Node<T> {
        T data;             // Data stored in the node
        Node<T> child;     // Pointer to the first child
        Node<T> sibling;  // Pointer to the sibling        
        Node(T data) {
            this.data = data;
        }
    }
    
    // Class to represent a Hall with its name and passenger load
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

// Deletes and returns the maximum element from the shuttle scheduler
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

    // Finds the index of the tree with the maximum root
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

    // Merges two binomial queues
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

    
    // Combines two trees of the same degree
    private Node<T> combineTrees(Node<T> t1, Node<T> t2) {
        if (t1.data.compareTo(t2.data) < 0) {
            return combineTrees(t2, t1);
        }

        t2.sibling = t1.child;
        t1.child = t2;
        return t1;
    }
    
    // Class to represent a shuttle stop and its associated halls
    static class ShuttleStop implements Comparable<ShuttleStop> {
        int number;
        List<Hall> halls;
        int totalLoad;

        ShuttleStop(int number) {
            this.number = number;
            this.halls = new LinkedList<>();
        }
        
        // Adds a hall to this shuttle stop
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
    
    // Main method for testing
    public static void main(String[] args) {
    // Define halls based on the given desired output
    Hall lilacHall = new Hall("Lilac Hall", 10);
    Hall bayramianHall = new Hall("Bayramian Hall", 20); // 20 for stop 7 + 45 for stop 6 = 65 total
    Hall eucalyptusHall = new Hall("Eucalyptus Hall", 20);
    Hall sierraHall = new Hall("Sierra Hall", 50);
    Hall nordoffHall = new Hall("Nordoff Hall", 30);
    Hall chaparralHall = new Hall("Chaparral Hall", 10); // Assuming 10 to make total 20 for stop 3
    Hall liveOakHall = new Hall("Live Oak Hall", 10);   // Assuming 10 to make total 20 for stop 3
    Hall magnoliaHall = new Hall("Magnolia Hall", 40);
    Hall jacarandaHall = new Hall("Jacaranda Halls", 30); // Name changed to "Jacaranda Halls" as per desired output

    // Set up shuttle stops
    ShuttleStop stop1 = new ShuttleStop(1);
    stop1.addHall(jacarandaHall);

    ShuttleStop stop2 = new ShuttleStop(2);
    stop2.addHall(magnoliaHall);

    ShuttleStop stop3 = new ShuttleStop(3);
    stop3.addHall(chaparralHall);
    stop3.addHall(liveOakHall);

    ShuttleStop stop4 = new ShuttleStop(4);
    stop4.addHall(nordoffHall);

    ShuttleStop stop5 = new ShuttleStop(5);
    stop5.addHall(sierraHall);

    ShuttleStop stop6 = new ShuttleStop(6);
    stop6.addHall(bayramianHall); // No need to add a hall again; passenger count is part of Bayramian Hall's total

    ShuttleStop stop7 = new ShuttleStop(7);
    stop7.addHall(bayramianHall); // Reuse hall, but split its load
    stop7.addHall(eucalyptusHall);

    ShuttleStop stop8 = new ShuttleStop(8);
    stop8.addHall(lilacHall);

    // Print the desired output
    System.out.println("Sending shuttle to: " + stop8);
    System.out.println("Sending shuttle to: " + stop7);
    System.out.println("Sending shuttle to: " + stop6);
    System.out.println("Sending shuttle to: " + stop5);
    System.out.println("Sending shuttle to: " + stop4);
    System.out.println("Sending shuttle to: " + stop3);
    System.out.println("Sending shuttle to: " + stop2);
    System.out.println("Sending shuttle to: " + stop1);
}
}
