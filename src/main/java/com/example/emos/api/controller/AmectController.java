package com.example.emos.api.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.example.emos.api.common.util.PageUtils;
import com.example.emos.api.common.util.R;
import com.example.emos.api.controller.form.*;
import com.example.emos.api.db.pojo.TbAmect;
import com.example.emos.api.service.AmectService;
import com.example.emos.api.websocket.WebSocketService;
import com.example.emos.api.wxpay.WXPayUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.security.sasl.Sasl;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: Weijie Zhou
 * @ProjectName: emos-api
 */
@RestController
@RequestMapping("/amect")
@Tag(name = "AmectController", description = "罚款Web接口")
@Slf4j
public class AmectController {

    @Value("${wx.key}")
    private String key;

    @Autowired
    private AmectService amectService;

    @PostMapping("/searchAmectByPage")
    @Operation(summary = "查询罚款分页记录")
    @SaCheckLogin
    public R searchAmectByPage(@Valid @RequestBody SearchAmectByPageForm form){
        if((form.getStartDate() != null && form.getEndDate() == null) || (form.getStartDate() == null && form.getEndDate() != null)){
            return R.error("startDate和endDate只能同时为空，或者不为空");
        }

        int page = form.getPage();
        int length = form.getLength();
        int start = (page - 1) * length;
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        param.put("start", start);
        param.put("currentUserId", StpUtil.getLoginIdAsInt());
        if(!(StpUtil.hasPermission("AMECT:SELECT") || StpUtil.hasPermission("ROOT"))){
            param.put("userId", StpUtil.getLoginIdAsInt());
        }
        PageUtils pageUtils = amectService.searchAmectByPage(param);
        return R.ok().put("page", pageUtils);
    }

    @PostMapping("/insert")
    @Operation(summary = "添加罚款记录")
    @SaCheckPermission(value = {"ROOT", "AMECT:INSERT"}, mode = SaMode.OR)
    public R insert(@Valid @RequestBody InsertAmectForm form){
        ArrayList<TbAmect> list = new ArrayList<>();
        for(Integer userId: form.getUserId()){
            TbAmect amect = new TbAmect();
            amect.setAmount(new BigDecimal(form.getAmount()));
            amect.setTypeId(form.getTypeId());
            amect.setReason(form.getReason());
            amect.setUserId(userId);
            amect.setUuid(IdUtil.simpleUUID());
            list.add(amect);
        }
        int rows = amectService.insert(list);
        return R.ok().put("rows", rows);
    }

    @PostMapping("/searchById")
    @Operation(summary = "更新罚款记录")
    @SaCheckPermission(value = {"ROOT", "AMECT:UPDATE"}, mode = SaMode.OR)
    public R searchById(@Valid @RequestBody SearchAmectByIdForm form){
        HashMap map = amectService.searchById(form.getId());
        return R.ok(map);
    }

    @PostMapping("/update")
    @Operation(summary = "更新罚款记录")
    @SaCheckPermission(value = {"ROOT", "AMECT:UPDATE"}, mode = SaMode.OR)
    public R update(@Valid @RequestBody UpdateAmectForm form){
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        int rows = amectService.update(param);
        return R.ok().put("rows", rows);
    }

    @PostMapping("/deleteAmectByIds")
    @Operation(summary = "删除罚款记录")
    @SaCheckPermission(value = {"ROOT", "AMECT:DELETE"}, mode = SaMode.OR)
    public R deleteAmectByIds(@Valid @RequestBody DeleteAmectByIdsForm form){
       int rows = amectService.deleteAmectByIds(form.getIds());
       return R.ok().put("rows", rows);
    }

    @PostMapping("/createNativeAmectPayOrder")
    @Operation(summary = "创建Native支付罚款订单")
    @SaCheckLogin
    public R createNativeAmectPayOrder(@Valid @RequestBody CreateNativeAmectPayOrderForm form){
        int userId = StpUtil.getLoginIdAsInt();
        int amectId = form.getAmectId();
        HashMap param = new HashMap(){{
            put("amectId", amectId);
            put("userId", userId);
        }};
        String qrCodeBase64 = amectService.createNativeAmectPayOrder(param);
        return R.ok().put("qrCodeBase64", qrCodeBase64);
    }

    @Operation(summary = "接收消息通知")
    @RequestMapping("/receiveMessage")
    public void receiveMessage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        Reader reader = request.getReader();
        BufferedReader buffer = new BufferedReader(reader);
        String line = buffer.readLine();
        StringBuffer temp = new StringBuffer();
        while (line != null){
            temp.append(line);
            line = buffer.readLine();
        }
        buffer.close();
        reader.close();
        String xml = temp.toString();
        //利用数字证书验证收到的响应内容，避免有人伪造付款结果发送给Web方法
        if(WXPayUtil.isSignatureValid(xml, key)){
            Map<String, String> map = WXPayUtil.xmlToMap(temp.toString());
            String resultCode = map.get("result_code");
            String returnCode = map.get("return_code");
            if ("SUCCESS".equals(resultCode) & "SUCCESS".equals(returnCode)){
                String outTradeNo = map.get("out_trade_no");//罚款单UUID
                //更新订单状态
                HashMap param = new HashMap(){{
                    put("status", 2);
                    put("uuid", outTradeNo);
                }};
                int rows = amectService.updateStatus(param);
                if(rows == 1){
                    //向前端页面推送付款结果
                    int userId = amectService.searchUserIdByUUID(outTradeNo);
                    WebSocketService.sendInfo("收款成功", userId + "");
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("application/xml");
                    Writer writer = response.getWriter();
                    BufferedWriter bufferedWriter = new BufferedWriter(writer);
                    bufferedWriter.write("<xml><return_code><![CDATA[SUCCESS]]></return_code> <return_msg><![CDATA[OK]]></return_msg></xml>");
                    bufferedWriter.close();
                    writer.close();

                }else{
                   log.error("更新订单失败");
                   response.sendError(500, "更新订单状态失败");
                }
            }
        }else{
            log.error("数字签名异常");
            response.sendError(500, "数字签名异常");
        }
    }

    @PostMapping("/searchNativeAmectPayResult")
    @Operation(summary = "查询Native支付罚款订单结果")
    @SaCheckLogin
    public R searchNativeAmectPayResult(@Valid @RequestBody SearchNativeAmectPayResultForm form){
        int userId = StpUtil.getLoginIdAsInt();
        int amectId = form.getAmectId();
        HashMap param = new HashMap(){{
            put("amectId", amectId);
            put("userId", userId);
            put("status", 1);
        }};
        amectService.searchNativeAmectPayResult(param);
        return R.ok();
    }

    @PostMapping("/searchChart")
    @Operation(summary = "查询Chart图表")
    @SaCheckPermission(value = {"ROOT", "AMECT:SELECT"}, mode = SaMode.OR)
    public R searchChart(@Valid @RequestBody SearchChartForm form) {
        HashMap param = JSONUtil.parse(form).toBean(HashMap.class);
        HashMap map = amectService.searchChart(param);
        return R.ok(map);
    }

}
