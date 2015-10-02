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
this document. Please see |BRLTTY key table URL|.

Android's visual 
Recovery Mode menu can still be used, though braille must first be disabled. Those who prefer to use an external 
video monitor must press both volume keys together (VolumeDown + 
VolumeUp) in order to stop BRLTTY's |product name| braille driver. The 
|product name| keyboard can then be used to navigate the visual menu.

The Braille Menu
----------------

User Confirmation
~~~~~~~~~~~~~~~~~

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

The Boot Action
~~~~~~~~~~~~~~~

|user confirmation|

The Cache Action
~~~~~~~~~~~~~~~~

|the system cache|

|user confirmation|

The Exit Action
~~~~~~~~~~~~~~~

|user confirmation|

The Reset Action
~~~~~~~~~~~~~~~~

|the system cache|
|user data|

|user confirmation|

The Shell Action
~~~~~~~~~~~~~~~~

The Update Action
~~~~~~~~~~~~~~~~~

|user confirmation|

The File Browser
~~~~~~~~~~~~~~~~

* |the SD card|
* |the system cache|
* |user data|

Dot1
  Go up to the previous entry within the current folder.

Dot4
  Go down to the next entry within the current folder.

Dot2
  Go back to the parent folder (equivalent to ``..`` within the visual 
  menu). If within the initial volume list then exit the file browser 
  without choosing a file.

Dot5
  If the current entry is a folder then go into it. If it's a file 
  then choose it and exit the file browser.

Dot3
  Exit the file browser without choosing a file.

