import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.Set;
import java.util.Arrays;
import java.util.Stack;
import java.util.PriorityQueue;
import java.util.Collections;
import java.util.Comparator;
import java.util.BitSet;
import java.util.TreeMap;
import java.util.HashSet;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.ThreadLocalRandom;

public class EOPI {
		
//#5.7

	public static class Problem5_7 {

		public static double power(double x, int y)
		{
			double r = 1;
			boolean isNeg = y < 0;
			if (isNeg)
				y = -y;
			
			for(double f = x; y != 0; f *= f, y >>= 1) {
				if ((y & 1) != 0)
					r *= f;
			}
			
			return !isNeg ? r : 1/r;
		}
	}

//#5.6

//One way would be to use binary long division.

//Another way is to reconstruct the quotient one bit at a time starting with its highest bit.  Determine highest set of i's for which 2^i * y fits
//into x: first find max(i) such that 2^i *y <= x.  Then let x1 = x - 2^i * y and repeat for x1 and so on ... x_n till x_n = 0.
//For each step first check i such that 2^i * y has the same number bits as x, if 2 ^i * y <= x the answer is i else if 2 ^ i * y > x then
//the answer is i - 1.  Looks like this cannot be done in constant time.  Long division looks more simple, so let's go with that:

	public static class Problem5_6 {
		//O(n)
		//can be optimized to O(log(n)) (binary search)
		private static int hiBit(int n)
		{
			return 1 << (31 - Integer.numberOfLeadingZeros(n));
		}

		//This solution is O(n) where n is number of bits of x.
		public static int longDivision(int x, int y)
		{
			int m = hiBit(x), d = 0, q = 0;
			
			while(m > 0) {
				d <<= 1;
				if ((x & m) != 0)
					d |= 1;
				m >>= 1;
				q <<= 1;
				if (d >= y) {
					q |= 1;
					d -= y;
				}
			}
			
			return q;
		}	
	}

//#7.1

	public static class Problem6_1 {
		
		//The only way i see so far is to do a regular quick-sort partition and then go through the left side again and move all
		//equal to a[i] to the right.
		
		public static void partition(int [] a, int i)
		{
			int e = a[i], l = 0, r = a.length - 1;
			while(l <= r) {
				if (a[l] <= e)
					l++;
				else {
					if (l != r) {
						int v = a[r];
						a[r] = a[l];
						a[l] = v;
					}
					r--;
				}
			}
			l = 0;
			while(l < r) {
				if (a[l] != e)
					l++;
				else {
					a[l] = a[r];
					a[r] = e;
					r--;
				}
			}
		}
		
		//Looks like there is a way to do it with only one partition, see the solution.
		
	}
	
	public static class Problem6_8 {
		
		//We only need to consider height coordinates.  At any time that the robot is at or below its initial height, the battery will have full capacity.
		//The min capacity need to cover the max climb from any of these positions, that is we need to find max (j > i) difference of h[j] - h[i].  The
		//min capacity will be proportional this max-difference (=max-differece of h * N joules per meter).  If robot has less capacity it won't be able to make
		//path between points i and j.  If it has more, it's capacity at point j will be > 0 which means it would make it between i and j with less capacity. (In case
		//the starint point is the lowest, the min capacity needed will be proportional to difference between highest point and start point, which is also
		//max difference in this case.)  So what we need to find is max-difference in the height array multiplied by number of Joules needed to climb 1 meter.
		
		public static double max_difference(double [] h)
		{
			double min = Double.MAX_VALUE, md = 0;
			for(int i = 0; i < h.length; i++) {
				if (h[i] < min)
					min = h[i];
				double c = h[i] - min;
				if (md < c)
					md = c;
			}
			return md;
		}
		
	}
	
	public static class Problem_6_8_1 {
		
		//In inteber array, find length of longest subarray all of whose entries are equal.
		
		public static int llsaee(int [] a)
		{
			if (a.length == 0)
				return 0;
			
			int c = 1, l = 1;
			for(int i = 1; i < a.length; i++) {
				if (a[i] == a[i-1])
					c++;
				else {
					if (l < c)
						l = c;
					c = 1;
				}
			}
			return l;
		}
		
	}
	
	public static class Problem_6_7 {
		
		//Go through the array and for each element i <= a.length put it into i's element (assuming 1-based indexing, so i-1's element in 0-based indexing).
		//Then go through the array again to find first out-of-place element ((i-1)'s element != i).
		
		public static int findFirstMissing(int [] a)
		{
			for(int i = 0; i < a.length; i++) {
				int x = a[i];
				if (x > 0 && x <= a.length && x != a[x-1]) {
					int tmp = a[x-1];
					a[x-1] = x;
					a[i] = tmp;
				}
			}
			int s = 1;
			while(s <= a.length && a[s-1] == s)
				s++;
			return s;
		}
		
	}
	
	public static class Problem_7_2 {
		
		//First, we remove b's in one loop, by iterating and keeping the shift count.
		//To replaces a's, we go from the end and move the contents to the end of the array, since the array has enough space to hold the result.
		
		public static void transform(char [] s, int len)
		{
			//remove b's
			for(int i = 0, c = 0; i < len; i++)
				if (s[i] == 'b')
					c++;
				else
					s[i-c] = s[i];
				
			//replace a's
			for(int i = len - 1, j = s.length - 1; i >= 0; i--, j--)
				if (s[i] == 'a') {
					s[j--] = 'd';
					s[j] = 'd';
				}
				else
					s[j] = s[i];
		}
		
		//It is easy to see that j will never become < i because if it did, this would mean we don't have enough space to transform the remainder (beginning)
		//of the string which contradicts the assumption that the array has enough space to accomodate the transformed string.
		
	}
	
	public static class Problem_7_4 {
		
		//end is position right after end of the word or string
		public static void reverse(char [] s, int begin, int end)
		{
			for(int i = begin, j = end -1; i < j; i++, j--) {
				char c = s[i];
				s[i] = s[j];
				s[j] = c;
			}
		}
		
		//First, we reverse the whole sentence.  Then we reverse each word back.
		public static void reverseWords(char [] s)
		{
			reverse(s, 0, s.length);
			int begin = 0, end = 0;
			while(end < s.length) {
				for(begin = end; begin < s.length && s[begin] == ' '; begin++);
				if (begin == s.length)
					break;
				end = begin;
				for(; end < s.length && s[end] != ' '; end++);
				reverse(s, begin, end);
			}
		}
	}

	public static class IntNode {
		int data;
		IntNode next;
		IntNode(int data)
		{
			this.data = data;
		}
	}
	
	public static class IntList {
		IntNode head;
		IntList() {}
		IntList(IntNode head) { this.head = head; }
	}	
	
	public static void printIntList(IntList l)
	{
		System.out.print('<');
		IntNode n = l.head;
		while(n != null) {
			System.out.print(n.data);
			if (n.next != null)
				System.out.print(',');
			n = n.next;
		}
		System.out.println('>');
	}
	
	public static void printIntList(IntNode n)
	{
		printIntList(new IntList(n));
	}
	
	public static IntList createSeqIntList(int len)
	{
		IntList l = new IntList();
		IntNode tail = null;
		for(int i = 0; i < len; i++) {
			IntNode n = new IntNode(i);
			if (tail == null) {
				tail = n;
				l.head = tail;
			}
			else {
				tail.next = n;
				tail = n;
			}
		}
		return l;
	}
	
	public static IntList intArrayToIntList(int [] a)
	{
		IntList l = new IntList();
		IntNode tail = null;
		if (a == null || a.length == 0)
			return l;
		for(int i = 0; i < a.length; i++) {
			IntNode n = new IntNode(a[i]);
			if (tail == null) {
				tail = n;
				l.head = n;
			}
			else {
				tail.next = n;
				tail = n;
			}
		}
		return l;
	}
	
	public static IntList readIntList() throws Exception
	{
		return intArrayToIntList(readIntArray());
	}
	
	//doubly-linked list
	public static class DLNode<T> {
		DLList<T> list;
		T data;
		DLNode next, prev;
		DLNode() {}
		DLNode(T data)
		{
			this.data = data;
		}
	}
	
	public static class DLList<T> {
		DLNode<T> head, tail;
		int size;
		
		boolean inList(DLNode<T> n)
		{
			return n.list == this;
		}
		
		void addFirst(DLNode<T> n)
		{
			n.list = this;
			size++;
			if (head == null)
				tail = head = n;
			else {
				n.next = head;
				head.prev = n;
				head = n;
			}
		}

		void addLast(DLNode<T> n)
		{
			n.list = this;
			size++;
			if (head == null)
				tail = head = n;
			else {
				n.prev = tail;
				tail.next = n;
				tail = n;
			}
		}
		
		void remove(DLNode<T> n)
		{
			if (n.prev == null) { //head
				head = n.next;
				if (head == null)
					tail = null;
				else
					head.prev = null;
			}
			else if (n.next == null) { //tail
				tail = n.prev;
				if (tail == null)
					head = null;
				else
					tail.next = null;
			}
			else {
				n.prev.next = n.next;
				n.next.prev = n.prev;
			}
			n.list = null;
			size--;
		}
		
	}
	
	public static class Problem_8_2 {
				
		public static void reverse(IntList l)
		{			
			IntNode n = l.head, r = null;
			while(n != null) {
				IntNode next = n.next;
				n.next = r;
				r = n;
				n = next;
			}
			l.head = r;
		}
		
	}
	
	public static class Problem_8_13 {
		
		//The way I see the solution is: find the middle of the list, reverse the second half of the list (nodes after the middle)
		//then merge the first half with the reversed second half.  To find the middle, we can have a loop with 2 pointer, one
		//travelling one node at a time, another 2 nodes at a time.  When 2nd pointer hits the end (or node before end), 1st pointer
		//will be in the middle.
		//An alternative way is to reverse the first half of the list instead of 2nd half and then reverse-merge the 2 halves.
		//This way reversing the 1st half can be combined with finding the middle, so we save one loop.  Then we merge 2nd half with
		//the reversed 1st while reversing the 1st half nodes, this can be done together.
		//I'm not sure if there is better solution in the book, but this is the best I could come up with so far.
		//P.S. Well, I looked ahead in the book and looks like they have the same idea for the solution as my first idea.
		//Here I will implement the 2nd idea.
		
		public static void addToFront(IntList l, IntNode n)
		{
			n.next = l.head;
			l.head = n;
		}
		
		public static void zip(IntList l)
		{
			IntList l1r = new IntList();
			
			//Find middle and reverse the 1st half of the list.
			IntNode n1 = l.head, n2 = l.head;
			while(n2 != null && n2.next !=  null) {
				n2 = n2.next.next;
				IntNode next = n1.next;
				addToFront(l1r, n1);
				n1 = next;
			}
						
			//Reverse-merge.
			l.head = null;
			//if odd number of elements, the node pointed to by n1 should go first
			if (n2 != null) { //list has odd number of elements
				IntNode next = n1.next;
				addToFront(l, n1);
				n1 = next;
			}

			//n1 points to the 2nd half, and we point n2 to the reversed 1st half
			n2 = l1r.head;
			while(n1 != null) {
				assert(n2 != null);
				IntNode next = n1.next;
				addToFront(l, n1);
				n1 = next;
				next = n2.next;
				addToFront(l, n2);
				n2 = next;
			}
		}
		
		public static void test()
		{
			for(int i = 0; i < 10; i++) {
				IntList l = createSeqIntList(i);
				printIntList(l);
				zip(l);
				printIntList(l);
				System.out.println();
			}
		}
	}
	
	
	public static class IntStack {
		public static int MAX_ELEM = 100;
		protected int [] a = new int[MAX_ELEM];
		protected int cnt;
		
		public void push(int v) throws Exception
		{
			if (cnt == MAX_ELEM)
				throw new Exception("IntStack is full");
			a[cnt++] = v;
		}
		
		public int pop() throws Exception
		{
			if (cnt == 0)
				throw new java.util.EmptyStackException();
			return a[--cnt];
		}
		
		public int peek() throws Exception
		{
			if (cnt == 0)
				throw new java.util.EmptyStackException();
			return a[cnt-1];			
		}
		
		public boolean isEmpty() { return cnt == 0; }
	}
	
	public static class Problem_9_1 {
	
		//In addition to our stack, we will keep additional stack such that the top element on this second stack is equal to the max
		//of the elements of the first stack.
		
		public static class MaxIntStack extends IntStack {
			
			private IntStack m = new IntStack();
			
			public void push(int v) throws Exception
			{
				super.push(v);
				m.push(m.isEmpty() ? v : Math.max(v, m.peek()));
			}
			
			public int pop() throws Exception
			{
				m.pop();
				return super.pop();
			}
			
			public int max() throws Exception
			{
				return m.peek();
			}
		}
		
		//We can improve this algorithm by reducing additional space required on the second stack, by having each element of 2nd stack also
		//contain a count, so that if during push the max doesn't increase, we only increment a count.
		static class CountedInt {
			int v;
			int cnt;
			CountedInt(int v) { this.v = v; cnt = 1; }
		}
		
		public static class MaxIntStack2 extends IntStack {
			
			private java.util.Stack<CountedInt> m = new java.util.Stack<CountedInt>();
			
			public void push(int v) throws Exception
			{
				super.push(v);
				if (m.empty() || v > m.peek().v)
					m.push(new CountedInt(v));
				else
					m.peek().cnt++;
			}
			
			public int pop() throws Exception
			{
				if (--m.peek().cnt == 0)
					m.pop();
				return super.pop();
			}
			
			public int max() throws Exception
			{
				return m.peek().v;
			}
		}
		
	}
	
	public static class IntTreeNode {
		public int val;
		public IntTreeNode left, right;
		public IntTreeNode(int val) { this.val = val; }
		
		//the following 2 functions are borrowed from stackoverflow
		public void print()
		{
			print("", true);
		}

		private void print(String prefix, boolean isTail)
		{
			System.out.println(prefix + (isTail ? "|__ " : "|-- ") + val);
			
			ArrayList<IntTreeNode> children = new ArrayList<IntTreeNode>();
			if (left != null)
				children.add(left);
			if (right != null)
				children.add(right);
			
			for (int i = 0; i < children.size() - 1; i++) {
				children.get(i).print(prefix + (isTail ? "    " : "|   "), false);
			}
			if (children.size() > 0) {
				children.get(children.size() - 1).print(prefix + (isTail ?"    " : "|   "), true);
			}
		}
		
		private static IntTreeNode insertBST(IntTreeNode t, int val)
		{
			if (t == null)
				return new IntTreeNode(val);
			if (t.val == val)
				return t;
			if (val > t.val)
				t.right = insertBST(t.right, val);
			else
				t.left = insertBST(t.left, val);
			return t;
		}
		
		private static IntTreeNode makeBST(String [] a)
		{
			IntTreeNode r = null;
			for(int i = 0; i < a.length; i++)
				r = insertBST(r, Integer.valueOf(a[i]));
			return r;
		}
		
		public static IntTreeNode readBST()
		{
			System.out.println("Enter integer values:");
			String l = System.console().readLine();
			return makeBST(l.split("\\s+"));
		}
	}
	
	public static class Problem_9_10 {
		
		//First, enqueue root.  Then, repeat: dequeue node, print it and enqueue it's children.  Repeat until no nodes remain in the queue.
		//Use special sentinel node to delineate the tree into rows of the same depth.  So to modify the algorithm above, first enqueue root,
		//then sentinel.  Every time sentinel is dequeued, print '\n' and enqueue sentinel again.  The sentinel will indicate boundaries
		//of the rows.
		
		//A little improvement to this algorithm would allow us not to print any extra spaces and in addition avoid using
		//any queue functions other than enqueue and dequeue.  We do this by keeping boolean variable to denote beginning
		//of new row.
		
		public static void printTreeRLO(IntTreeNode t)
		{
			if (t == null)
				return;
			
			IntTreeNode sntnl = new IntTreeNode(Integer.MAX_VALUE), n;
			LinkedBlockingQueue<IntTreeNode> q = new LinkedBlockingQueue<IntTreeNode>();
			
			q.add(t);
			q.add(sntnl);
			
			boolean beginRow = true;
			
			while((n = q.poll()) != null) {
				if (n == sntnl) {
					System.out.println();
					if (beginRow) //the queue is empty now
						break;
					q.add(n);
					beginRow = true;
				}
				else {
					if (!beginRow)
						System.out.print(' ');
					beginRow = false;
					System.out.print(n.val);
					if (n.left != null)
						q.add(n.left);
					if (n.right != null)
						q.add(n.right);
				}
			}
		}
		
		public static void test()
		{
			IntTreeNode t = IntTreeNode.readBST();
			System.out.println();
			t.print();
			System.out.println();
			printTreeRLO(t);
		}
		
	}
	
	public static class Problem_10_1 {
		
		//We can give an alternative recursive definition of balanced binary tree:
		//1) Empty tree (0 nodes) is balanced.
		//2) Tree with 1 or more nodes is balanced iff the height difference between left and right subtrees of the root is at most 1
		//and each of the subtrees themselves is balanced. (We assume null subtree has hight 0).
		//It is easy to see that this definition is equivalent to the original definition.
		//So this lends to the recursive solution.
		
		private static final int NOT_BALANCED = -2;
		
		//We assume height of empty tree (null) is -1.
		//This function returns the height of t if t is balanced or NOT_BALANCED otherwise
		private static int getHeightIfBalanced(IntTreeNode t)
		{
			if (t == null)
				return -1;
			int hLeft = getHeightIfBalanced(t.left);
			if (hLeft == NOT_BALANCED)
				return NOT_BALANCED;
			int hRight = getHeightIfBalanced(t.right);
			if (hRight == NOT_BALANCED)
				return NOT_BALANCED;
			if (Math.abs(hLeft - hRight) > 1)
				return NOT_BALANCED;
			return Math.max(hLeft, hRight) + 1;
		}
		
		public static boolean isBalanced(IntTreeNode t)
		{
			return getHeightIfBalanced(t) != NOT_BALANCED;
		}
		
	}
	
	public static class Problem_10_10 {
		
		//Preorder: push root on the stack, then while stack is not empty, repeat: pop the node off the stack, print it,
		//then push its right, left children on the stack.
		
