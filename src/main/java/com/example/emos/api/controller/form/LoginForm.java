package com.example.emos.api.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @description:
 * @author: Weijie Zhou
 * @ProjectName: emos-api
 */
@Data
@Schema(description = "Login Form class")
public class LoginForm {

    @NotBlank(message = "username cannot be null")
    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = "username content discorrect")
    @Schema(description = "username")
    private String username;

    @NotBlank(message = "password cannot be null")
    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = "password content discorrect")
    @Schema(description = "password")
    private String password;

}
