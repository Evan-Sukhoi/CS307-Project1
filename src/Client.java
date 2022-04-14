
public class Client {

    public static void main(String[] args) {
        try {
            long     start;
            long     end;
            start = System.currentTimeMillis();
            DataManipulation dm = new DataFactory().createDataManipulation(args[0]);
            dm.openDatasource();
//            dm.addOneMovie("流浪地球;cn;2019;127");
//            System.out.println(dm.allContinentNames());
//            System.out.println(dm.continentsWithCountryCount());
//            System.out.println(dm.FullInformationOfMoviesRuntime(65, 75));
//            System.out.println(dm.findMovieById(10));
//            System.out.println(dm.findSalesmanBySalesmanNumber(12112517));
            //System.out.println(dm.findAllProductModel());
            System.out.println(dm.updateSalesmenSupplyCenter("Southwestern China",12113000));
            //System.out.println(dm.addOneSalesman("12112517,Luo Haoyu,19,Male,13528855890,Southern China"));
            //System.out.println(dm.deleteSalesmanByNumber(12112517));
            //System.out.println(dm.addManySalesman("12113000,Luo Haoyu,19,Male,13528855890,Southern China",0,50000));
            //System.out.println(dm.deleteManySalesmenByNumber(12113000,0,50000));
            dm.closeDatasource();
            end = System.currentTimeMillis();
            System.out.println(end-start + "ms");
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}

