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

bind Backward+Dot1 BrailleInputOff
bind Forward+Dot4 BrailleInputOn

bind Backward+Dot3 LongPressOff
bind Forward+Dot6 LongPressOn

bind Backward+Dot7 OneHandOff
bind Forward+Dot8 OneHandOn

bind Backward+Forward DescribeStatus
bind Backward+Forward+Space DescribeBuild

bind Space+Dots12345678 Null

include required.keys.inc
include legacy.keys.inc
include developer-all.keys.inc
