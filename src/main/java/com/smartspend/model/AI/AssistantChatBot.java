package com.smartspend.model.AI;

import dev.langchain4j.service.TokenStream;

public interface AssistantChatBot {
    TokenStream chat(String message);
}
