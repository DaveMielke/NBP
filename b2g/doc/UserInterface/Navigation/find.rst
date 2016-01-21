Finding Text within the Current Screen Element
----------------------------------------------

To find a sequence of words within the text that's associated with the current
screen element, press Space+f (dots 124). This brings up a prompt (see
`Prompts`_) with the following header::

  find>

Enter one or more words (sequences of non-space characters) separated by
spaces. The same words must occur together, in the same order, within the text.
You don't need to know how many spaces are between each pair of words within
the text because however many spaces you enter will match any number of spaces
within the text. You also don't need to know which letters are in uppercase and
which are in lowercase because the search isn't case sensitive.

The first word you enter need only match the end of the corresponding word
within the text. Likewise, the last word you enter need only match the start of
the corresponding word within the text. Enter a leading space to force the
first word to match an entire word. Likewise, enter a trailing space to force
the last word to match an entire word.

These same rules apply if you enter a single word. Since it's both the first
and the last word, it need only match part (the start, middle, or end) of a
word within the text. Enter a leading space to force it to match to start of a
word, a trailing space to force it to match the end of a word, and both to
force it to match an entire word.

You can edit the word(s) that you're entering. Press Enter when you're done.

A forward search through the text is performed. If a match is found then the
braille display is repositioned to where it starts.

The following convenience key combinations have also been defined:

Space+Dot8+f (dots 124):
  Search forward through the text for the next match.

Space+Dot7+f (dots 124):
  Search backward through the text for the previous match.

Following a Reference
~~~~~~~~~~~~~~~~~~~~~

Since the ``find`` prompt is an input area, the current clipboard content
can be pasted into it. This capability makes it fairly easy to follow a
reference (``see``, ``see also``, etc).

The first step is to copy the reference to the clipboard:

1) Move to where the start of the reference is visible.
2) Hold Backward while pressing the cursor routing key behind its first character.
3) Move to where the end of the reference is visible.
4) Hold Forward while pressing the cursor routing key behind its last character.

The second step is to search through the text for a match:

5) Press Space+f (dots 124) to bring up the ``find`` prompt.
6) Press Space+Dot8+v (dots 1236) to paste the reference into the response.
7) Press Enter (dot 8) to search forward for the reference.

The next match may, of course, be another reference rather than the referenced
header. You may, therefore, need to continue searching further forward with
Space+Dot8+f (dots 124), or to search backward with Space+Dot7+f (dots 124).

