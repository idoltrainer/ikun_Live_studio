package com.idoltrainer.webvideo;

import com.idoltrainer.webvideo.recommend.RecommendProcess;
import com.idoltrainer.webvideo.service.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest
class WebVideoApplicationTests {

    @Resource
    private LivesService livesService;

    @Test
    void contextLoads() {
    }

}
