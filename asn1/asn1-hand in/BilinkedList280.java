package lib280.list;
/**
 name: Ruobing Fu
 NSID: xdt709
 student number: 11319234
 course number:CMPT-280-02
 */


import jdk.swing.interop.SwingInterOpUtils;
import lib280.base.BilinearIterator280;
import lib280.base.CursorPosition280;
import lib280.base.Pair280;
import lib280.exception.*;

/**	This list class incorporates the functions of an iterated 
	dictionary such as has, obtain, search, goFirst, goForth, 
	deleteItem, etc.  It also has the capabilities to iterate backwards 
	in the list, goLast and goBack. */
public class BilinkedList280<I> extends LinkedList280<I> implements BilinearIterator280<I>
{
	/* 	Note that because firstRemainder() and remainder() should not cut links of the original list,
		the previous node reference of firstNode is not always correct.
		Also, the instance variable prev is generally kept up to date, but may not always be correct.  
		Use previousNode() instead! */

	/**	Construct an empty list.
		Analysis: Time = O(1) */
	public BilinkedList280()
	{
		super();
	}

	/**
	 * Create a BilinkedNode280 this Bilinked list.  This routine should be
	 * overridden for classes that extend this class that need a specialized node.
	 * @param item - element to store in the new node
	 * @return a new node containing item
	 */
	protected BilinkedNode280<I> createNewNode(I item)
	{

		return new BilinkedNode280<I>(item);
	}

	/**
	 * Insert element at the beginning of the list
	 * @param x item to be inserted at the beginning of the list 
	 */
	public void insertFirst(I x) 
	{
		// create a new item
		BilinkedNode280<I> newNode = createNewNode(x);
		// set the newnode's next node and previous node
		newNode.setNextNode(this.head);
		newNode.setPreviousNode(null);

		// when the list is empty,the head and tail point to the new node
		if (this.isEmpty()) {
			this.tail = newNode;
		}
		// when it's not empty, point the head's previous node to the newnode.
		else
		{
			((BilinkedNode280<I>) this.head).setPreviousNode(newNode);
		}
		// if the position is on the head, set the previous position to the new node.

		if(this.position==this.head)
		{
			this.prevPosition = newNode;
		}
		// set the head to the newnode.
		this.head = newNode;


		}


	/**
	 * Insert element at the beginning of the list
	 * @param x item to be inserted at the beginning of the list 
	 */
	public void insert(I x) 
	{
		this.insertFirst(x);
	}

	/**
	 * Insert an item before the current position.
	 * @param x - The item to be inserted.
	 */
	public void insertBefore(I x) throws InvalidState280Exception {
		if( this.before() ) throw new InvalidState280Exception("Cannot insertBefore() when the cursor is already before the first element.");
		
		// If the item goes at the beginning or the end, handle those special cases.
		if( this.head == position ) {
			insertFirst(x);  // special case - inserting before first element
		}
		else if( this.after() ) {
			insertLast(x);   // special case - inserting at the end
		}
		else {
			// Otherwise, insert the node between the current position and the previous position.
			BilinkedNode280<I> newNode = createNewNode(x);
			newNode.setNextNode(position);
			newNode.setPreviousNode((BilinkedNode280<I>)this.prevPosition);
			prevPosition.setNextNode(newNode);
			((BilinkedNode280<I>)this.position).setPreviousNode(newNode);
			
			// since position didn't change, but we changed it's predecessor, prevPosition needs to be updated to be the new previous node.
			prevPosition = newNode;			
		}
	}
	
	
	/**	Insert x before the current position and make it current item. <br>
		Analysis: Time = O(1)
		@param x item to be inserted before the current position */
	public void insertPriorGo(I x) 
	{
		this.insertBefore(x);
		this.goBack();
	}