		//In addition, we need to prove that number of nodes on the stack at any point is O(h).
		//We prove that for tree if height h the number of nodes on the stack will always be <= h+1
		//Prove by induction on h:
		//Base case, h = 0.  We only have one node in the tree, so it is true.
		//Assume holds for tree of height h, prove for tree of height h+1.
		//The tree of height h+1 consists of tree of height h plus some leaves added to nodes at level h.
		//Every iteration of the loop will add at most 1 node to the stack (when node n has both children != null)
		//because we pop the parent node.
		//When the algorithm is at some node n at level h, it will pop n and add at most 2 children of n to the stack.
		//Those children are leaves.  They will be popped off the stack before we process any other node on the stack.
		//Thus the level h+1 will not increase size of the stack by more than 1 => Max size of the stack for tree of height
		//h+1 will be h+2.
		
		public static void preorder(IntTreeNode t)
		{
			if (t == null) {
				System.out.println();
				return;
			}
			
			java.util.Stack<IntTreeNode> s = new java.util.Stack<IntTreeNode>();
			s.push(t);
			while(!s.empty()) {
				IntTreeNode n = s.pop();
				System.out.print(n.val + " ");
				if (n.right != null)
					s.push(n.right);
				if (n.left != null)
					s.push(n.left);
			}
			
			System.out.println();
		}
		
		
		//Postorder: push root on the stack, then while the stack is not empty, repeat: look at the node at the top of the stack,
		//if it is marked, pop it and print it.  If it is not marked, mark it and then push its right, left
		//children onto the stack.
		//We have to mark the nodes that we push onto the stack so that we know that their children have already been visited.
		//We can't modify the IntTreeNode itself, since that would require additional O(n) space, so we just create a new node class for
		//use on the stack.

		//We need to prove that number of nodes on the stack at any point is O(h).
		//We prove that for a tree of height h the number of nodes on the stack will always be <= 2h + 1.
		//Base case, h = 0, 1 node in the tree => true.
		//Assume true for tree of height h, prove for tree of height h+1.
		//The tree of height h+1 consists of tree of height h plus some leaves added to nodes at level h.
		//Every iteration of the loop will add at most 2 nodes to the stack (when node n has both children != null).
		//When the algorithm is at some node n at level h, it will add at most 2 children of n to the stack.
		//Those children are leaves.  They will be popped off the stack before we process any other node on the stack.
		//Thus the level h+1 will not increase size of the stack by more than 2 => Max size of the stack for tree of height
		//h+1 will be 2h+3.
		
		private static class IntTreeStackNode {
			IntTreeNode n;
			boolean marked;
			IntTreeStackNode(IntTreeNode n) { this.n = n; }
		}
		
		public static void postorder(IntTreeNode t)
		{
			if (t == null) {
				System.out.println();
				return;
			}
			
			java.util.Stack<IntTreeStackNode> s = new java.util.Stack<IntTreeStackNode>();
			s.push(new IntTreeStackNode(t));
			while(!s.empty()) {
				IntTreeStackNode sn = s.peek();
				if (sn.marked) {
					System.out.print(sn.n.val + " ");
					s.pop();
				}
				else {
					sn.marked = true;
					if (sn.n.right != null)
						s.push(new IntTreeStackNode(sn.n.right));
					if (sn.n.left != null)
						s.push(new IntTreeStackNode(sn.n.left));
				
				}
			}
			
			System.out.println();
		}
		
		public static void test()
		{
			IntTreeNode t = IntTreeNode.readBST();
			System.out.println();
			t.print();
			System.out.println();
			System.out.print("Preorder: ");
			preorder(t);
			System.out.println();
			System.out.print("Postorder: ");
			postorder(t);
			System.out.println();			
		}
	}
	
	public static class Problem_11_4 {
		
		//Keep k stars on max-heap by distance.  For each read star, check if its distance is less than max on heap, if so remove max
		//from heap and add this start to the heap.
		
		private static class Star implements Comparable<Star> {
			String id;
			double x, y, z;
			
			public int compareTo(Star s) {
				return (int)(x*x + y*y + z*z - (s.x*s.x + s.y*s.y + s.z*s.z));
			}
		}
		
		static Star readNextStar() { //read from the file and return Star object or null if end of file
			return new Star();
		}
		
		public static Star [] findKClosest(int k)
		{
			PriorityQueue<Star> h = new PriorityQueue<Star>(); //Java implements PriorityQueue as heap
			Star s;
			for(int i = 0; i < k; i++) {
				s = readNextStar();
				if (s == null)
					return h.toArray(null);
				h.add(s);
			}
			
			while((s = readNextStar()) != null) {
				Star max = h.peek();
				if (s.compareTo(max) < 0) {
					h.poll();
					h.add(s);
				}
			}
			
			return h.toArray(null);
		}
		
		//It is easy to see that this algorithm works.  Invariant : at any iteration of the while loop above, the heap contains
		//k closest stars from the stars read so far.
		//True before the first iteration of the while loop.
		//Assume true before current iteration after reading N stars.  We know that the heap contains k closest out of N.  If star N+1
		//is farther than the farthest on heap, we don't do anything and invariant holds.  Otherwise we replace max with N+1, the invariant
		//also holds because all the stars seen so far excluding the ones in the heap will still be farther than the stars in the heap
		//(including the star we just replaced).
	}
		
	public static class Problem_11_9 {
		
		//We traverse the heap to find at most k elements > x and at most k elements >= x to find out the relationship between x and
		//Kth greatest element ek.  We compute counts g (>) and ge(>=).  If g >= k, then we know that ek > x.  If g < k bug ge >= k then
		//ek = x.  If both g < k and ge < k then ek < x.
		
		//argument i is the index of node to traverse
		//argument ge indicates to count g (ge = false) or ge (ge = true)  
		//argument cnt simulates a pointer to int
		private static void countGGE(int [] heap, int size, int k, int x, boolean ge, int i, int [] cnt)
		{
			if (heap[i] < x || (!ge && heap[i] == x))
				return;
			
			cnt[0]++;
			if (cnt[0] >= k)
				return;
			
			int left = i * 2 + 1, right = left + 1; //indexes of left and right children
			if (left < size) { //left child != null
				countGGE(heap, size, k, x, ge, left, cnt);
				if (right < size) //right child != null (if left child is null then so is the right in heap)
					countGGE(heap, size, k, x, ge, right, cnt);
			}
		}
		
		//return 1 if ek > x, 0 of ek = x, -1 if ek < x
		public static int answer(int [] heap, int size, int k, int x)
		{
			int [] cnt = new int[1];
			countGGE(heap, size, k, x, false, 0, cnt);
			if (cnt[0] >= k)
				return 1;
			cnt[0] = 0;
			countGGE(heap, size, k, x, true, 0, cnt);
			return cnt[0] >= k ? 0 : -1;
		}
		
		//It is easy to see that the running time is O(k).  Each invocation to countGGE() in answer() will result in O(k) recursive calls.
		//Every call to countGGE() will either return immediately (elem < x) or increment the count.  The count cannot go over k so
		//there are at most k calls that increment the count.  For each call that increments the count, there are at most 2 calls that
		//return immediately.  So number of calls returning immediately is also O(k).
		//Since the running time and number of calls to countGGE() is O(k), the number of concurrent stack frames cannot exceed that either,
		//so it is O(k) as well.
		
	}
	
	public static int [] readIntArray() throws Exception
	{
		String l = System.console().readLine();
		String [] elems = l.split("\\s+");
		ArrayList<String> elems1 = new ArrayList<String>();
		for(String elem : elems)
			if (elem.length() != 0)
				elems1.add(elem);
		int [] a = new int[elems1.size()];
		for(int i = 0; i < a.length; i++)
			a[i] = Integer.valueOf(elems1.get(i));
		return a;
	}
	
	public static void printIntArray(int [] a)
	{
		for(int i = 0; i < a.length; i++) {
			System.out.print(a[i]);
			if (i < a.length - 1)
				System.out.print(' ');
		}
		System.out.println();
	}
	
	public static char [] readCharArray() throws Exception
	{
		return System.console().readLine().toCharArray();
	}
	
	public static void printCharArray(char [] a)
	{
		System.out.println(new String(a));
	}
	
	public static class Problem_12_1 {
		
		//regular binary search from the book
		public static int bsearch(int [] a, int t)
		{
			int l = 0, u = a.length - 1;
			while(l <= u) {
				int m = l + (u - l)/2;
				if (a[m] < t)
					l = m + 1;
				else if (a[m] == t)
					return m;
				else
					u = m - 1;
			}
			return -1;
		}
		
		//This is modification of bsearch to find first equal element.  If middle is equal to the element, but we still have
		//more then one element to search, there is a chance there are more equal elements in the 1st half, so we narrow the search
		//to the 1st half but still keep middle in the range.  This will allow us to zero in on the 1st equal element.  Note that the
		//algorithm will converge to u = l: if u > l + 1, then l < m < u.  If u = l + 1, then m = l, and in case of a[m] = t, u becomes equal
		//to l.  Once u = l and a[m] still != t, the loop will exist on next iteration.
		public static int bsearch1(int [] a, int t)
		{
			int l = 0, u = a.length - 1;
			while(l <= u) {
				int m = l + (u - l)/2;
				if (a[m] < t)
					l = m + 1;
				else if (a[m] == t) {
					if (u == l)
						return m;
					u = m;
				}
				else
					u = m - 1;
			}
			return -1;
		}
		
		//The algorithm in bsearch1() is a little different than the one from the book solution, but still works with same running time.
		
		public static void test() throws Exception
		{
			int [] a = readIntArray();
			System.out.println(bsearch1(a, 10));
		}
		
	}
	
	public static class Problem_12_10 {
		
		//Instead of considering 1 element at a time, we will consider 2 at time.  First compare them, then use smaller one to update the min
		//and use large one to update the max.  In this case we are using 3 comparisons per 2 elements.
		
		//return 2 element array, [0] - min, [1] - max
		public static Comparable [] findMinMax(Comparable [] a)
		{
			if (a == null || a.length == 0)
				return null;
			if (a.length == 1)
				return new Comparable [] { a[0], a[0] };
			
			Comparable [] r = new Comparable[2];
			//take care of the first 2 elements with 1 comparison
			if (a[0].compareTo(a[1]) < 0) {
				r[0] = a[0];
				r[1] = a[1];
			}
			else {
				r[0] = a[1];
				r[1] = a[0];
			}
			
			for(int i = 2; i < a.length; i += 2) {
				Comparable s, l;
				if (i == a.length - 1) { //only one element left (a.length is odd)
					//max 2 comparisons, but could be only 1
					if (a[i].compareTo(r[0]) < 0)
						r[0] = a[i];
					else if (a[i].compareTo(r[1]) > 0)
						r[1] = a[i];
					break;
				}
				//2 elements - 3 comparisons
				if (a[i].compareTo(a[i+1]) >= 0) {
					l = a[i];
					s = a[i+1];
				}
				else {
					s = a[i];
					l = a[i+1];
				}
				if (s.compareTo(r[0]) < 0)
					r[0] = s;
				if (l.compareTo(r[1]) > 0)
					r[1] = l;
			}
			
			return r;
		}
		
		//Number of comparisons.
		//Let n = a.length, N be number of comparisons.
		//If n is even, N = 1 + 3 * (n-2)/2 = 1 + 3(n/2 - 1) = 3*n/2 - 3 + 1 = 3*n/2 - 2.
		//If n is odd, N = 1 + 3 * (n-3)/2 + 2 = 3 + (3*n - 9)/2 = 3 + (3*n+1-10)/2 = 3 + (3*n+1)/2 - 5 = ceil(3*n/2) - 2.
		
	}
	
	public static class IntPair {
		public int x, y;
		public IntPair() {}
		public IntPair(int x, int y) {
			this.x = x;
			this.y = y;
		}
		public int hashCode() { return x * 31 + y; }
		public boolean equals(Object o) { return o instanceof IntPair && ((IntPair)o).x == x && ((IntPair)o).y == y; }
		public String toString() { return "<"+x+","+y+">"; }
	}
	
	public static class Problem_13_7 {
		
		//Go point by point.  Keep a set of all lines encountered so far, as well as the line with most points.
		//See if the current point belongs to any of the lines in the set.  If so, update the point count of this line and possibly update pointer
		//to the line with most points.  If the current point does not belong to any existing line, add new lines to the set, between this point and
		//all points encountered previously. Worst case time = O(n^3) in case if no 3 points are on a line.
		//Can we improve it?
		//Yes there is a better solution.  There are total max of n*(n-1) lines.  Some of them are the equivalent, meaning they belong
		//to the same line.  We keep a hashtable that for each line keeps its count (that is count of lines identical to this one).
		//Then we choose the line with max count.  Line has equation y = a*x+b.  We can identify a line by its coefficients a,b.
		//However, a,b will be floating point numbers which would make the hash key imprecise.  How can we identify line by integer
		//coefficients?
		//We need to use some sort of Rational number class which stores numerator/denominator in minimal form.
		
		public static class Rational {
			
			private int gcd(int a, int b) {
				if (b == 0)
					return a;
				return gcd(b, a % b);
			}
			
			public int num, den;
			public Rational(int num, int den) {
				int g = gcd(num, den);
				this.num = num / g;
				this.den = den / g;
			}
		}
		
		public static class Point extends IntPair {}
		
		public static class Line {
			Rational slope, off;
			public Line(Point p1, Point p2) { //line that contains these 2 points
				if (p1.x == p2.x) { //vertical line
					//in this case off represent horizontal offset and we keep slope = null to represent infinity
					off = new Rational(p1.x, 1);
				}
				else {
					slope = new Rational(p2.y - p1.y, p2.x - p1.x);
					//in this case off is vertical offset
					off = new Rational(p1.y * p2.x - p2.y * p1.x, p2.x - p1.x);
				}
			}
			public int hashCode() { return (slope != null ? slope.hashCode() * 31 : 0) + off.hashCode(); }
		}
		
		public static Line findLineWithMostPoints(Point [] a)
		{
			HashMap<Line,Integer> h = new HashMap<Line,Integer>();
			for(int i = 0; i < a.length; i++)
				for(int j = i + 1; j < a.length; j++) {
					Line l = new Line(a[i], a[j]);
					if (!h.containsKey(l))
						h.put(l, new Integer(1));
					else
						h.put(l, new Integer(h.get(l).intValue() + 1));
				}
			
			Line maxL = null;
			int maxCnt = 0;
			Set<Line> ks = h.keySet();
			for(Line l : ks) {
				int cnt = h.get(l).intValue();
				if (cnt > maxCnt) {
					maxL = l;
					maxCnt = cnt;
				}
			}
			
			return maxL;
		}
		
	}
	
	public static class Problem_13_11 {
		
		//Keep hastable mapping element to its index in the array.  Advance until encountering duplicate (by checking the hash).
		//Once duplicate is encountered, record the range (compare with max length range recorded so far), then remove all values
		//in the hash up to and including the index of the duplicate in the hash, then repeat whole process starting with that
		//index + 1.
		
		//r.x - index of first element in subarray, r.y - index after last element; like String.substring()
		public static IntPair maxSubArrayNoDup(int [] a)
		{
			IntPair r = new IntPair();
			int maxLen = 0, start = 0;
			HashMap<Integer,Integer> h = new HashMap<Integer,Integer>();
			for(int i = 0; i < a.length; i++) {
				Integer e = Integer.valueOf(a[i]);
				if (h.containsKey(e)) {
					if (i - start > maxLen) {
						r.x = start;
						r.y = i;
						maxLen = i - start;
					}
					int j = h.get(e) + 1;
					if (a.length - j <= maxLen) //optimization
						return r;
					for(int k = start; k < j; k++)
						h.remove(Integer.valueOf(a[k]));
					start = j;
				}
				h.put(e, Integer.valueOf(i));
			}
			if (a.length - start > maxLen) { //case of the max range at the end of the array
				r.x = start;
				r.y = a.length;
			}
			return r;
		}
		
		public static void test() throws Exception
		{
			int [] a = readIntArray();
			IntPair r = maxSubArrayNoDup(a);
			System.out.println(r.x + " "  + r.y);
		}
		
	}
	
	/*
	public static class Problem_14_2 {
		
		//We use the extra space in array a to move greater elements when need to insert elements from b.
		//We know there is enough extra space because max of b.length elements would need to be moved.  We advance through a 1 element at a time
		//and do 3 way comparison - element of a, element of b and element of second portion of a with moved elements.
		//Actually, no 3-way comparison is not needed: if there is an element in the 2nd portion not yet processed, we know this element
		//would be <= the current element in the 1st portion.  In this case we just move either this element or current element of b
		//(if remain) to the 1st portion.
		
		//assume that there are a.length - b.length elements originally in a
		public static void merge(int [] a, int [] b) throws Exception
		{
			if (a == null || b == null || a.length < b.length)
				throw new Exception("Invalid input");
			
			//i iterates through a, j iterates through b, k iterates through second part of a, m designates end of moved elements range
			int aLen = a.length - b.length, i = 0, j = 0, k = aLen, m = aLen;
			for(i = 0; i < a.length; i++) {
				if (i == k)
					k = m;
				if (j < b.length && b[j] < a[k == m ? i : k]) {
					int tmp = a[i];
					a[i] = b[j++];
					if (tmp != Integer.MAX_VALUE)
						a[m++] = tmp;
				}
				else if (k < m) {
					int tmp = a[i];
					a[i] = a[k];
					a[m++] = tmp;
					a[k++] = Integer.MAX_VALUE;
					}
				}
			}
		}
		
		public static void test() throws Exception
		{
			System.out.println("Enter a:");
			int [] a1 = readIntArray();
			System.out.println("Enter b:");
			int [] b = readIntArray();
			int [] a = new int[a1.length + b.length];
			System.arraycopy(a1, 0, a, 0, a1.length);
			System.arraycopy(b, 0, a, a1.length, b.length);
			
			merge(a, b);
			System.out.println("Result:");
			printIntArray(a);
		}
		
	}
	*/
	
	public static class Problem_14_9 {
		
		//First we sort the list of tasks, to get tasks t[0] <= t[1] <= ... <= t[2*m-1]   (n = 2*m).
		//Then we pair t[0] and t[2*m-1], t[1] and t[2*m-2], ..., t[m-1], t[m].  We will prove that this is the optimum assignment.
		//Running time is O(n*log(n)).
		
		public IntPair [] assignTasks(int [] a) throws Exception
		{
			if (a == null || (a.length & 1) != 0)
				throw new Exception("Invalid input");
			Arrays.sort(a);
			int m = a.length >> 1;
			IntPair [] res = new IntPair[m];
			for(int i = 0; i < m; i++)
				res[i] = new IntPair(a[i], a[a.length-1-i]);
			return res;
		}
		
