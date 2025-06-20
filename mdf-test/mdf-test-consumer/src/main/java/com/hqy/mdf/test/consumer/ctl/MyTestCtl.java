package com.hqy.mdf.test.consumer.ctl;

import com.alibaba.fastjson2.JSON;
import com.hqy.mdf.common.bean.Result;
import com.hqy.mdf.common.util.ResultUtils;
import com.hqy.mdf.test.api.TestApi;
import com.hqy.mdf.test.api.dto.req.TestReqDTO;
import com.hqy.mdf.test.api.dto.resp.TestRespDTO;
import com.hqy.mdf.test.consumer.vo.TestRespVO;
import com.hqy.mdf.test.consumer.vo.TestVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * @author hqy
 */
@Slf4j
@RestController
@RequestMapping("/my")
public class MyTestCtl {

    @DubboReference
    private TestApi testApi;

    @GetMapping("/test1")
    public Result<?> test1(@RequestParam(value = "abc") String abc){
        // 测试 JSON 格式日志
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("phoneNumber", "13812345678");
        userInfo.put("mobileNo", "13879798878");
        userInfo.put("idCard", "310123199001011234");
        userInfo.put("bankCard", "6222021234567890123");
        userInfo.put("email", "test@example.com");

        List<Map> abcList = new ArrayList<>();
        abcList.add(userInfo);
        abcList.add(userInfo);

        log.info("jsonString用户信息:{}",JSON.toJSONString(userInfo));
        log.info("toString用户信息:{}",userInfo);
        log.info("all用户信息: jsonString:{},toString{}",JSON.toJSONString(userInfo),userInfo);
        log.info("list用户信息: jsonString:{},toString{}",JSON.toJSONString(abcList),abcList);
        return ResultUtils.success(abc);
    }

    @PostMapping("/test2")
    public Result<?> test2(@RequestBody @Validated TestVO vo){
        log.info("test2 vo:{}", JSON.toJSONString(vo));
        return ResultUtils.success(vo);
    }

    @GetMapping("/test3")
    public Result<?> test3(){
        TestRespVO testRespVO = new TestRespVO();
        testRespVO.setName("张三");
        testRespVO.setMobileNo("18638886999");
        testRespVO.setPassword("abcdefghijk123");
        testRespVO.setIdCardNo("41132919870101001X");
        testRespVO.setBankCard("6214830018601456");
        testRespVO.setAddress("上海市徐汇区上海南站300米");
        testRespVO.setCustomize("自定义字段");

        JSON.toJSONString(testRespVO);

        return ResultUtils.success(testRespVO);
    }

    @PostMapping("/test4")
    public Result<TestRespDTO> test4(@RequestBody TestReqDTO reqDTO){
        log.info("test4 reqDTO:{}", JSON.toJSONString(reqDTO));
        return testApi.test(reqDTO);
    }

    @PostMapping("/test5")
    public ResponseEntity<?> test5(@RequestParam(value = "file", required = true) MultipartFile file) throws IOException {
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(Objects.requireNonNull(file.getContentType())))
                .body(new InputStreamResource(file.getInputStream()));
    }
}
