package org.nbp.calculator;

import java.util.Collections;
import java.util.Collection;

import java.util.List;
import java.util.ArrayList;

import java.util.Set;
import java.util.HashSet;

import org.nbp.common.CommonActivity;
import org.nbp.common.CommonUtilities;
import org.nbp.common.Timeout;
import org.nbp.common.Tones;

import org.nbp.common.CharacterUtilities;
import org.nbp.common.OnTextEditedListener;

import android.util.Log;

import android.app.Activity;
import android.os.Bundle;
import android.os.Build;

import android.view.View;
import android.view.ViewGroup;
import android.view.KeyEvent;

import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.CompoundButton;

import android.text.InputFilter;
import android.text.Spanned;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.SuperscriptSpan;
import android.text.style.RelativeSizeSpan;

public class CalculatorActivity extends CommonActivity {
  private final static String LOG_TAG = CalculatorActivity.class.getName();

  private final View findView (DialogInterface dialog, int id) {
    return ((AlertDialog)dialog).findViewById(id);
  }

  private final void setClickListener (int id, View.OnClickListener listener) {
    View view = findViewById(id);
    view.setOnClickListener(listener);
  }

  private final void setLongClickListener (int id, View.OnLongClickListener listener) {
    View view = findViewById(id);
    view.setOnLongClickListener(listener);
  }

  private final static Object EXPRESSION_LOCK = new Object();
  private EditText expressionView;
  private TextView resultView;
  private AbstractNumber resultValue;

  private final void setFocusToExpression () {
    expressionView.requestFocus();
  }

  private final void setFocusToResult () {
    resultView.requestFocus();
  }

  private Button leftButton;
  private Button rightButton;
  private Button backspaceButton;
  private Button deleteButton;

  private final void setExpressionNavigationStates () {
    synchronized (EXPRESSION_LOCK) {
      {
        int end = expressionView.getSelectionEnd();
        boolean enabled = end > 0;

        leftButton.setEnabled(enabled);
        backspaceButton.setEnabled(enabled);
      }

      {
        int length = expressionView.length();
        int start = expressionView.getSelectionStart();
        boolean enabled = start < length;

        rightButton.setEnabled(enabled);
        deleteButton.setEnabled(enabled);
      }
    }
  }

  private Button upButton;
  private Button downButton;

  private final void setHistoryNavigationStates () {
    upButton.setEnabled(!History.atStart());
    downButton.setEnabled(!History.atEnd());
  }

  private final void saveExpression () {
    synchronized (EXPRESSION_LOCK) {
      SavedSettings.set(SavedSettings.EXPRESSION, expressionView.getText().toString());
      SavedSettings.set(SavedSettings.START, expressionView.getSelectionStart());
      SavedSettings.set(SavedSettings.END, expressionView.getSelectionEnd());
    }
  }

  private final Timeout expressionSaveDelay = new Timeout(1000, "expression-save-delay") {
    @Override
    public void run () {
      saveExpression();
    }
  };

  private final void restoreExpression () {
    expressionSaveDelay.cancel();

    synchronized (EXPRESSION_LOCK) {
      String expression = SavedSettings.get(SavedSettings.EXPRESSION, "");
      expressionView.setText(expression);

      int start = SavedSettings.get(SavedSettings.START, -1);
      int end = SavedSettings.get(SavedSettings.END, -1);
      int length = expression.length();

      if ((0 <= start) && (start <= end) && (end <= length)) {
        expressionView.setSelection(start, end);
      } else {
        expressionView.setSelection(length);
      }
    }
  }

  private final boolean evaluateExpression (boolean showError) {
    expressionSaveDelay.start();

    synchronized (EXPRESSION_LOCK) {
      ExpressionEvaluator evaluator = SavedSettings.getCalculatorMode().getEvaluator();

      try {
        try {
          String expression = expressionView.getText().toString();
          evaluator.evaluateExpression(expression);
          resultValue = evaluator.getResult();
          resultView.setText(resultValue.format());
          return true;
        } catch (NoExpressionException exception) {
          resultValue = null;
          resultView.setText("");
        }
      } catch (ExpressionException exception) {
        if (showError) {
          resultValue = null;
          resultView.setText(exception.getMessage());
          expressionView.setSelection(exception.getLocation());
        } else {
          StringBuilder sb = new StringBuilder();

          {
            int level = evaluator.getLevel();

            while (level > 0) {
              sb.append('(');
              level -= 1;
            }
          }

          {
            AbstractNumber result = evaluator.getResult();

            if (result == null) {
              result = resultValue;
              sb.append('?');
            } else {
              resultValue = result;
            }

            if (result != null) sb.append(result.format());
          }

          resultView.setText(sb.toString());
        }
      }
    }

    return false;
  }

