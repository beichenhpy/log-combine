package cn.beichenhpy.log.utils;

/**
 * @author beichenhpy
 * <p> 2022/5/18 21:26
 */
public class LogCombineUtil {

    public static final String DEFAULT_PATTERN = "DEFAULT";

    /**
     * 根据指定长度缩短全限定类名的包名部分
     * <p> 缩短包名,直到全限定类名到达指定的长度，
     * 如果已经缩短到最后的类还没有到达指定的长度则不再缩短
     *
     * @param origin 原数据
     * @param length 长度限定
     * @return 缩短后的全限定类名
     */
    public static String curtailReference(String origin, int length) {
        if (origin == null) {
            return null;
        }
        int originLength = origin.length();
        if (originLength <= length) {
            return origin;
        }
        String[] originItems = origin.split("\\.");
        String[] curtailItems = new String[originItems.length];
        System.arraycopy(originItems, 0, curtailItems, 0, originItems.length);
        for (int i = 0; i < originItems.length - 1; i++) {
            curtailItems[i] = originItems[i].substring(0, 1);
            int currentLength = originLength - (originItems[i].length() - 1);
            if (currentLength <= length) {
                return String.join(".", curtailItems);
            }
        }
        return String.join(".", curtailItems);
    }

    public static void main(String[] args) {
        String origin = "cn.beichenhpy.log.LogCombineHelper";
        String s = curtailReference(origin, 20);
        System.out.println(s);
    }

}
