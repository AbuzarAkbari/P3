import java.sql.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

public class Main {

    static Connection connection;

    public static void main(String[] args) throws SQLException {

        ReizigerDAOPsql rdao = new ReizigerDAOPsql(getConnection());

        AdresDAOPsql adao = new AdresDAOPsql(getConnection(),rdao);

        testReizigerDAO(rdao);

        testAdresDAO(adao);

        closeConnection();

    }

    private static Connection getConnection(){
        try {
            String url = "jdbc:postgresql://localhost/ovchip";
            Properties props = new Properties();
            props.setProperty("user","postgres");
            props.setProperty("password","VoetBal1223");
            connection = DriverManager.getConnection(url, props);

        } catch(Exception e ){
            System.out.println("connection failed");
        }
        return connection;
    }

    private static void closeConnection(){
        try {
            if (connection != null){
                connection.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    /**
     * P2. Reiziger DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de Reiziger DAO
     *
     * @throws SQLException
     */
    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
//        System.out.println("\n---------- Test ReizigerDAO -------------");
//
//        // Haal alle reizigers op uit de database
//        List<Reiziger> reizigers = rdao.findAll();
//        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
//        for (Reiziger r : reizigers) {
//            System.out.println(r);
//        }
//        System.out.println();
//
//        // Maak een nieuwe reiziger aan en persisteer deze in de database
//        String gbdatum = "1981-03-14";
//        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
//        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
//        rdao.save(sietske);
//        reizigers = rdao.findAll();
//        System.out.println(reizigers.size() + " reizigers\n");
//
//        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.
//
//        sietske.setVoorletters("A");
//        sietske.setTussenvoegsel(null);
//        sietske.setAchternaam("Akbari");
//        sietske.setGeboortedatum(java.sql.Date.valueOf("1999-08-01"));
//        rdao.update(sietske);

//        rdao.delete(sietske);

    }

    private static void testAdresDAO(AdresDAO adao) throws SQLException {
        ReizigerDAO rdao = new ReizigerDAOPsql(getConnection());

        System.out.println("\n---------- Test AdresDAO -------------");

        // Haal alle adressen op uit de database
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende adressen:");
        List<Adres> adressen = adao.findAll();
        for (Adres a : adressen) {
            System.out.println(a);
        }
        System.out.println();

        // Maak een nieuwe adres aan en persisteer deze in de database
        String gbdatum = "1999-08-01";
        Reiziger abuzar = new Reiziger(69, "A", "", "Akbari", java.sql.Date.valueOf(gbdatum));
        rdao.save(abuzar);
        Adres abuzarAdres = new Adres(69, "3721jl", "397", "Kometenlaan", "Utrecht", abuzar);
        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na AdresDAO.save() ");
        adao.save(abuzarAdres);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " adressen\n");


        // Voeg aanvullende tests van de ontbrekende CRUD-operaties in.


        System.out.println(adao.findById(69) + "\n");

        abuzarAdres.setHuisnummer("69");
        abuzarAdres.setPostcode("1234AB");
        adao.update(abuzarAdres);
        System.out.println(adao.findById(69) + "\n");


        adao.delete(abuzarAdres);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " adressen\n");

    }


}
