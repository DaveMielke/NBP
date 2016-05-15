/*
 * Copyright 2008, The Android Open Source Project
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *  * Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS ``AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

#ifndef ANPKeyCodes_DEFINED
#define ANPKeyCodes_DEFINED

/*  List the key codes that are set to a plugin in the ANPKeyEvent.
 
    These exactly match the values in android/view/KeyEvent.java and the
    corresponding .h file android/keycodes.h.
*/
enum ANPKeyCodes {
    kUnknown_ANPKeyCode = 0,

    kSoftLeft_ANPKeyCode = 1,
    kSoftRight_ANPKeyCode = 2,
    kHome_ANPKeyCode = 3,
    kBack_ANPKeyCode = 4,
    kCall_ANPKeyCode = 5,
    kEndCall_ANPKeyCode = 6,
    k0_ANPKeyCode = 7,
    k1_ANPKeyCode = 8,
    k2_ANPKeyCode = 9,
    k3_ANPKeyCode = 10,
    k4_ANPKeyCode = 11,
    k5_ANPKeyCode = 12,
    k6_ANPKeyCode = 13,
    k7_ANPKeyCode = 14,
    k8_ANPKeyCode = 15,
    k9_ANPKeyCode = 16,
    kStar_ANPKeyCode = 17,
    kPound_ANPKeyCode = 18,
    kDpadUp_ANPKeyCode = 19,
    kDpadDown_ANPKeyCode = 20,
    kDpadLeft_ANPKeyCode = 21,
    kDpadRight_ANPKeyCode = 22,
    kDpadCenter_ANPKeyCode = 23,
    kVolumeUp_ANPKeyCode = 24,
    kVolumeDown_ANPKeyCode = 25,
    kPower_ANPKeyCode = 26,
    kCamera_ANPKeyCode = 27,
    kClear_ANPKeyCode = 28,
    kA_ANPKeyCode = 29,
    kB_ANPKeyCode = 30,
    kC_ANPKeyCode = 31,
    kD_ANPKeyCode = 32,
    kE_ANPKeyCode = 33,
    kF_ANPKeyCode = 34,
    kG_ANPKeyCode = 35,
    kH_ANPKeyCode = 36,
    kI_ANPKeyCode = 37,
    kJ_ANPKeyCode = 38,
    kK_ANPKeyCode = 39,
    kL_ANPKeyCode = 40,
    kM_ANPKeyCode = 41,
    kN_ANPKeyCode = 42,
    kO_ANPKeyCode = 43,
    kP_ANPKeyCode = 44,
    kQ_ANPKeyCode = 45,
    kR_ANPKeyCode = 46,
    kS_ANPKeyCode = 47,
    kT_ANPKeyCode = 48,
    kU_ANPKeyCode = 49,
    kV_ANPKeyCode = 50,
    kW_ANPKeyCode = 51,
    kX_ANPKeyCode = 52,
    kY_ANPKeyCode = 53,
    kZ_ANPKeyCode = 54,
    kComma_ANPKeyCode = 55,
    kPeriod_ANPKeyCode = 56,
    kAltLeft_ANPKeyCode = 57,
    kAltRight_ANPKeyCode = 58,
    kShiftLeft_ANPKeyCode = 59,
    kShiftRight_ANPKeyCode = 60,
    kTab_ANPKeyCode = 61,
    kSpace_ANPKeyCode = 62,
    kSym_ANPKeyCode = 63,
    kExplorer_ANPKeyCode = 64,
    kEnvelope_ANPKeyCode = 65,
    kNewline_ANPKeyCode = 66,
    kDel_ANPKeyCode = 67,
    kGrave_ANPKeyCode = 68,
    kMinus_ANPKeyCode = 69,
    kEquals_ANPKeyCode = 70,
    kLeftBracket_ANPKeyCode = 71,
    kRightBracket_ANPKeyCode = 72,
    kBackslash_ANPKeyCode = 73,
    kSemicolon_ANPKeyCode = 74,
    kApostrophe_ANPKeyCode = 75,
    kSlash_ANPKeyCode = 76,
    kAt_ANPKeyCode = 77,
    kNum_ANPKeyCode = 78,
    kHeadSetHook_ANPKeyCode = 79,
    kFocus_ANPKeyCode = 80,
    kPlus_ANPKeyCode = 81,
    kMenu_ANPKeyCode = 82,
    kNotification_ANPKeyCode = 83,
    kSearch_ANPKeyCode = 84,
    kMediaPlayPause_ANPKeyCode = 85,
    kMediaStop_ANPKeyCode = 86,
    kMediaNext_ANPKeyCode = 87,
    kMediaPrevious_ANPKeyCode = 88,
    kMediaRewind_ANPKeyCode = 89,
    kMediaFastForward_ANPKeyCode = 90,
    kMute_ANPKeyCode = 91,
    kPageUp_ANPKeyCode = 92,
    kPageDown_ANPKeyCode = 93,
    kPictsymbols_ANPKeyCode = 94,
    kSwitchCharset_ANPKeyCode = 95,
    kButtonA_ANPKeyCode = 96,
    kButtonB_ANPKeyCode = 97,
    kButtonC_ANPKeyCode = 98,
    kButtonX_ANPKeyCode = 99,
    kButtonY_ANPKeyCode = 100,
    kButtonZ_ANPKeyCode = 101,
    kButtonL1_ANPKeyCode = 102,
    kButtonR1_ANPKeyCode = 103,
    kButtonL2_ANPKeyCode = 104,
    kButtonR2_ANPKeyCode = 105,
    kButtonThumbL_ANPKeyCode = 106,
    kButtonThumbR_ANPKeyCode = 107,
    kButtonStart_ANPKeyCode = 108,
    kButtonSelect_ANPKeyCode = 109,
    kButtonMode_ANPKeyCode = 110,
    kEscape_ANPKeyCode = 111,
    kForwardDel_ANPKeyCode = 112,
    kCtrlLeft_ANPKeyCode = 113,
    kCtrlRight_ANPKeyCode = 114,
    kCapsLock_ANPKeyCode = 115,
    kScrollLock_ANPKeyCode = 116,
    kMetaLeft_ANPKeyCode = 117,
    kMetaRight_ANPKeyCode = 118,
    kFunction_ANPKeyCode = 119,
    kSysRq_ANPKeyCode = 120,
    kBreak_ANPKeyCode = 121,
    kMoveHome_ANPKeyCode = 122,
    kMoveEnd_ANPKeyCode = 123,
    kInsert_ANPKeyCode = 124,
    kForward_ANPKeyCode = 125,
    kMediaPlay_ANPKeyCode = 126,
    kMediaPause_ANPKeyCode = 127,
    kMediaClose_ANPKeyCode = 128,
    kMediaEject_ANPKeyCode = 129,
    kMediaRecord_ANPKeyCode = 130,
    kF1_ANPKeyCode = 131,
    kF2_ANPKeyCode = 132,
    kF3_ANPKeyCode = 133,
    kF4_ANPKeyCode = 134,
    kF5_ANPKeyCode = 135,
    kF6_ANPKeyCode = 136,
    kF7_ANPKeyCode = 137,
    kF8_ANPKeyCode = 138,
    kF9_ANPKeyCode = 139,
    kF10_ANPKeyCode = 140,
    kF11_ANPKeyCode = 141,
    kF12_ANPKeyCode = 142,
    kNumLock_ANPKeyCode = 143,
    kNumPad0_ANPKeyCode = 144,
    kNumPad1_ANPKeyCode = 145,
    kNumPad2_ANPKeyCode = 146,
    kNumPad3_ANPKeyCode = 147,
    kNumPad4_ANPKeyCode = 148,
    kNumPad5_ANPKeyCode = 149,
    kNumPad6_ANPKeyCode = 150,
    kNumPad7_ANPKeyCode = 151,
    kNumPad8_ANPKeyCode = 152,
    kNumPad9_ANPKeyCode = 153,
    kNumPadDivide_ANPKeyCode = 154,
    kNumPadMultiply_ANPKeyCode = 155,
    kNumPadSubtract_ANPKeyCode = 156,
    kNumPadAdd_ANPKeyCode = 157,
    kNumPadDot_ANPKeyCode = 158,
    kNumPadComma_ANPKeyCode = 159,
    kNumPadEnter_ANPKeyCode = 160,
    kNumPadEquals_ANPKeyCode = 161,
    kNumPadLeftParen_ANPKeyCode = 162,
    kNumPadRightParen_ANPKeyCode = 163,
    kVolumeMute_ANPKeyCode = 164,
    kInfo_ANPKeyCode = 165,
    kChannelUp_ANPKeyCode = 166,
    kChannelDown_ANPKeyCode = 167,
    kZoomIn_ANPKeyCode = 168,
    kZoomOut_ANPKeyCode = 169,
    kTv_ANPKeyCode = 170,
    kWindow_ANPKeyCode = 171,
    kGuide_ANPKeyCode = 172,
    kDvr_ANPKeyCode = 173,
    kBookmark_ANPKeyCode = 174,
    kCaptions_ANPKeyCode = 175,
    kSettings_ANPKeyCode = 176,
    kTvPower_ANPKeyCode = 177,
    kTvInput_ANPKeyCode = 178,
    kStbPower_ANPKeyCode = 179,
    kStbInput_ANPKeyCode = 180,
    kAvrPower_ANPKeyCode = 181,
    kAvrInput_ANPKeyCode = 182,
    kProgRed_ANPKeyCode = 183,
    kProgGreen_ANPKeyCode = 184,
    kProgYellow_ANPKeyCode = 185,
    kProgBlue_ANPKeyCode = 186,
    kAppSwitch_ANPKeyCode = 187,

