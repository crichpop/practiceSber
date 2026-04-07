import collections.BinarySearchTreeCollection;

import java.util.Collection;

public class Main {
    public static void main(String[] args) {
        Collection<Integer> treeCollection = new BinarySearchTreeCollection<>();

        treeCollection.add(10);
        treeCollection.add(5);
        treeCollection.add(15);
        treeCollection.add(12);
        treeCollection.add(3);

        System.out.println("Contains 12: " + treeCollection.contains(12));
        System.out.println("Contains 100: " + treeCollection.contains(100));

        System.out.print("Iterator traversal: ");
        for (Integer value : treeCollection) {
            System.out.print(value + " ");
        }
        System.out.println();

        treeCollection.remove(10);
        System.out.print("After remove 10: ");
        for (Integer value : treeCollection) {
            System.out.print(value + " ");
        }
        System.out.println();
    }
}
