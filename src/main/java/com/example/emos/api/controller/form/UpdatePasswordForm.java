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
@Schema(description = "update password form")
@Data
public class UpdatePasswordForm {
    @NotBlank(message = "password cannot be null")
    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = "password content incorrect")
    @Schema(description = "password")
    String password;
}