		//Proof: let's say we have 2*m tasks in sorted order: t[0] <= t[1] <= ... <= t[m-1] <= t[m] <= ... <= t[2*m-1]
		//Let's keep adding them in the set in the following manner: add t[m-1],t[m], then add t[m-2],t[m+1], ..., then add t[0],t[2*m-1].
		//So that at each step for i = 1,2,...,m we have the following sorted set S(i):
		//	t[m-i],t[m-i+1],...,t[m-1],t[m],t[m+1],...,t[m+i-1]
		//We will prove that for each i, the optimum assignment of the tasks in S(i) would be
		//  (t[m-i],t[m+i-1]),(t[m-i+1],t[m+i-2),...(t[m-1],t[m]).
		//By induction:
		//1) i = 1, S(i) = t[m-1],t[m]  -  trivially true.
		//2) Assume it is true for S(i), prove for S(i+1):
		//S(i+1) = S(i),t[m-i-1],t[m+i] and we know that t[m-i-1] <= any task in S(i) <= t[m+i].  We shall prove that the optimum
		//assignment for S(i+1) would be (t[m-i-1],t[m+i]),(optimum assignment for S(i)).
		//Let k be such that t[m-i+k]+t[m+i-1-k] is max in S(i) - that is (t[m-i+k],t[m+i-1-k]) is the longest pair of tasks in S(i)
		//under assumed optimum assignment.
		//We have 2 cases to consider:
		//1) t[m-i-1]+t[m+i] >= t[m-i+k]+t[m+i-1-k], that is the new added pair of (t[m-i-1],t[m+i]) will become the longest pair and
		//increase the maximum time.
		//Then assigning of t[m-i-1] and t[m+i] must be optimal because otherwise we would have to assign t[m+i] to one of the tasks
		//in S(i) instead which would make the max time even greater because any task in S(i) >= t[m-i-1].
		//2) t[m-i-1]+t[m+i] < t[m-i+k]+t[m+i-1-k], so the new pair will not increase the maximum time.  So now the question is whether
		//by adding the new pair we can decrease the maximum time if we don't pair t[m-i-1] with t[m+i].
		//We can see that this is impossible:
		//Say we pair t[m-i-1] with some other task t[j] in S(i).  That means that our optimum assignment would be
		//(t[m-i-1],t[s]),(optimum assignment in the set S2 = S(i)-t[j]+t[m+i]).  Set S2 is the set constructed by substituting of t[j]
		//with t[m+i] in S(i).  Since t[j] <= t[m+i] (and otherwise S2 and S(i) are the same) the optimum assignment in S2 cannot be
		//better than the optimum assignment in S(i).  Thus by not pairing t[m-i-1] with t[m+i] we could not improve the maximum time
		//in S(i+1).
		//Thus the assignment (t[m-i-1],t[m+i]),(optimum assignment for S(i)) is optimum assignment for S(i+1).
		
	}
	
	public static class Problem_14_10 {
		
		public static class Person {
			public int key;
			public String name;
			public Person(int key, String name) {
				this.key = key;
				this.name = name;
			}
		}
		
		//1) Compute the counts for each key by keeping them in the hash table.
		//2) Then go through all keys and calculate the beginning destination offsets for each key (also keep the offsets in the hash table).
		//3) Then go through the array and swap each element to the destination offset and increment the offset in the hash table.
		//If we want the array to become sorted, then in step 2) we first sort the keys.
		
		//Modification to steps 2 and 3: we need to know when to skip over the elements that were already swapped to their correct destination.
		//Each key will have a destination range.  For each element we encounter, it can be either within its key's destination range or
		//before its key's destination range.  It can't be after it's destination range since all elements processed so far are already
		//in their correct places.  To find out, in step 2) we compute the beginning of each range and in step 3) we can use that to
		//tell if the element is within its key's destination range and if so we can move forward.
		
		
		public static void rearrange(Person [] a, boolean toSort)
		{
			HashMap<Integer,IntPair> h = new HashMap<Integer,IntPair>();  //pair.x is count, pair.y is offset
			
			//1)
			for(Person p : a) {
				Integer k = Integer.valueOf(p.key);
				IntPair en = h.get(k);
				if (en == null) {
					en = new IntPair();
					h.put(k, en);
				}
				en.x++;
			}
			
			//2) set pair.x and pair.y to the beginning of range, will use pair.y as running index to where to swap in step 3)
			Object [] keys = h.keySet().toArray();
			if (toSort)
				Arrays.sort(keys);
			
			int off = 0;
			for(Object k : keys) {
				IntPair en = h.get(k);
				int cnt = en.x;
				en.x = en.y = off;
				off += cnt;
			}
			
			//3)
			int i = 0;
			while(i < a.length) {
				Integer k = Integer.valueOf(a[i].key);
				IntPair en = h.get(k);
				if (i < en.x) {
					Person p = a[i];
					a[i] = a[en.y];
					a[en.y++] = p;
				}
				else {
					i++;
					//Actually at this point we don't need to worry about en.y because once i is within the destination range for key k
					//we would not need to move the elements with key k anymore.
					//if (en.y < i)
					//	en.y = i;
				}
			}
			
			//This algorithm uses additional space O(keys.length) (the hash table).  Now we need to show that its running time is O(a.length).
			//Each element will be swapped at most twice: once to current index i and once to its correct destination, because after
			//the element at its correct destination, it will not be swapped again => total number of swaps is O(a.length).
			//At each iteration of the loop we either swap or increment index i => total number of iterations is O(a.length).
		}
		
		public static void test() throws Exception
		{
			int [] a = readIntArray();
			Person [] p = new Person[a.length];
			for(int i = 0; i < p.length; i++)
				p[i] = new Person(a[i], ""+i);
			rearrange(p, true);
			for(int i = 0; i < p.length; i++)
				System.out.print(p[i].key + " ");
			System.out.println();
		}
		
	}
	
	public static class Problem_15_1 {

		//We test that each subtree contains keys in the appropriate range
		
		//tests whether t is BST and any node n of t has  low <= n.val <= high
		private static boolean isBSTInRange(IntTreeNode t, int low, int high)
		{
			if (t == null) //empty tree
				return true;
			if (t.val > high || t.val < low)
				return false;
			return isBSTInRange(t.left, low, t.val) && isBSTInRange(t.right, t.val, high);
		}
	
		public static boolean isBST(IntTreeNode t)
		{
			return isBSTInRange(t, Integer.MIN_VALUE, Integer.MAX_VALUE);
		}
		
	}
	
	public static class Problem_15_3 {
		
		//In general the idea is that we do in order traversal until we found the first key > k.  However, we can short circuit it since
		//when k >= n.key we don't need to go into the left sub-tree. On the other hand, when n.key > k we do go into the left sub-tree and
		//if we don't find the larger element there we return n itself without going into the right sub-tree, since in this case n is the
		//first node larger than k.  Since at invocation of find1 we do at most one recursive call, the running time is O(h).
		//By the condition of the problem, we need to return null if k is not found in the tree.  We can look for k in parallel and record
		//whether it is found in a state variable (foundK).  Alternatively we can look for k in a separate call first.
		
		private static IntTreeNode find1(IntTreeNode n, int k, boolean [] foundK)
		{
			if (n == null)
				return null;
			if (n.val > k) {
				IntTreeNode r = find1(n.left, k, foundK);
				if (r != null)
					return r;
				return n;
			}
			if (n.val == k)
				foundK[0] = true;
			return find1(n.right, k, foundK);
		}
		
		public static IntTreeNode findFirstLarger(IntTreeNode t, int k)
		{
			boolean [] foundK = new boolean[1];
			IntTreeNode r = find1(t, k, foundK);
			return foundK[0] ? r : null;
		}
		
		private static IntTreeNode findK(IntTreeNode n, int k)
		{
			if (n == null)
				return null;
			if (n.val == k)
				return n;
			return findK(k < n.val ? n.left : n.right, k);
		}
		
		private static IntTreeNode find2(IntTreeNode n, int k)
		{
			if (n == null)
				return null;
			if (n.val > k) {
				IntTreeNode r = find2(n.left, k);
				if (r != null)
					return r;
				return n;
			}
			return find2(n.right, k);			
		}
		
		public static IntTreeNode findFirstLarger2(IntTreeNode t, int k)
		{
			if (findK(t, k) == null)
				return null;
			return find2(t, k);
		}
		
		//So it looks like the solution in the book is pretty much the same except they do it iteratively instead of recursively.  This is
		//understandable since my solution is almost tail-recursive.  Still though, I consider mine as correct solution to this problem :)
		
	}
	
	public static void printBits(long v, int len)
	{
		for(int i = 0; i < len; i++)
			System.out.print((v&(1<<i)) != 0 ? "1" : "0");
	}
	
	public static class Problem_16_11 {
		
		//Compute Gray code for n-1, then duplicate in reverse order adding 1 bit for this second half.  This solution can be implemented
		//recursively or iteratively, we will do iterative version.  We can use integers in which case n <= 31 which is ok for this problem.
		
		public static int [] grayCode(int n)
		{
			if (n > 31)
				throw new RuntimeException("n out of range");
			int [] a = new int[1<<n];
			for(int k = 1; k < a.length; k<<=1)
				for(int j = 0; j < k; j++)
					a[k+j] = a[k-1-j]|k;
			return a;
		}
		
		public static void test(int n)
		{
			int [] gc = grayCode(n);
			for(int i = 0; i < gc.length; i++) {
				printBits(gc[i], n);
				System.out.println(' ');
			}
			System.out.println();
		}
		
	}
	
	public static class Problem_16_2 {
		
		//Extended match can be converted to strict match by prepending and/or appending .*
		
		private static boolean strictMatch(String s, String r)
		{
			//System.out.println("s=" + s + "; r=" + r);
			if (r.length() == 0)
				return s.length() == 0;
			if (Character.isLetterOrDigit(r.charAt(0))) {
				if (r.length() == 1)
					return s.length() == 1 && s.charAt(0) == r.charAt(0);
				if (r.charAt(1) == '*')
					return strictMatch(s, r.substring(2)) || //0 occurrences of r.charAt(0)
						(s.length() != 0 && s.charAt(0) == r.charAt(0) && strictMatch(s.substring(1), r)); //1 or more occurrences of r.charAt(0)
				else
					return s.length() != 0 && s.charAt(0) == r.charAt(0) && strictMatch(s.substring(1), r.substring(1));
			}
			else if (r.charAt(0) == '.') {
				if (r.length() == 1)
					return s.length() == 1;
				if (r.charAt(1) == '*')
					return strictMatch(s, r.substring(2)) || //0 occurrences of any char
						(s.length() != 0 && strictMatch(s.substring(1), r)); //1 or more occurrences of any char
				else
					return s.length() != 0 && strictMatch(s.substring(1), r.substring(1));
			}
			else
				throw new RuntimeException("Invalid regular expression");
		}
		
		public static boolean match(String s, String r)
		{
			if (r.length() == 0)
				return s.length() == 0;
			if (r.charAt(0) == '^')
				r = r.substring(1);
			else
				r = ".*" + r;
			if (r.charAt(r.length() - 1) == '$')
				r = r.substring(0, r.length() - 1);
			else
				r += ".*";
			return strictMatch(s, r);
		}
		
		public static void test(String [] args)
		{
			System.out.println("s=" + args[0] + "; r=" + args[1]);
			boolean r = match(args[0], args[1]);
			System.out.println(r ? "Match." : "No match.");
		}
		
	}
	
	public static class Problem_16_13 {
		
		//At first i doubted that my solution is most effective, but looks like the book answer has the same solution.
		//We provide a function that does a merge sort and also returns the number of inversions in original array.  In recursive
		//step we divide the array in half, get halves sorted and get number of inversions for each.  Then we merge and calculate number
		//of inversions between the halves which is easy to do when they are sorted.
		
		//aux array for temp merge result
		private static int countInvAndMerge(int [] a, int off, int len, int [] aux)
		{
			if (len == 1)
				return 0;
			int m = len / 2;
			int l = countInvAndMerge(a, off, m, aux),
				r = countInvAndMerge(a, off + m, len - m, aux);
			
			//merge
			int c = l + r, i = off, j = off + m, k = 0;
			while(i < off + m || j < off + len) {
				//if j is out of range then i has to be in range because of the while() condition
				//if j is in range but i is out of range we go to the "else" part
				if (j >= off + len || (i < off + m && a[i] <= a[j])) {
					aux[k++] = a[i++];
					c += j - off - m; //current element on the left is inverted with all elements on the right that have been already processed
				}
				else
					aux[k++] = a[j++];
			}
			
			//copy back to the original array
			for(i = 0; i < len; i++)
				a[off + i] = aux[i];
			
			return c;
		}
		
		public static int countInversions(int [] a)
		{
			if (a == null || a.length < 2)
				return 0;
			
			int [] aux = new int[a.length];
			return countInvAndMerge(a, 0, a.length, aux);
		}
		
		public static void test() throws Exception
		{
			int [] a = readIntArray();
			System.out.println(countInversions(a));
		}
		
	}
	
	public static class Problem_17_1 {

		//To calculate number of combinations, we observe the following:
		//Assume we calculated number of combinations for scores s = 0 .. N with plays w[0], w[1], ..., w[i] for some i < w.length.
		//Then we can calculate scores for plays w[0], ..., w[i], w[i+1] by observing that num combinations for score s
		//C(s, {w[0],...,w[i+1]}) = C(s, {w[0],...,w[i]}) + C(s - w[i+1], {w[0],...,w[i+1])
		//That is when we add new element w[i+1], the number of combinations will include all combinations with only w[0],...,w[i],
		//which we already calculated, and combinations that include at least one of w[i+1] in them which equals the total number of
        //combinations for score s-w[i+1].  From example in the book, C(12, {7,3,2}) = C(12, {7,3}) + C(10, {7,3,2}).
		//To calculate numbers of combinations for {w[0],...,w[i+1]} we can reuse the array of stored numbers of combinations for
		//{w[0],...,w[i]}.  Since when calculating c[s], c[s-w[i+1]] is already known, only one addition will be required at this step.
		//Thus time complexity is O(s * w.length) and space complexity is O(s) to store the array of num combinations for each score.
		
		//Calculating number of distinct sequences is much easier.  We know that the sequence can start with any of w[0],...,w[n-1].
		//The number of sequences starting with w[i] is total number of sequences for score s-w[i]. So,
		//NSeq[s] = NSeq[s-w[0]] + NSeq[s-w[1]] + ... + NSeq[s-w[n-1]].  This can be implemented either sequentially or recursively
		//with memoization.
		//The equation above requires w.length additions, so the time complexity is O(s * w.length) and space complexity is O(s).
		
		public static int numberOfCombinations(int s, int [] w)
		{
			int [] c = new int[s+1];
			c[0] = 1;
			for(int i = 0; i < w.length; i++)
				for(int j = w[i]; j < c.length; j++)
					c[j] += c[j-w[i]];
			return c[s];
		}
		
		public static int numberOfSequences(int s, int [] w)
		{
			int [] q = new int[s+1];
			q[0] = 1;
			for(int i = 1; i < q.length; i++)
				for(int j = 0; j < w.length; j++)
					if (i >= w[j])
						q[i] += q[i-w[j]];
			return q[s];
		}
		
		public static void test() throws Exception
		{
			int [] a = readIntArray();
			int s = a[0];
			int [] w = Arrays.copyOfRange(a, 1, a.length);
			int r = numberOfCombinations(s, w);
			System.out.println("C=" + r);
			r = numberOfSequences(s, w);
			System.out.println("Q=" + r);
		}
	
	}
	
	public static class Problem_17_2 {
		
		//Let A = a[1]a[2]...a[n], B = b[1]b[2]...b[m] and we compute distance between A and B
		//Let A(i) = a[1]a[2]...a[i] (i <= n) and B(j) = b[1]b[2]...b[j] (i <=m)
		//Let D(i, j) be minimal distance between A(i) and B(j).
		//1) If a[i] == b[j] then D(i, j) = D(i-1,j-1)
		//2) If a[i] != b[j] then D(i, j) = min(D(i,j-1)+1, D(i-1,j)+1, D(i-1,j-1)+1)

		//Proof of 1): consider optimal sequence of edits from A(i) to B(j).  We can create another sequence with the same or smaller
		//number of edits by working only on A(i-1) and setting a[i] aside. There are 2 cases: either a[i] will become b[j],
		//or some other character (that is equal to a[i]) will become b[j], possibly by insertion after a[i].  In the 1st case,
		//setting a[i] aside just converts A(i-1) to B(j-1).  In the 2nd case, at least 1 edit will be required to create b[j].  We can
		//create sequence with same number of edits by working on A(i-1) and borrowing extra edit to insert a[i] by not doing the edit
		//to create b[j] (since we already set a[i] aside for that).  This in effect would convert A(i-1) to B(j-1).
		
		//Proof of 2): consider optimal sequence of edits from A(i) to B(j).  There are 3 possibilities: either a[i] was not changed,
		//or a[i] was deleted, or a[i] was substituted.  If a[i] was not changed then some characters must have been appended after a[i],
		//so we can use recurrence D(i,j)=D(i,j-1)+1 since B(j-1) has to be created before B(j) and only 1 edit is needed to create b[j]
		//(we can swap the edit to create b[j] to the end, so in effect we have conversion of A(i) -> B(j-1) + 1 edit to create b[j]).
		//If a[i] was deleted, then D(i,j) = D(i-1,j)+1, we add 1 to account for the deletion (we can swap the deletion of a[i] to the end,
		//so in effect we have conversion of A(i-1) -> B(j) and then deletion of a[i]).  If a[i] was substituted,
		//then D(i,j) = D(i-1,j-1) + 1.  There are 2 cases: either a[i] becomes b[j] (by substitution), or some other character becomes b[j]
		//by insertion of some characters after a[i].  In the first case, we can swap the substitution to the end so in effect we have
		//conversion of A(i-1) -> B(j-1) and then a[i]->b[j].  In the 2nd case, we know 1 edit is needed to create b[j] and we can swap it
		//to the end.  So in effect we do A(i)->B(j-1) and then add b[j].  However since we know that in sequence A(i)->B(j-1) character a[i]
		//gets substituted we can replace this sequence with A(i-1)->B(j-1) where substitution of a[i] is replaced by insertion of a[i]
		//(and this new sequence has the same number of edits).  So in effect we have A(i-1)->B(j-1) and then adding b[j].
		//We don't know which of the 3 recurrences can be used to create the equivalent optimal sequence.  However, all 3 allow
		//us to construct valid sequence of edits from A(i) to B(i) and we know that one of them applies, so we can compute all 3 and choose
		//the minimum.
		
