The B2G User Interface
======================

.. contents::

Braille Indicators
------------------

The special character rendered as dots 3678 is used to represent a 
character that doesn't have its own defined representation.

When on an editable text field: Dot 8 is used to show where the cursor 
is, and dots 78 are used to show which characters have been selected. 
Note that the cursor isn't shown when at least one character has been 
selected.

A **checkbox** is rendered as either a space (meaning unchecked) or an 
``X`` (meaning checked) enclosed within [square] brackets, followed by 
its label. For example::

  [ ] This box is not checked.
  [X] This box is checked.

A **switch** is rendered as a **checkbox**. The box is checked if the 
switch is in the **on** position, and unchecked if the switch is in the 
**off** position. For example::

  [ ] Off
  [X] On

If meaningful text for a significant screen element cannot be found then 
it is rendered as its widget type enclosed within (parentheses). For 
example::

  (FrameLayout)
  (ImageView)
  (ListView)

Basic Screen Navigation
-----------------------

While the Left, Right, Up, and Down keys (of the D-Pad) can be used to 
navigate the screen, they don't actually work very well. The reason they 
don't is that they try to be too accurate. If the next screen element in 
the requested direction isn't close enough to being exactly in that 
direction then it doesn't get found.

The best way to navigate the screen is to use the Forward and Backward 
keys because they move sequentially through the screen elements without 
missing any of them. The Forward key stops at the end of the screen, and 
the Backward key stops at the beginning of the screen. While these
(Forward and Backward) keys, when used on their own, perform more
refined navigation within certain contexts (see below), combining them
with the Space key can always be used to force an immediate, direct
move to the start of the next or previous screen element.

If the text of a screen element is longer than the braille display 
and/or has more than one line then the Forward key pans to the right, 
wraps to the start of the next line, etc, as needed, so that all of the 
text is presented. When it reaches the end, it then moves to the start 
of the next element. The Backward key moves through such text in the 
reverse direction, and when it reaches the start of the text it then 
moves to the start of the previous element.

These key combinations perform basic system actions:

Space+Dots123456
  The Home button. Go to the app launcher.

Space+z (dots 1356)
  The Back button.

Space+m (dots 134)
  The Menu button. Go to the current app's main choices screen.

Space+n (dots 1345)
  Go to the Notifications screen.

Space+r (dots 1235)
  Go to the Recent Apps screen.

Space+t (dots 2345)
  Display the current date and time in the ISO 8601 international format
  (YYYY-MM-DD hh:mm:ss). The displayed time is continually updated.

Space+Dots1478
  Go to the Power Off screen.

Center (on the D-Pad)
  Tap (click) the current screen element.

Space+Center
  Hold (long click) the current screen element.

Dot8+c (dots 148)
  Copy all of the text to the clipboard.

Editable Text Fields
--------------------

Dots 1 through 6 are used, as defined by the `North American Braille 
Computer Code`_, to type characters, and the Space key is used to type a 
space. Add dot 7 to a letter to make it uppercase. Add dots 7 and 8 to a 
character to type its control variant (see `ASCII Control Characters`_). 
If no characters have been selected then, when a character is typed, it 
is inserted where the cursor is. If characters have been selected then 
the next typed character replaces them.

Dot 8 is the Enter key. If the field supports more than one line, this 
key ends the current line and starts a new one.

Dot 7 is the Backspace key. It deletes the character to the left of the 
cursor. If characters have been selected then it deletes all of them.

Space+d (dots 145) is the Delete key. It deletes the character where the 
cursor is. If characters have been selected then it deletes all of them.

The Forward and Backward keys navigate through the text as defined 
previously, except that they will not leave the field. They must be used 
in combination with the Space key to force an immediate move to the 
start of the next or previous field.

The four directional keys (of the D-Pad) move the cursor through the 
text, one step at a time. They will not leave the field. The braille 
display is panned, as needed, such that the cursor is always visible.

The Left key moves the cursor to the previous character of the current 
line. If the cursor is on the first character of the line then it wraps 
to the last character of the previous line. If characters are selected 
then the cursor is moved to just before the first selected character, 
and the character selection is cleared.

The Right key moves the cursor to the next character of the current 
line. If the cursor is on the last character of the line then it wraps 
to the first character of the next line. If characters are selected then 
the cursor is moved to just after the last selected character, and the 
character selection is cleared.

