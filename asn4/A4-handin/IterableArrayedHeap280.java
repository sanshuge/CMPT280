package lib280.tree;

import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.ItemNotFound280Exception;
import lib280.exception.NoCurrentItem280Exception;
/**
 name: Ruobing Fu
 NSID: xdt709
 student number: 11319234
 course number:CMPT-280-02
 */


public class IterableArrayedHeap280<I extends Comparable<? super I>> extends ArrayedHeap280<I> {

	/**
	 * Create an iterable heap with a given capacity.
	 * @param cap The maximum number of elements that can be in the heap.
	 */
	public IterableArrayedHeap280(int cap) {
		super(cap);
	}


	public ArrayedBinaryTreeIterator280<I> iterator() {
		// return a new iterator object for the tree

		return new ArrayedBinaryTreeIterator280<>(this);
	}

	public void deleteAtPosition(ArrayedBinaryTreeIterator280<I> i)throws ContainerEmpty280Exception, NoCurrentItem280Exception{
		// delete an item from an arrayed heap.
		// copy delete method from ArrayedHeap280 and make some slight changes
			if(this.isEmpty())
				throw new ContainerEmpty280Exception("Cannot delete an item from an empty heap.");

			// Delete the root by moving in the last item.
			// If there is more than one item, and we aren't deleting the last item,
			// copy the last item in the array to the current position.

			if( this.count > 1 ) {

				this.items[i.currentNode] = this.items[count];
				this.items[count] = null;
			}
			this.count--;

			// If we deleted the last remaining item, make the the current item invalid, and we're done.
			if( this.count == 0) {
				this.currentNode = 0;
			}

			// store the itertor's position to pos.
			int pos = i.currentNode;

			// While offset n has a left child...
			while( findLeftChild(pos) <= count ) {
				// Select the left child.
				int child = findLeftChild(pos);

				// If the right child exists and is larger, select it instead.
				if( child + 1 <= count && items[child].compareTo(items[child+1]) < 0 )
					child++;

				// If the parent is smaller than the root...
				if( items[pos].compareTo(items[child]) < 0 ) {
					// Swap them.
					I temp = items[pos];
					items[pos] = items[child];
					items[child] = temp;
					pos = child;
				}
				else return;

			}
		}
}