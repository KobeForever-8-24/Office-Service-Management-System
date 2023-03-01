package com.example.emos.api.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @description:
 * @author: Weijie Zhou
 * @ProjectName: emos-api
 */
@Schema(description = "search department page form")
@Data
public class SearchDeptByPageForm {

    @Pattern(regexp = "^[0-9a-zA-Z\\u4e00-\\u9fa5]{1,10}$", message = "deptName内容不正确")
    @Schema(description = "部门名称")
    private String deptName;

    @NotNull(message = "page cannot be null")
    @Min(value = 1, message = "page cannot smaller than 1")
    @Schema(description = "page")
    private Integer page;

    @NotNull(message = "length cannot be null")
    @Range(min = 10, max = 50, message = "length must contains between 10 and 50")
    @Schema(description = "each page record number")
    private Integer length;
}
