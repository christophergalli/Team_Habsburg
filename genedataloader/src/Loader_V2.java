//name of the package = medicalinformatics
package ch.fhnw.medicalinformatics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Loader_V2 {

    public static void main(String [] args) {

        // check command line parameter
        if (args.length != 1) {
            System.err.println("Usage: java -jar loader.jar add.properties");
            System.exit(0);
        }

        String propertyFile = args[0];
        System.out.println("Property File: " + propertyFile);

        // validate file
        File file = new File(propertyFile);
        if (!file.exists()) {
            System.err.println("File " + propertyFile + " not found");
            System.exit(0);
        }

        // open property file and read values
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(file));
            // read parameters
            String geneInfoFile = properties.getProperty("geneInfoFile");
            String hostname = properties.getProperty("hostname");
            Long port = Long.parseLong(properties.getProperty("port"));
            String dbname = properties.getProperty("dbname");
            String username = properties.getProperty("username");
            String password = properties.getProperty("password");
            String remoteFile = properties.getProperty("remoteFile");
            boolean downloadFile = Boolean.parseBoolean(properties.getProperty("downloadFile"));
            Long batchSize = Long.parseLong(properties.getProperty("batchSize"));

            // download file
            //if (downloadFile) {
            //    Util.downloadFile(remoteFile, geneInfoFile);
            //    Util.unzipFile(geneInfoFile, geneInfoFile.substring(0, geneInfoFile.indexOf(".gz")));
            //}

            Long start = System.currentTimeMillis();
            loadData(geneInfoFile, hostname, port, dbname, username, password, batchSize);
            Long stop = System.currentTimeMillis();

            System.out.println("time consumed " + (stop-start)/1000.0f);

        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }

    private static void loadData(String geneInfoFile, String hostname, Long port, String dbname, String username, String password, Long batchSize) throws Exception {
        //open db connection
        String connStr = "jdbc:mysql://" + hostname + ":" + port + "/" + dbname;
        Connection connection = DriverManager.getConnection(connStr, username, password);
        String insertCmd = createInsertCommand(connection);

        // open file
        File file = new File(geneInfoFile);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;
        int counter = 0;

        // create a table with all gene type information
        Map<String, Integer> geneTypes = new Hashtable<>();

        // skip the line with the header information
        br.readLine();

        List<String> rowsToInsert = new ArrayList<>();

        while((line = br.readLine())!=null) {

            String [] row = line.split("\t");
            rowsToInsert.add(createParameterString(row, connection, insertCmd, geneTypes));

            counter++;
            if (counter % batchSize == 0) {
                System.out.println(counter + " - " + row.length);
                executeInsert(rowsToInsert, connection, insertCmd);
                rowsToInsert.clear();
            }
        }
        if (rowsToInsert.size() > 0) {
            executeInsert(rowsToInsert, connection, insertCmd);
        }
        System.out.println(counter);
        connection.close();
    }

    private static String createParameterString(String [] row, Connection connection, String insertCmd, Map<String, Integer> geneTypes) throws Exception {
        PreparedStatement stmt = connection.prepareStatement(insertCmd);

        for (int i=0; i<row.length; i++) {
            if (row[i].trim().equals("-")) {
                stmt.setNull(i+1, Types.VARCHAR);
            } else {
                stmt.setString(i+1, row[i]);
            }
        }

        // handle integers
        if (row[0].trim().equals("-")) stmt.setNull(0, Types.INTEGER);
        else stmt.setInt(1, Integer.parseInt(row[0]));

        if (row[1].trim().equals("-")) stmt.setNull(1, Types.INTEGER);
        else stmt.setInt(2, Integer.parseInt(row[1]));

        // handle date
        if (row[14].trim().equals("-")) stmt.setNull(15, Types.DATE);
        else {
            Integer year = Integer.parseInt(row[14].substring(0, 4));
            Integer month = Integer.parseInt(row[14].substring(4, 6));
            Integer day = Integer.parseInt(row[14].substring(6, 8));
            Date date = Date.valueOf(year + "-" + month + "-" + day);
            stmt.setDate(15, date);
        }

        // check gene type
        Integer geneTypeId = geneTypes.get(row[9].trim());
        if (geneTypeId == null) {
            geneTypeId = insertGeneType(row[9].trim(), connection, geneTypes);
        }
        stmt.setInt(10, geneTypeId);

        String stmtStr = stmt.toString();
        int startIndex = stmtStr.substring(stmtStr.indexOf("VALUES")).indexOf("(");
        stmt.close();
        String result = stmtStr.substring(stmtStr.indexOf("VALUES")).substring(startIndex);
        return result;
    }

    private static int insertGeneType(String geneType, Connection connection, Map<String, Integer> geneTypes) throws Exception {
        Integer id = geneTypes.size() + 1;
        String cmd = "insert into genetype (id, name) VALUES (?, ?)";
        PreparedStatement stmt = connection.prepareStatement(cmd);
        stmt.setInt(1, id);
        stmt.setString(2, geneType);
        stmt.executeUpdate();
        stmt.close();
        geneTypes.put(geneType, id);
        return id;
    }

    private static void executeInsert(List<String> rowsToInsert, Connection connection, String insertCmd) throws Exception {
        String newICmd = insertCmd.substring(0, insertCmd.indexOf(")") + 1) + " VALUES ";
        StringBuilder sb = new StringBuilder();
        for (String row : rowsToInsert) {
            sb.append(row + ",");
        }

        newICmd = newICmd + sb.toString();
        newICmd = newICmd.substring(0, newICmd.length() - 1);

        PreparedStatement stmt = connection.prepareStatement(newICmd);
        stmt.executeUpdate();
        stmt.close();
    }


    private static String createInsertCommand(Connection connection) throws Exception {
        ResultSet rs = connection.getMetaData().getColumns(null, null, "allgenes", null);
        String cmd = "insert into allgenes(";
        String parameters = "(";

        while (rs.next()) {
            cmd += rs.getString("COLUMN_NAME") + ",";
            parameters += "?,";
        }

        cmd = cmd.substring(0, cmd.length() - 1) + ")";
        parameters = parameters.substring(0, parameters.length() - 1) + ")";

        return cmd + " VALUES " + parameters;
    }
}