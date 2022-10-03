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
        StringBuffer keyWordBuf = new StringBuffer();
        StringBuffer formatBuf = new StringBuffer();
        List<Pattern> patternList = new ArrayList<>();
        while (pointer < pattern.length()) {
            char c = pattern.charAt(pointer);
            switch (state) {
                case LITERAL_STATE:
                    handleLiteral(literalBuf, keyWordBuf, formatBuf, c, patternList);
                    break;
                case KEY_WORD_STATE:
                    handleKeyWord(literalBuf, keyWordBuf, formatBuf, c, patternList);
                    break;
                case FORMAT_STATE:
                    handleFormat(keyWordBuf, formatBuf, c, patternList);
                    break;
            }
            pointer++;
        }
        addLiteralValue(literalBuf, patternList);
        addKeywordValue(keyWordBuf, formatBuf, patternList);
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
     * @param keyWordBuf  关键字缓冲
     * @param formatBuf   格式化缓冲
     * @param c           当前字符
     * @param patternList 解析后的格式集合
     */
    private void handleKeyWord(StringBuffer literalBuf, StringBuffer keyWordBuf, StringBuffer formatBuf, char c, List<Pattern> patternList) {
        //保存文字
        addLiteralValue(literalBuf, patternList);
        switch (c) {
            case LEFT_CURLY_CHAR:
                state = ParseState.FORMAT_STATE;
                break;
            case PERCENT_CHAR:
                addKeywordValue(keyWordBuf, formatBuf, patternList);
                break;
            default:
                if (!Character.isJavaIdentifierPart(c)) {
                    state = ParseState.LITERAL_STATE;
                    literalBuf.append(c);
                } else {
                    keyWordBuf.append(c);
                }
                break;
        }
    }

    /**
     * 处理格式化数据
     *
     * @param keyWordBuf  关键字缓冲
     * @param formatBuf   格式化缓冲
     * @param c           当前字符
     * @param patternList 解析后的格式集合
     */
    private void handleFormat(StringBuffer keyWordBuf, StringBuffer formatBuf, char c, List<Pattern> patternList) {
        if (c == RIGHT_CURLY_CHAR) {
            addKeywordValue(keyWordBuf, formatBuf, patternList);
            state = ParseState.LITERAL_STATE;
        } else {
            formatBuf.append(c);
        }
    }

    /**
     * 处理文字类数据
     *
     * @param literalBuf  文字缓冲
     * @param keyWordBuf  关键字缓冲
     * @param formatBuf   格式化缓冲
     * @param c           当前字符
     * @param patternList 解析格式集合
     */
    private void handleLiteral(StringBuffer literalBuf, StringBuffer keyWordBuf, StringBuffer formatBuf, char c, List<Pattern> patternList) {
        if (c == PERCENT_CHAR) {
            addKeywordValue(keyWordBuf, formatBuf, patternList);
            state = ParseState.KEY_WORD_STATE;
        } else {
            literalBuf.append(c);
        }
    }

    /**
     * 保存关键字
     *
     * @param keyWordBuf  关键字缓冲
     * @param formatBuf   格式化缓冲
     * @param patternList 解析格式集合
     */
    private void addKeywordValue(StringBuffer keyWordBuf, StringBuffer formatBuf, List<Pattern> patternList) {
        if (keyWordBuf.length() > 0) {
            String format = null;
            if (formatBuf.length() > 0) {
                format = formatBuf.toString();
                formatBuf.setLength(0);
            }
            patternList.add(new Pattern(keyWordBuf.toString(), KEY_TYPE, format, null));
            keyWordBuf.setLength(0);
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
        LITERAL_STATE, KEY_WORD_STATE, FORMAT_STATE
    }


}
