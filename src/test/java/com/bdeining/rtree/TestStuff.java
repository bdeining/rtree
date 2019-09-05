package com.bdeining.rtree;

public class TestStuff {

    public static void main(String[] args) {

        RTree rTree = new RTree(3);
        rTree.insert(12, 12);
        rTree.insert(9, 11);
        rTree.insert(6, 9);
        rTree.insert(4, 7);
        rTree.insert(3, 1);


    }




}