  private class OnExpressionEditedListener extends OnTextEditedListener {
    public OnExpressionEditedListener () {
      super(expressionView);
    }

    private boolean DO_NOT_EVALUATE = false;

    @Override
    public void onTextEdited (boolean isDifferent) {
      if (DO_NOT_EVALUATE) {
        DO_NOT_EVALUATE = false;
      } else {
        evaluateExpression(false);
        setExpressionNavigationStates();
      }
    }

    public final void clearWithoutEvaluating () {
      synchronized (EXPRESSION_LOCK) {
        DO_NOT_EVALUATE = true;
        expressionView.setText("");
      }
    }
  }

  private OnExpressionEditedListener onExpressionEditedListener = null;

  private final void finishExpression () {
    if (evaluateExpression(true)) {
      LastResult.singleton().setValue(resultValue);
      setFocusToResult();
      showKeypad(0);

      History.setLastEntry(expressionView);
      History.addLastEntry();
      setHistoryNavigationStates();

      onExpressionEditedListener.clearWithoutEvaluating();
    }
  }

  private final void setExpressionListener () {
    expressionView.setFilters(
      new InputFilter[] {
        new InputFilter() {
          private final boolean handleCharacter (char character) {
            switch (character) {
              case CharacterUtilities.CHAR_SOH: // control A
                performLongClick(R.id.button_angleUnit);
                return true;

              case CharacterUtilities.CHAR_ETX: // control C
                performClick(R.id.button_clearExpression);
                return true;

              case CharacterUtilities.CHAR_EOT: // control D
                performClick(R.id.button_down);
                return true;

              case CharacterUtilities.CHAR_ENQ: // control E
                performClick(R.id.button_erase);
                return true;

              case CharacterUtilities.CHAR_ACK: // control F
                performClick(R.id.button_function);
                return true;

              case CharacterUtilities.CHAR_VT: // control K
                setFocusToKeypad();
                return true;

              case CharacterUtilities.CHAR_CR: // control M
                performLongClick(R.id.button_calculatorMode);
                return true;

              case CharacterUtilities.CHAR_SO: // control N
                performLongClick(R.id.button_decimalNotation);
                return true;

              case CharacterUtilities.CHAR_DC3: // control S
                performClick(R.id.button_store);
                return true;

              case CharacterUtilities.CHAR_NAK: // control U
                performClick(R.id.button_up);
                return true;

              case CharacterUtilities.CHAR_SYN: // control V
                performClick(R.id.button_variable);
                return true;

              default:
                if (!Character.isISOControl(character)) return false;
                break;
            }

            Tones.beep();
            return true;
          }

          @Override
          public CharSequence filter (
            CharSequence src, int srcStart, int srcEnd,
            Spanned dst, int dstStart, int dstEnd
          ) {
            if ((srcStart + 1) == srcEnd) {
              if (handleCharacter(src.charAt(srcStart))) {
                return "";
              }
            }

            return null;
          }
        }
      }
    );

    onExpressionEditedListener = new OnExpressionEditedListener();
  }

