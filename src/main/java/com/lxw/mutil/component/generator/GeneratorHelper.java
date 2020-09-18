package com.lxw.mutil.component.generator;

import util.ioUtil.MIOUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author lixiewen
 * @create 2020-01-17 17:29
 */
public class GeneratorHelper {

    public static void readFileByLine() throws IOException {
        File file = new File("src/main/resources/tmp.txt");
        List<String> strings = MIOUtil.readFileByLine(file);
        int tab = 0;
        for (String string : strings) {
            int length1 = string.length();

            int length2 = string.trim().length();
            string = string.trim().replace("\"", "\\\"");
            int length = length1 - length2;
            if(length>0){
                tab = (int) Math.floor(length/4);
            }
            // 没有空格 有空格&&是4的整数 有空格&&不是4的整数
            if(tab==0){
                System.out.println("list.add(" + "\"" + string + "\"" + ");");
            }else if(tab != 0 && length%4 == 0){
                System.out.println(String.format("list.add(%s," + "\"" + string + "\"" + ");",tab));
            }else {
                System.out.println(String.format("list.add(%s," + "\"" + string + "\"" + ");","?"));
            }
            tab = 0;
        }
    }
    public static void importPackages() throws IOException {
        File file = new File("src/main/resources/tmp.txt");
        List<String> strings = MIOUtil.readFileByLine(file);
        for (String string : strings) {
            string = string.trim().replace("\"", "\\\"");
            System.out.println("list.add(" + "\"" + string + "\"" + ");");
        }
    }
}
