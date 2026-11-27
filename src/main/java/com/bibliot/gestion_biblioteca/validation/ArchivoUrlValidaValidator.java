package com.bibliot.gestion_biblioteca.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.regex.Pattern;

public class ArchivoUrlValidaValidator implements ConstraintValidator<ArchivoUrlValida, String> {
    private static final Pattern PDF_LOCAL = Pattern.compile("^/uploads/pdfs/[A-Za-z0-9_-]+\\.pdf$");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true;
        if (PDF_LOCAL.matcher(value).matches()) return true;

        try {
            URI uri = new URI(value);
            String scheme = uri.getScheme();
            return scheme != null
                    && (scheme.toLowerCase(Locale.ROOT).equals("http")
                            || scheme.toLowerCase(Locale.ROOT).equals("https"))
                    && uri.getHost() != null
                    && !uri.getHost().isBlank()
                    && uri.getUserInfo() == null;
        } catch (URISyntaxException ex) {
            return false;
        }
    }
}
