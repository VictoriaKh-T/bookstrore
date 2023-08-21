package mate.academy.bookstore.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsbnValidator implements ConstraintValidator<Isbn, String> {
    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext constraintValidatorContext) {
        String cleanedIsbn = isbn.replaceAll("[\\-\\s]", "");

        if (cleanedIsbn.length() != 10 && cleanedIsbn.length() != 13) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < cleanedIsbn.length(); i++) {
            char c = cleanedIsbn.charAt(i);
            if (!Character.isDigit(c) && (i != cleanedIsbn.length() - 1 || c != 'X')) {
                return false;
            }
            int digit = (c == 'X') ? 10 : Character.getNumericValue(c);
            if (cleanedIsbn.length() == 10) {
                sum += digit * (10 - i);
            } else {
                sum += digit * (i % 2 == 0 ? 1 : 3);
            }
        }

        return sum % (cleanedIsbn.length() == 10 ? 11 : 10) == 0;
    }
}
