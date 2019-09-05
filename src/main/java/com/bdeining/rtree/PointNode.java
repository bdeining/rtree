package com.bdeining.rtree;

import com.vividsolutions.jts.geom.Coordinate;
import java.util.ArrayList;
import java.util.List;

public class PointNode implements Node {

  private List<Coordinate> coordinates;

  private List<Node> children;

  private boolean isRoot;

  private int mbr;

  public PointNode() {
    this.coordinates = new ArrayList<>();
    this.children = new ArrayList<>();
  }

  @Override
  public List<Coordinate> getCoordinates() {
    return coordinates;
  }

  @Override
  public List<Node> getChildren() {
    return children;
  }

  @Override
  public void setChildren(List<Node> children) {
    this.children = children;
  }

  @Override
  public void addCoordinate(Coordinate coordinate) {
    coordinates.add(coordinate);
  }

  @Override
  public boolean isRoot() {
    return isRoot;
  }

  @Override
  public void setIsRoot(boolean isRoot) {
    this.isRoot = isRoot;
  }

  @Override
  public void setMbr(int mbr) {
    this.mbr = mbr;
  }

  @Override
  public int getMbr() {
    return mbr;
  }
}
