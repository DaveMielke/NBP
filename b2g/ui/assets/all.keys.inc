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

bind Space+Dot8+Dots14#c CopyToClipboard

bind Space+Dots24#i InputModeConfirm
bind Space+Dot7+Dots24#i InputModeText
bind Space+Dot8+Dots24#i InputModeBraille

bind Space+Dots123#l LiteraryBrailleConfirm
bind Space+Dot7+Dots123#l LiteraryBrailleOff
bind Space+Dot8+Dots123#l LiteraryBrailleOn

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
