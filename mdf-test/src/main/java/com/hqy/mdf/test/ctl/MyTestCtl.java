package com.hqy.mdf.test.ctl;

import com.alibaba.fastjson2.JSON;
import com.hqy.mdf.common.bean.Result;
import com.hqy.mdf.common.util.ResultUtils;
import com.hqy.mdf.test.vo.TestRespVO;
import com.hqy.mdf.test.vo.TestVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author hqy
 */
@Slf4j
@RestController
@RequestMapping("/my")
public class MyTestCtl {

    @GetMapping("/test1")
    public Result<?> test1(@RequestParam(value = "abc") String abc){
        return ResultUtils.success(abc);
    }

    @PostMapping("/test2")
    public Result<?> test2(@RequestBody @Validated TestVO vo){
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
}
