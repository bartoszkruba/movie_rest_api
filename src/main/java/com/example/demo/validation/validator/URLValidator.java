package com.example.demo.validation.validator;

import com.example.demo.validation.constraint.ValidURLConstraint;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@NoArgsConstructor
public class URLValidator implements ConstraintValidator<ValidURLConstraint, String> {

    @Override
    public void initialize(ValidURLConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(String field, ConstraintValidatorContext constraintValidatorContext) {
        String regex = "\\b(https?|ftp|file)://[-A-Z0-9+&@#/%?=~_|!:,.;]*[-A-Z0-9+&@#/%=~_|]";

        return URLValidator.IsMatch(field, regex);
    }

    private static boolean IsMatch(String s, String pattern) {
        try {
            Pattern patt = Pattern.compile(pattern);
            Matcher matcher = patt.matcher(s);
            return matcher.matches();
        } catch (RuntimeException e) {
            return false;
        }
    }
}
