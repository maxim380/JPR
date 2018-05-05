//package com.maxim.jpr.Util;
//
//import android.os.AsyncTask;
//
//import java.io.IOException;
//
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//public class SongGetter extends AsyncTask<String, String, String> {
//
//    @Override
//    public String doInBackground(String... strings) {
//        final StringBuilder builder = new StringBuilder();
//        final String url = strings[0];
//        try {
//            Document doc = Jsoup.connect(url).get();
//            String[] test = doc.toString().split("\"");
//            for(int i = 0; i < test.length; i++) {
//                String s = test[i];
//                if (s.equals("song")) {
//                    builder.append(test[i + 2]);
//                }
//            }
//        } catch (IOException e) {
//            builder.append("Error : ").append(e.getMessage()).append("\n");
//        }
//        return builder.toString();
//    }
//}
