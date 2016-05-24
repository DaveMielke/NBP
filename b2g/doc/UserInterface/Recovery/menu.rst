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

  Type: user
  Build: v4.2.9
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