	/**	Insert x after the current item. <br>
		Analysis: Time = O(1) 
		@param x item to be inserted after the current position */
	public void insertNext(I x) 
	{
		if (isEmpty() || before())
			insertFirst(x); 
		else if (this.position==lastNode())
			insertLast(x); 
		else if (after()) // if after then have to deal with previous node  
		{
			insertLast(x); 
			this.position = this.prevPosition.nextNode();
		}
		else // in the list, so create a node and set the pointers to the new node 
		{
			BilinkedNode280<I> temp = createNewNode(x);
			temp.setNextNode(this.position.nextNode());
			temp.setPreviousNode((BilinkedNode280<I>)this.position);
			((BilinkedNode280<I>) this.position.nextNode()).setPreviousNode(temp);
			this.position.setNextNode(temp);
		}
	}

	/**
	 * Insert a new element at the end of the list
	 * @param x item to be inserted at the end of the list 
	 */
	public void insertLast(I x) 
	
	{
		// create a new node
		BilinkedNode280<I> newNode = createNewNode(x);
		newNode.setNextNode(null);
		//if the list is empty, set the head to the new node

		if (this.isEmpty()){
			this.head = newNode;
		}

		else{
			newNode.setPreviousNode((BilinkedNode280<I> )this.tail);
			this.tail.setNextNode(newNode);

		}
		//if the cursor is after the list, set the previous position to the new node.

		if (after()){
			this.prevPosition = newNode;
		}
		//sets the tail to the new node.
		this.tail = newNode;

	}

	/**
	 * Delete the item at which the cursor is positioned
	 * @precond itemExists() must be true (the cursor must be positioned at some element)
	 */
	public void deleteItem() throws NoCurrentItem280Exception
	{

		// when the list is empty:
		if(this.isEmpty()){
			throw new  ContainerEmpty280Exception("Cannot delete from an empty list.");
		}
		// when there is no item on this position
		if (!this.itemExists()){
			throw new ItemNotFound280Exception("Item to be deleted wasn't in the list.");

		}
		//general cases
		else {
			if (this.position==this.head){
				deleteFirst();

			}
			else if (this.position==this.tail){
				deleteLast();

			}
			else{
				this.prevPosition.setNextNode(this.position.nextNode);
			}

		}

	}

	
	@Override
	public void delete(I x) throws ItemNotFound280Exception {
		if( this.isEmpty() ) throw new ContainerEmpty280Exception("Cannot delete from an empty list.");

		// Save cursor position
		LinkedIterator280<I> savePos = this.currentPosition();
		
		// Find the item to be deleted.
		search(x);
		if( !this.itemExists() ) throw new ItemNotFound280Exception("Item to be deleted wasn't in the list.");

		// If we are about to delete the item that the cursor was pointing at,
		// advance the cursor in the saved position, but leave the predecessor where
		// it is because it will remain the predecessor.
		if( this.position == savePos.cur ) savePos.cur = savePos.cur.nextNode();
		
		// If we are about to delete the predecessor to the cursor, the predecessor 
		// must be moved back one item.
		if( this.position == savePos.prev ) {
			
			// If savePos.prev is the first node, then the first node is being deleted
			// and savePos.prev has to be null.
			if( savePos.prev == this.head ) savePos.prev = null;
			else {
				// Otherwise, Find the node preceding savePos.prev
				LinkedNode280<I> tmp = this.head;
				while(tmp.nextNode() != savePos.prev) tmp = tmp.nextNode();
				
				// Update the cursor position to be restored.
				savePos.prev = tmp;
			}
		}
				
		// Unlink the node to be deleted.
		if( this.prevPosition != null)
			// Set previous node to point to next node.
			// Only do this if the node we are deleting is not the first one.
			this.prevPosition.setNextNode(this.position.nextNode());
		
		if( this.position.nextNode() != null )
			// Set next node to point to previous node 
			// But only do this if we are not deleting the last node.
			((BilinkedNode280<I>)this.position.nextNode()).setPreviousNode(((BilinkedNode280<I>)this.position).previousNode());
		
		// If we deleted the first or last node (or both, in the case
		// that the list only contained one element), update head/tail.
		if( this.position == this.head ) this.head = this.head.nextNode();
		if( this.position == this.tail ) this.tail = this.prevPosition;
		
		// Clean up references in the node being deleted.
		this.position.setNextNode(null);
		((BilinkedNode280<I>)this.position).setPreviousNode(null);
		
		// Restore the old, possibly modified cursor.
		this.goPosition(savePos);
		
	}
	/**
	 * Remove the first item from the list.
	 * @precond !isEmpty() - the list cannot be empty
	 */
	public void deleteFirst() throws ContainerEmpty280Exception
	{

		if( this.isEmpty() )
		{
			throw new ContainerEmpty280Exception("Cannot delete from an empty list.");
		}
		// when there is only one element in the list

		else if (this.head== this.tail) {
			this.head = null;
			this.tail = null;
		}
		//general cases
		else{
			// if the cursor is on the first element,move it to the next node

			if(this.position == this.head){
				this.position = this.position.nextNode;


			} //if the cursor is on the second element,
			else if (this.prevPosition == this.head) {
				this.prevPosition = null;

			}
			BilinkedNode280<I> prevhead = (BilinkedNode280<I>)this.head;
			this.head = this.head.nextNode();
			prevhead.setNextNode(null);


		}

			}





