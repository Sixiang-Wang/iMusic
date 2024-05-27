<template>
  <div class="weekly-report">
    <h1>我的听歌周报</h1>

    <!-- 封面歌曲 -->
    <div class="weekly-cover">
      <h2>本周封面歌曲</h2>
      <img :src="topSong.coverImageUrl" alt="本周封面歌曲" class="cover-image">
      <div class="song-info">
        <h3>{{ topSong.title }}</h3>
        <p>歌手：{{ topSong.singer }}</p>
      </div>
    </div>

    <!-- 播放统计 -->
    <div class="play-statistics">
      <h2>播放统计</h2>
      <canvas id="weeklyListeningChart"></canvas>
    </div>

    <!-- 艺术家统计 -->
    <div class="artist-statistics">
      <h2>歌手统计</h2>
      <ul>
        <li>最喜爱的歌手：{{ favoriteArtist.name }}</li>
      </ul>
    </div>

    <!-- 本周热门歌曲列表 -->
    <div class="top-songs">
      <h2>本周热门歌曲</h2>
      <ul>
        <li v-for="(song, index) in topSongs" :key="index">
          {{ song.title }} - {{ song.artist }}
        </li>
      </ul>
    </div>

    <!-- 新发现的艺术家 -->
    <div class="new-artists">
      <h2>新发现的艺术家</h2>
      <ul>
        <li v-for="(artist, index) in newArtists" :key="index">
          {{ artist.name }}
        </li>
      </ul>
    </div>

    <!-- 用户反馈 -->
    <div class="user-feedback">
      <h2>用户反馈</h2>
      <!-- 这里可以添加用户反馈的组件或表单 -->
    </div>

    <!-- 个性化推荐 -->
    <div class="personalized-recommendations">
      <h2>个性化推荐</h2>
      <!-- 这里可以添加个性化推荐的歌曲列表 -->
    </div>

    <!-- 本周总结与下周预告 -->
    <div class="weekly-summary">
      <h2>本周总结与下周预告</h2>
      <p>这里是对本周听歌活动的总结，以及下周可能感兴趣的音乐活动或新发行的音乐预告。</p>
    </div>
  </div>
</template>

<script>
import Chart from 'chart.js';
export default {
  name: 'ListeningReport',
  data() {
    return {
      topSong: {
        title: '歌曲标题',
        singer: '歌手名字',
        coverImageUrl: 'path/to/top-song-cover.jpg'
      },
      weeklyPlayData: {
        labels: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
        datasets: [{
          label: '播放次数',
          data: [200, 450, 300, 480, 350, 600, 500], // 示例数据
          backgroundColor: [
            'rgba(255, 99, 132, 0.4)',
            'rgba(54, 162, 235, 0.4)',
            'rgba(255, 206, 86, 0.4)',
            'rgba(75, 192, 192, 0.4)',
            'rgba(153, 102, 255, 0.4)',
            'rgba(255, 159, 64, 0.4)',
            'rgba(255, 99, 132, 0.4)'
          ],
          borderColor: [
            'rgba(255, 99, 132, 1)',
            'rgba(54, 162, 235, 1)',
            'rgba(255, 206, 86, 1)',
            'rgba(75, 192, 192, 1)',
            'rgba(153, 102, 255, 1)',
            'rgba(255, 159, 64, 1)',
            'rgba(255, 99, 132, 1)'
          ],
          borderWidth: 1
        }]
      },
      favoriteArtist: {
        name: '最喜爱的歌手'
      },
      topSongs: [
        { title: '歌曲1', artist: '艺术家1' },
        { title: '歌曲2', artist: '艺术家2' },
        // 更多歌曲...
      ],
      newArtists: [
        { name: '新艺术家1' },
        { name: '新艺术家2' },
        // 更多艺术家...
      ],
    };
  },
  filters: {
    formatDuration(duration) {
      // 建议选择分钟作为单位
      const minutes = Math.floor(duration);
      return `${minutes}分钟`;
    }
  },
  mounted() {
    this.createChart();
  },
  methods: {
    createChart() {
      new Chart(document.getElementById('weeklyListeningChart'), {
        type: 'bar',
        data: this.weeklyPlayData,
        options: {
          scales: {
            xAxes: [{
              stacked: true, // 如果你想要堆叠条形图，就设置为 true
              ticks: {
                display: true // 取消刻度标签显示
              },
              gridLines: {
                display: true // 保留网格线
              },
              // 这里没有 categoryPercentage 选项
              // 但是你可以控制条形之间的间隔，例如：
              barThickness: 40, // 设置条形的厚度
              categoryPercentage: 0.5, // 设置每个分类的宽度占画布宽度的比例
            }],
            yAxes: [{
              stacked: true, // 如果你想要堆叠条形图，就设置为 true
              gridLines: {
                display: true // 保留网格线
              },
              ticks: {
                beginAtZero: false, // 设置是否从零开始刻度
                display: true // 显示刻度标签
                // 其他刻度配置...
              }
            }]
          },
          // 这里没有 barPercentage 选项
          // 但是可以设置 maxBarThickness 来限制条形的最大宽度
          tooltips: {
            // 工具提示配置...
          },
          responsive: true, // 图表响应式配置
          maintainAspectRatio: true // 是否保持原始的纵横比
        }
      });
    }
  }
};
</script>

