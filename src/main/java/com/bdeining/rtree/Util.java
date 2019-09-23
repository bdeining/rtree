package com.bdeining.rtree;

import com.conversantmedia.util.collection.geometry.Point2d;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import java.util.Random;

public class Util {

  private static Random random = new Random();

  public static Double randomDouble(int rangeMin, int rangeMax) {
    return rangeMin + (rangeMax - rangeMin) * random.nextDouble();
  }

  public static Point2d convertWktToPoint2D(String wkt) {
    WKTReader wktReader = new WKTReader();
    Geometry geometry = null;
    try {
      geometry = wktReader.read(wkt);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    if (geometry instanceof Point) {
      Point point = (Point) geometry;
      return new Point2d(point.getX(), point.getY());
    }

    return null;
  }
}
