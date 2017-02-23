package org.nbp.editor;
// saveAs
// confirmFormat
// menuAction_open
// restoreFile

import java.io.File;
import java.util.ArrayList;

import org.nbp.common.CommonActivity;
import org.nbp.common.AlertDialogBuilder;
import org.nbp.common.SpeechToText;

import org.nbp.common.FileFinder;
import org.nbp.common.FileSystems;

import org.nbp.common.OutgoingMessage;
import android.net.Uri;

import android.util.Log;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.Intent;

import android.content.ClipboardManager;
import android.content.ClipData;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.AsyncTask;

import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;

import android.text.InputFilter;
import android.text.Editable;

import android.text.Spanned;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;

import org.nbp.common.HighlightSpans;
import android.text.style.CharacterStyle;

import android.view.Menu;
import android.view.MenuItem;

import android.app.AlertDialog;
import android.content.DialogInterface;

public class EditorActivity extends CommonActivity {
  private final static String LOG_TAG = EditorActivity.class.getName();

  private File filesDirectory;
  private SharedPreferences prefs;

  private final static String PREF_CHECKPOINT_PREFIX = "checkpoint-";
  private final static String PREF_CHECKPOINT_NAME = PREF_CHECKPOINT_PREFIX + "name";
  private final static String PREF_CHECKPOINT_PATH = PREF_CHECKPOINT_PREFIX + "path";
  private final static String PREF_CHECKPOINT_TYPE = PREF_CHECKPOINT_PREFIX + "type";
  private final static String PREF_CHECKPOINT_WRITABLE = PREF_CHECKPOINT_PREFIX + "writable";
  private final static String PREF_CHECKPOINT_START = PREF_CHECKPOINT_PREFIX + "start";
  private final static String PREF_CHECKPOINT_END = PREF_CHECKPOINT_PREFIX + "end";
  private final static String PREF_CHECKPOINT_SPANS = PREF_CHECKPOINT_PREFIX + "spans";

  private final void removePreferenceKeys (SharedPreferences.Editor editor, String keyPrefix) {
    for (String key : prefs.getAll().keySet()) {
      if (key.startsWith(keyPrefix)) editor.remove(key);
    }
  }

  private EditText editArea = null;
  private TextView uriView = null;
  private ContentHandle contentHandle = null;
  private boolean hasChanged = false;

  private final void showActivityResultCode (int code) {
  }

  private final AlertDialog.Builder newAlertDialogBuilder (int... subtitles) {
    return new AlertDialogBuilder(this, subtitles)
              .setCancelable(false)
              ;
  }

