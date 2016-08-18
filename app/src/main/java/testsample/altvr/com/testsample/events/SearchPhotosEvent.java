package testsample.altvr.com.testsample.events;

/**
 * Created by root on 8/17/16.
 */
public class SearchPhotosEvent
{
    public String query;
    public SearchPhotosEvent(String _query)
    {
        query = _query;
    }
}
