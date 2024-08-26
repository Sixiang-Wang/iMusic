package com.java.imusic.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.imusic.config.PathConfig;
import com.java.imusic.dao.*;
import com.java.imusic.domain.Message;
import com.java.imusic.domain.SongList;
import com.java.imusic.service.*;
import com.java.imusic.utils.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 歌单控制类
 */
@RestController
@RequestMapping("/songList")
public class SongListController {

    @Autowired
    SongListService songListService;
    @Autowired
    private RankMapper rankMapper;
    @Autowired
    private SongListMapper songListMapper;
    @Autowired
    private MessageService messageService;
    @Autowired
    private CollectMapper collectMapper;
    @Autowired
    private ListSongMapper listSongMapper;
    @Autowired
    private ComplaintMapper complaintMapper;
    @Autowired
    private CommentService commentService;
    /**
     * 添加歌单
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Object addSongList(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id");
        String title = request.getParameter("title").trim();      //标题
        String pic = "/img/songListPic/default.jpg";
        String introduction = request.getParameter("introduction").trim();//简介
        String style = request.getParameter("style").trim();    //风格
        String userIdS = request.getParameter("userId");
        int userId;
        if (userIdS==null || userIdS.isEmpty()){
            userId = -1;
        } else{
            userId = Integer.parseInt(userIdS);
        }
        //保存到歌单的对象中
        SongList songList = new SongList();
        songList.setTitle(title);
        songList.setPic(pic);
        songList.setIntroduction(introduction);
        songList.setStyle(style);
        songList.setUserId(userId);

        if (id!=null&&!id.isEmpty()){
            songList.setId(Integer.parseInt(id));
        }

        boolean flag = songListService.insert(songList);
        if(flag){   //保存成功
            jsonObject.put(Consts.CODE,1);
            jsonObject.put(Consts.MSG,"添加成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE,0);
        jsonObject.put(Consts.MSG,"添加失败");
        return jsonObject;
    }

    /**
     * 修改歌单
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public Object updateSongList(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id").trim();          //主键
        String title = request.getParameter("title").trim();      //标题
        String introduction = request.getParameter("introduction").trim();//简介
        String style = request.getParameter("style").trim();    //风格
        //保存到歌单的对象中
        SongList songList = new SongList();
        songList.setId(Integer.parseInt(id));
        songList.setTitle(title);
        songList.setIntroduction(introduction);
        songList.setStyle(style);
        boolean flag = songListService.update(songList);
        if(flag){   //保存成功
            jsonObject.put(Consts.CODE,1);
            jsonObject.put(Consts.MSG,"修改成功");
            return jsonObject;
        }
        jsonObject.put(Consts.CODE,0);
        jsonObject.put(Consts.MSG,"修改失败");
        return jsonObject;
    }


    /**
     * 删除歌单
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Object deleteSongList(HttpServletRequest request){
        Integer id = Integer.parseInt(request.getParameter("id").trim());
        return songListService.delete(id);
    }

    /**
     * 根据主键查询整个对象
     */
    @RequestMapping(value = "/selectByPrimaryKey",method = RequestMethod.GET)
    public Object selectByPrimaryKey(HttpServletRequest request){
        String id = request.getParameter("id").trim();          //主键
        return songListService.selectByPrimaryKey(Integer.parseInt(id));
    }

    /**
     * 根据userId查询整个对象
     */
    @RequestMapping(value = "/selectByUserId",method = RequestMethod.GET)
    public Object selectByUserId(HttpServletRequest request){
        String userId = request.getParameter("userId").trim();          //userId
        return songListService.selectByUserId(Integer.parseInt(userId));
    }


    /**
     * 查询所有歌单
     */
    @RequestMapping(value = "/allSongList",method = RequestMethod.GET)
    public Object allSongList(HttpServletRequest request){
        return songListService.allSongList();
    }

    /**
     * 根据标题精确查询歌单列表
     */
    @RequestMapping(value = "/songListOfTitle",method = RequestMethod.GET)
    public Object songListOfTitle(HttpServletRequest request){
        String title = request.getParameter("title").trim();          //歌单标题
        return songListService.songListOfTitle(title);
    }

    /**
     * 根据标题模糊查询歌单列表
     */
    @RequestMapping(value = "/likeTitle",method = RequestMethod.GET)
    public Object likeTitle(HttpServletRequest request){
        String title = request.getParameter("title").trim();          //歌单标题
        return songListService.likeTitle("%"+title+"%");
    }

