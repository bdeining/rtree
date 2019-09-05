package com.bdeining.rtree;

import java.util.Random;
import java.util.UUID;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder.ModifyableColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptor;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;

public class HbaseClient {

  private Random random = new Random();

  public static void main(String[] args) {
    try {
      new HbaseClient().buildNormalIndex();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void buildNormalIndex() throws Exception {
    Configuration conf = HBaseConfiguration.create();
    Connection connection = ConnectionFactory.createConnection(conf);

    HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();

    TableName tablename = TableName.valueOf("test_table");
    ColumnFamilyDescriptor name = new ModifyableColumnFamilyDescriptor("title".getBytes());
    ColumnFamilyDescriptor wkt = new ModifyableColumnFamilyDescriptor("wkt".getBytes());

    TableDescriptor tableDescriptor =
        TableDescriptorBuilder.newBuilder(tablename)
            .setColumnFamily(name)
            .setColumnFamily(wkt)
            .build();

    if (!admin.tableExists(tablename)) {
      admin.createTable(tableDescriptor);
    }

    Table table = connection.getTable(tablename);

    // put 'test_table', 'r1', 'name', 'value', 10
    for (long i = 0; i < 100; i++) {
      Put put = new Put(("r" + i).getBytes());
      put.addColumn(
          "title".getBytes(), "value".getBytes(), UUID.randomUUID().toString().getBytes());
      put.addColumn("wkt".getBytes(), "value".getBytes(), getRandomWkt().getBytes());
      table.put(put);
    }

    connection.close();
  }

  private String getRandomWkt() {
    double lat = randomDouble(-90, 90);
    double lon = randomDouble(-180, 180);
    return String.format("POINT (%f %f)", lat, lon);
  }

  private double randomDouble(int rangeMin, int rangeMax) {
    return rangeMin + (rangeMax - rangeMin) * random.nextDouble();
  }

  private void buildRTreeIndex() throws Exception {

    Configuration conf = HBaseConfiguration.create();
    Connection connection = ConnectionFactory.createConnection(conf);

    TableName tablename = TableName.valueOf("test_table");

    /*        HBaseAdmin admin = (HBaseAdmin) connection.getAdmin();
            //HBaseAdmin admin = new HBaseAdmin(conf);



    /*        TableDescriptor tableDescriptor = TableDescriptorBuilder.newBuilder(tablename).build();
            admin.createTable(tableDescriptor);*/

    Table table = connection.getTable(tablename);

    System.out.println(table);

    /*
        Configuration config = HBaseConfiguration.create();
        Job job = new Job(config, "ExampleReadWrite");
        job.setJarByClass(HbaseClient.class);    // class that contains mapper

        Scan scan = new Scan();
        scan.setCaching(500);        // 1 is the default in Scan, which will be bad for MapReduce jobs
        scan.setCacheBlocks(false);  // don't set to true for MR jobs
                                     // set other scan attrs
        String sourceTable = "";
        String targetTable = "";

        TableMapReduceUtil.initTableMapperJob(
                sourceTable,      // input table
                scan,             // Scan instance to control CF and attribute selection
                MyMapper.class,   // mapper class
                null,             // mapper output key
                null,             // mapper output value
                job);
        TableMapReduceUtil.initTableReducerJob(
                targetTable,      // output table
                null,             // reducer class
                job);
        job.setNumReduceTasks(0);

        boolean b = job.waitForCompletion(true);
        if (!b) {
            throw new IOException("error with job!");
        }

    }*/
  }
}
