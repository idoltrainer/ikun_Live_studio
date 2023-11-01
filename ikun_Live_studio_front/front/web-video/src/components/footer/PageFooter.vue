<template>
	<div id="pageFooter">
		<div class="page-footer-content">
		</div>
	</div>
</template>

<script>
export default {
	name: "PageFooter",
	methods: {
		shareLink(type) {
			alert(type);
		}
	}
};
</script>

<style scoped>
#pageFooter {
	/*width: 100%;*/
  background-color: rgba(22,24,35,0.9);
	/*display: flex;*/
	/*justify-content: center;*/
  position:fixed;
  left:0px;
  bottom:0px;
  height:30px;
  width:100%;
}
.page-footer-content {
    max-width: 1400px;
    width: 80%;
}
/* 底栏第一行：网站信息 */
.website-info {
	padding: 52px 0 86px 0;
	border-bottom: 2px solid #dcdfe6;
}
/* 条目内容1*/
.footer-item1 .item-header {
	display: flex;
	align-items: center;
	color: #656b7b;
	font-size: 12px;
	font-weight: 500;
	height: 40px;
	width: 100%;
	padding: 12px 0;
}
.footer-item1 .item-content {
	font-size: 14px;
	color: #656b7b;
	font-weight: 400;
}
.footer-item1 .item-content ul li {
	list-style: none;
}

/* 底栏第二行：其他信息 */
.other-info {
	display: flex;
	align-items: center;
	padding: 32px 0px;
	font-size: 12px;
	color: #656b7b;
	letter-spacing: 1px;
}
/* icp备案信息 */
.footer-icp {
	display: flex;
	justify-content: center;
}
.brand-logo {
	font-size: 19px;
    margin-right: 32px;
    cursor: pointer;
}
</style>