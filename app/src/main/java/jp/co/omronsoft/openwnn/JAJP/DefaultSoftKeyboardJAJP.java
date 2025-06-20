/*
 * Copyright (C) 2008-2012  OMRON SOFTWARE Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.co.omronsoft.openwnn.JAJP;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import jp.co.omronsoft.openwnn.BaseInputView;
import jp.co.omronsoft.openwnn.DefaultSoftKeyboard;
import jp.co.omronsoft.openwnn.Keyboard;
import jp.co.omronsoft.openwnn.OpenWnn;
import jp.co.omronsoft.openwnn.OpenWnnEvent;
import jp.co.omronsoft.openwnn.OpenWnnJAJP;
import jp.co.omronsoft.openwnn.R;

/**
 * The default Software Keyboard class for Japanese IME.
 *
 * @author Copyright (C) 2009 OMRON SOFTWARE CO., LTD.  All Rights Reserved.
 */
public class DefaultSoftKeyboardJAJP extends DefaultSoftKeyboard {
    /** Enable English word prediction on half-width alphabet mode */
    private static final boolean USE_ENGLISH_PREDICT = true;

    /** Key code for switching to full-width HIRAGANA mode */
    private static final int KEYCODE_SWITCH_FULL_HIRAGANA = -301;

    /** Key code for switching to full-width KATAKANA mode */
    private static final int KEYCODE_SWITCH_FULL_KATAKANA = -302;

    /** Key code for switching to full-width alphabet mode */
    private static final int KEYCODE_SWITCH_FULL_ALPHABET = -303;

    /** Key code for switching to full-width number mode */
    private static final int KEYCODE_SWITCH_FULL_NUMBER = -304;

    /** Key code for switching to half-width KATAKANA mode */
    private static final int KEYCODE_SWITCH_HALF_KATAKANA = -306;

    /** Key code for switching to half-width alphabet mode */
    private static final int KEYCODE_SWITCH_HALF_ALPHABET = -307;

    /** Key code for switching to half-width number mode */
    private static final int KEYCODE_SWITCH_HALF_NUMBER = -308;

    /** Key code for case toggle key */
    private static final int KEYCODE_SELECT_CASE = -309;

    /** Key code for EISU-KANA conversion */
    private static final int KEYCODE_EISU_KANA = -305;

    /** Key code for NOP (no-operation) */
    private static final int KEYCODE_NOP = -310;


    /** Input mode toggle cycle table */
    private static final int[] JP_MODE_CYCLE_TABLE = {
            KEYMODE_JA_FULL_HIRAGANA, KEYMODE_JA_HALF_ALPHABET, KEYMODE_JA_HALF_NUMBER
    };

    /** Definition for {@code mInputType} (toggle) */
    private static final int INPUT_TYPE_TOGGLE = 1;

    /** Definition for {@code mInputType} (commit instantly) */
    private static final int INPUT_TYPE_INSTANT = 2;

    /** Max key number of the 12 key keyboard (depends on the definition of keyboards) */
    private static final int KEY_NUMBER_12KEY = 20;

    /** Toggle cycle table for full-width HIRAGANA */
    private static final String[][] JP_FULL_HIRAGANA_CYCLE_TABLE = {
            {"あ", "い", "う", "え", "お", "ぁ", "ぃ", "ぅ", "ぇ", "ぉ"},
            {"か", "き", "く", "け", "こ"},
            {"さ", "し", "す", "せ", "そ"},
            {"た", "ち", "つ", "て", "と", "っ"},
            {"な", "に", "ぬ", "ね", "の"},
            {"は", "ひ", "ふ", "へ", "ほ"},
            {"ま", "み", "む", "め", "も"},
            {"や", "ゆ", "よ", "ゃ", "ゅ", "ょ"},
            {"ら", "り", "る", "れ", "ろ"},
            {"わ", "を", "ん", "ゎ", "ー"},
            {"、", "。", "？", "！", "・", "\u3000"}
    };

    private static final HashMap<String, String> JP_FULL_HIRAGANA_REPLACE_TABLE = new HashMap<>() {{
        put("あ", "ぁ");
        put("い", "ぃ");
        put("う", "ぅ");
        put("え", "ぇ");
        put("お", "ぉ");
        put("ぁ", "あ");
        put("ぃ", "い");
        put("ぅ", "ヴ");
        put("ぇ", "え");
        put("ぉ", "お");
        put("か", "が");
        put("き", "ぎ");
        put("く", "ぐ");
        put("け", "げ");
        put("こ", "ご");
        put("が", "か");
        put("ぎ", "き");
        put("ぐ", "く");
        put("げ", "け");
        put("ご", "こ");
        put("さ", "ざ");
        put("し", "じ");
        put("す", "ず");
        put("せ", "ぜ");
        put("そ", "ぞ");
        put("ざ", "さ");
        put("じ", "し");
        put("ず", "す");
        put("ぜ", "せ");
        put("ぞ", "そ");
        put("た", "だ");
        put("ち", "ぢ");
        put("つ", "っ");
        put("て", "で");
        put("と", "ど");
        put("だ", "た");
        put("ぢ", "ち");
        put("っ", "づ");
        put("で", "て");
        put("ど", "と");
        put("づ", "つ");
        put("ヴ", "う");
        put("は", "ば");
        put("ひ", "び");
        put("ふ", "ぶ");
        put("へ", "べ");
        put("ほ", "ぼ");
        put("ば", "ぱ");
        put("び", "ぴ");
        put("ぶ", "ぷ");
        put("べ", "ぺ");
        put("ぼ", "ぽ");
        put("ぱ", "は");
        put("ぴ", "ひ");
        put("ぷ", "ふ");
        put("ぺ", "へ");
        put("ぽ", "ほ");
        put("や", "ゃ");
        put("ゆ", "ゅ");
        put("よ", "ょ");
        put("ゃ", "や");
        put("ゅ", "ゆ");
        put("ょ", "よ");
        put("わ", "ゎ");
        put("ゎ", "わ");
        put("゛", "゜");
        put("゜", "゛");
    }};

    /** Toggle cycle table for full-width KATAKANA */
    private static final String[][] JP_FULL_KATAKANA_CYCLE_TABLE = {
            {"ア", "イ", "ウ", "エ", "オ", "ァ", "ィ", "ゥ", "ェ", "ォ"},
            {"カ", "キ", "ク", "ケ", "コ"},
            {"サ", "シ", "ス", "セ", "ソ"},
            {"タ", "チ", "ツ", "テ", "ト", "ッ"},
            {"ナ", "ニ", "ヌ", "ネ", "ノ"},
            {"ハ", "ヒ", "フ", "ヘ", "ホ"},
            {"マ", "ミ", "ム", "メ", "モ"},
            {"ヤ", "ユ", "ヨ", "ャ", "ュ", "ョ"},
            {"ラ", "リ", "ル", "レ", "ロ"},
            {"ワ", "ヲ", "ン", "ヮ", "ー"},
            {"、", "。", "？", "！", "・", "\u3000"}
    };

