/**
 * 导入不同表时，注意修改stmt预编译用的String，修改LoadData方法和main方法里的String，set集合，part[]等等
 */



import com.sun.org.apache.xpath.internal.objects.XString;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Properties;
import java.sql.*;
import java.net.URL;
import java.util.stream.Collectors;

public class client_Loader {
    private static final int  BATCH_SIZE = 500;
    private static URL        propertyURL = GoodLoader.class
            .getResource("/loader.cnf");

    private static Connection         con = null;
    private static PreparedStatement  stmt = null;
    private static boolean            verbose = false;

    private static String contractLoader =
            "insert into contract(contract_number,contract_date,client_enterprise,supply_center)"
                    +" values(?,?,?,?)";
    private static String salesmanLoader =
            "insert into salesman(salesman_number,salesman,age,gender,mobile_phone,supply_center)"
                    +" values(?,?,?,?,?,?)";
    private static String modelLoader =
            "insert into model(product_model,product_name,product_code,unit_price)"
                    +" values(?,?,?,?)";
    private static String ordersLoader =
            "insert into orders(contract_number,product_model,quantity,estimated_delivery_date,lodgement_date,salesman_number)"
                    +" values(?,?,?,?,?,?)";
    private static String clientsLoader =
            "insert into client(client_enterprise,industry,country,city,supply_center)"+"values(?,?,?,?,?)";
    private static String supply_centerLoader =
            "insert into supply_center(supply_center,director)"+"values(?,?)";

    private static void openDB(String host, String dbname,
                               String user, String pwd) {
        try {
            //
            Class.forName("org.postgresql.Driver");
        } catch(Exception e) {
            System.err.println("Cannot find the Postgres driver. Check CLASSPATH.");
            System.exit(1);
        }
        String url = "jdbc:postgresql://" + host + "/" + dbname;
        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", pwd);
        try {
            con = DriverManager.getConnection(url, props);
            if (verbose) {
                System.out.println("Successfully connected to the database "
                        + dbname + " as " + user);
            }
            con.setAutoCommit(false);
        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
        try {
            stmt = con.prepareStatement(clientsLoader);
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDB();
            System.exit(1);
        }
    }

    private static void closeDB() {
        if (con != null) {
            try {
                if (stmt != null) {
                    stmt.close();
                }
                con.close();
                con = null;
            } catch (Exception e) {
                // Forget about it
            }
        }
    }

    private static void loadData(String client_enterprise,String industry,String country,String city,String supply_center)
            throws SQLException {
        if (con != null) {
            stmt.setString(1, client_enterprise);
            stmt.setString(2, industry);
            stmt.setString(3, country);
            stmt.setString(4, city);
            stmt.setString(5, supply_center);
            stmt.addBatch();
        }
    }

    public static void main(String[] args) {
        String  fileName = null;
        boolean verbose = false;

        switch (args.length) {
            case 1:
                fileName = args[0];
                break;
            case 2:
                switch (args[0]) {
                    case "-v":
                        verbose = true;
                        break;
                    default:
                        System.err.println("Usage: java [-v] client_Loader filename");
                        System.exit(1);
                }
                fileName = args[1];
                break;
            default:
                System.err.println("Usage: java [-v] client_Loader filename");
                System.exit(1);
        }

//        if (propertyURL == null) {
//           System.err.println("No configuration file (loader.cnf) found");
//           System.exit(1);
//        }
        Properties defprop = new Properties();
        defprop.put("host", "localhost");
        defprop.put("user", "checker");
        defprop.put("password", "123456");
        defprop.put("database", "postgres");
        Properties prop = new Properties(defprop);
//        try (BufferedReader conf
//                = new BufferedReader(new FileReader(propertyURL.getPath()))) {
//          prop.load(conf);
//        } catch (IOException e) {
//           // Ignore
//           System.err.println("No configuration file (loader.cnf) found");
//        }
        try (BufferedReader infile
                     = new BufferedReader(new FileReader(fileName))) {
            long     start;
            long     end;
            String   line;
            String[] parts;
            String   client_enterprise;
            String   industry;
            String   country;
            String   city;
            String   supply_center;
            Set client_enterprise_list = new HashSet<>();
            int      cnt = 0;
            int      count = 0;
            // Empty target table
            openDB(prop.getProperty("host"), prop.getProperty("database"),
                    prop.getProperty("user"), prop.getProperty("password"));
            Statement stmt0;
            if (con != null) {
                stmt0 = con.createStatement();
                //stmt0.execute("truncate table supply_center");
                stmt0.close();
            }
            closeDB();
            //
            start = System.currentTimeMillis();
            openDB(prop.getProperty("host"), prop.getProperty("database"),
                    prop.getProperty("user"), prop.getProperty("password"));
            while ((line = infile.readLine()) != null) {
                parts = line.split(",");
                if (parts.length > 1) {
                    if(!client_enterprise_list.contains(parts[1])) {
                        client_enterprise_list.add(parts[1]);
                        client_enterprise = parts[1];
                        industry = parts[5];
                        country = parts[3];
                        city = parts[4];
                        supply_center = parts[2];
                        loadData(client_enterprise,industry,country,city,supply_center);
                        cnt++;
                    }
                    count++;
                    if (cnt % BATCH_SIZE == 0) {
                        stmt.executeBatch();
                        stmt.clearBatch();
                    }
                }
            }
            if (cnt % BATCH_SIZE != 0) {
                stmt.executeBatch();
            }
            con.commit();
            stmt.close();
            closeDB();
            end = System.currentTimeMillis();
            System.out.println(cnt + " records successfully loaded"+"\n"+ count + " records successfully read");
            System.out.println("Loading speed : "
                    + (cnt * 1000)/(end - start)
                    + " records/s"+"\n"
                    + "Reading speed : "
                    +(count * 1000)/(end - start)
                    + " records/s");
        } catch (SQLException se) {
            System.err.println("SQL error: " + se.getMessage());
            try {
                con.rollback();
                stmt.close();
            } catch (Exception e2) {
            }
            closeDB();
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Fatal error: " + e.getMessage());
            try {
                con.rollback();
                stmt.close();
            } catch (Exception e2) {
            }
            closeDB();
            System.exit(1);
        }
        closeDB();
    }
}



