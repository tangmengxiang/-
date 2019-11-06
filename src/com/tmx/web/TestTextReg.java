package com.tmx.web;

import com.tmx.dao.FilmDao;
import com.tmx.domain.Film;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* 爬出电影下载链接
* */
public class TestTextReg {
    public static void main(String[] args) {
        Film film = new Film();
        FilmDao filmDao = new FilmDao();
        String filmDetailFilePath = "src/filmDetailPage.html";  //电影详情页面存放地址
        String filePagePath = "src/filePageList.html";      //电影列表页面存放地址
        String URL = "https://www.ygdy8.net/html/gndy/dyzz/list_23_1.html";
        int i = 0;//计数器
        while(URL != null){
            File file = new File(filmDetailFilePath);
            File file1 = new File(filePagePath);
            if (file != null){
                file.delete();
            }
            if (file1 != null){
                file1.delete();
            }
            //向网站发送请求得到网页代码
            getFilmPage(URL,filePagePath);
            //取得下一页链接
            String filmNextURL = getFilmNextURL(URL, filePagePath);
            //读取列表文本文件
            String pathname = filePagePath;
            List<String> filmPageList = readFilmPage(pathname);
            System.out.println(filmPageList);
            //访问filmPageList中的页面，并写入文本
            for (String s : filmPageList) {
                getFilmPage(s,filmDetailFilePath);
            }
            //得到所有的电影标题和下载链接
            Map<String, String> filmDownloadURL = getFilmDownloadURL(filmDetailFilePath);
            //打印出数据，后期可考虑存入数据库
            for (Map.Entry<String,String>entry : filmDownloadURL.entrySet()) {
                System.out.println(entry.getKey() + ":  " + entry.getValue());
                film.setFilm_name(entry.getKey());
                film.setFilm_downLoadURL(entry.getValue());
                filmDao.addFilm(film);
            }
            //开始下一次
            URL = filmNextURL;
            i++;
            if(i > 30)//30页开始下载链接改变
                break;
        }


    }


    public static void loopGetFilmDownloadURL(){

    }

    /**
     *
     * @param filePath 将要读取的文本地址
     * @return 将返回一个电影列表页面
     */
    public static List<String> readFilmPage(String filePath) {
        List<String> filePageList = new ArrayList<>();
        String prefix = "https://www.ygdy8.net";
        String filmUrl = null;
        String pathname = filePath;
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                //这是读取电影列表的
                if (line.indexOf("ulink") != -1){
                    if (line.indexOf("<a href=") != -1){
                        String filmPage = line.substring(line.indexOf("<a href=") + 9,line.indexOf("ulink") - 9);
                        filmUrl = prefix + filmPage;//拼接网站前缀
                        //把所有电影的详情页url存入list集合中
                        filePageList.add(filmUrl);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePageList;
    }

//    @Test
//    public void test2(){
//        Map<String, String> filmDownloadURL = getFilmDownloadURL("src/filmDetailPage.html");
//        for (Map.Entry<String, String> entry : filmDownloadURL.entrySet()) {
//            System.out.println(entry.getKey());
//            System.out.println(entry.getValue());
//        }
//    }
    /**
     *
     * @param filePath 将要读取的文本地址
     * @return 带有电影标题和下载地址的集合
     */
    public static Map<String,String> getFilmDownloadURL(String filePath) {
        Map<String,String> filmDownloadURLMap = new HashMap<String,String>();
        String pathname = filePath;
        String name = null;
        String downloadUrl = null;
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                if (line.indexOf("magnet") != -1){
                    try {
                         name = line.substring(line.indexOf("◎片　　名")+6,line.indexOf("<br />◎年"));
                         downloadUrl = line.substring(line.indexOf("magnet"),line.indexOf("\"><strong><font"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        //将电影名和下载链接存到map集合中
                        filmDownloadURLMap.put(name,downloadUrl);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filmDownloadURLMap;
    }
//    @Test
//    public void test(){
//        getFilmNextURL("src/filePageList.html");
//    }

    /**
     * 读取下一页的内容
     * @param filePath
     * @return
     */
    public static String getFilmNextURL(String URL,String  filePath) {
        String nextPage = null;
        String pathname = filePath;
        String substring = URL.substring(0, URL.indexOf("list"));
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            String line;
            while ((line = br.readLine()) != null) {
//                // 一次读入一行数据
                if (line.indexOf("list") != -1){
                    if (line.indexOf("下一页") != -1){
                        String nextUri = line.substring(line.indexOf("href=") + 6,line.indexOf("下一页")-2);
                        nextPage = substring + nextUri;
                        System.out.println("下一页链接" + nextPage);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nextPage;
    }
    //，获取电影下载详情页面存入集合中

    /**
     * 向网站发出请求并存取网站代码
     * @param Url 要请求的网址
     * @param filePath 网页源代码存放位置
     */
    public static void  getFilmPage(String Url,String filePath){

        //1，向网站发出请求并获得网站代码"https://www.ygdy8.net/html/gndy/china/index.html"
        try
        {
            URL url = new URL(Url);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection connection = null;
            if(urlConnection instanceof HttpURLConnection)
            {
                connection = (HttpURLConnection) urlConnection;
            }
            else
            {
                System.out.println("请输入 URL 地址");
                return;
            }
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(),"gb2312"));//加入字符集编码

            StringBuffer context = new StringBuffer();
            String current;
            while((current = in.readLine()) != null)
            {
                context.append(current);
                context.append("\r\n");
            }
            WriterTxt(context.toString(),filePath,true);
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 将网站代码写入本地
     * @param context 需要写入的内容
     * @param filePath 需要存的地址
     * @param isAppend 是否追加
     */
    public static void WriterTxt(String context,String filePath,boolean isAppend){
        try(
                FileOutputStream outputStream = new FileOutputStream(filePath, isAppend);
                PrintWriter writer = new PrintWriter(outputStream);
                ) {
            writer.print(context);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
