package com.bdeining.rtree;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

import java.util.List;

public interface Node {

    Coordinate getCoordinate();

    List<Node> getChildren();

    void setChildren(List<Node> children);

}
