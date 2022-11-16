package com.example.gp;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class maniadbapi extends AsyncTask<Void, String, Map<String, Object>> {

    private String url1;
    private String url2;
    private String key;
    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> singers = new ArrayList<>();
    private ArrayList<String> images = new ArrayList<>();

    private Map<String, Object> rtmap = new HashMap<>();

    private Context context;

    public maniadbapi(String url1, String url2, String key) {

        this.url1 = url1;
        this.url2 = url2;
        this.key = key;
    }

    @Override
    protected Map<String, Object> doInBackground(Void... params) {

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
            doc = dBuilder.parse(url1 + key + url2);
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }


        // root tag
        doc.getDocumentElement().normalize();
        System.out.println("Root element: " + doc.getDocumentElement().getNodeName()); // Root element: result

        // 파싱할 tag
        NodeList nList = doc.getElementsByTagName("item");

        for(int temp = 0; temp < nList.getLength(); temp++){
            Node nNode = nList.item(temp);
            if(nNode.getNodeType() == Node.ELEMENT_NODE){

                Element eElement = (Element) nNode;

                // MANIADB test
                //Log.d("MANIA"," 곡명  : " + getTagValue("title", eElement));
                //Log.d("MANIA"," 가수  : " + getTagValue("name", eElement));
                //Log.d("MANIA"," 앨범  : " + getTagValue("image", eElement));

                titles.add(getTagValue("title", eElement));
                singers.add(getTagValue("name", eElement));
                images.add(getTagValue("image", eElement));


            }	// for end
        }	// if end

        rtmap.put("title", titles);
        rtmap.put("singer", singers);
        rtmap.put("image", images);

        // map형태 저장 test
        /*
        for(int i = 0; i < titles.size(); i++){
            //System.out.println(titles.get(i));
            //System.out.println(singers.get(i));
            //System.out.println(images.get(i));
            System.out.println(rtmap.get("title"));
            System.out.println(rtmap.get("singer"));
            System.out.println(rtmap.get("image"));
        }
        */
        return rtmap;
    }

    @Override
    protected void onPostExecute(Map<String, Object> str) {
        super.onPostExecute(str);
    }

    private String getTagValue(String tag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        if(nValue == null)
            return null;
        return nValue.getNodeValue();
    }


}