package util.vue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import util.ioUtil.MIOUtil;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * .vue模板解析工具类
 * @author lixiewen
 */
public class MVueUtil {
    private static String vuePath = "src/main/resources/vue/";
    private static String mainJsPath = "src/main/resources/vue/main.js";
    private static String indexHtmlPath = "src/main/resources/static/index.html";
    private static String indexHtmlMbPath = "src/main/resources/vue/indexmb.html";
    private static String appId;
    private static String tab = "    ";
    private static Map<String,MVueComponent> vueComponentMap = new HashMap<>();
    private static List<MVueComponent> vueComponents = new ArrayList<>();
    private static List<String> mainJs = new ArrayList<>();

    // 扫描vue下的所有.vue文件解析为VueComponent
    static {
        getMainJs();
        List<File> files = new ArrayList<>();
        getFiles(new File(vuePath), files);
        for (File file : files) {
            if(file.getName().endsWith(".vue")){
                updateVueComponent(file);
            }
        }
        vueComponentSort();
    }

    public static void main(String[] args) {

    }


    /**
     * 获取指定数量的tab(1tab=4个空格)
     * @param num tab的数量
     * @return 指定数量的tab
     */
    private static String getTab(int num){
        String s = "";
        if(num>1){
            for(int i=0;i<num;i++){
                s+=tab;
            }
        }else{
            s = tab;
        }
        return s;
    }

