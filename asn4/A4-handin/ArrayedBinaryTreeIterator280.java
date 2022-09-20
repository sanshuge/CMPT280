package lib280.tree;

import lib280.base.LinearIterator280;
import lib280.exception.AfterTheEnd280Exception;
import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.NoCurrentItem280Exception;


/**
 name: Ruobing Fu
 NSID: xdt709
 student number: 11319234
 course number:CMPT-280-02
 */
public class ArrayedBinaryTreeIterator280<I extends Comparable<? super I>> extends ArrayedBinaryTreePosition280 implements LinearIterator280<I> {

	// This is a reference to the tree that created this iterator object.
	ArrayedBinaryTree280<I> tree;
	
	// An integer that represents the cursor position is inherited from
	// ArrayedBinaryTreePosition280.
	
	/**
	 * Create a new iterator from a given heap.
	 * @param t The heap for which to create a new iterator.
	 */
	public ArrayedBinaryTreeIterator280(ArrayedBinaryTree280<I> t) {
		super(t.currentNode);
		this.tree = t;
	}



	@Override
	public boolean before() {
		return this.currentNode == 0;
	}


	@Override
	public boolean after() {
		return this.currentNode> this.tree.count;

	}

	@Override
	public void goForth() throws AfterTheEnd280Exception {
		if (this.after()) throw new AfterTheEnd280Exception("The cursor has reached the end. ");
		this.currentNode ++;
	}

	@Override
	public void goFirst() throws ContainerEmpty280Exception {
		this.currentNode = 1;
	}

	@Override
	public void goBefore() {
		this.currentNode = 0;
	}

	@Override
	public void goAfter() {
		this.currentNode = this.tree.count+1;
	}

	@Override
	public I item() throws NoCurrentItem280Exception {
		 if(!tree.itemExists()) throw new NoCurrentItem280Exception("The item does not exist");
		 return tree.items[this.currentNode];
	}

	@Override
	public boolean itemExists() {
		return this.currentNode>0 && this.currentNode<=this.tree.count ;
	}
}
