Braille-only Dialogs
--------------------

A number of braille-only dialogs are used in order to directly communicate to
or interact with the user. They don't appear on an external video monitor.

Messages
~~~~~~~~

Messages are used to give feedback to the user for significant events. These
include (but aren't limited to):

* The successful changing of a setting.

* The timeout of a partially entered request, e.g. the control modifier has
  been pressed but the (then required) letter or special symbol hasn't been
  typed quickly enough (see `Typing a Control Character`_).

A message is a single-line, read-only dialog. No navigation may be performed
within it. It remains on the braille display for |message time|.

Popups
~~~~~~

Popups are used to present user-requested data as well as important system
information. This includes (but isn't limited to):

* The arrival of an Android notification.

* The description of a character (see `Identifying an Unrecognized
  Character`_).

* The values of various status indicators (see `Checking Status Indicators`_).

* An `Action Chooser`_.

A popup is a multi-line, read-only dialog. Normal navigation may be
performed within it. Dismiss it by pressing Enter. It's automatically dismissed
if no navigation operations have been performed within it for |popup time|.

Prompts
~~~~~~~

Prompts are used to request information from the user. This includes (but isn't
limited to):

* Requesting the value of a Unicode character (see `Indirectly Typing Any
  Character`_).

* Requesting the text to search for within the current screen element (see
  `Finding Text within the Current Screen Element`_).

A prompt is a read-write dialog. Normal navigation and editing may be performed
within its response area. Press Enter once the requested information has been
entered.

The prompt's header and response area are always on the first line. The
|user interface| may add helpful information to the rest of the first line
and/or to additional lines.  

The response to a prompt is remembered. The response area of a prompt is
initially empty, but, from then on, it's initialized to the previous response
entered for that prompt. The remembered response is selected (see `Selecting
Text`_) so that it can be easily replaced.

Action Chooser
~~~~~~~~~~~~~~

An action chooser is a special kind of popup (see `Popups`_)
that presents you with a list of actins
and lets you choose which one to invoke.
For places where it's used, see:

* `Ways to Learn`_
* `Shortcuts to Useful Android Screens`_

Each line of the dialog shows one of the available actions. It contains
the name of the action,
the key combination that invokes it (when not within the chooser),
and a summary of what it does.
Press Center to invoke it from within the chooser.
Press Enter to leave the chooser without invoking an action.

