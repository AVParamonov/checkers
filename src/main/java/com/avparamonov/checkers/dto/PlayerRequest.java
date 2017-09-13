package com.avparamonov.checkers.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;


@Data
@Accessors(chain = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PlayerRequest {

    String firstName;
    String lastName;
    String role;

    @NotBlank(message = "Please provide your nickname")
    String nickname;

    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotBlank(message = "Please provide your password")
    String password;


}
