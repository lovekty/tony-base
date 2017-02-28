package me.tony.base.orm;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tony on 2017/2/28.
 */
public abstract class BaseRelation implements Serializable {
    private static final long serialVersionUID = -2415499303722355187L;

    private Long id;

    private Date insertTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getInsertTime() {
        return insertTime;
    }

    public void setInsertTime(Date insertTime) {
        this.insertTime = insertTime;
    }
}
