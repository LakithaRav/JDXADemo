package demo.olc.com.jdxademo.model;

/**
 * Created by Lakitha on 10/30/15.
 */
public class WishItem {

    private int itemId;
    private int listId;
    private String name;
    private WishList list;

    // A no-arg constructor needed for JDX
    public WishItem() {

    }

    public WishItem(String _name, int _listid)
    {
        this.name = _name;
        this.listId = _listid;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WishList getList() {
        return list;
    }

    public void setList(WishList list) {
        this.list = list;
    }
}
