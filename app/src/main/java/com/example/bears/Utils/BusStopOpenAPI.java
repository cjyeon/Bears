package com.example.bears.Utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.bears.Activity.MainActivity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class BusStopOpenAPI extends AsyncTask<Void, Void, HashMap<String,String>> {
    private String url;
    public BusStopOpenAPI( String url) {

        this.url = url;
    }
    

    @Override
    protected HashMap<String, String> doInBackground(Void... params) {
        String ars_id = null;
        String stationNm = null;
        HashMap<String, String> resultMap = new HashMap<String,String>();

        Log.d("doInBACKGROUNTD", url);
        // parsing할 url 지정(API 키 포함해서)

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
        System.out.println("Root element: " + doc.getDocumentElement().getNodeName()); // Root element: result

        // 파싱할 tag
        NodeList nList = doc.getElementsByTagName("itemList");

        for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) nNode;
                ars_id =  getTagValue("arsId", eElement);
                stationNm = getTagValue("stationNm", eElement);

                resultMap.put("arsId", ars_id);
                resultMap.put("stationNm",stationNm);
                Log.d("OPEN_API", "arsId  : " + ars_id);
                Log.d("OPEN_API", "stationId  : " + getTagValue("stationId", eElement));
                Log.d("OPEN_API", "stationNm : " +stationNm);
                return resultMap;

            }    // for end
        }    // if end
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

