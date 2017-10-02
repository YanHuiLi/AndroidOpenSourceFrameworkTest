package site.yanhui.litepaltest.DatabaseBean;

/**
 * Created by Archer on 2017/8/25.
 * 再新建一张Category表
 */

public class Category {

    private int id;
    private String CategoryName;
    private String CategoryCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
}
