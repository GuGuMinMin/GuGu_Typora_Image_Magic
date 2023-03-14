package com.gugumin.tim.pojo;

import lombok.Data;

import java.net.URL;
import java.util.Map;

/**
 * @author minmin
 * @date 2023/03/14
 */
@Data
public class Request {
    private int timeout = 10;
    private Proxy proxy;
    private String url;
    private FormData formData;
    private Map<String,String> headers;
}
