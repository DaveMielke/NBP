package org.nbp.common;

import java.util.Set;
import java.util.TreeSet;
import java.util.LinkedHashSet;

import java.util.Map;
import java.util.LinkedHashMap;

import java.io.File;

import android.util.Log;

import android.app.Activity;
import android.os.AsyncTask;

import android.content.DialogInterface;
import android.app.AlertDialog;

import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;

public abstract class FileFinder extends ActivityComponent {
  private final static String LOG_TAG = FileFinder.class.getName();

  public interface FileHandler {
    public void handleFile (File file);
  }

  public interface FilesHandler {
    public void handleFiles (File[] files);
  }

  public final static class Builder {
    private final Activity ownerActivity;

    public Builder (Activity owner) {
      ownerActivity = owner;
    }

    private Integer userTitle = null;
    private final Map<String, File> rootLocations = new LinkedHashMap<String, File>();
    private final Set<String> fileExtensions = new LinkedHashSet<String>();
    private String fileName = null;
    private boolean forWriting = false;

    private final String getString (int resource) {
      return ownerActivity.getString(resource);
    }

    public final Activity getOwnerActivity () {
      return ownerActivity;
    }

    public final Integer getUserTitle () {
      return userTitle;
    }

    public final Builder setUserTitle (Integer title) {
      userTitle = title;
      return this;
    }

    public final String[] getRootLabels () {
      Set<String> labels = rootLocations.keySet();
      return labels.toArray(new String[labels.size()]);
    }

    public final File getRootLocation (String label) {
      return rootLocations.get(label);
    }

    public final Builder addRootLocation (String label, File location) {
      rootLocations.put(label, location);
      return this;
    }

    public final Builder addRootLocation (File location) {
      return addRootLocation(location.getAbsolutePath(), location);
    }

    public final Builder addRootLocation (String label) {
      return addRootLocation(label, FileSystems.getMountpoint(label));
    }

    public final String[] getFileExtensions () {
      return fileExtensions.toArray(new String[fileExtensions.size()]);
    }

    public final Builder addFileExtension (String extension) {
      fileExtensions.add(extension);
      return this;
    }

    public final String getFileName () {
      return fileName;
    }

    public final Builder setFileName (String name) {
      fileName = name;
      return this;
    }

    public final boolean getForWriting () {
      return forWriting;
    }

    public final Builder setForWriting (boolean yes) {
      forWriting = yes;
      return this;
    }

    public final void find (FileHandler handler) {
      new SingleFileFinder(this, handler);
    }

    public final void find (FilesHandler handler) {
      new MultipleFileFinder(this, handler);
    }
  }

  private final PathManager pathManager;

  private final Integer userTitle;
  private final String[] fileExtensions;
  private final String fileName;
  private final boolean forWriting;

  private final Map<String, File> rootLocations = new LinkedHashMap<String, File>();

  private File currentReference = null;

  protected abstract void setItems (AlertDialog.Builder builder, String[] items, File reference);
  protected abstract void handleFiles (File[] files);

  private final void setTitle (AlertDialog.Builder builder, int title, CharSequence... details) {
    setTitle(builder, title, userTitle, details);
  }

  private final void handleFile (File file) {
    handleFiles(new File[] {file});
  }

  private final void requestCancelled () {
    handleFiles(null);
  }

  private final void setTitle (AlertDialog.Builder builder, String... details) {
    setTitle(builder, R.string.FileFinder_title_main, details);
  }

