package com.cassandraguide.maoking;// CassandraClient.java
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

import java.net.InetSocketAddress;
import java.util.UUID;

/****
 * select * from system_views.clients;
 * 
 * client_options          | {'APPLICATION_NAME': 'my-java-app-client-id', 'APPLICATION_VERSION': '1.0.0', 'CLIENT_ID': 'ba401000-bf8f-4328-827e-812bb90601f6', 'CQL_VERSION': '3.0.0', 'DRIVER_NAME': 'Apache Cassandra Java Driver', 'DRIVER_VERSION': '4.19.0'}
 * 
 * **/
public class CassandraApplicationNameClientId4x {

    public static void main(String[] args) {
        // 客户端标识（相当于 "client-id"）
        String CLIENT_ID = "my-java-app-client-id";

        System.out.println("Initializing CQL session with client-id: " + CLIENT_ID);

        // 构建 CqlSession：连接本地 Cassandra
        try (CqlSession session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("127.0.0.1", 9042)) // 默认 Cassandra 地址
                .withLocalDatacenter("datacenter1")                        // 本地单节点默认 DC
                .withApplicationName(CLIENT_ID)                            // 👈 设置 client-id
                .withApplicationVersion("1.0.0")
                .withClientId(UUID.randomUUID())
                .withAuthCredentials("maoling", "maoling123456")
                .build()) {

            System.out.println("✅ Successfully connected to Cassandra!");

            // 验证连接：查询系统表
            ResultSet rs = session.execute("SELECT release_version FROM system.local");
            Row row = rs.one();
            if (row != null) {
                String version = row.getString("release_version");
                System.out.println("Cassandra server version: " + version);
            }

            // 可选：列出所有 keyspaces（验证读权限）
            ResultSet keyspaces = session.execute("DESCRIBE KEYSPACES");
            System.out.println("\nAvailable keyspaces:");
            for (Row ks : keyspaces) {
                System.out.println("- " + ks.getString("keyspace_name"));
            }
            Thread.sleep(300_000);
            System.out.println("\nCloseOperation: Session will auto-close.");
            
        } catch (Exception e) {
            System.err.println("❌ Failed to connect or query Cassandra:");
            e.printStackTrace();
        }
    }
}