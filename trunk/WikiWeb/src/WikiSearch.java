/**
 * @author Parth Mehrotra
 */

import java.util.ArrayList;

public class WikiSearch extends WikiLink {

    private RelevantLinks relaventLinks;
    private WikiFilter filter;
    private DownloadStatisticProvider dsp;
    private ResultFound resultFound;

    public WikiSearch(String pageTitle, DownloadStatisticProvider dsp) throws Exception {
        super(pageTitle);
        this.dsp = dsp;
        relaventLinks = new RelevantLinks(super.getURL(), dsp);
    }


    public WikiSearch(String pageTitle, WikiFilter filter, DownloadStatisticProvider dsp) throws Exception {
        super(pageTitle);
        this.dsp = dsp;
        this.filter = filter;
        relaventLinks = new RelevantLinks(super.getURL(), dsp);
    }

    public void attachResultsFound(ResultFound resultFound) {
        this.resultFound = resultFound;
    }

    public ArrayList<WikiLink> getFilteredWikipediaLinks() throws Exception {
        ArrayList<WikiLink> unfilteredList = getUnFilteredWikipediaLinks().getWikipediaLinks();
        ArrayList<WikiLink> filteredList = new ArrayList<WikiLink>();


        if (filter.getFilters().size() < 1) {
            return unfilteredList; // TODO Move this up and call the method instead, avoid creating unn objs.
        }

        for (int i = 0; i < unfilteredList.size(); i++) {
            boolean add = true;
            for (int j = 0; j < filter.getFilters().size(); j++) {
                if (unfilteredList.get(i).getTitle().contains(filter.getFilters().get(j))) {
                    add = false;
                }
            }
            if (add) {
                WikiLink wikiLink = unfilteredList.get(i);
                wikiLink.setPageVC(dsp.getPageView(wikiLink.getTitle()));
                filteredList.add(wikiLink);
                if (resultFound != null) {
                    resultFound.resultFound(wikiLink);
                }
            }
        }

        return filteredList;

    }

    public RelevantLinks getUnFilteredWikipediaLinks() {
        return relaventLinks;
    }
}