    /** Replace table for full-width KATAKANA */
    private static final HashMap<String, String> JP_FULL_KATAKANA_REPLACE_TABLE = new HashMap<>() {{
        put("ア", "ァ");
        put("イ", "ィ");
        put("ウ", "ゥ");
        put("エ", "ェ");
        put("オ", "ォ");
        put("ァ", "ア");
        put("ィ", "イ");
        put("ゥ", "ヴ");
        put("ェ", "エ");
        put("ォ", "オ");
        put("カ", "ガ");
        put("キ", "ギ");
        put("ク", "グ");
        put("ケ", "ゲ");
        put("コ", "ゴ");
        put("ガ", "カ");
        put("ギ", "キ");
        put("グ", "ク");
        put("ゲ", "ケ");
        put("ゴ", "コ");
        put("サ", "ザ");
        put("シ", "ジ");
        put("ス", "ズ");
        put("セ", "ゼ");
        put("ソ", "ゾ");
        put("ザ", "サ");
        put("ジ", "シ");
        put("ズ", "ス");
        put("ゼ", "セ");
        put("ゾ", "ソ");
        put("タ", "ダ");
        put("チ", "ヂ");
        put("ツ", "ッ");
        put("テ", "デ");
        put("ト", "ド");
        put("ダ", "タ");
        put("ヂ", "チ");
        put("ッ", "ヅ");
        put("デ", "テ");
        put("ド", "ト");
        put("ヅ", "ツ");
        put("ヴ", "ウ");
        put("ハ", "バ");
        put("ヒ", "ビ");
        put("フ", "ブ");
        put("ヘ", "ベ");
        put("ホ", "ボ");
        put("バ", "パ");
        put("ビ", "ピ");
        put("ブ", "プ");
        put("ベ", "ペ");
        put("ボ", "ポ");
        put("パ", "ハ");
        put("ピ", "ヒ");
        put("プ", "フ");
        put("ペ", "ヘ");
        put("ポ", "ホ");
        put("ヤ", "ャ");
        put("ユ", "ュ");
        put("ヨ", "ョ");
        put("ャ", "ヤ");
        put("ュ", "ユ");
        put("ョ", "ヨ");
        put("ワ", "ヮ");
        put("ヮ", "ワ");
    }};

    /** Toggle cycle table for half-width KATAKANA */
    private static final String[][] JP_HALF_KATAKANA_CYCLE_TABLE = {
            {"ｱ", "ｲ", "ｳ", "ｴ", "ｵ", "ｧ", "ｨ", "ｩ", "ｪ", "ｫ"},
            {"ｶ", "ｷ", "ｸ", "ｹ", "ｺ"},
            {"ｻ", "ｼ", "ｽ", "ｾ", "ｿ"},
            {"ﾀ", "ﾁ", "ﾂ", "ﾃ", "ﾄ", "ｯ"},
            {"ﾅ", "ﾆ", "ﾇ", "ﾈ", "ﾉ"},
            {"ﾊ", "ﾋ", "ﾌ", "ﾍ", "ﾎ"},
            {"ﾏ", "ﾐ", "ﾑ", "ﾒ", "ﾓ"},
            {"ﾔ", "ﾕ", "ﾖ", "ｬ", "ｭ", "ｮ"},
            {"ﾗ", "ﾘ", "ﾙ", "ﾚ", "ﾛ"},
            {"ﾜ", "ｦ", "ﾝ", "ｰ"},
            {"､", "｡", "?", "!", "･", " "}
    };

    /** Replace table for half-width KATAKANA */
    private static final HashMap<String, String> JP_HALF_KATAKANA_REPLACE_TABLE = new HashMap<>() {{
        put("ｱ", "ｧ");
        put("ｲ", "ｨ");
        put("ｳ", "ｩ");
        put("ｴ", "ｪ");
        put("ｵ", "ｫ");
        put("ｧ", "ｱ");
        put("ｨ", "ｲ");
        put("ｩ", "ｳﾞ");
        put("ｪ", "ｴ");
        put("ｫ", "ｵ");
        put("ｶ", "ｶﾞ");
        put("ｷ", "ｷﾞ");
        put("ｸ", "ｸﾞ");
        put("ｹ", "ｹﾞ");
        put("ｺ", "ｺﾞ");
        put("ｶﾞ", "ｶ");
        put("ｷﾞ", "ｷ");
        put("ｸﾞ", "ｸ");
        put("ｹﾞ", "ｹ");
        put("ｺﾞ", "ｺ");
        put("ｻ", "ｻﾞ");
        put("ｼ", "ｼﾞ");
        put("ｽ", "ｽﾞ");
        put("ｾ", "ｾﾞ");
        put("ｿ", "ｿﾞ");
        put("ｻﾞ", "ｻ");
        put("ｼﾞ", "ｼ");
        put("ｽﾞ", "ｽ");
        put("ｾﾞ", "ｾ");
        put("ｿﾞ", "ｿ");
        put("ﾀ", "ﾀﾞ");
        put("ﾁ", "ﾁﾞ");
        put("ﾂ", "ｯ");
        put("ﾃ", "ﾃﾞ");
        put("ﾄ", "ﾄﾞ");
        put("ﾀﾞ", "ﾀ");
        put("ﾁﾞ", "ﾁ");
        put("ｯ", "ﾂﾞ");
        put("ﾃﾞ", "ﾃ");
        put("ﾄﾞ", "ﾄ");
        put("ﾂﾞ", "ﾂ");
        put("ﾊ", "ﾊﾞ");
        put("ﾋ", "ﾋﾞ");
        put("ﾌ", "ﾌﾞ");
        put("ﾍ", "ﾍﾞ");
        put("ﾎ", "ﾎﾞ");
        put("ﾊﾞ", "ﾊﾟ");
        put("ﾋﾞ", "ﾋﾟ");
        put("ﾌﾞ", "ﾌﾟ");
        put("ﾍﾞ", "ﾍﾟ");
        put("ﾎﾞ", "ﾎﾟ");
        put("ﾊﾟ", "ﾊ");
        put("ﾋﾟ", "ﾋ");
        put("ﾌﾟ", "ﾌ");
        put("ﾍﾟ", "ﾍ");
        put("ﾎﾟ", "ﾎ");
        put("ﾔ", "ｬ");
        put("ﾕ", "ｭ");
        put("ﾖ", "ｮ");
        put("ｬ", "ﾔ");
        put("ｭ", "ﾕ");
        put("ｮ", "ﾖ");
        put("ﾜ", "ﾜ");
        put("ｳﾞ", "ｳ");
    }};

    /** Toggle cycle table for full-width alphabet */
    private static final String[][] JP_FULL_ALPHABET_CYCLE_TABLE = {
            {"．", "＠", "－", "＿", "／", "：", "～", "１"},
            {"ａ", "ｂ", "ｃ", "Ａ", "Ｂ", "Ｃ", "２"},
            {"ｄ", "ｅ", "ｆ", "Ｄ", "Ｅ", "Ｆ", "３"},
            {"ｇ", "ｈ", "ｉ", "Ｇ", "Ｈ", "Ｉ", "４"},
            {"ｊ", "ｋ", "ｌ", "Ｊ", "Ｋ", "Ｌ", "５"},
            {"ｍ", "ｎ", "ｏ", "Ｍ", "Ｎ", "Ｏ", "６"},
            {"ｐ", "ｑ", "ｒ", "ｓ", "Ｐ", "Ｑ", "Ｒ", "Ｓ", "７"},
            {"ｔ", "ｕ", "ｖ", "Ｔ", "Ｕ", "Ｖ", "８"},
            {"ｗ", "ｘ", "ｙ", "ｚ", "Ｗ", "Ｘ", "Ｙ", "Ｚ", "９"},
            {"－", "０"},
            {"，", "．", "？", "！", "・", "\u3000"}
    };

    /** Replace table for full-width alphabet */
    private static final HashMap<String, String> JP_FULL_ALPHABET_REPLACE_TABLE = new HashMap<>() {{
        put("Ａ", "ａ");
        put("Ｂ", "ｂ");
        put("Ｃ", "ｃ");
        put("Ｄ", "ｄ");
        put("Ｅ", "ｅ");
        put("ａ", "Ａ");
        put("ｂ", "Ｂ");
        put("ｃ", "Ｃ");
        put("ｄ", "Ｄ");
        put("ｅ", "Ｅ");
        put("Ｆ", "ｆ");
        put("Ｇ", "ｇ");
        put("Ｈ", "ｈ");
        put("Ｉ", "ｉ");
        put("Ｊ", "ｊ");
        put("ｆ", "Ｆ");
        put("ｇ", "Ｇ");
        put("ｈ", "Ｈ");
        put("ｉ", "Ｉ");
        put("ｊ", "Ｊ");
        put("Ｋ", "ｋ");
        put("Ｌ", "ｌ");
        put("Ｍ", "ｍ");
        put("Ｎ", "ｎ");
        put("Ｏ", "ｏ");
        put("ｋ", "Ｋ");
        put("ｌ", "Ｌ");
        put("ｍ", "Ｍ");
        put("ｎ", "Ｎ");
        put("ｏ", "Ｏ");
        put("Ｐ", "ｐ");
        put("Ｑ", "ｑ");
        put("Ｒ", "ｒ");
        put("Ｓ", "ｓ");
        put("Ｔ", "ｔ");
        put("ｐ", "Ｐ");
        put("ｑ", "Ｑ");
        put("ｒ", "Ｒ");
        put("ｓ", "Ｓ");
        put("ｔ", "Ｔ");
        put("Ｕ", "ｕ");
        put("Ｖ", "ｖ");
        put("Ｗ", "ｗ");
        put("Ｘ", "ｘ");
        put("Ｙ", "ｙ");
        put("ｕ", "Ｕ");
        put("ｖ", "Ｖ");
        put("ｗ", "Ｗ");
        put("ｘ", "Ｘ");
        put("ｙ", "Ｙ");
        put("Ｚ", "ｚ");
        put("ｚ", "Ｚ");
    }};

