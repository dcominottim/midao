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

import org.midao.jdbc.core.MjdbcFactory;
import org.midao.jdbc.core.MjdbcTypes;
import org.midao.jdbc.core.handlers.input.query.QueryInputHandler;
import org.midao.jdbc.core.handlers.model.CallResults;
import org.midao.jdbc.core.handlers.model.QueryParameters;
import org.midao.jdbc.core.handlers.output.MapOutputHandler;
import org.midao.jdbc.core.handlers.type.BaseTypeHandler;
import org.midao.jdbc.core.handlers.type.TypeHandlerUtils;
import org.midao.jdbc.core.service.QueryRunnerService;

import java.sql.*;
import java.util.Map;

/**
 */
public class CallLargeParametersExample {
    public static void main(String[] args) throws SQLException {
        Connection conn = DerbyParameters.createConnection();

        QueryRunnerService runner = MjdbcFactory.getQueryRunner(conn, BaseTypeHandler.class);

        try {

            // putting initial data into Database
            runner.update("CREATE PROCEDURE TEST_PROC_LARGE (IN clobIn CLOB, OUT clobOut CLOB, IN blobIn BLOB, OUT blobOut BLOB) PARAMETER STYLE JAVA LANGUAGE JAVA no sql EXTERNAL NAME 'org.midao.jdbc.examples.derby.CallLargeParametersExample.testProcedureLarge'");

            QueryInputHandler input = null;
            QueryParameters parameters = new QueryParameters();

            parameters.set("clobIn", "John", MjdbcTypes.CLOB, QueryParameters.Direction.IN);
            parameters.set("clobOut", null, MjdbcTypes.CLOB, QueryParameters.Direction.OUT);

            parameters.set("blobIn", "Doe", MjdbcTypes.BLOB, QueryParameters.Direction.IN);
            parameters.set("blobOut", null, MjdbcTypes.BLOB, QueryParameters.Direction.OUT);

            input = new QueryInputHandler("{call TEST_PROC_LARGE(:clobIn, :clobOut, :blobIn, :blobOut)}", parameters);

            CallResults<QueryParameters, Map<String, Object>> result = runner.call(input, new MapOutputHandler());

            System.out.println("Call parameters returned: " + result.getCallInput());
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            runner.update("DROP PROCEDURE TEST_PROC_LARGE");
        }
    }

    /*
     * DERBY JAVA SQL FUNCTIONS/PROCEDURES
     */
    public static void testProcedureLarge(java.sql.Clob clobIn, java.sql.Clob[] clobOut, java.sql.Blob blobIn, java.sql.Blob[] blobOut) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:default:connection");
        Statement stmt = null;

        Clob newClob = (Clob) TypeHandlerUtils.createClob(conn);
        newClob.setString(1, "Hello " + clobIn.getSubString(1, (int) clobIn.length()));

        Blob newBlob = (Blob) TypeHandlerUtils.createBlob(conn);
        newBlob.setBytes(1, ("Hi " + new String(blobIn.getBytes(1, (int) blobIn.length()))).getBytes());

        clobOut[0] = newClob;
        blobOut[0] = newBlob;
    }
}
