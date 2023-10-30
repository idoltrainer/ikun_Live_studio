package com.idoltrainer.webvideo.recommend;



import com.idoltrainer.webvideo.domain.Likes;
import com.idoltrainer.webvideo.domain.Video;
import com.idoltrainer.webvideo.model.recommend.UserRecommend;
import com.idoltrainer.webvideo.service.LikesService;
import com.idoltrainer.webvideo.service.UserService;
import com.idoltrainer.webvideo.service.VideoService;
import com.idoltrainer.webvideo.util.MapSortUtils;
import com.idoltrainer.webvideo.util.RelationCaculate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Auther: truedei
 * @Date: 2020 /20-6-13 20:19
 * @Description: 推荐系统
 */

@Component
public class RecommendProcess {


    @Resource
    private LikesService likesService;

    @Resource
    private VideoService videoService;

    @Resource
    private UserService userService;
    /**
     * 登录后推荐,未登录直接随机推荐
     */
    @Async
    @Scheduled(cron="0/5 * * * * ?")
    public void recommend(){
        //获取当前对象
        System.out.println("推荐算法开始");
        //1，使用该用户的名字获取订单信息
        System.out.println("----------------");
        //查询登录用户的点赞信息
//        List<ProductOrder> productOrderList = DBHelp.getProductOrderList(userId);
        List<Likes> personLikesList = likesService.query().eq("userId", 7).list();
        //存储个人 购买的所有的视频id
        Long[] videos = new Long[personLikesList.size()];

        //存储个人信息，封装成对象，方便计算
        UserRecommend userRecommend = new UserRecommend();

        //筛选出来个人订单中的商品的id
        System.out.println("个人的：");
        for (int i = 0; i < personLikesList.size(); i++) {
            videos[i] = personLikesList.get(i).getVideoId();
            System.out.println(personLikesList.get(i).toString());
        }
        userRecommend.setUserId(personLikesList.get(0).getUserId());
        userRecommend.setVideoId(videos);


        //2,拿到所有用户的订单信息
//        List<ProductOrder> productOrderLists = DBHelp.getProductOrderList(null);
        List<Likes> likesList = likesService.query().list();
        //存储所有人的订单信息
        List<UserRecommend> allUserRecommends = new ArrayList<>();

        //利用map的机制，计算出来其余用户的所有的购买商品的id  Map<用户id，商品ID拼接的字符串(1,2,3,4)>
        Map<Long, String> map = new HashMap<>();


        System.out.println("所有人的：");
        //筛选出来订单中的商品的id
        for (int i = 0; i < likesList.size(); i++) {
            System.out.println(likesList.get(i).toString());

            map.put(likesList.get(i).getUserId(),
                    map.containsKey(likesList.get(i).getUserId())?
                            map.get(likesList.get(i).getUserId())+","+likesList.get(i).getVideoId(): likesList.get(i).getVideoId()+"");
        }
        //开始封装每个人的数据
        for (Long key:map.keySet() ) {

            //new出来一个新的个人的对象，后面要塞到list中
            UserRecommend userR2 = new UserRecommend();

            //把其他每个人购买的商品的id 分割成数组
            String[] split = map.get(key).split(",");

            //转换成int数组 进行存储，方便后期计算
            Long[] longs1 = new Long[split.length];
            for (int i = 0; i < split.length; i++) {
                longs1[i] = Long.valueOf(split[i]);
            }

            //用户id 就是key
            userR2.setUserId(key);
            //用户购买的商品id的数组
            userR2.setVideoId(longs1);

            //塞到list中
            allUserRecommends.add(userR2);
        }

        //二值化 处理数据
        List<UserRecommend> userRList = jisuan(userRecommend, allUserRecommends);

        System.out.println("得出的结果：");
        for (int i = 0; i < userRList.size(); i++) {
            System.out.println(userRList.get(i).toString());
        }

        System.out.println("过滤处理数据之后：");
        //过滤处理
        String sqlId = chuli(userRList, userRecommend);

        System.out.println("推荐的商品：");
        System.out.println(sqlId);
        //通过拿到的拼接的被推荐商品的id，去查数据库
//        List<Video> videoList = videoService.query().eq("id",sqlId).list();
//        //最终拿到被推荐商品的信息
//        for (int i = 0; i < videoList.size(); i++) {
//            System.out.println(videoList.get(i).toString());
//        }

    }



