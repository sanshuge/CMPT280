package lib280.tree;

/**
 name: Ruobing Fu
 NSID: xdt709
 student number: 11319234
 course number:CMPT-280-02
 */


import com.sun.jdi.InterfaceType;
import lib280.base.Dispenser280;
import lib280.base.Searchable280;
import lib280.exception.ContainerFull280Exception;
import lib280.exception.DuplicateItems280Exception;
import lib280.exception.NoCurrentItem280Exception;

public class AVLtree280<I extends Comparable< I>> extends OrderedSimpleTree280<I> implements Searchable280<I>,Dispenser280<I>
{

//    public AVLNode<I> rootNode;
//
//    public AVLNode<I> cur;
//
//    public AVLNode<I> par;

    //using binary node
    protected BinaryNode280<I> cur;

    protected BinaryNode280<I> parent;

    protected boolean searchesContinue = false;



    public AVLtree280(){
        this.rootNode = null;
        super();

    }

    @Override
    protected AVLTreeNode280<I> createNewNode(I item) {
        return new AVLTreeNode280<>(item);
    }

    /**
     * return true if the tree is empty.
     * @return
     */
    public boolean isEmpty(){
        return this.rootNode == null;
    }

    public boolean isFull(){return false;}

    public I item throw NoCurrentItem280Exception{
        if (!itemExists()){throw new NoCurrentItem280Exception("There is no current item");}
        else {return cur.item();}
}

    /**
     *
     * @param node a given node
     * @return the height of the node
     */
    public int height(AVLNode<I> node){
        if (node == null) return 0;
        else {
            int lheight = height(node.getleftNode());
            int rheight = height(node.getrightNode());
            return Math.max(lheight,rheight)+1;
        }
    }

    /**
     * return the imbalance of the given node,
     * @param node
     * @return the imbalance of the node
     */

    public int getImbalance(AVLNode<I> node){
        if (node == null) return 0;
        return (height(node.getleftNode()) - height(node.getrightNode()));

    }


    /**
     * restore the AVL property
     * @param node
     */
    public void  restoreAVLproperty(AVLNode<I> node){
        int imbalance = getImbalance(node);
        // the node is not a critical node.
        if (Math.abs(imbalance) <=1) return ;
        //the node is a critical node.
        if (imbalance == 2){
            if (getImbalance(node.getleftNode())>0) {RightRotate(node);}
            else {LeftRotate(node.getleftNode());RightRotate(node);}}
        else {
            if (getImbalance(node.getrightNode())<=0) {LeftRotate(node);}
            else {RightRotate(node.getrightNode()); LeftRotate(node);}}


    }
    /*
              a              b
             / \            / \
            b   e          c   a
           / \            /   / \
          c   d          f   d   e
         /
        f

     */

    /***
     * perform the right rotation on node a
     * @param a
     */
    public void RightRotate(AVLNode<I> a){

        AVLNode<I> b = a.getleftNode();
        AVLNode<I> d = b.getrightNode();


        b.setRightNode(a);
        a.setLeftNode(d);

        a.height = 1+ Math.max(a.leftNode.height,a.rightNode.height);
        b.height = 1+ Math.max(b.leftNode.height,b.rightNode.height);


    }
     /*

              a                              c
             / \                            / \
            b   c                          a   e
               / \                        / \   \
              d   e                      b   d   f
                   \
                    f


     */

    /**
     * perform left rotation on node a
     * @param a
     */

    public void LeftRotate(AVLNode<I> a){
        AVLNode<I> c = a.getrightNode();
        AVLNode<I> d = c.getleftNode();

        c.setLeftNode(a);
        a.setRightNode(d);


        a.height = 1+ Math.max(a.lheight,a.rheight);
        c.height=1+ Math.max(c.lheight,c.rheight);


    }


    public void insert(I x, AVLNode<I> root){
        // create a new node
        AVLNode<I> newnode = new AVLNode<>(x);

        if (root == null){ root = newnode;}

        // if the x is smaller than the root, if the left node is empty, set the left node to the new node.
        // else continue recursing on the left node
        if (x.compareTo(root.item())<0){
            if (root.getleftNode() == null) {
                root.setLeftNode(newnode);
            }
            else insert(x,root.getleftNode());
            root.setLheight(height(root.getleftNode()));

        }
        // if the x is larger than the root
        else if  (x.compareTo(root.item())>0){
            if (root.getrightNode()==null){
                root.setRightNode(newnode);
            }
            else insert(x,root.getrightNode());
            root.setRheight(height(root.getrightNode()));
        }
        restoreAVLproperty(root);


    }


    public void delete(I x,AVLNode<I> root){
        if (root == null){throw new RuntimeException("cannot delete from an empty tree.");}
        // if the root item is less than x, find the
        if (root.item().compareTo(x)==0){
            if (root.getrightNode()==null && root.getrightNode()==null){
                root.setItem(null);}
            else if  (root.getrightNode()!=null && root.getrightNode()==null){
                root.setItem(root.getrightNode().item());
            }
            else {
                root.setItem(root.getleftNode().item());}}
        else {
            if (root.item().compareTo(x) < 0) {
                delete(x, root.getrightNode());
            } else if (root.item().compareTo(x) > 0) {
                delete(x, root.getleftNode());

            }

        }
        restoreAVLproperty(root);}


}

