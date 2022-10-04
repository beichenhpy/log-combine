package cn.beichenhpy.log.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * 不要设置为单例模式，该类是多例的
 *
 * @author beichenhpy
 * <p> 2022/10/2 16:03
 */
public class ParserHelper {

    protected static final char PERCENT_CHAR = '%';
    protected static final char LEFT_CURLY_CHAR = '{';
    protected static final char RIGHT_CURLY_CHAR = '}';
    protected static final int KEY_TYPE = 1;
    protected static final int LITERAL_TYPE = 0;
    /**
     * 解析状态
     */
    private ParseState state = ParseState.LITERAL_STATE;

    /**
     * 解析格式
     *
     * @param pattern 日志格式
     */
    public List<Pattern> parse(String pattern) {
        int pointer = 0;
        StringBuffer literalBuf = new StringBuffer();
        StringBuffer keywordBuf = new StringBuffer();
        StringBuffer optionBuf = new StringBuffer();
        List<Pattern> patternList = new ArrayList<>();
        while (pointer < pattern.length()) {
            char c = pattern.charAt(pointer);
            switch (state) {
                case LITERAL_STATE:
                    handleLiteralState(literalBuf, keywordBuf, optionBuf, c, patternList);
                    break;
                case KEY_WORD_STATE:
                    handleKeywordState(literalBuf, keywordBuf, optionBuf, c, patternList);
                    break;
                case OPTION_STATE:
                    handleOptionState(keywordBuf, optionBuf, c, patternList);
                    break;
            }
            pointer++;
        }
        addLiteralValue(literalBuf, patternList);
        addKeywordValue(keywordBuf, optionBuf, patternList);
        for (Pattern item : patternList) {
            if (item.getType() == KEY_TYPE) {
                Converter converter = ConvertUtil.getConverter(item.getText());
                item.setConverter(converter);
            }
        }
        return patternList;
    }

    /**
     * 处理关键字类
     *
     * @param literalBuf  字符缓冲
     * @param keywordBuf  关键字缓冲
     * @param optionBuf   格式化缓冲
     * @param c           当前字符
     * @param patternList 解析后的格式集合
     */
    private void handleKeywordState(StringBuffer literalBuf, StringBuffer keywordBuf, StringBuffer optionBuf, char c, List<Pattern> patternList) {
        //保存文字
        addLiteralValue(literalBuf, patternList);
        switch (c) {
            case LEFT_CURLY_CHAR:
                state = ParseState.OPTION_STATE;
                break;
            case PERCENT_CHAR:
                addKeywordValue(keywordBuf, optionBuf, patternList);
                break;
            default:
                if (Character.isJavaIdentifierPart(c)) {
                    keywordBuf.append(c);
                } else {
                    state = ParseState.LITERAL_STATE;
                    literalBuf.append(c);
                }
                break;
        }
    }

    /**
     * 处理格式化数据
     *
     * @param keywordBuf  关键字缓冲
     * @param optionBuf   格式化缓冲
     * @param c           当前字符
     * @param patternList 解析后的格式集合
     */
    private void handleOptionState(StringBuffer keywordBuf, StringBuffer optionBuf, char c, List<Pattern> patternList) {
        if (c == RIGHT_CURLY_CHAR) {
            addKeywordValue(keywordBuf, optionBuf, patternList);
            state = ParseState.LITERAL_STATE;
        } else {
            optionBuf.append(c);
        }
    }

    /**
     * 处理文字类数据
     *
     * @param literalBuf  文字缓冲
     * @param keywordBuf  关键字缓冲
     * @param optionBuf   格式化缓冲
     * @param c           当前字符
     * @param patternList 解析格式集合
     */
    private void handleLiteralState(StringBuffer literalBuf, StringBuffer keywordBuf, StringBuffer optionBuf, char c, List<Pattern> patternList) {
        if (c == PERCENT_CHAR) {
            addKeywordValue(keywordBuf, optionBuf, patternList);
            state = ParseState.KEY_WORD_STATE;
        } else {
            literalBuf.append(c);
        }
    }

    /**
     * 保存关键字
     *
     * @param keywordBuf  关键字缓冲
     * @param optionBuf   格式化缓冲
     * @param patternList 解析格式集合
     */
    private void addKeywordValue(StringBuffer keywordBuf, StringBuffer optionBuf, List<Pattern> patternList) {
        if (keywordBuf.length() > 0) {
            String option = null;
            if (optionBuf.length() > 0) {
                option = optionBuf.toString();
                optionBuf.setLength(0);
            }
            patternList.add(new Pattern(keywordBuf.toString(), KEY_TYPE, option, null));
            keywordBuf.setLength(0);
        }
    }

    /**
     * 保存letter类型的数据
     *
     * @param literalBuf  letter类型缓冲
     * @param patternList 解析格式集合
     */
    private void addLiteralValue(StringBuffer literalBuf, List<Pattern> patternList) {
        if (literalBuf.length() > 0) {
            patternList.add(new Pattern(literalBuf.toString(), LITERAL_TYPE, null, null));
            literalBuf.setLength(0);
        }
    }

    enum ParseState {
        LITERAL_STATE, KEY_WORD_STATE, OPTION_STATE
    }


}