  private final ClipboardManager getClipboard () {
    return (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
  }

  public static CharSequence getText (ClipData clip) {
    int count = clip.getItemCount();

    for (int index=0; index<count; index+=1) {
      ClipData.Item item = clip.getItemAt(index);
      if (item == null) continue;

      CharSequence text = item.getText();
      if (text != null) return text;
    }

    return null;
  }

  private static CharSequence getText (ClipboardManager clipboard) {
    synchronized (clipboard) {
      ClipData clip = clipboard.getPrimaryClip();
      if (clip != null) return getText(clip);
    }

    return null;
  }

  private final static boolean verifyTextRange (int start, int end, int length) {
    return (0 <= start) && (start <= end) && (end <= length);
  }

  private final boolean verifyTextRange (int start, int end) {
    return verifyTextRange(start, end, editArea.length());
  }

  private void setEditorContent (ContentHandle handle) {
    synchronized (this) {
      String path;

      if (handle != null) {
        path = handle.getNormalizedString();
      } else {
        path = getString(R.string.hint_new_file);
      }

      contentHandle = handle;
      uriView.setText(path);
    }
  }

  private void setEditorContent (ContentHandle handle, CharSequence content) {
    synchronized (this) {
      setEditorContent(handle);
      editArea.setText(content);
      hasChanged = false;
    }
  }

  private void setEditorContent () {
    setEditorContent(null, "");
  }

  private final void saveFile (File file, final Runnable onSaved) {
    CharSequence content;

    synchronized (this) {
      if (file == null) {
        String detail;

        if (contentHandle == null) {
          detail = ApplicationContext.getString(R.string.hint_new_file);
        } else if ((file = contentHandle.getFile()) != null) {
          detail = null;
        } else {
          String name = contentHandle.getProvidedName();

          if (name != null) {
            File directory = getDocumentsDirectory();

            if (directory != null) {
              file = new File(directory, name);
            }
          }

          detail = (file != null)? null: contentHandle.getNormalizedString();
        }

        if (detail != null) {
          showMessage(R.string.message_save_no, detail);
          return;
        }
      }

      content = editArea.getText();
      hasChanged = false;
    }

    final File f = file;
    final CharSequence output = content;

    new AsyncTask<Void, Void, Void>() {
      AlertDialog dialog = null;

      @Override
      protected void onPreExecute () {
        if (haveWindow()) {
          dialog = newAlertDialogBuilder(R.string.message_writing_content)
            .setMessage(f.getAbsolutePath())
            .create();

          dialog.show();
        }
      }

      @Override
      public Void doInBackground (Void... arguments) {
        Content.writeFile(f, output);
        return null;
      }

      @Override
      public void onPostExecute (Void result) {
        if (dialog != null) dialog.dismiss();

        if (!f.getParentFile().equals(filesDirectory)) {
          showMessage(R.string.message_file_saved, f.getAbsolutePath(), onSaved);
        } else {
          run(onSaved);
        }
      }
    }.execute();
  }

  private final void saveFile (Runnable onSaved) {
    saveFile(null, onSaved);
  }

  private final void saveFile (File file) {
    saveFile(file, null);
  }

  private final void saveFile () {
    saveFile(null, null);
  }

  private final void testHasChanged (final Runnable onSaved) {
    if (hasChanged) {
      OnDialogClickListener positiveListener = new OnDialogClickListener() {
        @Override
        public void onClick () {
          if (contentHandle != null) {
            saveFile(onSaved);
          } else {
            findFile(true, null,
              new FileFinder.FileHandler() {
                @Override
                public void handleFile (File file) {
                  if (file != null) saveFile(file, onSaved);
                }
              }
            );
          }
        }
      };

      OnDialogClickListener negativeListener = new OnDialogClickListener() {
        @Override
        public void onClick () {
          run(onSaved);
        }
      };

      newAlertDialogBuilder(R.string.changed_title)
        .setMessage(R.string.changed_message)
        .setPositiveButton(R.string.changed_button_positive, positiveListener)
        .setNeutralButton(R.string.changed_button_neutral, null)
        .setNegativeButton(R.string.changed_button_negative, negativeListener)
        .show();
    } else {
      run(onSaved);
    }
  }

  private final boolean loadContent (final ContentHandle handle, final Runnable onLoaded) {
    new AsyncTask<Void, Void, CharSequence>() {
      AlertDialog dialog;

      @Override
      protected void onPreExecute () {
        dialog = newAlertDialogBuilder(R.string.message_reading_content)
          .setMessage(handle.getNormalizedString())
          .create();

        dialog.show();
      }

      @Override
      protected CharSequence doInBackground (Void... arguments) {
        try {
          final SpannableStringBuilder input = new SpannableStringBuilder();
          Content.readContent(handle, input);
          return input.subSequence(0, input.length());
        } catch (OutOfMemoryError error) {
          System.gc();
          return null;
        }
      }

      @Override
      protected void onPostExecute (CharSequence content) {
        if (content != null) {
          final int maximum = 150000;
          int length = content.length();

          if (maximum < length) content = content.subSequence(0, maximum);
          setEditorContent(handle, content);

          run(onLoaded);
        }

        dialog.dismiss();
      }
    }.execute();

    return true;
  }

  private final boolean loadContent (ContentHandle handle) {
    return loadContent(handle, null);
  }

  private final File getDocumentsDirectory () {
    File directory = Environment.getExternalStoragePublicDirectory("Documents");

    if (!directory.exists()) {
      if (!directory.mkdir()) {
        Log.w(LOG_TAG, ("unable to create documents directory: " + directory.getAbsolutePath()));
        return null;
      }
    }

    return directory;
  }

  private final void addRootLocation (FileFinder.Builder builder, String name, File location) {
    builder.addRootLocation(FileSystems.makeLabel(name, location), location);
  }

  private final void addRootLocations (FileFinder.Builder builder) {
    if (contentHandle != null) {
      File file = contentHandle.getFile();

      if (file != null) {
        addRootLocation(builder, "current", file.getParentFile());
      }
    }

    {
      File directory = getDocumentsDirectory();

      if (directory != null) {
        addRootLocation(builder, "documents", directory);
      }
    }

    for (String label : FileSystems.getRemovableLabels()) {
      builder.addRootLocation(label);
    }
  }

  private final void findFile (
    boolean forWriting,
    String[] extensions,
    FileFinder.FileHandler handler
  ) {
    int title = forWriting?
                R.string.menu_options_saveAs:
                R.string.menu_options_open;

    FileFinder.Builder builder = new FileFinder
      .Builder(this)
      .setUserTitle(getString(title))
      .setForWriting(forWriting)
      ;

    if (extensions != null) {
      for (String extension : extensions) {
        builder.addFileExtension(extension);
      }
    }

    if (contentHandle != null) {
      File file = contentHandle.getFile();
      if (file != null) builder.setFileName(file.getName());
    }

    addRootLocations(builder);
    builder.find(handler);
  }

  private final void saveAs (Content.FormatDescriptor format) {
    findFile(true,
      ((format != null)? format.getFileExtensions(): null),
      new FileFinder.FileHandler() {
        @Override
        public void handleFile (final File file) {
          if (file != null) {
            saveFile(file,
              new Runnable() {
                @Override
                public void run () {
                  setEditorContent(new ContentHandle(file, null, true));
                }
              }
            );
          }
        }
      }
    );
  }

  private final void selectFormat () {
    final Content.FormatDescriptor[] formats = Content.getFormatDescriptors();
    String[] items = new String[1 + formats.length];

    {
      int count = 0;
      items[count++] = getString(R.string.format_select_all);

      for (Content.FormatDescriptor format : formats) {
        items[count++] = format.getSelectorLabel();
      }
    }

    DialogInterface.OnClickListener itemListener = new DialogInterface.OnClickListener() {
      @Override
      public void onClick (DialogInterface dialog, int index) {
        saveAs((index == 0)? null: formats[index-1]);
      }
    };

    newAlertDialogBuilder(R.string.format_request_select)
      .setItems(items, itemListener)
      .setNegativeButton(R.string.action_cancel, null)
      .show();
  }

  private final void confirmFormat () {
    File file = (contentHandle != null)? contentHandle.getFile(): null;

    if (file == null) {
      selectFormat();
    } else {
      String extension = Content.getFileExtension(file);

      final Content.FormatDescriptor format =
        (extension != null)?
        Content.getFormatDescriptorForFileExtension(extension):
        null;

      String message =
        (format != null)? format.getSelectorLabel():
        (extension == null)? getString(R.string.format_extension_none):
        String.format("%s (%s)",
          extension,
          getString(R.string.format_extension_unrecognized)
        );

      OnDialogClickListener okListener = new OnDialogClickListener() {
        @Override
        public void onClick () {
          saveAs(format);
        }
      };

      OnDialogClickListener changeListener = new OnDialogClickListener() {
        @Override
        public void onClick () {
          selectFormat();
        }
      };

      newAlertDialogBuilder(R.string.format_request_confirm)
        .setMessage(message)
        .setPositiveButton(R.string.action_ok, okListener)
        .setNeutralButton(R.string.action_change, changeListener)
        .setNegativeButton(R.string.action_cancel, null)
        .show();
    }
  }

  private void menuAction_new () {
    testHasChanged(
      new Runnable() {
        @Override
        public void run () {
          setEditorContent();
          checkpointFile();
        }
      }
    );
  }

  private void menuAction_open () {
    testHasChanged(
      new Runnable() {
        @Override
        public void run () {
          findFile(false, null,
            new FileFinder.FileHandler() {
              @Override
              public void handleFile (File file) {
                if (file != null) {
                  if (loadContent(new ContentHandle(file, null, true))) {
                    checkpointFile();
                  }
                }
              }
            }
          );
        }
      }
    );
  }

  private void menuAction_save () {
    if (contentHandle == null) {
      menuAction_saveAs();
    } else {
      saveFile();
    }
  }

  private void menuAction_saveAs () {
    confirmFormat();
  }

  private void menuAction_record () {
    SpeechToText.TextHandler handler = new SpeechToText.TextHandler() {
      @Override
      public void handleText (ArrayList<String> text) {
        for (String line : text) {
          editArea.append(line);
        }
      }
    };

    new SpeechToText.Builder(this)
                    .start(handler);
  }

  private void menuAction_send () {
    if (contentHandle != null) {
      OutgoingMessage message = new OutgoingMessage();
      message.addAttachment(contentHandle.getUri());

      if (message.send()) {
      }
    } else {
      showMessage(R.string.message_send_new);
    }
  }

  private void menuAction_delete () {
    FileFinder.Builder builder = new FileFinder
      .Builder(this)
      .setUserTitle(R.string.menu_options_delete)
      ;

    addRootLocations(builder);

    builder.find(
      new FileFinder.FileHandler() {
        @Override
        public void handleFile (final File file) {
          if (file != null) {
            final String path = file.getAbsolutePath();

            confirmAction(
              R.string.delete_question,
              path,
              new Runnable() {
                @Override
                public void run () {
                  if (!file.delete()) {
                    showMessage(R.string.delete_failed, path);
                  }
                }
              }
            );
          }
        }
      }
    );
  }

  private void menuAction_paste () {
    CharSequence text = getText(getClipboard());

    if (text != null) {
      int start = editArea.getSelectionStart();
      int end = editArea.getSelectionEnd();

      if (verifyTextRange(start, end)) {
        editArea.getText().replace(start, end, text);
        editArea.setSelection(start + text.length());
      }
    }
  }

  private void menuAction_copy (boolean delete) {
    int start = editArea.getSelectionStart();
    int end = editArea.getSelectionEnd();

    if (verifyTextRange(start, end)) {
      Editable text = editArea.getText();
      ClipData clip = ClipData.newPlainText("NBP Editor", text.subSequence(start, end));
      getClipboard().setPrimaryClip(clip);

      if (delete) {
        text.delete(start, end);
      } else {
        editArea.setSelection(end);
      }
    }
  }

  private void menuAction_uppercase () {
    int start = editArea.getSelectionStart();
    int end = editArea.getSelectionEnd();

    if (verifyTextRange(start, end)) {
      Editable text = editArea.getText();
      text.replace(start, end, text.subSequence(start, end).toString().toUpperCase());
    }
  }

  private void menuAction_lowercase () {
    int start = editArea.getSelectionStart();
    int end = editArea.getSelectionEnd();

    if (verifyTextRange(start, end)) {
      Editable text = editArea.getText();
      text.replace(start, end, text.subSequence(start, end).toString().toLowerCase());
    }
  }

  private void menuAction_highlight (HighlightSpans.Entry spanEntry) {
    int start = editArea.getSelectionStart();
    int end = editArea.getSelectionEnd();

    if (verifyTextRange(start, end)) {
      Editable text = editArea.getText();
      text.setSpan(spanEntry.newInstance(), start, end, text.SPAN_EXCLUSIVE_EXCLUSIVE);
      editArea.setSelection(end);
    }
  }

  @Override
  public boolean onOptionsItemSelected (MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_options_new:
        menuAction_new();
        return true;

      case R.id.menu_options_open:
        menuAction_open();
        return true;

      case R.id.menu_options_save:
        menuAction_save();
        return true;

      case R.id.menu_options_saveAs:
        menuAction_saveAs();
        return true;

      case R.id.menu_options_record:
        menuAction_record();
        return true;

      case R.id.menu_options_send:
        menuAction_send();
        return true;

      case R.id.menu_options_delete:
        menuAction_delete();
        return true;

      case R.id.menu_options_edit:
        return true;

      case R.id.menu_edit_paste:
        menuAction_paste();
        return true;

      case R.id.menu_edit_selectAll:
        editArea.selectAll();
        return true;

      case R.id.menu_edit_copy:
        menuAction_copy(false);
        return true;

      case R.id.menu_edit_cut:
        menuAction_copy(true);
        return true;

      case R.id.menu_edit_uppercase:
        menuAction_uppercase();
        return true;

      case R.id.menu_edit_lowercase:
        menuAction_lowercase();
        return true;

      case R.id.menu_edit_bold:
        menuAction_highlight(HighlightSpans.BOLD);
        return true;

      case R.id.menu_edit_italics:
        menuAction_highlight(HighlightSpans.ITALIC);
        return true;

      case R.id.menu_edit_strike:
        menuAction_highlight(HighlightSpans.STRIKE);
        return true;

      case R.id.menu_edit_subscript:
        menuAction_highlight(HighlightSpans.SUBSCRIPT);
        return true;

      case R.id.menu_edit_superscript:
        menuAction_highlight(HighlightSpans.SUPERSCRIPT);
        return true;

      case R.id.menu_edit_underline:
        menuAction_highlight(HighlightSpans.UNDERLINE);
        return true;

      default:
        return false;
    }
  }

