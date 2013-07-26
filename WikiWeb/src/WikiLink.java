/**
 * @author Parth Mehrotra
 */

import java.net.MalformedURLException;
import java.net.URL;

public class WikiLink implements Comparable<WikiLink> {

    public static String URL_HEAD = "http://en.wikipedia.org/wiki/";
    public static String STRING_SPLITTER = "href=" + "\"" + "/wiki/";
    private String pageTitle;
    private URL url;
    private long pageVC = 0;

    public WikiLink(String pageTitle) {
        this.pageTitle = pageTitle;
        pageTitle = pageTitle.replace(" ", "_");//XXX should this be switched?
        try {
            url = new URL(URL_HEAD + pageTitle);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        pageVC = -1;
    }

    public WikiLink(String pageTitle, URL url) {
        this.pageTitle = pageTitle;
        this.url = url;
        pageVC = -1;
    }

    public String getTitle() {
        return pageTitle;
    }

    public URL getURL() {
        return url;
    }

    @Override
    public String toString() {
        return pageTitle + ": " + pageVC + "\t" + " \t@\t " + url.toString();
    }

    public long getPageVC() {
        return pageVC;
    }

    public void setPageVC(long l) {
        pageVC = l;
    }

    @Override
    public int compareTo(WikiLink wikiLink) {
        if (this.pageVC < wikiLink.pageVC) {
            return -1;
        } else if (this.pageVC == wikiLink.pageVC) {
            return 0;
        } else if (this.pageVC > wikiLink.pageVC) {
            return 1;
        } else {
            return 2;
        }
    }
}
