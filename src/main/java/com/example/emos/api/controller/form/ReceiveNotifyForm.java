package com.example.emos.api.controller.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @description:
 * @author: Weijie Zhou
 * @ProjectName: emos-api
 */
@Data
public class ReceiveNotifyForm {
    @NotBlank
    private String processId;

    @NotBlank
    private String uuid;

    @NotBlank
    private String result;

}
