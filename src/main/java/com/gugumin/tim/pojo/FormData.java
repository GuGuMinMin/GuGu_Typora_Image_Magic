package com.gugumin.tim.pojo;

import lombok.Data;

import java.util.Map;

/**
 * @author minmin
 * @date 2023/03/14
 */
@Data
public class FormData {
    private boolean open;
    private String fileName;
    private Map<String, String> keyValue;
}
