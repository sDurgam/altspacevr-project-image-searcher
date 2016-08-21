package testsample.altvr.com.testsample.events;

import java.util.List;

import testsample.altvr.com.testsample.vo.PhotoVo;

public class PhotosEvent
{
    public List<PhotoVo> data;
    public String type;
    public int page;
    public PhotosEvent(List<PhotoVo> data, String type, int page)
    {
        this.data = data;
        this.type = type;
        this.page = page;
    }
}