<style scoped>
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

.weekly-cover {
  background-color: #f9f9f9;
  border: 1px solid #ddd;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 30px;
  text-align: center; /* 使文本居中 */
}

.weekly-cover h2 {
  color: #333;
  font-size: 1.5em; /* 标题字体大小 */
  margin-bottom: 10px;
}

.cover-image {
  max-width: 100%; /* 图片宽度最大为容器宽度 */
  height: auto; /* 高度自适应保持图片比例 */
  border-radius: 4px; /* 图片圆角 */
  margin-bottom: 15px; /* 图片与下方内容的间距 */
}

.song-info {
  background-color: #fff;
  border: 1px solid #eee;
  border-radius: 4px;
  padding: 15px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 轻微的投影效果 */
}

.song-info h3 {
  color: #444;
  font-size: 1.2em; /* 歌曲标题字体大小 */
  margin-bottom: 5px;
}

.song-info p {
  color: #666;
  font-size: 1em; /* 艺术家名称字体大小 */
}

/* 响应式设计 */
@media (max-width: 768px) {
  .weekly-cover {
    padding: 15px;
  }

  .song-info {
    padding: 10px;
  }
}
.play-statistics {
  text-align: center; /* 使标题居中 */
  margin: 20px 0; /* 上下边距 */
  padding: 10px; /* 内边距 */
  background: transparent !important;
  border-radius: 8px; /* 圆角边框 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 轻微的投影效果 */
}

.play-statistics h2 {
  margin-bottom: 15px; /* 标题与画布之间的间距 */
  color: #333; /* 标题颜色 */
  font-size: 24px; /* 标题字体大小 */
}

.artist-statistics {
  margin: 20px 0; /* 上下边距 */
  padding: 20px; /* 内边距 */
  background-color: #f9f9f9; /* 轻微的背景颜色 */
  border-radius: 8px; /* 圆角边框 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 轻微的投影效果 */
}

.artist-statistics h2 {
  margin-bottom: 15px; /* 标题与列表之间的间距 */
  color: #333; /* 标题颜色 */
  font-size: 24px; /* 标题字体大小 */
}

.artist-statistics ul {
  list-style-type: none; /* 移除列表项默认的标记 */
  padding: 0; /* 移除列表默认的内边距 */
}

.artist-statistics li {
  background-color: #ffffff; /* 列表项背景颜色 */
  border: 1px solid #ddd; /* 列表项边框 */
  border-radius: 4px; /* 列表项圆角边框 */
  padding: 10px; /* 列表项内边距 */
  margin-bottom: 8px; /* 列表项之间的间距 */
  font-size: 16px; /* 文字大小 */
  line-height: 1.5; /* 行高 */
}

/* 如果需要添加鼠标悬停效果 */
.artist-statistics li:hover {
  background-color: #e9ecef; /* 鼠标悬停时的背景颜色 */
  cursor: pointer; /* 鼠标悬停时的光标样式 */
}

/* 响应式设计 */
@media (max-width: 768px) {
  .artist-statistics h2 {
    font-size: 20px; /* 移动设备上的标题字体大小 */
  }

  .artist-statistics li {
    font-size: 14px; /* 移动设备上的文字大小 */
  }
}
</style>
