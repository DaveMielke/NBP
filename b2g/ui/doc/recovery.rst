Recovery Mode
=============

.. |BRLTTY home page URL| replace:: http://brltty.com
.. |BRLTTY key table URL| replace:: http://brltty.com/doc/KeyBindings/brl-bg-all.html

.. |the SD card| replace:: ``/sdcard`` (the removable SD card)
.. |the system cache| replace:: ``/cache`` (the system cache partition)
.. |user data| replace:: ``/data`` (the user data partition)

.. |user confirmation| replace:: This requires `User Confirmation`_.

Recovery Mode is a single-user environment within which system 
maintenance can be performed safely. The |user interface| isn't available in 
Recovery Mode. Braille access in Recovery Mode is provided by BRLTTY
(see |BRLTTY home page URL|).

BRLTTY's key bindings for the |product name| are different than those 
defined by the |user interface|. Describing them is beyond the scope of 
this document - please see |BRLTTY key table URL|.

Android's visual 
Recovery Mode menu can still be used, though braille must first be disabled. Those who prefer to use an external 
video monitor must press both volume keys together (VolumeDown + 
VolumeUp) in order to stop BRLTTY's |product name| braille driver. The 
|product name| keyboard can then be used to navigate the visual menu.

The Braille Menu
----------------

The braille menu offers the same functionality as the visual menu but in
a different way because it's been designed for a text-mode environment.
When it starts,
it shows a list summarizing the actions that can be selected,
and then presents the prompt::

  action>

To select an action, type its name and then press Enter.
Unambiguous abbreviations of action names are recognized.
Leading and/or trailing spaces are ignored.
Entering an empty line redisplays the action summary.

The Boot Action
~~~~~~~~~~~~~~~

This action reboots to Android.
|user confirmation|

The Cache Action
~~~~~~~~~~~~~~~~

This action wipes |the system cache|, and then reboots to Android.
|user confirmation|

The Exit Action
~~~~~~~~~~~~~~~

This action terminates this menu.
|user confirmation|

Once terminated, it's not that easy to restart the menu.
This action has only been defined so that the |product name| developers
can easily test changes to the menu.

The Reset Action
~~~~~~~~~~~~~~~~

This action performs a factory reset, and then reboots to Android.
Both |the system cache| and |user data| are wiped.
|user confirmation|

The Shell Action
~~~~~~~~~~~~~~~~

This action starts an interactive Unix-style shell.
Use the shell's ``exit`` command to return to this menu.

The Update Action
~~~~~~~~~~~~~~~~~

This action applies an OTA (over-the-air) update,
and then reboots to Android.
|user confirmation|

OTA updates are ``.zip`` files that contain special meta-data
(including an electronic signature).
They're used to update either the whole Android system
or just parts of it.
You must first choose the specific update that you'd like to apply
via `The File System Browser`_ (which is automatically started for you).

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

The following suffixes are used to describe the current file system entry:

=========  ========
Character  Type
---------  --------
/          a folder
=========  ========

The Top-Level Folder List
~~~~~~~~~~~~~~~~~~~~~~~~~

The top-level folder list contains the volumes that can be browsed.
They are:

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

User Confirmation
-----------------

Some actions require user confirmation.
The prompt is a brief description of the action that's to be performed,
followed by a question mark (``?``).
The user's response may be:

``yes`` (or any abbreviation thereof)
  The action is performed.

``no`` (or any abbreviation thereof)
  The action is cancelled.

anything else
  The confirmation prompt is reissued.

