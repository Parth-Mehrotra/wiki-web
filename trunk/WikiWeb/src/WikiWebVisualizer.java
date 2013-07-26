import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class WikiWebVisualizer extends JPanel {

    private static final int DOWNLOAD = 0;
    private static final int QUERY_INPUT = 1;
    private static final int QUERY_TRANSITION = 2;
    private static final int QUERY_DISPLAY = 3;
    private static final String OVERALL_STRING = "Overall Progress";
    private static final String INITIAL_PROMPT = "Enter a Query: ";
    private Thread dataRetrievalThread;
    private Thread query;
    private ProgressLine overall, task;
    private int mode;
    private String taskString = "Current Task";
    private WikiSearch wikiSearch;
    private String inputQuerySoFar;
    private DownloadStatisticProvider downloadStatisticProvider;
    private WikiLinkGroup wikiLinkGroup;

    public WikiWebVisualizer() throws Exception {
        int w = 200;
        int x = getWidth() - w / 2;
        int y1 = getHeight() - 50;
        int y2 = getHeight() + 50;

        overall = new ProgressLine(Color.WHITE, Color.RED);
        task = new ProgressLine(Color.WHITE, Color.RED);

        dataRetrievalThread = new Thread(new Runnable() {
            @Override
            public void run() {
                inputQuerySoFar = INITIAL_PROMPT;

                try {
                    final DownloadStatisticProvider
                            dsp = new DownloadStatisticProvider();

                    dsp.attachDownloadUpdater(new DownloadUpdater() {
                        @Override
                        public void downloadStarted() {
                        }

                        @Override
                        public void setOverallStatus(double status) {
                            overall.setStatus(status);
                            repaint();
                        }

                        @Override
                        public void setDownloadStatus(double status) {
                            taskString = "Download data file from MediaWiki";
                            task.setStatus(status);
                            repaint();
                        }

                        @Override
                        public void setUnzipStatus(double status) {
                            taskString = "Unzipping the file";
                            task.setStatus(status);
                            repaint();
                        }

                        @Override
                        public void setReWriteStatus(double status) {
                            taskString = "Extracting english portions";
                            task.setStatus(status);
                            repaint();
                        }

                        @Override
                        public void downloadFinished() {
                        }

                        @Override
                        public void downloadUnnessisary() {
                        }

                        @Override
                        public void unzipUnnessisary() {
                        }

                        @Override
                        public void reWriteUnnessisary() {
                        }

                        @Override
                        public void processStarted() {
                            mode = DOWNLOAD;
                            repaint();
                        }

                        @Override
                        public void processFinished() {
                            mode = QUERY_INPUT;
                            repaint();
                            setDownloadStatistics(dsp);
                        }

                        @Override
                        public void downloadRequired() {
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }

        });
        dataRetrievalThread.start();

        query = new Thread(new Runnable() {
            @Override
            public void run() {
                String input = inputQuerySoFar.replace(INITIAL_PROMPT, "");
                wikiLinkGroup = new WikiLinkGroup(input, 10);
                mode = QUERY_DISPLAY;
                repaint();
                try {
                    WikiSearch
                            search = new WikiSearch(input,
                            new WikiFilter(addFilters()), downloadStatisticProvider);
                    search.attachResultsFound(new ResultFound() {
                        @Override
                        public void resultFound(WikiLink result) {
                            wikiLinkGroup.add(result);
                            repaint();
                        }
                    });
                    search.getFilteredWikipediaLinks();
                } catch (Exception e) {
                }
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
        filterString
                .add("List of all English Wikipedia pages containing links to this page");
        filterString.add("Recent changes in pages linked from this page");
        filterString.add("Upload files");
        filterString.add("A list of all special pages");
        filterString.add("Wikipedia:About");
        filterString.add("Wikipedia:General disclaimer");
        filterString.add("Talk:");
        filterString.add("Wikipedia:");
        filterString.add("internal");
        filterString.add("This article is ");
        return filterString;
    }

    public void setDownloadStatistics(DownloadStatisticProvider dsp) {
        this.downloadStatisticProvider = dsp;
    }

    public void keyTyped(KeyEvent keyEvent) {
        if (mode == QUERY_INPUT) {
            if ((int) keyEvent.getKeyChar() == 8) {
                if (inputQuerySoFar.length() > INITIAL_PROMPT.length()) {
                    inputQuerySoFar = inputQuerySoFar.substring(0, inputQuerySoFar.length() - 1);
                }
            } else if (keyEvent.getKeyChar() == '\n') {
                mode = QUERY_TRANSITION;
                doQuery();
            } else {
                inputQuerySoFar += keyEvent.getKeyChar();
            }
        }
        repaint();
    }

    public void doQuery() {
        query.start();
    }

    @Override
    public void paint(Graphics g) {
        if (mode == DOWNLOAD) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());

            int w = 500;
            int x = (getWidth() / 2) - (w / 2);
            int y1 = getHeight() / 2 - 50;
            int y2 = getHeight() / 2 + 50;

            overall.setDim(x, y1, w);
            task.setDim(x, y2, w);

            overall.paint(g);
            task.paint(g);

            g.setColor(Color.WHITE);
            g.drawString(taskString, x, y2 - 7);
            g.drawString(OVERALL_STRING, x, y1 - 7);
        } else if (mode == QUERY_INPUT) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.drawString(inputQuerySoFar, 0, 0 + 15);
        } else if (mode == QUERY_DISPLAY) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            wikiLinkGroup.paint(g, this);
        }
    }
}
