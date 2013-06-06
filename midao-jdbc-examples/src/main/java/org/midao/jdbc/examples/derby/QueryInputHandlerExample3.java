/*
 * Copyright 2013 Zakhar Prykhoda
 *
 *    midao.org
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.midao.jdbc.examples.derby;

import org.midao.jdbc.core.MidaoFactory;
import org.midao.jdbc.core.handlers.input.named.MapListInputHandler;
import org.midao.jdbc.core.handlers.output.MapOutputHandler;
import org.midao.jdbc.core.handlers.output.RowCountOutputHandler;
import org.midao.jdbc.core.service.QueryRunnerService;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class QueryInputHandlerExample3 {
    public static void main(String[] args) throws SQLException {
        Connection conn = DerbyParameters.createConnection();

        QueryRunnerService runner = MidaoFactory.getQueryRunner(conn);

        try {
            runner.update("CREATE TABLE students ("
                    + "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"
                    + "name VARCHAR(24) NOT NULL,"
                    + "address VARCHAR(1024)," + "CONSTRAINT primary_key PRIMARY KEY (id))");

            runner.update("INSERT INTO students (name, address) VALUES ('Not me', 'unknown')", new RowCountOutputHandler<Integer>(), new Object[0]);

            final HashMap<String, Object> tableParams = new HashMap<String, Object>();
            tableParams.put("id", 1);

            final HashMap<String, Object> studentParams = new HashMap<String, Object>();
            studentParams.put("address", "unknown");

            HashMap<String, Map<String, Object>> paramsList = new HashMap<String, Map<String, Object>>();
            paramsList.put("table", tableParams);
            paramsList.put("student", studentParams);

            MapListInputHandler input = new MapListInputHandler(
                    "SELECT name FROM students WHERE id = :table.id AND address = :student.address",
                    paramsList);

            Map<String, Object> result = runner.query(input, new MapOutputHandler());

            System.out.println("Query output: " + result);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            runner.update("DROP TABLE students");
        }
    }
}