    /**
     * 如果不存在,则新增,如果已经存在,则更新
     * @param file file
     */
    private static void updateVueComponent(File file) {
        MVueComponent vueComponent = new MVueComponent();
        String id = file.getName().replaceAll(".vue", "");
        String fullName = file.getAbsolutePath();
        Document doc;
        try {
            doc = Jsoup.parse(file, "utf-8");
            // 解析template部分
            Element template = doc.select("template").first();
            Element templatRoot = template.children().first();
            // 若果是App.vue则显示template的根元素(id默认绑定给根元素),如果不是App.vue,则显示template元素(id绑定给template)
            if ("App".equals(id)){
                templatRoot.attr("id",appId);
            }else{
                template.attr("id",id);
            }
            // 解析script部分
            Element script = doc.select("script").first();
            String scriptHtml = script.html();
            String[] split = scriptHtml.split("export\\s*default");
            String scriptOther = split[0].trim();
            if(scriptOther.length()>0){
                String[] imports = scriptOther.split("import");
                for (String anImport : imports) {
                    if(anImport.contains("from")){
                        String importComfullName = anImport.split("from")[1].trim();
                        importComfullName = pathConverter(fullName, importComfullName);
                        vueComponent.addBefores(importComfullName);
                    }
                }

            }
            // 解析style部分
            Element style = doc.select("style").first();
            vueComponent.setName(id);
            vueComponent.setFullName(fullName);
            vueComponent.setTemplate(template);
            vueComponent.setScript(script);
            vueComponent.setStyle(style);
            // 如果vueComponentMap里面存在oldVueComponent,则更新vueComponents里面对应的组件,否则添加一个新组件
            MVueComponent oldVueComponent = vueComponentMap.get(vueComponent.getFullName());
            if(oldVueComponent == null){
                vueComponents.add(vueComponent);
            }else{
                vueComponents.set(vueComponents.indexOf(oldVueComponent),vueComponent);
            }
            vueComponentMap.put(fullName,vueComponent);
            vueComponentSort();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 移除组件
     * 目标:局部更新改动部分而不是每次更新全部
     * 因idea修改文件后不编译检测不到文件变化,需要编译=>生成新的html=>在编译一次新html才会生效,所以暂不使用,
     * 也可以自己起一个定时任务监视文件的变动(例如求文件的md5值),类似于热部署,频繁定时扫描文件系统对性能的影响待测试
     * @param fullName 组件完整名
     */
    public static  void removeVueComponent(String fullName){
        vueComponentMap.remove(fullName);
    }

    /**
     * 获取main.js文件的内容,即new Vue({})对象的代码模板
     */
    private static void getMainJs() {
        File file = new File(mainJsPath);
        List<String> lines;
        try {
            lines = MIOUtil.readFileByLine(file);
            mainJs = lines;
            for (String line : lines) {
                if(line.matches("\\s*el:\\s*('|\")#\\w+('|\")\\s*,\\s*")){
                    int end;
                    if(line.contains("'")){
                        end = line.lastIndexOf("'");
                    }else {
                        end = line.lastIndexOf("\"");
                    }
                    appId = line.substring(line.indexOf("#")+1, end);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static void indexGenerator() throws IOException {
        File indexMb = new File(indexHtmlMbPath);
        Document indexDoc = Jsoup.parse(indexMb, "utf-8");
        Element body = indexDoc.body();
        List<String> objs = new ArrayList<>();
        String obj;
        String appContext = "";
        for (MVueComponent vueComponent : vueComponents) {
            Element template = vueComponent.getTemplate();
            String scriptText = vueComponent.getScript().html();
            String[] export_defaults = scriptText.split("export default");
            String name = vueComponent.getName();
            obj = export_defaults[1];
            if("App".equals(name)){
                appContext = obj.substring(obj.indexOf("{")+1,obj.lastIndexOf("}"));
                body.append(template.html());
            }else {
                body.appendChild(template);
                String xxx = "{\n"+getTab(2)+"template:'#" + name + "',";
                obj = export_defaults[1].replaceFirst("\\{",xxx);
                obj = tab+"let " +name + " = " + obj;
                objs.add(obj);
            }
        }
        File file = new File(indexHtmlPath);
        PrintWriter coverPw = MIOUtil.getPrintWriter(file);
        PrintWriter appendPw = MIOUtil.getPrintWriter(file, true);
        coverPw.write(indexDoc.toString());
        coverPw.flush();
        coverPw.close();
        appendPw.write("\n<script>\n");
        for (String s : objs) {
            appendPw.write(s+"\n");
        }
        for (String mainJ : mainJs) {
            if(mainJ.matches("\\s*el:\\s*('|\")#\\w+('|\")\\s*,\\s*")){
                mainJ = mainJ.replaceAll(mainJ,mainJ+appContext);
            }
            appendPw.write("    "+mainJ+"\n");
        }
        appendPw.write("</script>");
        appendPw.flush();
        appendPw.close();
    }

    /**
     * 根据引用关系给组件排序
     */
    private static void vueComponentSort(){
        for (int i=0;i<vueComponents.size();i++) {
            MVueComponent vueComponent = vueComponents.get(i);
            List<String> befores = vueComponent.getBefores();
            if(befores.size()>0){
                for (String before : befores) {
                    int callVueComponentIndex = vueComponents.indexOf(vueComponentMap.get(before));
                    if(i<callVueComponentIndex){
                        Collections.swap(vueComponents,i,callVueComponentIndex);
                    }

                }
            }
        }

    }

    /**
     * windows输出的是\
     * @param fullName 正在解析的.vue文件的名字
     * @param relative .vue引用的相对路径
     */
    private static String pathConverter(String fullName,String relative){
        // 前路径结尾不带\
        String currentPath = fullName.substring(0,fullName.lastIndexOf("\\"));
        relative = relative.replaceAll("'","");
        // 将/转为\
        if(currentPath.contains("\\")){
            relative = relative.replace("/","\\");
        }
        if(relative.contains("..")){
            int count = relative.split("\\.\\.").length - 1;
            for(int i=0;i<count;i++){
                currentPath = currentPath.substring(0,currentPath.lastIndexOf("\\"));
            }
            relative = relative.substring(relative.lastIndexOf("..")+2);
            return currentPath + relative;
        }
        if(!relative.contains("..")&&relative.contains(".\\")){
            return relative.replace(".\\",currentPath+"\\");
        }
        return relative;

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


}