		//Note that in all cases above the sub-sequences of edits on the right side of the recurrences (A(i-1)->B(i-1), A(i)->B(j-1),
		//A(i-1)->B(j)) must have minimum number of edits.  Suppose that one of them is not.  Then we can substitute it with the minimal
		//sub-sequence.  The resulting sequence will still be valid conversion of A(i) to B(j) and will have fewer edits than
		//the sequence we constructed above, which has the same number of edit as the optimal sequence we started with.  Thus the optimal
		//sequence we started with cannot be optimal, thus a contradiction.
		
		//We solve this problem using dynamic programming by filling the 2D array D bottom up.  D(n, m) will be the answer.
		
		public static int getEditDistance(char [] a, char [] b)
		{
			int [][] d = new int[a.length+1][b.length+1]; //index 0 is for empty string
			for(int i = 0; i < d.length; i++)
				d[i][0] = i; //going from string of length i to empty string - i deletes
			for(int j = 0; j < d[0].length; j++)
				d[0][j] = j; //going from empty string to string of length j - j inserts
			
			for(int i = 1; i < d.length; i++)
				for(int j = 1; j < d[0].length; j++) {
					if (a[i-1] == b[j-1])
						d[i][j] = d[i-1][j-1];
					else
						d[i][j] = Math.min(Math.min(d[i][j-1],d[i-1][j]),d[i-1][j-1]) + 1;
				}
			return d[a.length][b.length];
		}
		
		public static void test(String [] args)
		{
			System.out.println("a="+args[0]+";b="+args[1]+";");
			int d = getEditDistance(args[0].toCharArray(), args[1].toCharArray());
			System.out.println(d);
		}
	}
	
	public static class Problem_17_11 {
		
		public static class SubSum {
			public int i, j, s; //subarray [i,j), sum s
		}
		
		//Say we have array A = a[0]a[1]a[n-1].  A max subarray for circular array A would be maximum of 1) max subarray of regular
		//array A, [i,j) i <=j and 2) max subarray which crosses the boundary, [i, j) where j < i.
		//1) is straightforward, see book. For 2), first, we could calculate for each i = 1 .. n-1, the maximum subarray of form [j, n)
		//where j >=i and store these values (for each i, both the max sum S'(i) and index j).  This is easy to do in O(n) time by iterating
		//backwards for i = n-1,n-2,...,1.  Then we iterate forward i=1,...,n-1, compute S(i) (sum of elements from 0 to i-1, see book),
		//and add that to S'(i) already calculated and stored, while keeping track of max S(i)+S'(i) and corresponding indexes.
		//This algorithm would require additional O(n) storage.
		//However we can compute 2) in O(1) time:
		//Subarray [j, i) where j > i consists of 2 ranges [j, n) and [0,i). Between them there is a hole [i,j).  Subarray sum of [j,i) =
		//Subarray sum of [0,n) (sum of whole array) minus subarray sum of the hole [i,j).  Subarray sum of [j,i) will be maximum when
		//the subarray sum of the whole [i, j) is minimum.  So to compute 2) we compute sum of the whole array and the minimum subarray
		//sum of form [i, j) and substruct the latter from the former.  Min subarray sum can be computed in the way symmetrical to max
		//subarray sum algorithm in the book.  Note that the min subarray sum is <= 0, with 0 being for empty subarray.
		
		//algorithm from the book
		private static SubSum maxSubSum(int [] a)
		{
			SubSum s = new SubSum();
			int sum = 0, minSum = 0;
			for(int i = 0; i < a.length; i++) {
				sum += a[i];
				if (sum < minSum) {
					minSum = sum;
					s.i = i+1;
				}
				if (sum - minSum > s.s) {
					s.s = sum - minSum;
					s.j = i+1;
				}
			}
			return s;
		}
		
		//symmetrical algorithm for min subarray sum
		private static SubSum minSubSum(int [] a)
		{
			SubSum s = new SubSum();
			int sum = 0, maxSum = 0;
			for(int i = 0; i < a.length; i++) {
				sum += a[i];
				if (sum > maxSum) {
					maxSum = sum;
					s.i = i+1;
				}
				if (sum - maxSum < s.s) {
					s.s = sum - maxSum;
					s.j = i+1;
				}
			}
			return s;
		}
		
		//Actually we can combine so we can compute everything in one loop
		public static SubSum circSubSum(int [] a)
		{
			SubSum minS = new SubSum(), maxS = new SubSum();
			int sum = 0, minSum = 0, maxSum = 0;
			for(int i = 0; i < a.length; i++) {
				sum += a[i];
				if (sum < minSum) {
					minSum = sum;
					maxS.i = i+1;
				}
				if (sum > maxSum) {
					maxSum = sum;
					minS.i = i+1;
				}
				if (sum - minSum > maxS.s) {
					maxS.s = sum - minSum;
					maxS.j = i+1;
				}
				if (sum - maxSum < minS.s) {
					minS.s = sum - maxSum;
					minS.j = i+1;
				}
			}
			minS.s = sum - minS.s;
			if (maxS.s >= minS.s)
				return maxS;
			
			int j = minS.i;
			minS.i = minS.j;
			minS.j = j;
			return minS;
		}
		
		public static void test() throws Exception
		{
			int [] a = readIntArray();
			SubSum s = circSubSum(a);
			System.out.println("["+s.i+", "+s.j+") Sum = " + s.s);
		}
	
	}
	
	public static class Problem_18_2 {
		
		//Total waiting time will be minimal if queries are sorted in ascending order of their service times.
		//Proof: consider queries with times t[0], ..., t[i], t[i+1], t[i+2], ..., t[n-1].  Suppose that t[i] > t[i+1].
		//If we swap t[i] and t[i+1] we will only improve the total waiting time: the waiting times for queries t[0], ..., t[i]
		//will remain the same (since it only depends on previous queries up to t[i-1].  The waiting times for queries
		//t[i+2], ..., t[n-1] will also remain the same since they include both t[i] and t[i+1].  But the waiting time for query t[i+1]
		//will decrease because t[i] has decreased.
		//By contradiction, if we assume there is optimal order which is not sorted ascending, we can find i such that t[i] > t[i+1].
		//Swapping them will decrease total waiting time, which contradicts that the assumed order was optimal.
		
		public static int getTotalWaitTime(int [] t)
		{
			Arrays.sort(t);
			int s = 0, wt = 0;
			for(int i = 0; i < t.length; i++) {
				wt += s;
				s += t[i];
			}
			return wt;
		}
		
	}
	
	public static class Problem_18_3 {
		
		//My solution is a little different from the one in the book, but the same idea.
		//We iterate right, once we see block height become shorter we can start record the water level until the block height becomes
		//same or taller then the one from which we started recording.  At this point we stop recording and wait for the next drop.
		//If we hit the end and the end block height is shorter than the last one we started recording from, that means that that last one
		//was the maximum and we cannot solve this problem going forward since the idea above would only work if the last block's height
		//is same or taller than the first.  In this case we solve this part of the problem by going backwards from the last block to
		//the recorded maximum by doing the same thing.  The difference from the solution in the book is that in the book they find maximum
		//first and then solve (at most) 2 subproblems, one going forward from the beginning to maximum and another going backward from end
		//to maximum.
		//Each subproblem can be simplified: we keep track of the max seen so far. If the current level is < max, we add
		//max - current level to the capacity.  Otherwise we increase the max and add what we collected so far.
		
		public static int getCapacity(int [] a)
		{
			if (a.length < 3)
				return 0;
			
			int c = 0, r = 0, j = 0;
			//here the j is the index of max level, we need to remember it if we need to iterate backwards
			for(int i = 1; i < a.length; i++) {
				if (a[i] >= a[j]) {
					j = i;
					c += r;
					r = 0;
				}
				else
					r += a[j] - a[i];
			}
			
			
			//here we don't need to keep track of max index or accumulate into separate variable r
			//since we know we won't overrun the max at j; we use r to keep track of max level
			r = 0;
			
			//If the last level is equal to the max, then we don't need to iterate backwards: in this case j = a.length - 1
			//and we skip the loop below
			for(int i = a.length - 1; i > j; i--) {
				if (a[i] >= r)
					r = a[i];
				else
					c += r - a[i];
			}
			
			return c;
		}

		public static void test() throws Exception
		{
			int [] a = readIntArray();
			int c = getCapacity(a);
			System.out.println(c);
		}
	}
	
	public static class Problem_18_10 {
		
		//We use the same approach decsribed in the book before problem 18.6 (Invariants).  We use pair of indices to designate highest
		//and lowest elements seen so far.  But either of them can be negative.  We start to look for lowest at from the end of the array
		//as the most abs negative number and look for the highest from the end of the array as the most abs positive number.
		
		//We could optimize by considering cases when K is either positive or negative, but here we will just present the generic algorithm.
		//The problem didn't specify whether both indexes in the sum can be the same, so we assume that they could.
		
		public static IntPair pairSumAbsSorted(int [] a, int k)
		{
			if (a == null || a.length == 0)
				return null;
			if (a.length == 1)
				return k == 2 * a[0] ? new IntPair(0, 0) : null;
			
			int h, l;
			
			//Initialize h and l.
			//We can determine one of h or l immediately by looking at the last element.
			if (a[a.length - 1] >= 0) {
				h = a.length - 1;
				for(l = a.length - 2; l > 0 && a[l] >= 0; l--);
				//if l hits 0, it can stay there regardless whether a[0] is positive or negative,
				//since there are no more negative values to consider.  Analogous for h below.
			}
			else {
				l = a.length - 1;
				for(h = a.length - 2; h > 0 && a[h] < 0; h--);
			}
			
			//Main loop.
			//h will go left positive then right negative, l will go left negative then right positive
			//h and l will converge
			//the invariant here is a[h] >= a[l]
			//If we imagine virtual array which is "a" but normally sorted (negatives with decreasing abs values followed
			//by positives with increasing abs values, then initially l will be in the beginning and h at the end and
			//then on each iteration of the loop either l will be incremented by 1 or h decremented by 1 (but not both).
			//That's why l and h are bound to converge.  This algorithm just simulates this virtual array with given
			//abs sorted array "a".
			while(h != l) {
				//System.out.println("l="+l+" h="+h);				
				int s = a[h] + a[l];
				if (s == k)
					return h > l ? new IntPair(l, h) : new IntPair(h, l);
				if (s > k) { //we need to decrease h, whatever that means at the moment
					if (h > 0 && a[h] >= 0)
						for(h--; h > 0 && a[h] < 0; h--);
					else
						for(h++; a[h] >= 0; h++);
					//System.out.println("s>k: h="+h);
				}
				else { //we need to increase l, whatever that means at the moment
					if (l > 0 && a[l] < 0)
						for(l--; l > 0 && a[l] >= 0; l--);
					else
						for(l++; a[l] < 0; l++);
					//We won't overrun the bounds in loop above because l will always be <= h in the virtual sense.
					//In loop above l is iterating forward so a[h] >= 0. Once l == h, the loop will exit because the condition
					//"a[l] < 0" is no longer true.  Same logic applies for similar loop in case of s > k.
					//System.out.println("s<k: l="+l);
				}
			}
			
			return k == 2 * a[h] ? new IntPair(h, h) : null;
		}
		
		public static void test() throws Exception
		{
			int [] a = readIntArray(), k = readIntArray();
			IntPair r = pairSumAbsSorted(a, k[0]);
			System.out.println();
			if (r == null)
				System.out.println("None");
			else
				System.out.println(r.x + " " + r.y);
		}
		
	}
	
	public static class Problem_20_2 {
		
		private static int n, c;
		
		private static class IncrementThread implements Runnable {
			public void run() {
				for(int i = 0; i < n; i++)
					c++;
			}
		}
		
		public static void test(String [] args) throws Exception
		{
			n = Integer.parseInt(args[0]);
			c = 0;
			Thread t1 = new Thread(new IncrementThread()), t2 = new Thread(new IncrementThread());
			t1.start();
			t2.start();
			t1.join();
			t2.join();
			System.out.println(c);
		}
		
		//Max(c) = 2*n.  This happens when one thread executes only after another, or if all increments perfectly interleave between
		//the 2 threads (t1, t2, t1, t2, ...) or if run of one thread is followed by run of another sequentially.
		//c cannot be > 2*n.  There are 2*n increments total and each increment operation will
		//write back value that is at most (current value) + 1 (it would be less if one thread overwrites the result of another thread's
		//increment operation).
		
		//Min(c) = 2 when n >= 2.  This happens in the following worst case scenario (we denote threads 1 and 2 reads as r and writes as w):
		//1r0 2r0 2w1 2r1 2w2 2r2 2w3 ... 2r(n-2) 2w(n-1) 1w1 2r1 1r1 1w2 1r2 1w3 ... 1r(n-1) 1w(n) 2w2
		//Note that in the line above each thread does n reads and n writes.
		//c < 2 is impossible if n >= 2: each thread, on the 2nd and all subsequent reads will read at least 1 since nobody writes 0.
		//This means that on 2nd and all subsequent writes each thread will write at least 2.  This will be true for the last write.
		
		//If n = 1, then obviously min(c) = 1.
		
	}
	
	public static class Problem_20_3 {
		
		public static class Counter {
		
			private int n, c = 1;
			
			public Counter(int n)
			{
				this.n = Math.max(1, n);
			}
			
			public synchronized void incrementAndPrint(boolean odd)
			{
				int lim = ((n&1) != 0) == odd ? n : n - 1;
				while(c <= lim) {
					if (((c&1)==0) == odd)
						try { wait(); } catch(InterruptedException e) { throw new IllegalStateException(e); }
					System.out.print((odd ? "o" : "e") + c + " ");
					c++;
					notify();
				}
			}
		}
		
		public static void runThreads(int n) throws Exception
		{
			final Counter c = new Counter(n);
			Thread tEven = new Thread(new Runnable() { public void run() { c.incrementAndPrint(false); } }),
				tOdd = new Thread(new Runnable() { public void run() { c.incrementAndPrint(true); } });
			tEven.start();
			tOdd.start();
			tEven.join();
			tOdd.join();
			System.out.println();
		}
		
		public static void test(String [] args) throws Exception
		{
			runThreads(args.length == 0 ? 100 : Integer.parseInt(args[0]));
		}
		
	}
	
	public static class Problem_5_4 {
		
		//To find the closest integer of the same weight we go from the least significant bit (LSB) of x, find the first change 10 or 01
		//and flip it.
		//The resulting integer x1 will obviously be the same weight and it will be closest to x.
		//We prove the last part:
		//1) If LSBs of x are 01 or 10, then swapping them will result in |x1-x| = 1 so x1 is obviously the closest.
		//2) Suppose that LSB of x is 0 and so x = ......100...0, ending with k 0s.  Then by swapping 10 to 01 we get |x1-x| = 2^(k-1).
		//We can show that this is the smallest difference we can get in this case.  Let's number bits from LSB to MSB starting from 0.
		//To get difference of 2^(k-1) we swapped (k-1)-th 0 with k-th 1.  To get smaller difference we would have to flip one or more
		//0 bits at indexes 0..k-2 (if we only work on bits >= k-1, we will always get difference >= 2^(k-1)).  Flipping these bits will
		//create small increase I < 2^(k-1). To bring x1 to the same weight, we would need to flip at least the same number of 1 bits
		//at indexes >= k.  But changing any bits at indexes >=k would result in increments/decrements of at least 2^k.
		//Consider 2 cases when 0 bit at k-1 gets flipped or when it doesn't:
		//	a) If it doesn't then any changes done at indexes >=k will result in change C >= 2^k, so the smallest difference
		//we would get C - I > 2^(k-1).
		//	b) On the other hand, if bit at k-1 becomes 1, then more than one 1 bit will have to be flipped at indexes >=k
		//(since we assumed some 0 bits are flipped at indexes < k-1).  In this case we consider 2 cases when 1 bit at index k gets
		//flipped or not:
		//		b1) If 1 bit at index k remains the same, then 2 or more bits would have to be flipped at indexes >=k+1.  This would generate
		//change C1 >= 2^(k+1).  The increase generated by flipping 0 bits at indexes 0..k-1 is I1 < 2^k.  So the total change
		//C1 - I1 > 2^k.
		//		b2) If 1 bit at index k becomes 0, then regardless of changes in 0 bits at indexes 0..k-1, the total decrease from
		//changed bits at indexes 0..k D <= 2^k (in fact, D < 2^(k-1) since by our assumption the 0 bit at k-1 became 1 and at least one
		//other 0 bit at indexes 0..k-2 became 1 also).  But by assumption in b) at least one more bit will have to be switched
		//at index >=k+1, which would generate a change C >= 2^(k+1).  Thus the total change C - D > 2^k.
		//This concludes the proof for 2).
		//3) Suppose that LSB of x is 1 and so x = ......011...1, ending with k 1's.  This case is symmetrical to 2), we just change
		//increase to decrease and vice versa in our argument.  The optimal solution will still have |x1-1| = 2^(k-1).
		
		//Here is somewhat more simple proof of 2):
		//Consider 1 bit at index k and 2 cases when it does or doesn't change during transformation:
		//a) If bit at index k doesn't change, then increase generated by flipping bits of indexes 0..k-1 I < 2^k.  But then at least one
		//bit will have to be flipped at indexes >=k+1 which would generate change C >= 2^(k+1).  So total change is C - I > 2^k.
		//b) If bit at index k becomes 0, then the decrease D of changing bits 0..k < 2^k. Now consider how many 0 bits get flipped to 1
		//at indexes 0..k-1.  If more than one, then at least one bit will have to be flipped at indexes >=k+1 which would generate
		//change C >=2^(k+1), so the total change C - D > 2^k.  If only one 0 bits get flipped then we consider which one.  Flipping 0 bit
		//at index k-1 will give us smallest difference = 2^(k-1) when we are considering only bits 0..k and that's the solution.  Changing
		//any bits at indexes >=k+1 will generate change C > 2^(k+1) and thus will not help us (because the biggest difference from
		//flipping one 0 bit at index 0..k-1 together with flipping 1 bit at k would still give us total decrease D < 2^k).
		
		public static long closestSameWeight(long n)
		{
			long mask = n & ~(n-1); //find first 1 bit
			if (mask == 1) //if LSB is 1, then find first 0 bit
				mask = ~n & (n+1);
			mask |= (mask>>1); //mask for LSBs 10 or 01
			return (n & ~mask) | (~n & mask);
		}
		
		public static void test(String [] args) throws Exception
		{
			System.out.println(closestSameWeight(Integer.parseInt(args[0])));
		}
		
	}
	
	public static class Problem_6_13 {
		