	/**
	 * Remove the last item from the list.
	 * @precond !isEmpty() - the list cannot be empty
	 */
	public void deleteLast() throws ContainerEmpty280Exception
	{

		// when the list is empty:
		if( this.isEmpty() )
		{
			throw new ContainerEmpty280Exception("Cannot delete from an empty list.");
		}
		// when there is only one element in the list

		else if (this.head== this.tail) {

			deleteFirst();
		}
		//general cases:
		else{

			// if the cursor is after the tail: move the cursor to the tail's previous element

			if (this.prevPosition == this.tail){
				this.prevPosition =((BilinkedNode280<I>) this.tail).previousNode;

			} // if the cursor is on the last element:

			else if (this.position == this.tail) {
				this.position = ((BilinkedNode280<I>) this.position).previousNode;
				this.prevPosition = ((BilinkedNode280<I>) this.prevPosition).previousNode;


			}
			//set this tail to previous's node's last node and this tail's next node to null.
			this.tail = ((BilinkedNode280<I>)this.tail).previousNode;
			this.tail.setNextNode(null);

		}

	}

	
	/**
	 * Move the cursor to the last item in the list.
	 * @precond The list is not empty.
	 */
	public void goLast() throws ContainerEmpty280Exception
	{

		// if the list is empty:
		if( this.isEmpty() )
		{
			throw new ContainerEmpty280Exception("Cannot delete from an empty list.");
		}
		// gerenal cases
		//set thus position to the tail, and previous position to the element before the tail.

		this.position = this.tail;
		this.prevPosition= ((BilinkedNode280<I>) this.position).previousNode();


	}
  
	/**	Move back one item in the list. 
		Analysis: Time = O(1)
		@precond !before() 
	 */
	public void goBack() throws BeforeTheStart280Exception {

		// when the cursor is before the first element
		if (this.before()) {
			throw new InvalidState280Exception("Cannot goBack() when the cursor is already before the first element.");

		}
		// if the cursor is after the last element:
		if (this.after()) {
			goLast();
		}
		else
		{
			this.position = ((BilinkedNode280<I>) this.position).previousNode();

			if (this.position != null)
			{
				// if the position is on head, the previous location is null.

				if (this.position == this.head)
				{
					this.prevPosition = null;
				}
				// if the position is not the head
				else
				{
					this.prevPosition = ((BilinkedNode280<I>) this.position).previousNode();
				}
			}

	}}




	/**	Iterator for list initialized to first item. 
		Analysis: Time = O(1) 
	*/
	public BilinkedIterator280<I> iterator()
	{
		return new BilinkedIterator280<I>(this);
	}

	/**	Go to the position in the list specified by c. <br>
		Analysis: Time = O(1) 
		@param c position to which to go */
	@SuppressWarnings("unchecked")
	public void goPosition(CursorPosition280 c)
	{
		if (!(c instanceof BilinkedIterator280))
			throw new InvalidArgument280Exception("The cursor position parameter" 
					    + " must be a BilinkedIterator280<I>");
		BilinkedIterator280<I> lc = (BilinkedIterator280<I>) c;
		this.position = lc.cur;
		this.prevPosition = lc.prev;
	}

