package com.example.graduation.server.PyServer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.graduation.server.mailService.MailService;
import com.example.graduation.utils.Res;
import com.example.graduation.utils.status;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.net.HttpURLConnection;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class PyServer {
    private final Logger logger = LoggerFactory.getLogger(PyServer.class);
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS) // 设置连接超时时间为3秒
            .build();
    @Autowired
    private PyServerConfig pyServerConfig;

    /**
     * 添加task
     * @param taskId
     * @param title
     * @param lat
     * @param lon
     * @param isOnLine 是否是在线任务
     * @param tags
     * @return
     */
    public Res<Boolean> addTask(String user,long taskId, String title, Double lat, Double lon, Boolean isOnLine, List<String> tags){
        StringBuilder ts= new StringBuilder();
        for(int i=0;i<tags.size();i++){
            if(i==0) ts.append(tags.get(i));
            else ts.append(",").append(tags.get(i));
        }
        //MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = new FormBody.Builder()
                .add("user",user)
                .add("task", String.valueOf(taskId))
                .add("title", title)
                .add("latitude", lat.toString())
                .add("longitude", lon.toString())
                .add("onLine", isOnLine.toString())
                .add("tags", ts.toString())
                .build();
        Request request = new Request.Builder()
                .url(pyServerConfig.getUrl()+"/api/addNewTask")
                .post(body)
                .build();

        try (Response res = client.newCall(request).execute()){
            if (!res.isSuccessful()) {
                logger.error("引擎连接失败");
                throw new Exception("Engine connection failed");
            }
            JSONObject object = JSON.parseObject(Objects.requireNonNull(res.body()).string());
            if(object.getInteger("statusCode")!=status.success){
                logger.error("引擎发生了意外");
                throw new Exception("there was an accident inside the engine");
            }
            if(!object.getBoolean("data")) throw new Exception("add task to engine failed");
            return Res.Success(true);
        }catch (Exception e){
            logger.error("引擎通信时发生了意外: " + e.getMessage());
            return Res.Error(e.getMessage());
        }
    }

    /**
     * 使某一task失效不再推荐，条件：超时或已被接取
     * @param taskId
     * @return
     */
    public Res<Boolean> disableTask(Long taskId){
        RequestBody body = new FormBody.Builder()
                .add("task",taskId.toString())
                .build();
        Request request = new Request.Builder()
                .url(pyServerConfig.getUrl()+"/api/disableTask")
                .put(body)
                .build();

        try (Response res = client.newCall(request).execute()){
            if (!res.isSuccessful()) {
                logger.error("引擎连接失败");
                throw new Exception("Engine connection failed");
            }
            JSONObject object = JSON.parseObject(Objects.requireNonNull(res.body()).string());
            if(object.getInteger("statusCode")!=status.success){
                logger.error("引擎发生了意外");
                throw new Exception("there was an accident inside the engine");
            }
            if(!object.getBoolean("data")) throw new Exception("disable task failed");
            return Res.Success(true);
        }catch (Exception e){
            logger.error("引擎通信时发生了意外: " + e.getMessage());
            return Res.Error(e.getMessage());
        }
    }

    /**
     * 获取推荐列表
     * @param user
     * @param search
     * @param num 获取数量
     * @param lon
     * @param lat
     * @param maxDistance 最大距离
     * @return
     */
    public Res<List<Map<String,Object>>> getTaskList(String user,String search,Integer num,Double lon,Double lat,Double maxDistance){
        Request request = new Request.Builder()
                .url(pyServerConfig.getUrl()+"/api/getTasks?"+"user="+user+"&search="+search+"&k="+num+"&longitude="+lon+"&latitude="+lat+"&s="+maxDistance)
                .get()
                .build();

        try (Response res = client.newCall(request).execute()){
            if (!res.isSuccessful()) {
                logger.error("引擎连接失败");
                throw new Exception("Engine connection failed");
            }
            JSONObject object = JSON.parseObject(Objects.requireNonNull(res.body()).string());
            if(object.getInteger("statusCode")!=status.success){
                logger.error("引擎发生了意外 "+object.getInteger("statusCode"));
                throw new Exception("there was an accident inside the engine");
            }
            JSONArray data = object.getJSONArray("data");
            List<Map<String,Object>> ls=new ArrayList<>();
            for (int i=0;i<data.size();i++){
                JSONObject item = data.getJSONObject(i);
                Map<String ,Object> mp=new HashMap<>();
                mp.put("taskId",item.getLongValue("taskId"));
                mp.put("value",item.getDouble("value"));
                mp.put("distance",item.getDouble("distance"));
                mp.put("onLine",item.getBoolean("onLine"));
                ls.add(mp);
            }
            return Res.Success(ls);
        }catch (Exception e){
            logger.error("引擎通信时发生了意外: " + e.getMessage());
            return Res.Error(e.getMessage());
        }
    }

    /**
     * 添加用户
     * @param user
     * @return
     */
    public Res<Boolean> addUser(String user){
        RequestBody body = new FormBody.Builder()
                .add("user", user)
                .build();
        Request request = new Request.Builder()
                .url(pyServerConfig.getUrl()+"/api/addUser")
                .post(body)
                .build();

        try (Response res = client.newCall(request).execute()){
            if (!res.isSuccessful()) {
                logger.error("引擎连接失败");
                throw new Exception("Engine connection failed");
            }
            JSONObject object = JSON.parseObject(Objects.requireNonNull(res.body()).string());
            if(object.getInteger("statusCode")!=status.success){
                logger.error("引擎发生了意外");
                throw new Exception("there was an accident inside the engine");
            }
            if(!object.getBoolean("data")) throw new Exception("add user to engine failed");
            return Res.Success(true);
        }catch (Exception e){
            logger.error("引擎通信时发生了意外: " + e.getMessage());
            return Res.Error(e.getMessage());
        }
    }

    /**
     *
     * @param user 用户账号
     * @param taskId 任务编号
     * @param action 0->点击,1->点赞,2->联系,3->接取,4->不喜欢
     * @return
     */
    public Res<Boolean> updatePrefer(String user,Integer taskId,Integer action){
        RequestBody body = new FormBody.Builder()
                .add("user",user)
                .add("task",taskId.toString())
                .add("do",action.toString())
                .build();
        Request request = new Request.Builder()
                .url(pyServerConfig.getUrl()+"/api/updatePrefer")
                .put(body)
                .build();

        try (Response res = client.newCall(request).execute()){
            if (!res.isSuccessful()) {
                logger.error("引擎连接失败");
                throw new Exception("Engine connection failed");
            }
            JSONObject object = JSON.parseObject(Objects.requireNonNull(res.body()).string());
            if(object.getInteger("statusCode")!=status.success){
                logger.error("引擎发生了意外");
                throw new Exception("there was an accident inside the engine");
            }
            if(!object.getBoolean("data")) throw new Exception("update prefer failed");
            return Res.Success(true);
        }catch (Exception e){
            logger.error("引擎通信时发生了意外: " + e.getMessage());
            return Res.Error(e.getMessage());
        }
    }
}
