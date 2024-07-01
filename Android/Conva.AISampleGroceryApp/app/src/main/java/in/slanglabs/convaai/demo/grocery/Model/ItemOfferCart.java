package in.slanglabs.convaai.demo.grocery.Model;

import androidx.room.Embedded;
import androidx.room.Relation;

public class ItemOfferCart {
    @Embedded
    public Item item;
    @Relation(
            parentColumn = "itemId",
            entityColumn = "itemId",
            entity = Offer.class
    )
    public Offer offer;
    @Relation(
            parentColumn = "itemId",
            entityColumn = "itemId"
    )
    public CartItem cart;

    public ItemOfferCart(Item item, Offer offer, CartItem cart) {
        this.item = item;
        this.offer = offer;
        this.cart = cart;
    }
}
