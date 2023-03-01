package com.example.emos.api.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: Weijie Zhou
 * @ProjectName: emos-api
 */
@Data
@Schema(description = "查询Navtive支付罚款单支付结果表单")
public class SearchNativeAmectPayResultForm {
    @NotNull(message = "amectId不能为空")
    @Min(value = 1, message = "amectId不能小于1")
    @Schema(description = "罚款单ID")
    private Integer amectId;
}