    /** Toggle cycle table for half-width alphabet */
    private static final String[][] JP_HALF_ALPHABET_CYCLE_TABLE = {
            {".", "@", "-", "_", "/", ":", "~", "1"},
            {"a", "b", "c", "A", "B", "C", "2"},
            {"d", "e", "f", "D", "E", "F", "3"},
            {"g", "h", "i", "G", "H", "I", "4"},
            {"j", "k", "l", "J", "K", "L", "5"},
            {"m", "n", "o", "M", "N", "O", "6"},
            {"p", "q", "r", "s", "P", "Q", "R", "S", "7"},
            {"t", "u", "v", "T", "U", "V", "8"},
            {"w", "x", "y", "z", "W", "X", "Y", "Z", "9"},
            {"-", "0"},
            {",", ".", "?", "!", ";", " "}
    };

    /** Replace table for half-width alphabet */
    private static final HashMap<String, String> JP_HALF_ALPHABET_REPLACE_TABLE = new HashMap<>() {{
        put("A", "a");
        put("B", "b");
        put("C", "c");
        put("D", "d");
        put("E", "e");
        put("a", "A");
        put("b", "B");
        put("c", "C");
        put("d", "D");
        put("e", "E");
        put("F", "f");
        put("G", "g");
        put("H", "h");
        put("I", "i");
        put("J", "j");
        put("f", "F");
        put("g", "G");
        put("h", "H");
        put("i", "I");
        put("j", "J");
        put("K", "k");
        put("L", "l");
        put("M", "m");
        put("N", "n");
        put("O", "o");
        put("k", "K");
        put("l", "L");
        put("m", "M");
        put("n", "N");
        put("o", "O");
        put("P", "p");
        put("Q", "q");
        put("R", "r");
        put("S", "s");
        put("T", "t");
        put("p", "P");
        put("q", "Q");
        put("r", "R");
        put("s", "S");
        put("t", "T");
        put("U", "u");
        put("V", "v");
        put("W", "w");
        put("X", "x");
        put("Y", "y");
        put("u", "U");
        put("v", "V");
        put("w", "W");
        put("x", "X");
        put("y", "Y");
        put("Z", "z");
        put("z", "Z");
    }};

    /** Character table for full-width number */
    private static final char[] INSTANT_CHAR_CODE_FULL_NUMBER = "１２３４５６７８９０＃＊".toCharArray();

    /** Character table for half-width number */
    private static final char[] INSTANT_CHAR_CODE_HALF_NUMBER = "1234567890#*".toCharArray();

    /** The constant for mFixedKeyMode. It means that input mode is not fixed. */
    private static final int INVALID_KEYMODE = -1;

    /** KeyIndex of "Moji" key on 12 keyboard (depends on the definition of keyboards) */
    private static final int KEY_INDEX_CHANGE_MODE_12KEY = 15;

    /** KeyIndex of "Moji" key on QWERTY keyboard (depends on the definition of keyboards) */
    private static final int KEY_INDEX_CHANGE_MODE_QWERTY = 29;

    /** Type of input mode */
    private int mInputType = INPUT_TYPE_TOGGLE;

    /** Previous input character code */
    private int mPrevInputKeyCode = 0;

    /**
     * Character table to input when mInputType becomes INPUT_TYPE_INSTANT.
     * (Either INSTANT_CHAR_CODE_FULL_NUMBER or INSTANT_CHAR_CODE_HALF_NUMBER)
     */
    private char[] mCurrentInstantTable = null;

    /** Input mode that is not able to be changed. If ENABLE_CHANGE_KEYMODE is set, input mode can change. */
    private int[] mLimitedKeyMode = null;

    /** Input mode that is given the first priority. If ENABLE_CHANGE_KEYMODE is set, input mode can change. */
    private int mPreferenceKeyMode = INVALID_KEYMODE;

    /** The last input type */
    private int mLastInputType = 0;

    /** Auto caps mode */
    private boolean mEnableAutoCaps = true;

    /** PopupResId of "Moji" key (this is used for canceling long-press) */
    private int mPopupResId = 0;

    /** Whether the InputType is null */
    private boolean mIsInputTypeNull = false;

    /** {@code SharedPreferences} for save the keyboard type */
    private SharedPreferences.Editor mPrefEditor = null;

    /** "Moji" key (this is used for canceling long-press) */
    private Keyboard.Key mChangeModeKey = null;


    /** Default constructor */
    public DefaultSoftKeyboardJAJP() {
        mCurrentLanguage = LANG_JA;
        mCurrentKeyboardType = KEYBOARD_QWERTY;
        mShiftOn = KEYBOARD_SHIFT_OFF;
        mCurrentKeyMode = KEYMODE_JA_FULL_HIRAGANA;
    }