		//We can think of a permutation as a graph with one or more disjoint cycles. E.g. if n = 5 and P = 3 4 1 0 2, then the cycles are
		// 0 3 0 ... and 1 4 2 1 ...
		//We can follow each cycle to permute the elements in this cycle while storing only one element (the one that was just replaced).
		//The first cycle starts at index 0.  Once we process all its elements, we need to know if index 1 is part of the cycle
		//we already processed or it is part of the new cycle.  In general case, if we are at index i, we need to know if this element is
		//part of new cycle or part of cycle already processed.  We can find this out by following the cycle and seeing if it leads to
		//any index < i.  If so, it is part of one of the previous cycles, so we move on to index i+1.  Otherwise, we process cycle that
		//starts at index i.  Total running time is O(n^2) because the step that checks whether the cycle is new is O(n).
		
		public static void permute(int [] a, int [] p)
		{
			for(int i = 0; i < a.length - 1; i++) {
				if (i > 0) { //check if i is part of new or previously processed cycle
					int j = p[i];
					for(; j > i; j = p[j]);
					if (j < i)
						continue;
				}
				//process the cycle beginning at i
				int curr = i, val = a[i];
				do {
					int next = p[curr], tmp = a[next];
					a[next] = val;
					val = tmp;
					curr = next;
				} while(curr != i);
			}
		}
		
		public static void test() throws Exception
		{
			int [] a = readIntArray(), p = readIntArray();
			permute(a, p);
			printIntArray(a);
		}
	}
	
	public static class Problem_6_26 {
		
		//We know that for every person, either he is a celebrity or he knows the celebrity.
		//We proceed from at the first row (person) and look at who he knows increasing index sequentially.
		//If he doesn't know anyone (we reached the end of the row), then he is the celebrity.  On the other hand,
		//say he knows person 5.  Now we go to the row 5 and start looking at who person 5 knows starting at index 5+1=6.
		//If person 5 does not know anyone with index 6+ then he is the celebrity: if he wasn't then he would know the celebrity and the celebrity
		//cannot be at index < 5 because otherwise we would already find the celebrity when looking at 1st person.
		//On the other hand, if person 5 knows someone say at index 7, then we go to 7th row and proceed in similar manner, and so forth.
		//Basically when we are at row i and column i+1, then either i is the celebrity (if we reach the end without finding anyone he knows),
		//or person i will know someone with index >= i+1.  Celebrity cannot be at index < i+1 because otherwise he would be already discovered
		//during previous lookup.  The running time is O(n), since we never decrease the column index.
		
		public static int findCelebrity(byte [][] p)
		{
			int row = 0;
			//Invariant: col >= row
			for(int col = 0; col < p.length; row=col++) {
				for(; col < p.length && (col == row || p[row][col] == 0); col++);
				if (col == p.length)
					return row;
			}
			return row;
		}
		
		public static void test()
		{
			byte [][] p = {
				{0,0,1,0,0,0,1},
				{1,0,0,0,1,0,1},
				{0,1,0,1,1,0,1},
				{1,0,1,0,1,1,1},
				{0,0,0,0,0,0,1},
				{1,1,1,1,1,0,1},
				{0,0,0,0,0,0,0}
			};
			System.out.println(findCelebrity(p));
		}
		
	}
	
	public static class Problem_8_15 {
		
		//The way I see it: find the middle, reverse 2nd half (or 1st half, doesn't matter), then compare the halves.  Not sure if there is any
		//more effective solution
		
		public static boolean isPalindromic(IntList l)
		{
			//n2 moves at twice the pace as n1, when n2 is null, n1 is at the middle
			IntNode n1 = l.head, n2 = n1;
			while(n2 != null && n2.next != null) {
				n1 = n1.next;
				n2 = n2.next.next;
			}
			if (n1 != null)
				System.out.println("The middle is " + n1.data);
			
			//reverse the list pointed to by n1, reversed list will be assigned to n2
			n2 = null;
			while(n1 != null) {
				IntNode next = n1.next;
				n1.next = n2;
				n2 = n1;
				n1 = next;
			}
			System.out.print("2nd half reversed: ");
			printIntList(n2);
			
			n1 = l.head;
			//compare lists pointed to by n1 and n2
			while(n2 != null) { //the list at n1 was not cut off at the middle, so we use list at n2 to check for end
				if (n1.data != n2.data)
					return false;
				n1 = n1.next;
				n2 = n2.next;
			}
			return true;
		}
		
		public static void test() throws Exception
		{
			IntList l = readIntList();
			printIntList(l);
			boolean r = isPalindromic(l);
			System.out.println("The list is " + (r ? "" : " NOT ") + "palindromic");
		}
		
	}
	
	public static class Problem_8_14 {
		
		//First we copy the list normally (with the jump fields unmodified).  During this process,
		//for each node of the original list, we can point its jump field to the copy of that node in the new list.
		//On the 2nd pass, we can use this to update the jump fields in the new list to point to nodes in the new list.
		//At the same time we will restore the jump pointers in the original list (since we had their values in the new list
		//after the 1st pass.
		
		//Oops.  The above is not gonna work because on the 2nd pass once we restore the original nodes jump pointer, we will
		//not be able to use it to update copy jump pointers that point backwards in the list.
		
		//So new solution:  We create a copy of the list merged into the original list like this:
		//Node1->Node1Copy->Node2->Node2Copy->...->NodeN->NodeNCopy
		//For copy nodes, we will initially have their jump pointers same as the original.  Now we can update the jump pointers by following
		//jump->next since each original node will be followed by its copy.  This will make correct values for the jump fields in the copy
		//nodes.  Then we can separate out the original and the copy lists from the merged list.
		
		public static class PostNode {
			int data;
			PostNode next, jump;
		}
		
		public static PostNode copyPostingsList(PostNode l)
		{			
			PostNode n = l;
			
			//make merged list
			while(n != null) {
				PostNode c = new PostNode();
				c.data = n.data;
				c.jump = n.jump;
				PostNode next = n.next;
				n.next = c;
				c.next = next;
				n = next;
			}
			
			//update jump pointers
			n = l;
			while(n != null) {
				n = n.next;
				n.jump = n.jump.next;
				n = n.next;
			}
			
			//separate out the lists
			PostNode head = null, tail = null; //for the copy list
			n = l;
			while(n != null) {
				if (head == null) {
					head = n.next;
					tail = head;
				}
				else {
					tail.next = n.next;
					tail = n.next;
				}
				n.next = n.next.next;
				n = n.next;
			}
			
			return head;
		}
		
		//input array will represent alternating data and jump fields (where jump field is represented as node index)
		private static PostNode readPostingsList() throws Exception
		{
			int [] a = readIntArray();
			PostNode [] pa = new PostNode[a.length/2];
			for(int i = 0; i < pa.length; i++) {
				pa[i] = new PostNode();
				pa[i].data = a[i*2];
			}
			for(int i = 0; i < pa.length; i++) {
				if (i < pa.length - 1)
					pa[i].next = pa[i+1];
				pa[i].jump = pa[a[i*2+1] - 1]; //1-based index
			}
			return pa.length > 0 ? pa[0] : null;
		}
		
		//print each node as <data,next.data,jump.data>
		private static void printPostingsList(PostNode n)
		{
			if (n == null) {
				System.out.println("<null>");
				return;
			}
			while(n != null) {
				System.out.print("<" + n.data + "," + (n.next == null ? "null" : n.next.data) + "," +
					(n.jump == null ? "null" : n.jump.data) + "> ");
				n = n.next;
			}
			System.out.println();
		}
		
		public static void test() throws Exception
		{
			PostNode l = readPostingsList();
			printPostingsList(l);
			PostNode c = copyPostingsList(l);
			System.out.print("New postings list: ");
			printPostingsList(c);
			System.out.print("Original postings list: ");
			printPostingsList(l);
		}
		
	}
	
	public static class Problem_9_3 {
		//Push each opening PBB on the stack.  For each closing PBB pop the stack and see if it matches the opening PBB.  If doesn't match,
		//or stack gets empty before input is processed, return false.  If anything left on the stack at the end of processing, return false.
		//Otherwise return true.
		
		//Pretty simple, however we need to prove this works.  We need to prove both directons: string is PBB-matched => algorithm returns true
		//and algorithm returns true => string is PBB-matched.
		
		//1) string is PBB-matched => algorithm returns true
		//Note that if string is PBB-matched, it has to have even length and number of each type of opening and closing PBB have to match.
		//Prove by induction on number of characters in input string.  Base case, len=0, obvious.
		//Induction: suppose it is true for all strings of length <= N.  Prove for N+2.  The string must be of the form (s1), [s1], {s1} or
		//s1s2, where s1 and s2 are PBB-matched strings for which algorithm works as expected by inductive hypothesis.
		//It is easy to see that in each case above the algorithm will return true (e.g. push '(', then process s1, by induction the algorithm
		//will not reject and the stack will only contain '(' after s1 is read, then we read ')', match to '(' and return true).
		
		//2) algorithm returns true => string is PBB-matched
		//Also prove by induction on lengh of the input string.  Base case len=0, obvious (empty string is PBB matched).
		//Induction: assume true for all strings of length <=N, prove for N+1 (note that the algorithm will reject any string of odd length or
		//where number of opening and closing PBB doesn't match, but we don't have to assume this for this proof).
		//The string must start with an opening PBB: '(', '[' or '{', otherwise algorithm would reject.  Let us run the algorithm until the
		//stack becomes empty again, this would be at the end or somewhere in the middle of the input string.  To illustrate, say
		//our input string s = c[0] c[1] c[2] ... c[m] c[m+1] ... c[N] (since len=N+1) and stack becomes empty again upon reading c[m].
		//We know that c[0] must be opening PBB and since the algorithm doesn't reject, c[m] must be closing PBB matching c[0].  So if
		//c[1]...c[m-1] is PBB matched, so would be c[0]...c[m].  Because the algorithm didn't reject, if we run the algorithm on string
		//c[1]...c[m-1], it would accept.  This means that c[1]...c[m-1] is PBB matched by inductive hypothesis and thus is c[0]...c[m].
		//If m < N (i.e. c[m+1]...c[N] is not empty) since the stack is empty before reading c[m+1] and the algorithm didn't reject, it
		//means by inductive hypothesis that c[m+1]...c[N] must be PBB matched since it's length N-m+1 <= N and the algorithm accepts.
		//Since both c[0]...c[m] and c[m+1]c[N] are PBB-matched so must be s=c[0]...c[N].
		
		public static boolean isPBBMatched(char [] s)
		{
			Stack<Character> stk = new Stack<Character>();
			for(char c : s) {
				if (c == '(' || c == '[' || c == '{')
					stk.push(Character.valueOf(c));
				else {
					if (stk.empty())
						return false;
					char cc = stk.pop().charValue();
					if ((cc == '(' && c != ')') || (cc == '[' && c != ']') || (cc == '{' && c != '}'))
						return false;
				}
			}
			return stk.empty();
		}
		
		public static void test(String [] args)
		{
			System.out.println(isPBBMatched(args[0].toCharArray()));
		}
	}
	
	public static class Problem_9_4 {
		
		//We can solve this problem analogous to problem 9.3 by pushing opening parenthesis on the stack.
		//We can find the longest substring by reading the input and keeping track of the longest matched string that ends at current
		//character.  The length of such substring will be (number of characters read) - (number of opening parenthesis on the stack).
		//The reason is because the opening parenthetis on the stack are parenthesis that are not yet matched, which means that if we
		//had processed the input from the index = (depth of the stack) we would find a matched string when the stack becomes empty.
		//If we encounter ')' when the stack is empty, the we just ignore it since the current matched length is already 0 and this ')'
		//will not be able to match any further input.  In the following function we will also take care of situation when we read
		//character other than '(' or ')'.  Since this character cannot be part of matched string, we empty the stack at that point
		//(since anything on the stack so far cannot match anything that follows since then we would have invalid character as part of
		//the matched string).
		//Actually, do we really need a stack here?  Since we would only push '(' on the stack, we only need to keep track of how many
		//we would push, so a simple counter will suffice.
		//One correction: if we encounter ')' when the stack is empty or we encounter invalid character, we know that nothing read so far
		//can be matched to any of the following.  That means that when we measure the current matched string, we need to start from the
		//next index rather than from the beginning, so we keep track of that index.

		//Ooooops! Fail: (()(()
		//We need to revise this algorithm.  Too bad i glanced at the solution and it gave me the idea of storing index of opening
		//parenthesis on the stack.  The reality is that the starting position of the longest matched string read so far is not indicated
		//by the depth of the stack, but instead it is a position following the index of the opening parenthesis at the top of the stack.
		
		//The range [0 to cutOff] represents indexes we no longer consider because of either invalid character or
		//unmatched closing parenthesis.
		
		public static String longestMatched(char [] s)
		{
			Stack<Integer> stk = new Stack<Integer>();
			int maxStart = 0, maxLen = 0, cutOff = -1;
			
			for(int i = 0; i < s.length; i++) {
				char c = s[i];
				if (c == '(')
					stk.push(Integer.valueOf(i));
				else if (c == ')' && !stk.empty() && stk.pop().intValue() > cutOff) {
					int l = stk.empty() ? cutOff : Math.max(cutOff, stk.peek().intValue());
					if (i - l > maxLen) {
						maxLen = i - l;
						maxStart = l + 1;
					}
				}
				else
					cutOff = i;
			}
			return new String(s, maxStart, maxLen);
		}
		
		public static void test(String [] args)
		{
			System.out.println(longestMatched(args[0].toCharArray()));
		}
		
	}
	
	public static class Problem_9_9 {
		
		//It is not possible to sort the stack without allocating any additional memory.  The problem mentions not to allocate any memory
		//explicitly.  I m gonna assume we can allocate memory implicitly on the function stack with recursion.
		
		//find and pop max element, this function assumes the stack is not empty
		private static Integer popMax(Stack<Integer> stk)
		{
			Integer v = stk.pop();
			if (stk.empty())
				return v;
			Integer m = popMax(stk);
			if (v.intValue() >= m.intValue()) {
				stk.push(m);
				return v;
			}
			else {
				stk.push(v);
				return m;
			}
		}
		
		public static void sortStack(Stack<Integer> stk)
		{
			if (stk.empty())
				return;
			Integer m = popMax(stk);
			sortStack(stk);
			stk.push(m);
		}
		
		public static void test() throws Exception
		{
			int [] a = readIntArray();
			Stack<Integer> stk = new Stack<Integer>();
			for(int v : a)
				stk.push(Integer.valueOf(v));
			sortStack(stk);
			while(!stk.empty())
				System.out.print(stk.pop() + " ");
			System.out.println();
		}
		
		//What is the worst case running time?  It seems to be O(n^2) in case when the stack is initially sorted in reverse order
		//(max element at the bottom), since popMax() will take O(n) for each element in this case.
		//What is the space used in the worst case (implicitly on the call stack)?  It is O(n).  popMax() uses O(n) space,
		//sortStack() itself is also using O(n) space. Because popMax() will bottom out before sortStack() is called recursively,
		//the total space is still O(n).
		
	}
	
	public static class Problem_17_21 {
		//We could reduce this problem to a well known problem of longest common subsequence.  Just create a sorted copy of
		//the input array and do LCS of the original array with the sorted copy.  LCS is covered in the Algorithms book.
		//However, we can also solve it in more simple manner.  Basically we keep track of the longest non-decreasing subsequence
		//ending at each index of the array.  We iterate over indexes sequentially.  For each new element a[i] the length of LNDS will be
		//length of maximum LNDS encountered so far that ends with element <= a[i] plus 1 (since we can add a[i] to that sequence
		//to get LNDS longer by 1).  We still have a task to reconstruct the actual sequence for the solution.  In this case, in
		//addition to storing length of each LNDS ending with index i for 0<=i<n, we also store the previous index of that LNDS
		//(so we will use 2 arrays instead of 1).  Then the final sequence can be easily reconstructed backwards.
		//The running time of this algorithm will be O(n^2) because for each i we need to spend O(i) to find LNDS to append to.
		//Let's write this algorithm and then we can think if we can improve the running time.
		
		//returns LNDS
		public static int [] findLNDS(int [] a)
		{
			if (a == null || a.length == 0)
				return new int[0];
			if (a.length == 1)
				return new int[] { a[0] };
			
			//prev = -1 if only one element in the subsequence
			int [] len = new int[a.length], prev = new int[a.length];
			len[0] = 1;
			prev[0] = -1;
			
			int lndsLen = 0, lndsLast = -1; //at the same time we look for LNDS over the whole array
			for(int i = 1; i < a.length; i++) {
				int maxLen = 0, maxIdx = -1;
				for(int j = 0; j < i; j++)
					if (a[j] <= a[i] && len[j] > maxLen) {
						maxLen = len[j];
						maxIdx = j;
					}
				len[i] = maxLen + 1;
				prev[i] = maxIdx;
				if (len[i] > lndsLen) {
					lndsLen = len[i];
					lndsLast = i;
				}
			}
			
			//now we reconstruct the LNDS
			int [] lnds = new int[lndsLen];
			for(int i = lndsLen - 1; i >= 0; i--) {
				lnds[i] = a[lndsLast];
				lndsLast = prev[lndsLast]; //lndsLast should not become -1 here since we already know the length of the LNDS (lndsLen).
			}
			
			return lnds;
		}
		
		public static void test() throws Exception
		{
			int [] a = readIntArray(), lnds = findLNDS(a);
			printIntArray(lnds);
		}
	}
	
	public static class Problem_9_12 {
		//We use one stack for enqueing and another stack for dequeing.  When the dequing stack is empty and we need to dequeue, we roll
		//the elements over from the first stack.
		
		//Note that the amortized time for each operation is O(1).  Consider sequence of m operations.  Each element that we use will be
		//enqueued once and dequeued once, which will involve one push and one pop.  In addition, each element may be transfered from 1st
		//stack to the 2nd, which will also involve 1 push and 1 pop.  Thus operating on each element takes constant amount of time.
		//Since m operations can use at most m elements, the time for m operations is O(m).
		
		public static class Queue<T> {
			Stack<T> es = new Stack<T>(), ds = new Stack<T>();
			
			public void enqueue(T v)
			{
				es.push(v);
			}
			
			public T dequeue()
			{
				if (ds.empty())
					while(!es.empty())
						ds.push(es.pop());
				try {
					return ds.pop();
				}
				catch(java.util.EmptyStackException e) {
					throw new java.util.NoSuchElementException();
				}
			}
			
			public boolean empty() { return es.empty() && ds.empty(); }
		}
	}
	
	public static class Problem_10_3 {
		//Binary tree is symmetric if left subtree is mirror image of the right subtree.  We can compute isMirrorImage recursively.
		