    // NOTE: If you add a new keycode here you must also add it to several other files.
    //       Refer to frameworks/base/core/java/android/view/KeyEvent.java for the full list.
    
    
    
    /*{KW} 256 Braille key combinations*/
	kBRL_DOTS_7_ANPKeyCode	=	257,	
	kBRL_DOTS_3_ANPKeyCode	=	258,	//  punctuation ' 
	kBRL_DOTS_37_ANPKeyCode	=	259,	
	kBRL_DOTS_2_ANPKeyCode	=	260,	// digit 1  
	kBRL_DOTS_27_ANPKeyCode	=	261,	
	kBRL_DOTS_23_ANPKeyCode	=	262,	// digit 2 
	kBRL_DOTS_237_ANPKeyCode	=	263,	
	kBRL_DOTS_1_ANPKeyCode	=	264,	// a 
	kBRL_DOTS_17_ANPKeyCode	=	265,	// A
	kBRL_DOTS_13_ANPKeyCode	=	266,	// k 
	kBRL_DOTS_137_ANPKeyCode	=	267,	// K 
	kBRL_DOTS_12_ANPKeyCode	=	268,	// b 
	kBRL_DOTS_127_ANPKeyCode	=	269,	// B
	kBRL_DOTS_123_ANPKeyCode	=	270,	// l
	kBRL_DOTS_1237_ANPKeyCode	=	271,	// L
	kBRL_DOTS_4_ANPKeyCode	=	272,	// sign ` 
	kBRL_DOTS_47_ANPKeyCode	=	273,	// sign @ 
	kBRL_DOTS_34_ANPKeyCode	=	274,	// math / 
	kBRL_DOTS_347_ANPKeyCode	=	275,	
	kBRL_DOTS_24_ANPKeyCode	=	276,	// i 
	kBRL_DOTS_247_ANPKeyCode	=	277,	// I
	kBRL_DOTS_234_ANPKeyCode	=	278,	// s 
	kBRL_DOTS_2347_ANPKeyCode	=	279,	// S
	kBRL_DOTS_14_ANPKeyCode	=	280,	// c 
	kBRL_DOTS_147_ANPKeyCode	=	281,	// C
	kBRL_DOTS_134_ANPKeyCode	=	282,	// m
	kBRL_DOTS_1347_ANPKeyCode	=	283,	// M
	kBRL_DOTS_124_ANPKeyCode	=	284,	// f
	kBRL_DOTS_1247_ANPKeyCode	=	285,	// F
	kBRL_DOTS_1234_ANPKeyCode	=	286,	// p
	kBRL_DOTS_12347_ANPKeyCode	=	287,	// P
	kBRL_DOTS_5_ANPKeyCode	=	288,	// punctuation "  
	kBRL_DOTS_57_ANPKeyCode	=	289,	
	kBRL_DOTS_35_ANPKeyCode	=	290,	// digit 9 
	kBRL_DOTS_357_ANPKeyCode	=	291,	
	kBRL_DOTS_25_ANPKeyCode	=	292,	// digit 3
	kBRL_DOTS_257_ANPKeyCode	=	293,	
	kBRL_DOTS_235_ANPKeyCode	=	294,	// digit 6
	kBRL_DOTS_2357_ANPKeyCode	=	295,	
	kBRL_DOTS_15_ANPKeyCode	=	296,	// e
	kBRL_DOTS_157_ANPKeyCode	=	297,	// E
	kBRL_DOTS_135_ANPKeyCode	=	298,	// o
	kBRL_DOTS_1357_ANPKeyCode	=	299,	// O
	kBRL_DOTS_125_ANPKeyCode	=	300,	// h
	kBRL_DOTS_1257_ANPKeyCode	=	301,	// H
	kBRL_DOTS_1235_ANPKeyCode	=	302,	// r
	kBRL_DOTS_12357_ANPKeyCode	=	303,	// R
	kBRL_DOTS_45_ANPKeyCode	=	304,	// math ~ 
	kBRL_DOTS_457_ANPKeyCode	=	305,	// sign ^ 
	kBRL_DOTS_345_ANPKeyCode	=	306,	// math > 
	kBRL_DOTS_3457_ANPKeyCode	=	307,	
	kBRL_DOTS_245_ANPKeyCode	=	308,	// j
	kBRL_DOTS_2457_ANPKeyCode	=	309,	// J
	kBRL_DOTS_2345_ANPKeyCode	=	310,	// t
	kBRL_DOTS_23457_ANPKeyCode	=	311,	// T
	kBRL_DOTS_145_ANPKeyCode	=	312,	// d
	kBRL_DOTS_1457_ANPKeyCode	=	313,	// D
	kBRL_DOTS_1345_ANPKeyCode	=	314,	// n
	kBRL_DOTS_13457_ANPKeyCode	=	315,	// N
	kBRL_DOTS_1245_ANPKeyCode	=	316,	// g
	kBRL_DOTS_12457_ANPKeyCode	=	317,	// G
	kBRL_DOTS_12345_ANPKeyCode	=	318,	// q
	kBRL_DOTS_123457_ANPKeyCode	=	319,	// Q
	kBRL_DOTS_6_ANPKeyCode	=	320,	// punctuation , 
	kBRL_DOTS_67_ANPKeyCode	=	321,	
	kBRL_DOTS_36_ANPKeyCode	=	322,	// punctuation - 
	kBRL_DOTS_367_ANPKeyCode	=	323,	
	kBRL_DOTS_26_ANPKeyCode	=	324,	// digit 5 
	kBRL_DOTS_267_ANPKeyCode	=	325,	
	kBRL_DOTS_236_ANPKeyCode	=	326,	// digit 8
	kBRL_DOTS_2367_ANPKeyCode	=	327,	
	kBRL_DOTS_16_ANPKeyCode	=	328,	// sign * 
	kBRL_DOTS_167_ANPKeyCode	=	329,	
	kBRL_DOTS_136_ANPKeyCode	=	330,	// u
	kBRL_DOTS_1367_ANPKeyCode	=	331,	// U
	kBRL_DOTS_126_ANPKeyCode	=	332,	// math < 
	kBRL_DOTS_1267_ANPKeyCode	=	333,	
	kBRL_DOTS_1236_ANPKeyCode	=	334,	// v
	kBRL_DOTS_12367_ANPKeyCode	=	335,	// V
	kBRL_DOTS_46_ANPKeyCode	=	336,	// punctuation . 
	kBRL_DOTS_467_ANPKeyCode	=	337,	
	kBRL_DOTS_346_ANPKeyCode	=	338,	// math + 
	kBRL_DOTS_3467_ANPKeyCode	=	339,	
	kBRL_DOTS_246_ANPKeyCode	=	340,	// punctuation { 
	kBRL_DOTS_2467_ANPKeyCode	=	341,	// punctuation [ 
	kBRL_DOTS_2346_ANPKeyCode	=	342,	// punctuation ! 
	kBRL_DOTS_23467_ANPKeyCode	=	343,	
	kBRL_DOTS_146_ANPKeyCode	=	344,	// sign % 
	kBRL_DOTS_1467_ANPKeyCode	=	345,	
	kBRL_DOTS_1346_ANPKeyCode	=	346,	// x
	kBRL_DOTS_13467_ANPKeyCode	=	347,	// X
	kBRL_DOTS_1246_ANPKeyCode	=	348,	// sign $ 
	kBRL_DOTS_12467_ANPKeyCode	=	349,	
	kBRL_DOTS_12346_ANPKeyCode	=	350,	// sign & 
	kBRL_DOTS_123467_ANPKeyCode	=	351,	
	kBRL_DOTS_56_ANPKeyCode	=	352,	// punctuation ; 
	kBRL_DOTS_567_ANPKeyCode	=	353,	
	kBRL_DOTS_356_ANPKeyCode	=	354,	// digit 0 
	kBRL_DOTS_3567_ANPKeyCode	=	355,	
	kBRL_DOTS_256_ANPKeyCode	=	356,	// digit 4
	kBRL_DOTS_2567_ANPKeyCode	=	357,	
	kBRL_DOTS_2356_ANPKeyCode	=	358,	// digit 7
	kBRL_DOTS_23567_ANPKeyCode	=	359,	
	kBRL_DOTS_156_ANPKeyCode	=	360,	// punctuation : 
	kBRL_DOTS_1567_ANPKeyCode	=	361,	
	kBRL_DOTS_1356_ANPKeyCode	=	362,	// z
	kBRL_DOTS_13567_ANPKeyCode	=	363,	// Z
	kBRL_DOTS_1256_ANPKeyCode	=	364,	// sign | 
	kBRL_DOTS_12567_ANPKeyCode	=	365,	// sign (backslash)
	kBRL_DOTS_12356_ANPKeyCode	=	366,	// punctuation ( 
	kBRL_DOTS_123567_ANPKeyCode	=	367,	
	kBRL_DOTS_456_ANPKeyCode	=	368,	// sign _ 
	kBRL_DOTS_4567_ANPKeyCode	=	369,	
	kBRL_DOTS_3456_ANPKeyCode	=	370,	// sign # 
	kBRL_DOTS_34567_ANPKeyCode	=	371,	
	kBRL_DOTS_2456_ANPKeyCode	=	372,	// w
	kBRL_DOTS_24567_ANPKeyCode	=	373,	// W
	kBRL_DOTS_23456_ANPKeyCode	=	374,	// punctuation ) 
	kBRL_DOTS_234567_ANPKeyCode	=	375,	
	kBRL_DOTS_1456_ANPKeyCode	=	376,	// punctuation ? 
	kBRL_DOTS_14567_ANPKeyCode	=	377,	
	kBRL_DOTS_13456_ANPKeyCode	=	378,	// y
	kBRL_DOTS_134567_ANPKeyCode	=	379,	// Y
	kBRL_DOTS_12456_ANPKeyCode	=	380,	// punctuation } 
	kBRL_DOTS_124567_ANPKeyCode	=	381,	// punctuation ] 
	kBRL_DOTS_123456_ANPKeyCode	=	382,	// math = 
	kBRL_DOTS_1234567_ANPKeyCode	=	383,	// enter
	kBRL_DOTS_8_ANPKeyCode	=	384,
	kBRL_DOTS_78_ANPKeyCode	=	385,	
	kBRL_DOTS_38_ANPKeyCode	=	386,	
	kBRL_DOTS_378_ANPKeyCode	=	387,	
	kBRL_DOTS_28_ANPKeyCode	=	388,	
	kBRL_DOTS_278_ANPKeyCode	=	389,	
	kBRL_DOTS_238_ANPKeyCode	=	390,	
	kBRL_DOTS_2378_ANPKeyCode	=	391,	
	kBRL_DOTS_18_ANPKeyCode	=	392,	
	kBRL_DOTS_178_ANPKeyCode	=	393,	
	kBRL_DOTS_138_ANPKeyCode	=	394,	
	kBRL_DOTS_1378_ANPKeyCode	=	395,	
	kBRL_DOTS_128_ANPKeyCode	=	396,	
	kBRL_DOTS_1278_ANPKeyCode	=	397,	
	kBRL_DOTS_1238_ANPKeyCode	=	398,	
	kBRL_DOTS_12378_ANPKeyCode	=	399,	
	kBRL_DOTS_48_ANPKeyCode	=	400,	
	kBRL_DOTS_478_ANPKeyCode	=	401,	
	kBRL_DOTS_348_ANPKeyCode	=	402,	
	kBRL_DOTS_3478_ANPKeyCode	=	403,	
	kBRL_DOTS_248_ANPKeyCode	=	404,	
	kBRL_DOTS_2478_ANPKeyCode	=	405,	
	kBRL_DOTS_2348_ANPKeyCode	=	406,	
	kBRL_DOTS_23478_ANPKeyCode	=	407,	
	kBRL_DOTS_148_ANPKeyCode	=	408,	
	kBRL_DOTS_1478_ANPKeyCode	=	409,	
	kBRL_DOTS_1348_ANPKeyCode	=	410,	
	kBRL_DOTS_13478_ANPKeyCode	=	411,	
	kBRL_DOTS_1248_ANPKeyCode	=	412,	
	kBRL_DOTS_12478_ANPKeyCode	=	413,	
	kBRL_DOTS_12348_ANPKeyCode	=	414,	
	kBRL_DOTS_123478_ANPKeyCode	=	415,	
	kBRL_DOTS_58_ANPKeyCode	=	416,	
	kBRL_DOTS_578_ANPKeyCode	=	417,	
	kBRL_DOTS_358_ANPKeyCode	=	418,	
	kBRL_DOTS_3578_ANPKeyCode	=	419,	
	kBRL_DOTS_258_ANPKeyCode	=	420,	
	kBRL_DOTS_2578_ANPKeyCode	=	421,	
	kBRL_DOTS_2358_ANPKeyCode	=	422,	
	kBRL_DOTS_23578_ANPKeyCode	=	423,	
	kBRL_DOTS_158_ANPKeyCode	=	424,	
	kBRL_DOTS_1578_ANPKeyCode	=	425,	
	kBRL_DOTS_1358_ANPKeyCode	=	426,	
	kBRL_DOTS_13578_ANPKeyCode	=	427,	
	kBRL_DOTS_1258_ANPKeyCode	=	428,	
	kBRL_DOTS_12578_ANPKeyCode	=	429,	
	kBRL_DOTS_12358_ANPKeyCode	=	430,	
	kBRL_DOTS_123578_ANPKeyCode	=	431,	
	kBRL_DOTS_458_ANPKeyCode	=	432,	
	kBRL_DOTS_4578_ANPKeyCode	=	433,	
	kBRL_DOTS_3458_ANPKeyCode	=	434,	
	kBRL_DOTS_34578_ANPKeyCode	=	435,	
	kBRL_DOTS_2458_ANPKeyCode	=	436,	
	kBRL_DOTS_24578_ANPKeyCode	=	437,	
	kBRL_DOTS_23458_ANPKeyCode	=	438,	
	kBRL_DOTS_234578_ANPKeyCode	=	439,	
	kBRL_DOTS_1458_ANPKeyCode	=	440,	
	kBRL_DOTS_14578_ANPKeyCode	=	441,	
	kBRL_DOTS_13458_ANPKeyCode	=	442,	
	kBRL_DOTS_134578_ANPKeyCode	=	443,	
	kBRL_DOTS_12458_ANPKeyCode	=	444,	
	kBRL_DOTS_124578_ANPKeyCode	=	445,	
	kBRL_DOTS_123458_ANPKeyCode	=	446,	
	kBRL_DOTS_1234578_ANPKeyCode	=	447,	
	kBRL_DOTS_68_ANPKeyCode	=	448,	
	kBRL_DOTS_678_ANPKeyCode	=	449,	
	kBRL_DOTS_368_ANPKeyCode	=	450,	
	kBRL_DOTS_3678_ANPKeyCode	=	451,	
	kBRL_DOTS_268_ANPKeyCode	=	452,	
	kBRL_DOTS_2678_ANPKeyCode	=	453,	
	kBRL_DOTS_2368_ANPKeyCode	=	454,	
	kBRL_DOTS_23678_ANPKeyCode	=	455,	
	kBRL_DOTS_168_ANPKeyCode	=	456,	
	kBRL_DOTS_1678_ANPKeyCode	=	457,	
	kBRL_DOTS_1368_ANPKeyCode	=	458,	
	kBRL_DOTS_13678_ANPKeyCode	=	459,	
	kBRL_DOTS_1268_ANPKeyCode	=	460,	
	kBRL_DOTS_12678_ANPKeyCode	=	461,	
	kBRL_DOTS_12368_ANPKeyCode	=	462,	
	kBRL_DOTS_123678_ANPKeyCode	=	463,	
	kBRL_DOTS_468_ANPKeyCode	=	464,	
	kBRL_DOTS_4678_ANPKeyCode	=	465,	
	kBRL_DOTS_3468_ANPKeyCode	=	466,	
	kBRL_DOTS_34678_ANPKeyCode	=	467,	
	kBRL_DOTS_2468_ANPKeyCode	=	468,	
	kBRL_DOTS_24678_ANPKeyCode	=	469,	
	kBRL_DOTS_23468_ANPKeyCode	=	470,	
	kBRL_DOTS_234678_ANPKeyCode	=	471,	
	kBRL_DOTS_1468_ANPKeyCode	=	472,	
	kBRL_DOTS_14678_ANPKeyCode	=	473,	
	kBRL_DOTS_13468_ANPKeyCode	=	474,	
	kBRL_DOTS_134678_ANPKeyCode	=	475,	
	kBRL_DOTS_12468_ANPKeyCode	=	476,	
	kBRL_DOTS_124678_ANPKeyCode	=	477,	
	kBRL_DOTS_123468_ANPKeyCode	=	478,	
	kBRL_DOTS_1234678_ANPKeyCode	=	479,	
	kBRL_DOTS_568_ANPKeyCode	=	480,	
	kBRL_DOTS_5678_ANPKeyCode	=	481,	
	kBRL_DOTS_3568_ANPKeyCode	=	482,	
	kBRL_DOTS_35678_ANPKeyCode	=	483,	
	kBRL_DOTS_2568_ANPKeyCode	=	484,	
	kBRL_DOTS_25678_ANPKeyCode	=	485,	
	kBRL_DOTS_23568_ANPKeyCode	=	486,	
	kBRL_DOTS_235678_ANPKeyCode	=	487,	
	kBRL_DOTS_1568_ANPKeyCode	=	488,	
	kBRL_DOTS_15678_ANPKeyCode	=	489,	
	kBRL_DOTS_13568_ANPKeyCode	=	490,	
	kBRL_DOTS_135678_ANPKeyCode	=	491,	
	kBRL_DOTS_12568_ANPKeyCode	=	492,	
	kBRL_DOTS_125678_ANPKeyCode	=	493,	
	kBRL_DOTS_123568_ANPKeyCode	=	494,	
	kBRL_DOTS_1235678_ANPKeyCode	=	495,	
	kBRL_DOTS_4568_ANPKeyCode	=	496,	
	kBRL_DOTS_45678_ANPKeyCode	=	497,	
	kBRL_DOTS_34568_ANPKeyCode	=	498,	
	kBRL_DOTS_345678_ANPKeyCode	=	499,	
	kBRL_DOTS_24568_ANPKeyCode	=	500,	
	kBRL_DOTS_245678_ANPKeyCode	=	501,	
	kBRL_DOTS_234568_ANPKeyCode	=	502,	
	kBRL_DOTS_2345678_ANPKeyCode	=	503,	
	kBRL_DOTS_14568_ANPKeyCode	=	504,	
	kBRL_DOTS_145678_ANPKeyCode	=	505,	
	kBRL_DOTS_134568_ANPKeyCode	=	506,	
	kBRL_DOTS_1345678_ANPKeyCode	=	507,	
	kBRL_DOTS_124568_ANPKeyCode	=	508,	
	kBRL_DOTS_1245678_ANPKeyCode	=	509,	
	kBRL_DOTS_1234568_ANPKeyCode	=	510,	
	kBRL_DOTS_12345678_ANPKeyCode	=	511,	
	/*{KW} end */
	
