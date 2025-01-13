package com.px.business.manage.feishu.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.px.business.manage.feishu.model.*;
import com.px.common.utils.BeanDtoVoUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Api(tags = "飞书接口")
@RequestMapping("/api/feishu")
@Slf4j
public class FeishuController {

    @Autowired
    private RestTemplate restTemplate;

    @ApiOperation("飞书app_ticket事件回调")
    @PostMapping("/feishuCallback")
    public Integer feishuCallback(@RequestBody String req) {
        /*log.info("======飞书回调开始========"+ JSONObject.toJSONString(req));
        Map<String,String> map = new HashMap<>();
        ReqModel reqModel = JSONObject.parseObject(req, ReqModel.class);
        String challenge = reqModel.getChallenge();
        ReqDTO reqDTO = new ReqDTO();
        reqDTO.setChallenge(challenge);
        String string = JSONObject.toJSONString(reqDTO);
        return string;*/
        TicketModel ticketModel = JSONObject.parseObject(req, TicketModel.class);
        String app_ticket = ticketModel.getEvent().getApp_ticket();
        log.info("=============ticket=============" + app_ticket);
        return 200;

    }


    @ApiOperation("获取用户列表")
    @GetMapping("/getUserList")
    public List<FuserDTO> appTicket(String openDepartmentId, Integer pageSize) throws IOException {

        String token = this.getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        String url = "https://open.feishu.cn/open-apis/contact/v3/users";
        if (openDepartmentId!=null){
            url = url + "?" + "department_id=" + openDepartmentId ;
        }
        ResponseEntity<String> forEntity = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        if (forEntity != null && forEntity.getStatusCode().equals(HttpStatus.OK)) {
            FeishuResponse feishuResponse = JSON.parseObject(forEntity.getBody(), FeishuResponse.class);
            ResponseData data = feishuResponse.getData();
            if (data != null) {
                List<ItemModel> items = data.getItems();
                List<FuserDTO> fuserDTOS = BeanDtoVoUtils.listVo(items, FuserDTO.class);
                log.info("==============token:"+token);
                return fuserDTOS;
                //TODO 插入数据库
            }
            System.out.println(token);
        }
        return null;
    }


    @ApiOperation("获取部门列表信息")
    @GetMapping("/getDeptList")
    public List<DepartmentDTO> getDeptList() {
        String token = this.getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        HttpEntity<Object> entity = new HttpEntity<>(headers);
        ResponseEntity<String> forEntity = restTemplate.exchange("https://open.feishu.cn/open-apis/contact/v3/departments", HttpMethod.GET, entity, String.class);
        if (forEntity != null && forEntity.getStatusCode().equals(HttpStatus.OK)) {
            FeishuResponse feishuResponse = JSON.parseObject(forEntity.getBody(), FeishuResponse.class);
            if (feishuResponse.getCode() == 0 && ("success").equals(feishuResponse.getMsg())) {
                List<ItemModel> items = feishuResponse.getData().getItems();
                List<DepartmentDTO> list = BeanDtoVoUtils.listVo(items, DepartmentDTO.class);
                //父级部门
                List<DepartmentDTO> parent = list.stream().filter(x -> x.getParentDepartmentId().equals("0")).collect(Collectors.toList());
                List<DepartmentDTO> departmentDTOS = new ArrayList<>();
                parent.forEach(x->{
                    DepartmentDTO child = getChild(x, list);
                    departmentDTOS.add(child);
                });
                return departmentDTOS;
            }
        }
        return null;
    }

    @GetMapping("/getChatList")
    @ApiOperation("获取群组列表")
    public List<ChatDTO> getChatList(){
        String token = this.getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+token);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<String> forEntity = restTemplate.exchange("https://open.feishu.cn/open-apis/im/v1/chats", HttpMethod.GET, httpEntity, String.class);
        if (forEntity != null && forEntity.getStatusCode().equals(HttpStatus.OK)) {
            FeishuResponse feishuResponse = JSON.parseObject(forEntity.getBody(), FeishuResponse.class);
            if (feishuResponse.getCode() == 0 && ("ok").equals(feishuResponse.getMsg())) {
                List<ItemModel> items = feishuResponse.getData().getItems();
                List<ChatDTO> chatDTOS = BeanDtoVoUtils.listVo(items, ChatDTO.class);
                return chatDTOS;
            }
        }
        return null;
    }

    @ApiOperation("获取群成员列表")
    @GetMapping("/getChatMemberList")
    public List<MemberDTO> getChatMemberList(@RequestParam String chatId){
        String token = this.getToken();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+token);
        HttpEntity<Object> httpEntity = new HttpEntity<>(headers);
        String url = "https://open.feishu.cn/open-apis/im/v1/chats/"+chatId+"/members";
        ResponseEntity<String> forEntity = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        if (forEntity != null && forEntity.getStatusCode().equals(HttpStatus.OK)) {
            FeishuResponse feishuResponse = JSON.parseObject(forEntity.getBody(), FeishuResponse.class);
            if (feishuResponse.getCode() == 0 && ("ok").equals(feishuResponse.getMsg())) {
                List<ItemModel> items = feishuResponse.getData().getItems();
                List<MemberDTO> memberDTOS = BeanDtoVoUtils.listVo(items, MemberDTO.class);
                return memberDTOS;
            }
        }
        return null;
    }



    private DepartmentDTO getChild(DepartmentDTO dto,List<DepartmentDTO> dtos){
        List<DepartmentDTO> list = new ArrayList<>();
        dtos.forEach(x->{
            if (!x.getParentDepartmentId().equals("0") && x.getParentDepartmentId().equals(dto.getOpenDepartmentId())){
                list.add(x);
                getChild(x,dtos);
            }
        });
        dto.setDepartmentDTOS(list);
        return dto;
    }


    private String getToken() {
        String token = null;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/json;charset=utf-8");
        Map<String, Object> map = new HashMap<>();
        map.put("app_id", "cli_a1fd0c18fb38d00e");
        map.put("app_secret", "w2Cm81xs8yMgnWJsYJLGKbw0Og74eAum");
        HttpEntity<Map<String, Object>> formEntity = new HttpEntity<>(map, httpHeaders);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("https://open.feishu.cn/open-apis/auth/v3/tenant_access_token/internal", formEntity, String.class);
        if (responseEntity != null && responseEntity.getStatusCode().equals(HttpStatus.OK)) {
            Map map1 = JSON.parseObject(responseEntity.getBody(), Map.class);
            if (map1 != null && 0 == (Integer) map1.get("code")) {
                token = (String) map1.get("tenant_access_token");
            }
        }
        return token;
    }

    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("app_id", "cli_a1f83a0db7f8d00e");
        jsonObject.put("app_secret", "sh4SmntpKbhy36qIhymF9cGQf0wg0Wn6");
        System.out.println(JSON.toJSONString(jsonObject));
    }
}