The Up key moves the cursor to the same column of the previous line. If 
the previous line is too short then the cursor is also moved leftward to 
just after its last character. If characters are selected then the 
cursor is moved to just above the first selected character, and the 
character selection is cleared.

The Down key moves the cursor to the same column of the next line. If 
the next line is too short then the cursor is also moved leftward to 
just after its last character. If characters are selected then the 
cursor is moved to just below the last selected character, and the 
character selection is cleared.

Pressing a cursor routing key brings the cursor to that character. If 
characters are selected then the character selection is cleared.

Pressing a cursor routing key in combination with the Backward key sets 
the first selected character, and pressing a cursor routing key in 
combination with the Forward key sets the last selected character. If 
either of these actions is performed while characters are already 
selected then the start or end of the selection is readjusted as 
requested. The sequence of selected characters may:

* Span multiple lines.
* Begin anywhere on its first line.
* End anywhere on its last line.

Pressing a cursor routing key in combination with the Space key scrolls 
the braille display to the right such that the visible portion of the 
current line begins with that character.

These key combinations perform actions on editable text fields:

Dot8+a (dot 1)
  Select all of the text.

Dot8+x (dots 13468)
  Cut the currently selected text to the clipboard.

Dot8+c (dots 148)
  Copy the currently selected text to the clipboard. If no text is
  selected then all of the text is copied.

Dot8+v (dots 12368)
  Paste the clipboard content into the text being edited. If no
  characters have been selected then the clipboard content is inserted
  where the cursor is. If characters have been selected then the
  clipboard content replaces them.

Legacy Key Combinations
-----------------------

Space+Dot1
  Arrow up. Equivalent to the Up key (on the D-Pad).

Space+Dot4
  Arrow down. Equivalent to the Down key (on the D-Pad).

Space+Dot3
  Arrow left. Equivalent to the Left key (on the D-Pad).

Space+Dot6
  Arrow right. Equivalent to the Right key (on the D-Pad).

Space+Dots45
  Enter a ``tab``. A number of apps use this character for moving
  forward through their control widgets.

Space+Dots12
  Enter a ``shift tab``. A number of apps use this character for moving
  backward through their control widgets.

Space+x (dots 1346)
  Enter a control character (see `ASCII Control Characters`_). This
  key combination is a sticky modifier. The next character typed will
  be translated into its control variant. For example, another way to
  enter a ``tab`` character is to type Space+x followed by the letter ``i``.

North American Braille Computer Code
------------------------------------

