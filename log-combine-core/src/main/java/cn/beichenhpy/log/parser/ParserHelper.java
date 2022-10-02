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
    protected static final int LITERAL_TYPE = 0;
    @Getter
    private final List<Pattern> patternList = new ArrayList<>();
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
        while (pointer < pattern.length()) {
            char c = pattern.charAt(pointer);
            switch (state) {
                case LITERAL_STATE:
                    handleLiteral(keyWordBuf, formatBuf, literalBuf, c);
                    break;
                case FORMAT_STATE:
                    handleFormat(keyWordBuf, formatBuf, c);
                    break;
                case KEY_WORD_STATE:
                    handleKeyWord(literalBuf, keyWordBuf, formatBuf, c);
                    break;
            }
            pointer++;
        }
        saveLiteral(literalBuf);
        saveKeyWord(keyWordBuf, formatBuf);
        for (Pattern item : patternList) {
            if (item.getType() == KEY_TYPE) {
                Converter converter = ParseUtil.KEYWORD_CONVERTER_CACHE.get(item.getText());
                if (converter == null) {
                    throw new IllegalArgumentException("[LOG-COMBINE]: 您输入的关键字[" + item.getText() + "]不存在, 请确认是否输入正确。");
                }
                item.setConverter(converter);
            }
        }
        return patternList;
    }

    /**
     * 处理关键字类
     *
     * @param literalBuf 字符缓冲
     * @param keyWordBuf 关键字缓冲
     * @param formatBuf  格式化缓冲
     * @param c          当前字符
     */
    private void handleKeyWord(StringBuffer literalBuf, StringBuffer keyWordBuf, StringBuffer formatBuf, char c) {
        // %
        //保存文字
        saveLiteral(literalBuf);
        switch (c) {
            case LEFT_CURLY_CHAR:
                state = ParseState.FORMAT_STATE;
                break;
            case PERCENT_CHAR:
                saveKeyWord(keyWordBuf, formatBuf);
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

    private void handleFormat(StringBuffer keyWordBuf, StringBuffer formatBuf, char c) {
        // {
        if (c == RIGHT_CURLY_CHAR) {
            saveKeyWord(keyWordBuf, formatBuf);
            state = ParseState.LITERAL_STATE;
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
    private void handleLiteral(StringBuffer keyWordBuf, StringBuffer formatBuf, StringBuffer letterBuf, char c) {
        if (c == PERCENT_CHAR) {
            saveKeyWord(keyWordBuf, formatBuf);
            state = ParseState.KEY_WORD_STATE;
        } else {
            letterBuf.append(c);
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
     * @param literalBuf letter类型缓冲
     */
    private void saveLiteral(StringBuffer literalBuf) {
        if (literalBuf.length() > 0) {
            patternList.add(new Pattern(literalBuf.toString(), LITERAL_TYPE, null, null));
            literalBuf.setLength(0);
        }
    }

    enum ParseState {
        LITERAL_STATE, KEY_WORD_STATE, FORMAT_STATE
    }


}
