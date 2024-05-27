<template>
  <div class="weekly-report">
    <h1>创作周报</h1>

    <!-- 周报封面 -->
    <div class="weekly-cover">
      <h2>封面故事</h2>
      <p>这里是封面故事的简介，可以包含一些重要的信息或者本周的亮点。</p>
      <!-- 假设你有一个封面图片的URL -->
      <img :src="coverImageUrl" alt="封面图片" class="weekly-report-image">
    </div>

    <!-- 周报内容 -->
    <div class="weekly-content">
      <h2>本周亮点</h2>
      <p>这里是本周亮点的详细内容，可以包含一些关键的成就或者有趣的事件。</p>

      <!-- 周报列表 -->
      <ul class="weekly-report-list">
        <li v-for="item in weeklyHighlights" :key="item">{{ item }}</li>
      </ul>
    </div>

    <!-- 周报总结 -->
    <div class="weekly-summary">
      <h2>本周总结</h2>
      <p>这里是对本周工作的总结，包括完成的任务和下周的计划。</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CreateReport',
  data() {
    return {
      // 封面图片的URL，实际使用时需要替换为有效的图片路径
      coverImageUrl: '',
      // 周报亮点列表
      weeklyHighlights: [
        '项目1的进展情况',
        '项目2的更新信息',
        '团队成员的成就',
        // 更多亮点...
      ]
    };
  }
};
</script>

<style scoped>
/* 重置浏览器默认样式 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Arial', sans-serif;
  background-color: #f4f4f4;
  color: #333;
  line-height: 1.6;
}

.weekly-report {
  width: 80%;
  margin: 15vh auto;
  padding: 20px;
  background-color: #fff;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

/* 周报标题 */
.weekly-report h1 {
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}

/* 周报内容 */
.weekly-report-content {
  margin-bottom: 20px;
}

.weekly-report-content h2 {
  margin-bottom: 10px;
  color: #555;
}

.weekly-report-content p {
  margin-bottom: 10px;
}

/* 周报列表 */
.weekly-report-list {
  list-style-type: none;
}

.weekly-report-list li {
  margin-bottom: 5px;
  padding: 5px;
  background-color: #e9ecef;
  border-left: 3px solid #007bff;
}

/* 周报图片 */
.weekly-report-image {
  max-width: 100%;
  height: auto;
  margin-bottom: 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .weekly-report {
    width: 95%;
  }
}
</style>
