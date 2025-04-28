package com.hqy.mdf.test.api;

import com.hqy.mdf.test.api.dto.req.TestReqDTO;
import com.hqy.mdf.test.api.dto.resp.TestRespDTO;
import com.hqy.mdf.common.bean.Result;

/**
 * @author hqy
 * @date 2025/4/27
 */
public interface TestApi {

    Result<TestRespDTO> test(TestReqDTO reqDTO);
}
