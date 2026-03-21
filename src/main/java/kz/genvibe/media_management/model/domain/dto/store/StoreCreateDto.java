package kz.genvibe.media_management.model.domain.dto.store;

public record StoreCreateDto(
    String name,
    String location,
    String email
) { }
