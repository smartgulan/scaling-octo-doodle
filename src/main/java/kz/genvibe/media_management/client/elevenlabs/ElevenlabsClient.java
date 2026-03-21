package kz.genvibe.media_management.client.elevenlabs;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/v1")
public interface ElevenlabsClient {

    @PostExchange("/text-to-speech/{voiceId}")
    void textToSpeech(@PathVariable String voiceId);
}
