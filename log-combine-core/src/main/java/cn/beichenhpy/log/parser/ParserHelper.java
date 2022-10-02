package cn.beichenhpy.log.parser;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author beichenhpy
 * <p> 2022/10/2 16:03
 */
public class ParserHelper {

    protected static final char PERCENT_CHAR = '%';
    protected static final char LEFT_CURLY_CHAR = '{';
    protected static final char RIGHT_CURLY_CHAR = '}';
    protected static final int KEY_TYPE = 1;
    protected static final int LETTER_TYPE = 0;
    @Getter
    private final List<Pattern> patternList = new ArrayList<>();
    /**
     * 解析状态
     */
    private ParseState state = ParseState.LETTER_STATE;

    /**
     * 解析格式
     *
     * @param pattern 日志格式
     */
    public List<Pattern> parse(String pattern) {
        int pointer = 0;
        StringBuffer letterBuf = new StringBuffer();
        StringBuffer keyWordBuf = new StringBuffer();
        StringBuffer formatBuf = new StringBuffer();
        while (pointer < pattern.length()) {
            char c = pattern.charAt(pointer);
            switch (state) {
                case LETTER_STATE:
                    handleLetter(letterBuf, c);
                    break;
                case FORMAT_STATE:
                    handleFormat(keyWordBuf, formatBuf, c);
                    break;
                case KEY_WORD_STATE:
                    handleKeyWord(letterBuf, keyWordBuf, formatBuf, c);
                    break;
            }
            pointer++;
        }
        saveLetter(letterBuf);
        saveKeyWord(keyWordBuf, formatBuf);
        for (Pattern item : patternList) {
            if (item.getType() == KEY_TYPE) {
                item.setConverter(ParseUtil.KEYWORD_CONVERTER_CACHE.get(item.getText()));
            }
        }
        return patternList;
    }

    /**
     * 处理关键字类
     *
     * @param letterBuf  字符缓冲
     * @param keyWordBuf 关键字缓冲
     * @param formatBuf  格式化缓冲
     * @param c          当前字符
     */
    private void handleKeyWord(StringBuffer letterBuf, StringBuffer keyWordBuf, StringBuffer formatBuf, char c) {
        // %
        //保存letter
        saveLetter(letterBuf);
        switch (c) {
            case LEFT_CURLY_CHAR:
                state = ParseState.FORMAT_STATE;
                break;
            case PERCENT_CHAR:
                saveKeyWord(keyWordBuf, formatBuf);
                break;
            default:
                if (!Character.isJavaIdentifierStart(c)) {
                    state = ParseState.LETTER_STATE;
                    letterBuf.append(c);
                    saveKeyWord(keyWordBuf, formatBuf);
                } else {
                    keyWordBuf.append(c);
                }
                break;
        }
    }

    private void handleFormat(StringBuffer keyWordBuf, StringBuffer formatBuf, char c) {
        // {
        if (c == RIGHT_CURLY_CHAR) {
            saveKeyWord(keyWordBuf, formatBuf);
            state = ParseState.LETTER_STATE;
        } else {
            formatBuf.append(c);
        }
    }

    /**
     * 处理字符类数据
     *
     * @param letterBuf 字符缓冲
     * @param c         当前字符
     */
    private void handleLetter(StringBuffer letterBuf, char c) {
        switch (c) {
            case PERCENT_CHAR:
                state = ParseState.KEY_WORD_STATE;
                break;
            case LEFT_CURLY_CHAR:
                state = ParseState.FORMAT_STATE;
            default:
                letterBuf.append(c);
                break;
        }
    }

    /**
     * 保存关键字
     *
     * @param keyWordBuf 关键字缓冲
     * @param formatBuf  格式化缓冲
     */
    private void saveKeyWord(StringBuffer keyWordBuf, StringBuffer formatBuf) {
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
     * @param letterBuf letter类型缓冲
     */
    private void saveLetter(StringBuffer letterBuf) {
        if (letterBuf.length() > 0) {
            patternList.add(new Pattern(letterBuf.toString(), LETTER_TYPE, null, null));
            letterBuf.setLength(0);
        }
    }

    enum ParseState {
        LETTER_STATE, KEY_WORD_STATE, FORMAT_STATE
    }


}
