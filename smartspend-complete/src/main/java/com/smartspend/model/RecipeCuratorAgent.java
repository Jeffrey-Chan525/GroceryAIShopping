package com.smartspend.model;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;


public interface RecipeCuratorAgent {
    @UserMessage("""
            You are a Recipe Curator
            return a recipe you find from any given url
            you must only return the most significant recipe
            The url is as follows {{URL}}
            """)
    @Agent("Generates a Recipe from a URL")
    String getRecipe(@V("URL") String URL);

}
