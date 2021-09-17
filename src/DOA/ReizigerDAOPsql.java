package DOA;

import domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO{

    private Connection conn;
    private AdresDAO adao;

    public ReizigerDAOPsql(Connection conn){
        this.conn = conn;
        this.adao = new AdresDAOPsql(conn,this);
    }


    @Override
    public boolean save(Reiziger reiziger) {


        try {
            PreparedStatement prepStatement = conn.prepareStatement("INSERT INTO REIZIGER (reiziger_id,voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?,?)");

            prepStatement.setInt(1, reiziger.getId());
            prepStatement.setString(2, reiziger.getVoorletters());
            prepStatement.setString(3, reiziger.getTussenvoegsel());
            prepStatement.setString(4, reiziger.getAchternaam());
            prepStatement.setDate(5, reiziger.getGeboortedatum());

            prepStatement.executeQuery();

            prepStatement.close();

            if (reiziger.getAdres() != null){
                adao.save(reiziger.getAdres());
            }

            return true;

        } catch(SQLException e) {
            System.out.println(e);
        }

        return false;
    }

    @Override
    public boolean update(Reiziger reiziger) {
        try {
            PreparedStatement prepStatement = conn.prepareStatement("UPDATE reiziger SET voorletters=?, tussenvoegsel=?,achternaam=?,geboortedatum=? WHERE reiziger_id = ?");

            prepStatement.setString(1, reiziger.getVoorletters());
            prepStatement.setString(2, reiziger.getTussenvoegsel());
            prepStatement.setString(3, reiziger.getAchternaam());
            prepStatement.setDate(4, reiziger.getGeboortedatum());
            prepStatement.setInt(5, reiziger.getId());

            prepStatement.executeQuery();
            prepStatement.close();

            if (reiziger.getAdres() != null){
                if (adao.findByReiziger(reiziger) == null){
                    adao.save(reiziger.getAdres());
                }
            }



            return true;
        } catch(SQLException e) {
            System.out.println(e);
        }

        return false;
    }

    @Override
    public boolean delete(Reiziger reiziger) {
            try{
                PreparedStatement prepStatement = conn.prepareStatement("DELETE FROM reiziger WHERE reiziger_id = ?");
                prepStatement.setInt(1, reiziger.getId());
                prepStatement.executeQuery();


                if (adao.findByReiziger(reiziger) != null){
                    adao.delete(reiziger.getAdres());
                }



                prepStatement.close();
                return true;
            } catch (SQLException e) {
                System.out.println(e);
            }

        return false;
    }

    @Override
    public Reiziger findById(int id) {
        try{
            PreparedStatement prepStatement = conn.prepareStatement("SELECT * FROM reiziger WHERE reiziger_id = ?");
            prepStatement.setInt(1, id);

            ResultSet result = prepStatement.executeQuery();
            result.next();

            Reiziger reiziger = null;


            while (result.next()) {

                int reizigerId = result.getInt("reiziger_id");
                String voorletters = result.getString("voorletters");
                String tussenvoegsel = result.getString("tussenvoegsel");
                String achternaam = result.getString("achternaam");
                Date geboortedatum = result.getDate("geboortedatum");

                prepStatement.close();
                result.close();

                reiziger = new Reiziger(reizigerId,voorletters,tussenvoegsel,achternaam,geboortedatum);
                adao.findByReiziger(reiziger);

            }

            return reiziger;

        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<Reiziger> findByGbdatum(String datum) {
        List<Reiziger> reizigers = new ArrayList<>();

        try {
            PreparedStatement prepStatement = conn.prepareStatement("SELECT * FROM reiziger WHERE geboortedatum = TO_DATE(?,'YYYY-MM-DD HH24:MI:SS')");

            prepStatement.setString(1, datum);

            ResultSet result = prepStatement.executeQuery();
            while(result.next()) {
                Reiziger reiziger = new Reiziger( result.getInt("reiziger_id"),result.getString("voorletters"), result.getString("tussenvoegsel"), result.getString("achternaam"), result.getDate("geboortedatum"));
                // voeg if toe als adao null is

                adao.findByReiziger(reiziger);
                reizigers.add(reiziger);
            }

            result.close();
            prepStatement.close();


        } catch(SQLException e) {
            System.out.println(e);
        }

        return reizigers;
    }

    @Override
    public List<Reiziger> findAll() {

        List<Reiziger> reizigers = new ArrayList<>();

        try{
            PreparedStatement prepStatement = conn.prepareStatement("SELECT * FROM reiziger");
            ResultSet result = prepStatement.executeQuery();
            while (result.next()){
                Reiziger reiziger = new Reiziger(result.getInt("reiziger_id"),result.getString("voorletters"),result.getString("tussenvoegsel"),result.getString("achternaam"),result.getDate("geboortedatum"));
                // voeg if toe als adao null is

                adao.findByReiziger(reiziger);
                reizigers.add(reiziger);
            }

            result.close();
            prepStatement.close();
            return reizigers;
        } catch (SQLException e) {
            System.out.println(e);
        }

        return reizigers;
    }
}

