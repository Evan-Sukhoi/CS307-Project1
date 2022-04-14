public interface DataManipulation {

    public void openDatasource();
    public void closeDatasource();
    public int addOneMovie(String str);
    public String allContinentNames();
    public String continentsWithCountryCount();
    public String FullInformationOfMoviesRuntime(int min, int max);
    public String findMovieById(int id);

    public int addOneColumn(String contract_number,String client_enterprise,String supply_center,
                            String country, String city, String industry,String product_code,
                            String product_name, String product_model, int unit_price,int quantity,
                            String contract_date,String estimated_delivery_date,String lodgement_date,
                            String director, String salesman,int salesman_number,
                            int age,String gender,String mobile_phone);
    public void addManyColumn(int amount);

    public String findSalesmanBySalesmanNumber(int salesman_number);

    public String findAllSalesman();

    public String findAllSupplyCenter();

    public String findAllProductModel();

    public String findAllClientEnterprise();

    public String clientEnterpriseWithSupplyCenter();

    public String salesmanWithSupplyCenter();

    public String contractWithSupplyCenter();

    public int addOneSalesman(String str);

    public int addManySalesman(String str);

    public int addOneSupplyCenter(String str);

    public int addOneProductModel(String str);

    public String deleteSalesmanByNumber(int number);


}
