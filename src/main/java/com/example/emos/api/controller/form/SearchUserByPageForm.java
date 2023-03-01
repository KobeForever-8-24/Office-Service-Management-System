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
@Data
@Schema(description = "search user page record form")
public class SearchUserByPageForm {

    @NotNull(message = "page cannot be null")
    @Min(value = 1, message = "page cannot smaller than 1")
    @Schema(description = "page count")
    private Integer page;

    @NotNull(message = "length cannot be null")
    @Range(min = 10, max = 50, message = "message range must within 10-50")
    @Schema(description = "each page record number")
    private Integer length;

    @Pattern(regexp = "^[\\u4e00-\\u9fa5]{1,10}$", message = "name content incorrect")
    @Schema(description = "name")
    private String name;

    @Pattern(regexp = "^男$|^女$", message = "sex content incorrect")
    @Schema(description = "sex")
    private String sex;

    @Pattern(regexp = "^[a-zA-Z0-9\\u4e00-\\u9fa5]{2,10}$", message = "role content incorrect")
    @Schema(description = "role")
    private String role;

    @Min(value = 1, message = "dept cannot smaller than 1")
    private Integer deptId;

    @Min(value = 1, message = "status cannot smaller than 1")
    private Integer status;
}
