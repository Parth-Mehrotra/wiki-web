/**
 * @author Parth Mehrotra
 * DownloadUpdater informs client classs about what's going on with the download
 */
public interface DownloadUpdater {
    public void downloadStarted();

    public void setOverallStatus(double status);

    public void setDownloadStatus(double status);

    public void setUnzipStatus(double status);

    public void setReWriteStatus(double status);

    public void downloadFinished();

    public void downloadUnnessisary();

    public void unzipUnnessisary();

    public void reWriteUnnessisary();

    public void processStarted();

    public void processFinished();

    public void downloadRequired();

}
