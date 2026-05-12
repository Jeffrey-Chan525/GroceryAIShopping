package com.smartspend.controller;

import com.smartspend.service.AIChatbotService;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AiChatController extends BaseController {
    @FXML private TextField messageField;
    @FXML private TextArea transcriptArea;
    @FXML private Label helperLabel;

    private AIChatbotService chatbot;
    @FXML
    public void initialize() {
        chatbot = new AIChatbotService();
        transcriptArea.setText("AI: Hi! I can help optimise your grocery basket, compare stores, and suggest pantry-friendly swaps.\n\n" +
                "You: Which store is best this week?\nAI: Aldi is currently cheapest overall, while Woolworths has the best milk price.\n");
    }

    @FXML
    private void handleSend() {
        String text = messageField.getText() == null ? "" : messageField.getText().trim();
        if (text.isBlank()) {
            helperLabel.setText("Type a question before sending.");
            return;
        }
        ChatResponse chatResponse = chatbot.prompt(text);
        String yourPrompt = "You: " + text + "\n";
        String AIResponse = "AI: \n" + chatResponse.aiMessage().text();
        transcriptArea.setText(yourPrompt + AIResponse);
        messageField.clear();
    }
}
