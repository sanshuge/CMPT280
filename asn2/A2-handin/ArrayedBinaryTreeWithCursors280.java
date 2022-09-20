package lib280.tree;

import lib280.base.Cursor280;
import lib280.base.CursorPosition280;
import lib280.dictionary.Dict280;
import lib280.exception.*;

/**
 name: Ruobing Fu
 NSID: xdt709
 student number: 11319234
 course number:CMPT-280-02
 */


public class ArrayedBinaryTreeWithCursors280<I> extends
		ArrayedBinaryTree280<I> implements Dict280<I>, Cursor280<I> {

	protected boolean searchesRestart;

	public ArrayedBinaryTreeWithCursors280(int cap) {
		super(cap);
		searchesRestart = true;
	}

	@Override
	public I obtain(I y) throws ItemNotFound280Exception {
		CursorPosition280 saved = this.currentPosition();
		this.goFirst();
		while (this.itemExists()) {
			if (membershipEquals(this.item(), y)) {
				I found = this.item();
				this.goPosition(saved);
				return found;
			}
			this.goForth();
		}
		this.goPosition(saved);
		throw new ItemNotFound280Exception("The given item could not be found.");
	}


	@Override
	// insert the element at the end of the array

	public void insert(I x) throws ContainerFull280Exception, DuplicateItems280Exception {
		if (this.isFull()) throw new ContainerFull280Exception("The tree is full,can not insert a new element");
		if (this.isEmpty()) this.items[1] = x;
		else this.items[this.count + 1] = x;
		this.count++;

	}


	//delete the item where the cursor is pointed at
	@Override
	public void deleteItem() throws NoCurrentItem280Exception {

		if (isEmpty()){ throw new NoCurrentItem280Exception("Container is empty"); }
		if (this.itemExists()) {

			this.items[currentNode] = this.items[count];
			this.items[count] =null;
			count--;
			if (this.currentNode == this.count+1) this.currentNode--;

		}
		else { throw new NoCurrentItem280Exception("No current item"); }
	}




	@Override
	public void delete(I x) throws ItemNotFound280Exception {
		if( this.isEmpty() ) throw new ContainerEmpty280Exception("Cannot delete from an empty list.");
		if (!this.has(x)) throw new ItemNotFound280Exception("The item to be deleted doesn't exist.");
		this.goFirst();
		while (this.itemExists()) {
			if (membershipEquals(this.item(), x))
				{
					this.items[this.currentNode] = this.items[this.count];
					this.items[this.count]=null;
					this.count--;
				}
				this.goForth();
			}

	}


	@Override
	public boolean has(I y) {

		boolean has = false;
		this.goFirst();
		while(this.itemExists()){
			if (this.membershipEquals(this.item(),y)){
				has = true;
			}
			this.goForth();
		}

		return has;
	}


	@Override
	public boolean membershipEquals(I x, I y) {
		return x.equals(y);
	}

	@Override
	public void search(I x) {
		this.goFirst();
		while(this.itemExists()) {
			if (membershipEquals(this.item(), x)) return;
			else this.goForth();
		}

	}


	@Override
	public boolean before() {

		return this.currentNode == 0; // this line is a placeholder to prevent a compiler error.  Remove as needed.
	}

	@Override
	public boolean after() {

		if (this.isEmpty()) return true;

		else return this.currentNode >= this.count+1;
	}

	@Override
	public void goForth() throws AfterTheEnd280Exception {
		if (after()) throw new AfterTheEnd280Exception("The cursor is already after all the elements");

		else this.currentNode ++;


	}

	@Override
	public void goFirst() throws ContainerEmpty280Exception {
		if (this.isEmpty()) throw new ContainerEmpty280Exception("Can not go to the first element in an empty tree.");

		else this.currentNode = 1;

	}

	@Override
	public void goBefore() {
		this.currentNode = 0;


	}

	@Override
	public void goAfter() {
		if (this.isEmpty()) this.goBefore();
		else this.currentNode = this.count+1;


	}

	@Override
	public void restartSearches() {
		this.searchesRestart = true;
	}

	@Override
	public void resumeSearches() {
		this.searchesRestart = false;
	}

	@Override
	public CursorPosition280 currentPosition() {
		return new ArrayedBinaryTreePosition280(this.currentNode);
	}

	@Override
	public void goPosition(CursorPosition280 c) {
		if (!(c instanceof ArrayedBinaryTreePosition280))
			throw new InvalidArgument280Exception("The cursor position parameter"
					+ " must be a ArrayedBinaryTreePosition280<I>");

		this.currentNode = ((ArrayedBinaryTreePosition280) c).currentNode;
	}

	/**
	 * Move the cursor to the parent of the current node.
	 *
	 * @throws InvalidState280Exception when the cursor is on the root already.
	 * @precond Current node is not the root.
	 */
	public void parent() throws InvalidState280Exception {

		if (this.currentNode == 1) throw new InvalidState280Exception("The cursor is on the root already.");

		else this.currentNode = this.currentNode/2;
	}

	/**
	 * Move the cursor to the left child of the current node.
	 *
	 * @throws ContainerEmpty280Exception if the tree is empty.
	 * @throws InvalidState280Exception   if the current node has no left child.
	 * @precond The tree must not be empty and the current node must have a left child.
	 */
	public void goLeftChild() throws InvalidState280Exception, ContainerEmpty280Exception {

		if (this.isEmpty()){
			throw new ContainerEmpty280Exception("The array is empty.");
		}
		else{
		if(this.currentNode*2>count) throw new InvalidState280Exception(" The node does not have left kid.");
		else this.currentNode = this.currentNode * 2;}

	}

	/**
	 * Move the cursor to the right child of the current node.
	 *
	 * @throws ContainerEmpty280Exception if the tree is empty.
	 * @throws InvalidState280Exception   if the current item has no right child.
	 * @precond The tree must not be empty and the current node must have a right child.
	 */
	public void goRightChild() throws InvalidState280Exception, ContainerEmpty280Exception {

		if (this.isEmpty()){
			throw new ContainerEmpty280Exception("The array is empty.");
		}
		else{
			if(this.currentNode*2+1>count) throw new InvalidState280Exception(" The node does not have left kid.");
			else this.currentNode = this.currentNode * 2+1;}

	}

	/**
	 * Move the cursor to the sibling of the current node.
	 *
	 * @throws ContainerEmpty280Exception if the tree is empty.
	 * @throws InvalidState280Exception   if the current item has no sibling.
	 * @precond The current node must have a sibling.  The tree must not be empty.
	 */
	public void goSibling() throws InvalidState280Exception, ContainerEmpty280Exception {
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("The array is empty.");
		}
		if (this.currentNode % 2 == 0) {
			parent();
			goRightChild(); // sibling
		} else {
			parent();
			goLeftChild();
		}
	}



	/**
	 * Move the cursor to the root of the tree.
	 *
	 * @throws ContainerEmpty280Exception if the tree is empty.
	 * @precond The tree must not be empty.
	 */
	public void root() throws ContainerEmpty280Exception {
		if (this.isEmpty()){
			throw new ContainerEmpty280Exception("The array is empty.");
		}

		this.currentNode = 1;
	}


	public static void main(String[] args) {
		ArrayedBinaryTreeWithCursors280<Integer> T = new ArrayedBinaryTreeWithCursors280<Integer>(10);

		// IsEmpty on empty tree.
		if(!T.isEmpty()) System.out.println("Test of isEmpty() on empty tree failed.");

		// test goAfter() when the tree is empty.
		T.goAfter();
		if(!T.before()) System.out.println("Cursor should be before in an empty tree, but it isn't.");
		if(!T.after()) System.out.println("Cursor should be after() in an empty tree but it isn't.");


		// Test root() on empty tree.
		Exception x = null;
		try {
			T.root();
		}
		catch(ContainerEmpty280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to root of empty tree.  Got none.");
		}

		// test goFirst() on empty tree
		x = null;
		try {
			T.goFirst();
		}
		catch(ContainerEmpty280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to first elelement of empty tree.  Got none.");
		}



		// Test goLeftChild() on empty tree.
		x = null;
		try {
			T.goLeftChild();
		}
		catch(ContainerEmpty280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to left child in empty tree.  Got none.");
		}

		// Test goLeftChild() on empty tree.
		x = null;
		try {
			T.goRightChild();
		}
		catch(ContainerEmpty280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to right child in empty tree.  Got none.");
		}


		// Check itemExists on empty tree
		if(T.itemExists() ) System.out.println("itemExists() returned true on an empty tree.");

		// Insert on empty tree.
		T.insert(1);

		// Check ItemExists on tree with one element.
		T.root();
		if(!T.itemExists() ) System.out.println("itemExists() returned false on a tree with one element with cursor at the root.");

		// isEmpty on tree with 1 element.
		if(T.isEmpty()) System.out.println("Test of isEmpty() on non-empty tree failed.");

		// Insert on tree with 1 element
		T.insert(2);

		// Insert some more elements
		for(int i=3; i <= 10; i++) T.insert(i);

		if(T.count() != 10  ) System.out.println("Expected tree count to be 10, got "+ T.count());


		// Test for isFull on a full tree.
		if(!T.isFull()) System.out.println("Test of isFull() on a full tree failed.");

		// Test insert on a full tree
		x = null;
		try {
			T.insert(11);
		}
		catch(ContainerFull280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception inserting into a full tree.  Got none.");
		}


		// Explicitly test search():

		// Search for item at root:
		T.search(1);
		if( !T.itemExists() )
			System.out.println("Error: search for item 1 failed when 1 is in the tree.");
		if( T.item() != 1)
			System.out.println("Error: search for item 1 did not result in cursor being at item 1.");


		// Search for item in middle
		T.search(5);
		if( !T.itemExists() )
			System.out.println("Error: search for item 5 failed when 5 is in the tree.");
		if( T.item() != 5)
			System.out.println("Error: search for item 5 did not result in cursor being at item 5.");

		// Search for item at end of array
		T.search(10);
		if( !T.itemExists() )
			System.out.println("Error: search for item 10 failed when 10 is in the tree.");
		if( T.item() != 10)
			System.out.println("Error: search for item 10 did not result in cursor being at item 10.");


		// Test positioning methods

		// Test root()
		T.root();
		if( T.item() != 1 ) System.out.println("Expected item at root to be 1, got " + T.item());

		T.goLeftChild();

		if( T.item() != 2 ) System.out.println("Expected current item to be 2, got " + T.item());

		T.goRightChild();
		if( T.item() != 5 ) System.out.println("Expected current item to be 5, got " + T.item());


		T.goLeftChild();
		if( T.item() != 10 ) System.out.println("Expected current item to be 10,  got " + T.item());

		// Current node now has no children.
		x = null;
		try {
			T.goLeftChild();
		}
		catch( InvalidState280Exception e ) {
			x = e;
		}
		finally {
			if( x == null) System.out.println("Expected exception moving to left child of a leaf.  Got none.");
		}

		x = null;
		try {
			T.goRightChild();
		}
		catch( InvalidState280Exception e ) {
			x = e;
		}
		finally {
			if( x == null) System.out.println("Expected exception moving to right child of a leaf.  Got none.");
		}

		// Remove the last item ( a leaf)
		T.deleteItem();
		if( T.item() != 9 ) System.out.println("Expected current item to be 9, got " + T.item());

		T.parent();


		// Remove a node with 2 children.  The right child 9 gets promoted.
		T.deleteItem();
		if( T.item() != 9 ) System.out.println("Expected current item to be 9, got " + T.item());


		// Remove a node with 1 child.  The left child 8 gets promoted.
		T.deleteItem();
		if( T.item() != 8 ) System.out.println("Expected current item to be 8, got " + T.item());

		// Remove the root successively.  There are 7 items left.
		T.root();
		T.deleteItem();
		if( T.item() != 7 ) System.out.println("Expected root to be 7, got " + T.item());

		T.deleteItem();
		if( T.item() != 6 ) System.out.println("Expected root to be 6, got " + T.item());

		T.deleteItem();
		if( T.item() != 5 ) System.out.println("Expected root to be 5, got " + T.item());

		T.deleteItem();
		if( T.item() != 8 ) System.out.println("Expected root to be 8, got " + T.item());

		T.deleteItem();
		if( T.item() != 3 ) System.out.println("Expected root to be 3, got " + T.item());

		T.deleteItem();
		if( T.item() != 2 ) System.out.println("Expected root to be 2, got " + T.item());


		// Tree has one item.  Try parent() on one item.
		x = null;
		try {
			T.parent();
		}
		catch( InvalidState280Exception e ) {
			x = e;
		}
		finally {
			if( x == null) System.out.println("Expected exception moving to parent of root.  Got none.");
		}


		// Try to go to the sibling
		x = null;
		try {
			T.goSibling();
		}
		catch(InvalidState280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to sibling when at the root.  Got none.");
		}




		T.deleteItem();


//		 Tree should now be empty
		if(!T.isEmpty()) System.out.println("Expected empty tree.  isEmpty() returned false.");

		if(T.capacity() != 10) System.out.println("Expected capacity to be 10, got "+ T.capacity());

		if(T.count() != 0  ) System.out.println("Expected tree count to be 0, got "+ T.count());

		// Remove from empty tree.
		x = null;
		try {
			T.deleteItem();
		}
		catch(NoCurrentItem280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception deleting from an empty tree.  Got none.");
		}



		// Try to go to the sibling
		x = null;
		try {
			T.goSibling();
		}
		catch(ContainerEmpty280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to sibling in empty tree tree.  Got none.");
		}


		T.insert(1);
		T.root();

		// Try to go to the sibling when there is no child.
		x = null;
		try {
			T.goSibling();
		}
		catch(InvalidState280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to sibling of node with no sibling.  Got none.");
		}

		T.goBefore();
		if(!T.before()) System.out.println("Error: Should be in 'before' position, but before() reports otherwise.");
		if(T.after()) System.out.println("Error: T.after() reports cursor in the after position when it should not be.");

		T.goForth();
		if(T.before()) System.out.println("Error: T.before() reports cursor in the before position when it should not be.");
		if(T.after()) System.out.println("Error: T.after() reports cursor in the after position when it should not be.");

		T.goForth();
		if(!T.after()) System.out.println("Error: Should be in 'after' position, but after() reports otherwise.");
		if(T.before()) System.out.println("Error: T.before() reports cursor in the before position when it should not be.");

		x=null;
		try {
			T.goForth();
		}
		catch(AfterTheEnd280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception advancing cursor when already after the end.  Got none.");
		}


		int y=-1;
		T.goBefore();
		try {
			y =  T.obtain(1);
		}
		catch( ItemNotFound280Exception e ) {
			System.out.println("Error: Unexpected exception occured when attempting T.obtain(1).");
		}
		finally {
			if(y != 1 ) System.out.println("Obtained item should be 1 but it isn't.");
			if(!T.before()) System.out.println("Error: cursor should still be in the before() position after T.obtain(1), but it isn't.");
		}

		if(!T.has(1)) System.out.println("Error: Tree has element 1, but T.has(1) reports that it does not.");


		T.insert(2);
		T.insert(3);
		T.insert(4);
		T.insert(5);
		T.insert(6);
		T.insert(7);

		// Test goSibling()
		try {
			T.goFirst();
			T.goLeftChild();
			T.goSibling();
		}
		catch (Exception e) {
			System.out.println("Error: unexpected exception attempting to move cursor to left child of root.");
		}
		finally {
			if(T.item() != 3) {
				System.out.println("Error: Cursor should be on 3 (sibling of 2) but it is not.");
			}
		}

		try {
			T.goSibling();
		}
		catch (Exception e) {
			System.out.println("Error: unexpected exception attempting to move cursor to left child of root.");
		}
		finally {
			if(T.item() != 2) {
				System.out.println("Error: Cursor should be on 2 (sibling of 3) but it is not.");
			}
		}

		// Explicitly test delete()

		// Test deleting root:
		T.delete(1);
		T.root();
		if( T.item() != 7) {
			System.out.println("Error: Tree state after deletion of root is incorrect.");
		}
		T.goPosition(new ArrayedBinaryTreePosition280(T.count()));
		if( T.item() != 6) {
			System.out.println("Error: Tree state after deletion of root is incorrect.");
		}

		// Test deleting leaf node.
		T.delete(5);
		T.goPosition(new ArrayedBinaryTreePosition280(T.count()));
		if( T.item() != 6) {
			System.out.println("Error: Tree state after deletion of 5 is incorrect.");
		}

		// Test deleting internal node.
		T.delete( 2);
		T.goPosition(new ArrayedBinaryTreePosition280(2));
		if( T.item() != 6 ) {
			System.out.println("Error: Tree state after deletion of 2 is incorrect.");
		}
		T.goPosition(new ArrayedBinaryTreePosition280(T.count()));
		if( T.item() != 4) {
			System.out.println("Error: Tree state after deletion of 2 is incorrect.");
		}


		if(T.count() != 4) {
			System.out.println("There should be 4 items in the tree now, but T.count() says otherwise.");
		}

		System.out.println("Regression test complete.");
	}

}
