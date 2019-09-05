package com.bdeining.rtree;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class RTree {

  /*
  Each leaf node has between 0.4B and B data points, where B ≥ 3
  is a parameter. The only exception applies when the leaf is the root,
  in which case it is allowed to have between 1 and B points. All the
  leaf nodes are at the same level.

  Each internal node has between 0.4B and B child nodes, except
  when the node is the root, in which case it needs to have at least 2
  child nodes.
   */

  private Node root;

  // B
  private int listSize;

  private GeometryFactory geometryFactory = new GeometryFactory();

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
      root = new PointNode();
      root.setIsRoot(true);
    }
    Coordinate coordinate = new Coordinate(lat, lon);
    insert(root, coordinate);
  }

  private void insert(Node root, Coordinate coordinate) {
    if (root.getChildren().isEmpty()) {

      root.addCoordinate(coordinate);

      if ((root.getCoordinates().size() + 1) == listSize) {
        handleOverflow(root);
      }

    } else {
      Node subtree = chooseSubtree(coordinate, root);
      insert(subtree, coordinate);
    }
  }

  /**
   * return the child whose MBR requires the minimum increase in perimeter to cover p.
   *
   * @param coordinate
   * @param root
   * @return
   */
  private Node chooseSubtree(Coordinate coordinate, Node root) {
    List<Node> children = root.getChildren();

    if (children.size() > 2) {
      return root;
    }

    /*List<Coordinate> coordinates = root.getCoordinates();



    LinearRing linearRing = geometryFactory.createLinearRing(coordinates.toArray(new Coordinate[0]));
    Polygon polygon = geometryFactory.createPolygon(linearRing);


    double mbr = polygon.getArea();

    System.out.println("MBR : " + mbr);

    for (Node child : coordinates) {
        List<Node> futureList = new ArrayList<>(coordinates);
        futureList.add(child);

        Coordinate[] futureCoordinates = (Coordinate[]) futureList.stream().map(Node::getCoordinates).toArray();

        LinearRing futureRing = geometryFactory.createLinearRing(futureCoordinates);
        Polygon futurePolygon = geometryFactory.createPolygon(futureRing);

        double futureMbr = futurePolygon.getArea();

        System.out.println("Future MBR : " + futureMbr);

    }




    return coordinates.get(0);*/
    return root;
  }

    /**
     *   split(u) into u and u
     *   if u is the root then
     *      create a new root with u and u as its child nodes
     *   else
     *      w ← the parent of u
     *      update MBR(u) in w
     *      add u' as a child of w
     *      if w overflows then
     *      handle-overflow(w)
     *
     * @param node u
     */
  private void handleOverflow(Node node) {

    Node[] splitNodes = split(node);

    if (splitNodes[0].isRoot()) {
      // create a new root with u and u' as its child nodes
      root = new PointNode();
      root.getChildren().add(splitNodes[0]);
      root.getChildren().add(splitNodes[1]);

    } else {
      Node newNode = new PointNode();
      newNode.getChildren().add(splitNodes[0]);
    }
  }

    /**
     *   m = the number of points in u
     *   sort the points of u on x-dimension
     *   for i = ciel0.4B to m − ciel0.4B
     *   S1 ← the set of the first i points in the list
     *   S2 ← the set of the other i points in the list
     *   calculate the perimeter sum of MBR(S1) and MBR(S2); record it
     *   if this is the best split so far
     *      Repeat Lines 2-6 with respect to y-dimension
     *      return the best split found
     *
     * @param node u
     * @return
     */
  private Node[] split(Node node) {
    int m = node.getCoordinates().size();

    List<Coordinate> sortedXCoordinates = node.getCoordinates();
    sortedXCoordinates.sort(new LatitudeComparator());

    for (double i = Math.ceil(0.4 * listSize); i < m - Math.ceil(0.4 * listSize); i++) {

      Set<Coordinate> s1;
      Set<Coordinate> s2;
    }

    return new Node[2];
  }

  private class LatitudeComparator implements Comparator<Coordinate> {
    @Override
    public int compare(Coordinate o1, Coordinate o2) {
      return Double.compare(o1.x, o2.x);
    }
  }

  private class LongitudeComparator implements Comparator<Coordinate> {
    @Override
    public int compare(Coordinate o1, Coordinate o2) {
      return Double.compare(o1.y, o2.y);
    }
  }
}