=========  =======  =======  =======
Character  Unicode  Braille  Dots
---------  -------  -------  -------
space      U+0020   ⠀        no dots
\!         U+0021   ⠮        2346   
\"         U+0022   ⠐        5      
\#         U+0023   ⠼        3456   
\$         U+0024   ⠫        1246   
\%         U+0025   ⠩        146    
\&         U+0026   ⠯        12346  
\'         U+0027   ⠄        3      
\(         U+0028   ⠷        12356  
\)         U+0029   ⠾        23456  
\*         U+002A   ⠡        16     
\+         U+002B   ⠬        346    
\,         U+002C   ⠠        6      
\-         U+002D   ⠤        36     
\.         U+002E   ⠨        46     
\/         U+002F   ⠌        34     
\0         U+0030   ⠴        356    
\1         U+0031   ⠂        2      
\2         U+0032   ⠆        23     
\3         U+0033   ⠒        25     
\4         U+0034   ⠲        256    
\5         U+0035   ⠢        26     
\6         U+0036   ⠖        235    
\7         U+0037   ⠶        2356   
\8         U+0038   ⠦        236    
\9         U+0039   ⠔        35     
\:         U+003A   ⠱        156    
\;         U+003B   ⠰        56     
\<         U+003C   ⠣        126    
\=         U+003D   ⠿        123456 
\>         U+003E   ⠜        345    
\?         U+003F   ⠹        1456   
\@         U+0040   ⡈        47     
\A         U+0041   ⡁        17     
\B         U+0042   ⡃        127    
\C         U+0043   ⡉        147    
\D         U+0044   ⡙        1457   
\E         U+0045   ⡑        157    
\F         U+0046   ⡋        1247   
\G         U+0047   ⡛        12457  
\H         U+0048   ⡓        1257   
\I         U+0049   ⡊        247    
\J         U+004A   ⡚        2457   
\K         U+004B   ⡅        137    
\L         U+004C   ⡇        1237   
\M         U+004D   ⡍        1347   
\N         U+004E   ⡝        13457  
\O         U+004F   ⡕        1357   
\P         U+0050   ⡏        12347  
\Q         U+0051   ⡟        123457 
\R         U+0052   ⡗        12357  
\S         U+0053   ⡎        2347   
\T         U+0054   ⡞        23457  
\U         U+0055   ⡥        1367   
\V         U+0056   ⡧        12367  
\W         U+0057   ⡺        24567  
\X         U+0058   ⡭        13467  
\Y         U+0059   ⡽        134567 
\Z         U+005A   ⡵        13567  
\[         U+005B   ⡪        2467   
\\         U+005C   ⡳        12567  
\]         U+005D   ⡻        124567 
\^         U+005E   ⡘        457    
\_         U+005F   ⠸        456    
\`         U+0060   ⠈        4      
\a         U+0061   ⠁        1      
\b         U+0062   ⠃        12     
\c         U+0063   ⠉        14     
\d         U+0064   ⠙        145    
\e         U+0065   ⠑        15     
\f         U+0066   ⠋        124    
\g         U+0067   ⠛        1245   
\h         U+0068   ⠓        125    
\i         U+0069   ⠊        24     
\j         U+006A   ⠚        245    
\k         U+006B   ⠅        13     
\l         U+006C   ⠇        123    
\m         U+006D   ⠍        134    
\n         U+006E   ⠝        1345   
\o         U+006F   ⠕        135    
\p         U+0070   ⠏        1234   
\q         U+0071   ⠟        12345  
\r         U+0072   ⠗        1235   
\s         U+0073   ⠎        234    
\t         U+0074   ⠞        2345   
\u         U+0075   ⠥        136    
\v         U+0076   ⠧        1236   
\w         U+0077   ⠺        2456   
\x         U+0078   ⠭        1346   
\y         U+0079   ⠽        13456  
\z         U+007A   ⠵        1356   
\{         U+007B   ⠪        246    
\|         U+007C   ⠳        1256   
\}         U+007D   ⠻        12456  
\~         U+007E   ⠘        45     
=========  =======  =======  =======

ASCII Control Characters
------------------------

=========  =======  =======  =======  ========  ========================
Character  Unicode  Braille  Dots     Mnemonic  Meaning
---------  -------  -------  -------  --------  ------------------------
\`         U+0000   ⠈        4        NUL       Null Character
\a         U+0001   ⠁        1        SOH       Start of Header
\b         U+0002   ⠃        12       STX       Start of Text
\c         U+0003   ⠉        14       ETX       End of Text
\d         U+0004   ⠙        145      EOT       End of Transmission
\e         U+0005   ⠑        15       ENQ       Enquiry
\f         U+0006   ⠋        124      ACK       Positive Acknowledgement
\g         U+0007   ⠛        1245     BEL       Ring Bell
\h         U+0008   ⠓        125      BS        Back Space
\i         U+0009   ⠊        24       HT        Horizontal Tab
\j         U+000A   ⠚        245      LF        Line Feed
\k         U+000B   ⠅        13       VT        Vertical Tab
\l         U+000C   ⠇        123      FF        Form Feed
\m         U+000D   ⠍        134      CR        Carriage Return
\n         U+000E   ⠝        1345     SO        Shift Out
\o         U+000F   ⠕        135      SI        Shift In
\p         U+0010   ⠏        1234     DLE       Data Link Escape
\q         U+0011   ⠟        12345    DC1       Direct Control 1 (X-On)
\r         U+0012   ⠗        1235     DC2       Direct Control 2
\s         U+0013   ⠎        234      DC3       Direct Control 3 (X-Off)
\t         U+0014   ⠞        2345     DC4       Direct Control 4
\u         U+0015   ⠥        136      NAK       Negative Acknowledgement
\v         U+0016   ⠧        1236     SYN       Synchronize
\w         U+0017   ⠺        2456     ETB       End of Text Block
\x         U+0018   ⠭        1346     CAN       Cancel
\y         U+0019   ⠽        13456    EM        End of Medium
\z         U+001A   ⠵        1356     SUB       Substitution Character
\{         U+001B   ⠪        246      ESC       Escape
\|         U+001C   ⠳        1256     FS        Field Separator
\}         U+001D   ⠻        12456    GS        Group Separator
\~         U+001E   ⠘        45       RS        Record Separator
\_         U+001F   ⠸        456      US        Unit Separator
\?         U+007F   ⠹        1456     DEL       Delete
=========  =======  =======  =======  ========  ========================

