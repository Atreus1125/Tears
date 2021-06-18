package com.example.tears;

import java.io.*;

public class Util {

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
        }
        return null;
    }

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
}
