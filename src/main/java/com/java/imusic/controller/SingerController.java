package com.java.imusic.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.imusic.domain.Singer;
import com.java.imusic.domain.Song;
import com.java.imusic.domain.User;
import com.java.imusic.service.SingerService;
import com.java.imusic.service.SongService;
import com.java.imusic.service.UserService;
import com.java.imusic.service.impl.SongServiceImpl;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 歌手控制类
 */
@RestController
@RequestMapping("/singer")
public class SingerController {

    @Autowired
    private SingerService singerService;
    @Autowired
    private UserService userService;
    @Autowired
    private SongService songService;
    /**
     * 添加歌手
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public Object addSinger(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        String name = request.getParameter("name").trim();      //姓名
        String sex = request.getParameter("sex").trim();        //性别
        String pic = request.getParameter("pic").trim();        //头像
        String birth = request.getParameter("birth").trim();    //生日
        String location = request.getParameter("location").trim();//地区
        String introduction = request.getParameter("introduction").trim();//简介
        //把生日转换成Date格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = new Date();
        if(!birth.equals("null")){
            try {
                birthDate = dateFormat.parse(birth);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            birthDate=null;
        }

        if (name == null || name.equals("")) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "歌手名字不能为空");
            return jsonObject;
        }

        Singer singer1 = singerService.oneSingerOfName(name);
        if (singer1 != null) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "歌手重名");
            return jsonObject;
        }
        if(sex.isEmpty()){
            sex = "3";
        }
        //保存到歌手的对象中
        Singer singer = new Singer();
        singer.setName(name);
        singer.setSex(new Byte(sex));
        singer.setPic(pic);
        if(singer.getSex()!=2) singer.setBirth(birthDate);
        singer.setLocation(location);
        singer.setIntroduction(introduction);
        boolean flag = singerService.insert(singer);
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
     * 修改歌手
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public Object updateSinger(HttpServletRequest request){
        JSONObject jsonObject = new JSONObject();
        String id = request.getParameter("id").trim();          //主键
        String name = request.getParameter("name").trim();      //姓名
        String nameOrigin = request.getParameter("nameOrigin").trim();
        String sex = request.getParameter("sex").trim();        //性别
        String birth = request.getParameter("birth").trim();    //生日
        String location = request.getParameter("location").trim();//地区
        String introduction = request.getParameter("introduction").trim();//简介
        //把生日转换成Date格式
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date birthDate = new Date();
        if(!birth.equals("null")){
            try {
                birthDate = dateFormat.parse(birth);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            birthDate=null;
        }

        if (name == null || name.equals("")) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "歌手名字不能为空");
            return jsonObject;
        }

        Singer singer1 = singerService.oneSingerOfName(name);
        if (singer1 != null && !singer1.getName().equals(nameOrigin)) {
            jsonObject.put(Consts.CODE, 0);
            jsonObject.put(Consts.MSG, "歌手重名");
            return jsonObject;
        }

        if(sex.isEmpty()){
            sex = "3";
        }
        //保存到歌手的对象中
        Singer singer = new Singer();
        singer.setId(Integer.parseInt(id));
        singer.setName(name);
        singer.setSex(new Byte(sex));
        singer.setBirth(birthDate);
        if(singer.getSex()==2)singer.setBirth(null);
        singer.setLocation(location);
        singer.setIntroduction(introduction);
        boolean flag = singerService.update(singer);

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
     * 删除歌手
     */
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public Object deleteSinger(HttpServletRequest request){
        String id = request.getParameter("id").trim();          //主键
        boolean flag = singerService.delete(Integer.parseInt(id));
        List<Song> songs = songService.songOfSingerId(Integer.parseInt(id));
        for (Song song : songs){
            SongController songController = SongController.getSongController();
            String songUrl =  song.getUrl();
            File songFile = new File("./"+songUrl);

            //不想删除歌曲用我
            songService.update(song);

            //想删除歌曲用我

            boolean flag2 = songFile.delete();
            if (!flag2)
                System.out.println(songFile.toString() + ": 没找到或删除失败（来自SingerController-deleteSinger）");
            song.setSingerId(-1);
            songService.delete(song.getId());
        }
        return flag;
    }

    /**
     * 根据主键查询整个对象
     */
    @RequestMapping(value = "/selectByPrimaryKey",method = RequestMethod.GET)
    public Object selectByPrimaryKey(HttpServletRequest request){
        String id = request.getParameter("id").trim();          //主键
        return singerService.selectByPrimaryKey(Integer.parseInt(id));
    }

    /**
     * 查询所有歌手
     */
    @RequestMapping(value = "/allSinger",method = RequestMethod.GET)
    public Object allSinger(HttpServletRequest request){
        return singerService.allSinger();
    }

    /**
     * 根据歌手名字模糊查询列表
     */
    @RequestMapping(value = "/singerOfName",method = RequestMethod.GET)
    public Object singerOfName(HttpServletRequest request){
        String name = request.getParameter("name").trim();          //歌手名字
        return singerService.singerOfName("%"+name+"%");
    }

    @RequestMapping(value = "/oneSingerOfName",method = RequestMethod.GET)
    public Object oneSingerOfName(HttpServletRequest request){
        String name = request.getParameter("name").trim();          //歌手名字
        Singer singer = singerService.oneSingerOfName("%"+name+"%");
        return singer;
    }

    /**
     * 根据性别查询
     */
    @RequestMapping(value = "/singerOfSex",method = RequestMethod.GET)
    public Object singerOfSex(HttpServletRequest request){
        String sex = request.getParameter("sex").trim();          //性别
        return singerService.singerOfSex(Integer.parseInt(sex));
    }

    /**
     * 更新歌手图片
     */
    @RequestMapping(value = "/updateSingerPic",method = RequestMethod.POST)
    public Object updateSingerPic(@RequestParam("file") MultipartFile avatorFile, @RequestParam("id")int id){
        JSONObject jsonObject = new JSONObject();
        if(avatorFile.isEmpty()){
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"文件上传失败");
            return jsonObject;
        }
        //文件名=当前时间到毫秒+原来的文件名
        String fileName = System.currentTimeMillis()+avatorFile.getOriginalFilename();
        //文件路径
        String filePath = System.getProperty("user.dir")+System.getProperty("file.separator")+"img"
                +System.getProperty("file.separator")+"singerPic";
        //如果文件路径不存在，新增该路径
        File file1 = new File(filePath);
        if(!file1.exists()){
            file1.mkdir();
        }
        //实际的文件地址
        File dest = new File(filePath+System.getProperty("file.separator")+fileName);
        //存储到数据库里的相对文件地址
        String storeAvatorPath = "/img/singerPic/"+fileName;
        try {
            avatorFile.transferTo(dest);
            Singer singer = new Singer();
            singer.setId(id);
            singer.setPic(storeAvatorPath);
            boolean flag = singerService.update(singer);
            if(flag){
                jsonObject.put(Consts.CODE,1);
                jsonObject.put(Consts.MSG,"上传成功");
                jsonObject.put("pic",storeAvatorPath);
                return jsonObject;
            }
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"上传失败");
            return jsonObject;
        } catch (IOException e) {
            jsonObject.put(Consts.CODE,0);
            jsonObject.put(Consts.MSG,"上传失败"+e.getMessage());
        }finally {
            return jsonObject;
        }
    }
}






















