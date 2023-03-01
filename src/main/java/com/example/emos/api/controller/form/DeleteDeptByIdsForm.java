package com.example.emos.api.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @description:
 * @author: Weijie Zhou
 * @ProjectName: emos-api
 */
@Schema(description = "delete dept form")
@Data
public class DeleteDeptByIdsForm {

    @NotEmpty(message = "ids不能为空")
    @Schema(description = "deptId")
    private Integer[] ids;
}
