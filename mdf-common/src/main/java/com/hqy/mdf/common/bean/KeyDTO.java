package com.hqy.mdf.common.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hqy
 */
@Data
public class KeyDTO implements Serializable {

    private String publicKey;

    private String privateKey;
}
