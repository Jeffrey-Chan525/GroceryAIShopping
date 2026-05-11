package com.smartspend.dao;

import com.smartspend.model.UserPreferences;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PreferenceDao implements DAO<UserPreferences> {
    private static final Logger log = LoggerFactory.getLogger(PreferenceDao.class);
    private final Connection connection;

    // these are the default values for column values
    private final int PREFERENCE_ID = 1;
    private final int USER_ID = 2;
    private final int WEEKLY_BUDGET = 3;
    private final int PRIMARY_STORE = 4;
    private final int SHOW_SALE_PREDICTIONS = 5;
    private final int SHOW_VALUE_SUGGESTIONS = 6;

    public PreferenceDao(Connection connection){
        this.connection = connection;
    }

    @Override
    public void insert(UserPreferences value) {
        String query = "INSERT INTO user_preferences (preference_id, user_id, weekly_budget, primary_store, show_sale_predictions, show_value_suggestions) "
                + "VALUES (?,?,?,?,?,?)" ;

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(PREFERENCE_ID, value.getPreferenceId());
            preparedStatement.setInt(USER_ID, value.getUserId());
            preparedStatement.setDouble(WEEKLY_BUDGET, value.getWeeklyBudget());
            preparedStatement.setString(PRIMARY_STORE, value.getPrimaryStore());
            preparedStatement.setBoolean(SHOW_SALE_PREDICTIONS, value.isShowSalePredictions());
            preparedStatement.setBoolean(SHOW_VALUE_SUGGESTIONS, value.isShowValueSuggestions());
            preparedStatement.execute();
        }catch (SQLException e){
            log.error("an error has occurred when inserting UserPreferences", e);
        }


    }

    @Override
    public void update(UserPreferences value) {
        String query = "UPDATE user_preferences SET "
                + "user_id = ?, "
                + "weekly_budget = ?, "
                + "primary_store = ?, "
                + "show_sale_predictions = ?, "
                + "show_value_suggestions = ? "
                + "WHERE preference_id = ?";
        int USER_ID = 1;
        int WEEKLY_BUDGET = 2;
        int PRIMARY_STORE = 3;
        int SHOW_SALE_PREDICTIONS = 4;
        int SHOW_VALUE_SUGGESTIONS = 5;
        int PREFERENCE_ID = 6;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(USER_ID, value.getUserId());
            preparedStatement.setDouble(WEEKLY_BUDGET, value.getWeeklyBudget());
            preparedStatement.setString(PRIMARY_STORE, value.getPrimaryStore());
            preparedStatement.setBoolean(SHOW_SALE_PREDICTIONS, value.isShowSalePredictions());
            preparedStatement.setBoolean(SHOW_VALUE_SUGGESTIONS, value.isShowValueSuggestions());
            preparedStatement.setInt(PREFERENCE_ID, value.getPreferenceId());
            preparedStatement.execute();
        } catch (SQLException e){
            log.error("an error has occurred while updating into the user_preferences table", e);
        }
    }

    @Override
    public void delete(UserPreferences value) {
        String query = "DELETE FROM user_preferences WHERE preference_id = ?";

        int PREFERENCE_ID = 1;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(PREFERENCE_ID, value.getPreferenceId());
            preparedStatement.execute();
        } catch (SQLException e){
            log.error("an error has occurred while deleting from the user_preferences table", e);
        }
    }

    @Override
    public List<UserPreferences> getAll() {
        String query = "SELECT * FROM user_preferences";
        List<UserPreferences> userPreferences = new ArrayList<>();
        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                int preferenceID = resultSet.getInt(PREFERENCE_ID);
                int userID = resultSet.getInt(USER_ID);
                double weeklyBudget = resultSet.getDouble(WEEKLY_BUDGET);
                String primaryStore = resultSet.getString(PRIMARY_STORE);
                boolean showSalePredictions = resultSet.getBoolean(SHOW_SALE_PREDICTIONS);
                boolean showValueSuggestions = resultSet.getBoolean(SHOW_VALUE_SUGGESTIONS);
                UserPreferences currentUserPreference = new UserPreferences(preferenceID, userID, weeklyBudget, primaryStore, showSalePredictions, showValueSuggestions);
                userPreferences.add(currentUserPreference);
            }
        } catch (SQLException e){
            log.error("an error has occurred when selecting the entire user_preference table",e);
        }
        return userPreferences;
    }

    @Override
    public UserPreferences get(int id) {
        String query = "SELECT * FROM user_preferences where preference_id = ?";
        UserPreferences userPreferences = null;
        try{
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                int preferenceID = resultSet.getInt(PREFERENCE_ID);
                int userID = resultSet.getInt(USER_ID);
                int weeklyBudget = resultSet.getInt(WEEKLY_BUDGET);
                String primaryStore = resultSet.getString(PRIMARY_STORE);
                boolean showSalePrediction = resultSet.getBoolean(SHOW_SALE_PREDICTIONS);
                boolean showValuePrediction = resultSet.getBoolean(SHOW_VALUE_SUGGESTIONS);
                userPreferences = new UserPreferences(preferenceID, userID, weeklyBudget, primaryStore, showSalePrediction, showValuePrediction);
            }
        } catch (SQLException e){
            log.error("an error has occurred while selecting a record from user_preference table");
        }

        return userPreferences;
    }

}
