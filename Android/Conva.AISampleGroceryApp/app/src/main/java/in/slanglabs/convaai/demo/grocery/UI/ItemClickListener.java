package in.slanglabs.convaai.demo.grocery.UI;


import in.slanglabs.convaai.demo.grocery.Model.Item;

public interface ItemClickListener {
    default void addItem(int position) {
    }

    default void removeItem(int position) {
    }

    default void itemClicked(int position) {
    }

    default void itemClicked(Item item) {
    }
}
