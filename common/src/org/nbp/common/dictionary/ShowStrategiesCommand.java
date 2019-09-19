package org.nbp.common.dictionary;

public class ShowStrategiesCommand extends ItemsRequest {
  public ShowStrategiesCommand () {
    super("show", "strategies");
  }

  @Override
  public boolean handleResponse (int code, DictionaryOperands operands) {
    switch (code) {
      case ResponseCodes.NO_STRATEGIES:
        return true;

      case ResponseCodes.BEGIN_STRATEGY_LIST:
        saveItems();
        return false;

      default:
        return super.handleResponse(code, operands);
    }
  }
}