	/*{KW} 256 Chorded key combinations*/
	kBRL_CHORD_7_ANPKeyCode	=	513,	
	kBRL_CHORD_3_ANPKeyCode	=	514,
	kBRL_CHORD_37_ANPKeyCode	=	515,	
	kBRL_CHORD_2_ANPKeyCode	=	516,	
	kBRL_CHORD_27_ANPKeyCode	=	517,	
	kBRL_CHORD_23_ANPKeyCode	=	518,	
	kBRL_CHORD_237_ANPKeyCode	=	519,	
	kBRL_CHORD_1_ANPKeyCode	=	520,
	kBRL_CHORD_17_ANPKeyCode	=	521,	
	kBRL_CHORD_13_ANPKeyCode	=	522,	
	kBRL_CHORD_137_ANPKeyCode	=	523,	
	kBRL_CHORD_12_ANPKeyCode	=	524,
	kBRL_CHORD_127_ANPKeyCode	=	525,	
	kBRL_CHORD_123_ANPKeyCode	=	526,	
	kBRL_CHORD_1237_ANPKeyCode	=	527,	
	kBRL_CHORD_4_ANPKeyCode	=	528,
	kBRL_CHORD_47_ANPKeyCode	=	529,	
	kBRL_CHORD_34_ANPKeyCode	=	530,	
	kBRL_CHORD_347_ANPKeyCode	=	531,	
	kBRL_CHORD_24_ANPKeyCode	=	532,	
	kBRL_CHORD_247_ANPKeyCode	=	533,	
	kBRL_CHORD_234_ANPKeyCode	=	534,	
	kBRL_CHORD_2347_ANPKeyCode	=	535,	
	kBRL_CHORD_14_ANPKeyCode	=	536,	
	kBRL_CHORD_147_ANPKeyCode	=	537,	
	kBRL_CHORD_134_ANPKeyCode	=	538,
	kBRL_CHORD_1347_ANPKeyCode	=	539,	
	kBRL_CHORD_124_ANPKeyCode	=	540,
	kBRL_CHORD_1247_ANPKeyCode	=	541,	
	kBRL_CHORD_1234_ANPKeyCode	=	542,	
	kBRL_CHORD_12347_ANPKeyCode	=	543,	
	kBRL_CHORD_5_ANPKeyCode	=	544,	
	kBRL_CHORD_57_ANPKeyCode	=	545,	
	kBRL_CHORD_35_ANPKeyCode	=	546,	
	kBRL_CHORD_357_ANPKeyCode	=	547,	
	kBRL_CHORD_25_ANPKeyCode	=	548,	
	kBRL_CHORD_257_ANPKeyCode	=	549,	
	kBRL_CHORD_235_ANPKeyCode	=	550,	
	kBRL_CHORD_2357_ANPKeyCode	=	551,	
	kBRL_CHORD_15_ANPKeyCode	=	552,	
	kBRL_CHORD_157_ANPKeyCode	=	553,	
	kBRL_CHORD_135_ANPKeyCode	=	554,	
	kBRL_CHORD_1357_ANPKeyCode	=	555,	
	kBRL_CHORD_125_ANPKeyCode	=	556,	
	kBRL_CHORD_1257_ANPKeyCode	=	557,	
	kBRL_CHORD_1235_ANPKeyCode	=	558,	
	kBRL_CHORD_12357_ANPKeyCode	=	559,	
	kBRL_CHORD_45_ANPKeyCode	=	560,	
	kBRL_CHORD_457_ANPKeyCode	=	561,	
	kBRL_CHORD_345_ANPKeyCode	=	562,	
	kBRL_CHORD_3457_ANPKeyCode	=	563,	
	kBRL_CHORD_245_ANPKeyCode	=	564,	
	kBRL_CHORD_2457_ANPKeyCode	=	565,	
	kBRL_CHORD_2345_ANPKeyCode	=	566,	
	kBRL_CHORD_23457_ANPKeyCode	=	567,	
	kBRL_CHORD_145_ANPKeyCode	=	568,
	kBRL_CHORD_1457_ANPKeyCode	=	569,	
	kBRL_CHORD_1345_ANPKeyCode	=	570,	
	kBRL_CHORD_13457_ANPKeyCode	=	571,	
	kBRL_CHORD_1245_ANPKeyCode	=	572,	
	kBRL_CHORD_12457_ANPKeyCode	=	573,	
	kBRL_CHORD_12345_ANPKeyCode	=	574,	
	kBRL_CHORD_123457_ANPKeyCode	=	575,	
	kBRL_CHORD_6_ANPKeyCode	=	576,
	kBRL_CHORD_67_ANPKeyCode	=	577,	
	kBRL_CHORD_36_ANPKeyCode	=	578,	
	kBRL_CHORD_367_ANPKeyCode	=	579,	
	kBRL_CHORD_26_ANPKeyCode	=	580,	
	kBRL_CHORD_267_ANPKeyCode	=	581,	
	kBRL_CHORD_236_ANPKeyCode	=	582,	
	kBRL_CHORD_2367_ANPKeyCode	=	583,	
	kBRL_CHORD_16_ANPKeyCode	=	584,	
	kBRL_CHORD_167_ANPKeyCode	=	585,	
	kBRL_CHORD_136_ANPKeyCode	=	586,	
	kBRL_CHORD_1367_ANPKeyCode	=	587,	
	kBRL_CHORD_126_ANPKeyCode	=	588,	
	kBRL_CHORD_1267_ANPKeyCode	=	589,	
	kBRL_CHORD_1236_ANPKeyCode	=	590,	
	kBRL_CHORD_12367_ANPKeyCode	=	591,	
	kBRL_CHORD_46_ANPKeyCode	=	592,	
	kBRL_CHORD_467_ANPKeyCode	=	593,	
	kBRL_CHORD_346_ANPKeyCode	=	594,	
	kBRL_CHORD_3467_ANPKeyCode	=	595,	
	kBRL_CHORD_246_ANPKeyCode	=	596,	
	kBRL_CHORD_2467_ANPKeyCode	=	597,	
	kBRL_CHORD_2346_ANPKeyCode	=	598,	
	kBRL_CHORD_23467_ANPKeyCode	=	599,	
	kBRL_CHORD_146_ANPKeyCode	=	600,	
	kBRL_CHORD_1467_ANPKeyCode	=	601,	
	kBRL_CHORD_1346_ANPKeyCode	=	602,	
	kBRL_CHORD_13467_ANPKeyCode	=	603,	
	kBRL_CHORD_1246_ANPKeyCode	=	604,	
	kBRL_CHORD_12467_ANPKeyCode	=	605,	
	kBRL_CHORD_12346_ANPKeyCode	=	606,	
	kBRL_CHORD_123467_ANPKeyCode	=	607,	
	kBRL_CHORD_56_ANPKeyCode	=	608,	
	kBRL_CHORD_567_ANPKeyCode	=	609,	
	kBRL_CHORD_356_ANPKeyCode	=	610,	
	kBRL_CHORD_3567_ANPKeyCode	=	611,	
	kBRL_CHORD_256_ANPKeyCode	=	612,	
	kBRL_CHORD_2567_ANPKeyCode	=	613,	
	kBRL_CHORD_2356_ANPKeyCode	=	614,	
	kBRL_CHORD_23567_ANPKeyCode	=	615,	
	kBRL_CHORD_156_ANPKeyCode	=	616,	
	kBRL_CHORD_1567_ANPKeyCode	=	617,	
	kBRL_CHORD_1356_ANPKeyCode	=	618,	
	kBRL_CHORD_13567_ANPKeyCode	=	619,	
	kBRL_CHORD_1256_ANPKeyCode	=	620,	
	kBRL_CHORD_12567_ANPKeyCode	=	621,	
	kBRL_CHORD_12356_ANPKeyCode	=	622,	
	kBRL_CHORD_123567_ANPKeyCode	=	623,	
	kBRL_CHORD_456_ANPKeyCode	=	624,	
	kBRL_CHORD_4567_ANPKeyCode	=	625,	
	kBRL_CHORD_3456_ANPKeyCode	=	626,	
	kBRL_CHORD_34567_ANPKeyCode	=	627,	
	kBRL_CHORD_2456_ANPKeyCode	=	628,	
	kBRL_CHORD_24567_ANPKeyCode	=	629,	
	kBRL_CHORD_23456_ANPKeyCode	=	630,	
	kBRL_CHORD_234567_ANPKeyCode	=	631,	
	kBRL_CHORD_1456_ANPKeyCode	=	632,
	kBRL_CHORD_14567_ANPKeyCode	=	633,	
	kBRL_CHORD_13456_ANPKeyCode	=	634,	
	kBRL_CHORD_134567_ANPKeyCode	=	635,	
	kBRL_CHORD_12456_ANPKeyCode	=	636,	
	kBRL_CHORD_124567_ANPKeyCode	=	637,	
	kBRL_CHORD_123456_ANPKeyCode	=	638,
	kBRL_CHORD_1234567_ANPKeyCode	=	639,	
	kBRL_CHORD_8_ANPKeyCode	=	640,	
	kBRL_CHORD_78_ANPKeyCode	=	641,	
	kBRL_CHORD_38_ANPKeyCode	=	642,	
	kBRL_CHORD_378_ANPKeyCode	=	643,	
	kBRL_CHORD_28_ANPKeyCode	=	644,	
	kBRL_CHORD_278_ANPKeyCode	=	645,	
	kBRL_CHORD_238_ANPKeyCode	=	646,	
	kBRL_CHORD_2378_ANPKeyCode	=	647,	
	kBRL_CHORD_18_ANPKeyCode	=	648,	
	kBRL_CHORD_178_ANPKeyCode	=	649,	
	kBRL_CHORD_138_ANPKeyCode	=	650,	
	kBRL_CHORD_1378_ANPKeyCode	=	651,	
	kBRL_CHORD_128_ANPKeyCode	=	652,	
	kBRL_CHORD_1278_ANPKeyCode	=	653,	
	kBRL_CHORD_1238_ANPKeyCode	=	654,	
	kBRL_CHORD_12378_ANPKeyCode	=	655,	
	kBRL_CHORD_48_ANPKeyCode	=	656,	
	kBRL_CHORD_478_ANPKeyCode	=	657,	
	kBRL_CHORD_348_ANPKeyCode	=	658,	
	kBRL_CHORD_3478_ANPKeyCode	=	659,	
	kBRL_CHORD_248_ANPKeyCode	=	660,	
	kBRL_CHORD_2478_ANPKeyCode	=	661,	
	kBRL_CHORD_2348_ANPKeyCode	=	662,	
	kBRL_CHORD_23478_ANPKeyCode	=	663,	
	kBRL_CHORD_148_ANPKeyCode	=	664,	
	kBRL_CHORD_1478_ANPKeyCode	=	665,	
	kBRL_CHORD_1348_ANPKeyCode	=	666,	
	kBRL_CHORD_13478_ANPKeyCode	=	667,	
	kBRL_CHORD_1248_ANPKeyCode	=	668,	
	kBRL_CHORD_12478_ANPKeyCode	=	669,	
	kBRL_CHORD_12348_ANPKeyCode	=	670,	
	kBRL_CHORD_123478_ANPKeyCode	=	671,	
	kBRL_CHORD_58_ANPKeyCode	=	672,	
	kBRL_CHORD_578_ANPKeyCode	=	673,	
	kBRL_CHORD_358_ANPKeyCode	=	674,	
	kBRL_CHORD_3578_ANPKeyCode	=	675,	
	kBRL_CHORD_258_ANPKeyCode	=	676,	
	kBRL_CHORD_2578_ANPKeyCode	=	677,	
	kBRL_CHORD_2358_ANPKeyCode	=	678,	
	kBRL_CHORD_23578_ANPKeyCode	=	679,	
	kBRL_CHORD_158_ANPKeyCode	=	680,	
	kBRL_CHORD_1578_ANPKeyCode	=	681,	
	kBRL_CHORD_1358_ANPKeyCode	=	682,	
	kBRL_CHORD_13578_ANPKeyCode	=	683,	
	kBRL_CHORD_1258_ANPKeyCode	=	684,	
	kBRL_CHORD_12578_ANPKeyCode	=	685,	
	kBRL_CHORD_12358_ANPKeyCode	=	686,	
	kBRL_CHORD_123578_ANPKeyCode	=	687,	
	kBRL_CHORD_458_ANPKeyCode	=	688,	
	kBRL_CHORD_4578_ANPKeyCode	=	689,	
	kBRL_CHORD_3458_ANPKeyCode	=	690,	
	kBRL_CHORD_34578_ANPKeyCode	=	691,	
	kBRL_CHORD_2458_ANPKeyCode	=	692,	
	kBRL_CHORD_24578_ANPKeyCode	=	693,	
	kBRL_CHORD_23458_ANPKeyCode	=	694,	
	kBRL_CHORD_234578_ANPKeyCode	=	695,	
	kBRL_CHORD_1458_ANPKeyCode	=	696,	
	kBRL_CHORD_14578_ANPKeyCode	=	697,	
	kBRL_CHORD_13458_ANPKeyCode	=	698,	
	kBRL_CHORD_134578_ANPKeyCode	=	699,	
	kBRL_CHORD_12458_ANPKeyCode	=	700,	
	kBRL_CHORD_124578_ANPKeyCode	=	701,	
	kBRL_CHORD_123458_ANPKeyCode	=	702,	
	kBRL_CHORD_1234578_ANPKeyCode	=	703,	
	kBRL_CHORD_68_ANPKeyCode	=	704,	
	kBRL_CHORD_678_ANPKeyCode	=	705,	
	kBRL_CHORD_368_ANPKeyCode	=	706,	
	kBRL_CHORD_3678_ANPKeyCode	=	707,	
	kBRL_CHORD_268_ANPKeyCode	=	708,	
	kBRL_CHORD_2678_ANPKeyCode	=	709,	
	kBRL_CHORD_2368_ANPKeyCode	=	710,	
	kBRL_CHORD_23678_ANPKeyCode	=	711,	
	kBRL_CHORD_168_ANPKeyCode	=	712,	
	kBRL_CHORD_1678_ANPKeyCode	=	713,	
	kBRL_CHORD_1368_ANPKeyCode	=	714,	
	kBRL_CHORD_13678_ANPKeyCode	=	715,	
	kBRL_CHORD_1268_ANPKeyCode	=	716,	
	kBRL_CHORD_12678_ANPKeyCode	=	717,	
	kBRL_CHORD_12368_ANPKeyCode	=	718,	
	kBRL_CHORD_123678_ANPKeyCode	=	719,	
	kBRL_CHORD_468_ANPKeyCode	=	720,	
	kBRL_CHORD_4678_ANPKeyCode	=	721,	
	kBRL_CHORD_3468_ANPKeyCode	=	722,	
	kBRL_CHORD_34678_ANPKeyCode	=	723,	
	kBRL_CHORD_2468_ANPKeyCode	=	724,	
	kBRL_CHORD_24678_ANPKeyCode	=	725,	
	kBRL_CHORD_23468_ANPKeyCode	=	726,	
	kBRL_CHORD_234678_ANPKeyCode	=	727,	
	kBRL_CHORD_1468_ANPKeyCode	=	728,	
	kBRL_CHORD_14678_ANPKeyCode	=	729,	
	kBRL_CHORD_13468_ANPKeyCode	=	730,	
	kBRL_CHORD_134678_ANPKeyCode	=	731,	
	kBRL_CHORD_12468_ANPKeyCode	=	732,	
	kBRL_CHORD_124678_ANPKeyCode	=	733,	
	kBRL_CHORD_123468_ANPKeyCode	=	734,	
	kBRL_CHORD_1234678_ANPKeyCode	=	735,	
	kBRL_CHORD_568_ANPKeyCode	=	736,	
	kBRL_CHORD_5678_ANPKeyCode	=	737,	
	kBRL_CHORD_3568_ANPKeyCode	=	738,	
	kBRL_CHORD_35678_ANPKeyCode	=	739,	
	kBRL_CHORD_2568_ANPKeyCode	=	740,	
	kBRL_CHORD_25678_ANPKeyCode	=	741,	
	kBRL_CHORD_23568_ANPKeyCode	=	742,	
	kBRL_CHORD_235678_ANPKeyCode	=	743,	
	kBRL_CHORD_1568_ANPKeyCode	=	744,	
	kBRL_CHORD_15678_ANPKeyCode	=	745,	
	kBRL_CHORD_13568_ANPKeyCode	=	746,	
	kBRL_CHORD_135678_ANPKeyCode	=	747,	
	kBRL_CHORD_12568_ANPKeyCode	=	748,	
	kBRL_CHORD_125678_ANPKeyCode	=	749,	
	kBRL_CHORD_123568_ANPKeyCode	=	750,	
	kBRL_CHORD_1235678_ANPKeyCode	=	751,	
	kBRL_CHORD_4568_ANPKeyCode	=	752,	
	kBRL_CHORD_45678_ANPKeyCode	=	753,	
	kBRL_CHORD_34568_ANPKeyCode	=	754,	
	kBRL_CHORD_345678_ANPKeyCode	=	755,	
	kBRL_CHORD_24568_ANPKeyCode	=	756,	
	kBRL_CHORD_245678_ANPKeyCode	=	757,	
	kBRL_CHORD_234568_ANPKeyCode	=	758,	
	kBRL_CHORD_2345678_ANPKeyCode	=	759,	
	kBRL_CHORD_14568_ANPKeyCode	=	760,	
	kBRL_CHORD_145678_ANPKeyCode	=	761,	
	kBRL_CHORD_134568_ANPKeyCode	=	762,	
	kBRL_CHORD_1345678_ANPKeyCode	=	763,	
	kBRL_CHORD_124568_ANPKeyCode	=	764,	
	kBRL_CHORD_1245678_ANPKeyCode	=	765,	
	kBRL_CHORD_1234568_ANPKeyCode	=	766,	
	kBRL_CHORD_12345678_ANPKeyCode	=	767,	
	/*{KW} end */

