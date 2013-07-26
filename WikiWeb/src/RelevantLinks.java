/**
 * @author Parth Mehrotra
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class RelevantLinks {

    private ArrayList<WikiLink> relaventLinks;

    public RelevantLinks(URL url, DownloadStatisticProvider dsp) throws Exception {

        try {
            URLConnection con = url.openConnection();
            BufferedReader allHtml = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String now;
            ArrayList<String> lines = new ArrayList<String>();

            ArrayList<String> a = null;
            while ((now = allHtml.readLine()) != null) {
                a = getLinks(now);

                if (a != null)
                    for (int i = 0; i < a.size(); i++) {
                        lines.add(a.get(i));
                    }
            }
            relaventLinks = getWikipediaLinks(lines);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String[] getTitleAndLink(String line) {
        String[] ar = line.split("\"");
        if (ar.length <= 2) {
            return null;
        }
        return new String[]{ar[2], ar[0]};
    }

    private static ArrayList<String> getLinks(String str) {
        if (!str.contains(WikiLink.STRING_SPLITTER))
            return null;

        ArrayList<String> s = new ArrayList<String>();
        String[] ar = str.split(WikiLink.STRING_SPLITTER);
        for (int i = 1; i < ar.length; i++) {
            s.add(ar[i]);
        }
        return s;
    }

    private ArrayList<WikiLink> getWikipediaLinks(ArrayList<String> lines) throws Exception {
        ArrayList<WikiLink> wl = new ArrayList<WikiLink>();

        for (int i = 0; i < lines.size(); i++) {
            String[] titleLink = getTitleAndLink(lines.get(i));
            if (titleLink != null) {
                try {
                    URL url = new URL(WikiLink.URL_HEAD + titleLink[1]);
                    WikiLink wikiLink = new WikiLink(titleLink[0], url);
                    wl.add(wikiLink);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        }
        return wl;
    }

    public ArrayList<WikiLink> getWikipediaLinks() {
        return relaventLinks;
    }
}
