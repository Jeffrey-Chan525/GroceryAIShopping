package com.smartspend.model;

public class GroceryStore {

    private int storeId;
    private GroceryChain chain;
    private String location;

    public GroceryStore(int storeId, GroceryChain chain, String location) {

        this.storeId = storeId;
        this.chain = chain;
        this.location = location;
    }

    //Getters
    public int getStoreId() { return storeId; }
    public GroceryChain getChain() { return chain; }
    public String getLocation() { return location; }

    //Helper to get the ID
    public int getChainId() {
        return chain.getChainId(); }
}
