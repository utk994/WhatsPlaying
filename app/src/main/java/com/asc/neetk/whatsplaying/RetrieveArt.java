package com.asc.neetk.whatsplaying;

import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Created by utk994 on 03/06/15.
 */
public class RetrieveArt extends AsyncTask<String, Void, String> {

    protected String doInBackground(String... urls) {
        String albumArtUrl = null;
        try {
            XMLParser parser = new XMLParser();
            String xml = parser.getXmlFromUrl(urls[0]); // getting XML from URL
            Document doc = parser.getDomElement(xml);
            NodeList nl = doc.getElementsByTagName("image");
            for (int i = 0; i < nl.getLength(); i++) {
                Element e = (Element) nl.item(i);

                if(e.getAttribute("size").contentEquals("large")){
                    albumArtUrl = parser.getElementValue(e);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return albumArtUrl;
    }
}