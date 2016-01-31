bind Dot8 Enter

bind Backward PanLeft
bind Forward PanRight

bind Left MoveLeft
bind Right MoveRight
bind Up MoveUp
bind Down MoveDown

bind Space+Left ScrollLeft
bind Space+Right ScrollRight
bind Space+Up ScrollUp
bind Space+Down ScrollDown

bind Dot7+Cursor DescribeCharacter
bind Dot8+Cursor SetLeft

bind Space+Dots123 ToTextStart
bind Space+Dots456 ToTextEnd

bind Space+Dot8+Dots14#c CopyToClipboard

bind Space+Dots1245#g BrailleCodeConfirm
bind Space+Dot7+Dots1245#g BrailleCodePrevious
bind Space+Dot8+Dots1245#g BrailleCodeNext

bind Space+Dot7+Dots2345#t TypingModeText
bind Space+Dot8+Dots2345#t TypingModeBraille
bind Space+Dots78+Dots2345#t TypingModeConfirm

bind Space+Dot7+Dots12#b TypingBoldOff
bind Space+Dot8+Dots12#b TypingBoldOn

bind Space+Dot7+Dots24#i TypingItalicOff
bind Space+Dot8+Dots24#i TypingItalicOn

bind Space+Dot7+Dots136#u TypingUnderlineOff
bind Space+Dot8+Dots136#u TypingUnderlineOn

bind Space+Dots235#6 LiteraryBrailleOn
bind Space+Dots236#8 LiteraryBrailleOff

bind Backward+Dot1 BrailleOff
bind Forward+Dot4 BrailleOn

bind Backward+Dot2 ReversePanningOff
bind Forward+Dot5 ReversePanningOn

bind Backward+Dot3 LongPressOff
bind Forward+Dot6 LongPressOn

bind Backward+Dot7 OneHandOff
bind Forward+Dot8 OneHandOn

bind Backward+Forward DescribeIndicators

bind Space+Dots12345678 Null

include required.keys.inc
include legacy.keys.inc
include developer-all.keys.inc
