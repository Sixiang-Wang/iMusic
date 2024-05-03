const configure = {
  state: {
    HOST: 'http://127.0.0.1.8888',//后台访问地址根目录
    activeName:  ''   //当前选中的菜单名
    // isPlay: false,//是否播放中
    // url: '',//歌曲地址
    // id: ''//歌曲id
  },
  getters: {
    activeName: state => {
      let activeName = state.activeName
      if(!activeName){
        activeName = JSON.parse(window.sessionStorage.getItem('activeName'));
      }
      return activeName
    }
  },
  mutations: {
    setActiveName: (state,activeName) => {
      state.activeName = activeName
      window.sessionStorage.setItem('activeName',JSON.stringify(activeName))

    }
  }
}

export default configure
