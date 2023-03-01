package com.example.emos.api.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @description:
 * @author: Weijie Zhou
 * @ProjectName: emos-api
 */
@Data
@Schema(description = "删除腾讯云COS文件表单")
public class DeleteCosFileForm {
    @NotEmpty(message = "pathes不能为空")
    private String[] pathes;
}
