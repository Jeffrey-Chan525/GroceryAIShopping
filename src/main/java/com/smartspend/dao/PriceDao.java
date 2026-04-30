package com.smartspend.dao;

import com.smartspend.model.Price;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PriceDao implements DAO<Price>{
    private final Connection connection;

    public PriceDao(Connection connection){
        this.connection = connection;
    }
    // these are the default column values
    private final int PRICE_ID = 1;
    private final int ITEM_ID = 2;
    private final int STORE_NAME = 3;
    private final int PRICE = 4;
    private final int PACKAGE_QUANTITY = 5;
    private final int PACKAGE_UNIT = 6;
    private final int LAST_UPDATED = 7;
    private final int IS_ON_SALE = 8;


    private Price turnResultSetToPrice(ResultSet resultSet) throws SQLException{
        int priceID = resultSet.getInt(PRICE_ID);
        int itemID = resultSet.getInt(ITEM_ID);
        String storeName = resultSet.getString(STORE_NAME);
        double price = resultSet.getDouble(PRICE);
        double packageQuantity = resultSet.getDouble(PACKAGE_QUANTITY);
        String packageUnit = resultSet.getString(PACKAGE_UNIT);
        String lastUpdated = resultSet.getString(LAST_UPDATED);
        boolean isOnSale = resultSet.getBoolean(IS_ON_SALE);
        return new Price(priceID, itemID, storeName, price, packageQuantity, packageUnit, lastUpdated, isOnSale);
    }

    @Override
    public void insert(Price value) {
        String query = "INSERT INTO prices (price_id, item_id, store_name, price, package_quantity, package_unit, last_updated, is_on_sale) " +
                "VALUES (?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(PRICE_ID, value.getPriceId());
            preparedStatement.setInt(ITEM_ID, value.getItemId());
            preparedStatement.setString(STORE_NAME, value.getStoreName());
            preparedStatement.setDouble(PRICE, value.getPrice());
            preparedStatement.setDouble(PACKAGE_QUANTITY, value.getPackageQuantity());
            preparedStatement.setString(PACKAGE_UNIT, value.getPackageUnit());
            preparedStatement.setString(LAST_UPDATED, value.getLastUpdated());
            preparedStatement.setBoolean(IS_ON_SALE, value.isOnSale());
            preparedStatement.execute();
        } catch (SQLException e){
            System.err.print("an error has occurred when inserting into the prices table" + e);
        }
    }

    @Override
    public void update(Price value) {
        String query = "UPDATE prices SET " +
                "item_id = ?, " +
                "store_name = ?, " +
                "price = ?, " +
                "package_quantity = ?, " +
                "package_unit = ?, " +
                "last_updated = ?, " +
                "is_on_sale = ? " +
                "WHERE price_id = ?";
        // these are local column values
        int ITEM_ID = 1;
        int STORE_NAME = 2;
        int PRICE = 3;
        int PACKAGE_QUANTITY = 4;
        int PACKAGE_UNIT = 5;
        int LAST_UPDATED = 6;
        int IS_ON_SALE = 7;
        int PRICE_ID = 8;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(ITEM_ID, value.getItemId());
            preparedStatement.setString(STORE_NAME, value.getStoreName());
            preparedStatement.setDouble(PRICE, value.getPrice());
            preparedStatement.setDouble(PACKAGE_QUANTITY, value.getPackageQuantity());
            preparedStatement.setString(PACKAGE_UNIT, value.getPackageUnit());
            preparedStatement.setString(LAST_UPDATED, value.getLastUpdated());
            preparedStatement.setBoolean(IS_ON_SALE, value.isOnSale());
            preparedStatement.setInt(PRICE_ID, value.getPriceId());
            preparedStatement.execute();
        } catch (SQLException e){
            System.err.print("and error has occurred when updating a record from the prices table" + e);
        }
    }

    @Override
    public void delete(Price value) {
        String query = "DELETE FROM prices WHERE price_id = ?";
        try{
           PreparedStatement preparedStatement = connection.prepareStatement(query);
           preparedStatement.setInt(PRICE_ID, value.getPriceId());
           preparedStatement.execute();
        }catch (SQLException e){
            System.err.print("an error has occurred when deleting from the prices table" + e);
        }
    }

    @Override
    public List<Price> getAll() {
        String query = "SELECT * FROM prices";
        List<Price> prices = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                int priceID = resultSet.getInt(PRICE_ID);
                int itemID = resultSet.getInt(ITEM_ID);
                String storeName = resultSet.getString(STORE_NAME);
                double price = resultSet.getDouble(PRICE);
                double packageQuantity = resultSet.getDouble(PACKAGE_QUANTITY);
                String packageUnit = resultSet.getString(PACKAGE_UNIT);
                String lastUpdated = resultSet.getString(LAST_UPDATED);
                boolean isOnSale = resultSet.getBoolean(IS_ON_SALE);
                Price currentPrice= new Price(priceID, itemID, storeName, price, packageQuantity, packageUnit, lastUpdated, isOnSale);
                prices.add(currentPrice);
            }
        } catch (SQLException e){
            System.err.print("an error has occurred when selecting the entire prices table" + e);
        }
        return prices;
    }

    @Override
    public Price get(int id) {
        String query = "SELECT * FROM prices WHERE price_id = ?";
        Price price = null;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(PRICE_ID, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            price = turnResultSetToPrice(resultSet);
        }catch (SQLException e){
            System.err.print("an error has occurred while trying to get a record from the prices table" + e);
        }

        return price;
    }
}
