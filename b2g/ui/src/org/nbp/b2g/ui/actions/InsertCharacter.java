package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

public class InsertCharacter extends InsertCharacterAction {
  @Override
  public boolean insertCharacter (char character) {
    ModifierAction control = ControlModifier.getControlModifier();
    InputService service = InputService.getInputService();

    if (service != null) {
      char value = character;

      if (control != null) {
        if (control.getState()) {
          if ((value >= 0X40) && (value <= 0X7E)) {
            value &= 0X1F;
          } else if (value == 0X3F) {
            value |= 0X40;
          } else {
            ApplicationUtilities.beep();
            return false;
          }
        }
      }

      if (service.insert(value)) {
        return true;
      }
    }

    return false;
  }

  public InsertCharacter () {
    super();
  }
}
