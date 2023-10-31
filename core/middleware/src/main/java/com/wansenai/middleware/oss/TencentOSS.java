/*
 * Copyright (C) 2023-2033 WanSen AI Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wansenai.middleware.oss;

import com.alibaba.fastjson.JSONObject;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.MultiObjectDeleteException;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.Copy;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.Upload;
import com.qcloud.cos.utils.IOUtils;
import com.wansenai.utils.FileUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executors;

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TencentOSS {

    private static transient TencentOSS instance;

    private String secretid;

    private String secretkey;

    private String region;

    private String bucket;

    private int fixedTthreadPool = 5;

    /**
     * 获取实例
     * @return
     */
    public static TencentOSS getInstance() {
        if (instance == null){
            instance = new TencentOSS();
        }
        return instance;
    }

    /**
     * 获取bucket
     * @return
     */
    public String getBucket(String bucket) {
        return bucket;
    }

    /**
     * 获取地址
     * @param key
     * @return
     */
    public String getPath(String key){
        return getPath(bucket,key);
    }

    /**
     * 获取地址
     * @param bucketName
     * @param key
     */
    public String getPath(String bucketName,String key){
        String url = "";
        COSClient cosClient = cosClient();
        try {
            url = cosClient.getObject(bucketName,key).getObjectContent().getHttpRequest().getURI().toString();
        }catch (Exception e){
            log.error("获取地址失败:[{}]",e);
        }finally {
            cosClient.shutdown();
        }
        return url;
    }

    /**
     * 上传文件
     * @param file
     * @param key
     */
    public String upload(File file, String key){
        return upload(file,bucket,key);
    }

    /**
     * 上传文件
     * @param file
     * @param bucketName
     * @param key
     */
    public String upload(File file, String bucketName, String key){
        String url = "";
        COSClient cosClient = cosClient();
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,key,file);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            url = cosClient.getObject(bucketName, key).getObjectContent().getHttpRequest().getURI().toString();
            log.info("上传文件结果:[{}]", putObjectResult.getRequestId());
        }catch (Exception e){
            log.error("上传文件异常:[{}]",e);
        }finally {
            cosClient.shutdown();
        }
        return url;
    }

    /**
     * 批量上传文件
     * @param files
     * @param keys
     */
    public String upload(List<File> files,List<String> keys){
        return upload(files,keys,bucket);
    }

    /**
     * 批量上传文件
     * @param files
     * @param bucketName
     * @param keys
     */
    public String upload(List<File> files,List<String> keys, String bucketName){
        String url = "";
        COSClient cosClient = cosClient();
        try {
            for (int i = 0,len = files.size(); i < len; i++) {
                String k = keys.get(i);
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,k,files.get(i));
                PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
                url = cosClient.getObject(bucketName, k).getObjectContent().getHttpRequest().getURI().toString();
                log.info("上传文件结果:[{}]", putObjectResult.getRequestId());
            }
        }catch (Exception e){
            log.error("上传文件异常:[{}]",e);
        }finally {
            cosClient.shutdown();
            FileUtil.deleteFiles(files);
        }
        return url;
    }

    /**
     * 批量上传文件
     * @param files
     * @param keys
     */
    public List<String> uploadBatch(List<File> files, List<String> keys) {
        return uploadBatch(files,keys,bucket);
    }

    /**
     * 批量上传文件
     * @param files
     * @param keys
     * @param bucketName
     */
    public List<String> uploadBatch(List<File> files,List<String> keys, String bucketName) {
        List<String> urls = new ArrayList<>();
        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为 folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        COSClient cosClient = cosClient();
        TransferManager transferManager = transferManager();
        List<Upload> uploads = new ArrayList<>();
        try {
            for (int i = 0,len = files.size(); i < len; i++) {
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, keys.get(i), files.get(i));
                Upload upload = transferManager.upload(putObjectRequest);
                uploads.add(upload);
            }
            for (Upload u : uploads) {
                UploadResult result = u.waitForUploadResult();
                log.info("批量上传文件key:[{}]", result.getKey());
                String url = cosClient.getObject(bucketName, result.getKey()).getObjectContent().getHttpRequest().getURI().toString();
                urls.add(url);
            }
        }catch (Exception e){
            log.error("批量上传文件异常:[{}]",e);
        }finally {
            transferManager.shutdownNow();
            cosClient.shutdown();
            FileUtil.deleteFiles(files);
        }
        return urls;
    }

    /**
     * 上传文件
     * @param file
     * @param key
     */
    public String uploadSteam(File file, String key){
        return uploadSteam(file,bucket,key);
    }

    /**
     * 上传文件
     * @param file
     * @param bucketName
     * @param key
     */
    public String uploadSteam(File file, String bucketName, String key){
        String url = "";
        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为 folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        COSClient cosClient = cosClient();
        TransferManager transferManager = transferManager();
        try {
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,key,file);
            Upload upload = transferManager.upload(putObjectRequest);
            UploadResult uploadResult = upload.waitForUploadResult();
            url = cosClient.getObject(bucketName, key).getObjectContent().getHttpRequest().getURI().toString();
            log.info("上传文件结果:[{}]", uploadResult.getKey());
        }catch (Exception e){
            log.error("上传文件异常:[{}]",e);
        }finally {
            transferManager.shutdownNow();
            cosClient.shutdown();
            FileUtil.deleteFile(file);
        }
        return url;
    }

    /**
     * 上传文件
     * @param in
     * @param key
     */
    public String upload(InputStream in, String key){
        return upload(in,bucket,key);
    }

    /**
     * 上传文件
     * @param in
     * @param bucketName
     * @param key
     */
    public String upload(InputStream in, String bucketName, String key){
        String url = "";
        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为 folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        COSClient cosClient = cosClient();
        TransferManager transferManager = transferManager();
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(in.available());
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,key,in,objectMetadata);
            /*PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            COSObject object = cosClient.getObject(bucketName, folder);
            String path = object.getObjectContent().getHttpRequest().getURI().toString();
            System.out.println(path);
            cosClient.shutdown();
            log.info("上传文件结果:[{}]", JSONUtil.toJsonStr(putObjectResult));*/
            /*URL url = cosClient.generatePresignedUrl(bucketName, folder, new Date(new Date().getTime() + 1000));
            log.info("上传文件结果:[{}]",url.getProtocol()+"://"+url.getHost()+url.getPath());*/
            Upload upload = transferManager.upload(putObjectRequest);
            UploadResult uploadResult = upload.waitForUploadResult();
            url = cosClient.getObject(bucketName, key).getObjectContent().getHttpRequest().getURI().toString();
            log.info("上传文件结果:[{}]", uploadResult.getKey());
        }catch (Exception e){
            log.error("上传文件异常:[{}]",e);
        }finally {
            transferManager.shutdownNow();
            cosClient.shutdown();
        }
        return url;
    }

    /**
     * 复制文件(可以进行文件重命名)
     * @param oldKey
     * @param newKey
     */
    public String copy(String oldKey,String newKey){
        return copy(bucket,oldKey,bucket,newKey);
    }

    /**
     * 复制文件(可以进行文件重命名)
     * @param oldBucketName
     * @param oldKey
     * @param newBucketName
     * @param newKey
     */
    public String copy(String oldBucketName,String oldKey,String newBucketName,String newKey){
        String url = "";
        COSClient cosClient = cosClient();
        try {
            CopyObjectRequest copyObjectRequest = new CopyObjectRequest(new Region(region), oldBucketName, oldKey, newBucketName, newKey);
            CopyObjectResult copyResult = cosClient.copyObject(copyObjectRequest);
            url = cosClient.getObject(newBucketName,newKey).getObjectContent().getHttpRequest().getURI().toString();
            log.info("复制文件结果:[{}]", copyResult.getRequestId());
        }catch (Exception e){
            log.error("复制文件异常:[{}]",e);
            return url;
        }finally {
            cosClient.shutdown();
        }
        return url;
    }

    /**
     * 批量复制文件(可以进行文件重命名)
     * @param keys Map<oldKey.newKey>
     */
    public List<String> copyBatch(Map<String,String> keys){
        return copyBatch(bucket,bucket,keys);
    }

    /**
     * 批量复制文件(可以进行文件重命名)
     * @param oldBucketName
     * @param keys Map<oldKey.newKey>
     */
    public List<String> copyBatch(String oldBucketName,Map<String,String> keys){
        return copyBatch(oldBucketName,oldBucketName,keys);
    }

    /**
     * 批量复制文件(可以进行文件重命名)
     * @param oldBucketName
     * @param newBucketName
     * @param keys Map<oldKey.newKey>
     */
    public List<String> copyBatch(String oldBucketName, String newBucketName, Map<String,String> keys){
        List<String> path = new ArrayList<>();
        COSClient cosClient = cosClient();
        try {
            for (Map.Entry<String, String> entry : keys.entrySet()) {
                String oldKey = entry.getKey();
                if (cosClient.doesObjectExist(oldBucketName,oldKey)){
                    String newKey = entry.getValue();
                    CopyObjectRequest copyObjectRequest = new CopyObjectRequest(new Region(region), oldBucketName, oldKey, newBucketName, newKey);
                    CopyObjectResult copyResult = cosClient.copyObject(copyObjectRequest);
                    String url = cosClient.getObject(newBucketName,newKey).getObjectContent().getHttpRequest().getURI().toString();
                    path.add(url);
                    log.info("复制文件结果:[{}]", copyResult.getRequestId());
                }else {
                    log.info("文件[{}]不存在",oldKey);
                }
            }
        }catch (Exception e){
            log.error("复制文件异常:[{}]",e);
            return path;
        }finally {
            cosClient.shutdown();
        }
        return path;
    }

    /**
     * 复制文件(可以进行文件重命名)
     * @param oldBucketName
     * @param oldKey
     * @param newBucketName
     * @param newKey
     */
    public String copyStream(String oldBucketName,String oldKey,String newBucketName,String newKey){
        String url = "";
        COSClient cosClient = cosClient();
        TransferManager transferManager = transferManager();
        try {
            CopyObjectRequest copyObjectRequest = new CopyObjectRequest(new Region(region), oldBucketName, oldKey, newBucketName, newKey);
            Copy copy = transferManager.copy(copyObjectRequest);
            // 高级接口会返回一个异步结果 Copy
            // 可同步的调用 waitForCopyResult 等待复制结束, 成功返回 CopyResult, 失败抛出异常
            CopyResult copyResult = copy.waitForCopyResult();
            url = cosClient.getObject(newBucketName,newKey).getObjectContent().getHttpRequest().getURI().toString();
            log.info("复制文件结果:[{}]", copyResult.getDestinationKey());
        }catch (Exception e){
            log.error("复制文件异常:[{}]",e);
            return url;
        }finally {
            transferManager.shutdownNow();
            cosClient.shutdown();
        }
        return url;
    }

    /**
     * 检查存储桶是否存在
     */
    public boolean hasBucketExist(String bucketName){
        COSClient cosClient = cosClient();
        try {
            return cosClient.doesBucketExist(bucketName);
        }catch (Exception e){
            log.error("检查存储桶是否存在异常:[{}]",e);
            return false;
        }finally {
            cosClient.shutdown();
        }
    }

    /**
     * 检查文件是否存在
     * @param key
     */
    public boolean hasExists(String key){
        return hasExists(bucket,key);
    }

    /**
     * 检查文件是否存在
     * @param bucketName
     * @param key
     */
    public boolean hasExists(String bucketName,String key){
        COSClient cosClient = cosClient();
        try {
            return cosClient.doesObjectExist(bucketName,key);
        }catch (Exception e){
            log.error("检查文件是否存在异常:[{}]",e);
            return false;
        }finally {
            cosClient.shutdown();
        }
    }

    /**
     * 获取目录下所有文件
     * @param dir
     */
    public List<String> getFileByDir(String dir){
        return getFileByDir(bucket,dir);
    }

    /**
     * 获取目录下所有文件
     * @param bucketName
     * @param dir
     */
    public List<String> getFileByDir(String bucketName,String dir){
        List<String> list = new ArrayList<>();
        COSClient cosClient = cosClient();
        try {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
            // 设置 bucket 名称
            listObjectsRequest.setBucketName(bucketName);
            // prefix 表示列出的对象名以 prefix 为前缀
            // 这里填要列出的目录的相对 bucket 的路径
            listObjectsRequest.setPrefix(dir);
            // 设置最大遍历出多少个对象, 一次 listobject 最大支持1000
            listObjectsRequest.setMaxKeys(1000);
            ObjectListing objectListing = cosClient.listObjects(listObjectsRequest);
            // 这里保存列出的对象列表
            List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
            if (!Objects.isNull(cosObjectSummaries) && cosObjectSummaries.size() > 0){
                for (COSObjectSummary f:cosObjectSummaries){
                    list.add(cosClient().getObject(bucketName,f.getKey()).getObjectContent().getHttpRequest().getURI().toString());
                }
            }
        }catch (Exception e){
            log.error("获取目录下所有文件异常:[{}]",e);
        }finally {
            cosClient.shutdown();
        }
        return list;
    }

    /**
     * 目录删除(如：2023/03/01.jpg，不能以/开头)
     * @param dir 这里填要列出的目录的相对 bucket 的路径
     */
    public boolean deleteDir(String dir){
        return deleteDir(bucket,dir);
    }

    /**
     * 目录删除(如：2023/03/01.jpg，不能以/开头)
     * @param bucketName
     * @param dir 这里填要列出的目录的相对 bucket 的路径
     */
    public boolean deleteDir(String bucketName,String dir){
        boolean flag = true;
        COSClient cosClient = cosClient();
        try {
            ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
            // 设置 bucket 名称
            listObjectsRequest.setBucketName(bucketName);
            // prefix 表示列出的对象名以 prefix 为前缀
            // 这里填要列出的目录的相对 bucket 的路径
            listObjectsRequest.setPrefix(dir);
            // 设置最大遍历出多少个对象, 一次 listobject 最大支持1000
            listObjectsRequest.setMaxKeys(1000);
            // 保存每次列出的结果
            ObjectListing objectListing = null;
            do {
                objectListing = cosClient.listObjects(listObjectsRequest);
                // 这里保存列出的对象列表
                List<COSObjectSummary> cosObjectSummaries = objectListing.getObjectSummaries();
                ArrayList<DeleteObjectsRequest.KeyVersion> delObjects = new ArrayList<DeleteObjectsRequest.KeyVersion>();
                for (COSObjectSummary cosObjectSummary : cosObjectSummaries) {
                    delObjects.add(new DeleteObjectsRequest.KeyVersion(cosObjectSummary.getKey()));
                }
                DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
                deleteObjectsRequest.setKeys(delObjects);
                deleteMethod(deleteObjectsRequest);
                // 标记下一次开始的位置
                String nextMarker = objectListing.getNextMarker();
                listObjectsRequest.setMarker(nextMarker);
            } while (objectListing.isTruncated());
        }catch (Exception e){
            log.error("删除目录:[{}]",e);
        }finally {
            cosClient.shutdown();
        }
        return flag;
    }

    /**
     * 删除(如：2023/03/01.jpg，不能以/开头)
     * @param key
     */
    public boolean delete(String key){
        return delete(bucket,key);
    }

    /**
     * 删除(如：2023/03/01.jpg，不能以/开头)
     * @param bucketName
     * @param key
     */
    public boolean delete(String bucketName,String key){
        if (key.startsWith("/")){
            return false;
        }
        COSClient cosClient = cosClient();
        try {
            cosClient.deleteObject(bucketName,key);
            return true;
        }catch (Exception e){
            log.error("删除异常:[{}]",e);
            return false;
        }finally {
            cosClient.shutdown();
        }
    }

    /**
     * 批量删除(如：2023/03/01.jpg，不能以/开头)
     * @param keys
     */
    public int deleteBatch(List<String> keys){
        return deleteBatch(bucket,keys);
    }

    /**
     * 批量删除(如：2023/03/01.jpg，不能以/开头)
     * @param bucketName
     * @param keys
     */
    public int deleteBatch(String bucketName,List<String> keys){
        // 最大只能同时删除1000
        List<DeleteObjectsRequest.KeyVersion> keyVersionList = new ArrayList<>();
        if (keys != null && keys.size() <= 1000){
            keys.forEach(f -> keyVersionList.add(new DeleteObjectsRequest.KeyVersion(f)));
            keys.clear();
            DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
            deleteObjectsRequest.setKeys(keyVersionList);
            return deleteMethod(deleteObjectsRequest);
        }
        return 0;
    }

    /**
     * 删除方法
     * @param deleteObjectsRequest
     */
    private int deleteMethod(DeleteObjectsRequest deleteObjectsRequest){
        COSClient cosClient = cosClient();
        try {
            DeleteObjectsResult objectsResult = cosClient.deleteObjects(deleteObjectsRequest);
            List<DeleteObjectsResult.DeletedObject> deletedObjects = objectsResult.getDeletedObjects();
            return deletedObjects.size();
        }catch (MultiObjectDeleteException mde){
            // 如果部分删除成功部分失败, 返回 MultiObjectDeleteException
            List<DeleteObjectsResult.DeletedObject> deleteObjects = mde.getDeletedObjects();
            List<MultiObjectDeleteException.DeleteError> deleteErrors = mde.getErrors();
            log.error("删除部分失败:[{}]",deleteObjects);
            return deleteErrors.size();
        }catch (Exception e){
            log.error("删除异常:[{}]",e);
            return 0;
        }finally {
            cosClient.shutdown();
        }
    }

    /**
     * 下载文件
     * @param bucketName
     * @param key
     * @param response
     */
    public void download(String bucketName, String key, HttpServletResponse response) throws Exception {
        String fileName = URLDecoder.decode(key.substring(key.lastIndexOf("/") + 1), StandardCharsets.UTF_8.name());
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
        fileName = new String(fileName.getBytes(), StandardCharsets.ISO_8859_1);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE+";charset=utf-8");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
        response.setHeader(HttpHeaders.PRAGMA, "no-cache");
        response.addHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
        byte[] bytes = download(bucketName, key);
        BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
        bos.write(bytes);
        bos.flush();
        bos.close();
        response.flushBuffer();
    }

    /**
     * 下载文件流
     * @param bucketName
     * @param key
     */
    public byte[] download(String bucketName,String key){
        byte[] bytes = null;
        COSClient cosClient = cosClient();
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        COSObjectInputStream cosObjectInput = null;
        try {
            cosObjectInput = cosClient.getObject(getObjectRequest).getObjectContent();
            bytes = IOUtils.toByteArray(cosObjectInput);
        }catch (Exception e){
            log.error("获取下载文件流异常:[{}]",e);
        }finally {
            cosClient.shutdown();
            if (cosObjectInput != null){
                try {
                    cosObjectInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytes;
    }

    /**
     * 获取存储桶列表
     */
    public List<Bucket> buckList(){
        COSClient cosClient = cosClient();
        try {
            return cosClient.listBuckets();
        }catch (Exception e){
            log.error("获取存储桶列表异常:[{}]",e);
            return new ArrayList<>();
        }finally {
            cosClient.shutdown();
        }
    }

    /**
     * 创建存储桶
     */
    public boolean createBucket(){
        COSClient cosClient = cosClient();
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucket);
        // 设置 bucket 的权限为 Private(私有读写)、其他可选有 PublicRead（公有读私有写）、PublicReadWrite（公有读写）
        createBucketRequest.setCannedAcl(CannedAccessControlList.PublicRead);
        try{
            Bucket bucketResult = cosClient.createBucket(createBucketRequest);
            log.info("创建存储桶结果:[{}]", JSONObject.parse(String.valueOf(bucketResult)));
            return true;
        } catch (Exception e) {
            log.error("创建存储桶异常:[{}]",e);
            return false;
        }finally {
            cosClient.shutdown();
        }
    }

    /**
     * 创建 transferManager
     */
    public TransferManager transferManager(){
        COSClient cosClient = cosClient();
        return new TransferManager(cosClient, Executors.newFixedThreadPool(fixedTthreadPool));
    }

    /**
     * 创建 COSClient 实例
     */
    public COSClient cosClient(){
        // 1 初始化用户身份信息（secretId, secretKey）。
        // SECRETID 和 SECRETKEY 请登录访问管理控制台 https://console.cloud.tencent.com/cam/capi 进行查看和管理
        COSCredentials cred = new BasicCOSCredentials(secretid, secretkey);
        // 2 设置 bucket 的地域, COS 地域的简称请参见 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region r = new Region(region);
        ClientConfig clientConfig = new ClientConfig(r);
        // 这里建议设置使用 https 协议
        // 从 5.6.54 版本开始，默认使用了 https
        clientConfig.setHttpProtocol(HttpProtocol.https);
        // 3 生成 cos 客户端。
        return new COSClient(cred, clientConfig);
    }
}
