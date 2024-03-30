package com.example.graduation.server.OSS;

import lombok.Data;

/**
 * @desc oss操作返回值
 */
@Data
public class FileUploadResult {
    // 文件唯一标识
    public String uid;
    // 文件名
    public String name;
    // 状态有：uploading done error removed
    public int status;
    // 服务端响应内容，如：'{"status": "success"}'
    public String response;
}