import {likeSongOfName} from "../api";

const song = {
  state: {
    listOfSongs: [ ]  //当前歌曲列表

  },
  getters: {
    listOfSongs: state =>{
      let listOfSongs = state.listOfSongs
      if(!listOfSongs.length){
        listOfSongs = JSON.parse(window.sessionStorage.getItem('listOfSongs')||null)
      }
      return listOfSongs
    }

  },
  mutations: {
    setListOfSongs: (state, listOfSongs) => {
      state.listOfSongs = listOfSongs;
      window.sessionStorage.setItem('listOfSongs',JSON.stringify(listOfSongs));
    }
  }
}

export default song
