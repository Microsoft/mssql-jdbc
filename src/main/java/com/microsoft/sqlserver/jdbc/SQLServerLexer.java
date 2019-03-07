// Generated from SQLServerLexer.g4 by ANTLR 4.7.2
package com.microsoft.sqlserver.jdbc;

import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.LexerATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;


@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SQLServerLexer extends Lexer {
        static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

        protected static final DFA[] _decisionToDFA;
        protected static final PredictionContextCache _sharedContextCache =
                new PredictionContextCache();
        public static final int
                SELECT=1, INSERT=2, DELETE=3, UPDATE=4, FROM=5, INTO=6, EXECUTE=7, WHERE=8, 
                HAVING=9, GROUP=10, ORDER=11, OPTION=12, BY=13, VALUES=14, OUTPUT=15, 
                OJ=16, IPV4_ADDR=17, DOLLAR_ACTION=18, SPACE=19, COMMENT=20, LINE_COMMENT=21, 
                DOUBLE_QUOTE=22, SINGLE_QUOTE=23, LOCAL_ID=24, DECIMAL=25, ID=26, QUOTED_URL=27, 
                QUOTED_HOST_AND_PORT=28, STRING=29, BINARY=30, FLOAT=31, REAL=32, EQUAL=33, 
                GREATER=34, LESS=35, EXCLAMATION=36, PLUS_ASSIGN=37, MINUS_ASSIGN=38, 
                MULT_ASSIGN=39, DIV_ASSIGN=40, MOD_ASSIGN=41, AND_ASSIGN=42, XOR_ASSIGN=43, 
                OR_ASSIGN=44, DOUBLE_BAR=45, DOT=46, UNDERLINE=47, AT=48, SHARP=49, DOLLAR=50, 
                LR_BRACKET=51, RR_BRACKET=52, LS_BRACKET=53, RS_BRACKET=54, LC_BRACKET=55, 
                RC_BRACKET=56, COMMA=57, SEMI=58, COLON=59, STAR=60, DIVIDE=61, MODULE=62, 
                PLUS=63, MINUS=64, BIT_NOT=65, BIT_OR=66, BIT_AND=67, BIT_XOR=68, PARAMETER=69, 
                IPV4_OCTECT=70;
        public static String[] channelNames = {
                "DEFAULT_TOKEN_CHANNEL", "HIDDEN"
        };

        public static String[] modeNames = {
                "DEFAULT_MODE"
        };

        private static String[] makeRuleNames() {
                return new String[] {
                        "SELECT", "INSERT", "DELETE", "UPDATE", "FROM", "INTO", "EXECUTE", "WHERE", 
                        "HAVING", "GROUP", "ORDER", "OPTION", "BY", "VALUES", "OUTPUT", "OJ", 
                        "IPV4_ADDR", "DOLLAR_ACTION", "SPACE", "COMMENT", "LINE_COMMENT", "DOUBLE_QUOTE", 
                        "SINGLE_QUOTE", "LOCAL_ID", "DECIMAL", "ID", "QUOTED_URL", "QUOTED_HOST_AND_PORT", 
                        "STRING", "BINARY", "FLOAT", "REAL", "EQUAL", "GREATER", "LESS", "EXCLAMATION", 
                        "PLUS_ASSIGN", "MINUS_ASSIGN", "MULT_ASSIGN", "DIV_ASSIGN", "MOD_ASSIGN", 
                        "AND_ASSIGN", "XOR_ASSIGN", "OR_ASSIGN", "DOUBLE_BAR", "DOT", "UNDERLINE", 
                        "AT", "SHARP", "DOLLAR", "LR_BRACKET", "RR_BRACKET", "LS_BRACKET", "RS_BRACKET", 
                        "LC_BRACKET", "RC_BRACKET", "COMMA", "SEMI", "COLON", "STAR", "DIVIDE", 
                        "MODULE", "PLUS", "MINUS", "BIT_NOT", "BIT_OR", "BIT_AND", "BIT_XOR", 
                        "PARAMETER", "IPV4_OCTECT", "DEC_DOT_DEC", "HEX_DIGIT", "DEC_DIGIT", 
                        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", 
                        "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "FullWidthLetter"
                };
        }
        public static final String[] ruleNames = makeRuleNames();

        private static String[] makeLiteralNames() {
                return new String[] {
                        null, null, null, null, null, null, null, null, null, null, null, null, 
                        null, null, null, null, null, null, "'$ACTION'", null, null, null, "'\"'", 
                        "'''", null, null, null, null, null, null, null, null, null, "'='", "'>'", 
                        "'<'", "'!'", "'+='", "'-='", "'*='", "'/='", "'%='", "'&='", "'^='", 
                        "'|='", "'||'", "'.'", "'_'", "'@'", "'#'", "'$'", "'('", "')'", "'['", 
                        "']'", "'{'", "'}'", "','", "';'", "':'", "'*'", "'/'", "'%'", "'+'", 
                        "'-'", "'~'", "'|'", "'&'", "'^'", "'?'"
                };
        }
        private static final String[] _LITERAL_NAMES = makeLiteralNames();
        private static String[] makeSymbolicNames() {
                return new String[] {
                        null, "SELECT", "INSERT", "DELETE", "UPDATE", "FROM", "INTO", "EXECUTE", 
                        "WHERE", "HAVING", "GROUP", "ORDER", "OPTION", "BY", "VALUES", "OUTPUT", 
                        "OJ", "IPV4_ADDR", "DOLLAR_ACTION", "SPACE", "COMMENT", "LINE_COMMENT", 
                        "DOUBLE_QUOTE", "SINGLE_QUOTE", "LOCAL_ID", "DECIMAL", "ID", "QUOTED_URL", 
                        "QUOTED_HOST_AND_PORT", "STRING", "BINARY", "FLOAT", "REAL", "EQUAL", 
                        "GREATER", "LESS", "EXCLAMATION", "PLUS_ASSIGN", "MINUS_ASSIGN", "MULT_ASSIGN", 
                        "DIV_ASSIGN", "MOD_ASSIGN", "AND_ASSIGN", "XOR_ASSIGN", "OR_ASSIGN", 
                        "DOUBLE_BAR", "DOT", "UNDERLINE", "AT", "SHARP", "DOLLAR", "LR_BRACKET", 
                        "RR_BRACKET", "LS_BRACKET", "RS_BRACKET", "LC_BRACKET", "RC_BRACKET", 
                        "COMMA", "SEMI", "COLON", "STAR", "DIVIDE", "MODULE", "PLUS", "MINUS", 
                        "BIT_NOT", "BIT_OR", "BIT_AND", "BIT_XOR", "PARAMETER", "IPV4_OCTECT"
                };
        }
        private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
        public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

        /**
         * @deprecated Use {@link #VOCABULARY} instead.
         */
        @Deprecated
        public static final String[] tokenNames;
        static {
                tokenNames = new String[_SYMBOLIC_NAMES.length];
                for (int i = 0; i < tokenNames.length; i++) {
                        tokenNames[i] = VOCABULARY.getLiteralName(i);
                        if (tokenNames[i] == null) {
                                tokenNames[i] = VOCABULARY.getSymbolicName(i);
                        }

                        if (tokenNames[i] == null) {
                                tokenNames[i] = "<INVALID>";
                        }
                }
        }

        @Override
        @Deprecated
        public String[] getTokenNames() {
                return tokenNames;
        }

        @Override

        public Vocabulary getVocabulary() {
                return VOCABULARY;
        }


        public SQLServerLexer(CharStream input) {
                super(input);
                _interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
        }

        @Override
        public String getGrammarFileName() { return "SQLServerLexer.g4"; }

        @Override
        public String[] getRuleNames() { return ruleNames; }

        @Override
        public String getSerializedATN() { return _serializedATN; }

        @Override
        public String[] getChannelNames() { return channelNames; }

        @Override
        public String[] getModeNames() { return modeNames; }

        @Override
        public ATN getATN() { return _ATN; }

        public static final String _serializedATN =
                "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2H\u028a\b\1\4\2\t"+
                "\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
                "\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
                "\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
                "\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
                "\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
                ",\t,\4-\t-\4.\t.\4/\t/\4\60\t\60\4\61\t\61\4\62\t\62\4\63\t\63\4\64\t"+
                "\64\4\65\t\65\4\66\t\66\4\67\t\67\48\t8\49\t9\4:\t:\4;\t;\4<\t<\4=\t="+
                "\4>\t>\4?\t?\4@\t@\4A\tA\4B\tB\4C\tC\4D\tD\4E\tE\4F\tF\4G\tG\4H\tH\4I"+
                "\tI\4J\tJ\4K\tK\4L\tL\4M\tM\4N\tN\4O\tO\4P\tP\4Q\tQ\4R\tR\4S\tS\4T\tT"+
                "\4U\tU\4V\tV\4W\tW\4X\tX\4Y\tY\4Z\tZ\4[\t[\4\\\t\\\4]\t]\4^\t^\4_\t_\4"+
                "`\t`\4a\ta\4b\tb\4c\tc\4d\td\4e\te\3\2\3\2\3\2\3\2\3\2\3\2\3\2\3\3\3\3"+
                "\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3"+
                "\5\3\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\b"+
                "\3\b\3\b\5\b\u00fa\n\b\3\t\3\t\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n"+
                "\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r"+
                "\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20"+
                "\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\22\5\22\u0131\n\22\3\22"+
                "\3\22\3\22\3\22\3\22\3\22\3\22\3\22\5\22\u013b\n\22\3\23\3\23\3\23\3\23"+
                "\3\23\3\23\3\23\3\23\3\24\6\24\u0146\n\24\r\24\16\24\u0147\3\24\3\24\3"+
                "\25\3\25\3\25\3\25\3\25\7\25\u0151\n\25\f\25\16\25\u0154\13\25\3\25\3"+
                "\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\7\26\u015f\n\26\f\26\16\26\u0162"+
                "\13\26\3\26\3\26\3\27\3\27\3\30\3\30\3\31\3\31\3\31\6\31\u016d\n\31\r"+
                "\31\16\31\u016e\3\32\6\32\u0172\n\32\r\32\16\32\u0173\3\33\3\33\5\33\u0178"+
                "\n\33\3\33\3\33\7\33\u017c\n\33\f\33\16\33\u017f\13\33\3\34\3\34\3\34"+
                "\6\34\u0184\n\34\r\34\16\34\u0185\3\34\3\34\3\34\3\34\3\34\3\34\6\34\u018e"+
                "\n\34\r\34\16\34\u018f\3\34\3\34\6\34\u0194\n\34\r\34\16\34\u0195\5\34"+
                "\u0198\n\34\3\34\5\34\u019b\n\34\3\34\3\34\3\34\3\34\3\35\3\35\6\35\u01a3"+
                "\n\35\r\35\16\35\u01a4\3\35\3\35\6\35\u01a9\n\35\r\35\16\35\u01aa\5\35"+
                "\u01ad\n\35\3\35\5\35\u01b0\n\35\3\35\3\35\3\35\3\35\3\35\3\36\5\36\u01b8"+
                "\n\36\3\36\3\36\3\36\3\36\7\36\u01be\n\36\f\36\16\36\u01c1\13\36\3\36"+
                "\3\36\3\37\3\37\3\37\7\37\u01c8\n\37\f\37\16\37\u01cb\13\37\3 \3 \3!\3"+
                "!\5!\u01d1\n!\3!\3!\5!\u01d5\n!\3!\6!\u01d8\n!\r!\16!\u01d9\3\"\3\"\3"+
                "#\3#\3$\3$\3%\3%\3&\3&\3&\3\'\3\'\3\'\3(\3(\3(\3)\3)\3)\3*\3*\3*\3+\3"+
                "+\3+\3,\3,\3,\3-\3-\3-\3.\3.\3.\3/\3/\3\60\3\60\3\61\3\61\3\62\3\62\3"+
                "\63\3\63\3\64\3\64\3\65\3\65\3\66\3\66\3\67\3\67\38\38\39\39\3:\3:\3;"+
                "\3;\3<\3<\3=\3=\3>\3>\3?\3?\3@\3@\3A\3A\3B\3B\3C\3C\3D\3D\3E\3E\3F\3F"+
                "\3G\5G\u0230\nG\3G\5G\u0233\nG\3G\3G\3H\6H\u0238\nH\rH\16H\u0239\3H\3"+
                "H\6H\u023e\nH\rH\16H\u023f\3H\6H\u0243\nH\rH\16H\u0244\3H\3H\3H\3H\6H"+
                "\u024b\nH\rH\16H\u024c\5H\u024f\nH\3I\3I\3J\3J\3K\3K\3L\3L\3M\3M\3N\3"+
                "N\3O\3O\3P\3P\3Q\3Q\3R\3R\3S\3S\3T\3T\3U\3U\3V\3V\3W\3W\3X\3X\3Y\3Y\3"+
                "Z\3Z\3[\3[\3\\\3\\\3]\3]\3^\3^\3_\3_\3`\3`\3a\3a\3b\3b\3c\3c\3d\3d\3e"+
                "\3e\3\u0152\2f\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16"+
                "\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34"+
                "\67\359\36;\37= ?!A\"C#E$G%I&K\'M(O)Q*S+U,W-Y.[/]\60_\61a\62c\63e\64g"+
                "\65i\66k\67m8o9q:s;u<w=y>{?}@\177A\u0081B\u0083C\u0085D\u0087E\u0089F"+
                "\u008bG\u008dH\u008f\2\u0091\2\u0093\2\u0095\2\u0097\2\u0099\2\u009b\2"+
                "\u009d\2\u009f\2\u00a1\2\u00a3\2\u00a5\2\u00a7\2\u00a9\2\u00ab\2\u00ad"+
                "\2\u00af\2\u00b1\2\u00b3\2\u00b5\2\u00b7\2\u00b9\2\u00bb\2\u00bd\2\u00bf"+
                "\2\u00c1\2\u00c3\2\u00c5\2\u00c7\2\u00c9\2\3\2)\3\2))\5\2\13\f\17\17\""+
                "\"\4\2\f\f\17\17\6\2%&\62;B\\aa\6\2%%C\\aac|\7\2%&\62;B\\aac|\3\2C\\\3"+
                "\2<<\3\2\60\60\4\2--//\3\2\62;\4\2\62;CH\4\2CCcc\4\2DDdd\4\2EEee\4\2F"+
                "Fff\4\2GGgg\4\2HHhh\4\2IIii\4\2JJjj\4\2KKkk\4\2LLll\4\2MMmm\4\2NNnn\4"+
                "\2OOoo\4\2PPpp\4\2QQqq\4\2RRrr\4\2SSss\4\2TTtt\4\2UUuu\4\2VVvv\4\2WWw"+
                "w\4\2XXxx\4\2YYyy\4\2ZZzz\4\2[[{{\4\2\\\\||\f\2\u00c2\u00d8\u00da\u00f8"+
                "\u00fa\u2001\u2c02\u3001\u3042\u3191\u3302\u3381\u3402\u4001\u4e02\ud801"+
                "\uf902\ufb01\uff02\ufff2\2\u0290\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2"+
                "\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2"+
                "\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2"+
                "\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2"+
                "\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2"+
                "\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2"+
                "\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2K\3\2\2\2\2M\3\2\2\2\2O"+
                "\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\2U\3\2\2\2\2W\3\2\2\2\2Y\3\2\2\2\2[\3\2"+
                "\2\2\2]\3\2\2\2\2_\3\2\2\2\2a\3\2\2\2\2c\3\2\2\2\2e\3\2\2\2\2g\3\2\2\2"+
                "\2i\3\2\2\2\2k\3\2\2\2\2m\3\2\2\2\2o\3\2\2\2\2q\3\2\2\2\2s\3\2\2\2\2u"+
                "\3\2\2\2\2w\3\2\2\2\2y\3\2\2\2\2{\3\2\2\2\2}\3\2\2\2\2\177\3\2\2\2\2\u0081"+
                "\3\2\2\2\2\u0083\3\2\2\2\2\u0085\3\2\2\2\2\u0087\3\2\2\2\2\u0089\3\2\2"+
                "\2\2\u008b\3\2\2\2\2\u008d\3\2\2\2\3\u00cb\3\2\2\2\5\u00d2\3\2\2\2\7\u00d9"+
                "\3\2\2\2\t\u00e0\3\2\2\2\13\u00e7\3\2\2\2\r\u00ec\3\2\2\2\17\u00f1\3\2"+
                "\2\2\21\u00fb\3\2\2\2\23\u0101\3\2\2\2\25\u0108\3\2\2\2\27\u010e\3\2\2"+
                "\2\31\u0114\3\2\2\2\33\u011b\3\2\2\2\35\u011e\3\2\2\2\37\u0125\3\2\2\2"+
                "!\u012c\3\2\2\2#\u0130\3\2\2\2%\u013c\3\2\2\2\'\u0145\3\2\2\2)\u014b\3"+
                "\2\2\2+\u015a\3\2\2\2-\u0165\3\2\2\2/\u0167\3\2\2\2\61\u0169\3\2\2\2\63"+
                "\u0171\3\2\2\2\65\u0177\3\2\2\2\67\u0180\3\2\2\29\u01a0\3\2\2\2;\u01b7"+
                "\3\2\2\2=\u01c4\3\2\2\2?\u01cc\3\2\2\2A\u01d0\3\2\2\2C\u01db\3\2\2\2E"+
                "\u01dd\3\2\2\2G\u01df\3\2\2\2I\u01e1\3\2\2\2K\u01e3\3\2\2\2M\u01e6\3\2"+
                "\2\2O\u01e9\3\2\2\2Q\u01ec\3\2\2\2S\u01ef\3\2\2\2U\u01f2\3\2\2\2W\u01f5"+
                "\3\2\2\2Y\u01f8\3\2\2\2[\u01fb\3\2\2\2]\u01fe\3\2\2\2_\u0200\3\2\2\2a"+
                "\u0202\3\2\2\2c\u0204\3\2\2\2e\u0206\3\2\2\2g\u0208\3\2\2\2i\u020a\3\2"+
                "\2\2k\u020c\3\2\2\2m\u020e\3\2\2\2o\u0210\3\2\2\2q\u0212\3\2\2\2s\u0214"+
                "\3\2\2\2u\u0216\3\2\2\2w\u0218\3\2\2\2y\u021a\3\2\2\2{\u021c\3\2\2\2}"+
                "\u021e\3\2\2\2\177\u0220\3\2\2\2\u0081\u0222\3\2\2\2\u0083\u0224\3\2\2"+
                "\2\u0085\u0226\3\2\2\2\u0087\u0228\3\2\2\2\u0089\u022a\3\2\2\2\u008b\u022c"+
                "\3\2\2\2\u008d\u022f\3\2\2\2\u008f\u024e\3\2\2\2\u0091\u0250\3\2\2\2\u0093"+
                "\u0252\3\2\2\2\u0095\u0254\3\2\2\2\u0097\u0256\3\2\2\2\u0099\u0258\3\2"+
                "\2\2\u009b\u025a\3\2\2\2\u009d\u025c\3\2\2\2\u009f\u025e\3\2\2\2\u00a1"+
                "\u0260\3\2\2\2\u00a3\u0262\3\2\2\2\u00a5\u0264\3\2\2\2\u00a7\u0266\3\2"+
                "\2\2\u00a9\u0268\3\2\2\2\u00ab\u026a\3\2\2\2\u00ad\u026c\3\2\2\2\u00af"+
                "\u026e\3\2\2\2\u00b1\u0270\3\2\2\2\u00b3\u0272\3\2\2\2\u00b5\u0274\3\2"+
                "\2\2\u00b7\u0276\3\2\2\2\u00b9\u0278\3\2\2\2\u00bb\u027a\3\2\2\2\u00bd"+
                "\u027c\3\2\2\2\u00bf\u027e\3\2\2\2\u00c1\u0280\3\2\2\2\u00c3\u0282\3\2"+
                "\2\2\u00c5\u0284\3\2\2\2\u00c7\u0286\3\2\2\2\u00c9\u0288\3\2\2\2\u00cb"+
                "\u00cc\5\u00b9]\2\u00cc\u00cd\5\u009dO\2\u00cd\u00ce\5\u00abV\2\u00ce"+
                "\u00cf\5\u009dO\2\u00cf\u00d0\5\u0099M\2\u00d0\u00d1\5\u00bb^\2\u00d1"+
                "\4\3\2\2\2\u00d2\u00d3\5\u00a5S\2\u00d3\u00d4\5\u00afX\2\u00d4\u00d5\5"+
                "\u00b9]\2\u00d5\u00d6\5\u009dO\2\u00d6\u00d7\5\u00b7\\\2\u00d7\u00d8\5"+
                "\u00bb^\2\u00d8\6\3\2\2\2\u00d9\u00da\5\u009bN\2\u00da\u00db\5\u009dO"+
                "\2\u00db\u00dc\5\u00abV\2\u00dc\u00dd\5\u009dO\2\u00dd\u00de\5\u00bb^"+
                "\2\u00de\u00df\5\u009dO\2\u00df\b\3\2\2\2\u00e0\u00e1\5\u00bd_\2\u00e1"+
                "\u00e2\5\u00b3Z\2\u00e2\u00e3\5\u009bN\2\u00e3\u00e4\5\u0095K\2\u00e4"+
                "\u00e5\5\u00bb^\2\u00e5\u00e6\5\u009dO\2\u00e6\n\3\2\2\2\u00e7\u00e8\5"+
                "\u009fP\2\u00e8\u00e9\5\u00b7\\\2\u00e9\u00ea\5\u00b1Y\2\u00ea\u00eb\5"+
                "\u00adW\2\u00eb\f\3\2\2\2\u00ec\u00ed\5\u00a5S\2\u00ed\u00ee\5\u00afX"+
                "\2\u00ee\u00ef\5\u00bb^\2\u00ef\u00f0\5\u00b1Y\2\u00f0\16\3\2\2\2\u00f1"+
                "\u00f2\5\u009dO\2\u00f2\u00f3\5\u00c3b\2\u00f3\u00f4\5\u009dO\2\u00f4"+
                "\u00f9\5\u0099M\2\u00f5\u00f6\5\u00bd_\2\u00f6\u00f7\5\u00bb^\2\u00f7"+
                "\u00f8\5\u009dO\2\u00f8\u00fa\3\2\2\2\u00f9\u00f5\3\2\2\2\u00f9\u00fa"+
                "\3\2\2\2\u00fa\20\3\2\2\2\u00fb\u00fc\5\u00c1a\2\u00fc\u00fd\5\u00a3R"+
                "\2\u00fd\u00fe\5\u009dO\2\u00fe\u00ff\5\u00b7\\\2\u00ff\u0100\5\u009d"+
                "O\2\u0100\22\3\2\2\2\u0101\u0102\5\u00a3R\2\u0102\u0103\5\u0095K\2\u0103"+
                "\u0104\5\u00bf`\2\u0104\u0105\5\u00a5S\2\u0105\u0106\5\u00afX\2\u0106"+
                "\u0107\5\u00a1Q\2\u0107\24\3\2\2\2\u0108\u0109\5\u00a1Q\2\u0109\u010a"+
                "\5\u00b7\\\2\u010a\u010b\5\u00b1Y\2\u010b\u010c\5\u00bd_\2\u010c\u010d"+
                "\5\u00b3Z\2\u010d\26\3\2\2\2\u010e\u010f\5\u00b1Y\2\u010f\u0110\5\u00b7"+
                "\\\2\u0110\u0111\5\u009bN\2\u0111\u0112\5\u009dO\2\u0112\u0113\5\u00b7"+
                "\\\2\u0113\30\3\2\2\2\u0114\u0115\5\u00b1Y\2\u0115\u0116\5\u00b3Z\2\u0116"+
                "\u0117\5\u00bb^\2\u0117\u0118\5\u00a5S\2\u0118\u0119\5\u00b1Y\2\u0119"+
                "\u011a\5\u00afX\2\u011a\32\3\2\2\2\u011b\u011c\5\u0097L\2\u011c\u011d"+
                "\5\u00c5c\2\u011d\34\3\2\2\2\u011e\u011f\5\u00bf`\2\u011f\u0120\5\u0095"+
                "K\2\u0120\u0121\5\u00abV\2\u0121\u0122\5\u00bd_\2\u0122\u0123\5\u009d"+
                "O\2\u0123\u0124\5\u00b9]\2\u0124\36\3\2\2\2\u0125\u0126\5\u00b1Y\2\u0126"+
                "\u0127\5\u00bd_\2\u0127\u0128\5\u00bb^\2\u0128\u0129\5\u00b3Z\2\u0129"+
                "\u012a\5\u00bd_\2\u012a\u012b\5\u00bb^\2\u012b \3\2\2\2\u012c\u012d\5"+
                "\u00b1Y\2\u012d\u012e\5\u00a7T\2\u012e\"\3\2\2\2\u012f\u0131\t\2\2\2\u0130"+
                "\u012f\3\2\2\2\u0130\u0131\3\2\2\2\u0131\u0132\3\2\2\2\u0132\u0133\5\u008d"+
                "G\2\u0133\u0134\5]/\2\u0134\u0135\5\u008dG\2\u0135\u0136\5]/\2\u0136\u0137"+
                "\5\u008dG\2\u0137\u0138\5]/\2\u0138\u013a\5\u008dG\2\u0139\u013b\t\2\2"+
                "\2\u013a\u0139\3\2\2\2\u013a\u013b\3\2\2\2\u013b$\3\2\2\2\u013c\u013d"+
                "\7&\2\2\u013d\u013e\7C\2\2\u013e\u013f\7E\2\2\u013f\u0140\7V\2\2\u0140"+
                "\u0141\7K\2\2\u0141\u0142\7Q\2\2\u0142\u0143\7P\2\2\u0143&\3\2\2\2\u0144"+
                "\u0146\t\3\2\2\u0145\u0144\3\2\2\2\u0146\u0147\3\2\2\2\u0147\u0145\3\2"+
                "\2\2\u0147\u0148\3\2\2\2\u0148\u0149\3\2\2\2\u0149\u014a\b\24\2\2\u014a"+
                "(\3\2\2\2\u014b\u014c\7\61\2\2\u014c\u014d\7,\2\2\u014d\u0152\3\2\2\2"+
                "\u014e\u0151\5)\25\2\u014f\u0151\13\2\2\2\u0150\u014e\3\2\2\2\u0150\u014f"+
                "\3\2\2\2\u0151\u0154\3\2\2\2\u0152\u0153\3\2\2\2\u0152\u0150\3\2\2\2\u0153"+
                "\u0155\3\2\2\2\u0154\u0152\3\2\2\2\u0155\u0156\7,\2\2\u0156\u0157\7\61"+
                "\2\2\u0157\u0158\3\2\2\2\u0158\u0159\b\25\2\2\u0159*\3\2\2\2\u015a\u015b"+
                "\7/\2\2\u015b\u015c\7/\2\2\u015c\u0160\3\2\2\2\u015d\u015f\n\4\2\2\u015e"+
                "\u015d\3\2\2\2\u015f\u0162\3\2\2\2\u0160\u015e\3\2\2\2\u0160\u0161\3\2"+
                "\2\2\u0161\u0163\3\2\2\2\u0162\u0160\3\2\2\2\u0163\u0164\b\26\2\2\u0164"+
                ",\3\2\2\2\u0165\u0166\7$\2\2\u0166.\3\2\2\2\u0167\u0168\7)\2\2\u0168\60"+
                "\3\2\2\2\u0169\u016c\7B\2\2\u016a\u016d\t\5\2\2\u016b\u016d\5\u00c9e\2"+
                "\u016c\u016a\3\2\2\2\u016c\u016b\3\2\2\2\u016d\u016e\3\2\2\2\u016e\u016c"+
                "\3\2\2\2\u016e\u016f\3\2\2\2\u016f\62\3\2\2\2\u0170\u0172\5\u0093J\2\u0171"+
                "\u0170\3\2\2\2\u0172\u0173\3\2\2\2\u0173\u0171\3\2\2\2\u0173\u0174\3\2"+
                "\2\2\u0174\64\3\2\2\2\u0175\u0178\t\6\2\2\u0176\u0178\5\u00c9e\2\u0177"+
                "\u0175\3\2\2\2\u0177\u0176\3\2\2\2\u0178\u017d\3\2\2\2\u0179\u017c\t\7"+
                "\2\2\u017a\u017c\5\u00c9e\2\u017b\u0179\3\2\2\2\u017b\u017a\3\2\2\2\u017c"+
                "\u017f\3\2\2\2\u017d\u017b\3\2\2\2\u017d\u017e\3\2\2\2\u017e\66\3\2\2"+
                "\2\u017f\u017d\3\2\2\2\u0180\u0181\7)\2\2\u0181\u0183\t\b\2\2\u0182\u0184"+
                "\t\b\2\2\u0183\u0182\3\2\2\2\u0184\u0185\3\2\2\2\u0185\u0183\3\2\2\2\u0185"+
                "\u0186\3\2\2\2\u0186\u0187\3\2\2\2\u0187\u0188\t\t\2\2\u0188\u0189\3\2"+
                "\2\2\u0189\u018a\7\61\2\2\u018a\u018b\7\61\2\2\u018b\u019a\3\2\2\2\u018c"+
                "\u018e\t\b\2\2\u018d\u018c\3\2\2\2\u018e\u018f\3\2\2\2\u018f\u018d\3\2"+
                "\2\2\u018f\u0190\3\2\2\2\u0190\u0191\3\2\2\2\u0191\u0198\t\n\2\2\u0192"+
                "\u0194\t\b\2\2\u0193\u0192\3\2\2\2\u0194\u0195\3\2\2\2\u0195\u0193\3\2"+
                "\2\2\u0195\u0196\3\2\2\2\u0196\u0198\3\2\2\2\u0197\u018d\3\2\2\2\u0197"+
                "\u0193\3\2\2\2\u0198\u019b\3\2\2\2\u0199\u019b\5#\22\2\u019a\u0197\3\2"+
                "\2\2\u019a\u0199\3\2\2\2\u019b\u019c\3\2\2\2\u019c\u019d\t\t\2\2\u019d"+
                "\u019e\5\63\32\2\u019e\u019f\7)\2\2\u019f8\3\2\2\2\u01a0\u01af\7)\2\2"+
                "\u01a1\u01a3\t\b\2\2\u01a2\u01a1\3\2\2\2\u01a3\u01a4\3\2\2\2\u01a4\u01a2"+
                "\3\2\2\2\u01a4\u01a5\3\2\2\2\u01a5\u01a6\3\2\2\2\u01a6\u01ad\t\n\2\2\u01a7"+
                "\u01a9\t\b\2\2\u01a8\u01a7\3\2\2\2\u01a9\u01aa\3\2\2\2\u01aa\u01a8\3\2"+
                "\2\2\u01aa\u01ab\3\2\2\2\u01ab\u01ad\3\2\2\2\u01ac\u01a2\3\2\2\2\u01ac"+
                "\u01a8\3\2\2\2\u01ad\u01b0\3\2\2\2\u01ae\u01b0\5#\22\2\u01af\u01ac\3\2"+
                "\2\2\u01af\u01ae\3\2\2\2\u01b0\u01b1\3\2\2\2\u01b1\u01b2\t\t\2\2\u01b2"+
                "\u01b3\5\63\32\2\u01b3\u01b4\3\2\2\2\u01b4\u01b5\7)\2\2\u01b5:\3\2\2\2"+
                "\u01b6\u01b8\7P\2\2\u01b7\u01b6\3\2\2\2\u01b7\u01b8\3\2\2\2\u01b8\u01b9"+
                "\3\2\2\2\u01b9\u01bf\7)\2\2\u01ba\u01be\n\2\2\2\u01bb\u01bc\7)\2\2\u01bc"+
                "\u01be\7)\2\2\u01bd\u01ba\3\2\2\2\u01bd\u01bb\3\2\2\2\u01be\u01c1\3\2"+
                "\2\2\u01bf\u01bd\3\2\2\2\u01bf\u01c0\3\2\2\2\u01c0\u01c2\3\2\2\2\u01c1"+
                "\u01bf\3\2\2\2\u01c2\u01c3\7)\2\2\u01c3<\3\2\2\2\u01c4\u01c5\7\62\2\2"+
                "\u01c5\u01c9\7Z\2\2\u01c6\u01c8\5\u0091I\2\u01c7\u01c6\3\2\2\2\u01c8\u01cb"+
                "\3\2\2\2\u01c9\u01c7\3\2\2\2\u01c9\u01ca\3\2\2\2\u01ca>\3\2\2\2\u01cb"+
                "\u01c9\3\2\2\2\u01cc\u01cd\5\u008fH\2\u01cd@\3\2\2\2\u01ce\u01d1\5\63"+
                "\32\2\u01cf\u01d1\5\u008fH\2\u01d0\u01ce\3\2\2\2\u01d0\u01cf\3\2\2\2\u01d1"+
                "\u01d2\3\2\2\2\u01d2\u01d4\7G\2\2\u01d3\u01d5\t\13\2\2\u01d4\u01d3\3\2"+
                "\2\2\u01d4\u01d5\3\2\2\2\u01d5\u01d7\3\2\2\2\u01d6\u01d8\5\u0093J\2\u01d7"+
                "\u01d6\3\2\2\2\u01d8\u01d9\3\2\2\2\u01d9\u01d7\3\2\2\2\u01d9\u01da\3\2"+
                "\2\2\u01daB\3\2\2\2\u01db\u01dc\7?\2\2\u01dcD\3\2\2\2\u01dd\u01de\7@\2"+
                "\2\u01deF\3\2\2\2\u01df\u01e0\7>\2\2\u01e0H\3\2\2\2\u01e1\u01e2\7#\2\2"+
                "\u01e2J\3\2\2\2\u01e3\u01e4\7-\2\2\u01e4\u01e5\7?\2\2\u01e5L\3\2\2\2\u01e6"+
                "\u01e7\7/\2\2\u01e7\u01e8\7?\2\2\u01e8N\3\2\2\2\u01e9\u01ea\7,\2\2\u01ea"+
                "\u01eb\7?\2\2\u01ebP\3\2\2\2\u01ec\u01ed\7\61\2\2\u01ed\u01ee\7?\2\2\u01ee"+
                "R\3\2\2\2\u01ef\u01f0\7\'\2\2\u01f0\u01f1\7?\2\2\u01f1T\3\2\2\2\u01f2"+
                "\u01f3\7(\2\2\u01f3\u01f4\7?\2\2\u01f4V\3\2\2\2\u01f5\u01f6\7`\2\2\u01f6"+
                "\u01f7\7?\2\2\u01f7X\3\2\2\2\u01f8\u01f9\7~\2\2\u01f9\u01fa\7?\2\2\u01fa"+
                "Z\3\2\2\2\u01fb\u01fc\7~\2\2\u01fc\u01fd\7~\2\2\u01fd\\\3\2\2\2\u01fe"+
                "\u01ff\7\60\2\2\u01ff^\3\2\2\2\u0200\u0201\7a\2\2\u0201`\3\2\2\2\u0202"+
                "\u0203\7B\2\2\u0203b\3\2\2\2\u0204\u0205\7%\2\2\u0205d\3\2\2\2\u0206\u0207"+
                "\7&\2\2\u0207f\3\2\2\2\u0208\u0209\7*\2\2\u0209h\3\2\2\2\u020a\u020b\7"+
                "+\2\2\u020bj\3\2\2\2\u020c\u020d\7]\2\2\u020dl\3\2\2\2\u020e\u020f\7_"+
                "\2\2\u020fn\3\2\2\2\u0210\u0211\7}\2\2\u0211p\3\2\2\2\u0212\u0213\7\177"+
                "\2\2\u0213r\3\2\2\2\u0214\u0215\7.\2\2\u0215t\3\2\2\2\u0216\u0217\7=\2"+
                "\2\u0217v\3\2\2\2\u0218\u0219\7<\2\2\u0219x\3\2\2\2\u021a\u021b\7,\2\2"+
                "\u021bz\3\2\2\2\u021c\u021d\7\61\2\2\u021d|\3\2\2\2\u021e\u021f\7\'\2"+
                "\2\u021f~\3\2\2\2\u0220\u0221\7-\2\2\u0221\u0080\3\2\2\2\u0222\u0223\7"+
                "/\2\2\u0223\u0082\3\2\2\2\u0224\u0225\7\u0080\2\2\u0225\u0084\3\2\2\2"+
                "\u0226\u0227\7~\2\2\u0227\u0086\3\2\2\2\u0228\u0229\7(\2\2\u0229\u0088"+
                "\3\2\2\2\u022a\u022b\7`\2\2\u022b\u008a\3\2\2\2\u022c\u022d\7A\2\2\u022d"+
                "\u008c\3\2\2\2\u022e\u0230\t\f\2\2\u022f\u022e\3\2\2\2\u022f\u0230\3\2"+
                "\2\2\u0230\u0232\3\2\2\2\u0231\u0233\t\f\2\2\u0232\u0231\3\2\2\2\u0232"+
                "\u0233\3\2\2\2\u0233\u0234\3\2\2\2\u0234\u0235\t\f\2\2\u0235\u008e\3\2"+
                "\2\2\u0236\u0238\5\u0093J\2\u0237\u0236\3\2\2\2\u0238\u0239\3\2\2\2\u0239"+
                "\u0237\3\2\2\2\u0239\u023a\3\2\2\2\u023a\u023b\3\2\2\2\u023b\u023d\7\60"+
                "\2\2\u023c\u023e\5\u0093J\2\u023d\u023c\3\2\2\2\u023e\u023f\3\2\2\2\u023f"+
                "\u023d\3\2\2\2\u023f\u0240\3\2\2\2\u0240\u024f\3\2\2\2\u0241\u0243\5\u0093"+
                "J\2\u0242\u0241\3\2\2\2\u0243\u0244\3\2\2\2\u0244\u0242\3\2\2\2\u0244"+
                "\u0245\3\2\2\2\u0245\u0246\3\2\2\2\u0246\u0247\7\60\2\2\u0247\u024f\3"+
                "\2\2\2\u0248\u024a\7\60\2\2\u0249\u024b\5\u0093J\2\u024a\u0249\3\2\2\2"+
                "\u024b\u024c\3\2\2\2\u024c\u024a\3\2\2\2\u024c\u024d\3\2\2\2\u024d\u024f"+
                "\3\2\2\2\u024e\u0237\3\2\2\2\u024e\u0242\3\2\2\2\u024e\u0248\3\2\2\2\u024f"+
                "\u0090\3\2\2\2\u0250\u0251\t\r\2\2\u0251\u0092\3\2\2\2\u0252\u0253\t\f"+
                "\2\2\u0253\u0094\3\2\2\2\u0254\u0255\t\16\2\2\u0255\u0096\3\2\2\2\u0256"+
                "\u0257\t\17\2\2\u0257\u0098\3\2\2\2\u0258\u0259\t\20\2\2\u0259\u009a\3"+
                "\2\2\2\u025a\u025b\t\21\2\2\u025b\u009c\3\2\2\2\u025c\u025d\t\22\2\2\u025d"+
                "\u009e\3\2\2\2\u025e\u025f\t\23\2\2\u025f\u00a0\3\2\2\2\u0260\u0261\t"+
                "\24\2\2\u0261\u00a2\3\2\2\2\u0262\u0263\t\25\2\2\u0263\u00a4\3\2\2\2\u0264"+
                "\u0265\t\26\2\2\u0265\u00a6\3\2\2\2\u0266\u0267\t\27\2\2\u0267\u00a8\3"+
                "\2\2\2\u0268\u0269\t\30\2\2\u0269\u00aa\3\2\2\2\u026a\u026b\t\31\2\2\u026b"+
                "\u00ac\3\2\2\2\u026c\u026d\t\32\2\2\u026d\u00ae\3\2\2\2\u026e\u026f\t"+
                "\33\2\2\u026f\u00b0\3\2\2\2\u0270\u0271\t\34\2\2\u0271\u00b2\3\2\2\2\u0272"+
                "\u0273\t\35\2\2\u0273\u00b4\3\2\2\2\u0274\u0275\t\36\2\2\u0275\u00b6\3"+
                "\2\2\2\u0276\u0277\t\37\2\2\u0277\u00b8\3\2\2\2\u0278\u0279\t \2\2\u0279"+
                "\u00ba\3\2\2\2\u027a\u027b\t!\2\2\u027b\u00bc\3\2\2\2\u027c\u027d\t\""+
                "\2\2\u027d\u00be\3\2\2\2\u027e\u027f\t#\2\2\u027f\u00c0\3\2\2\2\u0280"+
                "\u0281\t$\2\2\u0281\u00c2\3\2\2\2\u0282\u0283\t%\2\2\u0283\u00c4\3\2\2"+
                "\2\u0284\u0285\t&\2\2\u0285\u00c6\3\2\2\2\u0286\u0287\t\'\2\2\u0287\u00c8"+
                "\3\2\2\2\u0288\u0289\t(\2\2\u0289\u00ca\3\2\2\2\'\2\u00f9\u0130\u013a"+
                "\u0147\u0150\u0152\u0160\u016c\u016e\u0173\u0177\u017b\u017d\u0185\u018f"+
                "\u0195\u0197\u019a\u01a4\u01aa\u01ac\u01af\u01b7\u01bd\u01bf\u01c9\u01d0"+
                "\u01d4\u01d9\u022f\u0232\u0239\u023f\u0244\u024c\u024e\3\b\2\2";
        public static final ATN _ATN =
                new ATNDeserializer().deserialize(_serializedATN.toCharArray());
        static {
                _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
                for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
                        _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
                }
        }
}