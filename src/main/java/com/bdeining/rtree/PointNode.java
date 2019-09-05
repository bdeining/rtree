package com.bdeining.rtree;

import com.vividsolutions.jts.geom.Coordinate;

import java.util.ArrayList;
import java.util.List;

public class PointNode implements Node {

    private Coordinate coordinate;

    private List<Node> children;

    public PointNode(double lat, double lon) {
        this.coordinate = new Coordinate(lat, lon);
        this.children = new ArrayList<>();
    }

    @Override
    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public List<Node> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<Node> children) {
        this.children = children;
    }
}
