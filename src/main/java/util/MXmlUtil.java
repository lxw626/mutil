package util;

import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.List;

public class MXmlUtil {
    //创建xml文件，空文件
    public static boolean createXML(String filename) throws Exception {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("html");
        //添加根元素下的子元素及其属性,内容
        Element head = root.addElement("head");
        Element body = root.addElement("body");
        body.addElement("h1").addText("xmltest");
        body.addElement("div").addAttribute("id", "div1").addAttribute("name", "div1Name");
        body.addElement("div").addAttribute("id", "div2");
        XMLWriter output = new XMLWriter(new FileWriter(new File(filename)));
        output.write(document);
        output.close();
        return true;
    }

    //解析XML
    //SAXReader可以通过多种方式读取xml数据，并返回Document格式的对象。
    //通过查看源码，可以看出read()方法接收File，InputStream和URL等格式的参数来读取相应的xml数据。
    public static Document parsingXML(String path) throws DocumentException {
        //创建解析器
        SAXReader reader = new SAXReader();//创建读取文件内容对象
        Document document = reader.read(path);//指定文件并读取
        Element root = document.getRootElement();
        System.out.println("rootNote:" + root.getName());
        // root节点下面子节点迭代器
        Iterator<Element> it = root.elementIterator();
        // 遍历
        while (it.hasNext()) {
            Element e = it.next();
            System.out.print("节点名：" + e.getName() + " ");
            /*if (!(e.getTextTrim().equals(""))) {
                System.out.print(",文本内容:" + root.getText());
            }*/
            List<Attribute> attributes = e.attributes();
            // 遍历属性节点
            for (Attribute attr : attributes) {
                System.out.print(attr.getName() + ":" + attr.getText());
            }
            System.out.println();
        }
        return document;
    }

    //treeUrl位xml节点，例如/root/dict/*
    public static void xmlElementToHashmap_url(Document document, String treeUrl) {
        List<Element> list = document.selectNodes(treeUrl);
        for (Element element : list) {
            System.out.println(element.getName());
            List<Attribute> attributes = element.attributes();
            for (Attribute attribute : attributes) {
                System.out.println(attribute.getName()+","+attribute.getText());
            }
        }
    }
    public static void updateAttribute(Document document, String treeUrl) {
        Element element = (Element)document.selectSingleNode(treeUrl);
        element.addAttribute("value","hhh");
        List<Attribute> attributes = element.attributes();
        for (Attribute attribute : attributes) {
            System.out.println(attribute.getName()+","+attribute.getText());
        }
    }

}
