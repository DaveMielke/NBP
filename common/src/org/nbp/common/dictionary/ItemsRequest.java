package org.nbp.common.dictionary;

import android.util.Log;

public abstract class ItemsRequest extends CommandRequest implements ItemsHandler {
  private final static String LOG_TAG = ItemsRequest.class.getName();

  protected abstract int getResponseCode ();

  private final ItemList items = new ItemList();

  protected ItemsRequest (String... arguments) {
    super(arguments);
  }

  public final ItemList getItems () {
    return items;
  }

  @Override
  public void handleItems (ItemList items) {
  }

  @Override
  protected final void handleResult () {
    handleItems(getItems());
  }

  @Override
  public boolean handleResponse (int code, DictionaryOperands operands) {
    if (code != getResponseCode()) {
      return super.handleResponse(code, operands);
    }

    for (String item : getTextAsList()) {
      try {
        DictionaryOperands parameters = new DictionaryOperands(item);

        if (parameters.isEmpty()) throw new OperandException("missing item name");
        String name = parameters.removeFirst();

        if (parameters.isEmpty()) throw new OperandException("missing item Description");
        String description = parameters.removeFirst();

        items.add(name, description);
      } catch (OperandException exception) {
        Log.w(LOG_TAG, exception.getMessage());
      }
    }

    return false;
  }
}
