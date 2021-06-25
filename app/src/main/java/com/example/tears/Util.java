package com.example.tears;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import java.io.*;
import java.util.*;

public class Util {

    /**
     * 重命名音乐文件
     * @param file
     * @param newPath
     * @return
     */
    public static String formatName(File file, String newPath) {
        String oldFileName = file.getName();
        String newDir = file.getPath().replace("qqmusic/song/" + oldFileName, "/Android/data/com.example.tears/files");
        oldFileName = newDir + "/" + oldFileName;
        if (oldFileName.endsWith(".qmcflac")) {
            return oldFileName.replace(".qmcflac", ".mp3");
        } else if (oldFileName.endsWith(".qmc3")) {
            return oldFileName.replace(".qmc3", ".mp3");
        } else if (oldFileName.endsWith(".qmc0")) {
            return oldFileName.replace(".qmc0", ".mp3");
        } else if (oldFileName.endsWith(".mflac")) {
            return oldFileName.replace(".mflac", ".mp3");
        }
        return null;
    }

    /**
     * 转换单个音乐文件
     * @param file
     * @param newPath
     */
    public void doTransform (File file, String newPath) {
        FileInputStream fileInputStream = null;//文件字节输入流
        FileOutputStream fileOutputStream = null;//文件字节输出流

        try {
            String newFileName = formatName(file, newPath);
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[fileInputStream.available()];//available()方法返回与之关联的文件的字节数
            fileInputStream.read(buffer);
            QMFDecode de = new QMFDecode();
            for (int i = 0; i < buffer.length; ++i) {
                buffer[i] = (byte) (de.NextMask() ^ buffer[i]);
            }
            fileOutputStream = new FileOutputStream(newFileName);
            fileOutputStream.write(buffer);
            fileOutputStream.flush();//清空文件字节输出流
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 读取手机存储中的全部歌曲文件
     * @param context
     * @return
     */
    public LinkedHashMap<Integer, Music> getAllMusic(Context context) {
        LinkedHashMap<Integer, Music> linkedHashMap = new LinkedHashMap<>();
        String selection = MediaStore.Video.Media.DATA + " like ? ";
        String selectionArgs[] = {"/storage/emulated/0/%"};
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        /*cursor.moveToFirst();*/

        if (cursor != null) {
            while (cursor.moveToNext()) {
                if (!cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)).equals("<unknown>")) {
                    Music music = new Music();
                    Log.i("2022", String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)).equals("<unknown>")));
                    music.setId(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)));
                    music.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)));
                    /*music.setPicture();*/
                    music.setSize(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)));
                    music.setName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)));
                    music.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                    music.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)));
                    Log.i("2001", music.toString());//从存储中读取的音频文件信息
                    linkedHashMap.put(music.getId(), music);
                }
            }
        }
        cursor.close();
        return linkedHashMap;
    }

    /**
     * 将读取的LinkedHashMap转换为List
     * @param linkedHashMap
     * @return
     */
    public List<Map<String,Object>> getListFromLinkedHashMap(LinkedHashMap linkedHashMap) {
        List<Map<String,Object>> list = new ArrayList<>();
        Iterator iterator = linkedHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map<String, Object> item = new HashMap<>();
            Map.Entry entry = (Map.Entry) iterator.next();
            Music music = (Music) entry.getValue();
            Log.i("2003", music.toString());//将在ListView中显示的条目信息
            item.put("id", music.getId());
            item.put("name", music.getName());
            item.put("artist", music.getArtist());
            list.add(item);
        }
        return list;
    }

    /**
     * 在文件目录中按类型进行文件查找
     * @param path
     * @param fileType
     * @return
     */
    public List<Map<String,Object>> getAllFiles(String path, String fileType) {
        List<Map<String,Object>> list = new ArrayList<>();
        int id = 0;

        File file = new File(path);
        if (!file.exists()) {//判断路径是否存在
            return list;
        }
        File[] files = file.listFiles();
        if (files == null) {//判断权限
            return list;
        }
        for (File f : files) {//遍历目录
            if (f.isFile() && f.getName().endsWith(fileType)) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", id++);
                item.put("name", getMusicInfo(f.getAbsolutePath()));
                item.put("artist", "");
                item.put("path", f.getAbsolutePath());
                Log.i("2014", item.toString());
                list.add(item);
            } else if (f.isDirectory()) {//查询子目录
                getAllFiles(f.getAbsolutePath(), fileType);
            }
        }
        return list;
    }

    public String getMusicInfo(String path) {
        String info = path.substring(57, path.indexOf("[")).trim();
        return info;
    }
}
