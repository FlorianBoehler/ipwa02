package com.example.ipwa02_07.converters;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@FacesConverter("localDateTimeConverter")
public class LocalDateTimeConverter implements Converter {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return LocalDateTime.parse(value, formatter);
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value == null) {
            return "";
        }
        return formatter.format((LocalDateTime) value);
    }
}