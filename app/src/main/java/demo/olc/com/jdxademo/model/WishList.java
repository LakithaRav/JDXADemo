package demo.olc.com.jdxademo.model;

import java.util.ArrayList;

/**
 * Created by Lakitha on 10/30/15.
 */
public class WishList {

    private int listId;
    private String name;
    private ArrayList<WishItem> items;

    public WishList() {

    }

    public WishList(String _name, int _listid)
    {
        this.listId = _listid;
        this.name = _name;
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

    public ArrayList<WishItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<WishItem> items) {
        this.items = items;
    }
}
