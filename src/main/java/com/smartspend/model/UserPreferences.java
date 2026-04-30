package com.smartspend.model;

import java.util.Objects;

public class UserPreferences {
    private int preferenceId;
    private int userId;
    private double weeklyBudget;
    private String primaryStore;
    private boolean showSalePredictions;
    private boolean showValueSuggestions;

    public UserPreferences(int preferenceId, int userId, double weeklyBudget, String primaryStore, boolean showSalePredictions, boolean showValueSuggestions) {
        this.preferenceId = preferenceId;
        this.userId = userId;
        this.weeklyBudget = weeklyBudget;
        this.primaryStore = primaryStore;
        this.showSalePredictions = showSalePredictions;
        this.showValueSuggestions = showValueSuggestions;
    }

    public int getPreferenceId() { return preferenceId; }
    public int getUserId() { return userId; }
    public double getWeeklyBudget() { return weeklyBudget; }
    public String getPrimaryStore() { return primaryStore; }
    public boolean isShowSalePredictions() { return showSalePredictions; }
    public boolean isShowValueSuggestions() { return showValueSuggestions; }

    @Override
    public boolean equals(Object object){
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;

        UserPreferences userPreferences = (UserPreferences) object;
        boolean IsPreferenceIDEqual = this.preferenceId == userPreferences.preferenceId;
        boolean IsUserIDEqual = this.userId == userPreferences.userId;
        boolean IsWeeklyBudgetEqual = this.weeklyBudget == userPreferences.weeklyBudget;
        boolean IsPrimaryStoreEqual = this.primaryStore.equals(userPreferences.primaryStore);
        boolean IsOptionSalePredictionsEqual = this.showSalePredictions == userPreferences.showSalePredictions;
        boolean IsOptionValueSuggestionsEqual = this.showValueSuggestions == userPreferences.showValueSuggestions;
        return IsPreferenceIDEqual && IsUserIDEqual && IsWeeklyBudgetEqual && IsPrimaryStoreEqual && IsOptionValueSuggestionsEqual && IsOptionSalePredictionsEqual;
    }

    @Override
    public int hashCode(){
        return Objects.hash(preferenceId);
    }
}
