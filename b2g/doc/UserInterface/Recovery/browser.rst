The File System Browser
-----------------------

The file system browser is used to look through the file system in order
to find the file that's to be processed by the currently selected action.
It's used, for example, by `The Update Action`_.

When the file system browser starts,
it shows a list summarizing `The Navigation Keys`_,
and then presents `The Top-Level Folder List`_.

Only the current entry of the current folder is displayed.
The line containing it is always rewritten,
so the navigation key summary is always just above it for easy reference.

A single-character indicator is appended to the name of the current
file system entry that describes what it is.
The indicators are:

.. include:: browser-indicators.rst

The Top-Level Folder List
~~~~~~~~~~~~~~~~~~~~~~~~~

The top-level folder list contains the volumes that can be browsed.
They are:

* |internal memory|
* |the SD card|
* |the system cache|
* |user data|

The Navigation Keys
~~~~~~~~~~~~~~~~~~~

The following |product name| keys can be used to browse the file system:

Dot1
  Go up to the previous entry within the current folder.

Dot4
  Go down to the next entry within the current folder.

Dot2
  Go back to the parent folder (equivalent to ``..`` within the visual 
  file system browser). If within `The Top-Level Folder List`_
  then exit the file system browser without choosing a file.

Dot5
  If the current entry is a folder then go into it. If it's a file 
  then choose it and exit the file system browser.

Dot3
  Exit the file system browser without choosing a file.

