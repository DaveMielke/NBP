Administration
--------------

The Settings Screen
~~~~~~~~~~~~~~~~~~~

Pressing Space+o (dots 135) takes you to the Settings screen for the
|user interface|.

The top line contains the following buttons:

Save Settings
  Save the current values of the settings for later restoration. This is a good
  way to checkpoint the configuration that you're most comfortable with.

Restore Settings
  Restore the setting values that were most recently saved. This is how to get
  back to the configuration that you're most comfortable with.

Reset to Defaults
  Restore the setting values to an internally-defined configuration. This is
  how to recover if, for example, you've accidentally messed up your saved 
  settings.

System Maintenance
  Go to `The System Maintenance Screen`_.

Each subsequent line contains a setting that can be changed, and is laid out in
columns as follows:

1) This column contains the name of the setting.

2) If it's a boolean setting then this column contains an on/off switch. If
   it's any other kind of setting then this column shows that setting's current
   value.

3) For non-boolean settings, this column contains a button that changes the
   setting to its *previous* (lower for numeric settings) value.

4) For non-boolean settings, this column contains a button that changes the
   setting to its *next* (higher for numeric settings) value.

General Settings
````````````````

The following settings are presented:

.. include:: settings-general.rst

Developer Settings
``````````````````

If `Developer Mode`_ is enabled, then these additional settings are presented:

.. include:: settings-developer.rst

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

