import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Properties;
import java.sql.*;
import java.net.URL;

public class Loader2 extends Thread {
    private static final int  BATCH_SIZE = 500;
    private static URL        propertyURL = GoodLoader.class
            .getResource("/loader.cnf");

    private static Connection         con = null;
    private static PreparedStatement  stmt = null;
    private static boolean            verbose = false;
    private static String Loader =
            "insert into sustc(contract_number,client_enterprise,supply_center,country,city,industry," +
                    "product_code,product_name,product_model, unit_price,quantity," +
                    "contract_date,estimated_delivery_date,lodgement_date,director," +
                    "salesman,salesman_number,age,gender,mobile_phone)" +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    public static void main(String[] args) throws InterruptedException {
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
                        System.err.println("Usage: java [-v] GoodLoader filename");
                        System.exit(1);
                }
                fileName = args[1];
                break;
            default:
                System.err.println("Usage: java [-v] GoodLoader filename");
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
                     = new BufferedReader(new FileReader("cs307proj1.txt"))) {
            long     start;
            long     end;
            String   line;
            String[] parts;
            String   contract_number,
                    client_enterprise,
                    supply_center,
                    country,city,
                    industry,
                    product_code,
                    product_name,
                    product_model,
                    contract_date,
                    estimated_delivery_date,
                    lodgement_date,
                    director,
                    salesman,
                    gender,
                    mobile_phone;
            int   unit_price,quantity,salesman_number, age;
            int      cnt = 0;
            // Empty target table
            openDB(prop.getProperty("host"), prop.getProperty("database"),
                    prop.getProperty("user"), prop.getProperty("password"));
            Statement stmt0;
//            if (con != null) {
//                stmt0 = con.createStatement();
//                stmt0.execute("truncate table sustc");
//                stmt0.close();
//            }
            closeDB();
            //
            start = System.currentTimeMillis();
            openDB(prop.getProperty("host"), prop.getProperty("database"),
                    prop.getProperty("user"), prop.getProperty("password"));
            while ((line = infile.readLine()) != null) {
                parts = line.split(",");
                if (parts.length > 1) {
                    contract_number = parts[0];
                    client_enterprise = parts[1];
                    supply_center = parts[2];
                    country = parts[3];
                    city = parts[4];
                    industry = parts[5];
                    product_code = parts[6];
                    product_name = parts[7];
                    product_model = parts[8];
                    unit_price = Integer.parseInt(parts[9]);
                    quantity = Integer.parseInt(parts[10]);
                    contract_date = parts[11];
                    estimated_delivery_date = parts[12];
                    lodgement_date = parts[13];
                    director = parts[14];
                    salesman = parts[15];
                    salesman_number = Integer.parseInt(parts[16]);
                    age = Integer.parseInt(parts[18]);
                    gender = parts[17];
                    mobile_phone = parts[19];

                    loadData(contract_number,client_enterprise,supply_center,country,city,industry,
                            product_code,product_name,product_model, unit_price,quantity,
                            contract_date,estimated_delivery_date,lodgement_date,director,
                            salesman,salesman_number,age,gender,mobile_phone);
                    cnt++;

                    if (cnt % BATCH_SIZE == 0) {
                        for (int i = 1; i <=2; i++) {

                            new Loader2().start();
                        }
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
            System.out.println(cnt + " records successfully loaded");
            System.out.println("Loading speed : "
                    + (cnt * 1000)/(end - start)
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
    public void run(){
        try {
            stmt.executeBatch();
            stmt.clearBatch();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
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
            stmt = con.prepareStatement(Loader);
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

    private static void loadData(String contract_number,String client_enterprise,String supply_center,
                                 String country, String city, String industry,String product_code,
                                 String product_name, String product_model, int unit_price,int quantity,
                                 String contract_date,String estimated_delivery_date,String lodgement_date,
                                 String director, String salesman,int salesman_number,
                                 int age,String gender,String mobile_phone)
            throws SQLException {
        if (con != null) {

                stmt.setString(1, contract_number);
                stmt.setString(2, client_enterprise);
                stmt.setString(3, supply_center);
                stmt.setString(4, country);
                stmt.setString(5, city);
                stmt.setString(6, industry);
                stmt.setString(7, product_code);
                stmt.setString(8, product_name);
                stmt.setString(9, product_model);
                stmt.setInt(10, unit_price);
                stmt.setInt(11, quantity);
                stmt.setString(12, contract_date);
                stmt.setString(13, estimated_delivery_date);
                stmt.setString(14, lodgement_date);
                stmt.setString(15, director);
                stmt.setString(16, salesman);
                stmt.setInt(17, salesman_number);
                stmt.setInt(18, age);
                stmt.setString(19, gender);
                stmt.setString(20, mobile_phone);


            stmt.addBatch();
        }
    }
}



