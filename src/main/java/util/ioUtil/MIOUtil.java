package util.ioUtil;


import util.MDateUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MIOUtil {
    //获取覆盖输出pw
    public static PrintWriter getPrintWriter(File file) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return pw;
    }
    //获取追加输出pw
    public static PrintWriter getPrintWriter(File file,boolean append) {
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(new FileOutputStream(file,append));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return pw;
    }

    //根据给定的文件路径创建文件
    public static File getFile(String fullName) {
        File file = new File(fullName);
//        如果文件路径不存在就创建路径
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
//        如果文件不存在就创建文件,如果文件存在就删除重新创建新文件
        if (file.exists()) {
            file.delete();
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 将字符串输出到指定的位置
     *
     * @param fullName
     * @param str
     */
    public static void print2File(String fullName, String str) {
        File file = getFile(fullName);
        PrintWriter pw = getPrintWriter(file);
        if(str!=null){
            pw.print(str);
            pw.flush();
        }
        pw.close();
        System.out.println("文件:" + fullName + "生成完毕");
    }

    /**
     * 扫描指定路径下的文件
     *
     * @param dir
     * @param files
     */
    public static List<File> getFiles(File dir, List<File> files) {
        File[] fs = dir.listFiles();
        for (File f : fs) {
            if (f.isFile()) {
                files.add(f);
            } else {
                getFiles(f, files);
            }
        }
        return files;
    }
    public static void findFiles(String absolutePath,MFileInfo fileSelector) throws IOException {
        List<File> result = new ArrayList();
        File f = new File(absolutePath);
        List<File> files = new ArrayList();
        getFiles(f, files);
        for (File file : files) {
            boolean flag = true;
            // 文件名 hello*xx.txt
            String fileName = file.getName();
            // 如果文件名忽略大小写不匹配则不是我们要找的文件
            flag = flag && (!fileName.toLowerCase().contains(fileSelector.fileName.toLowerCase()));
            // 如果有扩展名
            if(fileName.contains(".")){
                String[] split = fileName.split(".");
                String extendName = split[split.length - 1];
                if("bak".equals(extendName)){
                    extendName += split[split.length - 2];
                }
                extendName = "." + extendName;
                // *.txt.bak
                // 如果扩展名不匹配则不是我们要找的文件
                flag = flag && (!extendName.equals(fileSelector.extendName.replaceAll("\\*","")));
            }

            // 文件全路径名
            File absoluteFile = file.getAbsoluteFile();
            // 文件大小 单位kb
            // 1b = 0.0009766kb

            System.out.println(file.length()*0.0009766);
            double fileSize = file.length(); // 默认为字节b

            if(fileSelector.size!=null){

                if(fileSelector.size.toLowerCase().contains("b")){

                }
                if(fileSelector.size.toLowerCase().contains("kb")){
                    fileSize = file.length() * 0.0009766;
                }
                if(fileSelector.size.toLowerCase().contains("m")){
                    fileSize = file.length() * 9.5367e-7;
                }
                if(fileSelector.size.toLowerCase().contains("g")){
                    fileSize = file.length() * 9.3132e-10;
                }
                // 如果文件大小(整数部分)不相等则不是我们想要的(返回false)
                flag = flag && !(Math.floor(fileSize) - Integer.parseInt(fileSelector.size.replaceAll("\\D*","")) != 0);
//
            }
            Path path= Paths.get(file.getAbsoluteFile().toString());
            BasicFileAttributeView basicview= Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS );
            BasicFileAttributes attr = basicview.readAttributes();
            // 获取文件创建时间,由于linux下不能获取文件的创建时间,获取到的是文件访问时间
            String creationTime = MDateUtil.Date2LongtString(new Date(attr.creationTime().toMillis()));
            System.out.println(creationTime);
            // 获取文件最后一次修改时间
            String updateTime = MDateUtil.Date2LongtString(new Date(attr.lastModifiedTime().toMillis()));
            System.out.println(updateTime);
            // 获取文件最后一次访问时间
            String accessTime = MDateUtil.Date2LongtString(new Date(attr.lastAccessTime().toMillis()));
            System.out.println(accessTime);
            if(flag){
                result.add(file);
            }
        }
    }

    /**
     * 逐行读取
     *
     * @param fin
     * @throws IOException
     */
    public static List<String> readFileByLine(File fin) throws IOException {
        List<String> list = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fin));
        String line = null;
        while ((line = br.readLine()) != null) {
            list.add(line);
        }
        br.close();
        return list;
    }
    /**
     * 计算文件的 MD5 值
     * hello.txt,hello.txt.bak,hello - 副本 .txt返回的md5值相同
     * @param file
     * @return
     */
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[8192];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}