  @Override
  public boolean onPrepareOptionsMenu (Menu menu) {
    super.onPrepareOptionsMenu(menu);

    Menu editSubmenu = menu.findItem(R.id.menu_options_edit).getSubMenu();
    boolean showCursorGroup = false;
    boolean showSelectionGroup = false;

    if (editArea.getSelectionStart() == editArea.getSelectionEnd()) {
      showCursorGroup = true;
    } else {
      showSelectionGroup = true;
    }

    editSubmenu.setGroupVisible(R.id.menu_group_cursor, showCursorGroup);
    editSubmenu.setGroupVisible(R.id.menu_group_selection, showSelectionGroup);

    return true;
  }

  @Override
  public boolean onCreateOptionsMenu (Menu menu) {
    getMenuInflater().inflate(R.menu.options, menu);
    return true;
  }

  private final void prepareMenuButton () {
    if (getActionBar() == null) {
      Button button = (Button)findViewById(R.id.menu_button);
      button.setVisibility(button.VISIBLE);

      button.setOnClickListener(
        new Button.OnClickListener() {
          @Override
          public void onClick (View view) {
            editArea.requestFocus();
            getActivity().openOptionsMenu();
          }
        }
      );
    }
  }

  private final static void saveSpan (StringBuilder sb, Spanned spanned, Object span, String identifier) {
    if (sb.length() > 0) sb.append(' ');
    sb.append(identifier);

    sb.append(' ');
    sb.append(spanned.getSpanStart(span));

    sb.append(' ');
    sb.append(spanned.getSpanEnd(span));

    sb.append(' ');
    sb.append(spanned.getSpanFlags(span));
  }

