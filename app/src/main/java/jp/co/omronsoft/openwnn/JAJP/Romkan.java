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

import android.content.SharedPreferences;

import java.util.HashMap;

import jp.co.omronsoft.openwnn.ComposingText;
import jp.co.omronsoft.openwnn.LetterConverter;
import jp.co.omronsoft.openwnn.StrSegment;

/**
 * The Romaji to Hiragana converter class for Japanese IME.
 *
 * @author Copyright (C) 2009 OMRON SOFTWARE CO., LTD.  All Rights Reserved.
 */
public class Romkan implements LetterConverter {
    /** HashMap for Romaji-to-Kana conversion (Japanese mode) */
    private static final HashMap<String, String> romkanTable = new HashMap<>() {{
        put("", "っ");

        put("a", "あ"); put("i", "い"); put("u", "う"); put("e", "え"); put("o", "お");

        put("ba", "ば"); put("bi", "び"); put("bu", "ぶ"); put("be", "べ"); put("bo", "ぼ");
        put("bya", "びゃ"); put("byi", "びぃ"); put("byu", "びゅ"); put("bye", "びぇ"); put("byo", "びょ");

        put("ca", "か"); put("ci", "し"); put("cu", "く"); put("ce", "せ"); put("co", "こ");
        put("cha", "ちゃ"); put("chi", "ち"); put("chu", "ちゅ"); put("che", "ちぇ"); put("cho", "ちょ");
        put("cya", "ちゃ"); put("cyi", "ちぃ"); put("cyu", "ちゅ"); put("cye", "ちぇ"); put("cyo", "ちょ");

        put("da", "だ"); put("di", "ぢ"); put("du", "づ"); put("de", "で"); put("do", "ど");
        put("dha", "でゃ"); put("dhi", "でぃ"); put("dhu", "でゅ"); put("dhe", "でぇ"); put("dho", "でょ");
        put("dwa", "どぁ"); put("dwi", "どぃ"); put("dwu", "どぅ"); put("dwe", "どぇ"); put("dwo", "どぉ");
        put("dya", "ぢゃ"); put("dyi", "ぢぃ"); put("dyu", "ぢゅ"); put("dye", "ぢぇ"); put("dyo", "ぢょ");

        put("fa", "ふぁ"); put("fi", "ふぃ"); put("fu", "ふ"); put("fe", "ふぇ"); put("fo", "ふぉ");
        put("fwa", "ふぁ"); put("fwi", "ふぃ"); put("fwu", "ふぅ"); put("fwe", "ふぇ"); put("fwo", "ふぉ");
        put("fya", "ふゃ"); put("fyi", "ふぃ"); put("fyu", "ふゅ"); put("fye", "ふぇ"); put("fyo", "ふょ");

        put("ga", "が"); put("gi", "ぎ"); put("gu", "ぐ"); put("ge", "げ"); put("go", "ご");
        put("gwa", "ぐぁ"); put("gwi", "ぐぃ"); put("gwu", "ぐぅ"); put("gwe", "ぐぇ"); put("gwo", "ぐぉ");
        put("gya", "ぎゃ"); put("gyi", "ぎぃ"); put("gyu", "ぎゅ"); put("gye", "ぎぇ"); put("gyo", "ぎょ");

        put("ha", "は"); put("hi", "ひ"); put("hu", "ふ"); put("he", "へ"); put("ho", "ほ");
        put("hya", "ひゃ"); put("hyi", "ひぃ"); put("hyu", "ひゅ"); put("hye", "ひぇ"); put("hyo", "ひょ");

        put("ja", "じゃ"); put("ji", "じ"); put("ju", "じゅ"); put("je", "じぇ"); put("jo", "じょ");
        put("jya", "じゃ"); put("jyi", "じぃ"); put("jyu", "じゅ"); put("jye", "じぇ"); put("jyo", "じょ");

        put("ka", "か"); put("ki", "き"); put("ku", "く"); put("ke", "け"); put("ko", "こ");
        put("kya", "きゃ"); put("kyi", "きぃ"); put("kyu", "きゅ"); put("kye", "きぇ"); put("kyo", "きょ");

        put("kwa", "くぁ");

        put("la", "ぁ"); put("li", "ぃ"); put("lu", "ぅ"); put("le", "ぇ"); put("lo", "ぉ");
        put("lya", "ゃ"); put("lyi", "ぃ"); put("lyu", "ゅ"); put("lye", "ぇ"); put("lyo", "ょ");

        put("ltsu", "っ");
        put("ltu", "っ");
        put("lwa", "ゎ");

        put("ma", "ま"); put("mi", "み"); put("mu", "む"); put("me", "め"); put("mo", "も");
        put("mya", "みゃ"); put("myi", "みぃ"); put("myu", "みゅ"); put("mye", "みぇ"); put("myo", "みょ");

        put("na", "な"); put("ni", "に"); put("nu", "ぬ"); put("ne", "ね"); put("no", "の");
        put("nya", "にゃ"); put("nyi", "にぃ"); put("nyu", "にゅ"); put("nye", "にぇ"); put("nyo", "にょ");

        put("pa", "ぱ"); put("pi", "ぴ"); put("pu", "ぷ"); put("pe", "ぺ"); put("po", "ぽ");
        put("pya", "ぴゃ"); put("pyi", "ぴぃ"); put("pyu", "ぴゅ"); put("pye", "ぴぇ"); put("pyo", "ぴょ");

        put("qa", "くぁ"); put("qi", "くぃ"); put("qu", "く"); put("qe", "くぇ"); put("qo", "くぉ");

        put("qwa", "くぁ"); put("qwi", "くぃ"); put("qwu", "くぅ"); put("qwe", "くぇ"); put("qwo", "くぉ");
        put("qya", "くゃ"); put("qyi", "くぃ"); put("qyu", "くゅ"); put("qye", "くぇ"); put("qyo", "くょ");

        put("ra", "ら"); put("ri", "り"); put("ru", "る"); put("re", "れ"); put("ro", "ろ");
        put("rya", "りゃ"); put("ryi", "りぃ"); put("ryu", "りゅ"); put("rye", "りぇ"); put("ryo", "りょ");

        put("sa", "さ"); put("si", "し"); put("su", "す"); put("se", "せ"); put("so", "そ");
        put("sha", "しゃ"); put("shi", "し"); put("shu", "しゅ"); put("she", "しぇ"); put("sho", "しょ");
        put("swa", "すぁ"); put("swi", "すぃ"); put("swu", "すぅ"); put("swe", "すぇ"); put("swo", "すぉ");
        put("sya", "しゃ"); put("syi", "しぃ"); put("syu", "しゅ"); put("sye", "しぇ"); put("syo", "しょ");

        put("ta", "た"); put("ti", "ち"); put("tu", "つ"); put("to", "と"); put("te", "て");
        put("tha", "てゃ"); put("thi", "てぃ"); put("thu", "てゅ"); put("the", "てぇ"); put("tho", "てょ");
        put("tsa", "つぁ"); put("tsi", "つぃ"); put("tsu", "つ"); put("tse", "つぇ"); put("tso", "つぉ");
        put("twa", "とぁ"); put("twi", "とぃ"); put("twu", "とぅ"); put("twe", "とぇ"); put("two", "とぉ");
        put("tya", "ちゃ"); put("tyi", "ちぃ"); put("tyu", "ちゅ"); put("tye", "ちぇ"); put("tyo", "ちょ");

        put("va", "ゔぁ"); put("vi", "ゔぃ"); put("vu", "ゔ"); put("ve", "ゔぇ"); put("vo", "ゔぉ");
        put("vya", "ゔゃ"); put("vyi", "ゔぃ"); put("vyu", "ゔゅ"); put("vye", "ゔぇ"); put("vyo", "ゔょ");

        put("wa", "わ"); put("wi", "うぃ"); put("wu", "う"); put("we", "うぇ"); put("wo", "を");
        put("wha", "うぁ"); put("whi", "うぃ"); put("whu", "う"); put("whe", "うぇ"); put("who", "うぉ");

        put("xa", "ぁ"); put("xi", "ぃ"); put("xu", "ぅ"); put("xe", "ぇ"); put("xo", "ぉ");
        put("xya", "ゃ"); put("xyi", "ぃ"); put("xyu", "ゅ"); put("xye", "ぇ"); put("xyo", "ょ");

        put("xn", "ん");
        put("xtu", "っ");
        put("xwa", "ゎ");

        put("ya", "や"); put("yi", "い"); put("yu", "ゆ"); put("ye", "いぇ"); put("yo", "よ");

        put("za", "ざ"); put("zi", "じ"); put("zu", "ず"); put("ze", "ぜ"); put("zo", "ぞ");
        put("zya", "じゃ"); put("zyi", "じぃ"); put("zyu", "じゅ"); put("zye", "じぇ"); put("zyo", "じょ");

        put("bb", "っb");
        put("cc", "っc");
        put("dd", "っd");
        put("ff", "っf");
        put("gg", "っg");
        put("hh", "っh");
        put("jj", "っj");
        put("kk", "っk");
        put("ll", "っl");
        put("mm", "っm");
        put("pp", "っp");
        put("qq", "っq");
        put("rr", "っr");
        put("ss", "っs");
        put("tt", "っt");
        put("vv", "っv");
        put("ww", "っw");
        put("xx", "っx");
        put("yy", "っy");
        put("zz", "っz");

        put("nb", "んb");
        put("nc", "んc");
        put("nd", "んd");
        put("nf", "んf");
        put("ng", "んg");
        put("nh", "んh");
        put("nj", "んj");
        put("nk", "んk");
        put("nl", "んl");
        put("nm", "んm");
        put("nn", "ん");
        put("np", "んp");
        put("nq", "んq");
        put("nr", "んr");
        put("ns", "んs");
        put("nt", "んt");
        put("nv", "んv");
        put("nw", "んw");
        put("nx", "んx");
        put("nz", "んz");

        put("!", "！");
        put("#", "＃");
        put("$", "＄");
        put("%", "％");
        put("&", "＆");
        put("'", "＇");
        put("(", "（");
        put(")", "）");
        put("*", "＊");
        put("+", "＋");
        put(",", "、");
        put("-", "ー");
        put(".", "。");
        put("/", "・");
        put("0", "０");
        put("1", "１");
        put("2", "２");
        put("3", "３");
        put("4", "４");
        put("5", "５");
        put("6", "６");
        put("7", "７");
        put("8", "８");
        put("9", "９");
        put(":", "：");
        put(";", "；");
        put("<", "＜");
        put("=", "＝");
        put(">", "＞");
        put("?", "？");
        put("@", "＠");
        put("[", "「");
        put("\"", "＂");
        put("\\", "＼");
        put("]", "」");
        put("^", "＾");
        put("_", "＿");
        put("`", "｀");
        put("{", "｛");
        put("|", "｜");
        put("}", "｝");
        put("~", "～");
        put("¥", "￥");
    }};

