package cn.hou.sse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/sse")
public class SseController {

    @GetMapping("/date")
    public String date() {
        return "sse";
    }

    @RequestMapping(value = "/getDate", produces = "text/event-stream;charset=UTF-8")
    @ResponseBody
    public String getData() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        //EventSource返回的参数必须以data:开头，"\n\n"结尾，不然页面onmessage方法无法执行。
//        return "data:北京时间: " + simpleDateFormat.format(date) + " \n\n";
        return "id: 001\n" +
                "data: {'username': 'root', 'time': " + simpleDateFormat.format(date) + "}\n"+
                "event: message\n"+//event默认message
                "retry: 1000\n\n";
    }

}
