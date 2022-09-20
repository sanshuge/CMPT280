package lib280.tree;

import java.util.HashMap;

class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {

        // index for nums1 which goes backwards

        int index = m+n-1;
        int s1 = m-1;
        int s2 = n-1;

        while (index>=0){
            if (s1<0){
                nums1[index] = nums2[s2];
                index--;
                s2--;
            }
            else if (s2<0){
                nums1[index] = nums1[s1];
                index--;
                s1--;}


            else if (nums1[s1] >  nums2[s2])
            {
                nums1[index] = nums1[s1];
                index--;
                s1--;}
            else  {
                nums1[index] = nums2[s2];
                index--;
                s2--;
            }

        }

    }


    public static void main(String[] args) {
    Solution s = new Solution();
    int[] s1 = {1,2,5,9,0,0,0};
    int[] s2 = {3,4,5};
    s.merge(s1,4,s2,3);
    for (int item:s1){
        System.out.println(item);

    }

    }
}

