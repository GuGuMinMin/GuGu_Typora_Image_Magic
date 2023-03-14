![](https://img.shields.io/badge/Java-11-green)

<center>配合Typora以命令行方式上传图片的工具，能够根据配置自定义对接图床完成请求与解析返回的结果。</center>

## 使用流程

### 确认对接的接口

先有一个可用的上传接口，然后根据它的文档，先用Postman等工具调通，然后再创建相应的配置。

### 创建配置

#### 手动创建配置

以Windows系统为例，在`%USERPROFILE%/GuGu_Typora_Image_Magic`创建`config.yml`文件，示例内容如下

```yaml
request:
# 超时时间 单位秒
  timeout: 10
# 代理配置
  proxy:
    open: false
    host:
    port:
  url: https://imgbed.link/imgbed/file/upload
  formData:
#   如果为false 那么数据将会存放在body中
    open: true
#   指定文件类型的key值
    fileName: file
#   添加其他需要的键值对
    keyValue:
      key: ab
# 添加请求头
  headers:
    Authorization: test
response:
#  超时时间 单位秒
  timeout: 10
#  json-path表达式取回需要的结果
  body: $.rows[0].url
```

#### 自动创建配置

只需要手动执行一次程序，程序就会在当前系统用户下创建`GuGu_Typora_Image_Magic/config.yml`文件，内容如上。

### 配置Typora

配置Typora的上传服务设定(文件>偏好设置>图像>上传服务设定>上传服务)，选择自定义命令。

在命令行中输入`java -jar gugu_typora_image_magic-1.0.0-jar-with-dependencies.jar `，输入值请根据自身环境做调整。

配置完后点击验证图片上传选项，检查上传是否可用。

## 如果不可用

如果上传不可用且与Postman等工具对照没什么差异的话，需要下载源码手动进行Debug调式寻找问题，或者向我提issues。