  private final void setEnterKeyListener () {
    expressionView.setOnEditorActionListener(
      new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction (TextView view, int action, KeyEvent event) {
          if (event != null) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
              if (event.getAction() == KeyEvent.ACTION_DOWN) {
                finishExpression();
              }

              return true;
            }
          }

          return false;
        }
      }
    );
  }

  private final void insertExpressionText (String text) {
    synchronized (EXPRESSION_LOCK) {
      int start = expressionView.getSelectionStart();
      int end = expressionView.getSelectionEnd();

      expressionView.getText().replace(start, end, text);
      expressionView.setSelection(start+text.length());
    }
  }

  private final void performClick (int view) {
    findViewById(view).performClick();
  }

  private final void performLongClick (int view) {
    findViewById(view).performLongClick();
  }

  private Keypad[] activeKeypads;
  private int currentKeypad;

  private final Keypad getCurrentKeypad () {
    return activeKeypads[currentKeypad];
  }

  private final void showKeypad () {
    getCurrentKeypad().show();
  }

  private final void showKeypad (int index) {
    if (index != currentKeypad) {
      currentKeypad = index;
      showKeypad();
    }
  }

  private void setActiveKeypads () {
    currentKeypad = 0;
    activeKeypads = SavedSettings.getCalculatorMode().getActiveKeypads();
    showKeypad();

    {
      Button button = (Button)findViewById(R.id.button_alternateKeypad);
      button.setEnabled(activeKeypads.length > 1);
    }
  }

  private final void setFocusToKeypad () {
    getCurrentKeypad().focus();
  }

  private final static CharacterStyle exponentSpans[] = {
    new SuperscriptSpan(),
    new RelativeSizeSpan(0.6f)
  };

  private final void prepareKeypads () {
    Keypad.prepareKeypads(this);

    final View.OnClickListener insertTextListener = new View.OnClickListener() {
      @Override
      public void onClick (View view) {
        Button button = (Button)view;
        insertExpressionText(button.getTag().toString());
        setFocusToExpression();
        showKeypad(0);
      }
    };

    final View.OnClickListener finishExpressionListener = new View.OnClickListener() {
      @Override
      public void onClick (View view) {
        finishExpression();
      }
    };

    final Keypad.KeyHandler keyHandler = new Keypad.KeyHandler() {
      private final boolean setTag (TextView key, String text) {
        CharSequence tag = (CharSequence)key.getTag();

        if (tag == null) {
          tag = text;

          if (text.length() > 0) {
            if (ExpressionParser.isIdentifierCharacter(text.charAt(0), true)) {
              Function function = Functions.get(text);

              if (function != null) {
                tag = function.getName() + Function.ARGUMENT_PREFIX;
              }
            }
          }

          key.setTag(tag);
        } else if (tag.length() == 0) {
          return false;
        }

        return true;
      }

      @Override
      public void handleKey (TextView key) {
        String text = key.getText().toString();

        if (text.equals("=")) {
          key.setOnClickListener(finishExpressionListener);
        } else if (setTag(key, text)) {
          key.setOnClickListener(insertTextListener);
        }

        {
          int index = text.indexOf('^');

          if (index >= 0) {
            SpannableStringBuilder sb = new SpannableStringBuilder();
            sb.append(text.substring(0, index));
            sb.append(text.substring(index+1));
            int length = sb.length();

            for (CharacterStyle span : exponentSpans) {
              sb.setSpan(span, index, length, sb.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            key.setText(sb.subSequence(0, length));
          }
        }

        key.setBackgroundColor(0);
      }
    };

    final Keypad.KeypadHandler keypadHandler = new Keypad.KeypadHandler() {
      @Override
      public void handleKeypad (final Keypad keypad) {
        keypad.forEachKey(keyHandler);
      }
    };

    Keypad.forEachKeypad(keypadHandler);
    ConversionListener.register(this, insertTextListener);
  }

  private final void setClearExpressionButtonListener () {
    setClickListener(
      R.id.button_clearExpression,
      new View.OnClickListener() {
        @Override
        public void onClick (View view) {
          synchronized (EXPRESSION_LOCK) {
            expressionView.setText("");
            setFocusToExpression();
          }
        }
      }
    );

    setLongClickListener(
      R.id.button_clearExpression,
      new View.OnLongClickListener() {
        @Override
        public boolean onLongClick (View view) {
          synchronized (EXPRESSION_LOCK) {
            resultValue = null;
            resultView.setText("");

            expressionView.setText("");
            setFocusToExpression();
          }

          return true;
        }
      }
    );
  }

  private final void setAlternateKeypadButtonListener () {
    setClickListener(
      R.id.button_alternateKeypad,
      new View.OnClickListener() {
        @Override
        public void onClick (View view) {
          currentKeypad += 1;
          currentKeypad %= activeKeypads.length;

          showKeypad();
          setFocusToKeypad();
        }
      }
    );

    setLongClickListener(
      R.id.button_alternateKeypad,
      new View.OnLongClickListener() {
        @Override
        public boolean onLongClick (View view) {
          showKeypad(0);
          setFocusToKeypad();
          return true;
        }
      }
    );
  }

  private final void setCalculatorModeButtonListener () {
    new EnumerationButtonListener<CalculatorMode>(this,
      (Button)findViewById(R.id.button_calculatorMode), CalculatorMode.class,
      SavedSettings.CALCULATOR_MODE, DefaultSettings.CALCULATOR_MODE,

      new EnumerationButtonListener.ChangeHandler () {
        @Override
        public void handleChange (Enum newValue) {
          setActiveKeypads();

          synchronized (EXPRESSION_LOCK) {
            resultValue = null;
            expressionView.setText("");
          }
        }
      }
    );
  }

  private final void setDecimalNotationButtonListener () {
    new EnumerationButtonListener<DecimalNotation>(this,
      (Button)findViewById(R.id.button_decimalNotation), DecimalNotation.class,
      SavedSettings.DECIMAL_NOTATION, DefaultSettings.DECIMAL_NOTATION,

      new EnumerationButtonListener.ChangeHandler () {
        @Override
        public void handleChange (Enum newValue) {
          evaluateExpression(false);
        }
      }
    );
  }

  private final void setAngleUnitButtonListener () {
    new EnumerationButtonListener<AngleUnit>(this,
      (Button)findViewById(R.id.button_angleUnit), AngleUnit.class,
      SavedSettings.ANGLE_UNIT, DefaultSettings.ANGLE_UNIT,

      new EnumerationButtonListener.ChangeHandler () {
        @Override
        public void handleChange (Enum newValue) {
          evaluateExpression(false);
        }
      }
    );
  }

  private final void setLeftButtonListener () {
    setClickListener(
      R.id.button_left,
      new View.OnClickListener() {
        @Override
        public void onClick (View view) {
          synchronized (EXPRESSION_LOCK) {
            int start = expressionView.getSelectionStart();

            if (start > 0) {
              expressionView.setSelection(start-1);
              setExpressionNavigationStates();
            }
          }
        }
      }
    );

    setLongClickListener(
      R.id.button_left,
      new View.OnLongClickListener() {
        @Override
        public boolean onLongClick (View view) {
          synchronized (EXPRESSION_LOCK) {
            if (expressionView.getSelectionStart() > 0) {
              expressionView.setSelection(0);
              setExpressionNavigationStates();
            }
          }

          return true;
        }
      }
    );
  }

  private final void setRightButtonListener () {
    setClickListener(
      R.id.button_right,
      new View.OnClickListener() {
        @Override
        public void onClick (View view) {
          synchronized (EXPRESSION_LOCK) {
            int end = expressionView.getSelectionEnd();

            if (end < expressionView.length()) {
              expressionView.setSelection(end+1);
              setExpressionNavigationStates();
            }
          }
        }
      }
    );

    setLongClickListener(
      R.id.button_right,
      new View.OnLongClickListener() {
        @Override
        public boolean onLongClick (View view) {
          synchronized (EXPRESSION_LOCK) {
            int length = expressionView.length();

            if (expressionView.getSelectionEnd() < length) {
              expressionView.setSelection(length);
              setExpressionNavigationStates();
            }
          }

          return true;
        }
      }
    );
  }

  private final void setBackspaceButtonListener () {
    setClickListener(
      R.id.button_backspace,
      new View.OnClickListener() {
        @Override
        public void onClick (View view) {
          synchronized (EXPRESSION_LOCK) {
            int start = expressionView.getSelectionStart();
            int end = expressionView.getSelectionEnd();

            if (start == end) {
              if (start == 0) return;
              end = start;
              start -= 1;
            }

            expressionView.getText().delete(start, end);
            setExpressionNavigationStates();
          }
        }
      }
    );

    setLongClickListener(
      R.id.button_backspace,
      new View.OnLongClickListener() {
        @Override
        public boolean onLongClick (View view) {
          synchronized (EXPRESSION_LOCK) {
            int end = expressionView.getSelectionEnd();

            if (end > 0) {
              expressionView.getText().delete(0, end);
              setExpressionNavigationStates();
            }
          }

          return true;
        }
      }
    );
  }

  private final void setDeleteButtonListener () {
    setClickListener(
      R.id.button_delete,
      new View.OnClickListener() {
        @Override
        public void onClick (View view) {
          synchronized (EXPRESSION_LOCK) {
            int start = expressionView.getSelectionStart();
            int end = expressionView.getSelectionEnd();

            if (start == end) {
              if (end == expressionView.length()) return;
              end = start + 1;
            }

            expressionView.getText().delete(start, end);
            setExpressionNavigationStates();
          }
        }
      }
    );

    setLongClickListener(
      R.id.button_delete,
      new View.OnLongClickListener() {
        @Override
        public boolean onLongClick (View view) {
          synchronized (EXPRESSION_LOCK) {
            int start = expressionView.getSelectionStart();
            int length = expressionView.length();

            if (start < length) {
              expressionView.getText().delete(start, length);
              setExpressionNavigationStates();
            }
          }

          return true;
        }
      }
    );
  }

  private final boolean startHistoryNavigationUp () {
    if (History.atStart()) return false;
    if (History.atEnd()) History.setLastEntry(expressionView);
    return true;
  }

  private final boolean startHistoryNavigationDown () {
    if (History.atEnd()) return false;
    return true;
  }

  private final void finishHistoryNavigation () {
    History.Entry entry =
      History.atEnd()?
      History.getLastEntry():
      History.getCurrentEntry();

    CharSequence text = entry.getText();
    int start = entry.getSelectionStart();
    int end = entry.getSelectionEnd();

    expressionView.setText(text);
    expressionView.setSelection(start, end);

    evaluateExpression(true);
    setHistoryNavigationStates();
  }

  private final void setUpButtonListener () {
    setClickListener(
      R.id.button_up,
      new View.OnClickListener() {
        @Override
        public void onClick (View view) {
          synchronized (EXPRESSION_LOCK) {
            if (startHistoryNavigationUp()) {
              History.moveToPreviousEntry();
              finishHistoryNavigation();
            }
          }
        }
      }
    );

    setLongClickListener(
      R.id.button_up,
      new View.OnLongClickListener() {
        @Override
        public boolean onLongClick (View view) {
          synchronized (EXPRESSION_LOCK) {
            if (startHistoryNavigationUp()) {
              History.moveToFirstEntry();
              finishHistoryNavigation();
            }
          }

          return true;
        }
      }
    );
  }

  private final void setDownButtonListener () {
    setClickListener(
      R.id.button_down,
      new View.OnClickListener() {
        @Override
        public void onClick (View view) {
          synchronized (EXPRESSION_LOCK) {
            if (startHistoryNavigationDown()) {
              History.moveToNextEntry();
              finishHistoryNavigation();
            }
          }
        }
      }
    );

    setLongClickListener(
      R.id.button_down,
      new View.OnLongClickListener() {
        @Override
        public boolean onLongClick (View view) {
          synchronized (EXPRESSION_LOCK) {
            if (startHistoryNavigationDown()) {
              History.moveToLastEntry();
              finishHistoryNavigation();
            }
          }

          return true;
        }
      }
    );
  }

  private final String formatVariableLine (Variable variable) {
    StringBuilder sb = new StringBuilder();
    sb.append(variable.getName());
    sb.append(" = ");

    {
      AbstractNumber value = variable.getValue();
      sb.append((value != null)? value.format(): "?");
    }

    {
      String description = variable.getDescription();

      if (description != null) {
        sb.append(" (");
        sb.append(description);
        sb.append(')');
      }
    }

    return sb.toString();
  }

  private final String getVariableName (List<String> variables, int index) {
    String variable = variables.get(index);
    return variable.substring(0, variable.indexOf(' '));
  }

  private final List<String> getUserVariableLines () {
    List<String> variables = new ArrayList<String>();

    for (String name : Variables.getUserVariableNames()) {
      variables.add(formatVariableLine(Variables.get(name)));
    }

    Collections.sort(variables);
    return variables;
  }

  private final void setVariableButtonListener () {
    setClickListener(
      R.id.button_variable,
      new View.OnClickListener() {
        @Override
        public void onClick (View view) {
          AlertDialog.Builder builder = ApplicationUtilities.newAlertDialogBuilder(
            getActivity(), R.string.title_variable
          );

          final List<String> variables = getUserVariableLines();

          for (String name : Variables.getPredefinedVariableNames()) {
            variables.add(formatVariableLine(Variables.getPredefinedVariable(name)));
          }

          builder.setItems(
            ApplicationUtilities.toArray(variables),
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick (DialogInterface dialog, int index) {
                insertExpressionText(getVariableName(variables, index));
                setFocusToExpression();
              }
            }
          );

          builder.show();
        }
      }
    );
  }

  private final void setStoreButtonListener () {
    setClickListener(
      R.id.button_store,
      new View.OnClickListener() {
        @Override
        public void onClick (View view) {
          AlertDialog.Builder builder = ApplicationUtilities.newAlertDialogBuilder(
            getActivity(), R.string.title_store
          );

          final AbstractNumber result = resultValue;

          if (result == null) {
            builder.setMessage(R.string.error_no_result);
          } else {
            final List<String> variables = getUserVariableLines();

            if (variables.isEmpty()) {
              builder.setMessage(R.string.error_no_variables);
            } else {
              builder.setItems(
                ApplicationUtilities.toArray(variables),
                new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick (DialogInterface dialog, int index) {
                    Variables.set(getVariableName(variables, index), result);
                  }
                }
              );
            }

            builder.setPositiveButton(
              R.string.button_new,
              new DialogInterface.OnClickListener() {
                @Override
                public void onClick (DialogInterface dialog, int button) {
                  AlertDialog.Builder builder = ApplicationUtilities.newAlertDialogBuilder(
                    getActivity(), R.string.title_new, R.string.button_new
                  );

                  builder.setView(
                    getLayoutInflater().inflate(R.layout.variable_name, null)
                  );

                  builder.setPositiveButton(
                    R.string.button_store,
                    new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick (DialogInterface dialog, int button) {
                        EditText view = (EditText)findView(dialog, R.id.variable);
                        String name = view.getText().toString();
                        Variables.set(name, result);
                      }
                    }
                  );

                  AlertDialog alert = builder.create();
                  alert.show();

                  EditText variableName = (EditText)findView(alert, R.id.variable);
                  final Button storeButton = alert.getButton(alert.BUTTON_POSITIVE);
                  storeButton.setEnabled(false);

                  variableName.setFilters(
                    new InputFilter[] {
                      new InputFilter() {
                        @Override
                        public CharSequence filter (
                          CharSequence src, int srcStart, int srcEnd,
                          Spanned dst, int dstStart, int dstEnd
                        ) {
                          int dstIndex = dstStart;

                          for (int srcIndex=srcStart; srcIndex<srcEnd; srcIndex+=1) {
                            if (!ExpressionParser.isIdentifierCharacter(src.charAt(srcIndex), (dstIndex == 0))) {
                              Tones.beep();
                              return "";
                            }

                            dstIndex += 1;
                          }

                          boolean isNew = false;
                          StringBuilder name = new StringBuilder(dst.toString());

                          name.delete(dstStart, dstEnd);
                          name.insert(dstStart, src.subSequence(srcStart, srcEnd));

                          if (name.length() > 0) {
                            if (ExpressionParser.isIdentifierCharacter(name.charAt(0), true)) {
                              if (Variables.get(name.toString()) == null) {
                                isNew = true;
                              }
                            }
                          }

                          storeButton.setEnabled(isNew);
                          return null;
                        }
                      }
                    }
                  );
                }
              }
            );
          }

          builder.show();
        }
      }
    );
  }

  private final void setEraseButtonListener () {
    setClickListener(
      R.id.button_erase,
      new View.OnClickListener() {
        @Override
        public void onClick (View view) {
          AlertDialog.Builder builder = ApplicationUtilities.newAlertDialogBuilder(
            getActivity(), R.string.title_erase
          );

          final List<String> variables = getUserVariableLines();

          if (variables.isEmpty()) {
            builder.setMessage(R.string.error_no_variables);
          } else {
            builder.setItems(
              ApplicationUtilities.toArray(variables),
              new DialogInterface.OnClickListener() {
                @Override
                public void onClick (DialogInterface dialog, int index) {
                  Variables.removeUserVariable(getVariableName(variables, index));
                }
              }
            );
          }

          builder.show();
        }
      }
    );

    setLongClickListener(
      R.id.button_erase,
      new View.OnLongClickListener() {
        @Override
        public boolean onLongClick (View view) {
          AlertDialog.Builder builder = ApplicationUtilities.newAlertDialogBuilder(
            getActivity(), R.string.title_eraseMultiple
          );

          final List<String> variables = getUserVariableLines();

          if (variables.isEmpty()) {
            builder.setMessage(R.string.error_no_variables);
          } else {
            final Set<String> names = new HashSet<String>();

            builder.setMultiChoiceItems(
              ApplicationUtilities.toArray(variables),
              null,
              new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick (DialogInterface dialog, int index, boolean isChecked) {
                  String name = getVariableName(variables, index);

                  if (isChecked) {
                    names.add(name);
                  } else {
                    names.remove(name);
                  }
                }
              }
            );

            builder.setPositiveButton(
              R.string.button_erase,
              new DialogInterface.OnClickListener() {
                @Override
                public void onClick (DialogInterface dialog, int button) {
                  for (String name : names) {
                    Variables.removeUserVariable(name);
                  }
                }
              }
            );
          }

          builder.show();
          return true;
        }
      }
    );
  }

  private final String formatFunctionLine (String name, Function function) {
    String line = function.getCall();

    String summary = function.getSummary();
    if (summary != null) line += ": " + summary;

    return line;
  }

  private final String getFunctionCall (List<String> functions, int index) {
    String function = functions.get(index);
    return function.substring(0, (function.indexOf(Function.ARGUMENT_SUFFIX) + 1));
  }

  private final List<String> getFunctionLines () {
    List<String> functions = new ArrayList<String>();

    for (String name : Functions.getNames()) {
      Function function = Functions.get(name);
      if (function == null) continue;
      if (function.getHideFromListing()) continue;
      functions.add(formatFunctionLine(name, function));
    }

    Collections.sort(functions);
    return functions;
  }

  private final void setFunctionButtonListener () {
    setClickListener(
      R.id.button_function,
      new View.OnClickListener() {
        @Override
        public void onClick (View view) {
          AlertDialog.Builder builder = ApplicationUtilities.newAlertDialogBuilder(
            getActivity(), R.string.title_function
          );

          final List<String> functions = getFunctionLines();

          if (functions.isEmpty()) {
            builder.setMessage(R.string.error_no_functions);
          } else {
            builder.setItems(
              ApplicationUtilities.toArray(functions),
              new DialogInterface.OnClickListener() {
                @Override
                public void onClick (DialogInterface dialog, int index) {
                  insertExpressionText(getFunctionCall(functions, index));
                  setFocusToExpression();
                }
              }
            );
          }

          builder.show();
        }
      }
    );
  }

  private final void configureViews (View view) {
    if (view != null) {
      if (view instanceof ViewGroup) {
        ViewGroup group = (ViewGroup)view;
        int count = group.getChildCount();

        for (int index=0; index<count; index+=1) {
          configureViews(group.getChildAt(index));
        }
      } else if (view instanceof Button) {
        Button button = (Button)view;
        button.setAllCaps(false);
      }
    }
  }

  private final void configureViews () {
    configureViews(findViewById(R.id.root));
  }

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.calculator);
    configureViews();

    expressionView = (EditText)findViewById(R.id.expression);
    resultView = (TextView)findViewById(R.id.result);
    resultValue = null;

    leftButton = (Button)findViewById(R.id.button_left);
    rightButton = (Button)findViewById(R.id.button_right);
    backspaceButton = (Button)findViewById(R.id.button_backspace);
    deleteButton = (Button)findViewById(R.id.button_delete);
    upButton = (Button)findViewById(R.id.button_up);
    downButton = (Button)findViewById(R.id.button_down);

    if (CommonUtilities.haveAndroidSDK(Build.VERSION_CODES.LOLLIPOP)) {
      expressionView.setShowSoftInputOnFocus(false);
    }

    prepareKeypads();
    setClearExpressionButtonListener();
    setAlternateKeypadButtonListener();
    setCalculatorModeButtonListener();
    setDecimalNotationButtonListener();
    setAngleUnitButtonListener();

    setExpressionListener();
    setEnterKeyListener();
    restoreExpression();
    setFocusToExpression();

    setLeftButtonListener();
    setRightButtonListener();
    setBackspaceButtonListener();
    setDeleteButtonListener();
    setExpressionNavigationStates();

    setUpButtonListener();
    setDownButtonListener();
    setHistoryNavigationStates();

    setVariableButtonListener();
    setFunctionButtonListener();
    setStoreButtonListener();
    setEraseButtonListener();
  }

  @Override
  protected void onPause () {
    try {
      saveExpression();
    } finally {
      super.onPause();
    }
  }
}
