package DOA;

import domain.Adres;
import domain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO{
    private Connection conn;
    private ReizigerDAO rdao;

    public AdresDAOPsql(Connection conn, ReizigerDAO rdao) {
        this.conn = conn;
        this.rdao = rdao;
    }

    @Override
    public boolean save(Adres adres) {
        try {
            PreparedStatement prepStatement = conn.prepareStatement("INSERT INTO adres values(?, ?, ?, ?, ?,?)");
            prepStatement.setInt(1, adres.getId());
            prepStatement.setString(2, adres.getPostcode());
            prepStatement.setString(3, adres.getHuisnummer());
            prepStatement.setString(4, adres.getStraat());
            prepStatement.setString(5, adres.getWoonplaats());
            prepStatement.setInt(6, adres.getReiziger().getId());
            prepStatement.execute();

            prepStatement.close();

            return true;
        } catch(SQLException e){
            return false;
        }
    }

    @Override
    public boolean update(Adres adres) {
        try {
            PreparedStatement prepStatement = conn.prepareStatement("UPDATE adres SET postcode=?, huisnummer=?, straat=?, woonplaats=? WHERE adres_id=?");
            prepStatement.setString(1, adres.getPostcode());
            prepStatement.setString(2, adres.getHuisnummer());
            prepStatement.setString(3, adres.getStraat());
            prepStatement.setString(4, adres.getWoonplaats());
            prepStatement.setInt(5, adres.getId());
            prepStatement.execute();

            prepStatement.close();
            return true;
        } catch(SQLException e) {
            return false;
        }
    }

    @Override
    public boolean delete(Adres adres) {

        try {
            PreparedStatement prepStatement = conn.prepareStatement("DELETE FROM adres WHERE reiziger_id=?");
            prepStatement.setInt(1, adres.getId());
            prepStatement.execute();
            prepStatement.close();
            return true;
        } catch(SQLException e) {
            return false;
        }
    }

    @Override
    public Adres findById(int id) {

        try {
            String query = "SELECT * FROM adres WHERE adres_id = ?";
            PreparedStatement prepStatement = conn.prepareStatement(query);
            prepStatement.setInt(1, id);

            ResultSet result = prepStatement.executeQuery();

            result.next();
            String straat = result.getString("straat");
            String huisnummer = result.getString("huisnummer");
            String postcode = result.getString("postcode");
            String woonplaats = result.getString("woonplaats");
            Reiziger reizigerId = rdao.findById(result.getInt("reiziger_id"));

            prepStatement.close();
            result.close();
            return new Adres(id,straat,huisnummer,postcode,woonplaats,reizigerId);

        } catch(SQLException e) {
            System.out.println(e);
        }
         return null;
    }


    @Override
    public Adres findByReiziger(Reiziger reiziger) {

        try {
            PreparedStatement prepStatement = conn.prepareStatement("SELECT * FROM adres WHERE reiziger_id=?");
            prepStatement.setInt(1, reiziger.getId());

            ResultSet result = prepStatement.executeQuery();
            if(result.next()){

                int adresId = result.getInt("adres_id");
                String postcode = result.getString("postcode");
                String huisnummer = result.getString("huisnummer");
                String straat = result.getString("straat");
                String woonplaats = result.getString("woonplaats");


                return new Adres(adresId,postcode,huisnummer,straat,woonplaats,reiziger);
            }

            prepStatement.close();
            result.close();

        } catch(SQLException e) {
            System.out.println(e);
        }
        return null;
    }

    @Override
    public List<Adres> findAll() {

        List<Adres> adressen = new ArrayList<>();

        try {
            PreparedStatement prepStatement = conn.prepareStatement("SELECT * FROM adres");
            ResultSet result = prepStatement.executeQuery();
            while (result.next()) {
                Adres adres = new Adres(result.getInt("adres_id"), result.getString("postcode"), result.getString("huisnummer"), result.getString("straat"), result.getString("woonplaats"),rdao.findById(result.getInt("reiziger_id")));
                adressen.add(adres);
            }

            result.close();
            prepStatement.close();
            return adressen;

        } catch (SQLException e) {
            System.out.println(e);
        }
        return null;
    }
}