package youdian.apk.ipqc.utils;

/**
 * Json格式化输出工具
 * 作者 create by H7111906 on 2020/4/8
 */
public class JsonFormatUtils {

    private JsonFormatUtils(){}

    public static String format(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) {
            return "";
        }
        StringBuilder mBuilder = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '{':
                case '[':
                    mBuilder.append(current);
                    mBuilder.append('\n');
                    indent++;
                    addIndentBlank(mBuilder, indent);
                    break;
                case '}':
                case ']':
                    mBuilder.append('\n');
                    indent--;
                    addIndentBlank(mBuilder, indent);
                    mBuilder.append(current);
                    break;
                case ',':
                    mBuilder.append(current);
                    if (last != '\\') {
                        mBuilder.append('\n');
                        addIndentBlank(mBuilder, indent);
                    }
                    break;
                default:
                    mBuilder.append(current);
            }
        }
        return mBuilder.toString();
    }

    private static void addIndentBlank(StringBuilder builder, int indent) {
        for (int i = 0; i < indent; i++) {
            builder.append('\t');
        }
    }
}
