package testsample.altvr.com.testsample.vo;

import java.sql.Blob;

/**
 * Created by root on 8/18/16.
 */
public class PhotoDBVo
{
    String id;
    byte[] photo;
    String tags;

    public PhotoDBVo()
    {

    }

    public PhotoDBVo(String _id, byte[] _photo, String _tags)
    {
        id = _id;
        photo = _photo;
        tags = _tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
