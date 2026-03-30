/*
 * Copyright (C) 2016 Jeff Carpenter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cassandraguide.maoking;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.BuiltStatement;
import com.datastax.driver.core.querybuilder.QueryBuilder;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

public class QueryBuilderExample {
	
	public static void main(String[] args) {
		
		Cluster cluster = Cluster.builder().addContactPoints("127.0.0.1", "127.0.0.2", "127.0.0.3",
						"127.0.0.4", "127.0.0.5", "127.0.0.6")
			//.withCredentials("jeff", "i6XJsj!k#9")
			.build();
		
		// create session on the "hotel" keyspace
		Session session = cluster.connect("schema3");

		long start = System.currentTimeMillis() / 1000;
		for (long i = start; i < start + 1000000; i++) {
			// create a Hotel ID
			try {
				String id = "" + i;

				// build an INSERT statement
				BuiltStatement hotelInsertBuilt = QueryBuilder.insertInto("users").
						value("user_id", id).
						value("first", id).
						value("last", id)
						.value("age", i);

				hotelInsertBuilt.setConsistencyLevel(ConsistencyLevel.QUORUM);

				ResultSet hotelInsertResult = session.execute(hotelInsertBuilt);

				System.out.println(hotelInsertResult);
				System.out.println(hotelInsertResult.wasApplied());
				System.out.println(hotelInsertResult.getExecutionInfo());
				System.out.println(hotelInsertResult.getExecutionInfo().getIncomingPayload());

				//Thread.sleep(300);
			} catch (Exception e) {
				System.out.println("insert Exception: " + e);
			}
		}
		
		// build a SELECT statement
//		BuiltStatement hotelSelectBuilt = QueryBuilder.select().all().
//				from("hotels").where(eq("id", id));
//
//		ResultSet hotelSelectResult = session.execute(hotelSelectBuilt);
//
//		// result metadata
//		System.out.println(hotelSelectResult);
//		System.out.println(hotelSelectResult.wasApplied());
//		System.out.println(hotelSelectResult.getExecutionInfo());
//		System.out.println(hotelSelectResult.getExecutionInfo().getIncomingPayload());
//
//		// print results
//		for (Row row : hotelSelectResult) {
//			System.out.format("id: %s, name: %s, phone: %s\n", row.getString("id"),
//				row.getString("name"), row.getString("phone"));
//		}
//
//		// build a DELETE statement
//		BuiltStatement hotelDeleteBuilt = QueryBuilder.delete().all().
//				from("hotels").where(eq("id", id));
//
//		ResultSet hotelDeleteResult = session.execute(hotelDeleteBuilt);
//
//		// result metadata
//		System.out.println(hotelSelectResult);
//		System.out.println(hotelSelectResult.wasApplied());
//		System.out.println(hotelSelectResult.getExecutionInfo());
//		System.out.println(hotelSelectResult.getExecutionInfo().getIncomingPayload());
//
		// close and exit
		cluster.close();
		System.exit(0);
	}
		
		
}
