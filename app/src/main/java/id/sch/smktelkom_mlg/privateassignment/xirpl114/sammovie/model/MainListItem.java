package id.sch.smktelkom_mlg.privateassignment.xirpl114.sammovie.model;

import com.orm.SugarRecord;

import java.io.Serializable;

/**
 * Created by paymin on 03/05/2017.
 */

public class MainListItem extends SugarRecord implements Serializable{

    private String imageUrl;
    private String head;
    private String desc;

    public MainListItem(String imageUrl, String head, String desc) {
        this.imageUrl = imageUrl;
        this.head = head;
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getHead() {
        return head;
    }

    public String getDesc() {
        return desc;
    }
}
