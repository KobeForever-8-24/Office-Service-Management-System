package com.example.emos.api.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.*;

/**
 * @description:
 * @author: Weijie Zhou
 * @ProjectName: emos-api
 */
@Schema(description = "insert user form")
@Data
public class InsertUserForm {

    @NotBlank(message = "username cannot be null")
    @Pattern(regexp = "^[a-zA-Z0-9]{5,20}$", message = "username content incorrect")
    @Schema(description = "username")
    private String username;

    @NotBlank(message = "password cannot be null")
    @Pattern(regexp = "^[a-zA-Z0-9]{6,20}$", message = "password content incorrect")
    @Schema(description = "password")
    private String password;

    @NotBlank(message = "name cannot be null")
    @Pattern(regexp = "^[a-zA-Z0-9]{2,10}$", message = "name content incorrect")
    @Schema(description = "name")
    private String name;

    @NotBlank(message = "sex cannot be null")
    @Pattern(regexp = "^男$|^女$", message = "sex content incorrect")
    @Schema(description = "sex")
    private String sex;

    @NotBlank(message = "tel cannot be null")
    @Pattern(regexp = "^1\\d{10}$", message = "tel content incorrect")
    @Schema(description = "telephone")
    private String tel;

    @NotBlank(message = "email cannot be null")
    @Email(message = "email content incorrect")
    @Schema(description = "email")
    private String email;

    @NotBlank(message = "hiredate cannot be null")
    @Pattern(regexp = "^((((1[6-9]|[2-9]\\\\d)\\\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\\\d|3[01]))|(((1[6-9]|[2-9]\\\\d)\\\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\\\d|30))|(((1[6-9]|[2-9]\\\\d)\\\\d{2})-0?2-(0?[1-9]|1\\\\d|2[0-8]))|(((1[6-9]|[2-9]\\\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-))$", message = "hiredate content incorrect")
    @Schema(description = "hiredate")
    private String hiredate;

    @NotEmpty(message = "role cannot be null")
    @Schema(description = "role")
    private Integer[] role;

    @NotNull(message = "deptId cannot be null")
    @Min(value = 1, message = "deptId cannot smaller than 1")
    @Schema(description = "department")
    private Integer deptId;



}
