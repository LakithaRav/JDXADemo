package demo.olc.com.jdxademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.softwaretree.jdx.JDXS;
import com.softwaretree.jdxandroid.JDXHelper;
import com.softwaretree.jdxandroid.JDXSeqUtil;
import com.softwaretree.jdxandroid.JDXSetup;
import com.softwaretree.jx.JXResource;
import com.softwaretree.jx.JXSession;
import com.softwaretree.jx.JXUtilities;

import java.util.ArrayList;
import java.util.List;

import demo.olc.com.jdxademo.model.WishItem;
import demo.olc.com.jdxademo.model.WishList;

public class MainActivity extends AppCompatActivity {

    JDXSetup jdxSetup = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try
        {
            JDXASetup.initialize();
            jdxSetup = JDXASetup.getInstance(this);
            runDemo(jdxSetup);
        } catch (Exception ex) { // Handle exception
            cleanup();
            return;
        }
    }

    private void cleanup() {
        JDXASetup.cleanup();
        jdxSetup = null;
    }

    private void runDemo(JDXSetup jdxSetup) throws Exception {

        JDXHelper jdxHelper = new JDXHelper(jdxSetup);

        String wishListClassName = WishList.class.getName();
        String wishItemClassName = WishItem.class.getName();

        // This is a utility class to easily mange persistently unique Named Sequence values.
        JDXSeqUtil wishListSeqUtil = new JDXSeqUtil("WishListIdSequence", jdxSetup, 1);

        // Create and save a WishList with a few WishItems
        WishList wishList = new WishList("My wishes");

        // Get the database Id of inserting WishList object
        int wishListId = (int) wishListSeqUtil.getNextSeq();

        // Adding couple of items to the wish-list
        ArrayList<WishItem> wishItems = new ArrayList<WishItem>();
        wishItems.add (new WishItem("My wish #1", wishListId));
        wishItems.add (new WishItem("My wish #2", wishListId));
        wishItems.add (new WishItem("My wish #3", wishListId));
        wishItems.add (new WishItem("My wish #4", wishListId));

        // Add the items to the wishList
        wishList.setItems(wishItems);

        // Bulk insert the wish-list along with the items, this is possible since we have specified 'BYVALUE' option in our jdxspec.jdx file
        // deep insert; all the related WishItems will also be saved
        jdxHelper.insert(wishList, true);

        // Retrieve all WishItems independently
        // ArrayList<WishItem> items = (ArrayList<WishItem>) jdxHelper.getObjects(wishItemClassName, null);
        // JXUtilities.printQueryResults(items);

        // Retrieve all WishItems independently but don't further retrieve WishItems of the related WishList

        // Avoiding back pointer circularity
        // Otherwise there will be an endless loop of parent to child, child to parent reference in the retrieving data objects
        List ignoreList = new ArrayList();
        ignoreList.add(wishListClassName); // the class name
        ignoreList.add("items");  // the name of the attribute to be ignored
        List queryOption1 = new ArrayList();
        queryOption1.add(JDXS.IGNORE);
        queryOption1.add(ignoreList);
        List queryDetails = new ArrayList();
        queryDetails.add(queryOption1);

        // Retrieving all the item from the database
        ArrayList<WishItem> items = (ArrayList<WishItem>) jdxHelper.getObjects(wishItemClassName, null, JDXS.ALL, JDXS.FLAG_DEEP, queryDetails);
        JXUtilities.printQueryResults(items);

        // Retrieve a particular WishList with Primary Key along with all the related WishItems
        wishList = (WishList) jdxHelper.getObjectById(wishListClassName, "listId=" + wishListId, true, null);
        JXUtilities.printObject(wishList, 0, null);

        // Retrieve all the Wishlist lists fron the database, this will also retrieve Wishlist items of a Wishlist object as well
        ArrayList<WishList> wishes = (ArrayList<WishList>) jdxHelper.getObjects(wishListClassName, null);
        JXUtilities.printQueryResults(wishes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        cleanup();
        super.onDestroy();
    }
}
