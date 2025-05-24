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

/**
 * The Romaji to half-width Katakana converter class for Japanese IME.
 *
 * @author Copyright (C) 2009 OMRON SOFTWARE CO., LTD.  All Rights Reserved.
 */
public class RomkanHalfKatakana implements LetterConverter {
    /** HashMap for Romaji-to-Kana conversion (Japanese mode) */
    private static final HashMap<String, String> mRomkanTable = new HashMap<>() {{
        put("", "ｯ");

        put("a", "ｱ"); put("i", "ｲ"); put("u", "ｳ"); put("e", "ｴ"); put("o", "ｵ");

        put("ba", "ﾊﾞ"); put("bi", "ﾋﾞ"); put("bu", "ﾌﾞ"); put("be", "ﾍﾞ"); put("bo", "ﾎﾞ");
        put("bya", "ﾋﾞｬ"); put("byi", "ﾋﾞｨ"); put("byu", "ﾋﾞｭ"); put("bye", "ﾋﾞｪ"); put("byo", "ﾋﾞｮ");

        put("ca", "ｶ"); put("ci", "ｼ"); put("cu", "ｸ"); put("ce", "ｾ"); put("co", "ｺ");
        put("cha", "ﾁｬ"); put("chi", "ﾁ"); put("chu", "ﾁｭ"); put("che", "ﾁｪ"); put("cho", "ﾁｮ");
        put("cya", "ﾁｬ"); put("cyi", "ﾁｨ"); put("cyu", "ﾁｭ"); put("cye", "ﾁｪ"); put("cyo", "ﾁｮ");

        put("da", "ﾀﾞ"); put("di", "ﾁﾞ"); put("du", "ﾂﾞ"); put("de", "ﾃﾞ"); put("do", "ﾄﾞ");
        put("dha", "ﾃﾞｬ"); put("dhi", "ﾃﾞｨ"); put("dhu", "ﾃﾞｭ"); put("dhe", "ﾃﾞｪ"); put("dho", "ﾃﾞｮ");
        put("dwa", "ﾄﾞｧ"); put("dwi", "ﾄﾞｨ"); put("dwu", "ﾄﾞｩ"); put("dwe", "ﾄﾞｪ"); put("dwo", "ﾄﾞｫ");
        put("dya", "ﾁﾞｬ"); put("dyi", "ﾁﾞｨ"); put("dyu", "ﾁﾞｭ"); put("dye", "ﾁﾞｪ"); put("dyo", "ﾁﾞｮ");

        put("fa", "ﾌｧ"); put("fi", "ﾌｨ"); put("fu", "ﾌ"); put("fe", "ﾌｪ"); put("fo", "ﾌｫ");
        put("fwa", "ﾌｧ"); put("fwi", "ﾌｨ"); put("fwu", "ﾌｩ"); put("fwe", "ﾌｪ"); put("fwo", "ﾌｫ");
        put("fya", "ﾌｬ"); put("fyi", "ﾌｨ"); put("fyu", "ﾌｭ"); put("fye", "ﾌｪ"); put("fyo", "ﾌｮ");

        put("ga", "ｶﾞ"); put("gi", "ｷﾞ"); put("gu", "ｸﾞ"); put("ge", "ｹﾞ"); put("go", "ｺﾞ");
        put("gwa", "ｸﾞｧ"); put("gwi", "ｸﾞｨ"); put("gwu", "ｸﾞｩ"); put("gwe", "ｸﾞｪ"); put("gwo", "ｸﾞｫ");
        put("gya", "ｷﾞｬ"); put("gyi", "ｷﾞｨ"); put("gyu", "ｷﾞｭ"); put("gye", "ｷﾞｪ"); put("gyo", "ｷﾞｮ");

        put("ha", "ﾊ"); put("hi", "ﾋ"); put("hu", "ﾌ"); put("he", "ﾍ"); put("ho", "ﾎ");
        put("hya", "ﾋｬ"); put("hyi", "ﾋｨ"); put("hyu", "ﾋｭ"); put("hye", "ﾋｪ"); put("hyo", "ﾋｮ");

        put("ja", "ｼﾞｬ"); put("ji", "ｼﾞ"); put("ju", "ｼﾞｭ"); put("je", "ｼﾞｪ"); put("jo", "ｼﾞｮ");
        put("jya", "ｼﾞｬ"); put("jyi", "ｼﾞｨ"); put("jyu", "ｼﾞｭ"); put("jye", "ｼﾞｪ"); put("jyo", "ｼﾞｮ");

        put("ka", "ｶ"); put("ki", "ｷ"); put("ku", "ｸ"); put("ke", "ｹ"); put("ko", "ｺ");
        put("kya", "ｷｬ"); put("kyi", "ｷｨ"); put("kyu", "ｷｭ"); put("kye", "ｷｪ"); put("kyo", "ｷｮ");

        put("kwa", "ｸｧ");

        put("la", "ｧ"); put("li", "ｨ"); put("lu", "ｩ"); put("le", "ｪ"); put("lo", "ｫ");
        put("lya", "ｬ"); put("lyi", "ｨ"); put("lyu", "ｭ"); put("lye", "ｪ"); put("lyo", "ｮ");

        put("ltsu", "ｯ");
        put("ltu", "ｯ");
        put("lwa", "ﾜ");

        put("ma", "ﾏ"); put("mi", "ﾐ"); put("mu", "ﾑ"); put("me", "ﾒ"); put("mo", "ﾓ");
        put("mya", "ﾐｬ"); put("myi", "ﾐｨ"); put("myu", "ﾐｭ"); put("mye", "ﾐｪ"); put("myo", "ﾐｮ");

        put("na", "ﾅ"); put("ni", "ﾆ"); put("nu", "ﾇ"); put("ne", "ﾈ"); put("no", "ﾉ");
        put("nya", "ﾆｬ"); put("nyi", "ﾆｨ"); put("nyu", "ﾆｭ"); put("nye", "ﾆｪ"); put("nyo", "ﾆｮ");

        put("pa", "ﾊﾟ"); put("pi", "ﾋﾟ"); put("pu", "ﾌﾟ"); put("pe", "ﾍﾟ"); put("po", "ﾎﾟ");
        put("pya", "ﾋﾟｬ"); put("pyi", "ﾋﾟｨ"); put("pyu", "ﾋﾟｭ"); put("pye", "ﾋﾟｪ"); put("pyo", "ﾋﾟｮ");

        put("qa", "ｸｧ"); put("qi", "ｸｨ"); put("qu", "ｸ"); put("qe", "ｸｪ"); put("qo", "ｸｫ");
        put("qwa", "ｸｧ"); put("qwi", "ｸｨ"); put("qwu", "ｸｩ"); put("qwe", "ｸｪ"); put("qwo", "ｸｫ");
        put("qya", "ｸｬ"); put("qyi", "ｸｨ"); put("qyu", "ｸｭ"); put("qye", "ｸｪ"); put("qyo", "ｸｮ");

        put("ra", "ﾗ"); put("ri", "ﾘ"); put("ru", "ﾙ"); put("re", "ﾚ"); put("ro", "ﾛ");
        put("rya", "ﾘｬ"); put("ryi", "ﾘｨ"); put("ryu", "ﾘｭ"); put("rye", "ﾘｪ"); put("ryo", "ﾘｮ");

        put("sa", "ｻ"); put("si", "ｼ"); put("su", "ｽ"); put("se", "ｾ"); put("so", "ｿ");
        put("sha", "ｼｬ"); put("shi", "ｼ"); put("shu", "ｼｭ"); put("she", "ｼｪ"); put("sho", "ｼｮ");
        put("swa", "ｽｧ"); put("swi", "ｽｨ"); put("swu", "ｽｩ"); put("swe", "ｽｪ"); put("swo", "ｽｫ");
        put("sya", "ｼｬ"); put("syi", "ｼｨ"); put("syu", "ｼｭ"); put("sye", "ｼｪ"); put("syo", "ｼｮ");

        put("ta", "ﾀ"); put("ti", "ﾁ"); put("tu", "ﾂ"); put("te", "ﾃ"); put("to", "ﾄ");
        put("tha", "ﾃｬ"); put("thi", "ﾃｨ"); put("thu", "ﾃｭ"); put("the", "ﾃｪ"); put("tho", "ﾃｮ");
        put("tsa", "ﾂｧ"); put("tsi", "ﾂｨ"); put("tsu", "ﾂ"); put("tse", "ﾂｪ"); put("tso", "ﾂｫ");
        put("twa", "ﾄｧ"); put("twi", "ﾄｨ"); put("twu", "ﾄｩ"); put("twe", "ﾄｪ"); put("two", "ﾄｫ");
        put("tya", "ﾁｬ"); put("tyi", "ﾁｨ"); put("tyu", "ﾁｭ"); put("tye", "ﾁｪ"); put("tyo", "ﾁｮ");

        put("va", "ｳﾞｧ"); put("vi", "ｳﾞｨ"); put("vu", "ｳﾞ"); put("ve", "ｳﾞｪ"); put("vo", "ｳﾞｫ");
        put("vya", "ｳﾞｬ"); put("vyi", "ｳﾞｨ"); put("vyu", "ｳﾞｭ"); put("vye", "ｳﾞｪ"); put("vyo", "ｳﾞｮ");

        put("wa", "ﾜ"); put("wi", "ｳｨ"); put("wu", "ｳ"); put("we", "ｳｪ"); put("wo", "ｦ");
        put("wha", "ｳｧ"); put("whi", "ｳｨ"); put("whu", "ｳ"); put("whe", "ｳｪ"); put("who", "ｳｫ");

        put("xa", "ｧ"); put("xi", "ｨ"); put("xu", "ｩ"); put("xe", "ｪ"); put("xo", "ｫ");
        put("xya", "ｬ"); put("xyi", "ｨ"); put("xyu", "ｭ"); put("xye", "ｪ"); put("xyo", "ｮ");

        put("xn", "ﾝ");
        put("xtu", "ｯ");
        put("xwa", "ﾜ");

        put("ya", "ﾔ"); put("yi", "ｲ"); put("yu", "ﾕ"); put("ye", "ｲｪ"); put("yo", "ﾖ");

        put("za", "ｻﾞ"); put("zi", "ｼﾞ"); put("zu", "ｽﾞ"); put("ze", "ｾﾞ"); put("zo", "ｿﾞ");
        put("zya", "ｼﾞｬ"); put("zyi", "ｼﾞｨ"); put("zyu", "ｼﾞｭ"); put("zye", "ｼﾞｪ"); put("zyo", "ｼﾞｮ");

        put("bb", "ｯb");
        put("cc", "ｯc");
        put("dd", "ｯd");
        put("ff", "ｯf");
        put("gg", "ｯg");
        put("hh", "ｯh");
        put("jj", "ｯj");
        put("kk", "ｯk");
        put("ll", "ｯl");
        put("mm", "ｯm");
        put("pp", "ｯp");
        put("qq", "ｯq");
        put("rr", "ｯr");
        put("ss", "ｯs");
        put("tt", "ｯt");
        put("vv", "ｯv");
        put("ww", "ｯw");
        put("xx", "ｯx");
        put("yy", "ｯy");
        put("zz", "ｯz");

        put("nb", "ﾝb");
        put("nc", "ﾝc");
        put("nd", "ﾝd");
        put("nf", "ﾝf");
        put("ng", "ﾝg");
        put("nh", "ﾝh");
        put("nj", "ﾝj");
        put("nk", "ﾝk");
        put("nm", "ﾝm");
        put("nn", "ﾝ");
        put("np", "ﾝp");
        put("nq", "ﾝq");
        put("nr", "ﾝr");
        put("ns", "ﾝs");
        put("nt", "ﾝt");
        put("nv", "ﾝv");
        put("nw", "ﾝw");
        put("nx", "ﾝx");
        put("nz", "ﾝz");
        put("nl", "ﾝl");

        put("-", "ｰ");
        put(".", "｡");
        put(",", "､");
        put("/", "･");
    }};

    /**
     * Default constructor
     */
    public RomkanHalfKatakana() {
        super();
    }

    /** @see LetterConverter#convert */
    public boolean convert(ComposingText text) {
        return RomkanFullKatakana.convert(text, mRomkanTable);
    }

    /** @see LetterConverter#setPreferences */
    public void setPreferences(SharedPreferences pref) {
    }
}
