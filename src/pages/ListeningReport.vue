<template>
  <div class="weekly-report">
    <h1>我的听歌周报</h1>
<!--     http://localhost:8081/#/listeningReport-->
    <!-- 封面歌曲 -->
    <div class="weekly-cover">
      <h2>本周封面歌曲</h2>
      <img :src="topSong.ImageUrl" class="cover-image">
      <div class="song-info">
        <h3>{{ topSong.title }}</h3>
        <p>歌手：{{ topSong.singer }}</p>
      </div>
    </div>

    <!-- 播放统计 -->
    <div class="play-statistics">
      <h2>播放统计</h2>
      <canvas id="weeklyListeningChart" v-if="noData===false"></canvas>
      <p v-else>暂无听歌数据</p>
    </div>

    <!-- 艺术家统计 -->
    <div class="artist-statistics">
      <h2>歌手统计</h2>
      <ul>
        <li>最喜爱的歌手：{{ favoriteSingerName }}</li>
      </ul>
    </div>

    <!-- 本周热门歌曲列表 -->
    <div class="top-songs">
      <h2>本周热门歌曲</h2>
      <canvas id="popularSongs" v-if="noData===false" style="max-height: 40vh;width: auto"></canvas>
      <p v-else>暂无听歌数据</p>
    </div>

<!--     新发现的歌手 -->
    <div class="content-list">
      <h2>新发现的歌手</h2>
      <ul class="section-content">
        <li class="content-item" v-for="(value,index) in newSingers" :key="index">
          <div class="kuo">
            <img class="item-img" :src="attachImageUrl(value.pic)">
          </div>
          <p class="item-name">{{ value.name }}</p>
        </li>
      </ul>
    </div>
    <!-- 个性化推荐 -->
    <div class="content-list">
      <h2>新发现的歌单</h2>
      <!-- 这里可以添加个性化推荐的歌单列表 -->
      <ul class="section-content">
        <li class="content-item" v-for="(value,index) in newSongLists" :key="index">
          <div class="kuo">
            <img class="item-img" :src="attachImageUrl(value.pic)">
          </div>
          <p class="item-name">{{ value.title }}</p>
        </li>
      </ul>
    </div>
    <!-- 用户反馈 -->
    <div class="user-feedback">
      <h2>用户反馈</h2>
      <form class="survey-form" @submit.prevent="submitFeedback">
        <h3>用户满意度调查</h3>
        <p>请为您本周的听歌体验打分（1-5分）：</p>
        <fieldset class="rating">
          <legend>选择您的评分：</legend>
          <label
              v-for="index in 5"
              :key="index"
              :for="'star' + index"
              class="star-label"
              :class="{ 'selected-left': index <= (hoverIndex || selectedRating) }"
              @mouseover="hoverIndex = index"
              @mouseleave="hoverIndex = 0"
            >
            <input type="radio" :id="'star' + index" :value="index" v-model="selectedRating" />
              ★
          </label>
        </fieldset>

        <h3>歌曲推荐反馈</h3>
        <p>您对本周推荐的歌曲满意吗？</p>
        <div class="options-container">
          <label class="radio-label" v-for="option in options" :key="option.value">
            <input type="radio" :value="option.value" v-model="selectedOption" @change="changeUserFeeling(option.value)"/>
            <span></span>
            {{ option.text }}
          </label>
          <img  :src="userFeeling" class="svg-icon-right">
        </div>

        <button class="submit-button" type="button" @click="submitFeedback">提交</button>
      </form>
    </div>
  </div>
</template>

    <script>