	/*{KW} Individual Braille keys */
	kBrl_Dot1   =  769,
	kBrl_Dot2   =  770,
	kBrl_Dot3   =  771,
	kBrl_Dot4   =  772,
	kBrl_Dot5   =  773,
	kBrl_Dot6   =  774,
	kBrl_Dot7   =  775,
	kBrl_Dot8   =  776,
	kBrl_Dot9   =  777,

	/*{KW} Cursor Routing keys block 1*/
	kBrl_Cursor_0  =  778,
	kBrl_Cursor_1  =  779,
	kBrl_Cursor_2  =  780,
	kBrl_Cursor_3   =  781,
	kBrl_Cursor_4   =  782,
	kBrl_Cursor_5   =  783,
	kBrl_Cursor_6   =  784,
	kBrl_Cursor_7   =  785,
	kBrl_Cursor_8   =  786,
	kBrl_Cursor_9   =  787,
	kBrl_Cursor_10  =  788,
	kBrl_Cursor_11  =  789,
	kBrl_Cursor_12  =  790,
	kBrl_Cursor_13  =  791,
	kBrl_Cursor_14  =  792,
	kBrl_Cursor_15  =  793,
	kBrl_Cursor_16  =  794,
	kBrl_Cursor_17  =  795,
	kBrl_Cursor_18  =  796,
	kBrl_Cursor_19  =  797,

