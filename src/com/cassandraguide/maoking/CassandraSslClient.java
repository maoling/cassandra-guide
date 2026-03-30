package com.cassandraguide.maoking;

import com.datastax.oss.driver.api.core.CqlSession;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Collection;

public class CassandraSslClient {

    public static SSLContext createSslContext(String truststorePath, String truststorePassword) throws Exception {
        KeyStore trustStore = KeyStore.getInstance("JKS");
        try (InputStream tsStream = Files.newInputStream(Paths.get(truststorePath))) {
            trustStore.load(tsStream, truststorePassword.toCharArray());
        }

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        return sslContext;
    }

    public static void main(String[] args) throws Exception {
        
        SSLContext sslContext = createSslContext("path/to/client.truststore", "password123");

        Collection<InetSocketAddress> contactPoints = new ArrayList<>();
        contactPoints.add(new InetSocketAddress("127.0.0.1", 9042));
        contactPoints.add(new InetSocketAddress("127.0.0.2", 9042));
        contactPoints.add(new InetSocketAddress("127.0.0.3", 9042));
        
        CqlSession session = CqlSession.builder()
                .addContactPoints(contactPoints)
                .withLocalDatacenter("datacenter1")
                .withSslContext(sslContext) // 核心配置
                .withAuthCredentials("user", "password") // 如果启用了 PasswordAuthenticator
                .build();

        System.out.println("Connected to: " + session.getMetadata().getClusterName());

        Thread.sleep(300_000);
    }
}