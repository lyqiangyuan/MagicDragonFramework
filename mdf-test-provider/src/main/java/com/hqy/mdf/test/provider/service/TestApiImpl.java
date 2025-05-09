package com.hqy.mdf.test.provider.service;

import com.hqy.mdf.common.bean.Result;
import com.hqy.mdf.common.util.ResultUtils;
import com.hqy.mdf.test.api.TestApi;
import com.hqy.mdf.test.api.dto.req.TestReqDTO;
import com.hqy.mdf.test.api.dto.resp.TestRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author hqy
 * @date 2025/4/27
 */
@Slf4j
@DubboService
public class TestApiImpl implements TestApi {


    @Override
    public Result<TestRespDTO> test(TestReqDTO reqDTO) {

        log.info("dubbo test req:{}",reqDTO);
//        throw new BaseException(ErrorEnum.PARAM_ERROR);

        TestRespDTO testRespDTO = new TestRespDTO();
        testRespDTO.setName("张三");
        testRespDTO.setMobileNo("18638886999");
        testRespDTO.setPassword("abcdefghijk123");
        testRespDTO.setIdCardNo("41132919870101001X");
        testRespDTO.setBankCard("6214830018601456");
        testRespDTO.setAddress("上海市徐汇区上海南站300米");
        testRespDTO.setCustomize("自定义字段");
        return ResultUtils.success(testRespDTO);
//        throw new RuntimeException("测试异常");
    }
}
