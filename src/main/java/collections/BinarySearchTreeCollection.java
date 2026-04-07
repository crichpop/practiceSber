package collections;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class BinarySearchTreeCollection<E extends Comparable<? super E>> implements Collection<E> {
    private Node<E> head;
    private int count;

    private static final class Node<E> {
        private E value;
        private Node<E> left;
        private Node<E> right;

        private Node(E value) {
            this.value = value;
        }
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public boolean contains(Object o) {
        if (o == null) {
            return false;
        }

        @SuppressWarnings("unchecked")
        E wanted = (E) o;

        Node<E> cursor = head;
        while (cursor != null) {
            int diff = wanted.compareTo(cursor.value);
            if (diff == 0) {
                return true;
            }
            cursor = diff < 0 ? cursor.left : cursor.right;
        }
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new InOrderIterator(head);
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[count];
        int idx = 0;
        for (E item : this) {
            result[idx++] = item;
        }
        return result;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        Objects.requireNonNull(a, "Array must not be null");
        T[] out = a.length >= count ? a : java.util.Arrays.copyOf(a, count);
        int idx = 0;
        for (E item : this) {
            @SuppressWarnings("unchecked")
            T casted = (T) item;
            out[idx++] = casted;
        }
        if (out.length > count) {
            out[count] = null;
        }
        return out;
    }

    @Override
    public boolean add(E e) {
        Objects.requireNonNull(e, "Element must not be null");

        if (head == null) {
            head = new Node<>(e);
            count++;
            return true;
        }

        Node<E> prev = null;
        Node<E> walk = head;
        int diff = 0;
        while (walk != null) {
            prev = walk;
            diff = e.compareTo(walk.value);
            if (diff == 0) {
                return false;
            }
            walk = diff < 0 ? walk.left : walk.right;
        }

        Node<E> fresh = new Node<>(e);
        if (diff < 0) {
            prev.left = fresh;
        } else {
            prev.right = fresh;
        }
        count++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        if (o == null || head == null) {
            return false;
        }

        @SuppressWarnings("unchecked")
        E wanted = (E) o;

        Node<E> prev = null;
        Node<E> walk = head;

        while (walk != null) {
            int diff = wanted.compareTo(walk.value);
            if (diff == 0) {
                deleteNode(prev, walk);
                count--;
                return true;
            }
            prev = walk;
            walk = diff < 0 ? walk.left : walk.right;
        }
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean hasChanges = false;
        for (E element : c) {
            hasChanges |= add(element);
        }
        return hasChanges;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean hasChanges = false;
        for (Object element : c) {
            hasChanges |= remove(element);
        }
        return hasChanges;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        Object[] copy = toArray();
        boolean hasChanges = false;
        for (Object element : copy) {
            if (!c.contains(element)) {
                hasChanges |= remove(element);
            }
        }
        return hasChanges;
    }

    @Override
    public void clear() {
        head = null;
        count = 0;
    }

    private void deleteNode(Node<E> parent, Node<E> node) {
        if (node.left != null && node.right != null) {
            Node<E> replParent = node;
            Node<E> repl = node.right;
            while (repl.left != null) {
                replParent = repl;
                repl = repl.left;
            }
            node.value = repl.value;
            deleteSingleOrLeaf(replParent, repl);
            return;
        }
        deleteSingleOrLeaf(parent, node);
    }

    private void deleteSingleOrLeaf(Node<E> parent, Node<E> node) {
        Node<E> replacementNode = node.left != null ? node.left : node.right;
        if (parent == null) {
            head = replacementNode;
        } else if (parent.left == node) {
            parent.left = replacementNode;
        } else {
            parent.right = replacementNode;
        }
    }

    private final class InOrderIterator implements Iterator<E> {
        private final Deque<Node<E>> chain = new ArrayDeque<>();

        private InOrderIterator(Node<E> node) {
            pushLeft(node);
        }

        @Override
        public boolean hasNext() {
            return !chain.isEmpty();
        }

        @Override
        public E next() {
            if (chain.isEmpty()) {
                throw new NoSuchElementException();
            }
            Node<E> nextNode = chain.pop();
            pushLeft(nextNode.right);
            return nextNode.value;
        }

        private void pushLeft(Node<E> node) {
            Node<E> cursor = node;
            while (cursor != null) {
                chain.push(cursor);
                cursor = cursor.left;
            }
        }
    }
}
