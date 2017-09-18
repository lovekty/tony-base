package me.tony.base.common.orm;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tony on 2017/2/28.
 */
public abstract class BaseEntity implements Serializable {
    private static final long serialVersionUID = -4668685056829150776L;

    private Long id;

    private Date insertTime;

    private Date updateTime;

    private Boolean visible;

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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }
}
