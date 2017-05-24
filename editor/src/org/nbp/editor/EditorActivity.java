package org.nbp.editor;

import java.io.File;
import java.util.Date;

import java.util.List;
import java.util.ArrayList;

import java.util.Set;
import java.util.LinkedHashSet;

import org.nbp.common.CommonActivity;
import org.nbp.common.CommonUtilities;
import org.nbp.common.AlertDialogBuilder;
import org.nbp.common.DialogFinisher;
import org.nbp.common.DialogHelper;
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
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;

import android.widget.TableLayout;
import android.widget.TableRow;

import android.text.InputFilter;
import android.text.Editable;

import org.nbp.common.HighlightSpans;
import android.text.Spanned;
import android.text.Spannable;
import android.text.SpannableStringBuilder;

import android.view.Menu;
import android.view.MenuItem;

import android.app.Dialog;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class EditorActivity extends CommonActivity {
  private final static String LOG_TAG = EditorActivity.class.getName();

  private File filesDirectory;
  private SharedPreferences prefs;

  private ArrayList<String> recentURIs = new ArrayList<String>();
  private final static String PREF_RECENT_URIS = "recent-URIs";

  private final void restoreRecentURIs () {
    Set<String> uris = prefs.getStringSet(PREF_RECENT_URIS, null);
    if (uris != null) recentURIs.addAll(uris);
  }

  private final void saveRecentURI (String uri) {
    recentURIs.remove(uri);

    while (recentURIs.size() >= ApplicationParameters.RECENT_URI_LIMIT) {
      recentURIs.remove(0);
    }

    recentURIs.add(uri);
    prefs.edit()
         .putStringSet(PREF_RECENT_URIS, new LinkedHashSet<String>(recentURIs))
         .commit();
  }

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

  private EditArea editArea = null;
  private TextView uriView = null;
  private ContentHandle contentHandle = null;
  private boolean hasChanged = false;

  private final void showActivityResultCode (int code) {
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

  private final boolean verifyTextRange (int start, int end) {
    return ApplicationUtilities.verifyTextRange(start, end, editArea.length());
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

  private final void runProtectedOperation (Runnable operation) {
    boolean wasEnforced = editArea.getEnforceTextProtection();
    editArea.setEnforceTextProtection(false);

    try {
      operation.run();
    } finally {
      editArea.setEnforceTextProtection(wasEnforced);
    }
  }

  private final void saveFile (
    final File file, final CharSequence content,
    final boolean confirm, final Runnable onSaved
  ) {
    new AsyncTask<Void, Void, Void>() {
      AlertDialog dialog = null;

      @Override
      protected void onPreExecute () {
        if (confirm) {
          dialog = newAlertDialogBuilder(R.string.message_writing_content)
            .setMessage(file.getAbsolutePath())
            .create();

          dialog.show();
        }
      }

      @Override
      public Void doInBackground (Void... arguments) {
        Content.writeFile(file, content);
        return null;
      }

      @Override
      public void onPostExecute (Void result) {
        if (dialog != null) dialog.dismiss();

        if (confirm) {
          showMessage(R.string.message_file_saved, file.getAbsolutePath(), onSaved);
        } else {
          run(onSaved);
        }
      }
    }.execute();
  }

  private final void saveFile (File file, Runnable onSaved) {
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

    saveFile(file, content, true, onSaved);
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

  private final boolean loadContent (
    final ContentHandle handle, final Runnable onLoaded
  ) {
    new AsyncTask<Void, Void, CharSequence>() {
      AlertDialog dialog = null;

      @Override
      protected void onPreExecute () {
        dialog = newAlertDialogBuilder(R.string.message_reading_content)
          .setMessage(handle.getNormalizedString())
          .show();
      }

      @Override
      protected CharSequence doInBackground (Void... arguments) {
        try {
          final Editable input = new SpannableStringBuilder();
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

        if (dialog != null) dialog.dismiss();
      }
    }.execute();

    return true;
  }

  private final boolean loadContent (ContentHandle handle) {
    saveRecentURI(handle.getNormalizedString());
    return loadContent(handle, null);
  }

  private final boolean loadContent (File file) {
    return loadContent(new ContentHandle(file));
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

  private final FileFinder.Builder newFileFinderBuilder (boolean forWriting) {
    int title = forWriting?
                R.string.menu_file_saveAs:
                R.string.menu_file_open;

    return new FileFinder
          .Builder(this)
          .setUserTitle(getString(title))
          .setForWriting(forWriting)
          ;
  }

  private final void findFile (
    boolean forWriting,
    String[] extensions,
    FileFinder.FileHandler handler
  ) {
    FileFinder.Builder builder = newFileFinderBuilder(forWriting);

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

  private void menuAction_recent () {
    DialogFinisher finisher = new DialogFinisher() {
      @Override
      public void finishDialog (DialogHelper helper) {
        final Dialog dialog = helper.getDialog();
        TableLayout table = (TableLayout)helper.findViewById(R.id.recent_table);
        int uriIndex = recentURIs.size();

        while (uriIndex > 0) {
          final String uri = recentURIs.get(--uriIndex);
          final View item;

          if (uri.charAt(0) == File.separatorChar) {
            final File file = new File(uri);
            final File parent = file.getParentFile();

            View name = newButton(
              file.getName(),
              new Button.OnClickListener() {
                @Override
                public void onClick (View vie) {
                  dialog.dismiss();
                  loadContent(file);
                }
              }
            );

            View folder = newButton(
              parent.getAbsolutePath(),
              new Button.OnClickListener() {
                @Override
                public void onClick (View vie) {
                  dialog.dismiss();
                  FileFinder.Builder builder = newFileFinderBuilder(false);
                  builder.addRootLocation(parent);

                  builder.find(
                    new FileFinder.FileHandler() {
                      @Override
                      public void handleFile (File file) {
                        if (file != null) loadContent(file);
                      }
                    }
                  );
                }
              }
            );

            ViewGroup row = newTableRow();
            row.addView(name);
            row.addView(folder);
            item = row;
          } else {
            item = newButton(uri,
              new Button.OnClickListener() {
                @Override
                public void onClick (View view) {
                  dialog.dismiss();
                  loadContent(new ContentHandle(uri, null, false));
                }
              }
            );
          }

          table.addView(item);
        }
      }
    };

    showDialog(R.string.menu_file_recent, R.layout.recent_list, finisher, false);
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
                  if (loadContent(file)) {
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
      .setUserTitle(R.string.menu_file_delete)
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

  private final void verifyRecording (final String text) {
    if (!text.isEmpty()) {
      DialogFinisher finisher = new DialogFinisher() {
        @Override
        public void finishDialog (DialogHelper helper) {
          helper.setText(R.id.record_text, text);
        }
      };

      DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int button) {
          EditText view = (EditText)CommonUtilities.findView(dialog, R.id.record_text);
          editArea.replaceSelection(view.getText());
        }
      };

      showDialog(
        R.string.menu_edit_record, R.layout.record_verify,
        finisher, R.string.action_accept, listener
      );
    }
  }

  private final void verifyRecording (final String[] choices) {
    int count = choices.length;

    if (count != 0) {
      if (count == 1) {
        verifyRecording(choices[0]);
      } else {
        showChooser(
          R.string.menu_edit_record, choices,
          new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog, int item) {
              verifyRecording(choices[item]);
            }
          }
        );
      }
    }
  }

  private void menuAction_record () {
    if (editArea.containsProtectedText()) {
      showMessage(R.string.message_protected_text);
    } else {
      SpeechToText.TextHandler handler = new SpeechToText.TextHandler() {
        @Override
        public void handleText (String[] choices) {
          verifyRecording(choices);
        }
      };

      new SpeechToText.Builder(this)
                      .start(handler);
    }
  }

  private void menuAction_selection (Menu menu) {
    boolean showSelectionActions = false;
    boolean showCursorActions = false;

    if (editArea.hasSelection()) {
      showSelectionActions = true;
    } else {
      showCursorActions = true;
    }

    menu.setGroupVisible(R.id.menu_group_selection, showSelectionActions);
    menu.setGroupVisible(R.id.menu_group_cursor, showCursorActions);
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

  private void menuAction_nextGroup () {
    if (!editArea.moveToNextGroup()) {
      showMessage(R.string.message_no_next_group);
    }
  }

  private void menuAction_previousGroup () {
    if (!editArea.moveToPreviousGroup()) {
      showMessage(R.string.message_no_previous_group);
    }
  }

  private void menuAction_nextRevision () {
    if (!editArea.moveToNextRevision()) {
      showMessage(R.string.message_no_next_revision);
    }
  }

  private void menuAction_previousRevision () {
    if (!editArea.moveToPreviousRevision()) {
      showMessage(R.string.message_no_previous_revision);
    }
  }

  private void menuAction_showRevision () {
    RevisionSpan revision = editArea.getRevisionSpan();

    if (revision != null) {
      showDialog(
        R.string.menu_currentRevision_showRevision, R.layout.revision_show, revision
      );
    } else {
      showMessage(R.string.message_original_text);
    }
  }

  private void menuAction_acceptRevision () {
    final RevisionSpan revision = editArea.getRevisionSpan();

    if (revision != null) {
      showDialog(
        R.string.menu_currentRevision_acceptRevision, R.layout.revision_show,
        revision, R.string.action_accept,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick (DialogInterface dialog, int button) {
            runProtectedOperation(
              new Runnable() {
                @Override
                public void run () {
                  int position = Markup.acceptRevision(editArea.getText(), revision);
                  editArea.setSelection(position);
                  hasChanged = true;
                }
              }
            );
          }
        }
      );
    } else {
      showMessage(R.string.message_original_text);
    }
  }

  private void menuAction_rejectRevision () {
    final RevisionSpan revision = editArea.getRevisionSpan();

    if (revision != null) {
      showDialog(
        R.string.menu_currentRevision_rejectRevision, R.layout.revision_show,
        revision, R.string.action_reject,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick (DialogInterface dialog, int button) {
            runProtectedOperation(
              new Runnable() {
                @Override
                public void run () {
                  int position = Markup.rejectRevision(editArea.getText(), revision);
                  editArea.setSelection(position);
                  hasChanged = true;
                }
              }
            );
          }
        }
      );
    } else {
      showMessage(R.string.message_original_text);
    }
  }

  private void menuAction_markChanges () {
    runProtectedOperation(
      new Runnable() {
        @Override
        public void run () {
          Markup.restoreRevisions(editArea.getText());
        }
      }
    );
  }

  private void menuAction_previewChanges () {
    runProtectedOperation(
      new Runnable() {
        @Override
        public void run () {
          Markup.previewRevisions(editArea.getText());
        }
      }
    );
  }

  private void menuAction_acceptChanges () {
    runProtectedOperation(
      new Runnable() {
        @Override
        public void run () {
          if (Markup.acceptRevisions(editArea.getText())) hasChanged = true;
        }
      }
    );
  }

  private void menuAction_showComment () {
    CommentSpan comment = editArea.getCommentSpan();

    if (comment != null) {
      showDialog(
        R.string.menu_comments_showComment, R.layout.comment_show, comment
      );
    } else {
      showMessage(R.string.message_uncommented_text);
    }
  }

  private void menuAction_nextComment () {
    if (!editArea.moveToNextComment()) {
      showMessage(R.string.message_no_next_comment);
    }
  }

  private void menuAction_previousComment () {
    if (!editArea.moveToPreviousComment()) {
      showMessage(R.string.message_no_previous_comment);
    }
  }

  private void menuAction_newComment () {
    showDialog(
      R.string.menu_comments_newComment,
      R.layout.comment_new, R.string.action_add,
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int button) {
          CommentSpan comment;

          {
            EditText view = (EditText)CommonUtilities.findView(dialog, R.id.comment_text);
            Editable text = view.getText();

            if (text.toString().trim().isEmpty()) return;
            comment = new CommentSpan(text);
            comment.setReviewTimestamp(new Date());
          }

          Editable text = editArea.getText();
          int start = editArea.getSelectionStart();
          int end = editArea.getSelectionEnd();

          text.setSpan(comment, start, end, Spanned.SPAN_POINT_POINT);
          comment.finishSpan(text);
          editArea.setSelection(comment);
          hasChanged = true;
        }
      }
    );
  }

  private void menuAction_removeComment () {
    final CommentSpan comment = editArea.getCommentSpan();

    if (comment != null) {
      showDialog(
        R.string.menu_comments_removeComment, R.layout.comment_show,
        comment, R.string.action_remove,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick (DialogInterface dialog, int button) {
            runProtectedOperation(
              new Runnable() {
                @Override
                public void run () {
                  int position = comment.removeSpan(editArea.getText());
                  editArea.setSelection(position);
                  hasChanged = true;
                }
              }
            );
          }
        }
      );
    } else {
      showMessage(R.string.message_uncommented_text);
    }
  }

  private void menuAction_summary () {
    ReviewSummary summary = new ReviewSummary(editArea.getText());
    summary.setContentURI(uriView.getText());

    showDialog(
      R.string.menu_review_summary, R.layout.review_summary, summary
    );
  }

  @Override
  public boolean onOptionsItemSelected (MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_options_file:
        return true;

      case R.id.menu_file_new:
        menuAction_new();
        return true;

      case R.id.menu_file_recent:
        menuAction_recent();
        return true;

      case R.id.menu_file_open:
        menuAction_open();
        return true;

      case R.id.menu_file_save:
        menuAction_save();
        return true;

      case R.id.menu_file_saveAs:
        menuAction_saveAs();
        return true;

      case R.id.menu_file_send:
        menuAction_send();
        return true;

      case R.id.menu_file_delete:
        menuAction_delete();
        return true;

      case R.id.menu_options_edit:
        return true;

      case R.id.menu_edit_paste:
        menuAction_paste();
        return true;

      case R.id.menu_edit_record:
        menuAction_record();
        return true;

      case R.id.menu_options_selection:
        menuAction_selection(item.getSubMenu());
        return true;

      case R.id.menu_selection_selectAll:
        editArea.selectAll();
        return true;

      case R.id.menu_selection_copy:
        menuAction_copy(false);
        return true;

      case R.id.menu_selection_cut:
        menuAction_copy(true);
        return true;

      case R.id.menu_selection_uppercase:
        menuAction_uppercase();
        return true;

      case R.id.menu_selection_lowercase:
        menuAction_lowercase();
        return true;

      case R.id.menu_options_highlight:
        return true;

      case R.id.menu_highlight_bold:
        menuAction_highlight(HighlightSpans.BOLD);
        return true;

      case R.id.menu_highlight_italics:
        menuAction_highlight(HighlightSpans.ITALIC);
        return true;

      case R.id.menu_highlight_strike:
        menuAction_highlight(HighlightSpans.STRIKE);
        return true;

      case R.id.menu_highlight_subscript:
        menuAction_highlight(HighlightSpans.SUBSCRIPT);
        return true;

      case R.id.menu_highlight_superscript:
        menuAction_highlight(HighlightSpans.SUPERSCRIPT);
        return true;

      case R.id.menu_highlight_underline:
        menuAction_highlight(HighlightSpans.UNDERLINE);
        return true;

      case R.id.menu_options_review:
        return true;

      case R.id.menu_options_changes:
        return true;

      case R.id.menu_options_moveTo:
        return true;

      case R.id.menu_moveTo_nextGroup:
        menuAction_nextGroup();
        return true;

      case R.id.menu_moveTo_previousGroup:
        menuAction_previousGroup();
        return true;

      case R.id.menu_moveTo_nextRevision:
        menuAction_nextRevision();
        return true;

      case R.id.menu_moveTo_previousRevision:
        menuAction_previousRevision();
        return true;

      case R.id.menu_options_currentRevision:
        return true;

      case R.id.menu_currentRevision_showRevision:
        menuAction_showRevision();
        return true;

      case R.id.menu_currentRevision_acceptRevision:
        menuAction_acceptRevision();
        return true;

      case R.id.menu_currentRevision_rejectRevision:
        menuAction_rejectRevision();
        return true;

      case R.id.menu_options_markup:
        return true;

      case R.id.menu_markup_allMarkup:
        menuAction_markChanges();
        return true;

      case R.id.menu_markup_noMarkup:
        menuAction_previewChanges();
        return true;

      case R.id.menu_changes_acceptChanges:
        menuAction_acceptChanges();
        return true;

      case R.id.menu_options_comments:
        return true;

      case R.id.menu_comments_showComment:
        menuAction_showComment();
        return true;

      case R.id.menu_comments_nextComment:
        menuAction_nextComment();
        return true;

      case R.id.menu_comments_previousComment:
        menuAction_previousComment();
        return true;

      case R.id.menu_comments_newComment:
        menuAction_newComment();
        return true;

      case R.id.menu_comments_removeComment:
        menuAction_removeComment();
        return true;

      case R.id.menu_review_summary:
        menuAction_summary();
        return true;

      default:
        return false;
    }
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

  private final void restoreSpans (String spans) {
    Spans.restoreSpans((Spannable)editArea.getText(), spans);
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

      final Editable content = new SpannableStringBuilder(editArea.getText());
      Markup.restoreRevisions(content);

      saveFile(newFile, content.toString(), false,
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

            putPreference(
              editor, PREF_CHECKPOINT_SPANS, Spans.saveSpans(content)
            );

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

                if (ApplicationUtilities.verifyTextRange(start, end, length)) {
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
        if (editArea.containsProtectedText(dst, dstStart, dstEnd)) {
          showMessage(R.string.message_protected_text);
          return dst.subSequence(dstStart, dstEnd);
        }

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

    restoreRecentURIs();

    setContentView(R.layout.editor);
    uriView = (TextView)findViewById(R.id.current_uri);
    editArea = (EditArea)findViewById(R.id.edit_area);
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
