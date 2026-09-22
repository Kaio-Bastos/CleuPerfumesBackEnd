package com.cleuperumes.CleuPerfumes.Model.Entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Collections;
import java.util.List;

@Converter(autoApply = false)
public class LoteConverter implements AttributeConverter<List<Lote>, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<Lote> lotes) {
        try {
            return objectMapper.writeValueAsString(lotes);
        } catch (Exception e) {
            return "[]";
        }
    }

    @Override
    public List<Lote> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) {
                return Collections.emptyList();
            }
            return objectMapper.readValue(dbData, new TypeReference<List<Lote>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}