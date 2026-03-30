package com.cassandraguide.maoking;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class CassandraClientId3x {
    public static void main(String[] args) {
        String[] clusterNodes = {"127.0.0.1", "127.0.0.2", "127.0.0.3"};
        
        // 在 3.x 中，通常通过 ClusterName 来标识不同的客户端实例
        Cluster cluster = Cluster.builder()
                .addContactPoints(clusterNodes)
                .withPort(9042)
                .withClusterName("Client-Instance-001") // 类似于 Session Name
                //.withQueryOptions();
                .build();

        try (Session session = cluster.connect()) {
            // 注意：3.x 驱动并不直接暴露 withClientId 接口给用户自定义 UUID，
            // 它是内部自动生成的。
            System.out.println("Connected to cluster: " + cluster.getClusterName());
            
            String version = session.execute("SELECT release_version FROM system.local")
                                    .one().getString("release_version");
            System.out.println("Cassandra Version: " + version);
            Thread.sleep(30_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            cluster.close();
        }
    }
}