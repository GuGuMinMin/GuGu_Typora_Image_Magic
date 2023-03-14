package com.gugumin.tim.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.gugumin.tim.pojo.Config;
import com.gugumin.tim.pojo.FormData;
import com.jayway.jsonpath.JsonPath;
import lombok.SneakyThrows;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author minmin
 * @date 2023/03/14
 */
public class ImageService implements AutoCloseable {
    private static final String DEFAULT_SUCCESS_RESULT_HEADER = "Upload Success:";
    private static final String DEFAULT_FAIL_RESULT_HEADER = "Upload Fail.";
    private static final String DEFAULT_USER_AGENT = "PostmanRuntime/7.28.3";
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newCachedThreadPool();

    @SneakyThrows
    public String postImage(Config config, List<Path> pathList) {
        List<String> resultList = Collections.synchronizedList(new LinkedList<>());
        CountDownLatch countDownLatch = new CountDownLatch(pathList.size());
        for (Path path : pathList) {
            EXECUTOR_SERVICE.execute(() -> {
                try {
                    HttpRequest post = HttpUtil.createPost(config.getRequest().getUrl());
                    if (config.getRequest().getProxy().isOpen()) {
                        post.setHttpProxy(config.getRequest().getProxy().getHost(), config.getRequest().getProxy().getPort());
                    }
                    post.setConnectionTimeout(config.getRequest().getTimeout() * 1000).setReadTimeout(config.getResponse().getTimeout() * 1000);
                    addDefaultHeaders(config, post);
                    post.addHeaders(config.getRequest().getHeaders());
                    inputData(config, path, post);
                    try (HttpResponse response = post.execute()) {
                        if (response.getStatus() == 200) {
                            String body = response.body();
                            resultList.add(JsonPath.read(body, config.getResponse().getBody()).toString());
                        }
                    }
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        if (countDownLatch.await(config.getRequest().getTimeout() + config.getResponse().getTimeout(), TimeUnit.SECONDS)) {
            if (resultList.size() < 1) {
                return DEFAULT_FAIL_RESULT_HEADER;
            }
            return printResultList(resultList);
        }
        return DEFAULT_FAIL_RESULT_HEADER;
    }

    @SneakyThrows
    private void addDefaultHeaders(Config config, HttpRequest post) {
        post.header("Host", new URL(config.getRequest().getUrl()).getHost(), true);
        post.header("User-Agent", DEFAULT_USER_AGENT, true);
    }

    private void inputData(Config config, Path path, HttpRequest post) {
        FormData formData = config.getRequest().getFormData();
        if (formData.isOpen()) {
            Map<String, String> keyValue = formData.getKeyValue();
            if (keyValue != null && keyValue.size() > 1){
                for (Map.Entry<String, String> entry : keyValue.entrySet()) {
                    post.form(entry.getKey(), entry.getValue());
                }
            }
            post.form(formData.getFileName(), path.toFile());
            return;
        }
        post.body(readFileBytes(path));
    }

    @SneakyThrows
    private byte[] readFileBytes(Path path) {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(path.toFile()))) {
            return bufferedInputStream.readAllBytes();
        }
    }

    private String printResultList(List<String> resultList) {
        StringBuilder stringBuilder = new StringBuilder(DEFAULT_SUCCESS_RESULT_HEADER);
        for (String result : resultList) {
            stringBuilder.append(System.lineSeparator()).append(result);
        }
        return stringBuilder.toString();
    }

    @Override
    public void close() {
        EXECUTOR_SERVICE.shutdownNow();
    }
}
