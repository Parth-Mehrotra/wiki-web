/**
 * @author Parth Mehrotra
 */

import java.util.ArrayList;
import java.util.Scanner;

public class Tester_WikiSearch {

    public static void main(String args[]) throws Exception {
        final DownloadStatisticProvider dsp = new DownloadStatisticProvider();
        dsp.attachDownloadUpdater(new DownloadUpdater() {
            @Override
            public void downloadStarted() {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setOverallStatus(double status) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setDownloadStatus(double status) {
                System.out.println(status);
            }

            @Override
            public void setUnzipStatus(double status) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void setReWriteStatus(double status) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void downloadFinished() {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void downloadUnnessisary() {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void unzipUnnessisary() {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void reWriteUnnessisary() {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void processStarted() {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void processFinished() {
                System.out.println("Ready");
                WikiFilter f = new WikiFilter(addFilters());
                Scanner sc = new Scanner(System.in);
                for (String input = sc.nextLine(); !input.equalsIgnoreCase("exit"); input = sc.nextLine()) {
                    WikiSearch wikiSearch = null;
                    try {
                        wikiSearch = new WikiSearch(input, f, dsp);
                    } catch (Exception e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    try {
                        for (int i = 0; i < wikiSearch.getFilteredWikipediaLinks().size(); i++) {
//                        System.out.println("\t" + wikiSearch.getFilteredWikipediaLinks().get(i));
                        }
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void downloadRequired() {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });

    }

    public static ArrayList<String> addFilters() {
        ArrayList<String> filterString = new ArrayList<String>();
        filterString.add("File:");
        filterString.add("image");
        filterString.add("Book:");
        filterString.add("Category:");
        filterString.add("Portal:");
        filterString.add("Help:");
        filterString.add("Discussion about the content page");
        filterString.add("Visit the main page");
        filterString.add("Guides to browsing Wikipedia");
        filterString.add("Featured content – the best of Wikipedia");
        filterString.add("Find background information on current events");
        filterString.add("Load a random article");
        filterString.add("Guidance on how to use and edit Wikipedia");
        filterString.add("Find out about Wikipedia");
        filterString.add("About the project");
        filterString.add("List of all English Wikipedia pages containing links to this page");
        filterString.add("Recent changes in pages linked from this page");
        filterString.add("Upload files");
        filterString.add("A list of all special pages");
        filterString.add("Wikipedia:About");
        filterString.add("Wikipedia:General disclaimer");
        filterString.add("Talk:");
        filterString.add("Wikipedia:");
        filterString.add("internal:");
        return filterString;
    }
}
