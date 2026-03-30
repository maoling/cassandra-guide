package com.cassandraguide.maoking;

import com.datastax.driver.core.*;

import java.util.Arrays;
import java.util.UUID;

/**
 * 3.12.1 不能使用vector type
 *
 * **/

public class VectorInsertExample3x {
    public static void main(String[] args) {
        Cluster cluster = Cluster.builder()
                .addContactPoint("127.0.0.1")
                .withPort(9042)
                .withQueryOptions(new QueryOptions().setConsistencyLevel(ConsistencyLevel.ONE)) // 👈 设置 ConsistencyLevel
                .build();

        Session session = cluster.connect("cycling");

        PreparedStatement ps = session.prepare(
            "INSERT INTO comments_vs (id, created_at, comment, comment_vector, commenter, record_id) " +
            "VALUES (?, toTimestamp(now()), ?, ?, ?, now())"
        );

        BoundStatement bs = ps.bind(
                UUID.randomUUID(),
                "Comment using 3.12.1 driver",
                Arrays.asList(0.1f, 0.2f, 0.3f, 0.4f, 0.5f),
                "Alice"
        );

        session.execute(bs);
        System.out.println("Inserted vector row using 3.x driver.");

        session.close();
        cluster.close();
    }
}
