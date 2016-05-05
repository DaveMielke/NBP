Dialogs
=======

.. |return to current file| replace::

  Return to the file currently being edited.

Confirm Format
--------------

This dialog is presented in order to confirm
that the file will be saved in the desired format.
If the file's path isn't known
(a new file is being edited)
then the `Select Format`_ dialog is used instead.

One of the following must be confirmed:

no extension
  The file's name doesn't have an extension.

unrecognized extension
  The file's name has an extension that the editor doesn't support.

an extension along with a description of its corresponding format
  The extension corresponds to one of the editor's `Supported Formats`_.

One of the following actions may be selected:

OK
  Save the file as is.
  If its name has either no extension or an unrecognized extension
  then its content is assumed to be plain text.

Change
  Use the `Select Format`_ dialog to choose a supported format.

Cancel
  |return to current file|

Select Format
-------------

This dialog is presented so that
one of the editor's `Supported Formats`_ can be selected when saving a file.
`The File Finder`_ is then used
either to locate an existing file
or to enter a new file name.

Each listed format shows its corresponding file extension and its description.
Folder listings will be filtered based on the selected extension.
If the special format **all files** is selected
then folder listings won't be filtered,
and the file will be saved in the format
associated with the extension of the selected or entered file's name.

One of the following actions may also be selected:

Cancel
  |return to current file|

Save Changes
------------

This dialog is presented before a new file is edited
if `The Edit Area`_ contains changes.

One of the following actions may be selected:

Save First
  The `Save`_ action is performed before the new file is edited.

Cancel This Action
  |return to current file|

Continue without Saving
  Edit the new file without first saving the current changes.

File Saved
----------

This dialog is presented in order to confirm
that a file has been successfully saved.
It contains the file's absolute path.
Select **OK** to clear it.

Delete File
-----------

This dialog is presented before a file is actually deleted
in order to ensure that the correct file has been chosen.

One of the following confirmations may be selected:

Yes
  Delete the file.
 
No
  Don't delete the file.

