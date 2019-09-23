package com.bdeining.rtree;

import com.conversantmedia.util.collection.geometry.Point2d;
import com.conversantmedia.util.collection.geometry.Point2d.Builder;
import com.conversantmedia.util.collection.spatial.RectBuilder;
import com.conversantmedia.util.collection.spatial.SpatialSearch;
import com.conversantmedia.util.collection.spatial.SpatialSearches;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessor;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.coprocessor.RegionObserver;
import org.apache.hadoop.hbase.wal.WALEdit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RtreeCoprocessor implements RegionCoprocessor, RegionObserver {

  private Logger LOGGER = LoggerFactory.getLogger(RtreeCoprocessor.class);

  @Override
  public Optional<RegionObserver> getRegionObserver() {
    return Optional.of(this);
  }

  @Override
  public void prePut(
      ObserverContext<RegionCoprocessorEnvironment> context,
      Put put,
      WALEdit edit,
      Durability durability) {
    RegionCoprocessorEnvironment regionCoprocessorEnvironment = context.getEnvironment();
    Map<String, Object> sharedData = regionCoprocessorEnvironment.getSharedData();

    SpatialSearch<Point2d> rTree;
    if (!sharedData.containsKey("rTree")) {
      RectBuilder<Point2d> rectBuilder = new Builder();
      rTree = SpatialSearches.rTree(rectBuilder);

      sharedData.put("rTree", rTree);
      LOGGER.info("Creating R Tree");
    } else {
      rTree = (SpatialSearch<Point2d>) regionCoprocessorEnvironment.getSharedData().get("rTree");
    }

    String wkt = getWktFromMap(put.getFamilyCellMap());

    LOGGER.info("WKT : {}", wkt);

    if (wkt == null) {
      return;
    }

    Point2d point2d = Util.convertWktToPoint2D(wkt);

    if (point2d != null) {
      rTree.add(point2d);
      LOGGER.info("Adding test point {}", wkt);
    }

    // TODO : Stop put with invalid wkt
  }

  private String getWktFromMap(NavigableMap<byte[], List<Cell>> cells) {
    for (Map.Entry<byte[], List<Cell>> entry : cells.entrySet()) {
      List<Cell> cellList = entry.getValue();
      for (Cell cell : cellList) {
        String rowKey = new String(CellUtil.cloneFamily(cell));
        if (rowKey.equals("wkt")) {
          return new String(CellUtil.cloneValue(cell));
        }
      }
    }

    return null;
  }
}
