package com.cassandraguide.maoking;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;

public class CassandraAuthUserConnect {
    
    public static void main(String[] args) throws InterruptedException {

        Collection<InetSocketAddress> contactPoints = new ArrayList<>();
        contactPoints.add(new InetSocketAddress("127.0.0.1", 9042));
        contactPoints.add(new InetSocketAddress("127.0.0.2", 9042));
        contactPoints.add(new InetSocketAddress("127.0.0.3", 9042));
                
        try (CqlSession session = CqlSession.builder()
                .addContactPoints(contactPoints)
                .withLocalDatacenter("datacenter1")
                // --- 关键代码：设置授权信息 ---
                .withAuthCredentials("maoling", "maoling123456")
                // ---------------------------
                .build()) {
            
            System.out.println("连接成功！当前 Keyspace: " + session.getKeyspace());
            
            // 执行查询测试权限
            int size = 10000;
            for (int i = 0; i < size; i++) {
                try {
                    ResultSet resultSet = session.execute("select * from quota_ks.quota_table");
                    if (i % 100 == 0) {
                        System.out.println("user_id: " + resultSet.one().getString("user_id"));
                    }
                } catch (Exception e) {
                    System.err.println("CassandraAuthUserConnectException: \n" + e);
                }
            }
            //Thread.sleep(300_000);
        }
    }
}