  private final static void saveHighlightSpans (StringBuilder sb, Spanned spanned) {
    CharacterStyle[] spans = spanned.getSpans(0, spanned.length(), CharacterStyle.class);

    if (spans != null) {
      for (CharacterStyle span : spans) {
        HighlightSpans.Entry spanEntry = HighlightSpans.getEntry(span);
        if (spanEntry == null) continue;
        saveSpan(sb, spanned, span, spanEntry.getIdentifier());
      }
    }
  }

  private final static void saveEditorSpans (StringBuilder sb, Spanned spanned) {
    EditorSpan[] spans = spanned.getSpans(0, spanned.length(), EditorSpan.class);

    if (spans != null) {
      for (EditorSpan span : spans) {
        saveSpan(sb, spanned, span, span.getSpanIdentifier());
      }
    }
  }

  private final static String saveSpans (CharSequence text) {
    if (text == null) return null;
    if (!(text instanceof Spanned)) return null;
    Spanned spanned = (Spanned)text;

    StringBuilder sb = new StringBuilder();
    saveHighlightSpans(sb, spanned);
    saveEditorSpans(sb, spanned);

    if (sb.length() == 0) return null;
    return sb.toString();
  }

  private final static Object restoreSpan (String identifier) {
    {
      HighlightSpans.Entry spanEntry = HighlightSpans.getEntry(identifier);
      if (spanEntry != null) return spanEntry.newInstance();
    }

    return null;
  }

