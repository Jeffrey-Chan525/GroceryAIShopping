package com.smartspend.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class OpenFoodFactsService {
    private static final String SEARCH_URL =
            "https://world.openfoodfacts.org/cgi/search.pl?search_terms=%s&search_simple=1&action=process&json=1&page_size=10";

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<ApiProduct> searchProducts(String searchTerm) throws Exception {
        String trimmed = searchTerm == null ? "" : searchTerm.trim();
        if (trimmed.isBlank()) {
            return List.of();
        }

        String encoded = URLEncoder.encode(trimmed, StandardCharsets.UTF_8);
        String urlString = String.format(SEARCH_URL, encoded);
        URL url = URI.create(urlString).toURL();

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("User-Agent", "SmartSpend/1.0 (student project)");
        connection.setConnectTimeout(10000);
        connection.setReadTimeout(10000);

        int status = connection.getResponseCode();
        if (status != 200) {
            throw new RuntimeException("Open Food Facts request failed with status code: " + status);
        }

        try (InputStream inputStream = connection.getInputStream()) {
            JsonNode root = objectMapper.readTree(inputStream);
            JsonNode productsNode = root.get("products");
            List<ApiProduct> results = new ArrayList<>();

            if (productsNode != null && productsNode.isArray()) {
                for (JsonNode productNode : productsNode) {
                    String name = getText(productNode, "product_name");
                    if (name.isBlank()) {
                        continue;
                    }

                    results.add(new ApiProduct(
                            getText(productNode, "code"),
                            name,
                            getText(productNode, "brands"),
                            getText(productNode, "quantity"),
                            getText(productNode, "categories")
                    ));
                }
            }
            return results;
        } finally {
            connection.disconnect();
        }
    }

    private String getText(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        return field != null && !field.isNull() ? field.asText() : "";
    }
}
