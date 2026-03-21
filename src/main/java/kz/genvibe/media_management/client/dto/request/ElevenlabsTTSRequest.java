package kz.genvibe.media_management.client.dto.request;

public record ElevenlabsTTSRequest(
    String text,
    String model_id
) { }
