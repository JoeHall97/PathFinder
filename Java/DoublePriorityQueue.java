// the java library's priority queue use ints for comparisons, 
// so need to implement a priority queue with double comparisons for more accuracy when searching
public class DoublePriorityQueue<T> {
    private class Node {
        public T item;
        public double priority;
        public Node next;

        public Node(T item, double priority)
        {
            this.item = item;
            this.priority = priority;
        }
    }

    private Node head;

    public DoublePriorityQueue()
    {
        head = null;
    }

    public void push(T item, double priority)
    {   
        Node newNode = new Node(item, priority);
        if (head == null) {
            head = newNode;
            return;
        }

        if (Double.compare(head.priority, priority) > 0) {
            newNode.next = head;
            head = newNode;
            return;
        }

        Node curr = head.next, prev = head;
        while (curr != null && Double.compare(curr.priority, priority) > 0) {
            prev = curr;
            curr = curr.next;
        }

        prev.next = newNode;
        newNode.next = curr;
    }

    public T peek()
    {
        return head.item;
    }

    public T pop()
    {
        if (head == null)
            return null;
        Node temp = head;
        head = head.next;
        return temp.item;
    }
}
