package com.smartspend.model.AI;

import com.smartspend.service.webScraperService;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.observability.AgentListener;
import dev.langchain4j.agentic.observability.AgentRequest;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

import java.util.Map;

public class ShoppingListCreatorAgent {
    /**
     * the model which represents the LLM
     */
    private final ChatModel model;
    // this is the path which currently serves the ollama application
    private final String MODEL_BASE_PORT = "http://localhost:11434";
    // this is the name of the model i'm currently running
    private final String MODEL_NAME = "qwen3.5:latest";
    private UntypedAgent controllerAgent;

    /**
     * this initializes the AIChatbot
     */
    public ShoppingListCreatorAgent() {
        model = OllamaChatModel.builder()
                .baseUrl(MODEL_BASE_PORT)
                .modelName(MODEL_NAME)
                .build();

        RecipeCuratorAgent RecipeCurator = AgenticServices.agentBuilder(RecipeCuratorAgent.class)
                .chatModel(model)
                .outputKey("Recipe")
                .listener(new AgentListener() {
                    @Override
                    public void beforeAgentInvocation(AgentRequest agentRequest) {
                        System.out.println("Scraping URL: " + agentRequest.inputs().get("URL"));
                    }
                })
                .tools(new webScraperService())
                .build();

        controllerAgent = AgenticServices
                .sequenceBuilder()
                .subAgents(RecipeCurator)
                .outputKey("Recipe")
                .build();

    }

    /**
     * this prompts the model with a given string
     * @param question the prompt which will be sent to the model
     * @return
     */
    public String prompt_model(String question){
        Map<String, Object> input = Map.of("getRecipe", question);
        return (String) controllerAgent.invoke(input);
    }
}

