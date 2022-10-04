package cn.beichenhpy.log.parser;

/**
 * @author beichenhpy
 * <p> 2022/10/2 18:36
 */
public class LoggerConverter implements Converter {

    private static final String DOT = ".";
    private static final String DOT_REGEX = "\\.";

    /**
     * 根据指定长度缩短全限定类名的包名部分
     * <p> 缩短包名,直到全限定类名到达指定的长度，
     * 如果已经缩短到最后的类还没有到达指定的长度则不再缩短
     *
     * @param origin 原数据
     * @param length 长度限定
     * @return 缩短后的全限定类名
     */
    private static String curtailReference(String origin, int length) {
        if (origin == null) {
            return null;
        }
        int originLength = origin.length();
        if (originLength <= length || length <= 0) {
            return origin;
        }
        String[] originItems = origin.split(DOT_REGEX);
        String[] curtailItems = new String[originItems.length];
        System.arraycopy(originItems, 0, curtailItems, 0, originItems.length);
        for (int i = 0; i < originItems.length - 1; i++) {
            curtailItems[i] = originItems[i].substring(0, 1);
            int currentLength = originLength - (originItems[i].length() - 1);
            if (currentLength <= length) {
                return String.join(DOT, curtailItems);
            }
        }
        return String.join(DOT, curtailItems);
    }

    @Override
    public String convert(String value, String option) {
        int loggerLength = ParseUtil.getLoggerLength(option);
        return curtailReference(Thread.currentThread().getStackTrace()[5].getClassName(), loggerLength);
    }
}
