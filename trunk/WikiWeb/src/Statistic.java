/**
 * @author Parth Mehrotra
 */
public interface Statistic {

    public long getPageView(String wl) throws Exception;

    public boolean existingWord(String wordToCheck) throws Exception;

    public void prep() throws Exception;
}

