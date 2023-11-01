import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from "@/page/home/Home.vue"
import Login from "@/page/login/Login.vue"
import RegisterTel from "@/page/login/RegisterTel.vue"
import RegisterMail from "@/page/login/RegisterMail.vue"
import Video from "@/page/video/Video.vue"
import VideoList from "@/page/sports/VideoList.vue"
import VideoUpload from "@/page/video_upload/VideoUpload.vue"
import Personal from "@/page/user/Personal";
import ShotVideo from "@/components/ShotVideo";

Vue.use(VueRouter)

const DEFAULT_PAGE = {
    path: "/",
    redirect: "/home"
}

const HOME = {
    path: "/home",
    component: Home
}

const LOGIN = {
    path: "/login/login",
    component: Login
}

const REGISTERTEL = {
    path: "/login/register_tel",
    component: RegisterTel
}

const REGISTERMAIL = {
    path: "/login/register_mail",
    component: RegisterMail
}

const VIDEO = {
    path: "/video/:param",
    component: Video
}

const VIDEO_LIST = {
    path:"/video_list",
    component:VideoList
}

const VIDEO_UPLOAD = {
    path: "/video_upload",
    component: VideoUpload
}

// const LIVE = {
//     path: "/live",
//     component: Room
// }

const INFO_PAGE = {
    // path: '/personal/info/:id',
    path: '/newsuser/personal/:id',
    component: Personal,
    meta: {
        requireLogin: true
    },
    children: [
        {
            path: '/newsuser/personal/info/:id',
            name:'info',
            component: r => require.ensure([], () => r(require('@/page/user/UserInfo')), 'info')
        },
        {
            path:'/newsuser/personal/myarticle/:id',
            name:'myarticle',
            component: r => require.ensure([], () => r(require('@/page/user/MyVideo')), 'myarticle')
        },
        {
            path:'/newsuser/personal/mycollect/:id',
            name:'mycollect',
            component: r => require.ensure([], () => r(require('@/page/user/MyFavour')), 'mycollect')
        },
        {
            path:'/newsuser/personal/myfan/:id',
            name:'myfan',
            component: r => require.ensure([], () => r(require('@/page/user/MyFan')), 'myfan')
        },
        {
            path:'/newsuser/personal/myfollow/:id',
            name:'myfollow',
            component: r => require.ensure([], () => r(require('@/page/user/MyFan')), 'myfollow')
        }
    ],

}

const SHOT_VIDEO = {
    path: "/shotVideo",
    component: ShotVideo
}



const routes = [
    DEFAULT_PAGE,   // 默认页面
    HOME,           // 主页
    LOGIN,          // 登陆/注册页面
    REGISTERTEL,
    REGISTERMAIL,
    VIDEO,          // 视频详情页面
    VIDEO_LIST,     // 视频列表页面，比如足球视频、排球视频等。
    VIDEO_UPLOAD,   // 上传视频
    INFO_PAGE,
    SHOT_VIDEO
]

const router = new VueRouter({
    routes,
    mode: 'history',
})


export default router


