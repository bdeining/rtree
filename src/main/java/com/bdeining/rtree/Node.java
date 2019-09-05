package com.bdeining.rtree;

import com.vividsolutions.jts.geom.Coordinate;
import java.util.List;

public interface Node {

  List<Coordinate> getCoordinates();

  List<Node> getChildren();

  void setChildren(List<Node> children);

  void addCoordinate(Coordinate coordinate);

  boolean isRoot();

  void setIsRoot(boolean isRoot);

  void setMbr(int mbr);

  int getMbr();
}
