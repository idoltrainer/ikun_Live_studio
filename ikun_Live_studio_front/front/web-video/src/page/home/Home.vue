<template>
	<div id="home">
		<div class="banner">
			<img src="../../assets/img/bilibili.webp" />
		</div>
		<div id="homeContent">
			<!-- 热门内容 下一行-->
			<div id="popular">
				<el-row class="popular-title-wrapper">
					<el-col :span="4">
						<el-row class="popular-title">
							<el-col :span="1">&nbsp;</el-col>
							<el-col :span="7" class="popular-icon-wrapper">
<!--								<font-awesome-icon :icon="['fab','hotjar']" class="popular-icon"></font-awesome-icon>-->
                <svg t="1698746940747" class="icon" viewBox="0 0 1024 1024" version="1.1"
                     xmlns="http://www.w3.org/2000/svg" p-id="3169" width="200" height="200">
                  <path d="M760.591059 332.739765L681.682824 409.6s0-307.2-263.107765-409.539765c0 0-26.262588 281.6-157.816471
                  383.939765-131.553882 102.460235-394.721882 409.6 131.433412 639.939765 0 0-263.168-281.6
                  78.908235-486.27953 0 0-26.322824 102.339765 105.231059 204.8 131.614118 102.4 0 281.6 0 281.6s631.506824-153.6
                  184.32-691.260235z" fill="#EA322B" p-id="3170"></path>
                </svg>
							</el-col>
							<el-col class="popular-title-text" :span="16">
								<span>热门内容</span>
							</el-col>
						</el-row>
					</el-col>
				</el-row>
				<!-- 内容item -->
				<el-row >
          <el-col :span="6">
            <div class="video-item">
              <img src="@/assets/image/basketball.jpg" @click="routerPush('/shotVideo')" />
              <div class="video-info">
                <span class="video-title" @click="routerPush('/shotVideo')">三十天精通篮球</span>
                <span class="video-publisher">作者：蔡徐坤</span>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="video-item">
              <img src="@/assets/image/basketball.jpg" @click="routerPush('/shotVideo')" />
              <div class="video-info">
                <span class="video-title" @click="routerPush('/shotVideo')">三十天精通篮球</span>
                <span class="video-publisher">作者：蔡徐坤</span>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="video-item">
              <img src="@/assets/image/basketball.jpg" @click="routerPush('/shotVideo')" />
              <div class="video-info">
                <span class="video-title" @click="routerPush('/shotVideo')">三十天精通篮球</span>
                <span class="video-publisher">作者：蔡徐坤</span>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="video-item">
              <img src="@/assets/image/basketball.jpg" @click="routerPush('/shotVideo')" />
              <div class="video-info">
                <span class="video-title" @click="routerPush('/shotVideo')">三十天精通篮球</span>
                <span class="video-publisher">作者：蔡徐坤</span>
              </div>
            </div>
          </el-col>
				</el-row>
			</div>

      <!--   标签内容   -->
      <div id="popular">
        <el-row class="popular-title-wrapper">
          <el-col :span="4">
            <el-row class="popular-title">
              <el-col :span="1">&nbsp;</el-col>
              <el-col :span="7" class="popular-icon-wrapper">
                <font-awesome-icon :icon="['fab','hotjar']" class="popular-icon"></font-awesome-icon>
              </el-col>
              <el-col class="popular-title-text" :span="16">
                <span>猜你喜欢</span>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
        <!-- 内容item -->
        <el-row>
          <el-col :span="6">
            <div class="video-item">
              <img src="@/assets/image/basketball.jpg" @click="routerPush('/shotVideo')" />
              <div class="video-info">
                <span class="video-title" @click="routerPush('/shotVideo')">三十天精通篮球</span>
                <span class="video-publisher">作者：蔡徐坤</span>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="video-item">
              <img src="@/assets/image/basketball.jpg" @click="routerPush('/shotVideo')" />
              <div class="video-info">
                <span class="video-title" @click="routerPush('/shotVideo')">三十天精通篮球</span>
                <span class="video-publisher">作者：蔡徐坤</span>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="video-item">
              <img src="@/assets/image/basketball.jpg" @click="routerPush('/shotVideo')" />
              <div class="video-info">
                <span class="video-title" @click="routerPush('/video/video-id')">三十天精通篮球</span>
                <span class="video-publisher">作者：蔡徐坤</span>
              </div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="video-item">
              <img src="@/assets/image/basketball.jpg" @click="routerPush('/shotVideo')" />
              <div class="video-info">
                <span class="video-title" @click="routerPush('/shotVideo')">三十天精通篮球</span>
                <span class="video-publisher">作者：蔡徐坤</span>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
		</div>
	</div>
