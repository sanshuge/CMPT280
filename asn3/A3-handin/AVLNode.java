package lib280.tree;

import java.security.PublicKey;

/**
 name: Ruobing Fu
 NSID: xdt709
 student number: 11319234
 course number:CMPT-280-02
 */

public class AVLNode<I extends Comparable<I>> extends BinaryNode280<I> {
    public int lheight;
    public int rheight;


    /**
     * constructor for a node with height of 1
     * @param x
     */
    public AVLNode(I x){
        super(x);


    }
    public AVLNode(AVLNode<I> lnode,I x,AVLNode<I> rnode){
        super(x);
        this.leftNode = lnode;
        this.rightNode = rnode;

    }

    public I item()
    {
        return item;
    }


    public AVLNode<I> getleftNode()
    {
        return (AVLNode<I>) super.leftNode();
    }

    public AVLNode<I> createNewNode(I x ) {

        BinaryNode280<I> newnode = new BinaryNode280<>(x);
        return (AVLNode<I>) newnode;
    }

    public AVLNode<I> getrightNode()
    {
        return (AVLNode<I>) super.rightNode();

    }

    public void setItem(I x) {
       super.setItem(x);
    }

    public void setLeftNode(AVLNode<I> n) {
        this.leftNode = n;
    }


    public void setRightNode(AVLNode<I> n) {
        this.rightNode = n;
    }

    public int getLheight(){return this.lheight;}
    public int getRheight(){return  this.rheight;}

    public void setLheight(int newheight){
        this.lheight = newheight;
    }

    public void setRheight(int newheight){
        this.rheight= newheight;
    }






}
