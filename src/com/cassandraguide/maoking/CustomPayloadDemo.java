package com.cassandraguide.maoking;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class CustomPayloadDemo {

    public static void main(String[] args) throws InterruptedException {

        try (CqlSession session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
                .withLocalDatacenter("datacenter1")
                .withClientId(UUID.randomUUID())
                .withAuthCredentials("maoling", "maoling123456")
                .withApplicationName("addCustomPayload")
                .build()) {

            SimpleStatement stmt = SimpleStatement.builder(
                        "SELECT release_version FROM system.local")
                    .addCustomPayload(
                        "CLIENT_ID",
                        ByteBuffer.wrap("client-123".getBytes(StandardCharsets.UTF_8)))
                    .addCustomPayload(
                        "PRIORITY",
                        ByteBuffer.wrap("HIGH".getBytes(StandardCharsets.UTF_8)))
                    .build();

            ResultSet rs = session.execute(stmt);

            System.out.println(
                "FUCK Cassandra version = " +
                rs.one().getString("release_version")
            );

            Thread.sleep(300_000);
        }
        
    }
}
