import {likeSongOfName} from "../api";

const song = {
  state: {
    listOfSongs: [ ],  //当前歌曲列表
    isPlay: false,     // 是否播放中
    url: '',           // 歌曲地址
    id: '',             // 歌曲id
    playButtonUrl : '#icon-bofang' // 播放状态的图标
  },
  getters: {
    listOfSongs: state =>{
      let listOfSongs = state.listOfSongs
      if(!listOfSongs.length){
        listOfSongs = JSON.parse(window.sessionStorage.getItem('listOfSongs')||null)
      }
      return listOfSongs;
    },
    isPlay: state => {
      let isPlay = state.isPlay;
      if(!isPlay){
        isPlay = JSON.parse(window.sessionStorage.getItem('isPlay')||null);
      }
      return isPlay;
    },
    url: state => {
      let url = state.url;
      if(!url){
        url = JSON.parse(window.sessionStorage.getItem('url')||null);
      }
      return url;
    },
    id: state => {
      let id = state.id;
      if(!id){
        id = JSON.parse(window.sessionStorage.getItem('id')||null);
      }
      return id;
    },
    playButtonUrl: state => {
      let playButtonUrl = state.playButtonUrl;
      if(!playButtonUrl){
        playButtonUrl = JSON.parse(window.sessionStorage.getItem('playButtonUrl')||null);
      }
      return playButtonUrl;
    },
  },
  mutations: {
    setListOfSongs: (state, listOfSongs) => {
      state.listOfSongs = listOfSongs;
      window.sessionStorage.setItem('listOfSongs',JSON.stringify(listOfSongs));
    },
    setIsPlay: (state, isPlay)=> {
      state.isPlay = isPlay;
      window.sessionStorage.setItem('listOfSongs',JSON.stringify(isPlay));
    },
    setUrl: (state,url) => {
      state.url = url;
      window.sessionStorage.setItem('listOfSongs',JSON.stringify(url));
    },
    setId: (state,id) => {
      state.id = id;
      window.sessionStorage.setItem('listOfSongs',JSON.stringify(id));
    },
    setPlayButtonUrl: (state,playButtonUrl) => {
      state.playButtonUrl = playButtonUrl;
      window.sessionStorage.setItem('listOfSongs',JSON.stringify(playButtonUrl));
    }
  }
}

export default song