</template>

<script>
import { routerPush } from "@/utils/vue_router_util.js";

export default {
	name: "Home",
	methods: {
		routerPush(path) {
			routerPush.call(this, path);
		}
	},
  created() {
    this.$parent.Ifshow= true
  },
	data() {
		return {
			program: [
				[
					{
						text: "足球"
					},
					{
						text: "篮球"
					},
					{
						text: "网球"
					},
					{
						text: "羽毛球"
					},
					{
						text: "乒乓球"
					},
					{
						text: "象棋"
					},
					{
						text: "围棋"
					},
					{
						text: "军棋"
					}
				],
				[
					{
						text: "游泳"
					},
					{
						text: "拳击"
					},
					{
						text: "体操"
					},
					{
						text: "田径"
					},
					{
						text: "赛马"
					},
					{
						text: "射击"
					},
					{
						text: "射箭"
					},
					{
						text: "速滑"
					}
				]
			]
		};
	}
};
</script>

<style scoped>
/* 横幅 */
.banner {
	width: 100%;

}
.banner img {
	width: 100%;
  height: 150px;
}
/* 主页内容 */

#homeContent {
  background-color: transparent;
  /*margin-top: 50px;*/
  /*background-color: rgba(22,24,35,0.8);*/
  /*margin-top: 100px;*/
	width: 80%;
	margin: 0 10%;
	max-width: 1400px;
}
/* 1.菜单栏 */
#menu {
	padding: 20px 0;
	box-sizing: border-box;
	border-bottom: 1px solid #e7e7e7;
}
.column-wrapper {
	display: flex;
	position: relative;
	align-items: center;
	height: 100%;
	width: 100%;
}
/* 菜单栏按钮-左栏 */
.menu-left {
	display: flex;
	padding-left: 20px;
	justify-content: space-between;
}
.ml-item-wrapper {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
}
.ml-item-wrapper span {
	margin-top: 4px;
	font-size: 14px;
}
.menu-left-item {
	height: 39px;
	width: 39px;
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
}
/* 菜单栏左栏 */
.menu-left-left-column {
	display: flex;
	justify-content: flex-start;
}
.home-wrapper {
	background-color: #f45b7b;
}
.menu-home {
	font-size: 24px;
	color: #ffffff;
}
.menu-left-middle-column {
	display: flex;
	justify-content: center;
}
.comment-dots-wrapper {
	background-color: #41a1d7;
}
.menu-comment-dots {
	font-size: 24px;
	color: #ffffff;
}
.menu-left-right-column {
	display: flex;
	justify-content: flex-end;
}
.trophy-wrapper {
	flex-direction: column-reverse;
	background-color: #ef6c00;
}
.menu-trophy {
	padding-top: 3px;
	font-size: 24px;
	color: white;
}
/* 纵向分割线 */
.break-column {
	float: left;
	height: 40px;
	border-left: 1px solid #e7e7e7;
	margin: 0 20px;
}
/* 菜单栏中间项目列表 */
.program-wrapper {
	position: relative;
	height: 100%;
}
.program-list {
	margin: 10px 0;
}
.program-item {
	font-size: 14px;
	color: #212121;
}
.program-item span:hover {
	color: #00a1d6;
	cursor: pointer;
	transition: color 0.5s;
}
/* 2.热门内容 */
#popular {
  color: #ffffff;
	/*margin: 40px 0 30px 0;*/
}
.popular-title-wrapper {
	/*margin: 20px 0;*/
}
.popular-title {
	display: flex;
	align-items: center;
}
.popular-icon-wrapper {
	display: flex;
	justify-content: center;
}
.popular-icon {
	font-size: 36px;
}
.popular-title-text {
	font-size: 20px;
  font-weight: bold;
}


/* 热门内容中的一个视频条目 */
.video-item {

	padding-top: 0.1%;
  padding-right: 10px;
	display: flex;
	flex-direction: column;
}
.video-item img {
	color: #212121;
	width: 100%;
	cursor: pointer;
  height: 100%;
  /*padding-top: 50%;*/
  border-radius: 20px;
}


el-card {
  min-width: 100%;
  height: 100%;
}


.video-title {
	display: block;
	color: #212121;
	margin: 10px 0 5px 0;
	font-size: 14px;
	font-weight: 500;
	cursor: pointer;
}
.video-publisher {
	display: block;
	font-size: 12px;
	color: #999;
	cursor: pointer;
}
</style>