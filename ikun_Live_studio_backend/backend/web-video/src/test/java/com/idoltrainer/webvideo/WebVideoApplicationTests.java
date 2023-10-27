package com.idoltrainer.webvideo;

import com.idoltrainer.webvideo.domain.User;
import com.idoltrainer.webvideo.domain.Video;
import com.idoltrainer.webvideo.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
class WebVideoApplicationTests {

    @Autowired
    private UserService userService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private RelationsService relationsService;

    @Autowired
    private LikesService likesService;

    @Autowired
    private FavourService favourService;


    @Test
    void contextLoads() {
//        User user = new User();
//        user.setId(1l);
//        user.setUserAccount("eduhudheuhfu");
//        user.setUserPassword("eufheufhue");
//        userService.save(user);
//        Video video = new Video();
//        video.setId(1l);
//        video.setCoverUrl("eudheuhfuehf");
//        video.setPlayUrl("fiejiejifjei");
//        video.setUserId(1l);
//        video.setCreateTime(new Date());
//        video.setUpdateTime(new Date());
//        videoService.save(video);
    }

}
