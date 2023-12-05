package com.rmf.demoparkapi.web.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserPassDto {

    @NotBlank
    @Size(min = 6, max = 6)
    @JsonProperty("current_password")
    private String currentPassword;

    @NotBlank
    @Size(min = 6, max = 6)
    @JsonProperty("new_password")
    private String newPassword;

    @NotBlank
    @Size(min = 6, max = 6)
    @JsonProperty("confirm_password")
    private String confirmPassword;
}