    /** @see jp.co.omronsoft.openwnn.DefaultSoftKeyboard#createKeyboards */
    @Override
    protected void createKeyboards(OpenWnn parent) {

        /* Keyboard[# of Languages][portrait/landscape][# of keyboard type][shift off/on][max # of key-modes][noinput/input] */
        mKeyboard = new Keyboard[3][2][4][2][8][2];

        if (mHardKeyboardHidden) {
            /* Create the suitable keyboard object */
            if (mDisplayMode == DefaultSoftKeyboard.PORTRAIT) {
                createKeyboardsPortrait(parent);
            } else {
                createKeyboardsLandscape(parent);
            }

            if (mCurrentKeyboardType == KEYBOARD_12KEY) {
                mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.CHANGE_MODE, OpenWnnJAJP.ENGINE_MODE_OPT_TYPE_12KEY));
            } else {
                mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.CHANGE_MODE, OpenWnnJAJP.ENGINE_MODE_OPT_TYPE_QWERTY));
            }
        } else {
            /* Create the hardware assist keyboard object */
            createKeyboardsAssist(parent);
            mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.CHANGE_MODE, OpenWnnJAJP.ENGINE_MODE_OPT_TYPE_QWERTY));
        }
    }

    /**
     * Commit the pre-edit string for committing operation that is not explicit
     * (ex. when a candidate is selected)
     */
    private void commitText() {
        if (!mNoInput) {
            mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.COMMIT_COMPOSING_TEXT));
        }
    }

    /**
     * Change input mode
     * <br>
     * @param keyMode   The type of input mode
     */
    public void changeKeyMode(int keyMode) {
        int targetMode = filterKeyMode(keyMode);
        if (targetMode == INVALID_KEYMODE) {
            return;
        }

        commitText();

        if (mCapsLock) {
            mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.INPUT_SOFT_KEY,
                    new KeyEvent(KeyEvent.ACTION_UP,
                            KeyEvent.KEYCODE_SHIFT_LEFT)));
            mCapsLock = false;
        }
        mShiftOn = KEYBOARD_SHIFT_OFF;
        Keyboard kbd = getModeChangeKeyboard(targetMode);
        mCurrentKeyMode = targetMode;
        mPrevInputKeyCode = 0;

        int mode = OpenWnnEvent.Mode.DIRECT;

        switch (targetMode) {
            case KEYMODE_JA_FULL_HIRAGANA:
                mInputType = INPUT_TYPE_TOGGLE;
                mode = OpenWnnEvent.Mode.DEFAULT;
                break;

            case KEYMODE_JA_HALF_ALPHABET:
                if (USE_ENGLISH_PREDICT) {
                    mInputType = INPUT_TYPE_TOGGLE;
                    mode = OpenWnnEvent.Mode.NO_LV1_CONV;
                } else {
                    mInputType = INPUT_TYPE_TOGGLE;
                    mode = OpenWnnEvent.Mode.DIRECT;
                }
                break;

            case KEYMODE_JA_FULL_NUMBER:
                mInputType = INPUT_TYPE_INSTANT;
                mode = OpenWnnEvent.Mode.DIRECT;
                mCurrentInstantTable = INSTANT_CHAR_CODE_FULL_NUMBER;
                break;

            case KEYMODE_JA_HALF_NUMBER:
                mInputType = INPUT_TYPE_INSTANT;
                mode = OpenWnnEvent.Mode.DIRECT;
                mCurrentInstantTable = INSTANT_CHAR_CODE_HALF_NUMBER;
                break;

            case KEYMODE_JA_FULL_KATAKANA:
                mInputType = INPUT_TYPE_TOGGLE;
                mode = OpenWnnJAJP.ENGINE_MODE_FULL_KATAKANA;
                break;

            case KEYMODE_JA_FULL_ALPHABET:
                mInputType = INPUT_TYPE_TOGGLE;
                mode = OpenWnnEvent.Mode.DIRECT;
                break;

            case KEYMODE_JA_HALF_KATAKANA:
                mInputType = INPUT_TYPE_TOGGLE;
                mode = OpenWnnJAJP.ENGINE_MODE_HALF_KATAKANA;
                break;

            default:
                break;
        }

        setStatusIcon();
        changeKeyboard(kbd);
        mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.CHANGE_MODE, mode));
    }

    /** @see jp.co.omronsoft.openwnn.DefaultSoftKeyboard#initView */
    @Override
    public View initView(OpenWnn parent, int width, int height) {

        View view = super.initView(parent, width, height);
        changeKeyboard(mKeyboard[mCurrentLanguage][mDisplayMode][mCurrentKeyboardType][mShiftOn][mCurrentKeyMode][0]);

        return view;
    }

    /** @see jp.co.omronsoft.openwnn.DefaultSoftKeyboard#changeKeyboard */
    @Override
    protected boolean changeKeyboard(Keyboard keyboard) {
        if (keyboard != null) {
            if (mIsInputTypeNull && mChangeModeKey != null) {
                mChangeModeKey.popupResId = mPopupResId;
            }

            List<Keyboard.Key> keys = keyboard.getKeys();
            int keyIndex = (KEY_NUMBER_12KEY < keys.size())
                    ? KEY_INDEX_CHANGE_MODE_QWERTY : KEY_INDEX_CHANGE_MODE_12KEY;
            mChangeModeKey = keys.get(keyIndex);

            if (mIsInputTypeNull && mChangeModeKey != null) {
                mPopupResId = mChangeModeKey.popupResId;
                mChangeModeKey.popupResId = 0;
            }
        }

        return super.changeKeyboard(keyboard);
    }

    /** @see jp.co.omronsoft.openwnn.DefaultSoftKeyboard#changeKeyboardType */
    @Override
    public void changeKeyboardType(int type) {
        commitText();
        Keyboard kbd = getTypeChangeKeyboard(type);
        if (kbd != null) {
            mCurrentKeyboardType = type;
            mPrefEditor.putBoolean("opt_enable_qwerty", type == KEYBOARD_QWERTY);
            mPrefEditor.commit();
            changeKeyboard(kbd);
        }
        if (type == KEYBOARD_12KEY) {
            mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.CHANGE_MODE, OpenWnnJAJP.ENGINE_MODE_OPT_TYPE_12KEY));
        } else {
            mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.CHANGE_MODE, OpenWnnJAJP.ENGINE_MODE_OPT_TYPE_QWERTY));
        }
    }

    /** @see jp.co.omronsoft.openwnn.DefaultSoftKeyboard#onKey */
    @Override
    public void onKey(int primaryCode, int[] keyCodes) {

        if (mDisableKeyInput) {
            return;
        }

        switch (primaryCode) {
            case KEYCODE_JP12_TOGGLE_MODE:
            case KEYCODE_QWERTY_TOGGLE_MODE:
                if (!mIsInputTypeNull) {
                    nextKeyMode();
                }
                break;

            case KEYCODE_QWERTY_BACKSPACE:
            case KEYCODE_JP12_BACKSPACE:
                mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.INPUT_SOFT_KEY, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)));
                break;

            case KEYCODE_QWERTY_SHIFT:
                toggleShiftLock();
                break;

            case KEYCODE_QWERTY_ALT:
                processAltKey();
                break;

            case KEYCODE_QWERTY_ENTER:
            case KEYCODE_JP12_ENTER:
                mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.INPUT_SOFT_KEY, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER)));
                break;

            case KEYCODE_JP12_REVERSE:
                if (!mNoInput) {
                    mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.TOGGLE_REVERSE_CHAR, mCurrentCycleTable));
                }
                break;

            case KEYCODE_QWERTY_KBD:
                changeKeyboardType(KEYBOARD_12KEY);
                break;

            case KEYCODE_JP12_KBD:
                changeKeyboardType(KEYBOARD_QWERTY);
                break;

            case KEYCODE_JP12_EMOJI:
            case KEYCODE_QWERTY_EMOJI:
                commitText();
                mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.CHANGE_MODE, OpenWnnJAJP.ENGINE_MODE_SYMBOL));
                break;

            case KEYCODE_JP12_1:
            case KEYCODE_JP12_2:
            case KEYCODE_JP12_3:
            case KEYCODE_JP12_4:
            case KEYCODE_JP12_5:
            case KEYCODE_JP12_6:
            case KEYCODE_JP12_7:
            case KEYCODE_JP12_8:
            case KEYCODE_JP12_9:
            case KEYCODE_JP12_0:
            case KEYCODE_JP12_SHARP:
                /* Processing to input by ten key */
                if (mInputType == INPUT_TYPE_INSTANT) {
                    /* Send a input character directly if instant input type is selected */
                    commitText();
                    mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.INPUT_CHAR,
                            mCurrentInstantTable[getTableIndex(primaryCode)]));
                } else {
                    if ((mPrevInputKeyCode != primaryCode)) {
                        mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.TOUCH_OTHER_KEY));
                        if ((mCurrentKeyMode == KEYMODE_JA_HALF_ALPHABET)
                                && (primaryCode == KEYCODE_JP12_SHARP)) {
                            /* Commit text by symbol character (',' '.') when alphabet input mode is selected */
                            commitText();
                        }
                    }

                    /* Convert the key code to the table index and send the toggle event with the table index */
                    String[][] cycleTable = getCycleTable();
                    if (cycleTable == null) {
                        Log.e("OpenWnn", "not founds cycle table");
                    } else {
                        int index = getTableIndex(primaryCode);
                        mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.TOGGLE_CHAR, cycleTable[index]));
                        mCurrentCycleTable = cycleTable[index];
                    }
                    mPrevInputKeyCode = primaryCode;
                }
                break;

            case KEYCODE_JP12_ASTER:
                if (mInputType == INPUT_TYPE_INSTANT) {
                    commitText();
                    mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.INPUT_CHAR,
                            mCurrentInstantTable[getTableIndex(primaryCode)]));
                } else {
                    if (!mNoInput) {
                        /* Processing to toggle Dakuten, Handakuten, and capital */
                        HashMap replaceTable = getReplaceTable();
                        if (replaceTable == null) {
                            Log.e("OpenWnn", "not founds replace table");
                        } else {
                            mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.REPLACE_CHAR, replaceTable));
                            mPrevInputKeyCode = primaryCode;
                        }
                    }
                }
                break;

            case KEYCODE_SWITCH_FULL_HIRAGANA:
                /* Change mode to Full width hiragana */
                changeKeyMode(KEYMODE_JA_FULL_HIRAGANA);
                break;

            case KEYCODE_SWITCH_FULL_KATAKANA:
                /* Change mode to Full width katakana */
                changeKeyMode(KEYMODE_JA_FULL_KATAKANA);
                break;

            case KEYCODE_SWITCH_FULL_ALPHABET:
                /* Change mode to Full width alphabet */
                changeKeyMode(KEYMODE_JA_FULL_ALPHABET);
                break;

            case KEYCODE_SWITCH_FULL_NUMBER:
                /* Change mode to Full width numeric */
                changeKeyMode(KEYMODE_JA_FULL_NUMBER);
                break;

            case KEYCODE_SWITCH_HALF_KATAKANA:
                /* Change mode to Half width katakana */
                changeKeyMode(KEYMODE_JA_HALF_KATAKANA);
                break;

            case KEYCODE_SWITCH_HALF_ALPHABET:
                /* Change mode to Half width alphabet */
                changeKeyMode(KEYMODE_JA_HALF_ALPHABET);
                break;

            case KEYCODE_SWITCH_HALF_NUMBER:
                /* Change mode to Half width numeric */
                changeKeyMode(KEYMODE_JA_HALF_NUMBER);
                break;


            case KEYCODE_SELECT_CASE:
                int shifted = (mShiftOn == 0) ? 1 : 0;
                Keyboard newKeyboard = getShiftChangeKeyboard(shifted);
                if (newKeyboard != null) {
                    mShiftOn = shifted;
                    changeKeyboard(newKeyboard);
                }
                break;

            case KEYCODE_JP12_SPACE:
                if ((mCurrentKeyMode == KEYMODE_JA_FULL_HIRAGANA) && !mNoInput) {
                    mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.CONVERT));
                } else {
                    mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.INPUT_CHAR, ' '));
                }
                break;

            case KEYCODE_EISU_KANA:
                mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.CHANGE_MODE, OpenWnnJAJP.ENGINE_MODE_EISU_KANA));
                break;

            case KEYCODE_JP12_CLOSE:
                mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.INPUT_KEY,
                        new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_BACK)));
                break;

            case KEYCODE_JP12_LEFT:
                mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.INPUT_SOFT_KEY,
                        new KeyEvent(KeyEvent.ACTION_DOWN,
                                KeyEvent.KEYCODE_DPAD_LEFT)));
                break;

            case KEYCODE_JP12_RIGHT:
                mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.INPUT_SOFT_KEY,
                        new KeyEvent(KeyEvent.ACTION_DOWN,
                                KeyEvent.KEYCODE_DPAD_RIGHT)));
                break;
            case KEYCODE_NOP:
                break;

            default:
                if (primaryCode >= 0) {
                    if (mKeyboardView.isShifted()) {
                        primaryCode = Character.toUpperCase(primaryCode);
                    }
                    mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.INPUT_CHAR, (char) primaryCode));
                }
                break;
        }

        /* update shift key's state */
        if (!mCapsLock && (primaryCode != DefaultSoftKeyboard.KEYCODE_QWERTY_SHIFT)) {
            setShiftByEditorInfo();
        }
    }

    /** @see jp.co.omronsoft.openwnn.DefaultSoftKeyboard#setPreferences */
    @Override
    public void setPreferences(SharedPreferences pref, EditorInfo editor) {
        mPrefEditor = pref.edit();
        boolean isQwerty;
        isQwerty = pref.getBoolean("opt_enable_qwerty", true);

        if (isQwerty && (mCurrentKeyboardType == KEYBOARD_12KEY)) {
            changeKeyboardType(KEYBOARD_QWERTY);
        }

        super.setPreferences(pref, editor);

        int inputType = editor.inputType;
        if (mHardKeyboardHidden) {
            if (inputType == EditorInfo.TYPE_NULL) {
                if (!mIsInputTypeNull) {
                    mIsInputTypeNull = true;
                    if (mChangeModeKey != null) {
                        mPopupResId = mChangeModeKey.popupResId;
                        mChangeModeKey.popupResId = 0;
                    }
                }
                return;
            }

            if (mIsInputTypeNull) {
                mIsInputTypeNull = false;
                if (mChangeModeKey != null) {
                    mChangeModeKey.popupResId = mPopupResId;
                }
            }
        }

        mEnableAutoCaps = pref.getBoolean("auto_caps", true);
        mLimitedKeyMode = null;
        mPreferenceKeyMode = INVALID_KEYMODE;
        mNoInput = true;
        mDisableKeyInput = false;
        mCapsLock = false;

        switch (inputType & EditorInfo.TYPE_MASK_CLASS) {

            case EditorInfo.TYPE_CLASS_NUMBER:
            case EditorInfo.TYPE_CLASS_DATETIME:
                mPreferenceKeyMode = KEYMODE_JA_HALF_NUMBER;
                break;

            case EditorInfo.TYPE_CLASS_PHONE:
                if (mHardKeyboardHidden) {
                    mLimitedKeyMode = new int[]{KEYMODE_JA_HALF_PHONE};
                } else {
                    mLimitedKeyMode = new int[]{KEYMODE_JA_HALF_ALPHABET};
                }
                break;

            case EditorInfo.TYPE_CLASS_TEXT:
                switch (inputType & EditorInfo.TYPE_MASK_VARIATION) {

                    case EditorInfo.TYPE_TEXT_VARIATION_PASSWORD:
                    case EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD:
                        mLimitedKeyMode = new int[]{KEYMODE_JA_HALF_ALPHABET, KEYMODE_JA_HALF_NUMBER};
                        break;

                    case EditorInfo.TYPE_TEXT_VARIATION_EMAIL_ADDRESS:
                    case EditorInfo.TYPE_TEXT_VARIATION_URI:
                        mPreferenceKeyMode = KEYMODE_JA_HALF_ALPHABET;
                        break;

                    default:
                        break;
                }
                break;

            default:
                break;
        }

        if (inputType != mLastInputType) {
            setDefaultKeyboard();
            mLastInputType = inputType;
        }

        setStatusIcon();
        setShiftByEditorInfo();
    }

    /** @see jp.co.omronsoft.openwnn.DefaultSoftKeyboard#onUpdateState */
    @Override
    public void onUpdateState(OpenWnn parent) {
        super.onUpdateState(parent);
        if (!mCapsLock) {
            setShiftByEditorInfo();
        }
    }

    /**
     * Change the keyboard to default
     */
    public void setDefaultKeyboard() {
        Locale locale = Locale.getDefault();
        int keymode = KEYMODE_JA_FULL_HIRAGANA;

        if (mPreferenceKeyMode != INVALID_KEYMODE) {
            keymode = mPreferenceKeyMode;
        } else if (mLimitedKeyMode != null) {
            keymode = mLimitedKeyMode[0];
        } else {
            if (!locale.getLanguage().equals(Locale.JAPANESE.getLanguage())) {
                keymode = KEYMODE_JA_HALF_ALPHABET;
            }
        }
        changeKeyMode(keymode);
    }


    /**
     * Change to the next input mode
     */
    public void nextKeyMode() {
        /* Search the current mode in the toggle table */
        boolean found = false;
        int index;
        for (index = 0; index < JP_MODE_CYCLE_TABLE.length; index++) {
            if (JP_MODE_CYCLE_TABLE[index] == mCurrentKeyMode) {
                found = true;
                break;
            }
        }

        if (!found) {
            /* If the current mode not exists, set the default mode */
            setDefaultKeyboard();
        } else {
            /* If the current mode exists, set the next input mode */
            int size = JP_MODE_CYCLE_TABLE.length;
            int keyMode = INVALID_KEYMODE;
            for (int i = 0; i < size; i++) {
                index = (++index) % size;

                keyMode = filterKeyMode(JP_MODE_CYCLE_TABLE[index]);
                if (keyMode != INVALID_KEYMODE) {
                    break;
                }
            }

            if (keyMode != INVALID_KEYMODE) {
                changeKeyMode(keyMode);
            }
        }
    }

    private void createKeyboardsAssist(OpenWnn parent) {
        Keyboard keyboardAssist = new Keyboard(parent, R.xml.keyboard_assist);
        Keyboard[][] keyList;

        keyList = mKeyboard[LANG_JA][PORTRAIT][KEYBOARD_QWERTY][KEYBOARD_SHIFT_OFF];
        keyList[KEYMODE_JA_FULL_HIRAGANA][0] = keyboardAssist;
        keyList[KEYMODE_JA_FULL_NUMBER][0] = keyboardAssist;
        keyList[KEYMODE_JA_HALF_ALPHABET][0] = keyboardAssist;
        keyList[KEYMODE_JA_HALF_NUMBER][0] = keyboardAssist;
        keyList[KEYMODE_JA_HALF_PHONE][0] = keyboardAssist;

        keyList = mKeyboard[LANG_JA][PORTRAIT][KEYBOARD_QWERTY][KEYBOARD_SHIFT_ON];
        keyList[KEYMODE_JA_FULL_HIRAGANA][0] = keyboardAssist;
        keyList[KEYMODE_JA_FULL_NUMBER][0] = keyboardAssist;
        keyList[KEYMODE_JA_HALF_ALPHABET][0] = keyboardAssist;
        keyList[KEYMODE_JA_HALF_NUMBER][0] = keyboardAssist;
        keyList[KEYMODE_JA_HALF_PHONE][0] = keyboardAssist;

        keyList = mKeyboard[LANG_JA][LANDSCAPE][KEYBOARD_QWERTY][KEYBOARD_SHIFT_OFF];
        keyList[KEYMODE_JA_FULL_HIRAGANA][0] = keyboardAssist;
        keyList[KEYMODE_JA_FULL_NUMBER][0] = keyboardAssist;
        keyList[KEYMODE_JA_HALF_ALPHABET][0] = keyboardAssist;
        keyList[KEYMODE_JA_HALF_NUMBER][0] = keyboardAssist;
        keyList[KEYMODE_JA_HALF_PHONE][0] = keyboardAssist;

        keyList = mKeyboard[LANG_JA][LANDSCAPE][KEYBOARD_QWERTY][KEYBOARD_SHIFT_ON];
        keyList[KEYMODE_JA_FULL_HIRAGANA][0] = keyboardAssist;
        keyList[KEYMODE_JA_FULL_NUMBER][0] = keyboardAssist;
        keyList[KEYMODE_JA_HALF_ALPHABET][0] = keyboardAssist;
        keyList[KEYMODE_JA_HALF_NUMBER][0] = keyboardAssist;
        keyList[KEYMODE_JA_HALF_PHONE][0] = keyboardAssist;

    }
    /**
     * Create the keyboard for portrait mode
     * <br>
     * @param parent  The context
     */
    private void createKeyboardsPortrait(OpenWnn parent) {
        Keyboard[][] keyList;
        /* qwerty shift_off (portrait) */
        keyList = mKeyboard[LANG_JA][PORTRAIT][KEYBOARD_QWERTY][KEYBOARD_SHIFT_OFF];
        keyList[KEYMODE_JA_FULL_HIRAGANA][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp);
        keyList[KEYMODE_JA_FULL_ALPHABET][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_full_alphabet);
        keyList[KEYMODE_JA_FULL_NUMBER][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_full_symbols);
        keyList[KEYMODE_JA_FULL_KATAKANA][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_full_katakana);
        keyList[KEYMODE_JA_HALF_ALPHABET][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_half_alphabet);
        keyList[KEYMODE_JA_HALF_NUMBER][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_half_symbols);
        keyList[KEYMODE_JA_HALF_KATAKANA][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_half_katakana);
        keyList[KEYMODE_JA_HALF_PHONE][0] = new Keyboard(parent, R.xml.keyboard_12key_phone);

        /* qwerty shift_on (portrait) */
        keyList = mKeyboard[LANG_JA][PORTRAIT][KEYBOARD_QWERTY][KEYBOARD_SHIFT_ON];
        keyList[KEYMODE_JA_FULL_HIRAGANA][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_shift);
        keyList[KEYMODE_JA_FULL_ALPHABET][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_full_alphabet_shift);
        keyList[KEYMODE_JA_FULL_NUMBER][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_full_symbols_shift);
        keyList[KEYMODE_JA_FULL_KATAKANA][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_full_katakana_shift);
        keyList[KEYMODE_JA_HALF_ALPHABET][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_half_alphabet_shift);
        keyList[KEYMODE_JA_HALF_NUMBER][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_half_symbols_shift);
        keyList[KEYMODE_JA_HALF_KATAKANA][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_half_katakana_shift);
        keyList[KEYMODE_JA_HALF_PHONE][0] = mKeyboard[LANG_JA][PORTRAIT][KEYBOARD_QWERTY][KEYBOARD_SHIFT_OFF][KEYMODE_JA_HALF_PHONE][0];


        /* 12-keys shift_off (portrait) */
        keyList = mKeyboard[LANG_JA][PORTRAIT][KEYBOARD_12KEY][KEYBOARD_SHIFT_OFF];
        keyList[KEYMODE_JA_FULL_HIRAGANA][0] = new Keyboard(parent, R.xml.keyboard_12keyjp);
        keyList[KEYMODE_JA_FULL_HIRAGANA][1] = new Keyboard(parent, R.xml.keyboard_12keyjp_input);
        keyList[KEYMODE_JA_FULL_ALPHABET][0] = new Keyboard(parent, R.xml.keyboard_12key_full_alphabet);
        keyList[KEYMODE_JA_FULL_ALPHABET][1] = new Keyboard(parent, R.xml.keyboard_12key_full_alphabet_input);
        keyList[KEYMODE_JA_FULL_NUMBER][0] = new Keyboard(parent, R.xml.keyboard_12key_full_num);
        keyList[KEYMODE_JA_FULL_KATAKANA][0] = new Keyboard(parent, R.xml.keyboard_12key_full_katakana);
        keyList[KEYMODE_JA_FULL_KATAKANA][1] = new Keyboard(parent, R.xml.keyboard_12key_full_katakana_input);
        keyList[KEYMODE_JA_HALF_ALPHABET][0] = new Keyboard(parent, R.xml.keyboard_12key_half_alphabet);
        keyList[KEYMODE_JA_HALF_ALPHABET][1] = new Keyboard(parent, R.xml.keyboard_12key_half_alphabet_input);
        keyList[KEYMODE_JA_HALF_NUMBER][0] = new Keyboard(parent, R.xml.keyboard_12key_half_num);
        keyList[KEYMODE_JA_HALF_KATAKANA][0] = new Keyboard(parent, R.xml.keyboard_12key_half_katakana);
        keyList[KEYMODE_JA_HALF_KATAKANA][1] = new Keyboard(parent, R.xml.keyboard_12key_half_katakana_input);
        keyList[KEYMODE_JA_HALF_PHONE][0] = new Keyboard(parent, R.xml.keyboard_12key_phone);

        /* 12-keys shift_on (portrait) */
        keyList = mKeyboard[LANG_JA][PORTRAIT][KEYBOARD_12KEY][KEYBOARD_SHIFT_ON];
        keyList[KEYMODE_JA_FULL_HIRAGANA] = mKeyboard[LANG_JA][PORTRAIT][KEYBOARD_12KEY][KEYBOARD_SHIFT_OFF][KEYMODE_JA_FULL_HIRAGANA];
        keyList[KEYMODE_JA_FULL_ALPHABET] = mKeyboard[LANG_JA][PORTRAIT][KEYBOARD_12KEY][KEYBOARD_SHIFT_OFF][KEYMODE_JA_FULL_ALPHABET];
        keyList[KEYMODE_JA_FULL_NUMBER] = mKeyboard[LANG_JA][PORTRAIT][KEYBOARD_12KEY][KEYBOARD_SHIFT_OFF][KEYMODE_JA_FULL_NUMBER];
        keyList[KEYMODE_JA_FULL_KATAKANA] = mKeyboard[LANG_JA][PORTRAIT][KEYBOARD_12KEY][KEYBOARD_SHIFT_OFF][KEYMODE_JA_FULL_KATAKANA];
        keyList[KEYMODE_JA_HALF_ALPHABET] = mKeyboard[LANG_JA][PORTRAIT][KEYBOARD_12KEY][KEYBOARD_SHIFT_OFF][KEYMODE_JA_HALF_ALPHABET];
        keyList[KEYMODE_JA_HALF_NUMBER] = mKeyboard[LANG_JA][PORTRAIT][KEYBOARD_12KEY][KEYBOARD_SHIFT_OFF][KEYMODE_JA_HALF_NUMBER];
        keyList[KEYMODE_JA_HALF_KATAKANA] = mKeyboard[LANG_JA][PORTRAIT][KEYBOARD_12KEY][KEYBOARD_SHIFT_OFF][KEYMODE_JA_HALF_KATAKANA];
        keyList[KEYMODE_JA_HALF_PHONE] = mKeyboard[LANG_JA][PORTRAIT][KEYBOARD_12KEY][KEYBOARD_SHIFT_OFF][KEYMODE_JA_HALF_PHONE];
    }

    /**
     * Create the keyboard for landscape mode
     * <br>
     * @param parent  The context
     */
    private void createKeyboardsLandscape(OpenWnn parent) {
        Keyboard[][] keyList;
        /* qwerty shift_off (landscape) */
        keyList = mKeyboard[LANG_JA][LANDSCAPE][KEYBOARD_QWERTY][KEYBOARD_SHIFT_OFF];
        keyList[KEYMODE_JA_FULL_HIRAGANA][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp);
        keyList[KEYMODE_JA_FULL_ALPHABET][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_full_alphabet);
        keyList[KEYMODE_JA_FULL_NUMBER][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_full_symbols);
        keyList[KEYMODE_JA_FULL_KATAKANA][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_full_katakana);
        keyList[KEYMODE_JA_HALF_ALPHABET][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_half_alphabet);
        keyList[KEYMODE_JA_HALF_NUMBER][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_half_symbols);
        keyList[KEYMODE_JA_HALF_KATAKANA][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_half_katakana);
        keyList[KEYMODE_JA_HALF_PHONE][0] = new Keyboard(parent, R.xml.keyboard_12key_phone);

        /* qwerty shift_on (landscape) */
        keyList = mKeyboard[LANG_JA][LANDSCAPE][KEYBOARD_QWERTY][KEYBOARD_SHIFT_ON];
        keyList[KEYMODE_JA_FULL_HIRAGANA][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_shift);
        keyList[KEYMODE_JA_FULL_ALPHABET][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_full_alphabet_shift);
        keyList[KEYMODE_JA_FULL_NUMBER][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_full_symbols_shift);
        keyList[KEYMODE_JA_FULL_KATAKANA][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_full_katakana_shift);
        keyList[KEYMODE_JA_HALF_ALPHABET][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_half_alphabet_shift);
        keyList[KEYMODE_JA_HALF_NUMBER][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_half_symbols_shift);
        keyList[KEYMODE_JA_HALF_KATAKANA][0] = new Keyboard(parent, R.xml.keyboard_qwerty_jp_half_katakana_shift);
        keyList[KEYMODE_JA_HALF_PHONE][0] = mKeyboard[LANG_JA][LANDSCAPE][KEYBOARD_QWERTY][KEYBOARD_SHIFT_OFF][KEYMODE_JA_HALF_PHONE][0];


        /* 12-keys shift_off (landscape) */
        keyList = mKeyboard[LANG_JA][LANDSCAPE][KEYBOARD_12KEY][KEYBOARD_SHIFT_OFF];
        keyList[KEYMODE_JA_FULL_HIRAGANA][0] = new Keyboard(parent, R.xml.keyboard_12keyjp);
        keyList[KEYMODE_JA_FULL_HIRAGANA][1] = new Keyboard(parent, R.xml.keyboard_12keyjp_input);
        keyList[KEYMODE_JA_FULL_ALPHABET][0] = new Keyboard(parent, R.xml.keyboard_12key_full_alphabet);
        keyList[KEYMODE_JA_FULL_ALPHABET][1] = new Keyboard(parent, R.xml.keyboard_12key_full_alphabet_input);
        keyList[KEYMODE_JA_FULL_NUMBER][0] = new Keyboard(parent, R.xml.keyboard_12key_full_num);
        keyList[KEYMODE_JA_FULL_KATAKANA][0] = new Keyboard(parent, R.xml.keyboard_12key_full_katakana);
        keyList[KEYMODE_JA_FULL_KATAKANA][1] = new Keyboard(parent, R.xml.keyboard_12key_full_katakana_input);
        keyList[KEYMODE_JA_HALF_ALPHABET][0] = new Keyboard(parent, R.xml.keyboard_12key_half_alphabet);
        keyList[KEYMODE_JA_HALF_ALPHABET][1] = new Keyboard(parent, R.xml.keyboard_12key_half_alphabet_input);
        keyList[KEYMODE_JA_HALF_NUMBER][0] = new Keyboard(parent, R.xml.keyboard_12key_half_num);
        keyList[KEYMODE_JA_HALF_KATAKANA][0] = new Keyboard(parent, R.xml.keyboard_12key_half_katakana);
        keyList[KEYMODE_JA_HALF_KATAKANA][1] = new Keyboard(parent, R.xml.keyboard_12key_half_katakana_input);
        keyList[KEYMODE_JA_HALF_PHONE][0] = new Keyboard(parent, R.xml.keyboard_12key_phone);

        /* 12-keys shift_on (landscape) */
        keyList = mKeyboard[LANG_JA][LANDSCAPE][KEYBOARD_12KEY][KEYBOARD_SHIFT_ON];
        keyList[KEYMODE_JA_FULL_HIRAGANA] = mKeyboard[LANG_JA][LANDSCAPE][KEYBOARD_12KEY][KEYBOARD_SHIFT_OFF][KEYMODE_JA_FULL_HIRAGANA];
        keyList[KEYMODE_JA_FULL_ALPHABET] = mKeyboard[LANG_JA][LANDSCAPE][KEYBOARD_12KEY][KEYBOARD_SHIFT_OFF][KEYMODE_JA_FULL_ALPHABET];
        keyList[KEYMODE_JA_FULL_NUMBER] = mKeyboard[LANG_JA][LANDSCAPE][KEYBOARD_12KEY][KEYBOARD_SHIFT_OFF][KEYMODE_JA_FULL_NUMBER];
        keyList[KEYMODE_JA_FULL_KATAKANA] = mKeyboard[LANG_JA][LANDSCAPE][KEYBOARD_12KEY][KEYBOARD_SHIFT_OFF][KEYMODE_JA_FULL_KATAKANA];
        keyList[KEYMODE_JA_HALF_ALPHABET] = mKeyboard[LANG_JA][LANDSCAPE][KEYBOARD_12KEY][KEYBOARD_SHIFT_OFF][KEYMODE_JA_HALF_ALPHABET];
        keyList[KEYMODE_JA_HALF_NUMBER] = mKeyboard[LANG_JA][LANDSCAPE][KEYBOARD_12KEY][KEYBOARD_SHIFT_OFF][KEYMODE_JA_HALF_NUMBER];
        keyList[KEYMODE_JA_HALF_KATAKANA] = mKeyboard[LANG_JA][LANDSCAPE][KEYBOARD_12KEY][KEYBOARD_SHIFT_OFF][KEYMODE_JA_HALF_KATAKANA];
        keyList[KEYMODE_JA_HALF_PHONE] = mKeyboard[LANG_JA][LANDSCAPE][KEYBOARD_12KEY][KEYBOARD_SHIFT_OFF][KEYMODE_JA_HALF_PHONE];
    }

    /**
     * Convert the key code to the index of table
     * <br>
     * @param keyCode     The key code
     * @return The index of the toggle table for input
     */
    private int getTableIndex(int keyCode) {
        int index =
                (keyCode == KEYCODE_JP12_1) ? 0 :
                        (keyCode == KEYCODE_JP12_2) ? 1 :
                                (keyCode == KEYCODE_JP12_3) ? 2 :
                                        (keyCode == KEYCODE_JP12_4) ? 3 :
                                                (keyCode == KEYCODE_JP12_5) ? 4 :
                                                        (keyCode == KEYCODE_JP12_6) ? 5 :
                                                                (keyCode == KEYCODE_JP12_7) ? 6 :
                                                                        (keyCode == KEYCODE_JP12_8) ? 7 :
                                                                                (keyCode == KEYCODE_JP12_9) ? 8 :
                                                                                        (keyCode == KEYCODE_JP12_0) ? 9 :
                                                                                                (keyCode == KEYCODE_JP12_SHARP) ? 10 :
                                                                                                        (keyCode == KEYCODE_JP12_ASTER) ? 11 :
                                                                                                                0;

        return index;
    }

    /**
     * Get the toggle table for input that is appropriate in current mode.
     *
     * @return The toggle table for input
     */
    private String[][] getCycleTable() {
        String[][] cycleTable = null;
        switch (mCurrentKeyMode) {
            case KEYMODE_JA_FULL_HIRAGANA:
                cycleTable = JP_FULL_HIRAGANA_CYCLE_TABLE;
                break;

            case KEYMODE_JA_FULL_KATAKANA:
                cycleTable = JP_FULL_KATAKANA_CYCLE_TABLE;
                break;

            case KEYMODE_JA_FULL_ALPHABET:
                cycleTable = JP_FULL_ALPHABET_CYCLE_TABLE;
                break;

            case KEYMODE_JA_FULL_NUMBER:
            case KEYMODE_JA_HALF_NUMBER:
                /* Because these modes belong to direct input group, No toggle table exists */
                break;

            case KEYMODE_JA_HALF_ALPHABET:
                cycleTable = JP_HALF_ALPHABET_CYCLE_TABLE;
                break;

            case KEYMODE_JA_HALF_KATAKANA:
                cycleTable = JP_HALF_KATAKANA_CYCLE_TABLE;
                break;

            default:
                break;
        }
        return cycleTable;
    }

    /**
     * Get the replace table that is appropriate in current mode.
     *
     * @return The replace table
     */
    private HashMap getReplaceTable() {
        HashMap hashTable = null;
        switch (mCurrentKeyMode) {
            case KEYMODE_JA_FULL_HIRAGANA:
                hashTable = JP_FULL_HIRAGANA_REPLACE_TABLE;
                break;
            case KEYMODE_JA_FULL_KATAKANA:
                hashTable = JP_FULL_KATAKANA_REPLACE_TABLE;
                break;

            case KEYMODE_JA_FULL_ALPHABET:
                hashTable = JP_FULL_ALPHABET_REPLACE_TABLE;
                break;

            case KEYMODE_JA_FULL_NUMBER:
            case KEYMODE_JA_HALF_NUMBER:
                /* Because these modes belong to direct input group, No replacing table exists */
                break;

            case KEYMODE_JA_HALF_ALPHABET:
                hashTable = JP_HALF_ALPHABET_REPLACE_TABLE;
                break;

            case KEYMODE_JA_HALF_KATAKANA:
                hashTable = JP_HALF_KATAKANA_REPLACE_TABLE;
                break;

            default:
                break;
        }
        return hashTable;
    }

    /**
     * Set the status icon that is appropriate in current mode
     */
    private void setStatusIcon() {
        int icon = 0;

        switch (mCurrentKeyMode) {
            case KEYMODE_JA_FULL_HIRAGANA:
                icon = R.drawable.immodeic_hiragana;
                break;
            case KEYMODE_JA_FULL_KATAKANA:
                icon = R.drawable.immodeic_full_kana;
                break;
            case KEYMODE_JA_FULL_ALPHABET:
                icon = R.drawable.immodeic_full_alphabet;
                break;
            case KEYMODE_JA_FULL_NUMBER:
                icon = R.drawable.immodeic_full_number;
                break;
            case KEYMODE_JA_HALF_KATAKANA:
                icon = R.drawable.immodeic_half_kana;
                break;
            case KEYMODE_JA_HALF_ALPHABET:
                icon = R.drawable.immodeic_half_alphabet;
                break;
            case KEYMODE_JA_HALF_NUMBER:
            case KEYMODE_JA_HALF_PHONE:
                icon = R.drawable.immodeic_half_number;
                break;
            default:
                break;
        }

        mWnn.showStatusIcon(icon);
    }

    /**
     * Get the shift key state from the editor.
     * <br>
     * @param editor    The editor information
     * @return The state id of the shift key (0:off, 1:on)
     */
    protected int getShiftKeyState(EditorInfo editor) {
        InputConnection connection = mWnn.getCurrentInputConnection();
        if (connection != null) {
            int caps = connection.getCursorCapsMode(editor.inputType);
            return (caps == 0) ? 0 : 1;
        } else {
            return 0;
        }
    }

    /**
     * Set the shift key state from {@link EditorInfo}.
     */
    private void setShiftByEditorInfo() {
        if (mEnableAutoCaps && (mCurrentKeyMode == KEYMODE_JA_HALF_ALPHABET)) {
            int shift = getShiftKeyState(mWnn.getCurrentInputEditorInfo());

            mShiftOn = shift;
            changeKeyboard(getShiftChangeKeyboard(shift));
        }
    }

    /** @see jp.co.omronsoft.openwnn.DefaultSoftKeyboard#setHardKeyboardHidden */
    @Override
    public void setHardKeyboardHidden(boolean hidden) {
        if (mWnn != null) {
            if (!hidden) {
                mWnn.onEvent(new OpenWnnEvent(OpenWnnEvent.CHANGE_MODE, OpenWnnJAJP.ENGINE_MODE_OPT_TYPE_QWERTY));
            }

            if (mHardKeyboardHidden != hidden) {
                if ((mLimitedKeyMode != null)
                        || ((mCurrentKeyMode != KEYMODE_JA_FULL_HIRAGANA)
                        && (mCurrentKeyMode != KEYMODE_JA_HALF_ALPHABET))) {

                    mLastInputType = EditorInfo.TYPE_NULL;
                    if (mWnn.isInputViewShown()) {
                        setDefaultKeyboard();
                    }
                }
            }
        }
        super.setHardKeyboardHidden(hidden);
    }

    /**
     * Change the key-mode to the allowed one which is restricted
     *  by the text input field or the type of the keyboard.
     * @param keyMode The key-mode
     * @return the key-mode allowed
     */
    private int filterKeyMode(int keyMode) {
        int targetMode = keyMode;
        int[] limits = mLimitedKeyMode;

        if (!mHardKeyboardHidden) { /* for hardware keyboard */
            if ((targetMode != KEYMODE_JA_FULL_HIRAGANA) && (targetMode != KEYMODE_JA_HALF_ALPHABET)) {

                Locale locale = Locale.getDefault();
                int keymode = KEYMODE_JA_HALF_ALPHABET;
                if (locale.getLanguage().equals(Locale.JAPANESE.getLanguage())) {
                    switch (targetMode) {
                        case KEYMODE_JA_FULL_HIRAGANA:
                        case KEYMODE_JA_FULL_KATAKANA:
                        case KEYMODE_JA_HALF_KATAKANA:
                            keymode = KEYMODE_JA_FULL_HIRAGANA;
                            break;
                        default:
                            /* half-alphabet */
                            break;
                    }
                }
                targetMode = keymode;
            }
        }

        /* restrict by the type of the text field */
        if (limits != null) {
            boolean hasAccepted = false;
            boolean hasRequiredChange = true;
            int size = limits.length;
            int nowMode = mCurrentKeyMode;

            for (int i = 0; i < size; i++) {
                if (targetMode == limits[i]) {
                    hasAccepted = true;
                    break;
                }
                if (nowMode == limits[i]) {
                    hasRequiredChange = false;
                }
            }

            if (!hasAccepted) {
                if (hasRequiredChange) {
                    targetMode = mLimitedKeyMode[0];
                } else {
                    targetMode = INVALID_KEYMODE;
                }
            }
        }

        return targetMode;
    }

}
