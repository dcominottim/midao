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

package org.midao.core.handlers.type;

import oracle.jdbc.OracleConnection;
import oracle.sql.ARRAY;
import oracle.sql.BLOB;
import oracle.sql.CLOB;
import org.midao.core.MidaoTypes;
import org.midao.core.Overrider;
import org.midao.core.handlers.model.QueryParameters;
import org.midao.core.handlers.utils.MappingUtils;

import java.io.InputStream;
import java.sql.*;
import java.util.Collection;
import java.util.List;

/**
 * TypeHandler Implementation tailored to Oracle JDBC Driver
 */
public class OracleTypeHandler implements TypeHandler {
	private final Overrider overrider;

    /**
     * Creates new OracleTypeHandler instance
     *
     * @param overrider overrider
     */
	public OracleTypeHandler(Overrider overrider) {
		this.overrider = overrider;
	}

    /**
     * {@inheritDoc}
     */
	public QueryParameters processInput(Statement stmt, QueryParameters params) throws SQLException {
		QueryParameters result = new QueryParameters(params);
		Object value = null;
		Object convertedValue = null;
		Connection conn = stmt.getConnection();
		
		for (String parameterName : params.keySet()) {
			value = params.getValue(parameterName);
			convertedValue = null;
			
			if (params.getType(parameterName) == MidaoTypes.ARRAY) {
				OracleConnection oraConn = (OracleConnection) stmt.getConnection();
				
				if (value instanceof Object[]) {
					convertedValue = oraConn.createARRAY(convertJavaClassToSqlType(value.getClass().getComponentType().getSimpleName()), value);
				} else if (value instanceof Collection) {
                    Object[] valueArray = ((Collection) value).toArray();
					convertedValue = oraConn.createARRAY(convertJavaClassToSqlType(valueArray.getClass().getComponentType().getSimpleName()), valueArray);
				} else {
					convertedValue = value;
				}
				
			} else if (params.getType(parameterName) == MidaoTypes.BLOB) {
				BLOB blob = null;
				
				if (value instanceof String) {
                    blob = oracle.sql.BLOB.createTemporary(conn, false, oracle.sql.BLOB.DURATION_SESSION);
					convertedValue = TypeHandlerUtils.convertBlob(blob, (String) value);
				} else if (value instanceof InputStream) {
                    blob = oracle.sql.BLOB.createTemporary(conn, false, oracle.sql.BLOB.DURATION_SESSION);
					convertedValue = TypeHandlerUtils.convertBlob(blob, (InputStream) value);
				} else if (value instanceof byte[]) {
                    blob = oracle.sql.BLOB.createTemporary(conn, false, oracle.sql.BLOB.DURATION_SESSION);
					convertedValue = TypeHandlerUtils.convertBlob(blob, (byte[]) value);
				} else {
					convertedValue = value;
				}
				
			} else if (params.getType(parameterName) == MidaoTypes.CLOB) {
				CLOB clob = null;
				
				if (value instanceof String) {
                    clob = oracle.sql.CLOB.createTemporary(conn, false, oracle.sql.CLOB.DURATION_SESSION);
					convertedValue = TypeHandlerUtils.convertClob(clob, (String) value);
				} else if (value instanceof InputStream) {
                    clob = oracle.sql.CLOB.createTemporary(conn, false, oracle.sql.CLOB.DURATION_SESSION);
					convertedValue = TypeHandlerUtils.convertClob(clob, (InputStream) value);
				} else if (value instanceof byte[]) {
                    clob = oracle.sql.CLOB.createTemporary(conn, false, oracle.sql.CLOB.DURATION_SESSION);
					convertedValue = TypeHandlerUtils.convertClob(clob, (byte[]) value);
				} else {
					convertedValue = value;
				}
				
			} else if (params.getType(parameterName) == MidaoTypes.SQLXML) {
				// as far as I know - it is not completely supported by Oracle JDBC Driver.
				// below you will find example usage of XMLType.
				// Other option would be using TO_CLOB on SQLXML(XMLType) type in PL/SQL.
				
				// oracle.xdb.XMLType
				
				// You also need to include the xdb.jar and xmlparserv2.jar files in the 
				// classpath environment variable to use SQLXML type data, if they are 
				// not already present in the classpath.
				
				/*
				if (value instanceof String) {
					XMLType xmlType = oracle.xdb.XMLType.createXML(conn, (String) value);
					convertedValue = xmlType;
				} else if (value instanceof InputStream) {
					XMLType xmlType = oracle.xdb.XMLType.createXML(conn, (InputStream) value);
					convertedValue = xmlType;
				} else if (value instanceof byte[]) {
					XMLType xmlType = oracle.xdb.XMLType.createXML(conn, new String((byte[]) value));
					convertedValue = xmlType;
				} else {
					convertedValue = value;
				}
				*/
				convertedValue = value;
				
			} else {
				convertedValue = value;
			}
			
			// any other type processing can be added to DataBase specific TypeHandler implementation.
			
			result.updateValue(parameterName, convertedValue);
		}
		
		return result;
	}