    /** Max length of the target text */
    private static final int MAX_LENGTH = 4;

    /**
     * Default constructor
     */
    public Romkan() {
        super();
    }

    /* **********************************************************************
     * LetterConverter's interface
     * *********************************************************************/

    /** @see LetterConverter#convert */
    public boolean convert(ComposingText text) {
        int cursor = text.getCursor(1);

        if (cursor <= 0) {
            return false;
        }

        StrSegment[] str = new StrSegment[MAX_LENGTH];
        int start = MAX_LENGTH;
        int checkLength = Math.min(cursor, MAX_LENGTH);
        for (int i = 1; i <= checkLength; i++) {
            str[MAX_LENGTH - i] = text.getStrSegment(1, cursor - i);
            start--;
        }

        StringBuilder key = new StringBuilder();
        while (start < MAX_LENGTH) {
            for (int i = start; i < MAX_LENGTH; i++) {
                key.append(str[i].string);
            }
            boolean upper = Character.isUpperCase(key.charAt(key.length() - 1));
            String match = Romkan.romkanTable.get(key.toString().toLowerCase());
            if (match != null) {
                if (upper) {
                    match = match.toUpperCase();
                }
                StrSegment[] out;
                if (match.length() == 1) {
                    out = new StrSegment[1];
                    out[0] = new StrSegment(match, str[start].from, str[MAX_LENGTH - 1].to);
                    text.replaceStrSegment(ComposingText.LAYER1, out, MAX_LENGTH - start);
                } else {
                    out = new StrSegment[2];
                    out[0] = new StrSegment(match.substring(0, match.length() - 1),
                            str[start].from, str[MAX_LENGTH - 1].to - 1);
                    out[1] = new StrSegment(match.substring(match.length() - 1),
                            str[MAX_LENGTH - 1].to, str[MAX_LENGTH - 1].to);
                    text.replaceStrSegment(1, out, MAX_LENGTH - start);
                }
                return true;
            }
            start++;
            key.delete(0, key.length());
        }

        return false;
    }

    /** @see LetterConverter#setPreferences */
    public void setPreferences(SharedPreferences pref) {
    }
}