import Chart from 'chart.js';
import {mapGetters} from "vuex";
import {getRecentSongOrderByCount, recommendSinger, recommendSongList} from "../api";
import {mixin} from "../mixins";
export default {
  name: 'ListeningReport',
  mixins: [mixin],
  data() {
    return {
      recentList : [],      // 最近播放列表
      playData : [],      // 听歌数据
      listeningSongsList: [],   // 听歌列表(歌名与歌手名）
      listeningTimesList: [],    // 听歌次数列表
      selectedRating: 0, // 默认选中的星星
      hoverIndex: null,    // 鼠标悬停时的星星索引
      selectedOption: '', // 用于v-model的数据属性
      noData : false,
      options: [
        { value: 'fairly_like', text: '非常满意' },
        { value: 'like',text:'满意'},
        { value: 'neutral', text: '一般' },
        { value: 'dislike', text: '不满意' },
        { value: 'fairly_dislike', text: '非常不满意' },
      ],
      topSong : {
        ImageUrl: '',
        title : '',
        singer : ''
      },
      favoriteSingerName: '',
      popularSongs: [],
      newSingers: [],
      newSongLists: [],
      userFeeling : require("../assets/js/icon/icon-like.svg")
    };
  },
  computed: {
    ...mapGetters([
      'userId'
    ])
  },
  mounted() {
    this.getOrderByCount(this.userId);
    this.getNewSingers(this.userId);
    this.getNewSongLists(this.userId);
    this.createChart();// 听歌时长
    // this.createPieChart(); // 播放量前五的歌曲
  },
  methods: {
    getOrderByCount(userId){
      getRecentSongOrderByCount(userId)
        .then(res => {
          if (res === null) {
            this.noData = true;
            this.$nextTick(() => {
              // 确保视图更新后执行 createPieChart
              this.createPieChart();
            });
            return;
          }
          this.recentList = res.data;
          this.favoriteSingerName = this.replaceLName(this.recentList[0].name);
          this.topSong.ImageUrl = this.attachImageUrl(this.recentList[0].pic);
          this.topSong.title = this.replaceFName(this.recentList[0].name);
          this.topSong.singer = this.favoriteSingerName;

          for(let i = 0;i < this.recentList.length && i<5 ;i++){
            var songs = this.recentList[i];
            this.listeningSongsList.push(this.replaceFName(songs.name)+"-"+this.replaceLName(songs.name));
            this.listeningTimesList.push(songs.playCount)
          }
          // console.log(this.listeningSongsList);
          // console.log(this.listeningTimesList);
          this.$nextTick(() => {
            // 确保视图更新后执行 createPieChart
            this.createPieChart();
          });
        })
        .catch(error => {
          console.log('getRecentSongOrderByCount failed' + error);
          this.noData = true;
        })
    },
    getNewSingers(userId){
      recommendSinger(userId)
        .then(res => {
          // console.log(res);
          this.newSingers = res.data.slice(0,6);
        })
        .catch(error =>{
          console.log(error);
        })
    },
    getNewSongLists(userId){
      recommendSongList(userId)
        .then(res =>{
          // console.log(res);
          this.newSongLists = res.data.slice(0,6);
        })
        .catch(error => {
          console.log(error);
        })
    },
    createChart() {
      for (let i = 0; i < 7; i++) {
        // 生成0到50的随机整数
        let randomNumber = Math.floor(Math.random() * 66); // 51是50+1，因为包含0和50
        this.playData.push(randomNumber);
      }
      new Chart(document.getElementById('weeklyListeningChart'), {
        type: 'bar',
        data: {
          labels: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],
            datasets: [{
            label: '听歌时长',
            data: this.playData, // 示例数据
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
    },
    createPieChart(){
      if (!this.listeningSongsList || !this.listeningTimesList) {
        console.error('Data is not ready for creating pie chart.');
        return;
      }
      new Chart(document.getElementById('popularSongs'), {
        type: 'pie',
        data: {
          labels: this.listeningSongsList,
          datasets: [{
            label: '播放次数',
            data: this.listeningTimesList,
            backgroundColor: [
              'rgba(255, 99, 132, 0.2)',
              'rgba(54, 162, 235, 0.2)',
              'rgba(255, 206, 86, 0.2)',
              'rgba(75, 192, 192, 0.2)',
              'rgba(153, 102, 255, 0.2)'
            ],
            borderColor: [
              'rgba(255, 99, 132, 1)',
              'rgba(54, 162, 235, 1)',
              'rgba(255, 206, 86, 1)',
              'rgba(75, 192, 192, 1)',
              'rgba(153, 102, 255, 1)'
            ],
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          legend: {
            position: 'top',
          },
          title: {
            display: false,
          },
          animation: {
            animateScale: true,
            animateRotate: true
          },
        }
      })
    },
    submitSurvey(){
      document.querySelectorAll('input[type="radio"][name="rating"]').forEach(radio => {
        radio.addEventListener('change', function() {
          // 移除所有星星的选中样式
          document.querySelectorAll('.star-label').forEach(label => {
            label.classList.remove('selected-left');
          });

          // 选择当前选中的单选按钮之前的所有label元素
          let previousLabels = this.previousElementSibling ?
            this.previousElementSibling.querySelectorAll('.star-label') :
            [];

          // 为这些label元素添加选中样式
          previousLabels.forEach(label => {
            label.classList.add('selected-left');
          });
        });
      });
    },
    changeUserFeeling(value){
      // this.userFeeling = value;
      if(value==='fairly_like'){
        this.userFeeling=require("../assets/js/icon/icon-fairly-like.svg");
      }
      else if(value==='like'){
        this.userFeeling=require("../assets/js/icon/icon-like.svg");
      }
      else if(value==='neutral'){
        this.userFeeling=require("../assets/js/icon/icon-neutral.svg");
      }
      else if(value==='dislike'){
        this.userFeeling=require('../assets/js/icon/icon-dislike.svg');
      }
      else{
        this.userFeeling=require('../assets/js/icon/icon-fairly-dislike.svg');
      }
      // console.log(value)
    },
    submitFeedback() {
      this.$notify({title:'感谢您的反馈',type:'success'});
    },
  }
};
</script>

<style lang="scss" scoped>
@import "../assets/css/listeningReport.scss";
@import "../assets/css/tryContentList.scss";
</style>