	/*{KW} Cursor Routing keys block 2*/
	kBrl_Cursor_20  =  798,
	kBrl_Cursor_21  =  799,
	kBrl_Cursor_22  =  800,
	kBrl_Cursor_23  =  801,
	kBrl_Cursor_24  =  802,
	kBrl_Cursor_25  =  803,
	kBrl_Cursor_26  =  804,
	kBrl_Cursor_27  =  805,
	kBrl_Cursor_28  =  806,
	kBrl_Cursor_29  =  807,
	kBrl_Cursor_30  =  808,
	kBrl_Cursor_31  =  809,
	kBrl_Cursor_32  =  810,
	kBrl_Cursor_33  =  811,
	kBrl_Cursor_34  =  812,
	kBrl_Cursor_35  =  813,
	kBrl_Cursor_36  =  814,
	kBrl_Cursor_37  =  815,
	kBrl_Cursor_38  =  816,
	kBrl_Cursor_39  =  817,

	/*{KW} Forward, backward keys*/
	kBrlBack_ANPKeyCode       =  818,
	kBrlForward_ANPKeyCode    =  819,
	
	/*{KW}: 256 additional key codes spared (820-1075) */
	
	/*{KW} Unmapped key code for wakeup from suspend*/
	kDummyWakeup_ANPKeyCode    =  1076,	

};

#endif