import {addNums, getCollectOfUserId, likeSongOfName} from "../api";
import {mapGetters} from "vuex";


// import {
//   addPlayCount,
//   gerAllSongAndSingerNameBySingerId,
//   getAllSongBySongListId,
//   getSongByPlayCount,
// } from "../api/Song";
// import { getAllCollectByConsumerId } from "../api/Collect";
import { addRecentSong } from "../api/index";


export const mixin = {
  computed: {
    ...mapGetters([
      'loginIn',    // 用户是否已登录
      'userId'      // 当前登录用户的id
    ])
  },
  methods: {
    // 提示信息
    notify(title, type) {
      this.$notify({
        title: title,
        type: type
      })
    },

    // 获取图片地址
    attachImageUrl(srcurl) {
      if (srcurl) {
        const imageUrl = this.$store.state.configure.HOST + srcurl;
        return imageUrl;
      } else {
        return "../assets/img/user.jpg";
      }
    },

    // 根据歌手名字模糊查询歌曲
    getSong() {
      if (!this.$route.query.keywords) {
        this.notify('您输入的内容为空', 'warning');
      } else {
        likeSongOfName(this.$route.query.keywords).then(res => {
          if ((!res.length)) {
            this.$store.commit('setListOfSongs', []);
            this.notify('系统暂无符合条件的歌曲', 'warning');
          } else {
            this.$store.commit('setListOfSongs', res);
          }
        }).catch(err => {
          console.log(err)
        })
      }
    },
    // 获取名字前半部分 -- 歌手名
    replaceLName(str) {
      let arr = str.split('-');
      return arr[0];
    },
    // 获取名字后半部分-- 歌名
    replaceFName(str) {
      let arr = str.split('-');
      return arr[1];
    },
    // 播放
    toplay: function (id, url, pic, index, name, lyric, playList) {
      this.$store.commit('setId', id);
      this.$store.commit('setUrl', this.$store.state.configure.HOST + url);
      this.$store.commit('setPicUrl', this.$store.state.configure.HOST + pic);
      this.$store.commit('setListIndex', index);
      this.$store.commit('setTitle', this.replaceFName(name));
      this.$store.commit('setArtist', this.replaceLName(name));
      this.$store.commit('setLyric', this.parseLyric(lyric));
      this.$store.commit('setPlayList',playList);
      this.$store.commit('setCurTime',0);
      this.$store.commit('setChangeTime',0);
      this.$store.commit('setIsActive', false);
      if (this.loginIn) {
        getCollectOfUserId(this.userId)
          .then(res => {
            // 日后优化
            for (let item of res) {
              if (item.songId === id) {
                this.$store.commit('setIsActive', true);
                break;
              }
            }
          })
      }

      let RecentSong = {
        userId: this.userId,
        songId: id,
        count: 1,
      };
      if (this.userId) {
        this.addRecentSong(RecentSong);
      }
      addNums(id);

    },


    addRecentSong(RecentSong) {
      addRecentSong(RecentSong).then((res) => {
        if (res.code === 200) {
          console.log("msg:", res.msg);
        }
      });
    },
    // addPlayCount(id) {
    //   let SongVo = {
    //     id: id,
    //   };
    //   addPlayCount(SongVo).then((res) => {
    //     if (res.data.code === 200) {
    //       console.log(res.data.msg);
    //     }
    //   });
    // },
    // getSongByPlayCount() {
    //   getSongByPlayCount().then((res) => {
    //     if (res.data.code === 200) {
    //       this.$store.commit("setListOfSongs", res.data.data);
    //     }
    //   });
    // },
    // getSongOfSingerId() {
    //   gerAllSongAndSingerNameBySingerId(this.$route.query.id).then((res) => {
    //     if (res.data.code === 200) {
    //       this.$store.commit("setListOfSongs", res.data.data);
    //     }
    //   });
    // },
    // getUserCollectData() {
    //   getAllCollectByConsumerId(this.userId).then((res) => {
    //     if (res.data.code === 200) {
    //       this.$store.commit("setCollectList", res.data.data);
    //     } else {
    //       this.$store.commit("setCollectList", []);
    //     }
    //   });
    // },
    // getSongListDetailById() {
    //   getAllSongBySongListId(this.$route.query.id).then((res) => {
    //     if (res.data.code === 200) {
    //       this.$store.commit("setListOfSongs", res.data.data);
    //     } else {
    //       this.$store.commit("setListOfSongs", []);
    //     }
    //   });
    // },


    // 解析歌词
    parseLyric(text) {
      let lines = text.split("\n");         // 将歌词按行分解成数组
      let pattern = /\[\d{2}:\d{2}.(\d{3}|\d{2})]/g;     // 时间格式的正则表达式
      let result = [];
      // 对于歌词格式不对的直接返回
      if (!(/\[.+]/.test(text))) {
        return [[0, text]];
      }
      // 去掉前面不正确的行
      while (lines.length !== 0 && !pattern.test(lines[0])) {
        lines = lines.slice(1);
      }
      // 遍历每一行，形成一个带着两个元素的数组，第一个元素是以秒为计算单位的时间，第二个元素是歌词
      for (let item of lines) {
        let time = item.match(pattern);     // 前面的时间段
        let value = item.replace(pattern, '')    // 存后面的歌词
        for (let item1 of time) {
          let t = item1.slice(1, -1).split(":");   // 取出时间，换算成数组
          if (value !== '') {
            result.push([parseInt(t[0], 10) * 60 + parseInt(t[1]), value]);
          }
        }
      }
      // 按照第一个元素 -- 时间 -- 排序
      result.sort(function (a, b) {
        return a[0] - b[0];
      });
      return result;
    },
    getBirth(value) {
      if (value == null)
        return '不明';
      return value.slice(0, 10);
    },

  }
}

