package com.gugumin.tim.pojo;

import lombok.Data;

/**
 * @author minmin
 * @date 2023/03/14
 */
@Data
public class Response {
    private int timeout = 10;
    private String body;
}
