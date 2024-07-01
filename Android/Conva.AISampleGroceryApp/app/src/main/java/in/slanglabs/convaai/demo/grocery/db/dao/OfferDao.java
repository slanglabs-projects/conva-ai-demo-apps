package in.slanglabs.convaai.demo.grocery.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

import in.slanglabs.convaai.demo.grocery.Model.Offer;
import in.slanglabs.convaai.demo.grocery.Model.OfferItemCart;

@Dao
public interface OfferDao {

    @Transaction
    @Query("SELECT * FROM offers")
    LiveData<List<OfferItemCart>> getOffersAndItems();

    @Query("DELETE FROM offers")
    public void removeAllOffers();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(List<Offer> offers);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Offer offer);

    @Transaction
    @Query("SELECT * FROM offers WHERE itemId=:itemId")
    Offer getOfferByItemId(int itemId);

}
