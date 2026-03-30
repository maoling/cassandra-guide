package com.cassandraguide.maoking;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class CassandraTooManyTableCreator {
    
    public static void main(String[] args) {
        // 集群地址
        String[] clusterNodes = {"127.0.0.1", "127.0.0.2", "127.0.0.3"};
        
        // 连接到集群
        Cluster cluster = Cluster.builder()
                                 .addContactPoints(clusterNodes)
                                 .build();
        Session session = cluster.connect("schema1");  // 连接到 schema3 keyspace

        // 创建表的 CQL 语句模板
        String createTableTemplate = "CREATE TABLE IF NOT EXISTS %s (" +
                                     "user_id varchar PRIMARY KEY, " +
                                     "first varchar, " +
                                     "last varchar, " +
                                     "age int);";

        // 创建 100 张表
        int start = 202;
        long startTs = System.currentTimeMillis();
        for (int i = start; i <= start + 1000; i++) {
            String tableName = "users_" + String.format("%02d", i);  // 表名从 users_01 到 users_100
            String createTableQuery = String.format(createTableTemplate, tableName);
            
            try {
                // 执行 CQL 语句
                session.execute(createTableQuery);
                System.out.println("Table " + tableName + " created successfully.");
            } catch (Exception e) {
                System.err.println("Error creating table " + tableName + ": " + e.getMessage());
            }
        }
        System.out.println("Table ALL created successfully. time: " + (System.currentTimeMillis() - startTs) );
        // 关闭会话和集群连接
        session.close();
        cluster.close();
    }
}
