//package com.cassandraguide.maoking;
//
//import com.datastax.oss.driver.api.core.CqlSession;
//import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
//import com.datastax.oss.driver.api.core.cql.*;
//import com.datastax.oss.driver.api.core.uuid.Uuids;
//
//import java.net.InetSocketAddress;
//import java.nio.ByteBuffer;
//import java.time.Instant;
//import java.util.Arrays;
//import java.util.List;
//import java.util.UUID;
//
//public class VectorExample {
//
//    public static void main(String[] args) {
//        try (CqlSession session = CqlSession.builder()
//                .addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
//                .addContactPoint(new InetSocketAddress("127.0.0.2", 9042))
//                .addContactPoint(new InetSocketAddress("127.0.0.3", 9042))
//                .withLocalDatacenter("datacenter1")
//                .withKeyspace("cycling")
//                .build()) {
//
//            //UUID id = UUID.randomUUID();
//            UUID id = Uuids.timeBased();
//            long now = System.currentTimeMillis();
//
//            List<Float> vector = Arrays.asList(0.1f, 0.2f, 0.3f, 0.4f, 0.5f);
//            ByteBuffer encodedVector = encodeVector(vector);
//
//            // 插入数据
//            String insertQuery = "INSERT INTO comments_vs (id, created_at, comment, comment_vector, commenter, record_id) " +
//                    "VALUES (?, ?, ?, ?, ?, ?)";
//            PreparedStatement insertStmt = session.prepare(insertQuery);
//            BoundStatement boundInsert = insertStmt.bind(
//                    id,
//                    Instant.now(),
//                    "这是一个带有 vector 的评论",
//                    encodedVector,
//                    "小明",
//                    Uuids.timeBased()
//            ).setConsistencyLevel(DefaultConsistencyLevel.ONE);;
//            session.execute(boundInsert);
//            System.out.println("插入完成====================================");
//
//            // 查询并解码 vector
//            String selectQuery = "SELECT id, comment_vector FROM comments_vs WHERE id = ?";
//            PreparedStatement selectStmt = session.prepare(selectQuery);
//            BoundStatement boundSelect = selectStmt.bind(id);
//            ResultSet rs = session.execute(boundSelect);
//
//            Row row = rs.one();
//            if (row != null) {
//                ByteBuffer vbuf = row.getByteBuffer("comment_vector");
//                List<Float> decoded = decodeVector(vbuf, 5);
//                System.out.println("读取向量: " + decoded);
//            } else {
//                System.out.println("未找到数据。");
//            }
//        }
//    }
//
//    // vector<float, N> 类型以 float32 array 编码成 ByteBuffer
//    private static ByteBuffer encodeVector(List<Float> vector) {
//        ByteBuffer buffer = ByteBuffer.allocate(4 * vector.size());
//        for (Float f : vector) {
//            buffer.putFloat(f);
//        }
//        buffer.flip();
//        return buffer;
//    }
//
//    private static List<Float> decodeVector(ByteBuffer buffer, int dim) {
//        buffer.rewind();
//        Float[] floats = new Float[dim];
//        for (int i = 0; i < dim; i++) {
//            floats[i] = buffer.getFloat();
//        }
//        return Arrays.asList(floats);
//    }
//}
