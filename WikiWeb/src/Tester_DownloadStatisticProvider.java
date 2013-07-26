/**
 * @author Parth Mehrotra
 */

import java.util.Scanner;

public class Tester_DownloadStatisticProvider implements DownloadUpdater {
    DownloadStatisticProvider dsp;
    public Tester_DownloadStatisticProvider() throws Exception {
        dsp= new DownloadStatisticProvider();
        dsp.attachDownloadUpdater(this);
    }

    public static void main(String[] args) throws Exception {
        new Tester_DownloadStatisticProvider();
    }

    @Override
    public void downloadStarted() {
        System.out.println("Download Has Begun...");
    }

    @Override
    public void setOverallStatus(double status) {
        System.out.println("Overall Status: " + (status * 100) + "%");
    }

    @Override
    public void setDownloadStatus(double status) {
        System.out.println("Download Status: " + (status * 100) + "%");
    }

    @Override
    public void setUnzipStatus(double status) {
        System.out.println("Unzip Status: " + (status * 100) + "%");
    }


    @Override
    public void setReWriteStatus(double status) {
        System.out.println("Rewrite Status: " + (status * 100) + "%");
    }

    @Override
    public void downloadFinished() {
        System.out.println("Download Finished");
    }

    @Override
    public void downloadUnnessisary() {
        System.out.println("Down Unn");
    }

    @Override
    public void unzipUnnessisary() {
        System.out.println("zip unn");
    }

    @Override
    public void reWriteUnnessisary() {
        System.out.println("zip unn");
    }

    @Override
    public void processStarted() {
        System.out.println("Process Started");
    }

    @Override
    public void processFinished() {
        System.out.println("Process Finished");
        Scanner sc = new Scanner(System.in);
        for (String in = sc.nextLine(); !in.equalsIgnoreCase("exit"); in=sc.nextLine()) {
            try {
                System.out.println(dsp.getPageView(in));
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    @Override
    public void downloadRequired() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