	/**	The current position in this list. 
		Analysis: Time = O(1) */
	public BilinkedIterator280<I> currentPosition()
	{
		return  new BilinkedIterator280<I>(this, this.prevPosition, this.position);
	}

	
  
	/**	A shallow clone of this object. 
		Analysis: Time = O(1) */
	public BilinkedList280<I> clone() throws CloneNotSupportedException
	{
		return (BilinkedList280<I>) super.clone();
	}


	/* Regression test. */
	public static void main(String[] args) {

		BilinkedList280<Double> B = new BilinkedList280<>();
		Double x;

		if( !B.isEmpty() )
			System.out.println("Error: Newly created list should be empty, but is not.");

		//test cases for insertFirst()
		B.insertFirst(1.0);
		try{
			x = B.firstItem();
			if (x!= 1.0){
				System.out.println("Error: Expected first list item should be 1.0 , got: "+ x);
			}
		}
		catch(RuntimeException e){
			System.out.println(" Error: unexpected exception occurred while testing insertFirst().");
		}

		B.insertFirst(2.0);
		try{
			 x = B.firstItem();
			if (x!= 2.0){
				System.out.println("Error: Expected first list item should be 2.0 , got: "+ x);
			}
		}
		catch(RuntimeException e){
			System.out.println(" Error: unexpected exception occurred while testing insertFirst().");
		}

		B.insertFirst(3.0);
		try{
			x = B.firstItem();
			if (x!= 3.0){
				System.out.println("Error: Expected first list item should be 3.0 , got: "+ x);
			}


		}	catch(RuntimeException e){
			System.out.println(" Error: unexpected exception occurred while testing insertFirst().");
		}

		//test cases for deleteFirst()
		// now we have three elements in the doubly linked list which are 3-2-1
		B.deleteFirst();
		try{
			x = B.firstItem();
			if (x!= 2.0){
				System.out.println("Error: Expected first list item should be 2.0 , got: "+ x);
			}


		}	catch(RuntimeException e){
			System.out.println(" Error: unexpected exception occurred while testing deleteFirst().");
		}
		B.deleteFirst();
		try{
			x = B.firstItem();
			if (x!= 1.0){
				System.out.println("Error: Expected first list item should be 1.0 , got: "+ x);
			}


		}	catch(RuntimeException e){
			System.out.println(" Error: unexpected exception occurred while testing deleteFirst().");
		}
		B.deleteFirst();// the list should be empty
		try{

			if (!B.isEmpty()){
				System.out.println("Error: Expected  list item should be empty. " );
			}


		}	catch(RuntimeException e){
			System.out.println(" Error: unexpected exception occurred while testing deleteFirst().");
		}

		// try to delete one item from an empty list.
		try {
			B.deleteFirst();
			System.out.println("Error: An exception was expected and not caught while calling deleteFirst() on empty list.");
		}
		catch (RuntimeException e) {
			// RuntimeException expected, do nothing.
		}



//		//test cases for insertLast()
		B.insertLast(4.5);
		try{
			 x = B.lastItem();
			if (x!= 4.5){
				System.out.println("Error: Expected first list item should be 4.5, got: "+ x);
			}


		}	catch(RuntimeException e){
			System.out.println(" Error: unexpected exception occurred while testing insertLast().");
		}
		B.insertLast(6.5);
		try{
			 x = B.lastItem();
			if (x!= 6.5){
				System.out.println("Error: Expected first list item should be 6.5, got: "+ x);
			}


		}	catch(RuntimeException e){
			System.out.println(" Error: unexpected exception occurred while testing insertLast().");
		}
		B.insertLast(8.5);
		try{
			x = B.lastItem();
			if (x!= 8.5){
				System.out.println("Error: Expected first list item should be 8.5, got: "+ x);
			}


		}	catch(RuntimeException e){
			System.out.println(" Error: unexpected exception occurred while testing insertLast().");
		}

		//test cases for deleteLast()
		// now we have three elements in the doubly linked list which are 4.5----6.5-----8.5

		try{
			B.deleteLast();
			 x = B.lastItem();
			if (x!= 6.5){
				System.out.println("Error: Expected last list item should be 6.5 , got: "+ x);
			}


		}	catch(RuntimeException e){
			System.out.println(" Error: unexpected exception occurred while testing deleteLast().");
		}


		try{
			B.deleteLast();
			 x = B.lastItem();
			if (x!= 4.5){
				System.out.println("Error: Expected last list item should be 4.5 , got: "+ x);
			}


		}	catch(RuntimeException e){
			System.out.println(" Error: unexpected exception occurred while testing deleteLast().");
		}

		try{
			B.deleteLast();
			if (!B.isEmpty()){
				System.out.println("Error: after testing deleteLast() the list should be empty, but it is not.");
			}


		}	catch(RuntimeException e){
			System.out.println(" Error: unexpected exception occurred while testing deleteLast().");
		}

		try {
			B.deleteLast();
			System.out.println("Error: An exception was expected and not caught while calling deleteLast() on empty list.");
		}
		catch (RuntimeException e) {
			// RuntimeException expected, do nothing.
		}


		//test cases for deleteitem()


		B.insertFirst(2.0);
		B.insertFirst(3.0);
		B.insertFirst(4.0);
		B.insertFirst(5.0);


		// the list is 5--4--3--2

		B.goFirst();// the cursor is on 5

		B.deleteItem(); // the list is 4-3-2
		try{
			 x = B.firstItem();
			if (x!= 4.0){
				System.out.println("Error: Expected first list item should be 4.0 , got: "+ x);
			}


		}	catch(RuntimeException e){
			System.out.println(" Error: unexpected exception occurred while testing deleteItem().");
		}

		B.deleteItem();//the list is 3-2
		try{
			 x = B.firstItem();
			if (x!= 3.0){
				System.out.println("Error: Expected first list item should be 3.0 , got: "+ x);
			}


		}	catch(RuntimeException e){
			System.out.println(" Error: unexpected exception occurred while testing deleteItem().");
		}

		B.deleteItem(); //the list is 2
		try{
			x = B.firstItem();
			if (x!= 2.0){
				System.out.println("Error: Expected first list item should be 2.0 , got: "+ x);
			}

		}	catch(RuntimeException e){
			System.out.println(" Error: unexpected exception occurred while testing deleteItem().");
		}
		B.deleteItem();//the list is empty
		try{

			if (!B.isEmpty()){
				System.out.println("Error: after testing deleteItem() the list should be empty, but it is not.");
			}


		}	catch(RuntimeException e){
			System.out.println(" Error: unexpected exception occurred while testing deleteItem().");
		}


//       test cases for goLast() and goBack()
		B.insertFirst(1.0);
		B.insertFirst(2.0);
		B.insertFirst(3.0);
		B.insertFirst(4.0);
		//the list is 4-3-2-1
		B.goLast();
		try{
			 x = B.item();
			if (x!= 1.0){
				System.out.println("Error: Expected first list item should be 1.0 , got: "+ x);
			}


		}
		catch(RuntimeException e){
			System.out.println(" Error: unexpected exception occurred while testing goLast().");
		}

		B.goBack();

		try{
			 x = B.item();
			if (x!= 2.0){
				System.out.println("Error: Expected first list item should be 2.0 , got: "+ x);
			}


		}
		catch(RuntimeException e){
			System.out.println(" Error: unexpected exception occurred while testing goBack().");
		}

		B.goBack();

		try{
			x = B.item();
			if (x!= 3.0){
				System.out.println("Error: Expected first list item should be 3.0 , got: "+ x);
			}
		}
		catch(RuntimeException e){
			System.out.println(" Error: unexpected exception occurred while testing goBack().");
		}
		B.goBack();

		try{
			 x = B.item();
			if (x!= 4.0){
				System.out.println("Error: Expected first list item should be 4.0 , got: "+ x);
			}
		}
		catch(RuntimeException e){
			System.out.println(" Error: unexpected exception occurred while testing goBack().");
		}

		B.goLast();
		try{
			x = B.item();
			if (x!= 1.0){
				System.out.println("Error: Expected first list item should be 1.0 , got: "+ x);
			}
		}
		catch(RuntimeException e){
			System.out.println(" Error: unexpected exception occurred while testing goLast().");
		}


	}
	}