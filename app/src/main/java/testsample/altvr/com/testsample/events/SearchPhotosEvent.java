package testsample.altvr.com.testsample.events;

/**
 * Created by root on 8/17/16.
 */
public class SearchPhotosEvent
{
    String query;
    String type;

    public SearchPhotosEvent(String query, String type)
    {
        this.query = query;
        this.type = type;
    }
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
