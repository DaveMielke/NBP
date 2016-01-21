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

