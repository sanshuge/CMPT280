package lib280.tree;

import com.sun.source.tree.Tree;
import lib280.base.Container280;
import lib280.base.NDPoint280;
import lib280.exception.ContainerEmpty280Exception;

import javax.print.attribute.standard.NumberOfDocuments;
import javax.swing.*;

public class KDTree280 implements Container280 {
    public KDNode280 rootNode;

    public  int dimension;


    /**
     * a constructor for an empty tree
     */
    public KDTree280(int d){
        this.rootNode = null;
        this.dimension = d;


    }


    @Override
    public boolean isEmpty() {
        return this.rootNode == null;
    }

    @Override
    public boolean isFull() {
        return false;
    }




    @Override
    public void clear() {
        setRootNode(null);
    }
    public void setRootNode(KDNode280 newRootNode){
        this.rootNode =  newRootNode;
    }

    public NDPoint280 getRootItem(){
        return rootNode.item();
    }


    /**
     * find the j-th smallest element in the array
     * @param list array of comparable elements
     * @param left offset of start of subarray for which we want the median element
     * @param right offset of end of subarray for which we want the median element
     * @param j we want to find the element that belongs at array index j
     * To find the median of the subarray between array indices ’left ’ and ’right ’,
     * @param d dimension of the tree
     */

    public void jSmaillest(NDPoint280[] list, int left, int right, int j, int d ){
        if (right > left){
            int pivotIndex = Partition(list,left,right,d);
            if (j<pivotIndex){
                jSmaillest(list,left,pivotIndex-1,j,d);
            }
            else if (j>pivotIndex){
                jSmaillest(list,pivotIndex+1,right,j,d);
            }
        }

    }

    /**
     * returns the offset at which the pivot element ended up
     * @param list  array of comparable elements
     * @param left  lower limit on subarray to be partitioned
     * @param right upper limit on subarray to be partitioned
     * @param d the dimension of the tree
     * @return
     */
    public int Partition (NDPoint280[] list , int left, int right, int d){

        double Pivot = list[right].idx(d);
        int swapOffset = left;
        for (int i = left;i<right;i++){

            if (list[i].idx(d) <= Pivot){
               NDPoint280 temp = list[i];
               list[i] =  list[swapOffset];
               list[swapOffset] = temp;
                swapOffset++;

            }

        }
        NDPoint280 temp = list[right];
        list[right] =  list[swapOffset];
        list[swapOffset] = temp;
        return swapOffset;

    }

    /**
     *
     * @param pointArray array of k- dimensional points
     * @param left offset of start of subarray from which to build a kd - tree
     * @param right offset of end of subarray from which to build a kd - tree
     * @param depth  the current depth in the partially built tree
     * @return a newly built tree node
     */
    public KDNode280 kdtree(NDPoint280[] pointArray, int left, int right, int depth){
        if (left>right) return null;
        else {
            int d  = depth % (this.dimension);
            int medianOffset = (left+right)/2;

            jSmaillest(pointArray,left,right,medianOffset,d);

            KDNode280 newnode = new KDNode280();
            newnode.item = pointArray[medianOffset];
            newnode.leftChild = kdtree(pointArray,left,medianOffset-1,depth+1);
            newnode.rightChild = kdtree(pointArray,medianOffset+1,right,depth+1);
            return newnode;


        }

    }

    /** return an array of points in the range
     * based on pseudocode on page 137
     * @param tree   subtree in which to search for elements
     * @param upper upper bound
     * @param lower lower bound
     * @param depth  level of the root of subtree T in the overall tree .
     * @return an array of points in the range
     */

    public NDPoint280[] rangeSearch(KDTree280 tree, NDPoint280 upper, NDPoint280 lower, int depth){
        if (tree.isEmpty()){
            return new NDPoint280[0];
        }
        int d = depth %  this.dimension;
        double splitValue = tree.getRootItem().idx(d);
        double max = upper.idx(d);
        double min = lower.idx(d);

        if(splitValue < min) {
            // in - range elements are in right subtree
            return rangeSearch(tree.rootRightSubtree(),upper,lower,depth+1);

        } else if (splitValue>max) {
            // in - range elements are in left subtree
            return rangeSearch(tree.rootLeftSubtree(),upper,lower,depth+1);

        }
        else {
            // in - range elements could exist in both subtrees .
            NDPoint280[] R = rangeSearch(tree.rootRightSubtree(),upper,lower,depth+1);
            NDPoint280[] L = rangeSearch(tree.rootLeftSubtree(),upper,lower,depth+1);

            boolean inRange = false;
            //if both coordinates of T. rootItem () are in range
            // add them to a set union;

            for (int i = 0;i<this.dimension;i++){

           if (tree.getRootItem().idx(i) >= lower.idx(i) && tree.getRootItem().idx(i) <= upper.idx(i))
           {
               inRange = true;
           }else
           {
               inRange = false;
               break;
           }

           }
            if(inRange){

               NDPoint280[] SetUnion = new NDPoint280[R.length+L.length+1];
               for (int i = 0;i<R.length;i++){
                   SetUnion[i] =  R[i];
               }
               for (int i = 0;i<L.length;i++){
                   SetUnion[R.length+i] =  L[i];
               }
               SetUnion[R.length+L.length] = tree.getRootItem();

               return SetUnion;

           }
           else {
               NDPoint280[] SetUnion = new NDPoint280[R.length+L.length];
               for (int i = 0;i<R.length;i++){
                   SetUnion[i] =  R[i];
               }
               for (int i = 0;i<L.length;i++){
                   SetUnion[R.length+i] =  L[i];
               }

               return SetUnion;

           }
        }

    }


