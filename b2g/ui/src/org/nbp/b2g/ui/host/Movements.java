package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.host.actions.*;
import org.nbp.b2g.ui.*;

import android.view.accessibility.AccessibilityNodeInfo;
import android.os.Bundle;

public abstract class Movements {
  private Movements () {
  }

  public enum Action {
    FORWARD,
    BACKWARD,
    SHOW;
  }

  public abstract static class Movement {
    private final String movementLabel;

    protected Movement (String label) {
      movementLabel = label;
    }

    protected abstract int getForwardAction ();
    protected abstract int getBackwardAction ();
    protected abstract void setArgument (Bundle arguments);

    private final boolean performAction (AccessibilityNodeInfo node, int action) {
      Bundle arguments = new Bundle();
      setArgument(arguments);
      return node.performAction(action, arguments);
    }

    public final boolean perform (AccessibilityNodeInfo node, Action action) {
      switch (action) {
        case FORWARD:
          return performAction(node, getForwardAction());

        case BACKWARD:
          return performAction(node, getBackwardAction());

        case SHOW:
          ApplicationUtilities.message(movementLabel);
          return true;
      }

      return false;
    }
  }

  public static class GranularityMovement extends Movement {
    private final int argumentValue;

    public GranularityMovement (int value, String label) {
      super(label);
      argumentValue = value;
    }

    @Override
    protected final int getForwardAction () {
      return AccessibilityNodeInfo.ACTION_NEXT_AT_MOVEMENT_GRANULARITY;
    }

    @Override
    protected final int getBackwardAction () {
      return AccessibilityNodeInfo.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY;
    }

    @Override
    protected final void setArgument (Bundle arguments) {
      arguments.putInt(AccessibilityNodeInfo.ACTION_ARGUMENT_MOVEMENT_GRANULARITY_INT, argumentValue);
    }
  }

  public static class ElementMovement extends Movement {
    private final String argumentValue;

    public ElementMovement (String value, String label) {
      super(label);
      argumentValue = value;
    }

    @Override
    protected final int getForwardAction () {
      return AccessibilityNodeInfo.ACTION_NEXT_HTML_ELEMENT;
    }

    @Override
    protected final int getBackwardAction () {
      return AccessibilityNodeInfo.ACTION_PREVIOUS_HTML_ELEMENT;
    }

    @Override
    protected final void setArgument (Bundle arguments) {
      arguments.putString(AccessibilityNodeInfo.ACTION_ARGUMENT_HTML_ELEMENT_STRING, argumentValue);
    }
  }

  public final static Movement CHARACTER = new GranularityMovement(
    AccessibilityNodeInfo.MOVEMENT_GRANULARITY_CHARACTER, "Character"
  );

  public final static Movement WORD = new GranularityMovement(
    AccessibilityNodeInfo.MOVEMENT_GRANULARITY_WORD, "Word"
  );

  public final static Movement LINE = new GranularityMovement(
    AccessibilityNodeInfo.MOVEMENT_GRANULARITY_LINE, "Line"
  );

  public final static Movement PARAGRAPH = new GranularityMovement(
    AccessibilityNodeInfo.MOVEMENT_GRANULARITY_PARAGRAPH, "Paragraph"
  );

  public final static Movement PAGE = new GranularityMovement(
    AccessibilityNodeInfo.MOVEMENT_GRANULARITY_PAGE, "Page"
  );

  public final static Movement PARENT_OR_FIRST_CHILD = new ElementMovement(
    "PARENT_FIRST_CHILD", "Parent / First Child"
  );

  public final static Movement SIBLING = new ElementMovement(
    "SIBLING", "Sibling"
  );

  public final static Movement HEADING_LEVEL_1 = new ElementMovement(
    "H1", "Level 1 Heading"
  );

  public final static Movement HEADING_LEVEL_2 = new ElementMovement(
    "H2", "Level 2 Heading"
  );

  public final static Movement HEADING_LEVEL_3 = new ElementMovement(
    "H3", "Level 3 Heading"
  );

  public final static Movement HEADING_LEVEL_4 = new ElementMovement(
    "H4", "Level 4 Heading"
  );

  public final static Movement HEADING_LEVEL_5 = new ElementMovement(
    "H5", "Level 5 Heading"
  );

  public final static Movement HEADING_LEVEL_6 = new ElementMovement(
    "H6", "Level 6 Heading"
  );

  public final static Movement ARTICLE = new ElementMovement(
    "ARTICLE", "Article"
  );

  public final static Movement BUTTON = new ElementMovement(
    "BUTTON", "Button"
  );

  public final static Movement COMBOBOX = new ElementMovement(
    "COMBOBOX", "Combo Box"
  );

  public final static Movement DOCUMENT = new ElementMovement(
    "MAIN", "Document"
  );

  public final static Movement EDITABLE_INPUT = new ElementMovement(
    "TEXT_FIELD", "Editable Input"
  );

  public final static Movement FORM_FIELD = new ElementMovement(
    "CONTROL", "Form Field"
  );

  public final static Movement GRAPHIC = new ElementMovement(
    "GRAPHIC", "Graphic"
  );

  public final static Movement HEADING = new ElementMovement(
    "HEADING", "Heading"
  );

  public final static Movement LIST_ITEM = new ElementMovement(
    "LIST_ITEM", "List Item"
  );

  public final static Movement LINK = new ElementMovement(
    "LINK", "Link"
  );

  public final static Movement LANDMARK = new ElementMovement(
    "LANDMARK", "Land Mark"
  );

  public final static Movement LIST = new ElementMovement(
    "LIST", "List"
  );

  public final static Movement RADIO_BUTTON = new ElementMovement(
    "RADIO", "Radio Button"
  );

  public final static Movement TABLE = new ElementMovement(
    "TABLE", "Table"
  );

  public final static Movement UNVISITED_LINK = new ElementMovement(
    "UNVISITED_LINK", "Unvisited Link"
  );

  public final static Movement VISITED_LINK = new ElementMovement(
    "VISITED_LINK", "Visited Link"
  );

  public final static Movement CHECKBOX = new ElementMovement(
    "CHECKBOX", "Check Box"
  );
}
