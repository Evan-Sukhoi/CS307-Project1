import java.io.*;
import java.util.*;

public class FileManipulation implements DataManipulation {

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
        return null;
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
        return 0;
    }

    @Override
    public int addManySalesman(String str) {
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
    public String deleteSalesmanByNumber(int number) {
        return null;
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
