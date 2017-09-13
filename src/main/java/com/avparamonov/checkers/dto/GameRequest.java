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
public class GameRequest {

    @NotBlank(message = "Please provide already registered nickname")
    String whiteCheckersNickname;

    @NotBlank(message = "Please provide already registered nickname")
    String blackCheckersNickname;

    @NotBlank(message = "Please provide type of new Game")
    String gameType;

}
