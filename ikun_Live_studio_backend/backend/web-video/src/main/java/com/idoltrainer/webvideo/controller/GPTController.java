package com.idoltrainer.webvideo.controller;


import com.idoltrainer.webvideo.common.BaseResponse;
import com.idoltrainer.webvideo.common.ErrorCode;
import com.idoltrainer.webvideo.common.ResultUtils;
import com.idoltrainer.webvideo.domain.User;
import com.idoltrainer.webvideo.exception.BusinessException;
import com.idoltrainer.webvideo.service.UserService;
import com.unfbx.chatgpt.OpenAiClient;
import com.unfbx.chatgpt.entity.chat.ChatChoice;
import com.unfbx.chatgpt.entity.chat.ChatCompletion;
import com.unfbx.chatgpt.entity.chat.ChatCompletionResponse;
import com.unfbx.chatgpt.entity.chat.Message;
import com.unfbx.chatgpt.function.KeyRandomStrategy;
import com.unfbx.chatgpt.interceptor.OpenAILogger;
import com.unfbx.chatgpt.interceptor.OpenAiResponseInterceptor;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/gpt")
@Slf4j
@Api("ChatGPT模块")
public class GPTController {

    @Resource
    private UserService userService;
    /**
     * 询问gpt
     *
     * @param
     * @param request
     * @return resultNum 收藏变化数
     */
    @GetMapping("/question")
    @ApiOperation("询问gpt")
    public BaseResponse<String> doVideoFavour(@RequestParam String context,
                                               HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        if(user == null){
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR);
        }
        if (context == null || context.isEmpty()){
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR,"对不起，无法识别您说的。");
        }
        // 登录才能操作
        //国内访问需要做代理，国外服务器不需要
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new OpenAILogger());
        //！！！！千万别再生产或者测试环境打开BODY级别日志！！！！
        //！！！生产或者测试环境建议设置为这三种级别：NONE,BASIC,HEADERS,！！！
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .proxy(proxy)//自定义代理
                .addInterceptor(httpLoggingInterceptor)//自定义日志输出
                .addInterceptor(new OpenAiResponseInterceptor())//自定义返回值拦截
                .connectTimeout(10, TimeUnit.SECONDS)//自定义超时时间
                .writeTimeout(30, TimeUnit.SECONDS)//自定义超时时间
                .readTimeout(30, TimeUnit.SECONDS)//自定义超时时间
                .build();
        //构建客户端
        OpenAiClient openAiClient = OpenAiClient.builder()
                .apiKey(Arrays.asList("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"))
                //自定义key的获取策略：默认KeyRandomStrategy
                //.keyStrategy(new KeyRandomStrategy())
                .keyStrategy(new KeyRandomStrategy())
                .okHttpClient(okHttpClient)
                //自己做了代理就传代理地址，没有可不不传
//                .apiHost("https://自己代理的服务器地址/")
                .build();
        //聊天模型：gpt-3.5
        final String[] msg = new String[1];
        try{
            Message message = Message.builder().role(Message.Role.USER).content(context).build();
            ChatCompletion chatCompletion = ChatCompletion.builder().messages(Arrays.asList(message)).build();
            ChatCompletionResponse chatCompletionResponse = openAiClient.chatCompletion(chatCompletion);
            for (ChatChoice e : chatCompletionResponse.getChoices()) {
                msg[0] = e.getMessage().toString();
            }
            //正则表达式匹配
            String regex = "content=(.*?), name=";
            System.out.println(msg[0]);
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(msg[0]);
            boolean b = matcher.find();
            //        System.out.println(matcher.group(1));
            return ResultUtils.success(matcher.group(1));
        }catch (Exception e){
            return ResultUtils.error(ErrorCode.SYSTEM_ERROR,"今日的访问次数已用完！");
        }

    }

}