		public static boolean isMirrorImage(IntTreeNode n1, IntTreeNode n2)
		{
			if (n1 == null || n2 == null)
				return n1 == n2;
			return isMirrorImage(n1.left, n2.right) && isMirrorImage(n1.right, n2.left);
		}
		
		public static boolean isSymmetric(IntTreeNode n)
		{
			return n == null || isMirrorImage(n.left, n.right);
		}
		
	}
	
	public static class Problem_10_17 {
		
		//It seems that using one boolean field per node would suffice.  This field tells if any node in the subtree of this node
		//(including itself) is locked.
		
		public static class BinaryTree {
			public Object data;
			public BinaryTree left, right, parent;
			public boolean locked;
			
			//to run the test program from the website
			public BinaryTree getLeft() { return left; }
			public BinaryTree getRight() { return right; }
			public BinaryTree getParent() { return parent; }
			public void setLeft(BinaryTree left) { this.left = left; }
			public void setRight(BinaryTree right) { this.right = right; }
			public void setParent(BinaryTree parent) { this.parent = parent; }
			
			public boolean isLocked()
			{
				return locked && (left == null || !left.locked) && (right == null || !right.locked);
			}
			
			public boolean lock()
			{
				if (locked) //if any node in this subtree is locked
					return false;
				for(BinaryTree n = parent; n != null; n = n.parent) //all ancestors
					if (n.isLocked())
						return false;
					
				//put lock on this and all ancestor subtrees
				for(BinaryTree n = this; n != null; n = n.parent)
					n.locked = true;
				return true;
			}
			
			public void unlock()
			{
				if (!isLocked())
					return;
				locked = false;
				for(BinaryTree n = this; n.parent != null; n = n.parent)
					if ((n.parent.left == this && !n.parent.right.locked) || (n.parent.right == this && !n.parent.left.locked))
						n.parent.locked = false;
			}
		}
		
		//test program from the website
		public static void test()
		{
			BinaryTree root = new BinaryTree();
			root.setLeft(new BinaryTree());
			root.getLeft().setParent(root);
			root.setRight(new BinaryTree());
			root.getRight().setParent(root);
			root.getLeft().setLeft(new BinaryTree());
			root.getLeft().getLeft().setParent(root.getLeft());
			root.getLeft().setRight(new BinaryTree());
			root.getLeft().getRight().setParent(root.getLeft());

			assert(!root.isLocked());
			System.out.println(root.isLocked());

			assert(root.lock());
			assert(root.isLocked());
			System.out.println(root.isLocked());
			assert(!root.getLeft().lock());
			assert(!root.getLeft().isLocked());
			assert(!root.getRight().lock());
			assert(!root.getRight().isLocked());
			assert(!root.getLeft().getLeft().lock());
			assert(!root.getLeft().getLeft().isLocked());
			assert(!root.getLeft().getRight().lock());
			assert(!root.getLeft().getRight().isLocked());

			root.unlock();
			assert(root.getLeft().lock());
			assert(!root.lock());
			assert(!root.getLeft().getLeft().lock());
			assert(!root.isLocked());

			System.out.println(root.isLocked());
			assert(root.getRight().lock());
			assert(root.getRight().isLocked());
			System.out.println(root.isLocked());
		}	
	}
	
	public static class Problem_11_5 {

		//We keep the half of data of lower numbers in max-heap and 2nd half of higher numbers in a min-heap.
		//If number of elements is even, then both medians are at heads of both heaps.  If number of elements is odd, we will keep the median
		//at the head of the max heap.  When new element is added, we add it (depending on the value) to the min heap or max heap.  Then if the
		//rules above are violated (number of elements in max-heap (+1) != number elements in min-heap), we move the head of one heap
		//to the other heap to equalize the number of elements.
		
		public static void printMedians(int [] a)
		{
			PriorityQueue<Integer> maxHeap = new PriorityQueue<Integer>(10, Collections.<Integer>reverseOrder());
			PriorityQueue<Integer> minHeap = new PriorityQueue<Integer>();
			for(int i = 0; i < a.length; i++) {
				if (maxHeap.size() == 0 || a[i] <= maxHeap.peek().intValue())
					maxHeap.add(Integer.valueOf(a[i]));
				else
					minHeap.add(Integer.valueOf(a[i]));
				int odd = 1-(i&1); //odd number of elements when i is even, since we just added an element
				//equalize the heaps
				if (maxHeap.size() < minHeap.size() + odd)
					maxHeap.add(minHeap.poll());
				else if (maxHeap.size() > minHeap.size() + odd)
					minHeap.add(maxHeap.poll());
				
				int med = (odd == 1) ? maxHeap.peek() : (maxHeap.peek() + minHeap.peek())/2;
				System.out.print(med + " ");
			}
			System.out.println();
		}
		
		public static void test() throws Exception
		{
			int [] a = readIntArray();
			printMedians(a);
		}
	}
	
	public static class Problem_11_6 {
		//We can find k largest elements by keeping a set of candidates among which we choose largest one for each of the k times.
		//Root would be the largest element.  The next largest is one of 2 root's children (the larger child).  The next largest is
		//one of 3 elements: the smaller child and 2 children of the larger child.  And so forth.  So we keep a set of largest candidates.
		//Initially we place root in that set.  Then, at each step, we select the largest in the set, remove it and place its 2 children
		//into the set.  Then repeat, total of k times.  We improve this algorithm by keeping this set partially sorted, so we can get largest
		//in constant time.  In fact, we can use max-heap for this purpose.
		
		//The running time should be O(k*log(k)) (loop iterates k times and log(k) for removing largest in max-heap).
		
		public static void printKLargest(final int [] h, int k) //we assume the size of the heap is h.length
		{
			if (h == null || h.length == 0 || k <= 0)
				return;
			if (k > h.length)
				k = h.length;
			
			//Our heap keeps indexes of elements in h but compares elements themselves
			PriorityQueue<Integer> maxHeap = new PriorityQueue<Integer>(10,
				new Comparator<Integer>() {
					//reverse order of comparison because PriorityQueue is min-heap by default
					public int compare(Integer o1, Integer o2) { return Integer.compare(h[o2.intValue()], h[o1.intValue()]); }
				});
			maxHeap.add(Integer.valueOf(0));
			for(int i = 0; i < k; i++) {
				int n = maxHeap.poll().intValue();
				System.out.print(h[n] + " ");
				int cIdx = 2*n + 1;
				if (cIdx < h.length)
					maxHeap.add(cIdx);
				if (++cIdx < h.length)
					maxHeap.add(cIdx);
			}
			System.out.println();
		}
		
		public static void test(String [] args) throws Exception
		{
			int k = Integer.parseInt(args[0]);
			int [] a = readIntArray();
			printKLargest(a, k);
		}
		
	}
	
	public static class Problem_16_4 {
		//we partition the array into 2 parts, the values that are fixed and the values to be permutted.
		//Let n be number of elements that are fixed and L be length of the array.  Then at this point we need
		//to print all possible permutations of elements a[n]...a[L-1] prefixed by elements a[0]...a[n-1] (which are fixed at this point).
		//We do this by making elements a[n],a[n+1],...,a[L-1] fixed one by one and recursing into printPermutations with value n = n+1
		//Note that for this to work and to maintain lexicographical ordering, when we enter printPermutations, the elements
		//a[n],...,a[L-1] have to be sorted.  This has to be maintained every time we enter printPermutaions.  This means that
		//at the end of printPermutations we have to restore the elements a[n]...a[L-1] to their original order.  The main solution in the book
		//restores back after each call to printPermutations, but this will not print the permutations in lexicographical order.
		//Instead we make it so when we pick one of the a[i], n<=i<L, the elements following a[n] will still be kept in order.  We do this
		//by swapping a[n] and a[i] each time with increasing i and calling printPermutations recursively.  We don't swap back after
		//the recursive call.  E.g.  say a[n],...,[L-1] are 1,2,3,4,5.  Then we get:
		//2,1,3,4,5  3,1,2,4,5  4,1,2,3,5	5,1,2,3,4
		//so that elements a[n+1]...a[L-1] are always in order before the recursive call.
		//After we are done, we need to restore 5,1,2,3,4 to 1,2,3,4,5.  This can be done by saving element a[n], shifting elements
		//a[n+1]...a[L-1] back by distance 1 and then restoring a[L-1] to be a[n].
		//Because each call to printPermutations does that (except the case when n == L), we are assured that after each recursive call
		//to printPermutations, the elements a[n+1],...a[L-1] will in order.
		
		//Spaced used: only the stack of O(L) since we never allocated additional memory explicitly.
		//Running time: obviously Omega(L!) but what's the upper bound? We can prove that it is O(L!) by substitution method:
		//The recurrence here is T(n+1) = T(n)*(n+1) + O(n).  O(n) represents overhead in the function from the for loops, function calls
		//and constant overhead as well.
		//Thus T(n+1) <= T(n)*(n+1) + K*n where K is some constant.
		//As our inductive hypothesis let us choose T(n) <= C1*n! - C2.  We will show that for sufficently large n we can choose constants
		//C1 and C2 such that the recurrence holds:
		//T(n+1) <= (C1*n! - C2)*(n+1) + K*n = C1*(n+1)! - C2 - C2*n + K*n = C1*(n+1)! - C2 - n*(C2-K) <= C1*(n+1)! - C2
		//as long as C2>=K.
		//Or, more exactly, we choose T(n) <= C*n!-K, then T(n+1)<= (C*n!-K)*(n+1) + K*n = C*(n+1)! - K - K*n + K*n =
		//C*(n+1)! - K
		
		//n is number of elements that are fixed
		private static void printPermutations(int [] a, int n)
		{
			if (n == a.length) { //print the permutation
				System.out.print('<');
				for(int i = 0; i < n; i++) {
					System.out.print(a[i]);
					if (i < n-1)
						System.out.print(',');
				}
				System.out.println('>');
			}
			else { //swap each consequtive element into a[n] and recurse
				int x, i;
				for(i = n; i < a.length; i++) {
					//swap a[n] and a[i]
					x = a[n];
					a[n] = a[i];
					a[i] = x;
					printPermutations(a, n + 1);
				}
				//now we need to restore elements n ... a.length-1 to original order
				x = a[n];
				for(i = n; i < a.length - 1; i++)
					a[i] = a[i+1];
				a[a.length-1] = x;
			}
		}
		
		public static void printPermutations(int [] a)
		{
			Arrays.sort(a);
			printPermutations(a, 0);
		}
		
		public static void test() throws Exception
		{
			int [] a = readIntArray();
			printPermutations(a);
		}
	}
	
	public static class Problem_6_21 {
		//There are 9 rows, 9 columns and 9 3x3 sub-arrays.  Thus for each digit, we keep 27 bit-flags to indicate whether this digit
		//is present in given row, column or 3x3 sub-array.  This can be kept as bit-flags in array of 9 integers.  We read the board one
		//by one and update the bit-flags array. Encountering 1-bit in given row, column or 3x3 sub-array indicates duplicate.
		
		public static boolean sudokuCheck(int [][] b)
		{
			assert(b != null && b.length == 9);
			assert(b[0] != null && b[0].length == 9);
			
			int [] f = new int[9]; //bits 0-8 rows, 9-17 columns, 18-26 3x3 sub-arrays
			
			for(int i = 0; i < 9; i++)
				for(int j = 0; j < 9; j++) {
					int d = b[i][j];
					assert(d >= 0 && d <= 9);
					if (d == 0)
						continue;
					int rowMask = 1<<i, colMask = 1<<(9+j), subMask = 1<<(18 + 3*(i/3) + (j/3));
					
					if ((f[d-1] & rowMask) != 0 || (f[d-1] & colMask) != 0 || (f[d-1] & subMask) != 0)
						return false;
					f[d-1] |= rowMask|colMask|subMask;
				}
			return true;
		}
	}
	
	public static class Problem_16_10 {
		//The simpliest idea is to find free square, find valid digits for this square and then put one of these digits on it.
		//After that try to solve the remaining problem recursively.  If successful, we found the solution.  If not successful,
		//we try another valid digit and repeat.
		//We can speed up finding valid digits for given square by keeping a bit mask for each column, row and 3x3 subarray indicating
		//which digits are available.  We need 9 bits for each row, column and subarray, thus we need 9*(9+9+9)=243 bits (31 bytes).
		//We can also speed up finding free square by using bitmask of available squares.  We need 9*9=81 bits (11 bytes).
		//Perhaps we can use java.util.BitSet for both purposes, at least it will reduce used space and hopefully nextSetBit() and
		//nextClearBit() have efficient implementation.
		
		private static boolean solve(int [][] board, BitSet rcsMask, BitSet freeMask, int freeOff)
		{
			int nextFree = freeMask.nextClearBit(freeOff);
			if (nextFree > 80) //all 81 bits are set, we solved it
				return true;
				
			freeMask.set(nextFree); //occupy the square
			
			int row = nextFree/9, col = nextFree%9, sub = 3*(row/3) + (col/3);
			int rowStart = row*9, colStart = 81 + col*9, subStart = 162 + sub*9;
			BitSet digMask = rcsMask.get(rowStart, rowStart + 9);
			digMask.or(rcsMask.get(colStart, colStart + 9));
			digMask.or(rcsMask.get(subStart, subStart+9));

			int d = -1;
			//try out all available digits
			while((d = digMask.nextClearBit(d+1)) < 9) {
				board[row][col] = d+1;
				rcsMask.set(rowStart + d);
				rcsMask.set(colStart + d);
				rcsMask.set(subStart + d);
				//if solved, back out of the recursion, we don't care about clearing the state anymore
				if (solve(board, rcsMask, freeMask, nextFree + 1))
					return true;
				//clear what we previously set so that our state is the same when before and after this call
				board[row][col] = 0;
				rcsMask.clear(rowStart + d);
				rcsMask.clear(colStart + d);
				rcsMask.clear(subStart + d);
			}
			
			//we tried all available digits and can't solve on this branch
			freeMask.clear(nextFree);
			return false;
		}
		
		public static void solveSudoku(String [] args)
		{
			int [][] board = new int[9][9];
			BitSet rcsMask = new BitSet(243), freeMask = new BitSet(81);
			for(String a: args) {
				int r = a.charAt(0) - '1', c = a.charAt(1) - '1', d = a.charAt(2) - '1';
				board[r][c] = d+1;
				freeMask.set(9*r+c);
				int s = 3*(r/3) + (c/3);
				int rowStart = r*9, colStart = 81 + c*9, subStart = 162 + s*9;
				rcsMask.set(rowStart + d);
				rcsMask.set(colStart + d);
				rcsMask.set(subStart + d);
			}
			if (solve(board, rcsMask, freeMask, 0)) {
				for(int i = 0; i < 9; i++) {
					for(int j = 0; j < 9; j++)
						System.out.print(board[i][j] + " ");
					System.out.println();
				}
				System.out.println("Board check: " + (Problem_6_21.sudokuCheck(board) ? "passed" : "failed"));
			}
			else
				System.out.println("No solution is possible");
		}
		
		public static void test(String [] args)
		{
			solveSudoku(args);
		}
		
	}
	
	public static class Problem_18_9 {
		
		//The idea is simple.  We keep 2 variables: word and a count.  We read 1st word, assign it to the word variable and set count to 1.
		//Then if we read the same word again, we increment the count.  If we read different word: if the count > 1 we decrement it,
		//otherwise we assign the word variable to the word we just read.  Once we read the whole stream, the word variable will contain
		//the majority element.
		//Now we need to prove that this works:
		//First, it is more convenient to think that when we change the word variable to point to different word, the count goes from 1 to 0
		//(we remove previous word) and then back to 1 (we assigned the new word).  During reading of the stream, the count will fluctuate
		//possibly hitting 0 one or more times.
		//1) In simpliest case, the count never hits 0 after we start.  Then we know the word variable contains the word that
		//occured > 1/2 times of all the words read (otherwise the count would hit 0).  So the word variable must contain the majority element.
		//2) The count may hit 0 one more more times.  If we consider any interval in between 2 times when the count hit 0, it is clear that
		//during this interval no word has occured more than 1/2 times (otherwise the count would not hit 0 or would hit 0 sooner).  This
		//means that from the start and until the count hit 0 for the last time, no word has occurred > 1/2 times.  Since we know we do
		//have the majority element, the count must be > 0 at the end.  It is also clear that the after the count hits 0 for the last time,
		//the word variable will contain the majority element.  This is easy to see since any element other than the word variable one would
		//have occured < 1/2 times (since it occured <= 1/2 times until the last 0 and must have occurred < 1/2 times since last count=0,
		//because the word variable word has occured > 1/2 times since last count=0.  So we see that at the end the count > 0 and only
		//the latest word assigned to the word variable could have occured > 1/2 times in the stream, and since we know we have
		//the majority element, this latest word must be it.
		
		public static String findMajorElem1(String [] words)
		{
			String word = "";
			int cnt = 1;
			for(String s : words)
				if (s.equals(word))
					cnt++;
				else {
					if (cnt > 1)
						cnt--;
					else
						word = s;
				}
			return word;
		}
		
		//Ok, I made one mistake in the reasoning above.  For the proof above to be valid, we cannot replace the word in the word variable
		//when count=1.  Then the count goes from 1 to 0 and then back to one while we only read one word, so our intervals between count=0
		//will overlap.  The correct solution is to let the count go to 0 and then assign next read word to the word variable.  This is
		//pretty much same as the solution in the book, however proved by different reasoning.
		
		public static String findMajorElem2(String [] words)
		{
			String word = "";
			int cnt = 0;
			for(String s : words)
				if (cnt == 0) {
					word = s;
					cnt++;
				}
				else if (s.equals(word))
					cnt++;
				else
					cnt--;
			return word;
		}
		
		//It seems though that findMajorElem1 still works as well.  Why?  Can we modify the our proof to show that it works even if
		//word variable gets replaced without letting count go to 0 first?
		//Instead of considering intervals between count=0, consider interval where word variable gets replaced twice, including 1st
		//replace and excluding the 3rd one, say we have sequence a.....b.....c...., where a, b, c are words with which word variable
		//gets replaced.  So our interval would be [a....b....] excluding c.  In interval [a....b] word a will occur 1/2 the time, which
		//means that in interval [a....] word a will occur 1/2 time + 1 and every other word including b will occur <= 1/2 time - 1.
		//Same reasoning applies to word b and interval [b....].  We can sort of swap a and b between these intervals to see that in
		//interval [a....b....] no word would occur more than 1/2 the time.
		//So in our stream we can skip all such intervals [a....b....] and only consider the tail.  Since we do have the majority element
		//we know that the tail cannot be of the form [a....b....], otherwise the reasoning above would apply.  So the tail can be of
		//2 forms:
		//1) [a....] meaning that a is assigned to the word variable and will not get replaced.  In this case a will occur > 1/2 time
		//in the tail and so it must be the majority element.
		//2) [a....b...], like the interval [a....b....] that we considered except the count remains > 1 at the end.  This means that
		//in the interval [b...] the number b's is greater than number of other words by at least 2.  If we swap a and b in
		//between [a....] and [b...] then a will occur in [a....] at most 1/2 of the time (same as for reasoning about [a....b....] above),
		//and number of b's in [b...] will still be greater than number of other words by at least 1.  This means that either a or any
		//element other than b cannot be the majority element so the majority element must be b.
		