  private final static void restoreSpans (Spannable spannable, String[] fields) {
    int length = spannable.length();
    int count = fields.length;
    int index = 0;

    while (index < count) {
      String identifier;
      int start;
      int end;
      int flags;

      try {
        identifier = fields[index++];
        start = Integer.valueOf(fields[index++]);
        end = Integer.valueOf(fields[index++]);
        flags = Integer.valueOf(fields[index++]);
      } catch (Exception exception) {
        break;
      }

      if (verifyTextRange(start, end, length)) {
        Object span = restoreSpan(identifier);
        if (span == null) continue;
        spannable.setSpan(span, start, end, flags);
      }
    }
  }

  private final void restoreSpans (String spans) {
    if (spans == null) return;

    spans = spans.trim();
    if (spans.isEmpty()) return;

    String[] fields = spans.split("\\s+");
    CharSequence text = editArea.getText();

    if (text instanceof Spannable) {
      restoreSpans((Spannable)text, fields);
    } else {
      SpannableString string = new SpannableString(text);
      restoreSpans(string, fields);
      editArea.setText(string);
    }
  }

  private final static void putPreference (
    SharedPreferences.Editor editor, String key, String value
  ) {
    if (value != null) {
      editor.putString(key, value);
    } else {
      editor.remove(key);
    }
  }

