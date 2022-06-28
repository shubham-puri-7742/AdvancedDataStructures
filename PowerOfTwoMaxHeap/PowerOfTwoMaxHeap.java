import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import static java.lang.Character.toLowerCase;

/** POWER OF TWO MAX HEAP (D-ARY HEAP WITH D = 2 ^ X, for a constructor argument x)
 * CHECKLIST
 * Heap property (max)
 * Each parent must have 2^x (parameter of the ctor) children
 * Insert
 * PopMax
 * Optimise!!!
 *
 */

class PowerOfTwoMaxHeap {
  // number of children (2 ^ x, for an argument supplied to the ctor)
  private final int children;
  // current size of the heap
  private int size;
  // the actual array-backed heap
  private final int[] heap;


  // ctor
  public PowerOfTwoMaxHeap(int maxSize, int logChildren) {
    // initialise to size 0
    size = 0;
    // 2^x children
    children = (int) Math.pow(2, logChildren);
    // initialise the heap with all -1s (could use another value in a use case where negative values are in the data)
    heap = new int[maxSize + 1];
    Arrays.fill(heap, -1);
  }

  // checks if the heap is empty
  public boolean empty() {
    return size == 0;
  }

  // checks if the heap is full
  public boolean full() {
    return size == heap.length;
  }

  // clears the heap
  public void clear() {
    size = 0;
  }

  // gets the parent of the element at a given position
  private int parent(int pos) {
    return (pos - 1) / children;
  }

  // gets the nth child of the element at the given position
  private int nthChild(int pos, int n) {
    return children * pos + n;
  }

  // gets the max child of an element
  private int maxChild(int pos) {
    // start with the first
    int result = nthChild(pos, 1);
    // starting with the next...
    int n = 2;
    int i = nthChild(pos, n);
    // check all the subsequent children
    while ((n <= children) && (i < size)) {
      // if it has a greater value, set it as the max
      if (heap[i] > heap[result]) {
        result = i;
      }
      i = nthChild(pos, n++);
    }
    return result;
  }

  // maintains the heap property up from a given element
  private void heapifyUp(int pos) {
    int temp = heap[pos];
    // as long as the index is valid and the current element is greater than its parent
    while (pos > 0 && temp > heap[parent(pos)]) {
      // keep moving it up (= move its parents down)
      heap[pos] = heap[parent(pos)];
      pos = parent(pos);
    }
    // assign the temp value to the element that breaks the loop
    heap[pos] = temp;
  }

  // maintains the heap property down from a given element
  private void heapifyDown(int pos) {
    int child, temp = heap[pos];
    // as long as the first child index is less than the size (= the element has at least one child)
    while (nthChild(pos, 1) < size) {
      // get the max child index
      child = maxChild(pos);
      // if the corresponding element has a greater value
      if (heap[child] > temp) {
        // move it down (= move its child up)
        heap[pos] = heap[child];
      } else {
        break;
      }
      // update the current index
      pos = child;
    }
    // assign the temp value to the element that breaks the loop
    heap[pos] = temp;
  }

  // insert a given value at the appropriate position while maintaining the heap property
  public void insert(int val) {
    // overflow error
    if (full()) {
      throw new NoSuchElementException("Overflow!");
    }
    // insert at the end, increment the size...
    heap[size++] = val;
    // and heapify up from the last element
    heapifyUp(size - 1);
  }

  // delete the max element while maintaining the heap property
  int popMax() {
    // underflow error
    if (empty()) {
      throw new NoSuchElementException("Underflow!");
    }
    // the first (index 0) element is always the greatest
    int max = heap[0];
    // make the last element the top element; decrement the size
    heap[0] = heap[--size];
    // heapify down from the root
    heapifyDown(0);
    // return the max value popped above
    return max;
  }

  // display the heap
  public void display() {
    // column headings
    System.out.println("PARENT\tCHILDREN");
    // for each element up to size / children (to ensure no repeats)
    for (int i = 0; i <= size / children; ++i) {
      // print the node
      System.out.print(" " + heap[i]);
      // for each of its children
      for (int j = 1; j <= children; ++j) {
        // if the child is not -1 (our 'empty' flag)
        if (heap[children * i + j] != -1) {
          // display it
          System.out.print("\t\t" + heap[children * i + j]);
        }
      }
      // blank line
      System.out.println();
    }
  }
}

// DRIVER CODE
public class Main {
  public static void main(String[] args) {

    // scanner for input
    Scanner s = new Scanner(System.in);
    // intro
    System.out.println("POWER OF TWO HEAP TEST\n\n");
    System.out.println("Enter the max size and x (the heap will have 2^x children:");

    // initialise the heap
    PowerOfTwoMaxHeap h = new PowerOfTwoMaxHeap(s.nextInt(), s.nextInt());

    // do loop for user input
    char ch;
    do {
      // menu
      System.out.println("\nHeap operations:\n1. Insert\n2. Pop Max\n3. Full?\n4. Empty?\n5. Clear");
      int choice = s.nextInt();
      switch (choice) {
        case 1: // INSERT
          try {
            // get the input and try to insert it
            System.out.println("Enter the int to insert:");
            h.insert(s.nextInt());
          } catch (Exception e) {
            // catch any errors
            System.out.println(e.getMessage());
          }
          break;
        case 2: // POP MAX
          try {
            // try to pop the max value
            System.out.println("Trying to pop max...");
            h.popMax();
          } catch (Exception e) {
            // catch any errors
            System.out.println(e.getMessage());
          }
          break;
        case 3: // CHECK FULL
          System.out.println("Full? " + h.full());
          break;
        case 4: // CHECK EMPTY
          System.out.println("Empty? " + h.empty());
          break;
        case 5: // CLEAR HEAP
          System.out.println("Clearing...");
          h.clear();
          break;
        default: // Some gibberish input
          System.out.println("Invalid choice!");
          break;
      }
      // display the heap after each operation
      System.out.println("UPDATED HEAP:");
      h.display();

      // choice to run again
      System.out.println("Run again? (y/n)");
      ch = s.next().charAt(0);
    } while (toLowerCase(ch) == 'y'); // repeat on anything starting with 'y' or 'Y'
  }
}