    /**
     * 根据风格模糊查询歌单列表
     */
    @RequestMapping(value = "/likeStyle",method = RequestMethod.GET)
    public Object likeStyle(HttpServletRequest request){
        String style = request.getParameter("style").trim();          //歌单风格
        if ("其他".equals(style)|| "其它".equals(style)){
            return songListMapper.likeOtherStyle();
        }
        return songListService.likeStyle("%"+style+"%");
    }

    /**
     * 更新歌单图片
     */
    @RequestMapping(value = "/updateSongListPic",method = RequestMethod.POST)
    public Object updateSongListPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id")int id){
        SongList songList = songListService.selectByPrimaryKey(id);
        String oldPic = songList.getPic();
        JSONObject jsonObject = new JSONObject();
        if(avatorFile.isEmpty()){
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"文件上传失败");
            return jsonObject;
        }
        //文件名=当前时间到毫秒+原来的文件名
        String fileName = System.currentTimeMillis()+avatorFile.getOriginalFilename();
        //文件路径
        String filePath = PathConfig.path +System.getProperty("file.separator")+"img"
                +System.getProperty("file.separator")+"songListPic";
        //如果文件路径不存在，新增该路径
        File file1 = new File(filePath);
        if(!file1.exists()){
            file1.mkdir();
        }
        //实际的文件地址
        File dest = new File(filePath+System.getProperty("file.separator")+fileName);
        //存储到数据库里的相对文件地址
        String storeAvatorPath = "/img/songListPic/"+fileName;
        try {
            avatorFile.transferTo(dest);
            songList.setId(id);
            songList.setPic(storeAvatorPath);
            boolean flag = songListService.update(songList);
            if(!flag){
                jsonObject.put(Consts.CODE,0);
                jsonObject.put(Consts.MSG,"上传失败");
                return jsonObject;
            }
            jsonObject.put(Consts.CODE,1);
            jsonObject.put(Consts.MSG,"上传成功");
            jsonObject.put("pic",storeAvatorPath);

            File oldPicFile = new File(PathConfig.path +System.getProperty("file.separator")+oldPic);
            if(!oldPic.equals("/img/songListPic/default.jpg")){
                if(!oldPicFile.delete()) {
                    System.out.println("删除歌单旧图片失败-deleteSongList");
                }
            }
            return jsonObject;
        } catch (IOException e) {
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"上传失败"+e.getMessage());
        }finally {
            return jsonObject;
        }
    }

    /**
     * 某用户被评分最高的歌单
     * @param request
     * @return
     */
    @RequestMapping(value = "/bestSongListOfUser",method = RequestMethod.GET)
    public Object bestSongListOfUser(HttpServletRequest request){
        String userId = request.getParameter("userId");
        SongList songList =  rankMapper.bestSongListOfUser(Integer.parseInt(userId));
        if(songList == null){
            List<SongList> songListList = songListService.selectByUserId(Integer.parseInt(userId));
            if(songListList==null || songListList.isEmpty()) {
                return null;
            }
            System.out.println("哦，可怜的孩子，根本就没有人给他的歌曲评分！算了给你随便返回个他的歌单好了");
            return songListList.toArray()[0];
        }
        return songList;
    }


    @RequestMapping(value = "/invisible",method = RequestMethod.GET)
    public Object invisibleSongList(HttpServletRequest request){
        String id = request.getParameter("id").trim();          //主键
        SongList songList = songListService.selectByPrimaryKey(Integer.parseInt(id));
        songList.setVisible(0);
        boolean flag = songListService.update(songList);
        if(flag){
            Message message = new Message();
            message.setTo(songList.getUserId());
            message.setFrom(-1);
            message.setText("您的歌单《"+songList.getTitle()+"》已被下架");
            message.setIsRead(0);
            message.setType(0);
            messageService.insert(message);
        }
        return flag;
    }

    @RequestMapping(value = "/allInvisible",method = RequestMethod.GET)
    public Object allInvisibleSongList(HttpServletRequest request){
        return songListMapper.allInvisible();
    }

    @RequestMapping(value = "/visible",method = RequestMethod.GET)
    public Object visibleSongList(HttpServletRequest request){
        String id = request.getParameter("id").trim();          //主键
        SongList songList = songListService.selectByPrimaryKey(Integer.parseInt(id));
        songList.setVisible(1);
        boolean flag = songListService.update(songList);
        if(flag){
            Message message = new Message();
            message.setTo(songList.getUserId());
            message.setFrom(-1);
            message.setText("您的歌单《"+songList.getTitle()+"》已被恢复");
            message.setIsRead(0);
            message.setType(0);
            messageService.insert(message);
        }
        return flag;
    }

}
