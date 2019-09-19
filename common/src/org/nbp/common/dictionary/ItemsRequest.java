package org.nbp.common.dictionary;

import android.util.Log;

public abstract class ItemsRequest extends CommandRequest implements ItemsHandler {
  private final static String LOG_TAG = ItemsRequest.class.getName();

  private final ItemList savedItems = new ItemList();

  protected ItemsRequest (String... arguments) {
    super(arguments);
  }

  public final ItemList getItems () {
    return savedItems;
  }

  @Override
  public void handleItems (ItemList items) {
  }

  @Override
  protected final void handleResult () {
    handleItems(getItems());
  }

  protected final void saveItems () {
    for (String item : getTextAsList()) {
      try {
        DictionaryOperands parameters = new DictionaryOperands(item);

        if (parameters.isEmpty()) throw new OperandException("missing item name");
        String name = parameters.removeFirst();

        if (parameters.isEmpty()) throw new OperandException("missing item Description");
        String description = parameters.removeFirst();

        savedItems.add(name, description);
      } catch (OperandException exception) {
        Log.w(LOG_TAG, exception.getMessage());
      }
    }
  }
}
