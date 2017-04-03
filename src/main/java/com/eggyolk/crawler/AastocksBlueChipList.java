package com.eggyolk.crawler;

import com.eggyolk.vo.AastocksLabel;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class AastocksBlueChipList implements AastocksList {

    public String getJson() throws Exception {

        JSONArray list = new JSONArray();

        (new AastocksBlueChipList()).getList().stream().forEach((label) -> {
            System.out.println(label);
            JSONObject cobj = new JSONObject();
            cobj.put("label", label.descZh);
            cobj.put("code", label.code);
            list.add(cobj);
        });

        return list.toJSONString();
    }

    public ArrayList<AastocksLabel> getList() throws Exception {

        Document doc;
        ArrayList<AastocksLabel> list  = new ArrayList();

        try {

            // need http protocol
            doc = Jsoup.connect("http://www.etnet.com.hk/www/tc/stocks/indexes_detail.php?subtype=HSI").get();

            // get page title
            String title = doc.title();
            System.out.println("title : " + title);

            Element table = doc.select("table[class=figureTable]").get(0);
            Elements rows = table.select("tr");

            for (int i = 1; i < rows.size(); i++) { // first row is the col names so skip it.
                Element row = rows.get(i);
                Elements cols = row.select("td");

                AastocksLabel lbl = new AastocksLabel();
                lbl.code = cols.get(0).text();
                lbl.descZh = cols.get(1).text();
                list.add(lbl);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        return list;
    }

    public static void main(String[] args) throws Exception {

        AastocksList list = new AastocksBlueChipList();
        System.out.println(list.getJson());
    }
}
