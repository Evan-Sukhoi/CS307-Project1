import java.io.*;
import java.util.*;

public class FileManipulation implements DataManipulation {

    String orders ="postgres_public_orders.csv";
    String salesman ="postgres_public_salesman.csv";

    @Override
    public void openDatasource() {

    }

    @Override
    public void closeDatasource() {

    }

    @Override
    public int addOneMovie(String str) {
        try (FileWriter writer = new FileWriter("movies.txt", true)) {
            writer.write(str);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    @Override
    public void addManyColumn(int amount) {

    }

    @Override
    public String findSalesmanBySalesmanNumber(int salesman_number) {
        return null;
    }

    @Override
    public String findAllSalesman() {
        return null;
    }

    @Override
    public String findAllSupplyCenter() {
        String line;
        int supplyCenterIndex = 5;
        Set<String> supplyCenterNames = new HashSet<>();
        StringBuilder sb = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(salesman))) {
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                line = line.split(",")[supplyCenterIndex];
                if (!supplyCenterNames.contains(line)) {
                    sb.append(line).append("\n");
                    supplyCenterNames.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Override
    public String findAllProductModel() {
        return null;
    }

    @Override
    public String findAllClientEnterprise() {
        return null;
    }

    @Override
    public String clientEnterpriseWithSupplyCenter() {
        return null;
    }

    @Override
    public String salesmanWithSupplyCenter() {
        return null;
    }

    @Override
    public String contractWithSupplyCenter() {
        return null;
    }

    @Override
    public int addOneSalesman(String str) {
        try (BufferedWriter writer
                     = new BufferedWriter(new FileWriter(salesman,true))) {
            writer.write(str);
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }

    @Override
    public int addManySalesman(String str,int num1, int num2) {
        return 0;
    }

    @Override
    public int addManyOrder(String str, int num1, int num2) {
        return 0;
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
    public int addOneOrder(String str) {
        try (BufferedWriter writer
                     = new BufferedWriter(new FileWriter(orders,true))) {
            writer.write(str);
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return 1;
    }



    @Override
    public String deleteSalesmanByNumber(int number) throws IOException {
        String[] salesmaninf = new String[6];
        String[] salesmaninf1 = new String[6];
        File file = new File(salesman);
        FileInputStream intput = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(intput));
        String tempString;//定义一个字符串，每一次读出该行字符串内容
        List<String> list = new ArrayList<>();//定义一个list字符串集合用来储存每一行的字符串信息
        while ((tempString = reader.readLine()) != null) {
            list.add(tempString);
        }
        //遍历字符串集合
        for (String order : list) {
            salesmaninf = order.split(",");//将‘,‘作为分隔符，将字符串分隔开存放进入数组中
            System.out.print(salesmaninf[0] + " ");
        }

        for (String delBook : list) {
            salesmaninf1 = delBook.split(",");
            //找到即将删除的书籍在集合中的位置，将该部分内容从集合中删除，然后清空整个文件
            if (Integer.toString(number).equals(salesmaninf1[0])) {
                list.remove(delBook);//在集合中删除该行
                FileWriter fd = new FileWriter(file, false);//append传入false表示写入内容时将会覆盖文件中之前存在的内容
                fd.write("");//执行删除操作，写入空内容覆盖之前的内容
                fd.close();
                break;
            }
        }

        //重新遍历一遍更改后的集合，将内容重新写入文件内
        for (String user : list) {
            salesmaninf1 = user.split(",");
            FileWriter fw = new FileWriter(file, true);//append传入true表示写入内容时将不会覆盖文件中之前存在的内容，将新的内容写在之前内容的后面
            fw.write(salesmaninf1[0] + "," + salesmaninf1[1] +
                    "," + salesmaninf1[2] + "," + salesmaninf1[3] +
                    "," + salesmaninf1[4] + "," + salesmaninf1[5]);//执行重新写入内容的操作，将修改过的集合通过数组读下标后，再重新存写入文件中
            fw.write(System.getProperty("line.separator"));//在段落后添加一个换行符
            fw.close();
        }

        return "The salesman of "+number+" has been deleted";
    }

    @Override
    public String deleteOrderBySalesmanNumber(int number) throws IOException {
        String[] orderinf = new String[7];
        String[] orderinf1 = new String[7];
        File file = new File(orders);
        FileInputStream intput = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(intput));
        String tempString;//定义一个字符串，每一次读出该行字符串内容
        List<String> list = new ArrayList<>();//定义一个list字符串集合用来储存每一行的字符串信息
        while ((tempString = reader.readLine()) != null) {
            list.add(tempString);
        }
        //遍历字符串集合
        for (String order : list) {
            orderinf = order.split(",");//将‘,‘作为分隔符，将字符串分隔开存放进入数组中
            System.out.print(orderinf[3] + " ");
        }

        for (String delBook : list) {
            orderinf1 = delBook.split(",");
            //找到即将删除的书籍在集合中的位置，将该部分内容从集合中删除，然后清空整个文件
            if (Integer.toString(number).equals(orderinf1[3])) {
                list.remove(delBook);//在集合中删除该行
                FileWriter fd = new FileWriter(file, false);//append传入false表示写入内容时将会覆盖文件中之前存在的内容
                fd.write("");//执行删除操作，写入空内容覆盖之前的内容
                fd.close();
                break;
            }
        }

        //重新遍历一遍更改后的集合，将内容重新写入文件内
        for (String user : list) {
            orderinf1 = user.split(",");
            FileWriter fw = new FileWriter(file, true);//append传入true表示写入内容时将不会覆盖文件中之前存在的内容，将新的内容写在之前内容的后面
            fw.write(orderinf1[0] + "," + orderinf1[1] +
                    "," + orderinf1[2] + "," + orderinf1[3] +
                    "," + orderinf1[4] + "," + orderinf1[5] +
                    "," + orderinf1[6]);//执行重新写入内容的操作，将修改过的集合通过数组读下标后，再重新存写入文件中
            fw.write(System.getProperty("line.separator"));//在段落后添加一个换行符
            fw.close();
        }

        return "The order of "+number+" has been deleted";
    }


    @Override
    public String deleteManySalesmenByNumber(int number,int num1, int num2) {
        return null;
    }

    @Override
    public int updateSalesmenSupplyCenter(String supply_center, int number) throws IOException {
        String[] salesmaninf = new String[6];
        String[] salesmaninf1 = new String[6];
        File file = new File(salesman);
        FileInputStream intput = new FileInputStream(file);
        BufferedReader reader = new BufferedReader(new InputStreamReader(intput));
        String tempString;//定义一个字符串，每一次读出该行字符串内容
        List<String> list = new ArrayList<>();//定义一个list字符串集合用来储存每一行的字符串信息
        while ((tempString = reader.readLine()) != null) {
            list.add(tempString);
        }
        //遍历字符串集合
        for (String order : list) {
            salesmaninf = order.split(",");//将‘,‘作为分隔符，将字符串分隔开存放进入数组中
            System.out.print(salesmaninf[0] + " ");
        }

        for (String delBook : list) {
            salesmaninf1 = delBook.split(",");
            //找到即将删除的书籍在集合中的位置，将该部分内容从集合中删除，然后清空整个文件
            if (Integer.toString(number).equals(salesmaninf1[0])) {
                list.remove(delBook);//在集合中删除该行
                list.add(salesmaninf1[0] + "," + salesmaninf1[1] +
                        "," + salesmaninf1[2] + "," + salesmaninf1[3] +
                        "," + salesmaninf1[4] + "," + supply_center);
                FileWriter fd = new FileWriter(file, false);//append传入false表示写入内容时将会覆盖文件中之前存在的内容
                fd.write("");//执行删除操作，写入空内容覆盖之前的内容
                fd.close();
                break;
            }
        }

        //重新遍历一遍更改后的集合，将内容重新写入文件内
        for (String user : list) {
            salesmaninf1 = user.split(",");
            FileWriter fw = new FileWriter(file, true);//append传入true表示写入内容时将不会覆盖文件中之前存在的内容，将新的内容写在之前内容的后面
            fw.write(salesmaninf1[0] + "," + salesmaninf1[1] +
                    "," + salesmaninf1[2] + "," + salesmaninf1[3] +
                    "," + salesmaninf1[4] + "," + salesmaninf1[5]);//执行重新写入内容的操作，将修改过的集合通过数组读下标后，再重新存写入文件中
            fw.write(System.getProperty("line.separator"));//在段落后添加一个换行符
            fw.close();
        }
        return 0;
    }

    @Override
    public String salesmanWithOrderCount() {
        String line;
        int salesmanIndex = 3;
        Map<String, Integer> salesmanCount = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(orders))) {
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                line = line.split(",")[salesmanIndex];
                if (salesmanCount.containsKey(line)) {
                    salesmanCount.put(line, salesmanCount.get(line) + 1);
                } else {
                    salesmanCount.put(line, 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, Integer> entry : salesmanCount.entrySet()) {
            sb.append(entry.getKey())
                    .append("\t")
                    .append(entry.getValue())
                    .append("\n");
        }

        return sb.toString();
    }

    @Override
    public String allContinentNames() {
        String line;
        int continentIndex = 2;
        Set<String> continentNames = new HashSet<>();
        StringBuilder sb = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("countries.txt"))) {
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                line = line.split(";")[continentIndex];
                if (!continentNames.contains(line)) {
                    sb.append(line).append("\n");
                    continentNames.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Override
    public String continentsWithCountryCount() {
        String line;
        int continentIndex = 2;
        Map<String, Integer> continentCount = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("countries.txt"))) {
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                line = line.split(";")[continentIndex];
                if (continentCount.containsKey(line)) {
                    continentCount.put(line, continentCount.get(line) + 1);
                } else {
                    continentCount.put(line, 1);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, Integer> entry : continentCount.entrySet()) {
            sb.append(entry.getKey())
                    .append("\t")
                    .append(entry.getValue())
                    .append("\n");
        }

        return sb.toString();
    }

    private Map<String, String> getCountryMap() {
        String line;
        String[] splitArray;
        int countryCodeIndex = 0, countryNameIndex = 1, continentIndex = 2;
        Map<String, String> rst = new HashMap<>();

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("countries.txt"))) {
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                splitArray = line.split(";");
                rst.put(splitArray[countryCodeIndex].trim(), String.format("%-18s", splitArray[countryNameIndex])
                        + splitArray[continentIndex]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rst;
    }

    private List<FullInformation> getFullInformation(Map<String, String> countryMap, int min, int max) {
        String line;
        String[] splitArray;
        List<FullInformation> list = new ArrayList<>();
        int titleIndex = 1, countryIndex = 2, runTimeIndex = 4, runTime;

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("movies.txt"))) {
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                splitArray = line.split(";");

                if (!"null".equals(splitArray[runTimeIndex])) {
                    runTime = Integer.parseInt(splitArray[runTimeIndex]);
                    if (runTime >= min && runTime <= max) {
                        line = runTime + "\t" + countryMap.get(splitArray[countryIndex].trim()) + "\t"
                                + splitArray[titleIndex] + "\n";
                        list.add(new FullInformation(runTime, line));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    @Override
    public String FullInformationOfMoviesRuntime(int min, int max) {
        Map<String, String> countryMap = getCountryMap();
        List<FullInformation> list = getFullInformation(countryMap, min, max);
        list.sort(Comparator.comparing(f -> f.runTime));

        StringBuilder sb = new StringBuilder();
        for (FullInformation f : list) {
            sb.append(f.information);
        }

        return sb.toString();
    }

    @Override
    public String findMovieById(int id) {
        return null;
    }

    @Override
    public int addOneColumn(String contract_number, String client_enterprise, String supply_center, String country, String city, String industry, String product_code, String product_name, String product_model, int unit_price, int quantity, String contract_date, String estimated_delivery_date, String lodgement_date, String director, String salesman, int salesman_number, int age, String gender, String mobile_phone) {
        return 0;
    }

    class FullInformation {
        int runTime;
        String information;

        FullInformation(int runTime, String information) {
            this.runTime = runTime;
            this.information = information;
        }
    }
}
