package kr.co.bizframe.exlink.util;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SqlScriptRunner {

    public static final String DEFAULT_SCRIPT_DELIMETER = ";";
	public static final Logger logger = LoggerFactory.getLogger(SqlScriptRunner.class);
    private final boolean autoCommit, logErrors;
    private final Connection connection;

    /**
     * 
     * @param connection : Connection to database.
     * @param autoCommit : True - it will commit automatically, false - you have to commit manualy.
     */
    public SqlScriptRunner(final Connection connection, final boolean autoCommit) {
        this(connection, autoCommit, true);
    }
    
    /**
     * 
     * @param connection : Connection to database.
     * @param autoCommit : True - it will commit automatically, false - you have to commit manualy.
     * @param logErrors : True - it will log errors, false - it will not log errors.
     */
    public SqlScriptRunner(final Connection connection, final boolean autoCommit, final boolean logErrors) {
        if (connection == null) {
            throw new RuntimeException("Connection is required");
        }
        this.connection = connection;
        this.autoCommit = autoCommit;       
        this.logErrors = logErrors;
    }
    
    /**
     * 
     * @param reader - file with your script
     * @throws SQLException - throws SQLException on error
     */
    public void runScript(final Reader reader) throws SQLException {
        final boolean originalAutoCommit = this.connection.getAutoCommit();
        try {
            if (originalAutoCommit != this.autoCommit) {
                this.connection.setAutoCommit(autoCommit);
            }
            this.runScript(this.connection, reader);
        } finally {
            this.connection.setAutoCommit(originalAutoCommit);
        }
    }
    
    private void runScript(final Connection connection, final Reader reader) {

        for (String script : formatString(reader)) {
            PreparedStatement statement = null;
            
            try {
                statement = connection.prepareStatement(script);                
                statement.execute();
                
                //If auto commit is enabled, then commit
                if (autoCommit) {
                    connection.commit();
                }

            } catch (SQLException ex) {
                if (logErrors) {
                	logger.error("ERROR RUN SCIRPT=["+script+"]");
                    logger.error(ex.getMessage(), ex);
                } else {
                    ex.fillInStackTrace();
                }
            } finally {                
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException ex) {
                        if (logErrors) {
                        	logger.error(ex.getMessage(), ex);
                        } else {
                            ex.fillInStackTrace();
                        }
                    }
                }
            }
        }

    }
    
    /**
     * Parses file into commands delimeted by ';'
     * @param reader 
     * @return string[] - commands from file
     */
    private String[] formatString(final Reader reader) {
        String result = "";
        String line;
        final LineNumberReader lineReader = new LineNumberReader(reader);

        try {
            while ((line = lineReader.readLine()) != null) {
                if (line.startsWith("--") || line.startsWith("//") || line.startsWith("#")) {
                    //DO NOTHING - It is a commented line
                } else {
                    result += line;
                }
            }
        } catch (IOException ex) {
            if (logErrors) {
            	logger.error(ex.getMessage(), ex);
            } else {
                ex.fillInStackTrace();

            }
        }

        if (result == null) {
            throw new RuntimeException("Error while parsing or no scripts in file!");
        } else {
            return result.replaceAll("(?<!"+DEFAULT_SCRIPT_DELIMETER+")(\\r?\\n)+", "").split(DEFAULT_SCRIPT_DELIMETER);
        }
    }
}
