package de.krieger.management.model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Author {

    private static final int MIN_LENGTH = 2;
    private static final int MAX_LENGTH = 50;

    int id;

    @NotBlank(message = "firstName should not be empty!")
    @Size(min = MIN_LENGTH, max = MAX_LENGTH, message = "firstName length should be between "+ MIN_LENGTH +" and "+ MAX_LENGTH +".")
    String firstName;

    @NotBlank(message = "lastName should not be empty!")
    @Size(min = MIN_LENGTH, max = MAX_LENGTH, message = "firstName length should be between "+ MIN_LENGTH +" and "+ MAX_LENGTH +".")
    String lastName;

}
