package lib280.tree;

import jdk.dynalink.NamedOperation;
import lib280.base.NDPoint280;

public class KDNode280 <I extends Comparable<? super I >>{

    public NDPoint280 item;

    public KDNode280<I> leftChild;

    public  KDNode280<I> rightChild;

    public KDNode280(){
        this.item = null;

    }

    public boolean isEmpty(){
        return item==null;
    }

    public NDPoint280 item (){
        return item;
    }
    public KDNode280 getLeftChild(){
        return leftChild;
    }
    public KDNode280 getRightChild(){
        return rightChild;
    }



}
