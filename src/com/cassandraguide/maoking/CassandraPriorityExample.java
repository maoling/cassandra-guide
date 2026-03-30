package com.cassandraguide.maoking;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CassandraPriorityExample {

    public void executeHighPriorityQuery(CqlSession session) {
        // 1. 准备 Payload 键值对
        // 注意：Payload 的 Value 必须是 ByteBuffer
        Map<String, ByteBuffer> payload = new HashMap<>();
        payload.put("request-priority", ByteBuffer.wrap("high".getBytes(StandardCharsets.UTF_8)));
        payload.put("tenant-id", ByteBuffer.wrap("premium-user".getBytes(StandardCharsets.UTF_8)));

        // 2. 创建 Statement 并附加 Payload
        SimpleStatement statement = SimpleStatement.newInstance(
                "SELECT * FROM system.local") // 替换为你的 CQL
                .setCustomPayload(payload)
                .setExecutionProfileName("high-priority-profile"); // 可选：配合 Profile 使用

        // 3. 执行
        ResultSet rs = session.execute(statement);
        System.out.println("Query executed with custom payload.");
    }
}