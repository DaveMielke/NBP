package org.nbp.common.dictionary;

import android.util.Log;

public abstract class DescriptionsRequest extends CommandRequest implements DescriptionsHandler {
  private final static String LOG_TAG = DescriptionsRequest.class.getName();

  protected abstract int getResponseCode ();

  private final DescriptionList descriptions = new DescriptionList();

  protected DescriptionsRequest (String... arguments) {
    super(arguments);
  }

  public final DescriptionList getDescriptions () {
    return descriptions;
  }

  @Override
  public void handleDescriptions (DescriptionList descriptions) {
  }

  @Override
  protected final void handleResult () {
    handleDescriptions(getDescriptions());
  }

  @Override
  public boolean handleResponse (int code, DictionaryOperands operands) {
    final int begin = getResponseCode();

    if (code == begin) {
      for (String item : getTextAsList()) {
        try {
          DictionaryOperands parameters = new DictionaryOperands(item);

          if (parameters.isEmpty()) throw new OperandException("missing item name");
          String name = parameters.removeFirst();

          if (parameters.isEmpty()) throw new OperandException("missing item Description");
          String text = parameters.removeFirst();

          descriptions.add(name, text);
        } catch (OperandException exception) {
          Log.w(LOG_TAG, exception.getMessage());
        }
      }

      return false;
    }

    return super.handleResponse(code, operands);
  }
}