  private final void setBackButton (AlertDialog.Builder builder) {
    builder.setNeutralButton(
      R.string.FileFinder_action_back,
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int button) {
          showListing();
        }
      }
    );
  }

  private final void setCancelButton (AlertDialog.Builder builder) {
    builder.setNegativeButton(
      android.R.string.no,
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int button) {
          requestCancelled();
        }
      }
    );
  }

  private final void setEditedPath (AlertDialog dialog, File reference) {
    String directory;
    String file;

    if (!reference.isAbsolute()) {
      directory = currentReference.getAbsolutePath();
      file = reference.getPath();
    } else if (reference.isDirectory()) {
      directory = reference.getAbsolutePath();
      file = fileName;
      if (file == null) file = "";
    } else {
      directory = reference.getParentFile().getAbsolutePath();
      file = reference.getName();
    }

    TextView directoryView = dialog.findViewById(R.id.PathEditor_directory);
    directoryView.setText(directory + File.separator);

    final Button doneButton = dialog.getButton(dialog.BUTTON_POSITIVE);
    EditText nameView = dialog.findViewById(R.id.PathEditor_name);
    nameView.setText("");

    String extension =
      ((fileExtensions != null) && (fileExtensions.length > 0))?
      fileExtensions[0]: null;

    if (extension != null) {
      final int extensionLength = extension.length();

      Editable text = nameView.getText();
      text.append(extension);

      text.setSpan(
        HighlightSpans.BOLD.newInstance(),
        0, text.length(),
        text.SPAN_EXCLUSIVE_EXCLUSIVE
      );

      InputFilter protectExtension = new InputFilter() {
        @Override
        public CharSequence filter (
          CharSequence src, int srcStart, int srcEnd,
          Spanned dst, int dstStart, int dstEnd
        ) {
          if (dstEnd <= (dst.length() - extensionLength)) return null;
          Tones.beep();
          return dst.subSequence(dstStart, dstEnd);
        }
      };

      nameView.setFilters(new InputFilter[] {protectExtension});
    }

    new OnTextEditedListener(nameView) {
      @Override
      protected void onTextEdited (boolean isDifferent) {
        doneButton.setEnabled(isDifferent);
      }
    };

    if (extension != null) {
      int end = file.lastIndexOf('.');

      if (end > 0) {
        if (end < (file.length() - 1)) {
          file = file.substring(0, end);
        }
      }
    }

    nameView.getText().insert(0, file);
    nameView.setSelection(0, file.length());
  }

  private final File getEditedPath (DialogInterface dialog) {
    TextView directoryView = findView(dialog, R.id.PathEditor_directory);
    String directory = directoryView.getText().toString();
    if (directory.isEmpty()) return null;

    EditText nameView = findView(dialog, R.id.PathEditor_name);
    String name = nameView.getText().toString().trim();
    if (name.isEmpty()) return null;

    return new File(new File(directory), name);
  }

  private final void showPathProblem (final File file, int problem) {
    showMessage(
      problem, file.getAbsolutePath(),
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int button) {
          showPathEditor(file);
        }
      }
    );
  }

  private final void showPathEditor (File path) {
    AlertDialog.Builder builder = newAlertDialogBuilder();
    setView(builder, R.layout.path_editor);

    setDoneButton(builder,
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick (DialogInterface dialog, int button) {
          File file = getEditedPath(dialog);

          if (file == null) {
            requestCancelled();
          } else {
            if (!file.isAbsolute()) {
              file = new File(currentReference, file.getPath());
            }

            if (!file.exists()) {
              File directory = file.getParentFile();

              if (directory.isDirectory() || directory.mkdirs()) {
                handleFile(file);
              } else {
                showPathProblem(directory, R.string.FileFinder_message_uncreatable_directory);
              }
            } else if (file.isFile()) {
              handleFile(file);
            } else if (file.isDirectory()) {
              showListing(file);
            } else {
              showPathProblem(file, R.string.FileFinder_message_uneditable_file);
            }
          }
        }
      }
    );

    setTitle(builder, getString(R.string.FileFinder_action_edit));
    setBackButton(builder);
    setCancelButton(builder);

    AlertDialog dialog = builder.create();
    dialog.show();
    setEditedPath(dialog, path);
  }

  private interface ListingCreator {
    public Set<String> createListing ();
  }

  private final void showListing (final File reference, final ListingCreator listingCreator) {
    currentReference = reference;
    final boolean haveReference = reference != null;

    new AsyncTask<Void, Void, AlertDialog.Builder>() {
      String[] items = null;

      final AlertDialog message = newAlertDialogBuilder()
        .setMessage(R.string.FileFinder_message_creating_listing)
        .create();

      @Override
      protected void onPreExecute () {
        message.show();
      }

      @Override
      protected AlertDialog.Builder doInBackground (Void... arguments) {
        AlertDialog.Builder builder = newAlertDialogBuilder();

        if (haveReference) {
          setTitle(builder, reference.getAbsolutePath());
        } else {
          setTitle(builder);
        }

        Set<String> listing = listingCreator.createListing();
        int count = listing.size();

        if (count == 0) {
          builder.setMessage(R.string.FileFinder_message_empty_directory);
        } else {
          items = new String[count];
          count = 0;

          for (String item : listing) {
            items[count++] = item;
          }

          setItems(builder, items, reference);
        }

        if (forWriting) {
          builder.setPositiveButton(
            R.string.FileFinder_action_edit,
            new DialogInterface.OnClickListener() {
              @Override
              public void onClick (DialogInterface dialog, int button) {
                showPathEditor(reference);
              }
            }
          );
        }

        builder.setNeutralButton(
          R.string.FileFinder_action_up,
          new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog, int button) {
              showListing(reference.getParentFile());
            }
          }
        );

        setCancelButton(builder);
        return builder;
      }

      @Override
      protected void onPostExecute (AlertDialog.Builder builder) {
        message.dismiss();

        AlertDialog dialog = builder.create();

        if (haveReference && (items != null)) {
          final ListView list = dialog.getListView();

          dialog.setOnShowListener(
            new DialogInterface.OnShowListener() {
              @Override
              public void onShow (DialogInterface dialog) {
                list.setOnItemLongClickListener(
                  new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick (AdapterView parent, View view, int position, long id) {
                      pathManager.managePath(new File(reference, items[position]));
                      return true;
                    }
                  }
                );
              }
            }
          );
        }

        dialog.show();

        if (forWriting) {
          boolean canCreate =  haveReference
                            && (reference.isDirectory())
                            && (reference.canWrite())
                            ;

          if (!canCreate) {
            setButtonEnabled(dialog, dialog.BUTTON_POSITIVE, false);
          }
        }

        if (!haveReference) {
          setButtonEnabled(dialog, dialog.BUTTON_NEUTRAL, false);
        }
      }
    }.execute();
  }

  private final static boolean canAccessDirectory (File directory) {
    if (!directory.canRead()) return false;
    if (!directory.canExecute()) return false;
    return true;
  }

  private final Set<String> createRootListing () {
    Set<String> listing = new LinkedHashSet<String>();

    for (String label : rootLocations.keySet()) {
      if (canAccessDirectory(rootLocations.get(label))) {
        listing.add(label);
      }
    }

    return listing;
  }

  private final Set<String> createDirectoryListing (File directory) {
    Set<String> listing = new TreeSet();
    File[] files = directory.listFiles();

    if (files != null) {
      for (File file : files) {
        if (file.isHidden()) continue;

        String name = file.getName();
        char indicator = 0;

        if (file.isDirectory()) {
          if (!canAccessDirectory(file)) continue;
          indicator = File.separatorChar;
        } else {
          if (!file.isFile()) continue;

        EXTENSION_CHECK:
          if (fileExtensions != null) {
            if (fileExtensions.length > 0) {
              for (String extension : fileExtensions) {
                if (name.endsWith(extension)) break EXTENSION_CHECK;
              }

              continue;
            }
          }

          if (forWriting) {
            if (!file.canWrite()) continue;
          } else {
            if (!file.canRead()) continue;
          }
        }

        if (indicator != 0) name += indicator;
        listing.add(name);
      }
    }

    return listing;
  }

  protected final void showListing (final File reference) {
    if (reference == null) {
      showListing(null,
        new ListingCreator() {
          @Override
          public Set<String> createListing () {
            return createRootListing();
          }
        }
      );
    } else if (reference.isDirectory()) {
      showListing(reference,
        new ListingCreator() {
          @Override
          public Set<String> createListing () {
            return createDirectoryListing(reference);
          }
        }
      );
    } else {
      handleFile(reference);
    }
  }

  protected final void showListing () {
    showListing(currentReference);
  }

  protected final void showListing (String label) {
    showListing(rootLocations.get(label));
  }

  private FileFinder (Builder builder) {
    super(builder.getOwnerActivity());

    pathManager = new PathManager(ownerActivity);

    userTitle = builder.getUserTitle();
    fileExtensions = builder.getFileExtensions();
    fileName = builder.getFileName();
    forWriting = builder.getForWriting();

    {
      String[] labels = builder.getRootLabels();

      if (labels.length == 0) {
        for (String label : FileSystems.getAllLabels()) {
          rootLocations.put(label, FileSystems.getMountpoint(label));
        }
      } else {
        for (String label : labels) {
          rootLocations.put(label, builder.getRootLocation(label));
        }
      }
    }

    if (rootLocations.size() == 1) {
      Set<String> labels = rootLocations.keySet();
      showListing(labels.toArray(new String[1])[0]);
    } else {
      showListing((File)null);
    }
  }

  private static class SingleFileFinder extends FileFinder {
    private final FileHandler fileHandler;

    protected final void handleFiles (File[] files) {
      File file = null;
      if (files != null) file = files[0];
      fileHandler.handleFile(file);
    }

    protected final void setItems (
      AlertDialog.Builder builder,
      final String[] items,
      final File reference
    ) {
      builder.setItems(items,
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick (DialogInterface dialog, int index) {
            String item = items[index];

            if (reference == null) {
              showListing(item);
            } else if (item.charAt(0) == File.separatorChar) {
              showListing(new File(item));
            } else {
              showListing(new File(reference, item));
            }
          }
        }
      );
    }

    private SingleFileFinder (Builder builder, FileHandler handler) {
      super(builder);
      fileHandler = handler;
    }
  }

  private static class MultipleFileFinder extends FileFinder {
    private final FilesHandler filesHandler;
    private final Set<String> selectedFiles = new TreeSet<String>();
    private boolean firstSelection;

    protected final void handleFiles (File[] files) {
      filesHandler.handleFiles(files);
    }

    protected final void setItems (
      AlertDialog.Builder builder,
      final String[] items,
      File reference
    ) {
      if (reference == null) {
        builder.setItems(items,
          new DialogInterface.OnClickListener() {
            @Override
            public void onClick (DialogInterface dialog, int index) {
              showListing(items[index]);
            }
          }
        );
      } else {
        selectedFiles.clear();
        firstSelection = true;

        builder.setMultiChoiceItems(items, null,
          new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick (DialogInterface dialog, int index, boolean isChecked) {
              String item = items[index];

              if (firstSelection) {
                firstSelection = false;
              }

              if (isChecked) {
                selectedFiles.add(item);
              } else {
                selectedFiles.remove(item);
              }
            }
          }
        );
      }
    }

    private MultipleFileFinder (Builder builder, FilesHandler handler) {
      super(builder);
      filesHandler = handler;
    }
  }
}
