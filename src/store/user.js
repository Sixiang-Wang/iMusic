const user = {
  state: {
      userId : '',       // 用户id
      username : '',     // 用户名
      avatar : '',       // 头像

      collectList: [],
      control: "#icon-xunhuanbofang",
      flag: 0,
      recentSongList: [],

  },
  getters: {
    userId: state => {
      let userId = state.userId
      if(!userId){
        userId = JSON.parse(window.sessionStorage.getItem('userId'));
      }
      return userId;
    },
    username: state => {
      let username = state.username
      if(!username){
        username = JSON.parse(window.sessionStorage.getItem('username'));
      }
      return username
    },
    avatar: state => {
      let avatar = state.avatar
      //console.log(window.sessionStorage.getItem('avatar'));
      if(!avatar){
        if(window.sessionStorage.getItem('avatar') !== "undefined")
          avatar = JSON.parse(window.sessionStorage.getItem('avatar'))
      }
      return avatar
    },

    recentSongList: (state) => {
      state.recentSongList = JSON.parse(
        window.sessionStorage.getItem("recentSongList")
      );
      return state.recentSongList;
    },
    collectList: (state) => {
      state.collectList = JSON.parse(
        window.sessionStorage.getItem("collectList")
      );
      return state.collectList;
    },
    control: (state) => {
      state.control = JSON.parse(window.sessionStorage.getItem("control"));
      return state.control;
    },
    flag: (state) => {
      state.flag = JSON.parse(window.sessionStorage.getItem("flag"));
      return state.flag;
    },


  },
  mutations: {
    setUserId: (state,userId) => {
      state.userId = userId
      window.sessionStorage.setItem('userId',JSON.stringify(userId))
    },
    setUsername: (state,username) => {
      state.username = username
      window.sessionStorage.setItem('username',JSON.stringify(username))
    },
    setAvatar: (state,avatar) => {
      state.avatar = avatar
      window.sessionStorage.setItem('avatar',JSON.stringify(avatar))
    },


    setRecentSongList: (state, recentSongList) => {
      state.recentSongList = recentSongList;
      window.sessionStorage.setItem(
        "recentSongList",
        JSON.stringify(recentSongList)
      );
    },
    setCollectList: (state, collectList) => {
      state.collectList = collectList;
      window.sessionStorage.setItem("collectList", JSON.stringify(collectList));
    },
    setControl: (state, control) => {
      state.control = control;
      window.sessionStorage.setItem("control", JSON.stringify(control));
    },
    setFlag: (state, flag) => {
      state.flag = flag;
      window.sessionStorage.setItem("flag", JSON.stringify(flag));
    },


  }

}

export default user
