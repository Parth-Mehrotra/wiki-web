/**
 * @author Parth Mehrotra
 */

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

/**
 * DownloadStatisticProvide is in charge of going to the MediaWiki dumps and getting the files that contain download information.
 * <p/>
 * Problems: Linear search NEEDS to be replaced with a binary search
 */
public class DownloadStatisticProvider implements Statistic {

    private long downloadedTextFileSize;
    private File unzippedFile, zippedFile, finalEnglishQueryFile;
    private String[] downloadURLInfo;
    private DownloadUpdater downloadUpdater;
    private double overallStatus;

    public DownloadStatisticProvider() throws Exception {
//        downloadURLInfo = new String[]{"http://localhost/", "xampp/", "learning/", "pagecounts-20130616-170004.gz"};
        downloadURLInfo = getMostRecentName();
        String fileWOExtension = downloadURLInfo[3].replace(".gz", "");
        zippedFile = new File(fileWOExtension + ".gz");
        unzippedFile = new File(fileWOExtension);
        finalEnglishQueryFile = new File("en-" + unzippedFile.toString());
        System.out.println("Created");

    }

    private static ArrayList<String> getOccurancesInHtml(URL url, String spl)
            throws Exception {
        URLConnection con = url.openConnection();
        con.addRequestProperty("User-agent", "Mozilla/2.0.0.11");
        Thread.sleep(1000);
        BufferedReader allHtml = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String now;
        ArrayList<String> lines = new ArrayList<String>();

        while ((now = allHtml.readLine()) != null) {
            if (now.contains(spl))
                lines.add(now);
        }
        ArrayList<String> split = new ArrayList<String>();
        for (int i = 0; i < lines.size(); i++) {
            String[] arr = lines.get(i).split(spl);
            for (int j = 1; j < arr.length; j++) {
                split.add(spl + arr[1]);
            }
        }
        return split;
    }

    private static String fixString(String string) {
        String s = string;
        s.replace("%20", " ");
        s.replace("%27", "'");
        s.replace(" ", "");
        return s;
    }

    public void attachDownloadUpdater(DownloadUpdater downloadUpdater) {
        this.downloadUpdater = downloadUpdater;
        downloadUpdater.processStarted();
        System.out.println("Started");
        try {
            prep();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        downloadUpdater.processFinished();
    }

    public long getSize() {
        return downloadedTextFileSize;
    }

    @Override
    public boolean existingWord(String wordToCheck) throws Exception {
        return getPageView(wordToCheck) >= 0;
    }

    @Override
    public long getPageView(String wl) throws Exception {
        wl = wl.replace(" ", "_");
        BufferedReader br = new BufferedReader(new FileReader(finalEnglishQueryFile));
        String line;
        long num = 0;
        while ((line = br.readLine()) != null) {
            line = fixString(line);
            String[] data = line.split(" ");
            if (data[0].equals("en") && data[1].equalsIgnoreCase(wl)) {
                num += Integer.parseInt(data[2]);
            }
        }
        return num;
    }

    private String[] getMostRecentName() throws Exception {
        String root = "http://dumps.wikimedia.org/other/pagecounts-raw/";
        URL stepOne = new URL(root);
        ArrayList<String> last = getOccurancesInHtml(stepOne, "href=\"2");

        String year = last.get(last.size() - 1).split("\"")[1] + "/";

        URL stepTwo = new URL(root + year);
        ArrayList<String> yearPage = getOccurancesInHtml(stepTwo, "href=\"2");
        String hour = yearPage.get(yearPage.size() - 1).split("\"")[1] + "/";

        URL stepThree = new URL(root + year + hour);
        ArrayList<String> filePage = getOccurancesInHtml(stepThree,
                "href=\"pagecounts");
        return new String[]{root, year, hour, (filePage.get(filePage.size() - 1).split("\"")[1])};
    }

    @Override
    public void prep() throws Exception {
        BufferedReader br;
        URL theFileToBeDownloaded = new URL(downloadURLInfo[0] + downloadURLInfo[1] + downloadURLInfo[2] + downloadURLInfo[3]);
        if (!zippedFile.exists()) {
            InputStream in = null;
            OutputStream out = null;
            URLConnection con = null;

            out = new BufferedOutputStream(new FileOutputStream(zippedFile));
            con = theFileToBeDownloaded.openConnection();
            con.addRequestProperty("User-agent", "Mozilla/2.0.0.11");
            in = con.getInputStream();
            long length = Long.parseLong(con.getHeaderFields()
                    .get("Content-Length").get(0));

            byte[] buffer = new byte[1024];

            int numRead;
            long numWritten = 0;

            downloadUpdater.downloadStarted();
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
                downloadUpdater.setDownloadStatus((double) numWritten / (double) length);
                overallStatus = ((double) numWritten / (double) length);
                downloadUpdater.setOverallStatus(overallStatus * (1d / 3d));
                numWritten += numRead;
            }

            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            downloadUpdater.downloadFinished();
        } else {
            downloadUpdater.downloadUnnessisary();
        }

        downloadUpdater.setOverallStatus(overallStatus);
        if (!unzippedFile.exists()) {

            GZIPInputStream gzipInputStream = new GZIPInputStream(
                    new FileInputStream(zippedFile));
            FileOutputStream fileOutputStream = new FileOutputStream(
                    unzippedFile);

            int len;
            byte[] zipBuffer = new byte[1024];

            overallStatus = 0.5;

            while ((len = gzipInputStream.read(zipBuffer)) != -1) {
                fileOutputStream.write(zipBuffer, 0, len);
                downloadUpdater.setUnzipStatus(.50);
                downloadUpdater.setOverallStatus(overallStatus);
            }
            gzipInputStream.close();
            fileOutputStream.close();
        } else {
            downloadUpdater.unzipUnnessisary();
        }

        overallStatus = (2d / 3d);
        downloadUpdater.setOverallStatus(overallStatus);

        LineNumberReader lnr = new LineNumberReader(new FileReader(unzippedFile));
        lnr.skip(Long.MAX_VALUE);
        downloadedTextFileSize = lnr.getLineNumber();

        double rStatus;

        if (!finalEnglishQueryFile.exists()) {
            br = new BufferedReader(new FileReader(unzippedFile));
            FileOutputStream finalEnglishFile = new FileOutputStream("en-"
                    + unzippedFile.toString());
            int j = 0;
            for (String line = br.readLine(); line != null; line = br
                    .readLine()) {
                String[] data = line.split(" ");
                if (data[0].equals("en")) {
                    line += "\n";
                    finalEnglishFile.write(line.getBytes());
                }
                rStatus = ((double) j++ / (double) downloadedTextFileSize);
                downloadUpdater.setReWriteStatus(rStatus);
                downloadUpdater.setOverallStatus(overallStatus + (rStatus * (1d / 3d)));
            }
            finalEnglishFile.close();
        } else {
            downloadUpdater.reWriteUnnessisary();
        }
        downloadUpdater.setOverallStatus(1d);
        finalEnglishQueryFile = new File("en-" + unzippedFile.toString());
    }
}