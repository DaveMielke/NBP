The System Maintenance Screen
-----------------------------

Pressing Space+Dots78+m (dots 134) takes you to the
System Maintenance screen for the |user interface|.

A text area at the top of the screen is used to show progress information
for the currently running (or most recently performed) operation.
This text area is initially empty.
The list of available operations is below it.

Maintenance Operations
~~~~~~~~~~~~~~~~~~~~~~

The following operations may be performed:

.. |uses recovery mode| replace::

  The |product name| will be temporarily rebooted into `Recovery Mode`_.

.. |uses file picker| replace::

  A `File Picker`_ will be invoked so that you can
  locate and select the needed file.

Restart System
  Reboot the |product name|.

Update User Interface
  Upgrade to the latest version of the |user interface|.
  See `Updating Applications`_ for details.
  |uses internet|

Update NBP Editor
  Upgrade to the latest version of the NBP Editor.
  See `Updating Applications`_ for details.
  |uses internet|

Verify System Update
  Verify a `System Update`_ without applying it.
  |uses file picker|

Update System
  Apply a `System Update`_.
  |uses file picker|
  |uses recovery mode|

Recovery Mode
  Reboot the |product name| into `Recovery Mode`_.

View Recovery Log
  View the log of the most recent reboot of the |product name|
  into `Recovery Mode`_.

Clear Cache
  Reinitialize the system cache partition.
  |uses recovery mode|

Factory Reset
  Reinitialize the user data and system cache partitions.
  |uses recovery mode|

Boot Loader
  Reboot the |product name| into its `Boot Loader`_.

Updating Applications
~~~~~~~~~~~~~~~~~~~~~

1) You'll be asked to choose a package downloader.
   We recommend |recommended package downloader|.

2) When the download is complete, select ``Open File``.
   You'll then see lots of information about the package.
   Compare the new version of the package
   with that of the already-installed one.
   Select ``Install`` if you wish to proceed or ``Cancel`` if not.

3) You'll be asked to choose a package installer.
   We recommend |recommended package installer|.

4) You'll be asked to confirm that you really want
   to replace the currently-installed version with the new one.

5) You may be asked to confirm that the application should be allowed
   to perform various privileged system operations.

6) The currently running version will be replaced by the new one.
   If the application is running then it'll be stopped.
   If you're updating the |user interface| then it'll be restarted, so don't
   worry if the keys stop responding and the display goes blank for a moment.

7) Click ``Done`` to clear the confirmation screen.

System Update
~~~~~~~~~~~~~

System updates are also known as **OTA** (over-the-air) updates.
They're used to update either the whole Android system
or just parts of it.

A system update is a ``.zip`` file that contain special meta-data
(including an electronic signature).

File Picker
~~~~~~~~~~~

Some of the maintenance operations require you to choose a file.
Android refers to the type of application that enables you to choose a file
as a file picker.

If you haven't used a file picker yet,
or if you haven't yet chosen your favourite one,
then you'll be presented with a list of the file pickers
that are currently installed on the |product name|, 
and be asked to select the one that the action should be completed with.
We recommend |recommended file picker|.

Select the one you'd like to use (or just try) by clicking on it,
and then click on one of the buttons at the bottom:

Always
  This button will make your selection permanent. Only make this choice
  if you're absolutely sure that you've made the right selection.

Once
  This button makes your selection temporary. You'll be presented
  with this dialog again the next time you need a file picker.
  Make this choice if you're not sure yet or if you're still trying them out.

Boot Loader
~~~~~~~~~~~

