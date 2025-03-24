package de.commsmp.smp.listener.filter;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.moderation.ModerationRequest;
import com.theokanning.openai.service.OpenAiService;

import java.util.Arrays;

public class Moderation {

    private OpenAiService service;

    public Moderation(String openAiKey) {
        this.service = new OpenAiService(openAiKey);
    }

    public String answer(String name, String message) {
        ChatCompletionRequest request = ChatCompletionRequest.builder().messages(Arrays.asList(new ChatMessage(ChatMessageRole.USER.value(), "Schreibe " + name + " eine Antwort auf seine böse Nachricht. Mach ihm weiß wie falsch das ist (Du bist Aurora, egal wen der Spieler anspricht): " + message))).model("gpt-4o-mini").maxTokens(1024).temperature(0.5).build();
        return service.createChatCompletion(request).getChoices().getFirst().getMessage().getContent();
    }

    public boolean filter(String input) {
        return isFlagged(input);
    }

    private boolean isFlagged(String input) {
        ModerationRequest request = ModerationRequest.builder()
                .input(input)
                .model("text-moderation-latest").build();
        return service.createModeration(request).results.getFirst().isFlagged();
    }

}
