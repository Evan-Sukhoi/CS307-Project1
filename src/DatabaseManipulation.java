
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class DatabaseManipulation implements DataManipulation {
    private static Connection con = null;
    private ResultSet resultSet;

    private String host = "localhost";
    private String dbname = "postgres";
    private String user = "checker";
    private String pwd = "123456";
    private String port = "5432";

    private static PreparedStatement  stmt = null;
    private static final int  BATCH_SIZE = 100;
    private static String Loader =
            "insert into sustc(contract_number,client_enterprise,supply_center,country,city,industry," +
                    "product_code,product_name,product_model, unit_price,quantity," +
                    "contract_date,estimated_delivery_date,lodgement_date,director," +
                    "salesman,salesman_number,age,gender,mobile_phone)" +
                    "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    private static String fileName ="C:\\Users\\jimmylaw21\\cs307\\contract_info (1).csv";

    @Override
    public void openDatasource() {
        try {
            Class.forName("org.postgresql.Driver");

        } catch (Exception e) {
            System.err.println("Cannot find the PostgreSQL driver. Check CLASSPATH.");
            System.exit(1);
        }

        try {
            String url = "jdbc:postgresql://" + host + ":" + port + "/" + dbname;
            con = DriverManager.getConnection(url, user, pwd);

        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    @Override
    public void closeDatasource() {
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 预编译sql语句，在运行时相对应的？位置传递参数。
     */

    @Override
    public int addOneMovie(String str) {
        int result = 0;
        String sql = "insert into movies (title, country,year_released,runtime) " +
                "values (?,?,?,?)";
        String[] movieInfo = str.split(";");
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, movieInfo[0]);
            preparedStatement.setString(2, movieInfo[1]);
            preparedStatement.setInt(3, Integer.parseInt(movieInfo[2]));
            preparedStatement.setInt(4, Integer.parseInt(movieInfo[3]));
            System.out.println(preparedStatement.toString());

            result = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    public String allContinentNames() {
        StringBuilder sb = new StringBuilder();
        String sql = "select continent from countries group by continent";
        try {
            Statement statement = con.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                sb.append(resultSet.getString("continent")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Override
    public String continentsWithCountryCount() {
        StringBuilder sb = new StringBuilder();
        String sql = "select continent, count(*) countryNumber from countries group by continent;";
        try {
            Statement statement = con.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                sb.append(resultSet.getString("continent")).append("\t");
                sb.append(resultSet.getString("countryNumber"));
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Override
    public String FullInformationOfMoviesRuntime(int min, int max) {
        StringBuilder sb = new StringBuilder();
        String sql = "select m.title,c.country_name country,c.continent ,m.runtime " +
                "from movies m " +
                "join countries c on m.country=c.country_code " +
                "where m.runtime between ? and ? order by runtime;";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, min);
            preparedStatement.setInt(2, max);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sb.append(resultSet.getString("runtime")).append("\t");
                sb.append(String.format("%-18s", resultSet.getString("country")));
                sb.append(resultSet.getString("continent")).append("\t");
                sb.append(resultSet.getString("title")).append("\t");
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public String findMovieById(int id) {
        StringBuilder sb = new StringBuilder();
        String sql = "select m.title, c.country_name, m.year_released, m.runtime " +
                "from movies m\n" +
                "join countries c\n" +
                "on m.country = c.country_code\n" +
                "where m.movieid = ?;";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                sb.append(String.format("runtime:%-18s\n",resultSet.getString("runtime")));
                sb.append(String.format("country:%-18s\n",resultSet.getString("country_name")));sb.append(String.format("title:%-18s",resultSet.getString("title")));
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


    public String findSalesmanBySalesmanNumber(int salesman_number) {
        StringBuilder sb = new StringBuilder();
        String sql = "select salesman, salesman_number, gender, age, mobile_phone, supply_center from salesman where salesman_number = ?;";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1,salesman_number);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                sb.append(String.format("salesman:%s\n",resultSet.getString("salesman")));
                sb.append(String.format("salesman_number:%s\n",resultSet.getInt("salesman_number")));
                sb.append(String.format("gender:%s\n",resultSet.getString("gender")));
                sb.append(String.format("age:%d\n",resultSet.getInt("age")));
                sb.append(String.format("mobile_phone:%s\n",resultSet.getString("mobile_phone")));
                sb.append(String.format("supply_center:%s",resultSet.getString("supply_center")));
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public String findAllSalesman() {
        StringBuilder sb = new StringBuilder();
        String sql = "select salesman from salesman group by salesman";
        try {
            Statement statement = con.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                sb.append(resultSet.getString("salesman")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Override
    public String findAllSupplyCenter() {
        StringBuilder sb = new StringBuilder();
        String sql = "select supply_center from supply_center group by supply_center";
        try {
            Statement statement = con.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                sb.append(resultSet.getString("supply_center")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public String findAllProductModel() {
        StringBuilder sb = new StringBuilder();
        String sql = "select product_model from product_model group by product_model";
        try {
            Statement statement = con.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                sb.append(resultSet.getString("product_model")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public String findAllClientEnterprise() {
        StringBuilder sb = new StringBuilder();
        String sql = "select client_enterprise from client group by client_enterprise";
        try {
            Statement statement = con.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                sb.append(resultSet.getString("client_enterprise")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public String clientEnterpriseWithSupplyCenter() {
        return null;
    }

    @Override
    public String salesmanWithSupplyCenter() {
        StringBuilder sb = new StringBuilder();
        String sql = "select supply_center, count(*) salesmanAmount from salesman group by supply_center;";
        try {
            Statement statement = con.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                sb.append(resultSet.getString("supply_center")).append("\t");
                sb.append(resultSet.getString("salesmanAmount"));
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Override
    public String contractWithSupplyCenter() {
        return null;
    }

    @Override
    public int addOneSalesman(String str) {
        int result = 0;
        String sql = "insert into salesman (salesman_number,salesman,age,gender,mobile_phone,supply_center) " +
                "values (?,?,?,?,?,?)";
        String[] salesmanInfo = str.split(",");
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, Integer.parseInt(salesmanInfo[0]));
            preparedStatement.setString(2, salesmanInfo[1]);
            preparedStatement.setInt(3, Integer.parseInt(salesmanInfo[2]));
            preparedStatement.setString(4, salesmanInfo[3]);
            preparedStatement.setString(5, salesmanInfo[4]);
            preparedStatement.setString(6, salesmanInfo[5]);
            System.out.println(preparedStatement.toString());

            result = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public int addManySalesman(String str,int num1,int num2) {
        int result = 0;
        int cnt = 0;
        String sql = "insert into salesman (salesman_number,salesman,age,gender,mobile_phone,supply_center) " +
                "values (?,?,?,?,?,?)";
        String[] salesmanInfo = str.split(",");
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            for (int i = 0; i <= num2-num1; i++) {
                preparedStatement.setInt(1, Integer.parseInt(salesmanInfo[0]) + num1 + i);
                preparedStatement.setString(2, salesmanInfo[1]);
                preparedStatement.setInt(3, Integer.parseInt(salesmanInfo[2]));
                preparedStatement.setString(4, salesmanInfo[3]);
                preparedStatement.setString(5, salesmanInfo[4]);
                preparedStatement.setString(6, salesmanInfo[5]);
                System.out.println(preparedStatement.toString());
                preparedStatement.addBatch();
                cnt++;
                if (cnt % BATCH_SIZE == 0) {
                    preparedStatement.executeBatch();
                    preparedStatement.clearBatch();
                }
            }
            if (cnt % BATCH_SIZE != 0) {
                preparedStatement.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return num2 - num1;
    }


    @Override
    public int addOneSupplyCenter(String str) {
        return 0;
    }

    @Override
    public int addOneProductModel(String str) {
        return 0;
    }

    @Override
    public String deleteSalesmanByNumber(int number) {
        String sql = "delete from salesman where salesman_number = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, number);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "The salesman of "+number+" has been deleted";
    }

    @Override
    public String deleteManySalesmenByNumber(int number,int num1, int num2) {
        String sql = "delete from salesman where salesman_number = ?";
        int cnt = 0;
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            for (int i = 0; i <= num2-num1; i++) {
                preparedStatement.setInt(1, number + num1 + i);
                preparedStatement.addBatch();
                cnt++;
                if (cnt % BATCH_SIZE == 0) {
                    preparedStatement.executeBatch();
                    preparedStatement.clearBatch();
                }
            }
            if (cnt % BATCH_SIZE != 0) {
                preparedStatement.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "The salesmen between "+ (number +num1) +" and "+ (number + num2) +" have been deleted";
    }

    @Override
    public int updateSalesmenSupplyCenter(String supply_center, int number) {
        int resultSet = 0;
        String sql = "update salesman set supply_center = ? where salesman_number = ?;";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1,supply_center);
            preparedStatement.setInt(2,number);
            resultSet = preparedStatement.executeUpdate();
            System.out.println(preparedStatement.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }


    @Override
    public int addOneColumn(String contract_number,String client_enterprise,String supply_center,
                             String country, String city, String industry,String product_code,
                             String product_name, String product_model, int unit_price,int quantity,
                             String contract_date,String estimated_delivery_date,String lodgement_date,
                             String director, String salesman,int salesman_number,
                             int age,String gender,String mobile_phone) {
        int result = 0;
        String sql = Loader;
        try {
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
            System.out.println(stmt.toString());

            result = stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addManyColumn(int amount){
        try {
            stmt = con.prepareStatement(Loader);
        } catch (SQLException e) {
            System.err.println("Insert statement failed");
            System.err.println(e.getMessage());
            closeDatasource();
            System.exit(1);
        }
        try (BufferedReader infile
                     = new BufferedReader(new FileReader(fileName))) {
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
            Set product_model_list = new HashSet<>();
            int      cnt = 0;
            int      count = 0;
            start = System.currentTimeMillis();
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
                    count++;
                    if (cnt % BATCH_SIZE == 0) {
                        stmt.executeBatch();
                        stmt.clearBatch();
                    }
                }
                if(cnt > amount){
                    break;
                }
            }
            if (cnt % BATCH_SIZE != 0) {
                stmt.executeBatch();
            }
            con.commit();
            stmt.close();
            closeDatasource();
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
            closeDatasource();
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Fatal error: " + e.getMessage());
            try {
                con.rollback();
                closeDatasource();
            } catch (Exception e2) {
            }
            closeDatasource();
            System.exit(1);
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