    /**
     * {@inheritDoc}
     */
	public void afterExecute(Statement stmt, QueryParameters processedInput, QueryParameters params) throws SQLException {
		Object value = null;
		Object convertedValue = null;
		Connection conn = stmt.getConnection();
		
		for (String parameterName : params.keySet()) {
			value = params.getValue(parameterName);
			convertedValue = processedInput.getValue(parameterName);
			
			if (params.getType(parameterName) == MidaoTypes.ARRAY) {
				
				if (value instanceof Object[] || value instanceof Collection) {
					if (convertedValue instanceof ARRAY) {
						((ARRAY) convertedValue).free();
					}
				}
				
			} else if (params.getType(parameterName) == MidaoTypes.BLOB) {
				
				if (value instanceof String || value instanceof InputStream || value instanceof byte[]) {
					if (convertedValue instanceof BLOB) {
						((BLOB) convertedValue).freeTemporary();
					}
				}
				
			} else if (params.getType(parameterName) == MidaoTypes.CLOB) {
				
				if (value instanceof String || value instanceof InputStream || value instanceof byte[]) {
					if (convertedValue instanceof CLOB) {
						((CLOB) convertedValue).freeTemporary();
					}
				}
			} else if (params.getType(parameterName) == MidaoTypes.SQLXML) {
				
				if (value instanceof String || value instanceof InputStream || value instanceof byte[]) {
					/*
					if (convertedValue instanceof XMLType) {
						((XMLType) convertedValue).close();
					}
					*/
				}
				
			}
			
		}
	}

    /**
     * {@inheritDoc}
     */
	public QueryParameters processOutput(Statement stmt, QueryParameters params) throws SQLException {
		Object value = null;
		Object convertedValue = null;
		
		ARRAY sqlArray = null;
		BLOB sqlBlob = null;
		CLOB sqlClob = null;
		//XMLType sqlXml = null;
		
		for (String parameterName : params.keySet()) {
			value = params.getValue(parameterName);
			
			convertedValue = null;
			
			if (params.getType(parameterName) == MidaoTypes.ARRAY) {
				
				if (value instanceof ARRAY) {
					sqlArray = ((ARRAY) value);
					
					convertedValue = sqlArray.getArray();
					sqlArray.free();
				} else {
					convertedValue = value;
				}
				
			} else if (params.getType(parameterName) == MidaoTypes.BLOB) {
				
				if (value instanceof BLOB) {
					sqlBlob = (BLOB) value;
					
					convertedValue = TypeHandlerUtils.readBlob(sqlBlob, false);
					
					sqlBlob.freeTemporary();
				} else {
					convertedValue = value;
				}
				
			} else if (params.getType(parameterName) == MidaoTypes.CLOB) {
				
				if (value instanceof CLOB) {
					sqlClob = (CLOB) value;
					
					convertedValue = new String(TypeHandlerUtils.readClob(sqlClob, false));
					
					sqlClob.freeTemporary();
				} else {
					convertedValue = value;
				}
			} else if (params.getType(parameterName) == oracle.jdbc.OracleTypes.CURSOR) {
				if (value instanceof ResultSet) {
					ResultSet rs = (ResultSet) value;
					convertedValue = MappingUtils.convertResultSet(rs);
				} else {
					convertedValue = value;
				}
			} else if (params.getType(parameterName) == MidaoTypes.SQLXML) {
				convertedValue = value;
				/*
				if (value instanceof XMLType) {
					sqlXml = (XMLType) value;
					
					convertedValue = TypeHandlerUtils.readSqlXml(sqlXml, false);
					
					sqlXml.close();
				} else {
					convertedValue = value;
				}
				*/
				
			} else {
				convertedValue = value;
			}
			
			// any other type processing can be added to DataBase specific TypeHandler implementation.
			
			params.updateValue(parameterName, convertedValue);
		}
		
		return params;
	}

    /**
     * {@inheritDoc}
     */
	public List<QueryParameters> processOutput(Statement stmt, List<QueryParameters> paramsList) throws SQLException {
		QueryParameters params = null;
		
		for (int i = 1; i < paramsList.size(); i++) {
			params = paramsList.get(i);
			
			params = processOutput (stmt, params);
			
			paramsList.set(i, params);
		}
		
		return paramsList;
	}
	
	/**
	 * Converts Java class name into Oracle SQL Type.
     * Needs improvement
     *
     * @param simpleClassName Java simple class name
	 */
	private String convertJavaClassToSqlType(String simpleClassName) throws SQLException {
		if ("String".equals(simpleClassName) == true) {
			return "VARCHAR2";
		}
		
		throw new SQLException(String.format("Could not convert java class %s", simpleClassName));
	}


}