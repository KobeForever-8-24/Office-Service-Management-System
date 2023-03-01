package com.example.emos.api.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * @description:
 * @author: Weijie Zhou
 * @ProjectName: emos-api
 */
@Schema(description = "insert role form")
@Data
public class InsertRoleForm {
    @NotBlank(message = "roleName cannot be null")
    @Schema(description = "role name")
    private String roleName;

    @NotEmpty(message = "permissions cannot be null")
    @Schema(description = "permissions")
    private Integer[] permissions;

    @Length(max = 20, message = "desc cannot longer than 20 chars")
    @Schema(description = "Note")
    private String desc;

}
