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
 * The Romaji to full-width Katakana converter class for Japanese IME.
 *
 * @author Copyright (C) 2009 OMRON SOFTWARE CO., LTD.  All Rights Reserved.
 */
public class RomkanFullKatakana implements LetterConverter {
    /** HashMap for Romaji-to-Kana conversion (Japanese mode) */
    private static final HashMap<String, String> mRomkanTable = new HashMap<>() {{
        put("", "ッ");

        put("a", "ア"); put("i", "イ"); put("u", "ウ"); put("e", "エ"); put("o", "オ");

        put("ba", "バ"); put("bi", "ビ"); put("bu", "ブ"); put("be", "ベ"); put("bo", "ボ");
        put("bya", "ビャ"); put("byi", "ビィ"); put("byu", "ビュ"); put("bye", "ビェ"); put("byo", "ビョ");

        put("ca", "カ"); put("ci", "シ"); put("cu", "ク"); put("ce", "セ"); put("co", "コ");
        put("cha", "チャ"); put("chi", "チ"); put("chu", "チュ"); put("che", "チェ"); put("cho", "チョ");
        put("cya", "チャ"); put("cyi", "チィ"); put("cyu", "チュ"); put("cye", "チェ"); put("cyo", "チョ");

        put("da", "ダ"); put("di", "ヂ"); put("du", "ヅ"); put("de", "デ"); put("do", "ド");
        put("dha", "デャ"); put("dhi", "ディ"); put("dhu", "デュ"); put("dhe", "デェ"); put("dho", "デョ");
        put("dwa", "ドァ"); put("dwi", "ドィ"); put("dwu", "ドゥ"); put("dwe", "ドェ"); put("dwo", "ドォ");
        put("dya", "ヂャ"); put("dyi", "ヂィ"); put("dyu", "ヂュ"); put("dye", "ヂェ"); put("dyo", "ヂョ");

        put("fa", "ファ"); put("fi", "フィ"); put("fu", "フ"); put("fe", "フェ"); put("fo", "フォ");
        put("fwa", "ファ"); put("fwi", "フィ"); put("fwu", "フゥ"); put("fwe", "フェ"); put("fwo", "フォ");
        put("fya", "フャ"); put("fyi", "フィ"); put("fyu", "フュ"); put("fye", "フェ"); put("fyo", "フョ");

        put("ga", "ガ"); put("gi", "ギ"); put("gu", "グ"); put("ge", "ゲ"); put("go", "ゴ");
        put("gwa", "グァ"); put("gwi", "グィ"); put("gwu", "グゥ"); put("gwe", "グェ"); put("gwo", "グォ");
        put("gya", "ギャ"); put("gyi", "ギィ"); put("gyu", "ギュ"); put("gye", "ギェ"); put("gyo", "ギョ");

        put("ha", "ハ"); put("hi", "ヒ"); put("hu", "フ"); put("he", "ヘ"); put("ho", "ホ");
        put("hya", "ヒャ"); put("hyi", "ヒィ"); put("hyu", "ヒュ"); put("hye", "ヒェ"); put("hyo", "ヒョ");

        put("ja", "ジャ"); put("ji", "ジ"); put("ju", "ジュ"); put("je", "ジェ"); put("jo", "ジョ");
        put("jya", "ジャ"); put("jyi", "ジィ"); put("jyu", "ジュ"); put("jye", "ジェ"); put("jyo", "ジョ");

        put("ka", "カ"); put("ki", "キ"); put("ku", "ク"); put("ke", "ケ"); put("ko", "コ");
        put("kya", "キャ"); put("kyi", "キィ"); put("kyu", "キュ"); put("kye", "キェ"); put("kyo", "キョ");

        put("kwa", "クァ");

        put("la", "ァ"); put("li", "ィ"); put("lu", "ゥ"); put("le", "ェ"); put("lo", "ォ");
        put("lya", "ャ"); put("lyi", "ィ"); put("lyu", "ュ"); put("lye", "ェ"); put("lyo", "ョ");

        put("lka", "ヵ");
        put("lke", "ヶ");
        put("ltsu", "ッ");
        put("ltu", "ッ");
        put("lwa", "ヮ");

        put("ma", "マ"); put("mi", "ミ"); put("mu", "ム"); put("me", "メ"); put("mo", "モ");
        put("mya", "ミャ"); put("myi", "ミィ"); put("myu", "ミュ"); put("mye", "ミェ"); put("myo", "ミョ");

        put("na", "ナ"); put("ni", "ニ"); put("nu", "ヌ"); put("ne", "ネ"); put("no", "ノ");
        put("nya", "ニャ"); put("nyi", "ニィ"); put("nyu", "ニュ"); put("nye", "ニェ"); put("nyo", "ニョ");

        put("pa", "パ"); put("pi", "ピ"); put("pu", "プ"); put("pe", "ペ"); put("po", "ポ");
        put("pya", "ピャ"); put("pyi", "ピィ"); put("pyu", "ピュ"); put("pye", "ピェ"); put("pyo", "ピョ");

        put("qa", "クァ"); put("qi", "クィ"); put("qu", "ク"); put("qe", "クェ"); put("qo", "クォ");
        put("qwa", "クァ"); put("qwi", "クィ"); put("qwu", "クゥ"); put("qwe", "クェ"); put("qwo", "クォ");
        put("qya", "クャ"); put("qyi", "クィ"); put("qyu", "クュ"); put("qye", "クェ"); put("qyo", "クョ");

        put("ra", "ラ"); put("ri", "リ"); put("ru", "ル"); put("re", "レ"); put("ro", "ロ");
        put("rya", "リャ"); put("ryi", "リィ"); put("ryu", "リュ"); put("rye", "リェ"); put("ryo", "リョ");

        put("sa", "サ"); put("si", "シ"); put("su", "ス"); put("se", "セ"); put("so", "ソ");
        put("sha", "シャ"); put("shi", "シ"); put("shu", "シュ"); put("she", "シェ"); put("sho", "ショ");
        put("swa", "スァ"); put("swi", "スィ"); put("swu", "スゥ"); put("swe", "スェ"); put("swo", "スォ");
        put("sya", "シャ"); put("syi", "シィ"); put("syu", "シュ"); put("sye", "シェ"); put("syo", "ショ");

        put("ta", "タ"); put("ti", "チ"); put("tu", "ツ"); put("te", "テ"); put("to", "ト");
        put("tha", "テャ"); put("thi", "ティ"); put("thu", "テュ"); put("the", "テェ"); put("tho", "テョ");
        put("tsa", "ツァ"); put("tsi", "ツィ"); put("tsu", "ツ"); put("tse", "ツェ"); put("tso", "ツォ");
        put("twa", "トァ"); put("twi", "トィ"); put("twu", "トゥ"); put("twe", "トェ"); put("two", "トォ");
        put("tya", "チャ"); put("tyi", "チィ"); put("tyu", "チュ"); put("tye", "チェ"); put("tyo", "チョ");

        put("va", "ヴァ"); put("vi", "ヴィ"); put("vu", "ヴ"); put("ve", "ヴェ"); put("vo", "ヴォ");
        put("vya", "ヴャ"); put("vyi", "ヴィ"); put("vyu", "ヴュ"); put("vye", "ヴェ"); put("vyo", "ヴョ");

        put("wa", "ワ"); put("wi", "ウィ"); put("wu", "ウ"); put("we", "ウェ"); put("wo", "ヲ");
        put("wha", "ウァ"); put("whi", "ウィ"); put("whu", "ウ"); put("whe", "ウェ"); put("who", "ウォ");

        put("xa", "ァ"); put("xi", "ィ"); put("xu", "ゥ"); put("xe", "ェ"); put("xo", "ォ");
        put("xya", "ャ"); put("xyi", "ィ"); put("xyu", "ュ"); put("xye", "ェ"); put("xyo", "ョ");

        put("xn", "ン");
        put("xka", "ヵ");
        put("xke", "ヶ");
        put("xtu", "ッ");
        put("xwa", "ヮ");

        put("ya", "ヤ"); put("yi", "イ"); put("yu", "ユ"); put("ye", "イェ"); put("yo", "ヨ");

        put("za", "ザ"); put("zi", "ジ"); put("zu", "ズ"); put("ze", "ゼ"); put("zo", "ゾ");
        put("zya", "ジャ"); put("zyi", "ジィ"); put("zyu", "ジュ"); put("zye", "ジェ"); put("zyo", "ジョ");

        put("bb", "ッb");
        put("cc", "ッc");
        put("dd", "ッd");
        put("ff", "ッf");
        put("gg", "ッg");
        put("hh", "ッh");
        put("jj", "ッj");
        put("kk", "ッk");
        put("ll", "ッl");
        put("mm", "ッm");
        put("pp", "ッp");
        put("qq", "ッq");
        put("rr", "ッr");
        put("ss", "ッs");
        put("tt", "ッt");
        put("vv", "ッv");
        put("ww", "ッw");
        put("xx", "ッx");
        put("yy", "ッy");
        put("zz", "ッz");

        put("nb", "ンb");
        put("nc", "ンc");
        put("nd", "ンd");
        put("nf", "ンf");
        put("ng", "ンg");
        put("nh", "ンh");
        put("nj", "ンj");
        put("nk", "ンk");
        put("nm", "ンm");
        put("nn", "ン");
        put("np", "ンp");
        put("nq", "ンq");
        put("nr", "ンr");
        put("ns", "ンs");
        put("nt", "ンt");
        put("nv", "ンv");
        put("nw", "ンw");
        put("nx", "ンx");
        put("nz", "ンz");
        put("nl", "ンl");

        put(",", "、");
        put("-", "ー");
        put(".", "。");
        put("/", "・");
        put("?", "？");
    }};

    /** Max length of the target text */
    private static final int MAX_LENGTH = 4;

    /**
     * Default constructor
     */
    public RomkanFullKatakana() {
        super();
    }

    /** @see LetterConverter#convert */
    public boolean convert(ComposingText text) {
        return convert(text, mRomkanTable);
    }

    /**
     * convert Romaji to Full Katakana
     *
     * @param text              The input/output text
     * @param table             HashMap for Romaji-to-Kana conversion
     * @return                  {@code true} if conversion is completed; {@code false} if not
     */
    public static boolean convert(ComposingText text, HashMap<String, String> table) {
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
            String match = table.get(key.toString().toLowerCase());
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
