package com.example.bears.Utils;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class StationByUidItem extends AsyncTask<Void, Void, HashMap<String, String>> {
    private String url;
    private String busnumber;

    public StationByUidItem(String url, String busnumber) {
        this.url = url;
        this.busnumber = busnumber;
    }

    @Override
    protected HashMap<String, String> doInBackground(Void... params) {
        HashMap<String, String> resultMap = new HashMap<>();
        String rtNm = null;
        String arrmsg1 = null;
        String arrmsg2 = null;
        String vehId1 = null;
        String nxtStn = null;
        String stNm = null;

        DocumentBuilderFactory dbFactoty = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {
            dBuilder = dbFactoty.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document doc = null;
        try {
            doc = dBuilder.parse(url);
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }

        // root tag
        doc.getDocumentElement().normalize();

        // 파싱할 tag
        NodeList nList = doc.getElementsByTagName("itemList");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                rtNm = getTagValue("rtNm", eElement);
                if(rtNm == null){
                    Log.d("api Null", "rtNm : 널이다");
                }
                if(rtNm.equals(busnumber)){
                    vehId1 = getTagValue("vehId1",eElement);
                    arrmsg1 = getTagValue("arrmsg1", eElement);
                    arrmsg2 = getTagValue("arrmsg2", eElement);
                    nxtStn = getTagValue("nxtStn", eElement);
                    stNm = getTagValue("stNm", eElement);
                    resultMap.put("rtNm", rtNm);
                    resultMap.put("arrmsg1", arrmsg1);
                    resultMap.put("arrmsg2", arrmsg2);
                    resultMap.put("vehId1", vehId1);
                    resultMap.put("nxtStn", nxtStn);
                    resultMap.put("stNm", stNm);
                    return resultMap;
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(HashMap<String, String> result) {
        super.onPostExecute(result);
        // TODO Auto-generated method stub
    }

    private String getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if (nValue == null)
            return null;
        return nValue.getNodeValue();
    }
}

