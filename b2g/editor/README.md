# Editor

## What works?

The following keybindings can be used within a document:

 * ctrl-b: Bolds typed characters
 * ctrl-i: Italicises typed characters
 * ctrl-u: Underlines typed characters
 * ctrl-n: Opens a new document
 * ctrl-s: Saves the current document
 * ctrl-shift-s: Save-as on currently-open document
 * ctrl-o: Opens a new document
 * Dots 1-4 chord: Speaks the current line
 * Dots 2-5 chord: Speaks the current word
 * Dots 3-6 chord: Speaks the current character

Formatting only works when typing new characters, and does not work for selection-based operations.

Word documents can be saved and loaded with bold, italic and underlining intact. Other formats may work but have not been extensively tested.

Arrowing around the text area provides a better, more accessible experience than is typically found in Android. In particular:

 * Arrowing up and down moves up and down between physical lines. On stock Android, arrowing only transitions along a single screen line, speaking only the first character on which focus lands. This is confusing, as it may take several presses to navigate a single line, and the spoken feedback is sub-optimal. With this editor, arrowing moves an entire line, as is typical on all other accessible platforms. Speaking is somewhat intermittent and I'm not sure why, but this is on the accessibility level and not due to the editor.
 * Bold, italic and underlined regions are spoken when arrowing through.

## What doesn't?

 * Printing. While this was included on the initial checklist, it was included last as a stretch goal. The Aspose Android library does not yet support rendering subsections of documents, and even so printing is only available on Android 4.4+. It should be possible to build supplemental printing services that accept entire documents, and indeed such probably already exist.
 * Advanced formatting. Headings and paragraph-level formatting elements do not yet work. The current foundation should support implementing them, but they weren't specified in this iteration and I ran out of time.

## Building

With the Android SDK installed, run the following to install to a connected device:

    $ ./gradlew installDebug

See the output of:

    $ ./gradlew tasks

for additional actions that can be performed.