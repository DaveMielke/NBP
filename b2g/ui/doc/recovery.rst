Recovery Mode
=============

.. |BRLTTY home page URL| replace:: http://brltty.com
.. |BRLTTY key table URL| replace:: http://brltty.com/doc/KeyBindings/brl-bg-all.html

.. |the SD card| replace:: |SD card path| (the removable SD card)
.. |the system cache| replace:: |system cache path| (the system cache partition)
.. |user data| replace:: |user data path| (the user data partition)

.. |user confirmation| replace:: It requires `User Confirmation`_.
.. |engineering build| replace:: It's only available within an engineering build.

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

The Action Menu
---------------

The braille menu offers the same functionality as the visual menu but in
a different way because it's been designed for a text-mode environment.
When it starts,
it shows a list summarizing the actions that can be selected,
and then presents the prompt::

  recovery>

To select an action, type its name and then press Enter.
Unambiguous abbreviations of action names are recognized.
Leading and/or trailing spaces are ignored.
Entering an empty line redisplays the action summary.

The Boot Action
~~~~~~~~~~~~~~~

This action reboots to Android.
|user confirmation|

The Clear Action
~~~~~~~~~~~~~~~~

This action wipes |the system cache|, and then reboots to Android.
|user confirmation|

The Details Action
~~~~~~~~~~~~~~~~~~

Display information about the current build.
For example::

  Kernel: 2.6.37
  Firmware: v3.1
  Hardware: armv7l

The Exit Action
~~~~~~~~~~~~~~~

This action terminates this menu.
|user confirmation|
|engineering build|

Once terminated, it's not that easy to restart the menu.
This action has only been defined so that the |product name| developers
can easily test changes to the menu.

The Log Action
~~~~~~~~~~~~~~

This action uses the ``vi`` editor to view the log
for the current Recovery Mode session.
The edit session is read-only in order to prevent any changes
from being saved (written back) to the log.

Type ``:q`` followed by Enter to exit the edit session.
If you've accidentally made any changes to the read-only edit buffer
then you'll need to use ``:q!`` instead.

The Off Action
~~~~~~~~~~~~~~

This action powers down the |product name|.
|user confirmation|

The Reset Action
~~~~~~~~~~~~~~~~

This action performs a factory reset, and then reboots to Android.
|user confirmation|

Both |the system cache| and |user data| are wiped.

The Shell Action
~~~~~~~~~~~~~~~~

This action starts an interactive Unix-style shell.
|engineering build|

Use the shell's ``exit`` command to return to this menu.

The Time Action
~~~~~~~~~~~~~~~

Display the current date and time.
The format is ``yyyy-mm-dd@hh:mm:ss``.
For example::

  2015-12-07@09:43:26

The Update Action
~~~~~~~~~~~~~~~~~

This action applies a `System Update`_, and then reboots to Android.
|user confirmation|

You must first choose the update that you'd like to apply
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

A single-character indicator is appended to the name of the current
file system entry that describes what it is.
The indicators are:

=========  ==================
Indicator  Type
---------  ------------------
\/         folder (directory)
\*         executable file
\|         FIFO (named pipe)
\=         socket
\%         character device
\$         block device
\@         symbolic link
\?         unknown
=========  ==================

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

Booting into Recovery Mode
--------------------------

There are a number of ways to boot the |product name| into Recovery Mode:

* Via the |user interface|:

  1) Go to `The Settings Screen`_::

       Space + o (dots 135)

  2) Ensure that `Developer Mode`_ is enabled.

  3) Go to `The System Maintenance Screen`_::

       Space + Dots78 + m (dots 134)

  4) Click on ``Recovery Mode``.

* When the |product name| is fully shut down:

  1) Press and hold VolumeDown.
  2) Slide `The Power Switch`_ to its ``on`` position.
  3) Continue to hold VolumeDown until ``Starting`` appears on the braille display.

* For developer builds
  (the super-user shell capability is disabled in user builds):

  1) Start an interactive shell on the |product name|
     with the Android SDK command::

       adb shell

  2) Reboot the |product name| into Recovery Mode with the command::

       reboot recovery
* Via `The Serial Port`_:

  1) Reboot the |product name| into Recovery Mode
     with the ``u-boot`` command::

       run recoverycmd

