package util;

import java.util.Map;
import java.util.Set;

public class MStringUtil {
    /**
     * 判断字符串不为空白
     * 1.如果字符串为null，返回false
     * 2.如果字符串为空串返回false
     * 3.如果字符串只有空格返回false
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str){
        if(str == null || str.trim().isEmpty()){
            return false;
        }
        return true;
    }
    //首字母转小写
    public static String toLowerCaseFirstOne(String s){
        if(Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }


    //首字母转大写
    public static String toUpperCaseFirstOne(String s){
        if(Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }
    /**
     * y_yyy==>yYyy
     * 下划线命名风格转为驼峰命名风格
     * @param s
     * @return
     */
    public static String _x2X(String s){
    	if(s.contains("_")){
//        找出_x或者_X
    		String _xor_X = "_" + s.charAt(s.indexOf("_") + 1);
//        找出X
    		String X = String.valueOf(s.charAt(s.indexOf("_") + 1)).toUpperCase();
//        转为驼峰命名法
    		return _x2X(s.replaceAll(_xor_X, X));
    	}
    	return s;
    }

    public static boolean isChinese(char c) {

        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS

                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS

                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A

                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION

                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION

                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {

            return true;

        }

        return false;

    }

    /**
     * 将字符串里面的key替换成value
     * @param str
     * @param map
     * @return
     */
    public static String myReplanceAll(String str,Map<String,String> map){
        Set<String> keys = map.keySet();
        for (String key : keys) {
            String value = map.get(key);
            if(value!=null){
                str = str.replaceAll(key,value);
            }else{
                str = str.replaceAll(key,"");
            }
        }
        return str;
    }
    /**
     * 获取指定数量的tab(1tab=4个空格)
     * @param num tab的数量
     * @return 指定数量的tab
     */
    public static String getTab(int num){
        String s = "    ";
        if(num>1){
            for(int i=0;i<num;i++){
                s+=s;
            }
        }
        return s;
    }
}