  private final void checkpointFile () {
    synchronized (this) {
      String oldName = prefs.getString(PREF_CHECKPOINT_NAME, null);
      final String newName;

      if (oldName == null) {
        newName = "checkpoint1";
      } else {
        StringBuilder sb = new StringBuilder(oldName);
        int last = oldName.length() - 1;

        sb.setCharAt(last, (oldName.charAt(last) == '1')? '2': '1');
        newName = sb.toString();
      }

      final File oldFile = (oldName != null)? new File(filesDirectory, oldName): null;
      final File newFile = new File(filesDirectory, newName);
      final ContentHandle handle = contentHandle;

      saveFile(newFile,
        new Runnable() {
          @Override
          public void run () {
            SharedPreferences.Editor editor = prefs.edit();
            removePreferenceKeys(editor, PREF_CHECKPOINT_PREFIX);
            editor.putString(PREF_CHECKPOINT_NAME, newFile.getName());

            if (handle != null) {
              putPreference(editor, PREF_CHECKPOINT_PATH, handle.getUriString());
              putPreference(editor, PREF_CHECKPOINT_TYPE, handle.getType());
              editor.putBoolean(PREF_CHECKPOINT_WRITABLE, handle.canWrite());
            }

            putPreference(editor, PREF_CHECKPOINT_SPANS, saveSpans(editArea.getText()));
            editor.putInt(PREF_CHECKPOINT_START, editArea.getSelectionStart());
            editor.putInt(PREF_CHECKPOINT_END, editArea.getSelectionEnd());

            if (editor.commit()) {
              if (oldFile != null) {
                oldFile.delete();
              }
            }
          }
        }
      );
    }
  }

  private final void restoreFile () {
    synchronized (this) {
      String name = prefs.getString(PREF_CHECKPOINT_NAME, null);
      final String path = prefs.getString(PREF_CHECKPOINT_PATH, null);

      if (name != null) {
        loadContent(
          new ContentHandle(new File(filesDirectory, name), null, true),
          new Runnable() {
            @Override
            public void run () {
              int length = editArea.length();
              String type = prefs.getString(PREF_CHECKPOINT_TYPE, null);
              boolean writable = prefs.getBoolean(PREF_CHECKPOINT_WRITABLE, true);

              if ((path == null) || path.isEmpty()) {
                setEditorContent(null);
              } else if (path.charAt(0) == File.separatorChar) {
                setEditorContent(new ContentHandle(new File(path), type, writable));
              } else {
                setEditorContent(new ContentHandle(path, type, writable));
              }

              {
                String spans = prefs.getString(PREF_CHECKPOINT_SPANS, null);
                restoreSpans(spans);
              }

              {
                int start = prefs.getInt(PREF_CHECKPOINT_START, -1);
                int end = prefs.getInt(PREF_CHECKPOINT_END, -1);

                if (verifyTextRange(start, end, length)) {
                  editArea.setSelection(start, end);
                }
              }
            }
          }
        );
      }
    }
  }

  private final void editContent (ContentHandle handle) {
    loadContent(handle);
  }

  private final void addInputFilters () {
    InputFilter hasChangedMonitor = new InputFilter() {
      @Override
      public CharSequence filter (
        CharSequence src, int srcStart, int srcEnd,
        Spanned dst, int dstStart, int dstEnd
      ) {
        hasChanged = true;
        return null;
      }
    };

    InputFilter[] inputFilters = new InputFilter[] {
      hasChangedMonitor
    };

    editArea.setFilters(inputFilters);
  }

  private final void setup () {
    ApplicationContext.setMainActivity(this);

    filesDirectory = getFilesDir();
    prefs = getSharedPreferences("editor", MODE_PRIVATE);

    setContentView(R.layout.editor);
    uriView = (TextView)findViewById(R.id.current_uri);
    editArea = (EditText)findViewById(R.id.edit_area);
    setEditorContent();

    showReportedErrors();
    prepareMenuButton();
    addInputFilters();
  }

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setup();

    int result = RESULT_CANCELED;
    Intent intent = getIntent();

    if (intent != null) {
      String action = intent.getAction();

      if (action != null) {
        Uri uri = intent.getData();
        String type = intent.getType();

        if (action.equals(Intent.ACTION_MAIN)) {
          restoreFile();
          result = RESULT_OK;
        } else if (action.equals(Intent.ACTION_VIEW)) {
          editContent(new ContentHandle(uri, type, false));
          result = RESULT_OK;
        } else if (action.equals(Intent.ACTION_EDIT)) {
          editContent(new ContentHandle(uri, type, true));
          result = RESULT_OK;
        }
      }
    }

    setResult(result);
  }

  @Override
  protected void onResume () {
    super.onResume();

    editArea.requestFocus();
  }

  @Override
  protected void onPause () {
    super.onPause();
    checkpointFile();
  }

  @Override
  protected void onDestroy () {
    try {
    } finally {
      super.onDestroy();
    }
  }
}
