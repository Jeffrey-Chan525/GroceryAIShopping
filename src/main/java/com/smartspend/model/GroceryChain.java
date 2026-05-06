package com.smartspend.model;

public enum GroceryChain {

        WOOLWORTHS(1),
        COLES(2);

        private final int chainId;

        GroceryChain(int chainId) {
            this.chainId = chainId;
        }

        public int getChainId() {
            return chainId;
        }
}
