Booting into Recovery Mode
--------------------------

There are a number of ways to boot the |product name| into Recovery Mode:

* Via the |user interface|:

  1) Go to `The Settings Screen`_::

       Space + o (dots 135)

  2) Go to `The System Maintenance Screen`_::

       Space + Dots78 + m (dots 134)

  3) Click on ``Recovery Mode``.

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

