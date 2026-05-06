package com.smartspend.model;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.UntypedAgent;
import dev.langchain4j.agentic.observability.AgentListener;
import dev.langchain4j.agentic.observability.AgentRequest;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.service.AiServices;

import java.util.Map;

public class AI {
    private final ChatModel model;
    // this is the path which currently serves the ollama application
    private final String MODEL_BASE_PORT = "http://localhost:11434";
    // this is the name of the model i'm currently running
    private final String MODEL_NAME = "qwen3.5:latest";
    public AI() {
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
                .tools(new webScraper())
                .build();

        UntypedAgent controllerAgent = AgenticServices
                .sequenceBuilder()
                .subAgents(RecipeCurator)
                .outputKey("Recipe")
                .build();

//        String testing = "Say 'Hi this is Qwen running locally'";
//        System.out.print(prompt_model(testing));
        String url =  "https://www.recipetineats.com/egg-fried-rice/";
        Map<String, Object> input = Map.of("URL", url);
        String result = (String) controllerAgent.invoke(input);

        System.out.println(result);


    }

    private String prompt_model(String string){
        return model.chat(string);
    }
}

