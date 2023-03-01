package com.example.emos.api.controller.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: Weijie Zhou
 * @ProjectName: emos-api
 */
@Data
@Schema(description = "delete meeting application form")
public class DeleteMeetingApplication {
    @NotNull(message = "id不能为空")
    @Min(value = 1)
    @Schema(description = "id")
    private Long id;

    @NotBlank(message = "uuid不能为空")
    @Schema(description = "uuid")
    private String uuid;

    @NotBlank(message = "instanceId不能为空")
    @Schema(description = "工作流instanceId")
    private String instanceId;

    @NotBlank(message = "原因不能为空")
    @Schema(description = "原因")
    private String reason;

}
