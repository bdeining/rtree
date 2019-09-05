package com.bdeining.rtree;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RTree {

    private Node root;

    private int listSize;

    private int MBR = 7;

    private GeometryFactory geometryFactory =  new GeometryFactory();

    public RTree(int listSize) {
        this.listSize = listSize;
    }

    /*
    Algorithm insert(u, p)
1. if u is a leaf node then
2. add p to u
3. if u overflows then
/* namely, u has B + 1 points
4. handle-overflow(u)
5. else
6. v ← choose-subtree(u, p)
    which subtree under u should we insert p into?
7. insert(v, p)

     */

     public void insert(double lat, double lon) {
         if (root == null) {
             root = new PointNode(lat, lon);
         } else {
             Node node = new PointNode(lat, lon);
             insert(root, node);
         }

     }

    private void insert(Node root, Node node) {
        if (root.getChildren() == null) {
            List<Node> children = new ArrayList<>();
            children.add(node);
            root.setChildren(children);

        // if overflow
        if ((root.getChildren().size() + 1) == listSize) {
            // handle overflow
        }


        } else {
            Node subtree = chooseSubtree(node, root.getChildren());
            insert(subtree, node);
        }


    }

    /**
     * return the child whose MBR requires the
     * minimum increase in perimeter to cover p.
     *
     * @param node
     * @param children
     * @return
     */
    private Node chooseSubtree(Node node, List<Node> children) {

        Coordinate[] coordinates = children.stream().map(Node::getCoordinate).toArray(Coordinate[]::new);

        LinearRing linearRing = geometryFactory.createLinearRing(coordinates);
        Polygon polygon = geometryFactory.createPolygon(linearRing);


        double mbr = polygon.getArea();

        System.out.println("MBR : " + mbr);

        for (Node child : children) {
            List<Node> futureList = new ArrayList<>(children);
            futureList.add(child);

            Coordinate[] futureCoordinates = (Coordinate[]) futureList.stream().map(Node::getCoordinate).toArray();

            LinearRing futureRing = geometryFactory.createLinearRing(futureCoordinates);
            Polygon futurePolygon = geometryFactory.createPolygon(futureRing);

            double futureMbr = futurePolygon.getArea();

            System.out.println("Future MBR : " + futureMbr);

        }




        return node;

    }


}
