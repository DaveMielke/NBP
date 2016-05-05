The Actions Menu
================

.. |save changes dialog| replace::
  If `The Edit Area`_ contains changes
  then they're first saved (see the `Save Changes`_ dialog).

.. |file saved dialog| replace::

  The `File Saved`_ dialog is presented
  once the file has been successfully saved.

Press Android's Back key to leave the menu without selecting an action.

Edit
----

Perform an action on the text currently being edited.

* If text hasn't been selected:

  Paste
    Paste (insert) what's on the clipboard into the text
    at the cursor's location.

  Select All
    Select all of the text.

* If text has been selected:

  Copy
    Copy the current text selection to the clipboard.

  Cut
    Copy the current text selection to the clipboard and then delete it.

  Uppercase
    Translate all of the lowercase letters
    within the current text selection
    to uppercase.

  Lowercase
    Translate all of the uppercase letters
    within the current text selection
    to lowercase.

New
---

Start editing a new file.
`The Edit Area`_ and `The Current Path`_ are both cleared.
|save changes dialog|

Open
----

Edit an existing file.
|save changes dialog|
`The File Finder`_ is used to locate the file.

Save
----

Save the current file to persistent storage.
The content of `The Edit Area`_ is written
to the file referenced by `The Current Path`_.
If the path isn't known
then the `Save As`_ action is automatically performed instead.
|file saved dialog|

Save As
-------

Save the current file
to a new location,
in a different format,
etc.
The `Confirm Format`_ dialog is first used
in order to ensure that it'll be saved in the desired format.
`The File finder`_ is then used
either to locate or to specify the desired location.
|file saved dialog|

Send
----

Send the current file
to someone via EMail,
to another device via Bluetooth,
etc.
If you haven't already done so,
you'll be asked to select how files are to be sent.
First choose the mechanism, and then select one of the following actions:

Just Once
  Your current choice is temporary;
  you'll be asked again the next time.
  Select this action
  if you're not yet sure,
  if you think you might want to use another mechanism later,
  etc.

Always
  Your current choice is to be made permanent;
  it'll be difficult to change it later.
  Only select this action if you're absolutely sure.

EMail
~~~~~

Sending a file via EMail requires that:

* You have access to the internet.

  + To configure a Wi-Fi connection, go to Settings -> Wi-Fi.
    A shortcut is to press Space+s (dots 234),
    and then type the letter ``w`` (dots 2456).

* You've defined at least one EMail account.
  To define an EMail account, go to Settings -> Add Account -> EMail.

Android's EMail composition app will be launched,
the current file will be attached to the message being composed,
and you'll be placed into the (empty) message body editing area.
Other fields (the recipient, the subject, etc)
will also be empty and need to be filled in.

Delete
------

Delete an existing file.
`The File Finder`_ is used to locate it.
The "Delete File" dialog is used in order to ensure
that the correct file will be deleted.

More
----

This is an Androidism rather than an editor action.
Android limits the number of menu items
that may be on the screen at the same time.
This pseudo action is presented if that limit has been exceeded,
and causes the menu to be scrolled forward
in order to show the next set of items.

