package com.smartspend.model;

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
}
