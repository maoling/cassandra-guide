package com.cassandraguide.maoking;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.config.DefaultDriverOption;
import com.datastax.oss.driver.api.core.config.DriverConfigLoader;
import com.datastax.oss.driver.api.core.config.DriverOption;
import java.net.InetSocketAddress;

public class CassandraClientFixed {

    public static void main(String[] args) throws InterruptedException {
        String myClientId = "MY_CLIENT_001";

        // 核心技巧：构造一个临时的 DriverOption 来绕过编译限制
        // 路径必须是 advanced.protocol.startup-options.CLIENT_ID
        DriverOption clientIdOption = new DriverOption() {
            @Override
            public String getPath() {
                return "advanced.protocol.startup-options.CLIENT_ID";
            }
        };

        DriverConfigLoader loader = DriverConfigLoader.programmaticBuilder()
                .withString(DefaultDriverOption.LOAD_BALANCING_LOCAL_DATACENTER, "datacenter1")
                // 现在可以通过编译了
                .withString(clientIdOption, myClientId)
                .build();

        try (CqlSession session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
                .withLocalDatacenter("datacenter1")
                .withConfigLoader(loader)
                .withAuthCredentials("maoling", "maoling123456")
                .build()) {
            System.out.println("Session created successfully with CLIENT_ID.");

            String version = session.execute("SELECT release_version FROM system.local")
                    .one().getString("release_version");
            System.out.println("Cassandra Version: " + version);
            Thread.sleep(30_000);
        }

        Thread.sleep(300_000);
    }
}