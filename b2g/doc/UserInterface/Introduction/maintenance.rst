The System Maintenance Screen
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

Pressing Space+Dots78+m (dots 134) takes you to the
System Maintenance screen for the |user interface|.

A text area at the top of the screen is used to show progress information
for the currently running (or most recently invoked) operation.
This text area is initially empty.
The list of available operations is below it.

Maintenance Operations
``````````````````````

The following operations may be performed:

.. |uses recovery mode| replace::

  The |product name| will be temporarily rebooted into `Recovery Mode`_.

.. |uses file picker| replace::

  A `File Picker`_ will be invoked so that you can
  locate and select the needed file.

Restart System
  Reboot the |product name|.

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

System Update
`````````````

System updates are also known as **OTA** (over-the-air) updates.
They're used to update either the whole Android system
or just parts of it.

A system update is a ``.zip`` file that contain special meta-data
(including an electronic signature).

File Picker
```````````

Some of the maintenance operations require you to choose a file.
Android refers to the type of application that enables you to choose a file
as a file picker.

If you haven't used a file picker yet,
or if you haven't yet chosen your favourite one,
then you'll be presented with a list of the file pickers
that are currently installed on the |product name|, 
and be asked to select the one that the action should be completed with.
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
```````````