    public KDTree280 clone()
    {
        try
        {
            return (KDTree280) super.clone();
        } catch(CloneNotSupportedException e)
        {
            /*	Should not occur because Container280 extends Cloneable */
            e.printStackTrace();
            return null;
        }
    }
    public KDTree280 rootRightSubtree()throws ContainerEmpty280Exception
    {
        if (isEmpty())
            throw new ContainerEmpty280Exception("Cannot return a subtree of an empty lib280.tree.");

        KDTree280 result = this.clone();
        result.clear();
        result.setRootNode(rootNode.getRightChild());
        return result;


    }
    public KDTree280 rootLeftSubtree()throws ContainerEmpty280Exception
    {
        if (isEmpty())
            throw new ContainerEmpty280Exception("Cannot return a subtree of an empty lib280.tree.");

        KDTree280 result = this.clone();
        result.clear();
        result.setRootNode(rootNode.getLeftChild());
        return result;


    }

    protected String toStringByLevel(int i){
        StringBuffer blanks = new StringBuffer((i - 1) * 5);
        for (int j = 0; j < i - 1; j++)
            blanks.append("            ");

        String result = new String();
        if (!isEmpty() && (rootNode.leftChild!=null || rootNode.rightChild!=null))
            result += rootRightSubtree().toStringByLevel(i + 1);

        result += "\n" + blanks + i + "/";

        if (isEmpty())
            result += "-";
        else
        {
            result += getRootItem();
            if (rootLeftSubtree()!=null || rootRightSubtree()!=null)
                result += rootLeftSubtree().toStringByLevel(i+1);
        }
        return result;
    }

    public String toString() {
        return this.toStringByLevel(1);
    }

public static void main(String[] args) {

        double[][] list ={
                {5.0,2.0},
                {9.0,10.0},
                {11.0,1.0},
                {4.0,3.0},
                {2.0,12.0},
                {3.0,7.0},
                {1.0,5.0}
        };
        NDPoint280[] array = new NDPoint280[list.length];
        for (int i = 0;i<list.length;i++){
            array[i] =  new NDPoint280(list[i]);

        }

    KDTree280 tree = new KDTree280(2);

        tree.rootNode = tree.kdtree(array,0, array.length-1, 0);

    System.out.println("The 2D lib280 . tree built from these points is:");

    System.out.println(tree);


    double[][] list1 ={
            {1.0,12.0,0.0},
            {18.0,1.0,2.0},
            {2.0,13.0,16.0},
            {7.0,3.0,3.0},
            {3.0,7.0,5.0},
            {16.0,4.0,4.0},
            {4.0,6.0,1.0},
            {5.0,5.0,17.0}
    };


    NDPoint280[] array1 = new NDPoint280[list1.length];
    for (int i = 0;i<list1.length;i++){
        array1[i] =  new NDPoint280(list1[i]);

    }

    KDTree280 tree1 = new KDTree280(3);

    tree1.rootNode = tree1.kdtree(array1,0, array1.length-1,0);

    System.out.println("The 3D lib280 . tree built from these points is:");
    System.out.println(tree1);


    NDPoint280 lower = new NDPoint280(new double[]{0.0, 1.0, 0.0});
    NDPoint280 upper = new NDPoint280(new double[]{4.0, 6.0, 3.0});
    NDPoint280[] res = tree1.rangeSearch(tree1,upper,lower,0);

    System.out.println("Looking for points between (0.0 , 1.0 , 0.0) and (4.0 , 6.0 , 3.0). Found :");
    for (int i = 0;i<res.length;i++){
        System.out.println(res[i]);
    }

    System.out.println("Looking for points between (0.0 , 1.0 , 0.0) and (8.0 , 7.0 , 4.0). Found :");

    NDPoint280 lower1 = new NDPoint280(new double[]{0.0, 1.0, 0.0});
    NDPoint280 upper1 = new NDPoint280(new double[]{8.0, 7.0, 4.0});
    NDPoint280[] res1 = tree1.rangeSearch(tree1,upper1,lower1,0);
    for (int i = 0;i<res1.length;i++){
        System.out.println(res1[i]);
    }
    System.out.println("Looking for points between (0.0 , 1.0 , 0.0) and (17.0 , 9.0 , 10.0).Found :");

    NDPoint280 lower2 = new NDPoint280(new double[]{0.0, 1.0, 0.0});
    NDPoint280 upper2 = new NDPoint280(new double[]{17.0, 9.0, 10.0});
    NDPoint280[] res2 = tree1.rangeSearch(tree1,upper2,lower2,0);
    for (int i = 0;i<res2.length;i++){
        System.out.println(res2[i]);
    }

}}
