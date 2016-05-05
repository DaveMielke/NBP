The File Finder
===============

The file finder is used to locate a file
for editing (see the `Open`_ action),
for saving (see the `Save As`_ action),
etc.

Location Listing
----------------

The initial screen presents a list of the available locations
where files can be stored.
For each of them, a descriptive label and the absolute path are shown.
This listing may contain:

current
  The folder that contains the current file.
  This location isn't listed if a new file is being edited.

documents
  The folder within internal storage that's used
  as the primary location for storing documentation.
  This location is always listed.
  If the folder doesn't yet exist then it's created.

sdcard
  The first partition on the removable SD card.
  This location isn't listed if an SD card
  hasn't been inserted,
  hasn't been formatted,
  is corrupt,
  etc.

usb
  The first partition on the removable USB mass storage device.
  This location isn't listed if a USB mass storage device
  hasn't been inserted,
  hasn't been formatted,
  is corrupt,
  etc.

Selecting the desired location goes to its top-level `Folder Listing`_.
Alternatively, one of the following actions may be selected:

Cancel
  |return to current file|

Folder Listing
--------------

A folder listing presents a list of the files and/r folders
that the current folder contains.
A forward slash [/] is appended to the name of each folder within the listing.

Folders that aren't readable won't be listed.
The listing may be further constrained by editor features
(e.g. the "Select Format" dialog).

* Selecting a file causes the editor to act on that file.

* Selecting a folder goes to its listing.

* Additionally, one of the following actions may be selected:

  Parent Folder
    Go to the listing for the folder that contains
    the one currently being listed.
    If currently listing the system root folder
    then go to the `Location Listing`_.

  Cancel
    |return to current file|

For actions that only require read access to the selected file
(e.g. the `Open`_ action):

* The listing won't contain files that aren't readable.

For actions that require write access to the selected file
(e.g. the `Save As`_ action):

* These additional actions may also be selected:

  Type File Name
    Go to the `Path Editor`_ so that the name of a new file can be entered.

Path Editor
-----------

The absolute path of the folder currently being listed
is shown within a read-only field,
and the name of the file currently being edited
is shown within an editable field.
If the file name extension is being imposed
(e.g. by the `Select Format`_ dialog)
then it's shown in bold and can't be edited.

Forward slashes [/] may be appended to and/or inserted into the file name.
This is how a subfolder (to any depth) can be specified.

Any number of parent folder references [../] may be prepended to the file name
in order to escape the folder currently being listed.

One of the following actions may be selected:

Done
  Create the file (including its subfolder if specified), and then select it.

Back
  Return to the `Folder Listing`_.

Cancel
  |return to current file|

