package spatial.knnutils;

import java.util.ArrayList;
import java.util.Iterator;

import spatial.exceptions.UnimplementedMethodException;


/**
 * <p>{@link BoundedPriorityQueue} is a priority queue whose number of elements
 * is bounded. Insertions are such that if the queue's provided capacity is surpassed,
 * its length is not expanded, but rather the maximum priority element is ejected
 * (which could be the element just attempted to be enqueued).</p>
 *
 * <p><b>YOU ***** MUST ***** IMPLEMENT THIS CLASS!</b></p>
 *
 * @author  <a href = "https://github.com/jasonfillipou/">Jason Filippou</a>
 *
 * @see PriorityQueue
 * @see PriorityQueueNode
 */
public class BoundedPriorityQueue<T> implements PriorityQueue<T>{

	/* *********************************************************************** */
	/* *************  PLACE YOUR PRIVATE FIELDS AND METHODS HERE: ************ */
	/* *********************************************************************** */
	private ArrayList<PriorityQueueNode<T>> data;
	private int capacity;
	private int insertionOrder;

	/* *********************************************************************** */
	/* ***************  IMPLEMENT THE FOLLOWING PUBLIC METHODS:  ************ */
	/* *********************************************************************** */

	/**
	 * Constructor that specifies the size of our queue.
	 * @param size The static size of the {@link BoundedPriorityQueue}. Has to be a positive integer.
	 * @throws IllegalArgumentException if size is not a strictly positive integer.
	 */
	public BoundedPriorityQueue(int size) throws IllegalArgumentException{
		data = new ArrayList<PriorityQueueNode<T>>();
		this.capacity = size;
		insertionOrder = 0;
	}

	/**
	 * <p>Enqueueing elements for BoundedPriorityQueues works a little bit differently from general case
	 * PriorityQueues. If the queue is not at capacity, the element is inserted at its
	 * appropriate location in the sequence. On the other hand, if the object is at capacity, the element is
	 * inserted in its appropriate spot in the sequence (if such a spot exists, based on its priority) and
	 * the maximum priority element is ejected from the structure.</p>
	 * 
	 * @param element The element to insert in the queue.
	 * @param priority The priority of the element to insert in the queue.
	 */
	@Override
	public void enqueue(T element, double priority) {
		if (data.isEmpty()) {
			data.add(new PriorityQueueNode<T>(element, priority, ++insertionOrder));
		} else if (data.size() <= capacity) {
			ArrayList<PriorityQueueNode<T>> newData = new ArrayList<PriorityQueueNode<T>>();
			PriorityQueueNode<T> newPQN = new PriorityQueueNode<T>(element, priority, ++insertionOrder);
			for (int i = 0; i < data.size(); i++) {
				if (data.get(i).getPriority() > priority || (data.get(i).getPriority() == priority && data.get(i).compareTo(newPQN) < 0)) {
					if (newData.contains(newPQN) == false) {
						newData.add(newPQN);
						newData.add(data.get(i));
					} else {
						newData.add(data.get(i));
					}	
				} else {
					
					if (newData.contains(newPQN) == false) {
						newData.add(data.get(i));
						newData.add(newPQN);			
					} else {
						newData.set(newData.indexOf(newPQN), data.get(i));
						newData.add(newPQN);
					}
				}
			}
			
			// Capacity is reached
			if (data.size() == capacity) {
				newData.remove(newData.size()-1);
			}
			data.clear();
			data.addAll(newData);
		} 
	}

	@Override
	public T dequeue() {
		if (isEmpty())
			return null;		
		T oldFirst = first();
		data.remove(0);
		return oldFirst;	
	}

	@Override
	public T first() {
		return data.get(0).getData();
	}
	
	/**
	 * Returns the last element in the queue. Useful for cases where we want to 
	 * compare the priorities of a given quantity with the maximum priority of 
	 * our stored quantities. In a minheap-based implementation of any {@link PriorityQueue},
	 * this operation would scan O(n) nodes and O(nlogn) links. In an array-based implementation,
	 * it takes constant time.
	 * @return The maximum priority element in our queue, or null if the queue is empty.
	 */
	public T last() {
		return data.get(data.size()-1).getData();
	}

	/**
	 * Inspects whether a given element is in the queue. O(N) complexity.
	 * @param element The element to search for.
	 * @return {@code true} iff {@code element} is in {@code this}, {@code false} otherwise.
	 */
	public boolean contains(T element)
	{
		for (PriorityQueueNode<T> elt : data) {
			if (elt.getData().equals(element))
				return true;
		}
		return false;
	}

	@Override
	public int size() {
		return data.size();
	}

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Iterator<T> iterator() {
		return (Iterator<T>) data.iterator();
	}
}