    /**
     * 过滤处理
     * @param userRList 所有用户的订单数据
     * @param userR 当前登录用户的订单数据
     * @return
     */
    private String chuli(List<UserRecommend> userRList,UserRecommend userR) {

        //为了方便下面过滤数据，预先把登录用户的订单购物的商品的id做一个map，在过滤的时候，只需要查一下map中是否存在key就ok
        Map<Long,Long> map1 = new HashMap<>();
        for (int i = 0; i < userR.getVideoId().length; i++) {
            map1.put(userR.getVideoId()[i],userR.getVideoId()[i]);
        }


        //盛放最终过滤出来的数据 Map<商品id,出现的次数>
        Map<Long,Integer> map = new HashMap<>();

        for (int i = 0; i < userRList.size(); i++) {
            //userRList.get(i).getCos_th()>0：过滤掉相似度等于0，也就是完全不匹配的
            //userRList.get(i).getUserId()!=userR.getUserId()：过滤掉当前用户的订单信息
            if(userRList.get(i).getCos_th()>0 && userRList.get(i).getUserId()!=userR.getUserId()){
                //求当前登录用户的购买商品的id和其他用户的所购买商品的差集，例如：A=[1, 2],B=[1, 2, 3]  那么这个3就是最终想要的结果
                Long[] j = RelationCaculate.getC(userRList.get(i).getVideoId(), userR.getVideoId());

                //遍历求差集之后的结果
                for (int i1 = 0; i1 < j.length; i1++) {
                    //如果其余的用户所购买撒谎那个品的id不在当前用的所购买商品的id，那么就存起来
                    if(!map1.containsKey(j[i1])){
                        //存储时，数量每次都+1，方便后面排序，出现的次数多，说明被推荐的机会越高
                        map.put(j[i1],map.containsKey(j[i1])?(map.get(j[i1])+1):1);
                    }
                }
            }
        }


        System.out.println("处理之后的map：");
        for (Long key:map.keySet()) {
            System.out.println("商品id="+key+"--用户所购数量="+map.get(key));
        }

        //把map进行降序排序
        Map<Long, Integer> map2 = MapSortUtils.sortByKeyDesc(map);
        System.out.println("按降序" + map2);


        //拼接成一个sql，方便去查数据库
        String sqlId = "";
        for (Long key:map2.keySet()) {
            sqlId = sqlId+key +",";
        }

        if (sqlId==""){
            System.out.println("数据量少，没有可以推荐的");
            return null;
        }
        sqlId = sqlId.substring(0,sqlId.length()-1);

        System.out.println("最终拿到的被推荐给当前用户的商品id--->"+sqlId);

        return sqlId;
    }

    /**
     * 二值化 处理数据
     * @param userR 当前登录用户的订单信息
     * @param userRS 其他用户的订单信息
     * @return 二值化处理之后的结果
     */
    private List<UserRecommend> jisuan(UserRecommend userR, List<UserRecommend> userRS) {

        //对个人做二值化处理，为了好计算 [0,0,0,0,0,1,1,0,1]这种
        //个人的
        int userErzhihua[] = new int[10];
        for (int i = 0; i < userR.getVideoId().length; i++) {
            userErzhihua[Math.toIntExact(userR.getVideoId()[i])]=1;
        }


        //库里所有人的
        int erzhihua[] = new int[10];
        //对其他人，做二值化处理，为了好计算 [0,0,0,0,0,1,1,0,1]这种
        for (int i = 0; i < userRS.size(); i++) {
            UserRecommend product = userRS.get(i);
            for (int j = 0; j < product.getVideoId().length; j++) {
                erzhihua[Math.toIntExact(product.getVideoId()[j])]=1;
            }
            //计算当前登录用户与其余每个人的余弦值 cos_th
            Double compare = compare( erzhihua,userErzhihua);
            product.setCos_th(compare);

            //把计算好的值，重新塞到原来的位置，替换到旧的数据
            userRS.set(i,product);

            //防止数组中的值重复，起到清空的作用
            erzhihua = new int[10];
        }

        return userRS;

    }

    /**
     * 代码核心内容
     * @param o1 当前登录用户的
     * @param o2 其他用户的 n1 n2 n3 n4 n....
     * @return
     */
    private static Double compare(int[] o1, int[] o2) {
        //分子求和
        Double fenzi = 0.0 ;

        for (int i = 0; i < o1.length; i++) {
            fenzi += o1[i]*o2[i];
        }
        //分母第一部分
        Double fenmu1 = 0.0;
        for (int i = 0; i < o1.length; i++) {
            fenmu1 += o1[i] * o1[i];
        }
        fenmu1 = Math.sqrt(fenmu1);
        //分母第二部分
        Double fenmu2 = 0.0;
        for (int i = 0; i < o2.length; i++) {
            fenmu2 += o2[i] * o2[i];
        }
        fenmu2 = Math.sqrt(fenmu2);
        return fenzi / (fenmu1 * fenmu2);
    }
}
