package com.magonote.environment;

import com.ictlab.kintone.Helper;
import com.ictlab.log.SystemLogger;
import com.magonote.Main;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 設定ファイルクラス(Singleton)
 */
public class Config {
    private static final Config config = new Config();

    private Document document;

    private XPath xpath;

    private String mode;

    /**
     * configファイル設置フォルダ
     */
    private final String filePath = String.format("/%s/%s/", Main.MODULE_NAME, "config");

    private Map<String, String> officeMap = new HashMap<>();

    private Config() {
    }

    public static Config getInstance() {
        return Config.config;
    }

    /**
     * XMLファイル読込
     */
    public void initialize() {
        try {
            final String path = System.getenv("MAGONOTE_PROGRAM_PATH");
            // configファイル名
            String fileName = "config.xml";
            final String filePath = String.format("%s%s%s", path, this.filePath, fileName);

            FileInputStream is = new FileInputStream(Paths.get(filePath).toFile());
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.document = builder.parse(is);
            this.xpath = XPathFactory.newInstance().newXPath();
            this.mode = this.getExecuteMode();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            SystemLogger.getInstance().write("Config:initialize:" + e.getMessage());
        }
    }

    /**
     * kintone接続情報取得
     *
     * @return kintone接続情報
     */
    public Map<String, String> getAuth() {
        Map<String, String> map = new HashMap<>();

        final String xpathStr = String.format("//%s/kintone/auth", this.mode);

        try {
            Node node = (Node) this.xpath.evaluate(xpathStr, this.document, XPathConstants.NODE);

            NamedNodeMap nodeMap = node.getAttributes();

            for (int i = 0; i < nodeMap.getLength(); i++) {
                String name = nodeMap.item(i).getNodeName();
                String value = nodeMap.item(i).getNodeValue();

                map.put(name, value);
            }

        } catch (XPathExpressionException e) {
            SystemLogger.getInstance().write("Config:getAuth:" + e.getMessage());
        }

        return map;
    }

    /**
     * 実行環境取得
     *
     * @return 実行環境
     */
    public String getExecuteMode() {
        final String xpathStr = "//option";

        String mode = "";

        try {
            Node node = (Node) this.xpath.evaluate(xpathStr, this.document, XPathConstants.NODE);

            NamedNodeMap nodeMap = node.getAttributes();

            Node dailyNode = nodeMap.getNamedItem("mode");

            mode = dailyNode.getNodeValue();

        } catch (XPathExpressionException e) {
            SystemLogger.getInstance().write("Config:getExecuteMode:" + e.getMessage());
        }

        return mode;
    }


    /**
     * ファイル出力先情報取得
     *
     * @return ファイル出力先情報
     */
    public String getOutputPath() {
        final String xpathStr = "//option";

        String path = "";

        try {
            Node node = (Node) this.xpath.evaluate(xpathStr, this.document, XPathConstants.NODE);

            NamedNodeMap nodeMap = node.getAttributes();

            Node outputNode = nodeMap.getNamedItem("output");

            path = outputNode.getNodeValue();

        } catch (XPathExpressionException e) {
            SystemLogger.getInstance().write("Config:getOutputPath:" + e.getMessage());
        }

        return path;
    }

    /**
     * 事業所ファイル名取得
     *
     * @return
     */
    public Map<String, String> getOffice() {
        Map<String, String> map = new HashMap<>();

        final String xpathStr = String.format("//offices/office");

        try {
            NodeList nodeList = (NodeList) this.xpath.evaluate(xpathStr, this.document, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                NamedNodeMap nodeMap = node.getAttributes();

                Node nameNode = nodeMap.getNamedItem("name");
                Node fileNode = nodeMap.getNamedItem("file");
                map.put(fileNode.getNodeValue(), nameNode.getNodeValue());
            }

        } catch (XPathExpressionException e) {
            SystemLogger.getInstance().write("Config:getAuth:" + e.getMessage());
        }

        return map;
    }
}
