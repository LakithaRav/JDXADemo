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

/**
 * This class shows how JDXA ORM can be initialized and used for exchanging data of model
 * class objects with a SQLite database on the Android platform.
 *
 * The project uses JDXA ORM with two model classes: WishList and WishItem.
 * There is a one-to-many relationship between WishList and WishItem.
 *
 * Created by Lakitha on 10/30/15.
 */
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

    /**
     * This method demonstrates various CRUD operations using JDXA ORM.
     * It also demonstrates JDXA utility methods printObject and printQueryResults, which can
     * easily display object graphs. You may check Logcat for the output.
     */
    private void runDemo(JDXSetup jdxSetup) throws Exception {

        JDXHelper jdxHelper = new JDXHelper(jdxSetup);

        String wishListClassName = WishList.class.getName();
        String wishItemClassName = WishItem.class.getName();

        // This is a utility class to easily mange persistently unique Named Sequence values.
        JDXSeqUtil wishListSeqUtil = new JDXSeqUtil("WishListIdSequence", jdxSetup, 1);

        // First delete all the existing WishList objects along with the related WishItem objects
        // comment this line if you like
        jdxHelper.delete2(wishListClassName, null);

        //Unique id through a Sequence Generator
        int wishListId = (int) wishListSeqUtil.getNextSeq();

        // Create and save a WishList with a few WishItems
        // Will use 'Deep Insert' approach to minimize the database access cycles
        WishList wishList = new WishList("My wishes", wishListId);

        // Adding couple of items to the wish-list
        ArrayList<WishItem> newWishItems = new ArrayList<WishItem>();
        newWishItems.add (new WishItem("My wish #1", wishListId));
        newWishItems.add (new WishItem("My wish #2", wishListId));
        newWishItems.add (new WishItem("My wish #3", wishListId));
        newWishItems.add (new WishItem("My wish #4", wishListId));

        // Add the items to the wishList
        wishList.setItems(newWishItems);

        // Bulk insert the wish-list along with the items, this is possible since we have specified 'BYVALUE' option in our jdxspec.jdx file
        // deep insert; all the related WishItems will also be saved
        jdxHelper.insert(wishList, true);

        /**
         * Listed down below are different ways that you access you database record using JDXA
         */

        // Retrieve all the WishList objects along with the related WishItem objects (Deep Query)
        List<WishList> wishLists =  jdxHelper.getObjects(wishListClassName, null);
        JXUtilities.printQueryResults(wishLists);

        // Retrieve all the WishList objects without the related WishItem objects (Shallow Query)
        wishLists = jdxHelper.getObjects(wishListClassName, null, JDXS.ALL, JDXS.FLAG_SHALLOW, null);
        JXUtilities.printQueryResults(wishLists);

        // Retrieve all the WishItem objects along with the related WishList objects (Deep Query)
        List<WishItem> wishItems =  jdxHelper.getObjects(wishItemClassName, null);
        JXUtilities.printQueryResults(wishItems);

        // Retrieve all WishItem objects independently without the related WishList objects (Shallow Query)
        wishItems = jdxHelper.getObjects(wishItemClassName, null, JDXS.ALL, JDXS.FLAG_SHALLOW, null);
        JXUtilities.printQueryResults(wishItems);

        // Now create and save a new WishItem independently for a known WishList (Shallow Insert)
        WishItem wishItem = new WishItem("My wish #5", wishListId);
        jdxHelper.insert(wishItem, false);

        // Get the number of WishItem objects in the database (Aggregate Query)
        Integer count = (Integer) jdxHelper.getObjectCount(wishItemClassName, "itemId", null);
        JXUtilities.log("\n-- Number of WishItems=" + count + " --\n"); // count should be 5

        // Retrieve a particular WishList object along with the related WishItem objects (Deep Query)
        wishList = (WishList) jdxHelper.getObjectById(wishListClassName, "listId=" + wishListId, true, null);
        JXUtilities.printObject(wishList, 0, null); // It should also show the "My wish #5" WishItem

        // Modify the name of a WishList object and update it in the database (Shallow Update)
        wishList.setName("My updated wishes");
        jdxHelper.update(wishList, false);

        // Retrieve a particular WishList object without the related WishItem objects (Shallow Query)
        wishList = (WishList) jdxHelper.getObjectById(wishListClassName, "listId=" + wishListId, false, null);
        JXUtilities.printObject(wishList, 0, null); // It should show "My updated wishes"


        // Create and save an empty WishList (Deep or Shallow Insert)
        wishListId = (int) wishListSeqUtil.getNextSeq(); // Unique id through a Sequence Generator
        wishList.setListId(wishListId);
        wishList = new WishList("My empty wishes", wishListId);

        jdxHelper.insert(wishList, true); // no WishItems are there to be saved
        
        // Retrieve all WishItems independently but don't further retrieve WishItems of
        // the related WishLists (Directed Query)
        // This is an example of directed query where you can control the depth of the query operation.
        List ignoreList = new ArrayList();
        ignoreList.add(wishListClassName); // the containing class name
        ignoreList.add("items");  // the name of the attribute to be ignored
        List queryOption1 = new ArrayList();
        queryOption1.add(JDXS.IGNORE);
        queryOption1.add(ignoreList);
        List queryDetails = new ArrayList();
        queryDetails.add(queryOption1);

        wishItems = jdxHelper.getObjects(wishItemClassName, null, JDXS.ALL, JDXS.FLAG_DEEP, queryDetails);
        JXUtilities.printQueryResults(wishItems);
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
