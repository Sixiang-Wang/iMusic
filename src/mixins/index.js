import {likeSongOfName} from "../api";

export const mixin = {
  methods: {
    // 获取图片地址
    attachImageUrl (srcurl){
      if(srcurl){
        return this.$store.state.configure.HOST + srcurl;
      }
      else{
        return '../assets/img/user.jpg';
      }
    },
    // 根据歌手名字模糊查询歌曲
    getSong(){
      if(!this.$route.query.keywords){
        this.notify('您输入的内容为空','warning');
      }
      else{
        likeSongOfName(this.$route.query.keywords).then(res => {
          if((!res.length)){
            this.$store.commit('setListOfSongs',[]);
            this.notify('系统暂无符合条件的歌曲','warning');
          }
          else{
            this.$store.commit('setListOfSongs',res);
          }
        }).catch(err => {
          console.log(err)
        })
      }
    }
  }
}