		//One side note: in the interval [a....b....] the numbers of [....] in both cases are even.  Because the count starts with 1
		//and returns to 1, so number of count increments must equal to number of count decrements.
		//We can use this reasoning instead of swapping a and b in [a....b....] since this means that in 1st [....] a occurs
		//half the time (same for b in 2nd [....]).
		
		//Well, this was exhausting proof, but looks like it works :)
		
		public static void test(String [] args)
		{
			System.out.println(findMajorElem1(args));
		}
	}

	public static class Problem_13_9 {
		
		//We keep a hash table where we map elements of Q into indexes of A where they occur.  We iterate over A sequentially.  Once we got
		//all values in Q covered, the covering range size will be the distance between max and min indexes of A stored in the hash table.
		//Every time we encounter element of A that is element of Q, we replace the index of A in the hash table with the one we just found.
		//The covering range will move to the right every time minimum index changes.  We keep track of the size of the covering range
		//as we iterate over A in order to find the minimum one.
		//Note that we will replace the index with the latest one if we encounter element of Q in A, even if the index was not minimal.
		//There is a reason for that.  From any point in our iteration, the only way to get smaller covering range than we already have is
		//when the minimal index advances.  By replacing index > minimal, we get a chance to advance the minimal index by bigger distance than
		//if we didn't replace.  So replacing at each point will always yield range of size <= current covering range size.
		
		//Since we iterated over "a" only once, this already answers the 2nd question in the book.

		//We need to keep a sorted set of indexes so that we can keep track of running min and max values.
		//java.util.TreeSet will suffice.  The running time of the algorithm will be O(a.length * log(q.length)).
		
		public static IntPair findMinCoverRange1(String [] a, String [] q)
		{
			IntPair r = new IntPair(0, Integer.MAX_VALUE);
			int minLen = Integer.MAX_VALUE;
			HashMap<String,Integer> hs = new HashMap<String,Integer>(q.length);
			TreeSet<Integer> ss = new TreeSet<Integer>();
			
			//This way we will have unique pseudo-index for each uncovered value of Q.  Every time element of Q
			//gets covered in A, we will replace its pseudo-index with real index.
			//The whole range of Q will be covered once min element of ss is non-negative.
			//Note that this will work even if Q contains duplicate values, we just won't use all values of i.
			for(int i = 0; i < q.length; i++) {
				Integer v = Integer.valueOf(-1-i);
				if (!hs.containsKey(q[i])) {
					hs.put(q[i], v);
					ss.add(v);
				}
			}
			
			//We will always have size(hs) = size(ss) = length of Q with duplicates removed
			boolean covered = false;
			
			for(int i = 0; i < a.length; i++) {
				Integer v = hs.get(a[i]);
				if (v != null) {
					if (hs.size() == 1)
						return new IntPair(i, i); //optimization
					Integer nv = Integer.valueOf(i);
					hs.put(a[i], nv);
					ss.remove(v);
					ss.add(nv);
					covered = covered || (ss.first().intValue() >= 0);
					if (covered) {
						int len = ss.last() - ss.first() + 1;
						if (len < minLen) {
							len = minLen;
							r.x = ss.first().intValue();
							r.y = ss.last().intValue();
						}
					}
				}
			}
			
			return covered ? r : new IntPair(-1,-1);
		}
		
		//Hmmm, but the book contains solution that is O(a.length).  Can we change our solution to be O(a.length)?
		//We know that at each iteration if index gets replaced then max index is i itself.  How do we keep track
		//of the minimum index without sorted set?  What if we keep the indexes in linked list?  The list would
		//already be sorted because every time we replace, we replace with the index > any index in the list.
		//This should work.  The running time of the new solution will be O(a.length).
		//We can keep the nodes of the linked list as values in the hashtable.  We initialize the hashtable with
		//empty nodes.  For every key that gets covered, we add its value to the linked list.  Once the size
		//of the linked list reaches size of the hashtable, we know all values in Q are covered.
		
		public static IntPair findMinCoverRange2(String [] a, String [] q)
		{
			IntPair r = new IntPair(-1, -1);
			int minLen = Integer.MAX_VALUE;
			DLList<Integer> ls = new DLList<Integer>();
			HashMap<String,DLNode<Integer> > hs = new HashMap<String,DLNode<Integer> >(q.length);
			
			for(String s : q)
				hs.put(s, new DLNode<Integer>());
			if (hs.size() == 0) //q is empty
				return a.length == 0 ? r : new IntPair(0, 0);
			
			for(int i = 0; i < a.length; i++) {
				DLNode<Integer> n = hs.get(a[i]);
				if (n != null) {
					if (hs.size() == 1)
						return new IntPair(i, i); //optimization
					n.data = Integer.valueOf(i);
					if (ls.inList(n))
						ls.remove(n);
					ls.addLast(n);
					if (ls.size == hs.size()) { //values in q are covered
						int len = ls.tail.data - ls.head.data + 1; 
						if (len < minLen) {
							minLen = len;
							r.x = ls.head.data;
							r.y = ls.tail.data;
						}
					}
				}
			}
			
			return r;
		}
		
		public static void test(String [] args) throws Exception
		{
			int i = Arrays.asList(args).indexOf("in");
			if (i == -1)
				return;
			String [] q = Arrays.copyOfRange(args, 0, i), a = Arrays.copyOfRange(args, i+1, args.length);
			System.out.println("Q= " + Arrays.toString(q));
			System.out.println("A= " + Arrays.toString(a));
			IntPair r = findMinCoverRange2(a, q);
			System.out.println(String.format("[%d,%d]", r.x, r.y));
		}
		
		//I feel there is a need for a more rigorous proof that this algorithm in fact does find the minimum covering range.
		//The main property of this algorithm is that for each word of Q, it keeps the max index in A where this word was found.
		//When we cover values in Q the first time, we have the minimum covering range from part of A considered so far: suppose
		//the element Q[j] was the last element of Q found at index k in A to get full coverage.  Since A[k] is the first occurence
		//of Q[j], the covering range has to include k.  We know that for every other element of Q we store the max index where it
		//occured in A, so these indexes must form minimum covering range, otherwise some element of Q would repeat within that range
		//which contradicts the main property of our algorithm.
		//Suppose that at this time index k1 in A containing element Q[j1] is the lowest covered index in A, that is k1 is the left
		//end of the range.  Also suppose that the next occurence of Q[j1] is at some index k2 in A.  Note that any covering range
		//has to contain k1 or k2, since Q[j1] has to occur.  This means that the range we found so far (when we first got it covered),
		//must be the minimal covering range in subarray A[0..k2-1] (since we cannot move the left end of that range k1 until we look
		//at k2).  Once we read at index k2, the only way to get smaller range is to change index of Q[j1] from k1 to k2.  Once we do
		//that, we have the range that is minimal range among those ending at k2 since we keep max index in A for each element of Q
		//(same as the reasoning above).  This means that so far during our iteration we have not missed the minimum covering range.
		//Now suppose at this time that k3 is the left end of the range in A containing element Q[j2].  Now we can proceed at the same
		//manner and make the same argument about k3 and Q[j2] as we made about k1 and Q[j1], that is there is no smaller covering
		//range until we find next occurence of Q[j2].  And so forth.  So all candidates for minimal covering range have been
		//considered.
		
		//Ok, this was complicated.  In fact there is more simple proof:
		//The minimal covering range must contain at both ends elements of Q (otherwise we could obviously trim it).  Our algorithm
		//will consider a covering range ending at index k for each index k of A containing an element of Q (once we got Q covered).
		//It only remains to show that for each such index k, our algorithm see the minimum range of all ranges ending at k.  This
		//follows from the fact that we keep the latest index in A for each element of Q (so it is not possible to shift the left
		//end of the range).
		
	}
	
	public static void swap(int [] a, int i, int j)
	{
		int x = a[i];
		a[i] = a[j];
		a[j] = x;
	}
	
	public static void reverse(int [] a, int off, int len)
	{
		for(int i = off, j = off + len - 1; i < j; i++,j--)
			swap(a, i, j);
	}
	
	public static class Problem_6_15 {
		
		//We can solve it by repeated swapping.  Say we have array a[0....n-1] and need to shift left by j positions.  If j <= n/2, first
		//we swap elements a[0]...a[j-1] with elements a[n-j]...a[n-1] correspondingly.  Now we have array:
		//a[n-j]...a[n-1]a[j]...a[n-j-1]a[0]...a[j-1]
		//Now the last j elements are already in their proper place and the problem reduces to shifting subarray a[0...n-j-1] by
		//j positions to the left (since this subarray of size n-j in our final result should start with a[j] and end with a[n-1]).
		//If j > n/2, then we instead we start by n-j elements a[0]...a[n-j-1] with a[j]...a[n-1] correspondingly, so we get:
		//a[j]...a[n-1]a[n-j]...a[j-1]a[0]...a[n-j-1]
		//Now the first n-j elements are in their proper place so the problem reduces to shifting subarray a[n-j...n-1] of size j
		//by s positions to the left where s = j - (n-j) = 2*j - n, because in the final result this subarray should begin with a[0]
		//and end with a[j-1].
		//We proceed in this manner reducing the size of the problem and chosing which elements to swap depending on whether j <= n/2 or
		//j > n/2.
		//This algorithm requires O(1) additional storage and runs in O(n) time.  To prove the latter, notice that after doing k swaps
		//(where k is either j or n-j) we reduce the size of the problem by k elements, so only total n swaps are needed.
		
		public static void rotateIntArray(int [] a, int j)
		{
			if (j <= 0)
				return;
			
			int start = 0, len = a.length, shift = j;
			while(shift < len) {
				//System.out.println("start="+start+";shift="+shift+";len="+len);
				//printIntArray(a);
				if (shift <= (len>>1)) {
					for(int i = 0; i < shift; i++)
						swap(a, start + i, start + len - shift + i);
					len -= shift;
				}
				else {
					for(int i = 0; i < len - shift; i++)
						swap(a, start + i, start + shift + i);
					start += len - shift;
					int l = len;
					len = shift;
					shift = (shift<<1)-l;
				}
			}
		}
		
		//Oh well, i looked at the answer too far as usual and it seems that there is a more simple solution using reverse().
		//Let's see...
		
		public static void rotateIntArray2(int [] a, int j)
		{
			reverse(a, 0, a.length);
			reverse(a, 0, a.length - j);
			reverse(a, a.length -j, j);
		}
		
		//Why does this work?  We can think of shifting array a by j positions to the left as swapping subarrays a[0..j-1] and a[j..n-1].
		//To do this, we can reverse the whole array and then reverse the subarrays back (similar to problem 7.4).
		
		public static void test(String [] args) throws Exception
		{
			int [] a = readIntArray();
			rotateIntArray2(a, Integer.parseInt(args[0]));
			printIntArray(a);
		}
		
	}
	
	public static int [][] readInt2D() throws Exception
	{
		ArrayList<int []> al = new ArrayList<int []>();
		while(true) {
			int [] a = readIntArray();
			if (a.length == 0)
				break;
			al.add(a);
		}
		return al.toArray(new int[][]{});
	}
	
	public static void printInt2D(int [][] m)
	{
		for(int [] a : m)
			printIntArray(a);
	}
	
	public static char [][] readChar2D() throws Exception
	{
		ArrayList<char []> al = new ArrayList<char []>();
		while(true) {
			char [] a = readCharArray();
			if (a.length == 0)
				break;
			al.add(a);
		}
		return al.toArray(new char[][]{});
	}
	
	public static void printChar2D(char [][] m)
	{
		for(char [] a : m)
			printCharArray(a);
		System.out.println();
	}
	
	public static class Problem_19_1 {
		
		//It seems that the solution in the book uses DFS.  Let's code this problem with BFS and DFS both, since BFS will find shortest path.
		//Assume that the matrix elems are integers that store 0 (free space) or 1 (wall).  We can use them to store additional information
		//needed for BFS. We only need 3 bits: 1 bit to indicate visited/not visited and 2 bits to save the parent node - for parent, we don't //need to save the parent's coordinates, only the direction from the node to the parent, there are 4 directions possible.
		//In addition, after we find the path, we can mark it in the maze by putting some special number into each element on the path,
		//e.g. 5.  For this to look good, we will also need to remove previous markings we made.
		
		private static final int V_MASK = 0x200, D_OFF = 10, D_MASK = 0xC00; //visited mask, direction offset, mask
		private static final int LEFT = 0, UP = 1, DOWN = 2, RIGHT = 3; //choose such that opposite dir = 3 - dir
		private static final IntPair [] DIR = { new IntPair(0, -1), new IntPair(-1, 0), new IntPair(1, 0), new IntPair(0, 1) };
		private static final int PATH = 5;
		
		private static ArrayList<IntPair> makePath(int [][] m, IntPair start, IntPair exit)
		{
			ArrayList<IntPair> path = new ArrayList<IntPair>();
			IntPair n = exit;
			while(true) {
				int d = (m[n.x][n.y] >> D_OFF);
				m[n.x][n.y] = PATH;
				path.add(n);
				if (n.equals(start))
					break;
				n = new IntPair(n.x + DIR[d].x, n.y + DIR[d].y);
			}
			Collections.reverse(path);
			
			//clean up all entries for better output view
			for(int i = 0; i < m.length; i++)
				for(int j = 0; j < m[0].length; j++)
					m[i][j] &= 0xff;
				
			return path;
		}
		
		public static ArrayList<IntPair> findPathBFS(int [][] m, IntPair start, IntPair exit)
		{
			if (m.length == 0 || m[0].length == 0)
				return null;
			
			ArrayDeque<IntPair> q = new ArrayDeque<IntPair>();
			m[start.x][start.y] = V_MASK;
			q.add(start);
			
			while(!q.isEmpty()) {
				IntPair v = q.poll();
				for(int i = 0; i < 4; i++) {
					IntPair n = new IntPair(v.x + DIR[i].x, v.y + DIR[i].y);
					if (n.x < 0 || n.x >= m.length || n.y < 0 || n.y >= m[0].length ||
						m[n.x][n.y] != 0)
						continue;
					m[n.x][n.y] = (V_MASK|((3-i)<<D_OFF)); //mark visited and direction to parent (4-i)
					if (n.equals(exit))
						return makePath(m, start, exit);
					q.add(n);
				}
			}
			return null; //path from start to exit does not exist
		}
		
		//For DFS, we don't need to store direction to the parent in the matrix itself, since we will have all
		//ancestors on the function stack.
		
		private static boolean doFindPathDFS(int [][] m, IntPair v, IntPair exit, ArrayList<IntPair> path)
		{
			if (v.equals(exit)) { //we found exit, start unwinding
				path.add(v);
				m[v.x][v.y] = PATH;
				return true;
			}
			
			for(int i = 0; i < 4; i++) {
				IntPair n = new IntPair(v.x + DIR[i].x, v.y + DIR[i].y);
				if (n.x < 0 || n.x >= m.length || n.y < 0 || n.y >= m[0].length ||
					m[n.x][n.y] != 0)
					continue;
				m[n.x][n.y] = V_MASK;
				if (doFindPathDFS(m, n, exit, path)) { //if found, continue unwinding
					path.add(v);
					m[v.x][v.y] = PATH;
					return true;
				}
			}
			
			return false;
		}
		
		public static ArrayList<IntPair> findPathDFS(int [][] m, IntPair start, IntPair exit)
		{
			ArrayList<IntPair> ls = new ArrayList<IntPair>();
			m[start.x][start.y] = V_MASK;
			if (!doFindPathDFS(m, start, exit, ls))
				return null;
			
			//clean up all entries for better output view
			for(int i = 0; i < m.length; i++)
				for(int j = 0; j < m[0].length; j++)
					m[i][j] &= 0xff;
			
			return ls;
		}
		
		public static void test(String [] args) throws Exception
		{
			int [][] m = readInt2D();
			IntPair start = null, exit = null;
			//we'll have start and exit embedded in the matrix, start=2, exit=3
			for(int i = 0; i < m.length; i++)
				for(int j = 0; j < m[0].length; j++)
					if (m[i][j] == 2) {
						start = new IntPair(i, j);
						m[i][j] = 0;
					}
					else if (m[i][j] == 3) {
						exit = new IntPair(i, j);
						m[i][j] = 0;
					}
			System.out.println("Start="+start+", exit="+exit);
			System.out.println();

			ArrayList<IntPair> path = args.length > 0 ? findPathDFS(m, start, exit) : findPathBFS(m, start, exit);
			if (path == null)
				System.out.println("No path exists...");
			else {
				printInt2D(m);
				System.out.println();
				System.out.print("Path: ");
				for(IntPair p : path)
					System.out.print(p + " ");
				System.out.println();
			}		
		}
	}
	
	public static class Problem_19_3 {
		
		//Seems pretty straightforward.  Iterate over inner elements (not on the boundary).  When we find W, do DFS or BFS starting from it
		//marking the notes as some intermedate (like G for gray), checking if boundary is reached.  If boundary not reached, mark all Cs as Bs,
		//otherwise leave them as Gs.  Then continue iterating to search for next W.  Once finished, go through all again and change all Gs
		//back to Ws.
		
		//When we do find that a region is enclosed, we would have to do another BFS or DFS on it to change all Gs to Bs.  So far I don't
		//see any better way for this (of course if it's an array of ints we could number each region and then reference those numbers (via
		//a hash table for example), to repaint as W or B on the final pass, but this doesn't scale assymptotically).

		//I think that either BFS or DFS would have the same time and space assymptotically.  We can use DFS for simplicity here.
		
		//Once we hit the boundary when searching a region, there might still remain some unexplored elements at this point.  So,
		//this region will consist of some As and some Gs at this point which will be misleading for the next search (since next found
		//A could be just part of previous region that was not fully searched).  So it seems like we have to explore each region fully
		//even if we already know it is not enclosed.
		
