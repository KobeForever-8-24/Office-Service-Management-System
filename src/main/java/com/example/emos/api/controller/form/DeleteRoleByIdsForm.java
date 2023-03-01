package com.example.emos.api.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @description:
 * @author: Weijie Zhou
 * @ProjectName: emos-api
 */
@Schema(description = "delete role form")
@Data
public class DeleteRoleByIdsForm {
    @NotEmpty(message = "ids cannot be null")
    @Schema(description = "roleId")
    private Integer[] ids;
}
