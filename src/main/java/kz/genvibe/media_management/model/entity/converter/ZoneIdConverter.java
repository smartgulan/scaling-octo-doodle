package kz.genvibe.media_management.model.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.ZoneId;

/**
 * Persists {@link ZoneId} as its IANA id (e.g. {@code "Asia/Almaty"}). Applied automatically
 * to every {@code ZoneId} attribute so store-level timezones round-trip transparently.
 */
@Converter(autoApply = true)
public class ZoneIdConverter implements AttributeConverter<ZoneId, String> {

    @Override
    public String convertToDatabaseColumn(ZoneId zoneId) {
        return zoneId == null ? null : zoneId.getId();
    }

    @Override
    public ZoneId convertToEntityAttribute(String dbData) {
        return dbData == null ? null : ZoneId.of(dbData);
    }
}