		private static final int LEFT = 0, UP = 1, DOWN = 2, RIGHT = 3;
		private static final IntPair [] DIR = { new IntPair(0, -1), new IntPair(-1, 0), new IntPair(1, 0), new IntPair(0, 1) };
		
		//last arg enclosed: if true, we already know it is enclosed and repainting with Bs, otherwise we paint with Gs
		//returns true if reached the boundary
		private static boolean doDFS(char [][] m, int startX, int startY, boolean enclosed)
		{
			if (startX == 0 || startX == m.length - 1 || startY == 0 || startY == m[0].length - 1)
				return true;
			
			m[startX][startY] = enclosed ? 'b' : 'g';
			
			boolean found = false;
			for(int i = 0; i < 4; i++) {
				//we won't fall off since start is not on the boundary
				int x = startX + DIR[i].x, y = startY + DIR[i].y;
				if (m[x][y] == (enclosed ? 'g' : 'a') && doDFS(m, x, y, enclosed))
					found = true;
			}
			return found;
		}
		
		public static void paintEnclosed(char [][] m)
		{
			if (m.length < 3 || m[0].length < 3)
				return;
			
			for(int i = 1; i < m.length - 1; i++)
				for(int j = 1; j < m[0].length - 1; j++) {
					if (m[i][j] == 'a' && !doDFS(m, i, j, false))
						doDFS(m, i, j, true); //found enclosed region, repaint with 'B's
				}
						
			//repaint non-enclosed regions back to 'A's
			for(int i = 1; i < m.length - 1; i++)
				for(int j = 1; j < m[0].length - 1; j++)
					if (m[i][j] == 'g')
						m[i][j] = 'a';
		}
		
		public static void test() throws Exception
		{
			char [][] m = readChar2D();
			paintEnclosed(m);
			printChar2D(m);
		}
		
	}
		
	public static class Problem_19_6 {
		
		//Basically we need to determine if the graph is bipartite and return the vertex subsets if it is.
		//We can do it the following way:
		//First we build a set of vertices that have no edges between each other.  We can do that iteratively: start with empty set,
		//then consider each vertex in turn: if it has no edges connecting it to vertices in the set, then add it to the set.
		//Once we have built this set, we check the set of remaining vertices to see if there are any edges between them.  If there are
		//no such edges than the graph is bipartite and these 2 sets represent the desired division.  If there are edges between them,
		//the graph is not bipartite.
		//How do we prove that it works:
		//1) If the graph is bipartite, then the 1st vertix we consider will belong to one of the 2 (left or right) divisions of the bipartite
		//graph.  Let's say it is left division.  After running the 1st part of our algorithm, our first set will contain all the vertices of
		//the left division (it might contain other vertices as well, since in general left/right bipartite devisions are not unique.  These
		//additional vertices will be isolated vertices having no edges at all since our algorithm already determined they have no edges
		//between themselves or remaining vertices of the left devision and they have no edges to the vertices of the right division since
		//we assumed the graph is bipartite).
		//The remaining vertices will belong to the right division and there are no edges between them (by definition of bipartite graph).
		//So our algorithm will return the correct division.
		//2) If the graph is not bipartite, the 2nd part of our algorithm will find an edge in between the vertices of the remaining set
		//(if it didn't find such an edge, the graph would be bipartite).
		//The problem doesn't specify how the graph is represented, so we can assume adjacency list representation.
		
		public static class Pin {
			int n;
			ArrayList<Pin> edges = new ArrayList<Pin>();
			boolean marked;
			Pin(int n) { this.n = n; }
		}
		
		public static boolean isBipartite(ArrayList<Pin> g)
		{
			for(Pin v: g) {
				boolean edgesToSet = false;
				for(Pin e: v.edges)
					if (e.marked) {
						edgesToSet = true;
						break;
					}
				if (!edgesToSet)
					v.marked = true;
			}
			for(Pin v: g) {
				if (v.marked)
					continue;
				for(Pin e: v.edges)
					if (!e.marked) //if e is not in the first set, then it must be one of remaining pins
						return false;
			}
			//Marked/unmarked pins will represent bipartite division.
			return true;
		}
		
		//Assume we have N pins with numbers 0...N-1 and the edges are represented as IntPair[] containing vertix numbers
		private static ArrayList<Pin> makePinGraph(int n, ArrayList<IntPair> wires)
		{
			ArrayList<Pin> g = new ArrayList<Pin>(n);
			for(int i = 0; i < n; i++)
				g.add(new Pin(i));
			for(IntPair w: wires) {
				g.get(w.x).edges.add(g.get(w.y));
				g.get(w.y).edges.add(g.get(w.x));
			}
			return g;
		}
		
		private static ArrayList<IntPair> array2wires(int [] a)
		{
			ArrayList<IntPair> ls = new ArrayList<IntPair>(a.length/2);
			for(int i = 0; i < a.length; i+= 2)
				ls.add(new IntPair(a[i], a[i+1]));
			return ls;
		}
		
		private static int [] g2array(ArrayList<Pin> g, boolean marked) throws Exception
		{
			int [] a = new int[g.size()];
			int i = 0;
			for(Pin v: g)
				if (v.marked == marked)
					a[i++] = v.n;
			return Arrays.copyOfRange(a, 0, i);
		}
		
		public static void test() throws Exception
		{
			int n = Integer.parseInt(System.console().readLine());
			ArrayList<IntPair> wires = array2wires(readIntArray());
    		System.out.println();
			ArrayList<Pin> g = makePinGraph(n, wires);
			if (!isBipartite(g))
				System.out.println("The graph is not bipartite");
			else {
				printIntArray(g2array(g, true));
				printIntArray(g2array(g, false));
			}
		}
	}
	
	public static class Problem_18_12 {
		
		//We will think analogous to the book solution for 18.9.
		//Suppose there are m searched words out of n, so the ratio is m/n.  If we discard x distinct words, the ratio will be
		//at least (m-1)/(n-x).  We need to find x such that (m-1)/(n-x) >= m/n   =>
		//(m-1)*n >= m*(n-x)  =>  m*n - n >= m*n - m*x  =>  m*x >= n  => x >= n/m.  From the problem description, m = ceil(n/k).
		//=> x >= n/(ceil(n/k)).  ceil(n/k) >= n/k  => n/ceil(n/k) <= n/(n/k) = k, so it is sufficient to choose x >= k.
		//So we can discard k distinct elements from the sequence and the frequency of the searched word will not decrease.
		//The remaining sequence will have n' = n-k elements and at least ceil(n/k)-1 = ceil(n-k/k) = ceil(n'/k) of the searched
		//element.  Thus we can repeat this procedure again by removing another k distinct elements with frequency of the searched
		//word not changing, and so on.  Note that this frequency is ceil(n/k)/n >= (n/k)/n = 1/k.
		
		//So we need to read from the stream and keep discarding k distinct words at a time.  We can keep a hash table of max
		//k-1 entries keyed by candidate words with values as counters for each of these words.  When we read a word, we check if it is
		//already in the hash, and if so, increment the counter.  If not and number of hash elements < k-1 we add it to the hash table.
		//If number of entries is k-1, then now we have total of k distinct elements.  We discard the element we just read and k-1 elements
		//from the hash table by decrementing their counters or removing them if counter reaches 0 - that is unless we reached the end of input.
		//At the end we will have 0<=m<=k candidate elements.  We can check them one by one to see if we have at least ceil(n/k) of each
		//on the 2nd pass (since even though we know the searched words will be among the candidates, not all candidates are searched words
		//(that is they may have occured less than ceil(n/k) times).
		
		//Actually it might be easier to keep up to k elements in the hash table instead of k-1, this way we don't need to read ahead to
		//know if we reached the end of the stream.
		
		public static String [] findHeavyHitters(String [] a, int k)
		{
			HashMap<String,int[]> m = new HashMap<String,int[]>();
			
			//1st iteration - find candidate words
			for(String s: a) {
				int [] c = m.get(s);
				if (c != null)
					c[0]++;
				else {
					if (m.size() == k) {//discard k elements
						Iterator<String> it = m.keySet().iterator();
						while(it.hasNext())
							if (--m.get(it.next())[0] == 0)
								it.remove();
					}
					c = new int[] {1};
					m.put(s, c);
				}
			}
			
			//reset candidate counts
			for(Map.Entry<String,int[]> en: m.entrySet())
				en.getValue()[0] = 0;
			
			//2nd iteration, filter out candidates to find the searched words.
			for(String s: a) {
				int [] c = m.get(s);
				if (c != null)
					c[0]++;
			}
			
			//number of occurences required
			int t = a.length/k;
			if (a.length%k != 0)
				t++;
			
			//remove words with that occur less than t times
			Iterator<String> it = m.keySet().iterator();
			while(it.hasNext())
				if (m.get(it.next())[0] < t)
					it.remove();
				
			return m.keySet().toArray(new String[0]);
		}
		
		//The space used is O(k) (for the hash table).
		//What is the running time? If we consider that each hash table operation takes O(1), then the running time should be O(n).
		//It is obvious that each of 2 passes iterates n times.  The only question is about the discarding operation in the first loop.
		//However we can prove that the amortized cost of each discard operation is O(1). For each element we have read in array 1,
		//there is 1 operation of either putting it in the hash table or incrementing its count, and one operation for discarding it,
		//which is done either by decrementing its count or removing it from the hash table.  Well, actually it may have 2 operations of both
		//decrementing the count and then discarding it, but in any case the point is that there is at most const number of operations
		//per each element we have read (const that is same throughout the 1st pass).  This means that the whole discard is O(n) for the
		//whole 1st pass, meaning amortized cost of discard is O(1)  (the cost of creating iterator can also be amortized into the cost
		//for each element).
		//This means that the total running time is O(n) as long as hash table operations are O(1) (otherwise if, e.g. hash is implemented as
		//binary tree, the cost would be O(n*log(k)).
		
		public static void test(String [] args) throws Exception
		{
			int k = Integer.parseInt(args[0]);
			String [] a = findHeavyHitters(Arrays.copyOfRange(args, 1, args.length), k);
			for(String w: a)
				System.out.print(w + ' ');
			System.out.println();
		}
		
	}
	
	public static interface RWLock {
		public void readLock() throws InterruptedException;
		public void readUnlock() throws InterruptedException;
		public void writeLock() throws InterruptedException;
		public void writeUnlock() throws InterruptedException;
	}
	
	public static class RWLockTest implements Runnable {
		private RWLock lock;
		private int percentReaders, maxSleep;
		private AtomicInteger activeThreadCnt = new AtomicInteger(0);
		private static final int READER = 0, WRITER = 1;
		private AtomicIntegerArray rwCnt = new AtomicIntegerArray(2);
		private AtomicIntegerArray totalCnt = new AtomicIntegerArray(2),
			acquiredCnt = new AtomicIntegerArray(2);
		private long [] maxDelay = new long[2], avgDelay = new long[2];
		
		private synchronized void updateDelay(int rw, long delay)
		{
			if (delay > maxDelay[rw])
				maxDelay[rw] = delay;
			int n = acquiredCnt.get(rw); //how many acquired before current thread
			avgDelay[rw] = (n * avgDelay[rw] + delay)/(n+1);
		}
		
		private synchronized void checkInvariant()
		{
			if (rwCnt.get(WRITER) > 1 || (rwCnt.get(WRITER) == 1 && rwCnt.get(READER) > 0)) {
				System.out.println("INVARIANT VIOLATED!!!!!");
				System.out.println("Writer count = " + rwCnt.get(WRITER));
				System.out.println("Reader count = " + rwCnt.get(READER));
				System.exit(0);
			}
		}
		
		public RWLockTest(RWLock lock, int percentReaders, int maxSleep)
		{
			this.lock = lock;
			this.percentReaders = percentReaders;
			this.maxSleep = maxSleep;
		}
		
		public RWLockTest(RWLock lock)
		{
			this(lock, 50, 100);
		}
		
		public void run()
		{
			activeThreadCnt.incrementAndGet();
			try {
				ThreadLocalRandom r = ThreadLocalRandom.current();
				int rw = (Math.abs((r.nextInt()%100)) < percentReaders) ? READER : WRITER;
				totalCnt.incrementAndGet(rw);
				long t = System.currentTimeMillis();
				
				if (rw == READER)
					lock.readLock();
				else
					lock.writeLock();
				
				updateDelay(rw, System.currentTimeMillis() - t);
				acquiredCnt.incrementAndGet(rw);
				
				rwCnt.incrementAndGet(rw);
				checkInvariant();
				Thread.currentThread().sleep(Math.abs(r.nextLong())%maxSleep);
				checkInvariant();
				rwCnt.decrementAndGet(rw);
				
				if (rw == READER)
					lock.readUnlock();
				else
					lock.writeUnlock();
			}
			catch(Exception e) {
				System.out.println("Exception thrown: " + e);
			}
			finally {
				activeThreadCnt.decrementAndGet();
			}
		}
		
		public void printStats()
		{
			System.out.println("Active thread count: " + activeThreadCnt.get());
			System.out.printf("Readers:\n Total %d, aquired lock %d, max delay %d ms, avg delay: %d ms\n\n",
				totalCnt.get(READER), acquiredCnt.get(READER), maxDelay[READER], avgDelay[READER]);
			System.out.printf("Writers:\n Total %d, aquired lock %d, max delay %d ms, avg delay: %d ms\n",
				totalCnt.get(WRITER), acquiredCnt.get(WRITER), maxDelay[WRITER], avgDelay[WRITER]);
		}
		
		public void test(int total, int delay) throws Exception
		{
			for(int i = 0; i < total; i++) {
				checkInvariant();
				new Thread(this).start();
				Thread.currentThread().sleep(delay);
				checkInvariant();
				System.out.print('.');
			}
			System.out.println();
			
			do {
				Thread.currentThread().sleep(2000);
				printStats();
				System.out.println();
				System.out.println();
			} while(activeThreadCnt.get() > 0);
		}
		
		public static void test(RWLock lock, String [] args) throws Exception
		{
			int total = Integer.parseInt(args[0]), percentReaders = args.length > 1 ? Integer.parseInt(args[1]) : 50,
				delay = args.length > 2 ? Integer.parseInt(args[2]) : 0,
				maxSleep = args.length > 3 ? Integer.parseInt(args[3]) : 100;
			new RWLockTest(lock, percentReaders, maxSleep).test(total, delay);
		}
		
	}
	
	public static class Problem_20_7 {
		
		//I assume that for this problem we already have a mutex available to use.
		//Note that if we are not writing in Java and thus cannot use synchronized methods, we can just use another mutex for that purpose.
		
		public static class RWLock1 implements RWLock {
			
			private ReentrantLock l = new ReentrantLock();
			private int readerCount;
			
			public synchronized void writeLock() { l.lock(); }
			
			public synchronized void writeUnlock() { l.unlock(); }
			
			public synchronized void readLock()
			{
				if (readerCount == 0)
					l.lock();
				readerCount++;
			}
			
			public synchronized void readUnlock()
			{
				if (--readerCount == 0)
					l.unlock();
			}
		}
		
		//By glancing at the solution it looks like no mutex is available to use, so let's rewrite the solution using only
		//synchronized methods and wait/notify.
		
		//Oops, actually RWLock1 will not work because if l.lock() blocks then the methods of RWLock1 may deadlock.  We shouldn't
		//have the same problem with RWLock2 since we are synchronizing on the same object.
		
		public static class RWLock2 implements RWLock {
			
			protected int readerCount;
			protected boolean locked;
			
			protected synchronized void lock() throws InterruptedException
			{
				while(locked)
					wait();
				locked = true;
			}
			
			protected synchronized void unlock() throws InterruptedException
			{
				locked = false;
				notifyAll();
			}
			
			public void writeLock() throws InterruptedException { lock(); }
			
			public void writeUnlock() throws InterruptedException { unlock(); }
			
			public synchronized void readLock() throws InterruptedException
			{
				if (readerCount == 0)
					lock();
				readerCount++;
			}
			
			public synchronized void readUnlock() throws InterruptedException
			{
				if (--readerCount == 0)
					unlock();
			}
			
		}
		
		public static class RWFakeLock implements RWLock {
			public void writeLock() throws InterruptedException {}
			public void writeUnlock() throws InterruptedException {}
			public void readLock() throws InterruptedException {}
			public void readUnlock() throws InterruptedException {}
		}
		
		public static void test(String [] args) throws Exception
		{
			RWLockTest.test(new RWLock2(), args);
		}
		
	}
	
	public static class Problem_20_8 {
		
		public static class RWLock1 extends Problem_20_7.RWLock2 {
			protected int writerWaitCount; //number of writers waiting
			
			public synchronized void writeLock() throws InterruptedException
			{
				writerWaitCount++;
				lock();
				writerWaitCount--;
			}
			
			public synchronized void readLock() throws InterruptedException
			{
				//If reader count becomes > 0 after wait() that means another reader has aquired the lock,
				//so this reader can enter as long as writerWaitCount = 0.
				while(writerWaitCount != 0 || (readerCount == 0 && locked))
					wait();
				locked = true;
				readerCount++;
			}

		}
		
		public static void test(String [] args) throws Exception
		{
			RWLockTest.test(new RWLock1(), args);
		}

	}
	
	public static class Problem_6_4 {
		
		//We could mark the positions that are reachable.  Start by marking first position, then mark positions reachable from it,
		//keep iterating through positions and mark all positions reachable from current position.  Return true if the end position is
		//marked.
		
		//But since all reachable positions are consequitive, we can simplify it by just keeping max reachable index.
		
		public static boolean isEndReachable(int [] a)
		{
			int max = 0;
			for(int i = 0; i < a.length; i++) {
				if (max < i)
					return false;
				if (i + a[i] > max) {
					max = i + a[i];
					if (max >= a.length - 1)
						return true;
				}
			}
			return true; //this will not be reached
		}
		
		public static void test() throws Exception
		{
			int [] a = readIntArray();
			System.out.println(isEndReachable(a));
		}
		
	}
	
	public static class Problem_6_20 {
		
		//Pretty trivial.  Divide interval [0,1] according to the values of p[i].  The book solution mentions needing O(n) space,
		//however we don't need it since we can compute the value on the fly.
		
		public static double getRandDistVal(double [] t, double [] p)
		{
			double v = ThreadLocalRandom.current().nextDouble(); //in [0,1)
			double ps = 0;
			for(int i = 0; i < p.length; i++) {
				ps += p[i];
				if (v < ps)
					return t[i];
			}
			return t[t.length - 1]; //not reached if correct input
		}
		
	}
	
	public static void main(String [] args) throws Exception
	{
		Problem_6_4.test();
	}
	
} //public class EOPI
