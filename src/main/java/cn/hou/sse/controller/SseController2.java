package cn.hou.sse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 服务器端实时推送技术之 SseEmitter 的用法测试
 * 测试步骤:
 * 1.请求http://localhost:8080/sse/start?clientId=001接口,浏览器会阻塞,等待服务器返回结果;
 * 2.请求http://localhost:8080/sse/send?clientId=001接口,可以请求多次,并观察第1步的浏览器页面返回结果;
 * 3.请求http://localhost:8080/sse/end?clientId=001接口结束某个请求,第1步的浏览器页面将结束阻塞;
 * 其中clientId代表请求的唯一标志;
 */
@Controller
@RequestMapping("/sse2")
public class SseController2 {

    // 用于保存每个请求对应的 SseEmitter
    private Map<String, SseEmitter> sseEmitterMap = new ConcurrentHashMap<>();

    /**
     * 返回SseEmitter对象
     *
     * @param clientId
     * @return
     */
    @RequestMapping("/start")
    public SseEmitter testSseEmitter(String clientId) {
        // 默认30秒超时,设置为0L则永不超时
        SseEmitter sseEmitter = new SseEmitter(0L);
        sseEmitterMap.put(clientId, sseEmitter);
        return sseEmitter;
    }

    /**
     * 向SseEmitter对象发送数据
     *
     * @param clientId
     * @return
     */
    @RequestMapping("/send")
    @ResponseBody
    public String setSseEmitter(String clientId) {
        try {
            SseEmitter sseEmitter = sseEmitterMap.get(clientId);
            if (sseEmitter != null) {
                sseEmitter.send(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            }
        } catch (IOException e) {
            System.out.println("IOException:" + e);
            return "error";
        }

        return "Succeed!";
    }

    /**
     * 将SseEmitter对象设置成完成
     *
     * @param clientId
     * @return
     */
    @RequestMapping("/end")
    @ResponseBody
    public String completeSseEmitter(String clientId) {
        SseEmitter sseEmitter = sseEmitterMap.get(clientId);
        if (sseEmitter != null) {
            sseEmitterMap.remove(clientId);
            sseEmitter.complete();
        }
        return "Succeed!";
    }

}

