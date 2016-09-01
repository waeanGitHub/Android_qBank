package pojo;

import java.io.Serializable;

/**
 * Created by Asus on 2016/08/31.
 */

/**
 * Id是问题的id
 * pubTime是问题的发布时间
 * typeid是问题的类型（问题的类型： 1单选、2多选、3判断、4简答）
 * answer是问题的答案
 * cataid是问题的类别
 * options是问题为单选和多选的选项内容，其中title是选项的内容，checked是选项是否为答案
 */
public class QuestionInfo implements Serializable {
    private String content;
    private int id;
    private String pubTime;
    private int typeid;
    private String answer;
    private int cataid;
    private Boolean isCollect = false;

    public QuestionInfo() {
    }

    public QuestionInfo(String content, int id, String pubTime, int typeid, String answer, int cataid) {
        this.content = content;
        this.id = id;
        this.pubTime = pubTime;
        this.typeid = typeid;
        this.answer = answer;
        this.cataid = cataid;
    }

    public QuestionInfo(boolean isCollect, String content, int id, String pubTime, int typeid, String answer, int cataid) {
        this.isCollect = isCollect;
        this.content = content;
        this.id = id;
        this.pubTime = pubTime;
        this.typeid = typeid;
        this.answer = answer;
        this.cataid = cataid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPubTime() {
        return pubTime;
    }

    public void setPubTime(String pubTime) {
        this.pubTime = pubTime;
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getCataid() {
        return cataid;
    }

    public void setCataid(int cataid) {
        this.cataid = cataid;
    }

    public boolean getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(boolean isCollect) {
        this.isCollect = isCollect;
    }

    @Override
    public String toString() {
        return "QuestionInfo{" +
                "content='" + content + '\'' +
                ", id=" + id +
                ", pubTime='" + pubTime + '\'' +
                ", typeid=" + typeid +
                ", answer='" + answer + '\'' +
                ", cataid=" + cataid +
                ", isCollect='" + isCollect + '\'' +
                '}';
    }
}
